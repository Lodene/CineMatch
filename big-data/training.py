# CSV
import pandas as pd
import pickle  # 👈 tout en minuscule
# Clean de la donnée
import re
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences

# Encodage multilabel (tags)
from sklearn.preprocessing import MultiLabelBinarizer

from sklearn.model_selection import train_test_split

# LTSM
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Embedding, LSTM, Dense, Bidirectional

import tensorflow as tf

# Optimizer
from tensorflow.keras.optimizers import Adam

# reporting
from sklearn.metrics import classification_report

# Constantes 
MAX_VOCAB_SIZE = 10000
MAX_SEQUENCE_LENGTH = 500

EMBEDDING_DIM = 128
LSTM_UNITS = 64

def clean_text(text):
    text = text.lower()
    text = re.sub(r'<.*?>', '', text)  # Supprimer les balises HTML
    text = re.sub(r'[^a-z\s]', '', text)  # Conserver uniquement les lettres et les espaces
    return text

print("Num GPUs Available: ", len(tf.config.list_physical_devices('GPU')))


# Ouverture du csv
df = pd.read_csv("mpst_full_data.csv")

# Nettoyage & tokenerization des synipsis
df = df[['plot_synopsis', 'tags']].dropna()
df['tags'] = df['tags'].apply(lambda x: x.split('|'))

# Initialiser le tokenizer
tokenizer = Tokenizer(num_words=MAX_VOCAB_SIZE, oov_token="<OOV>")
tokenizer.fit_on_texts(df['plot_synopsis'])

# Initialiser le binariseur
mlb = MultiLabelBinarizer()
y = mlb.fit_transform(df['tags'])

# Convertir les textes en séquences
X_seq  = tokenizer.texts_to_sequences(df['plot_synopsis'])
# Appliquer le padding
X_pad  = pad_sequences(X_seq, maxlen=MAX_SEQUENCE_LENGTH)

# Nombre total de tags uniques
num_tags = len(mlb.classes_)

X_train, X_test, y_train, y_test = train_test_split(X_pad, y, test_size=0.2, random_state=42)

model = Sequential()
model.add(Embedding(input_dim=10000, output_dim=128, input_length=300))
model.add(Bidirectional(LSTM(64, return_sequences=False)))
model.add(Dense(len(mlb.classes_), activation='sigmoid'))  # SIGMOID pour multi-label
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
model.fit(X_train, y_train, epochs=5, batch_size=64, validation_split=0.1)

model.save("film_tag_predictor_model.h5")
pickle.dump(tokenizer, open("tokenizer.pkl", "wb"))
pickle.dump(mlb, open("mlb.pkl", "wb"))

y_pred = model.predict(X_test)
print(y_pred[:5])
y_pred_bin = (y_pred > 0.5).astype(int)

print(classification_report(y_test, y_pred_bin, target_names=mlb.classes_))