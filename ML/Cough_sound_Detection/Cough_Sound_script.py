import librosa
import pandas as pd
import numpy as np
import csv
from sklearn.preprocessing import StandardScaler
from tensorflow import keras

import librosa, librosa.display, csv

header = "filename chroma_stft rmse spectral_centroid spectral_bandwidth rolloff zero_crossing_rate"
for i in range(1, 21):
    header += f" mfcc{i}"
header += " label"
header = header.split()

file = open("data_new_extended.csv", "w")
with file:
    writer = csv.writer(file)
    writer.writerow(header)

for i in range(total_files):
    file_name = "input audio file path"
    y, sr = librosa.load(file_name, mono=True, duration=5)
    chroma_stft = librosa.feature.chroma_stft(y=y, sr=sr)
    rmse = librosa.feature.rms(y=y)
    spec_cent = librosa.feature.spectral_centroid(y=y, sr=sr)
    spec_bw = librosa.feature.spectral_bandwidth(y=y, sr=sr)
    rolloff = librosa.feature.spectral_rolloff(y=y, sr=sr)
    zcr = librosa.feature.zero_crossing_rate(y)
    mfcc = librosa.feature.mfcc(y=y, sr=sr)
    to_append = f"{np.mean(chroma_stft)} {np.mean(rmse)} {np.mean(spec_cent)} {np.mean(spec_bw)} {np.mean(rolloff)} {np.mean(zcr)}"
    for e in mfcc:
        to_append += f" {np.mean(e)}"

    file = open("data_new_extended.csv", "a")
    with file:
        writer = csv.writer(file)
        writer.writerow(to_append.split())

data = pd.read_csv("data_new_extended.csv")
scaler = StandardScaler()
X = scaler.fit_transform(np.array(data.iloc[:, :-1], dtype=float))


model = keras.models.load_model("path to/cough_sound_classifier.h5")

y_pred = model.predict(X)
postive_probability = y_pred[0][1]
# y_pred = np.argmax(y_pred)
