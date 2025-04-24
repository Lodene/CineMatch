from os.path import exists
from time import sleep

import pandas as pd
import requests
from config import (
    CSV_PATH,
    ENRICHMENT_DONE,
    ENRICHMENT_FAIL,
    OMDB_API_KEYS,
    OMDB_CSV_PATH,
)
from utils.utils_data import load_clean_dataset

if not OMDB_API_KEYS:
    raise ValueError("Error, you must provide at least one OMDB API key.")

DELAY_BETWEEN_CALLS = 0.5


def main():
    # Data loading
    print("Loading dataset...")
    if exists(OMDB_CSV_PATH):
        print(f"[INFO] Existing data at '{OMDB_CSV_PATH}'.")
        df = pd.read_csv(OMDB_CSV_PATH, low_memory=False)
        print(df["omdb_status"].value_counts(dropna=False))
    else:
        df = load_clean_dataset(CSV_PATH)
        df["omdb_status"] = ""

    # Filter for elements not done
    df_filtered = df[df["omdb_status"].isna()].reset_index(drop=True)
    print(f"[INFO] Movies to enrich: {len(df_filtered)}.")

    # Data enrichment
    key_index = 0
    for i, (idx, row) in enumerate(df_filtered.iterrows()):
        # Get IMDB ID
        imdb_id = row.get("imdb_id", None)
        if not isinstance(imdb_id, str) or imdb_id.strip() == "":
            df.loc[df["imdb_id"] == imdb_id, "omdb_status"] = ENRICHMENT_FAIL
            continue

        success = True
        while key_index < len(OMDB_API_KEYS):
            api_key = OMDB_API_KEYS[key_index]

            try:
                # Request data
                url = f"http://www.omdbapi.com/?i={imdb_id}&apikey={api_key}&plot=full"
                response = requests.get(url)
                response.raise_for_status()
                data = response.json()

                # Request successful
                if data.get("Response") == "True":
                    long_plot = data.get("Plot", "")
                    original = str(row["overview"])

                    # If data is new compared to the original overview, add it
                    if long_plot and long_plot != "N/A" and long_plot not in original:
                        enriched = f"{original.strip()} {long_plot.strip()}"
                        df.loc[df["imdb_id"] == imdb_id, "overview"] = enriched
                        df.loc[df["imdb_id"] == imdb_id, "omdb_status"] = (
                            ENRICHMENT_DONE
                        )
                        print(f"[OK] {row['title']} (IMDB: {imdb_id}).")
                    # Else, skip it
                    else:
                        df.loc[df["imdb_id"] == imdb_id, "omdb_status"] = "skip"
                        print(f"[SKIP] {row['title']} (IMDB: {imdb_id}).")

                # Request failed
                else:
                    print(
                        f"[FAIL] Error on {row['title']} (IMDB: {imdb_id}) ",
                        f"-> {data.get('Error', 'Unknown error')}.",
                    )
                break

            # API key rate limit
            except Exception as e:
                print(f"[ERROR] {row['title']} (on key {api_key}) -> {e}.")
                # Skip to next key
                key_index += 1
                success = False

            sleep(DELAY_BETWEEN_CALLS)

        # If out of loop without success, stop program
        if not success:
            print(
                f"[END ON] {row['title']} (IMDB: {imdb_id}) ",
                "-> No more keys available.",
            )
            break

        # Save every 10 requests
        if i % 10 == 0:
            df.to_csv(OMDB_CSV_PATH, index=False)
            print(f"[SAVE] {i} elements done.")

    # Last save
    df.to_csv(OMDB_CSV_PATH, index=False)
    print(f"[SAVE] Full save to '{OMDB_CSV_PATH}'.")


if __name__ == "__main__":
    main()
