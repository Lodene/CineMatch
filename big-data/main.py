import re
import numpy as np
import pickle
from bs4 import BeautifulSoup
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.sequence import pad_sequences

# Charger les objets
model = load_model("film_tag_predictor_model.h5")

def tag_tokenizer(x):
    return x.split(",")

with open("vectorizer.pkl", "rb") as f:
    vectorizer = pickle.load(f)
vect = pickle.load(open("vect.pkl", "rb"))          # Keras Tokenizer
#vectorizer = pickle.load(open("vectorizer.pkl", "rb"))  # CountVectorizer pour tags (si tu le sauvegardes aussi)

# Reprends les mêmes stopwords que dans l'entraînement
stopwords = set([...])

# Fonctions de nettoyage
def decontracted(phrase):
    phrase = re.sub(r"won't", "will not", phrase)
    phrase = re.sub(r"can\'t", "can not", phrase)
    phrase = re.sub(r"n\'t", " not", phrase)
    phrase = re.sub(r"\'re", " are", phrase)
    phrase = re.sub(r"\'s", " is", phrase)
    phrase = re.sub(r"\'d", " would", phrase)
    phrase = re.sub(r"\'ll", " will", phrase)
    phrase = re.sub(r"\'t", " not", phrase)
    phrase = re.sub(r"\'ve", " have", phrase)
    phrase = re.sub(r"\'m", " am", phrase)
    return phrase

def preprocess_text(text):
    text = re.sub(r"http\S+", "", text)
    text = BeautifulSoup(text, 'lxml').get_text()
    text = decontracted(text)
    text = re.sub("\S*\d\S*", "", text).strip()
    text = re.sub('[^A-Za-z]+', ' ', text)
    text = ' '.join(e.lower() for e in text.split() if e.lower() not in stopwords)
    return text.strip()

def tag_tokenizer(x):
    return x.split(",")

# Texte d'exemple
text = "Two thousand years ago, Nhagruul the Foul, a sorcerer who reveled in corrupting the innocent and the spread of despair, neared the end of his mortal days and was dismayed. Consumed by hatred for the living, Nhagruul sold his soul to the demon Lords of the abyss so that his malign spirit would survive. In an excruciating ritual, Nhagrulls skin was flayed into pages, his bones hammered into a cover, and his diseased blood became the ink to pen a book most vile. Creatures vile and depraved rose from every pit and unclean barrow to partake in the fever of destruction. The kingdoms of Karkoth were consumed by this plague of evil until an order of holy warriors arose from the ashes. The Knights of the New Sun swore an oath to resurrect hope in the land. The purity of their hearts was so great that Pelor, the God of Light, gave the Knights powerful amulets with which to channel his power. Transcendent with divine might, the Knights of the New Sun pierced the shadow that had darkened the land for twelve hundred years and cast it asunder. But not all were awed by their glory. The disciples of Nhagruul disassembled the book and bribed three greedy souls to hide the pieces until they could be retrieved. The ink was discovered and destroyed but, despite years of searching, the cover and pages were never found. Peace ruled the land for centuries and the Knights got lost in the light of their own glory. As memory of the awful events faded so did the power of servants of Pelor. They unwittingly abandoned themselves in the incorrect belief that the Book of Vile Darkness could never again be made whole.Now, the remaining pieces have been discovered, and an ancient evil is attempting to bring them together and restore the relic and the evil it brought. But at the same time a potential new paladin has been named to the Knights of the New Sun to attempt to renew their power to fight this evil. But, to do so, he may need to go against all that he has held dear, risking more that just his own soul in his quest to destroy the evil that surrounds him at every turn."

# Prétraitement
clean_text = preprocess_text(text)
seq = vect.texts_to_sequences([clean_text])
padded = pad_sequences(seq, maxlen=1200, padding='post')

# Prédiction
probas = model.predict(padded)

# Appliquer un seuil
threshold = 0.2
binary_pred = (probas >= threshold).astype(int)

# Récupérer les tags prédits
predicted_tags = vectorizer.inverse_transform(binary_pred)[0]

print("Texte :", text)
print("Tags prédits :", predicted_tags)