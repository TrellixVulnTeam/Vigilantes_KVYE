import cv2
import pytesseract
import numpy as np

pytesseract.pytesseract.tesseract_cmd = r'/mnt/c/Program Files/Tesseract-OCR/tesseract.exe'

def main():
    img = cv2.imread('test_license_plate.jpg')
    gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
    gray, img_bin = cv2.threshold(gray,128,255,cv2.THRESH_BINARY | cv2.THRESH_OTSU)
    gray = cv2.bitwise_not(img_bin)
    kernel = np.ones((2, 1), np.uint8)
    img = cv2.erode(gray, kernel, iterations=1)
    img = cv2.dilate(img, kernel, iterations=1)
    print("======== Image text =======")
    print(pytesseract.image_to_string(img, config='-psm 6'))
    print("===========================")

if __name__ == '__main__':
    main()
