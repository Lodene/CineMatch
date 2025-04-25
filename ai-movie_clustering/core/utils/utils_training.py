import multiprocessing
from typing import List

import pandas as pd
from config import (
    DM,
    MIN_COUNT,
    NB_EPOCHS,
    VECTOR_SIZE,
    WINDOW,
)
from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from tqdm import tqdm
from utils.decorators import to_time
from utils.utils_text import build_enriched_row


@to_time("Tagged document creation")
def create_tagged_documents(df: pd.DataFrame) -> List[TaggedDocument]:
    """
    Create TaggedDocuments from the dataset.

    Args:
        df (pd.DataFrame): Dataset.

    Returns:
        List[TaggedDocument]: TaggedDocuments generated.
    """
    print("> Creating TaggedDocuments...")
    documents = []
    for _, row in tqdm(df.iterrows(), total=len(df)):
        tokens = build_enriched_row(row)
        if tokens:
            documents.append(TaggedDocument(words=tokens, tags=[str(row["id"])]))
    return documents


@to_time("Model training")
def train_doc2vec_model(documents: List[TaggedDocument]) -> Doc2Vec:
    """
    Train the Doc2Vec model.

    Args:
        documents (List[TaggedDocument]): TaggedDocuments to use.

    Returns:
        Doc2Vec: Trained model.
    """
    print("> Training Doc2Vec model...")
    model = Doc2Vec(
        vector_size=VECTOR_SIZE,
        window=WINDOW,
        min_count=MIN_COUNT,
        epochs=NB_EPOCHS,
        dm=DM,
        workers=multiprocessing.cpu_count(),
    )
    model.build_vocab(documents)
    model.train(documents, total_examples=model.corpus_count, epochs=model.epochs)
    return model
