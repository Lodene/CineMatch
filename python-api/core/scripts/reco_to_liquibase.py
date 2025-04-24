import uuid
from os.path import join

from config import LIQUIBASE_FOLDER
from utils.decorators import to_time
from utils.utils_inference import load_inference_data, recommend_movies

SOURCE_TABLE = "movies"
JOIN_TABLE = "related_movies"
SOURCE_ID_COLUMN = "movie1_id"
TARGET_ID_COLUMN = "movie2_id"
CHANGE_AUTHOR = "T-H-O-M-A-S"


@to_time("Database full inference pipeline")
def main():
    # Data loading
    df_full, df_enriched, model = load_inference_data()

    for i, (idx, row) in enumerate(df_enriched.iterrows()):
        input_id = str(row["id"])
        print(f"> [{i}] On ID: {input_id} ('{row['title']}')")

        # Recommend movies
        recommended_movies = recommend_movies(df_full, model, [input_id], topn=10)

        # Create save file
        output_file = join(LIQUIBASE_FOLDER, f"reco_{input_id}.xml")

        with open(output_file, "w") as f:
            f.write('<?xml version="1.0" encoding="UTF-8"?>\n')
            f.write(
                "<databaseChangeLog "
                'xmlns="http://www.liquibase.org/xml/ns/dbchangelog"\n'
                '                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n'
                '                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog '
                'http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd">\n\n'
            )

            change_set_id = str(uuid.uuid4())
            f.write(f'    <changeSet id="{change_set_id}" author="{CHANGE_AUTHOR}">\n')

            for rec_id in recommended_movies:
                f.write(f'        <insert tableName="{JOIN_TABLE}">\n')
                f.write(
                    f'            <column name="{SOURCE_ID_COLUMN}" value="{input_id}"/>\n'
                )
                f.write(
                    f'            <column name="{TARGET_ID_COLUMN}" value="{rec_id}"/>\n'
                )
                f.write("        </insert>\n")

            f.write("    </changeSet>\n")
            f.write("</databaseChangeLog>\n")

        print(f"[SAVE] Liquibase file saved at '{output_file}'.")


if __name__ == "__main__":
    main()
