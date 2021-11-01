from keras.models import load_model
from keras.preprocessing.image import load_img, img_to_array, ImageDataGenerator
from tensorflow import math
from keras.applications import resnet_v2
from keras.preprocessing import image
import json
from numpy.random import seed
from numpy import expand_dims
import cv2
import numpy as np
seed(42) # keras seed fixing import
from tensorflow import random,reverse,convert_to_tensor
from tensorflow import stack
random.set_seed(1)
from keras import backend as K
K.set_image_data_format('channels_last')
if __name__ == "__main__":
    model = load_model("checkpoints/best.h5")
    img = cv2.imread("train/185/00683.jpg")
    img = expand_dims(img,axis=0)
    img = convert_to_tensor(img,dtype='float32')
    test_datagen = image.ImageDataGenerator(preprocessing_function=resnet_v2.preprocess_input)
    test_generator = test_datagen.flow_from_directory(
        "dummy",
        target_size=(224,224),
        batch_size=1,
        shuffle=False,
        class_mode=None)


    #img = resnet_v2.preprocess_input(img)
    predicted_label_probs = model.predict(test_generator)
    predicted_labels = np.argmax(predicted_label_probs, axis=1)
    probs = np.max(predicted_label_probs, axis=1)
    num = 0
    for prob in probs:
        print(prob,predicted_labels[num])
        num += 1
    with open('easy_labels.json') as f:
        data = json.load(f)
    prediction = math.argmax(prediction,axis=1)[0].numpy()
    print(prediction)
    print(data[str(prediction)])