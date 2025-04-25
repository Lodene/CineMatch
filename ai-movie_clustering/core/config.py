from os import environ, getcwd, makedirs
from os.path import join

# Hyperparameters
VECTOR_SIZE = int(environ["VECTOR_SIZE"])
WINDOW = int(environ["WINDOW"])
MIN_COUNT = int(environ["MIN_COUNT"])
NB_EPOCHS = int(environ["NB_EPOCHS"])
DM = int(environ["DM"])


# Training config
MODEL_VERSION = environ["MODEL_VERSION"]
KEEP_STOPWORDS = environ["KEEP_STOPWORDS"] == "1"


# OMDB API Keys
OMDB_API_KEYS = [
    key.strip() for key in environ["OMDB_API_KEYS"].split(",") if key.strip()
]


# Folder paths
DATA_FOLDER = join(getcwd(), "core", "data")
MODELS_FOLDER = join(getcwd(), "core", "models")
OUTPUT_FOLDER = join(getcwd(), "core", "output")
LIQUIBASE_FOLDER = join(OUTPUT_FOLDER, "liquibase_xml")

makedirs(DATA_FOLDER, exist_ok=True)
makedirs(MODELS_FOLDER, exist_ok=True)
makedirs(OUTPUT_FOLDER, exist_ok=True)
makedirs(LIQUIBASE_FOLDER, exist_ok=True)


# File paths
CSV_PATH = join(DATA_FOLDER, "TMDB_all_movies.csv")
CSV_PATH_ENRICHED = join(DATA_FOLDER, "films_enriched_full.csv")
OMDB_CSV_PATH = join(DATA_FOLDER, "films_enriched_omdb.csv")
WIKI_CSV_PATH = join(DATA_FOLDER, "films_enriched_wiki.csv")

MODEL_PATH = join(
    MODELS_FOLDER,
    f"d2v_mc{MODEL_VERSION}_"
    + f"v{VECTOR_SIZE}_w{WINDOW}_c{MIN_COUNT}_e{NB_EPOCHS}_dm{DM}_"
    + f"s{'1' if KEEP_STOPWORDS else '0'}.model",
)


# Constants
ENRICHMENT_DONE = "ok"
ENRICHMENT_SKIP = "skip"
ENRICHMENT_FAIL = "fail"
