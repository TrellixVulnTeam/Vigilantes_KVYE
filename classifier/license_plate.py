import cv2
import pytesseract
import numpy as np
import os
from PIL import Image
import sys
import matplotlib.pyplot as plt
from string import *


#pytesseract.pytesseract.tesseract_cmd = r'/mnt/c/Program Files/Tesseract-OCR/tesseract.exe'

def lpn_predict(img=None):
    states = [
        "Alabama",
        "Alaska",
        "Arizona",
        "Arkansas",
        "California",
        "Colorado",
        "Connecticut",
        "Delaware",
        "Florida",
        "Georgia",
        "Hawaii",
        "Idaho",
        "Illinois",
        "Indiana",
        "Iowa",
        "Kansas",
        "Kentucky",
        "Louisiana",
        "Maine",
        "Maryland",
        "Massachusetts",
        "Michigan",
        "Minnesota",
        "Mississippi",
        "Missouri",
        "Montana",
        "Nebraska",
        "Nevada",
        "New Hampshire",
        "New Jersey",
        "New Mexico",
        "New York",
        "North Carolina",
        "North Dakota",
        "Ohio",
        "Oklahoma",
        "Oregon",
        "Pennsylvania",
        "Rhode Island",
        "South Carolina",
        "South Dakota",
        "Tennessee",
        "Texas",
        "Utah",
        "Vermont",
        "Virginia",
        "Washington",
        "West Virginia",
        "Wisconsin",
        "Wyoming",
    ]

    config_str = "-l eng --oem 4 --psm 8"
    #print() #, lang = 'eng', config ='--psm 0 txt'))
    #img = cv2.imread("sample.jpg")
    #img = cv2.resize(img, dsize=(img.shape[1], img.shape[0]))
    img2 = cv2.imread(img, cv2.IMREAD_GRAYSCALE)
    #gray, img_bin = cv2.threshold(gray,128,255,cv2.THRESH_BINARY | cv2.THRESH_OTSU)
    gray = cv2.bitwise_not(img2)#img_bin)
    kernel = np.ones((2, 1), np.uint8)
    img = cv2.erode(gray, kernel, iterations=1)
    img = cv2.dilate(img, kernel, iterations=1)
    img = cv2.bitwise_not(img)
    sizes = np.arange(0.1, 1, 0.2)
    possible = set()
    for x in sizes:
        for y in sizes:
            for z in [1]:#range(-30, 30):
                print(x, y, z)
                temp = cv2.resize(img, None, fx=x, fy=y)
                #temp = cv2.cv.rotate(temp, z)
                cv2.imwrite('lplates/testing.png', temp)
                print(Image.open('lplates/testing.png'))
                plate = pytesseract.image_to_string(Image.open('lplates/testing.png'), lang='eng', config=("txt "+config_str))
                print(plate)
                words = plate.split('\n')
                #print(words)
                # From https://stackoverflow.com/questions/49001670/remove-elements-contains-lowercase-null-from-list
                words_kept = [word for word in words if all([letter in punctuation+ascii_uppercase+digits+' ' for letter in word]) and word]
                for w in words_kept:
                    word = ""
                    non_space = False
                    space = 0
                    for letter in w:
                        if letter not in punctuation:
                            word += letter
                        if letter != ' ':
                            non_space = True
                        else:
                            space += 1
                    if non_space and space <= 1:
                        possible.add(word)

    # plate = plate.replace(" ", "")
    # print(plate)
    # plate = plate.lower()
    # for i in range(len(states)):
    #     states[i] = states[i].lower()
    # while plate.find("\n\n") != -1:
    #     plate = plate.replace("\n\n", "\n")

    # words = plate.split('\n')
    # print(words)
    # final_state = None
    # for word in words:
    #     for state in states:
    #         if word.find(state) != -1:
    #             final_state = state
    # print("final_state:", final_state)

    print(possible)

if __name__ == '__main__':
    print(sys.argv[1])
    lpn_predict(sys.argv[1])
    #predict_source()
