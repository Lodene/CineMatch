import re
from typing import List

import nltk
import pandas as pd
from config import KEEP_STOPWORDS, OMDB_CSV_PATH, WIKI_CSV_PATH
from gensim.utils import simple_preprocess
from nltk.corpus import stopwords
from utils.decorators import to_time

nltk.download("stopwords")
stop_words = set(stopwords.words("english"))


def clean_text(text: str) -> str:
    """
    Clean a string by:
    - Removing HTML elements
    - Keeping only letters
    - Transforming to lowercase

    Args:
        text (str): String to clean.

    Returns:
        str: Cleaned string.
    """
    text = str(text)
    text = re.sub(r"<[^>]+>", " ", text)
    text = re.sub(r"[^a-zA-Z ]", " ", text)
    text = text.lower()
    return text


def tokenize_str_list(value: str) -> List[str]:
    """
    Convert a coma-separated string into a list of lowercase tokens.

    Args:
        value (str): String to tokenize.

    Returns:
        List[str]: Tokenized string.
    """
    token_data = str(value).lower().replace(" ", "_")
    return [tkn.strip() for tkn in token_data.split(",")]


def build_enriched_row(row: pd.Series) -> List[str]:
    """
    Build a token list from a DataFrame row.

    Args:
        row (pd.Series): Row to tokenize.

    Returns:
        List[str]: Tokenized row.
    """
    tokens = []

    # Add overview and wiki overview if available
    if row.get("overview"):
        tokens += simple_preprocess(clean_text(row["overview"]), deacc=True)
    if row.get("overview_wiki"):
        tokens += simple_preprocess(clean_text(row["overview_wiki"]), deacc=True)

    # Add genre tokens
    if row.get("genres") and isinstance(row["genres"], str):
        tokens += tokenize_str_list(row["genres"])

    # Add cast tokens
    if row.get("cast") and isinstance(row["cast"], str):
        tokens += tokenize_str_list(row["cast"])[:5]

    # Add director tokens
    if row.get("director") and isinstance(row["director"], str):
        tokens += tokenize_str_list(row["director"])

    # Filter stopwords
    if KEEP_STOPWORDS:
        return tokens
    else:
        return [t for t in tokens if t not in stop_words]
