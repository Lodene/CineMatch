import pandas as pd
from config import CSV_PATH, CSV_PATH_ENRICHED

# Load datasets
df_full = pd.read_csv(CSV_PATH)
print(len(df_full))
df_enriched = pd.read_csv(CSV_PATH_ENRICHED)
print(len(df_enriched))

# Truncate full dataset
df_filtre = df_full[df_full["id"].isin(df_enriched["id"])]
print(len(df_filtre))
df_filtre.to_csv("subset.csv", index=False)
