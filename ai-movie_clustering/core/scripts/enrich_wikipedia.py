from os.path import exists
from time import sleep

import pandas as pd
from config import (
    CSV_PATH,
    ENRICHMENT_FAIL,
    WIKI_CSV_PATH,
)
from utils.utils_data import load_clean_dataset
from utils.utils_enrich import get_wiki_summary_from_url, get_wikipedia_url_from_imdb

DELAY_BETWEEN_CALLS = 0.7


def main():
    # Data loading
    print("Loading dataset...")
    if exists(WIKI_CSV_PATH):
        print(f"[INFO] Existing data at '{WIKI_CSV_PATH}'.")
        df = pd.read_csv(WIKI_CSV_PATH, low_memory=False)
        print(df["wiki_status"].value_counts(dropna=False))
    else:
        df = load_clean_dataset(CSV_PATH)
        df["wiki_status"] = ""
        df["overview_wiki"] = ""

    # Filter for elements not done
    df_filtered = df[df["wiki_status"].isna()].reset_index(drop=True)
    print(f"[INFO] Movies to enrich: {len(df_filtered)}.")

    # Data enrichment
    for i, (idx, row) in enumerate(df_filtered.iterrows()):
        # Get IMDB ID
        imdb_id = str(row.get("imdb_id", "")).strip()
        if not isinstance(imdb_id, str) or imdb_id.strip() == "":
            df.loc[df["imdb_id"] == imdb_id, "wiki_status"] = ENRICHMENT_FAIL
            continue

        # Get data
        wiki_url = get_wikipedia_url_from_imdb(imdb_id)
        summary, status = get_wiki_summary_from_url(wiki_url)

        if summary:
            df.loc[df["imdb_id"] == imdb_id, "overview_wiki"] = summary
        df.loc[df["imdb_id"] == imdb_id, "wiki_status"] = status
        print(f"[{status.upper()}] {row['title']} (IMDB: {imdb_id}).")

        # Save every 10 requests
        if i % 10 == 0:
            df.to_csv(WIKI_CSV_PATH, index=False)
            print(f"[SAVE] {i} elements done.")

        sleep(DELAY_BETWEEN_CALLS)

    # Last save
    df.to_csv(WIKI_CSV_PATH, index=False)
    print(f"[SAVE] Full save to '{WIKI_CSV_PATH}'.")


if __name__ == "__main__":
    main()
