import cv2
import pytesseract
import numpy as np
import os
from PIL import Image
import sys
import matplotlib.pyplot as plt
from string import *
import glob
from pytesseract import Output
from autocorrect import Speller
import re


#pytesseract.pytesseract.tesseract_cmd = r'/mnt/c/Program Files/Tesseract-OCR/tesseract.exe'

def lpn_predict(img=None):

    config_str = "-l eng --oem 4 --psm 8 --print-parameters"
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
        word = re.sub(r'\W+', '', word)
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

def ensemble(img,kernel): # Can be made clean by passing functions and *args maybe to a helper
    config_str = "-l eng --oem 4 --psm 7 --print-parameters"
    originalimg = img
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
    return None

def predict_state():
    config_str = "-l eng --oem 4 --psm 11 --print-parameters"
    success = 0
    kernel = np.ones((2, 1), np.uint8)
    for file in glob.glob("plate_test/*"):
        correct = False
        originalimg = cv2.imread(file)
        height = originalimg.shape[0]
        width = originalimg.shape[1]
        try:
            top = originalimg[10:int(height/3),0:width]
            topcenter = originalimg[10:int(height/3),int(15/100*width):int(85/100*width)]
            bottom = originalimg[int(2*height/3):height-10,0:width]
            botcenter = originalimg[int(2*height/3):height-10,int(15/100*width):int(85/100*width)]
            firstthirds = originalimg[10:int(height/3),0:int(2*width/3)]
            noright = originalimg[10:int(height/3),0:int(4*width/5)]
        except:
            print("Really poor image. Please retake")
        print(file.split('\\'))
        if file.split('\\')[1].split('-')[0] == 'illinois':
            cv2.imwrite("topcrop.jpg",top)
            cv2.imwrite("botcrop.jpg", bottom)
            cv2.imwrite("botcenter.jpg", botcenter)
            cv2.imwrite("topcenter.jpg", topcenter)
        crops = [originalimg,top,bottom,topcenter,botcenter,firstthirds,noright]
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

"""def most_common_level(levels):
    return max(set(levels), key = levels.count)"""

def plurality_vote(images): # Please use an odd number of
    config_str = "--oem 3 --psm 8"
    strings = []
    for image in images:
        plate = pytesseract.image_to_string(image, lang='eng', config=(config_str), output_type=Output.STRING)
        plate = re.sub(r'\W+', '', plate)
        strings.append(plate)
    strings = [string for string in strings if len(string) >= 5 and len(string) <= 8]
    if (len(strings) == 0):
        return None
    else:
        dict = {}
        maxstring, maxnum = '',0
        for string in strings:
            dict[string] = dict.get(string,0) + 1
            if dict[string] >= maxnum:
                if(dict[string] > maxnum):
                    maxstring = string
                else:
                    if(len(string) > len(maxstring)):
                        maxstring = string
                maxnum = dict[string]
        return maxstring


def predict_lpn():
    success = 0
    for file in glob.glob("plate_test/*"):
        origimg = cv2.imread(file)
        origimg = cv2.resize(origimg, None, fx=1.2, fy=1.2, interpolation=cv2.INTER_CUBIC)
        height = origimg.shape[0]
        width = origimg.shape[1]
        img = origimg[int(25/100*height):int(85/100*height),0:width]
        aggressive_cut = origimg[int(25 / 100 * height):int(85 / 100 * height), int(5 / 100 * width):int(95 / 100 * width)]
        img2 = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        cv2.imwrite("example.jpg",img)
        kernel = np.ones((2, 1), np.uint8)
        img3 = cv2.erode(img, kernel, iterations=1) # No good
        img4 = cv2.dilate(img, kernel, iterations=1) # No good
        img5 = cv2.fastNlMeansDenoising(img, None, 7, 15)
        choices = [img2, aggressive_cut]
        sizes = np.arange(0.3, 1, 0.3)
        for x in sizes:
            for y in sizes:
                choices.append(cv2.resize(img2, None, fx=x, fy=y))
        plate = plurality_vote(choices)

        #print(plate)
        print("Predicted:",plate)
        delimiter = '/'
        print("Correct: ", file.split(delimiter)[1].split('-')[1].split('.')[0].upper())
        if plate == file.split(delimiter)[1].split('-')[1].split('.')[0].upper():
            success += 1
        resp = None
        while resp is not None:
            print('Continue')
            resp = input()
    print("Accuracy:", success / len(glob.glob("plate_test/*")))
if __name__ == '__main__':
    # print(sys.argv[1])
    # lpn_predict(sys.argv[1])

    #lpn_predict(cv2.imread(sys.argv[1]))
    #predict_state()
    predict_lpn()
    #predict_source()
