from typing import List, Tuple

import pandas as pd
from config import CSV_PATH, CSV_PATH_ENRICHED, MODEL_PATH
from gensim.models.doc2vec import Doc2Vec
from utils.decorators import to_time
from utils.utils_text import build_enriched_row


@to_time("Inference data loading")
def load_inference_data() -> Tuple[pd.DataFrame, pd.DataFrame, Doc2Vec]:
    """
    Load datasets and model used for inference.

    Returns:
        Tuple[pd.DataFrame, pd.DataFrame, Doc2Vec]:
            Tuple (full dataset, training dataset, trained model).
    """
    # Data loading and cleaning
    print("Loading datasets...")
    df_full = pd.read_csv(CSV_PATH)
    df_full["id"] = df_full["id"].astype(str)
    df_enriched = pd.read_csv(CSV_PATH_ENRICHED)
    df_enriched["id"] = df_enriched["id"].astype(str)

    # Model loading
    print("Loading model...")
    model: Doc2Vec = Doc2Vec.load(MODEL_PATH)  # type: ignore
    print(f"> Used model: '{MODEL_PATH}'")

    return df_full, df_enriched, model


def get_random_movie_id(
    df_full: pd.DataFrame,
    df_enriched: pd.DataFrame,
    in_model: bool = True,
) -> str:
    """
    Get random movie ID from the dataset.

    Args:
        df_full (pd.DataFrame): Full dataset.
        df_enriched (pd.DataFrame): Enriched dataset (used to train the model).
        in_model (bool, optional): If True, get random ID from data used to train the model.
            Else, get ID unused in model training (in df_full - df_enriched).
            Defaults to True.

    Returns:
        str: Random movie ID.
    """
    all_ids = set(df_full["id"].astype(str))
    enriched_ids = set(df_enriched["id"].astype(str))

    if in_model:
        # Get random from elements in the model
        candidates = df_enriched[df_enriched["id"].astype(str).isin(enriched_ids)]
    else:
        # Get random from elements not in the model
        unknown_ids = all_ids - enriched_ids
        candidates = df_full[df_full["id"].astype(str).isin(unknown_ids)]

    return candidates.sample(1).iloc[0]["id"]


@to_time("Movie recommendation")
def recommend_movies(
    df_full: pd.DataFrame,
    model: Doc2Vec,
    movie_ids: List[str],
    topn: int = 10,
    infer_epochs: int = 20,
) -> List[str]:
    """
    Get recommended movie IDs based on one or more input movies.

    Args:
        df_full (pd.DataFrame): Full dataset, to get movies info for inference.
        model (Doc2Vec): Trained model.
        movie_ids (List[str]): Input movie IDs.
        topn (int, optional): Number of movies to return.
            Defaults to 10.
        infer_epochs (int, optional): Number of epochs for inferred vectors.
            Defaults to 20.

    Returns:
        List[str]: Recommended movie IDs.
    """
    movie_ids = [str(fid) for fid in movie_ids]

    vectors = []
    known_ids = set(model.dv.index_to_key)

    for movie_id in movie_ids:
        # Movie was used in the model training
        if movie_id in known_ids:
            vectors.append(model.dv[movie_id])
        # Movie is not known, infer vector on the go
        else:
            row = df_full[df_full["id"] == movie_id]
            if row.empty:
                print(f"[ERROR] {movie_id} not found in dataset.")
                continue
            tokens = build_enriched_row(row.iloc[0])
            vector = model.infer_vector(tokens, epochs=infer_epochs)
            vectors.append(vector)

    if not vectors:
        print("[ERROR] No vectors for recommendation.")
        return []

    # Average if multiple vectors
    avg_vector = sum(vectors) / len(vectors)
    # Get extra in case original movies are recommended
    similar = model.dv.most_similar([avg_vector], topn=topn + len(movie_ids))
    # Remove original movies from recommendations
    similar = [rec_id for (rec_id, score) in similar if rec_id not in movie_ids]
    return similar
