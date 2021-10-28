from keras.models import load_model
from keras.preprocessing.image import load_img, img_to_array, ImageDataGenerator
from tensorflow import math
from keras.applications import resnet_v2
from keras.preprocessing import image
import json
from numpy.random import seed
from numpy import expand_dims
seed(42) # keras seed fixing import
from tensorflow import random
seed(42) # tensorflow seed fixing

if __name__ == "__main__":
    model = load_model("checkpoints/best.h5")
    test_datagen = image.ImageDataGenerator(preprocessing_function=resnet_v2.preprocess_input)
    img = load_img("train/185/00430.jpg")
    img = expand_dims(img,axis=0)
    test_generator = test_datagen.flow(
        img,
        batch_size=1,
        shuffle=False)  # 'categorical')

    #img = efficientnet.preprocess_input(img)
    prediction = model.predict(test_generator)
    with open('easy_labels.json') as f:
        data = json.load(f)
    prediction = math.argmax(prediction,axis=1)[0].numpy()
    print(prediction)
    print(data[str(prediction)])