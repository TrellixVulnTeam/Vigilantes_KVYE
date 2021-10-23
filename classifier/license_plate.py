import cv2
import pytesseract
import numpy as np
import os
from PIL import Image

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
    #img = cv2.imread('test_michigan.png')

    gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
    gray, img_bin = cv2.threshold(gray,128,255,cv2.THRESH_BINARY | cv2.THRESH_OTSU)
    gray = cv2.bitwise_not(img_bin)
    kernel = np.ones((2, 1), np.uint8)
    img = cv2.erode(gray, kernel, iterations=1)
    img = cv2.dilate(img, kernel, iterations=1)
    
    plate = pytesseract.image_to_string(img, lang='eng', config=("txt "+config_str))
    plate = plate.replace(" ", "")
    plate = plate.lower()
    for i in range(len(states)):
        states[i] = states[i].lower()
    while plate.find("\n\n") != -1:
        plate = plate.replace("\n\n", "\n")

    words = plate.split('\n')
    print(words)
    final_state = None
    for word in words:
        for state in states:
            if word.find(state) != -1:
                final_state = state
    print("final_state:", final_state)
    print()



    

if __name__ == '__main__':
    lpn_predict()
