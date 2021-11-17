import cv2
import pytesseract
import numpy as np
import os
from PIL import Image
import sys
from string import *
import glob


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

    config_str = "-l eng --oem 4 --psm 8 --print-parameters"
    #print() #, lang = 'eng', config ='--psm 0 txt'))
    #img = cv2.imread('test_michigan.png')
    #img = cv2.resize(img, dsize=(img.shape[1], img.shape[0]))
    gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
    gray, img_bin = cv2.threshold(gray,128,255,cv2.THRESH_BINARY | cv2.THRESH_OTSU)
    gray = cv2.bitwise_not(img_bin)
    kernel = np.ones((2, 1), np.uint8)
    img = cv2.erode(gray, kernel, iterations=1)
    img = cv2.dilate(img, kernel, iterations=1)
    img = cv2.bitwise_not(img)
    sizes = np.arange(0.1, 1, 0.2)
    possible = set()
    for x in sizes:
        for y in sizes:
            print(x, y)
            temp = cv2.resize(img, None, fx=x, fy=y)
            cv2.imwrite('lplates/testing.png', temp)
            plate = pytesseract.image_to_string(temp, lang='eng', config=("txt "+config_str))
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


def results_parse(words):
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
        "NewHampshire",
        "NewJersey",
        "NewMexico",
        "NewYork",
        "NorthCarolina",
        "NorthDakota",
        "Ohio",
        "Oklahoma",
        "Oregon",
        "Pennsylvania",
        "RhodeIsland",
        "SouthCarolina",
        "SouthDakota",
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
    for i in range(len(states)):
        states[i] = states[i].lower() # inefficient, i know
    for word in words:
        for state in states:
            if word.find(state) != -1:
                final_state = state
                print("final_state:", final_state)
                return final_state
    return None
def clean(plate):
    plate = plate.replace(" ", "")
    plate = plate.lower()
    while plate.find("\n\n") != -1:
        plate = plate.replace("\n\n", "\n")
    words = plate.split('\n')
    return words

def ensemble(img,kernel):
    config_str = "-l eng --oem 4 --psm 7 --print-parameters"
    state = results_parse(clean(pytesseract.image_to_string(img, lang='eng', config=("txt " + config_str))))
    if state is not None:
        return state
    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    state = results_parse(clean(pytesseract.image_to_string(img, lang='eng', config=("txt " + config_str))))
    if state is not None:
        return state
    thr = cv2.adaptiveThreshold(img, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                cv2.THRESH_BINARY_INV, 11, 2)
    state = results_parse(clean(pytesseract.image_to_string(thr, lang='eng', config=("txt " + config_str))))
    if state is not None:
        return state
    if state is not None:
        return state
    img = cv2.fastNlMeansDenoising(img, None, 7, 15)
    state = results_parse(clean(pytesseract.image_to_string(img, lang='eng', config=("txt " + config_str))))
    if state is not None:
        return state
    filter = cv2.GaussianBlur(img, (3, 3), 0)
    state = results_parse(clean(pytesseract.image_to_string(filter, lang='eng', config=("txt " + config_str))))
    if state is not None:
        return state
    img = cv2.addWeighted(filter, 0.5, img, 0.5, 0)
    state = results_parse(clean(pytesseract.image_to_string(img, lang='eng', config=("txt " + config_str))))
    if state is not None:
        return state

    return None

    # img = cv2.bitwise_not(img) BAD accuracy change
    img = cv2.erode(img, kernel, iterations=1)
    cv2.imwrite("erode.jpg", img)
    state = results_parse(clean(pytesseract.image_to_string(img, lang='eng', config=("txt " + config_str))))
    if state is not None:
        return 1
    img = cv2.dilate(img, kernel, iterations=1)
    cv2.imwrite("dilate.jpg", img)
    state = results_parse(clean(pytesseract.image_to_string(img, lang='eng', config=("txt " + config_str))))
    if state is not None:
        return 1

def predict_state():
    config_str = "-l eng --oem 4 --psm 7 --print-parameters"
    success = 0
    kernel = np.ones((2, 1), np.uint8)
    for file in glob.glob("plate_test/*"):
        correct = False
        originalimg = cv2.imread(file)
        height = originalimg.shape[0]
        width = originalimg.shape[1]
        top = originalimg[0:int(height/3),0:width]
        bottom = originalimg[int(2*height/3):height,0:width]
        crops = [originalimg,top,bottom]
        for crop in crops:
            response = ensemble(crop,kernel)
            if response is not None:
                success += 1
                print(response)
                correct = True
                break


        #cv2.imwrite("gray.jpg",img_gray)
        #cv2.imwrite("blur.jpg", img_blur)
        #cv2.imwrite("denoise.jpg",img_denoise)
        #sobelx = cv2.Sobel(src=img_blur, ddepth=cv2.CV_64F, dx=1, dy=0, ksize=3)
        #sobely = cv2.Sobel(src=img_blur, ddepth=cv2.CV_64F, dx=0, dy=1, ksize=3)
        #sobelxy = cv2.Sobel(src=img_blur, ddepth=cv2.CV_64F, dx=1, dy=1, ksize=3)
        #sobelxy = Image.fromarray(sobelxy)
        #sobelxy = sobelxy.convert('RGB')
        '''plate = pytesseract.image_to_string(img, lang='eng', config=("txt "+config_str))
        words = clean(plate)
        if(results_parse(words)):
            success += 1
            continue
        if not correct:
            path,file = file.split('\\')
            cv2.imwrite('failures/'+file, img)'''
    print("Accuracy:", success / len(glob.glob("plate_test/*")))


if __name__ == '__main__':
    #lpn_predict(cv2.imread(sys.argv[1]))
    predict_state()
    #predict_source()
