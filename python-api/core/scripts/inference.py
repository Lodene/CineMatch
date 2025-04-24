from utils.decorators import to_time
from utils.utils_inference import load_inference_data, recommend_movies


@to_time("Inference pipeline")
def main():
    # Data loading
    df_full, df_enriched, model = load_inference_data()

    # movie_ids = [get_random_movie_id(df_full, df_enriched, in_model=False)]
    # print("Random IDs chosen:", movie_ids)
    movie_ids = [
        "157336",  # Interstellar
    ]
    recommended_movies = recommend_movies(df_full, model, movie_ids, topn=10)

    print("\n-> Movie recommendations:\n")
    for movie_id in recommended_movies:
        row = df_full[df_full["id"] == movie_id]
        if not row.empty:
            row = row.iloc[0]
            print(
                f"- {row['title']} (ID: {row['id']})",
                f"https://www.imdb.com/fr/title/{row['imdb_id']}",
            )


if __name__ == "__main__":
    main()
