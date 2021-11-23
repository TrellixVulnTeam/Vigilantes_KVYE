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
from license_plate import plurality_vote
seed(42) # keras seed fixing import
from tensorflow import random,reverse,convert_to_tensor
from tensorflow import stack
random.set_seed(1)
from keras import backend as K
from pytesseract import image_to_string
from re import sub
import string
K.set_image_data_format('channels_last')
def car_predict():
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
def license_plate_read():
    img = cv2.imread("example.jpg")
    img = cv2.resize(img, None, fx=1.2, fy=1.2, interpolation=cv2.INTER_CUBIC)
    height, width = img.shape
    img = img[int(25 / 100 * height):int(85 / 100 * height), 0:width]
    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    plate = plurality_vote([img])  # Just use one function for now
    response = {}
    response['lpn'] = plate
    response['state'] = state_inference(img)
    return JsonResponse(response)

if __name__ == "__main__":
    config_str = "--oem 3 --psm 12 tessedit_char_whitelist=abcdefghijklmnopqrstuvwxyz"
    img = cv2.imread("vin_test/vin5.jpg")
    img = cv2.resize(img, None, fx=1.2, fy=1.2, interpolation=cv2.INTER_CUBIC)
    #img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    #cv2.fastNlMeansDenoising(img, None, 7, 15)
    #img = cv2.GaussianBlur(img, (3, 3), 0)
    cv2.imwrite("vin_test/failure.jpg",img)
    plate = image_to_string(img, lang='eng', config=config_str)
    targets = plate.split('\n')
    for i,s in enumerate(targets):
        targets[i] = sub(r'\W+', '', targets[i])
    plate = max(targets, key=lambda target: len(target))
    print(plate)

