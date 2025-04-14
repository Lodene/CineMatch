import csv
import os
import html
import re
from lxml import etree as ET
import datetime

# === CONFIGURATION ===
CSV_PATH = "TMDB_all_movies.csv"
OUTPUT_DIR = "liquibase_chunks"
CHUNK_SIZE = 500
MAIN_TABLE = "movie"
CHANGESET_AUTHOR = "T-H-O-M-A-S"

LIST_FIELDS = [
    "cast", "director", "director_of_photography", "genres",
    "music_composer", "producers", "production_companies", "production_countries",
    "spoken_languages", "writers"
]

DISCARDED_FIELDS = [
    "tagline",
    "poster_path"
]

# If "" value => return "0.0"
NULLABLE_FLOAT_FIELD = [
    "imdb_rating",
    "imdb_votes",
    "vote_average",
    "vote_count",
    "revenue",
    "runtime",
    "budget",
]
# If "" value => return new timestamp
DATE_FIELD = [
    "release_date"
]

NULLABLE_STRING = [
    "overview",
    "imdb_id"
]


COLUMN_NAME = {
    "cast": "movie_entity_cast",
    "director": "movie_entity_director",
    "director_of_photography": "movie_entity_director_of_photography",
    "music_composer": "movie_entity_music_composer",
    "producers": "movie_entity_producers",
    "production_companies": "movie_entity_production_companies",
    "production_countries": "movie_entity_production_countries",
    "spoken_languages": "movie_entity_spoken_languages",
    "writers": "movie_entity_writers",
    "genres": "movie_entity_genres"
}

# === HELPERS ===

def fix_encoding(text):
    if not text:
        return ""
    try:
        return text.encode("windows-1252").decode("utf-8")
    except:
        try:
            return text.encode("latin1").decode("utf-8")
        except:
            return text

def recursive_unescape(value):
    prev = None
    while value != prev:
        prev = value
        value = html.unescape(value)
    return value.strip()

def clean_value(value):
    if not value:
        return ""
    value = fix_encoding(value)
    value = recursive_unescape(value)
    value = re.sub(r'"""(.*?)"""', '[...]', value)
    value = value.replace('"""', '"')
    return value.strip()

def split_list_field(value):
    if not value:
        return []
    return [item.strip() for item in value.split(",") if item.strip()]

def start_changeset_root():
    NSMAP = {
        None: "http://www.liquibase.org/xml/ns/dbchangelog",  # default namespace
        "xsi": "http://www.w3.org/2001/XMLSchema-instance"
    }

    root = ET.Element("databaseChangeLog", nsmap=NSMAP)
    root.set("{http://www.w3.org/2001/XMLSchema-instance}schemaLocation",
             "http://www.liquibase.org/xml/ns/dbchangelog "
             "http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd")
    return root

def write_chunk_file(xml_root, chunk_number):
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    filename = os.path.join(OUTPUT_DIR, f"tmdb_chunk_{chunk_number}.xml")

    # Serialize with pretty_print and single quotes
    raw_xml = ET.tostring(xml_root, encoding="utf-8", pretty_print=True, xml_declaration=True).decode("utf-8")
    # single_quoted_xml = re.sub(r'(\s\w+)=(".*?")', lambda m: f"{m.group(1)}='{m.group(2)[1:-1]}'", raw_xml)
    
    with open(filename, "w", encoding="utf-8") as f:
        f.write(raw_xml)

    print(f"✅ Wrote: {filename}")

# === MAIN PROCESS ===

def process_csv_in_chunks():
    with open(CSV_PATH, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)

        chunk_count = 1
        row_count = 0
        xml_root = start_changeset_root()
        changeset = ET.SubElement(xml_root, "changeSet", id=f"chunk-{chunk_count}", author=CHANGESET_AUTHOR)

        for row in reader:
            movie_id = clean_value(row.get("id"))
            row_count += 1

            # Insert to main table
            main_insert = ET.SubElement(changeset, "insert", tableName=MAIN_TABLE)
            for col, val in row.items():
                if col not in LIST_FIELDS and col not in DISCARDED_FIELDS:
                    cleaned = clean_value(val)
                    if val == "" and col in NULLABLE_FLOAT_FIELD:
                        cleaned = "0.0"
                    if val == "" and col in DATE_FIELD:
                        cleaned = datetime.datetime(2999, 1, 1).strftime('%Y-%m-%d %H:%M:%S')
                    #if val == "" and col in NULLABLE_STRING:
                        # default value for nullable string
                        # cleaned = "-1"
                    ET.SubElement(main_insert, "column", name=col, value=cleaned)

            # Handle list fields
            for list_field in LIST_FIELDS:
                items = split_list_field(row.get(list_field))
                table_name = COLUMN_NAME.get(list_field, list_field)
                for item in items:
                    insert = ET.SubElement(changeset, "insert", tableName=table_name)
                    ET.SubElement(insert, "column", name="movie_entity_id", value=movie_id)
                    ET.SubElement(insert, "column", name=list_field, value=clean_value(item))

            # Chunk write logic
            if row_count % CHUNK_SIZE == 0:
                write_chunk_file(xml_root, chunk_count)
                chunk_count += 1
                xml_root = start_changeset_root()
                changeset = ET.SubElement(xml_root, "changeSet", id=f"chunk-{chunk_count}", author=CHANGESET_AUTHOR)

        # Final chunk
        if row_count % CHUNK_SIZE != 0:
            write_chunk_file(xml_root, chunk_count)

if __name__ == "__main__":
    process_csv_in_chunks()
