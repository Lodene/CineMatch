from config import MODEL_PATH
from utils.decorators import to_time
from utils.utils_data import load_enriched_dataset
from utils.utils_training import create_tagged_documents, train_doc2vec_model


@to_time("Training pipeline")
def main():
    df = load_enriched_dataset()
    documents = create_tagged_documents(df)
    model = train_doc2vec_model(documents)

    print(f"[SAVE] Saving model to '{MODEL_PATH}'...")
    model.save(MODEL_PATH)
    print("[END] Training pipeline done.")


if __name__ == "__main__":
    main()
