from os.path import exists

import pandas as pd
from config import CSV_PATH_ENRICHED, ENRICHMENT_DONE, OMDB_CSV_PATH, WIKI_CSV_PATH
from utils.decorators import to_time


@to_time("Dataset loading + cleaning")
def load_clean_dataset(csv_path: str) -> pd.DataFrame:
    """
    Clean and load the original dataset.

    Args:
        csv_path (str): Dataset path.

    Returns:
        pd.DataFrame: Cleaned dataset.
    """
    print(f"Loading '{csv_path}'...")
    df = pd.read_csv(csv_path)
    print(f"> Dataset length: {len(df)}")
    df = df.dropna(subset=["overview"])
    print(f"> Dataset length (dropna overview): {len(df)}")
    df = df.drop_duplicates(subset=["id"])
    print(f"> Dataset length (drop_duplicates): {len(df)}")
    df = df[df["vote_average"] > 0].copy()
    print(f"> Dataset length (vote_average > 0): {len(df)}")
    df = df[df["vote_count"] > 20].copy()
    print(f"> Dataset length (vote_count > 20): {len(df)}")
    return df


@to_time("Utils data loading")
def load_enriched_dataset() -> pd.DataFrame:
    """
    Merge the two enriched datasets (OMDB + Wikimedia).

    Returns:
        pd.DataFrame: Final enriched dataset.
    """
    print("Loading enriched dataset...")
    if exists(CSV_PATH_ENRICHED):
        df = pd.read_csv(CSV_PATH_ENRICHED)
    else:
        df_omdb = pd.read_csv(OMDB_CSV_PATH)
        df_wiki = pd.read_csv(WIKI_CSV_PATH)

        # Merging datasets
        df_result = pd.merge(
            df_omdb,
            df_wiki[["id", "overview_wiki", "wiki_status"]],
            on="id",
            how="left",
        )
        df = df_result[
            (df_result["wiki_status"] == ENRICHMENT_DONE)
            & (df_result["omdb_status"] == ENRICHMENT_DONE)
        ]

        # Cleaning
        df = df.drop_duplicates(subset=["id"])
        df = df.reset_index(drop=True)

    print(f"> Dataset length (enriched OMDB + Wiki): {len(df)}")
    return df
