from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
import os, time
from django.conf import settings
from django.core.files.storage import FileSystemStorage
from django.views.decorators.csrf import csrf_exempt
from PIL import  Image
from torchvision import transforms
from torch import topk, load
from license_plate import plurality_vote,state_inference
import cv2
from pytesseract import image_to_string
from numpy import array
from re import sub
def healthcheck(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    response = {}
    response['chatts'] = ['Health Check!']
    return JsonResponse(response)

@csrf_exempt
def postimages(request):
    if request.method != 'POST':
        return HttpResponse(status=400)
    if request.FILES.get("image"):
        content = request.FILES['image']
        #img = Image.open(content.read())
        img = Image.open(content)
        preprocessing = transforms.Compose([
        transforms.Resize(299),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])])
        im_tense = preprocessing(img)
        model = load("app/model/cpufinal.pth")
        model.eval()
        output = model(im_tense.expand(1,im_tense.shape[0],im_tense.shape[1],im_tense.shape[2]))
        pred = topk(output,3)[1][0].tolist()
    else:
        pred = [-1,-1,-1]
    response = {}
    response['prediction'] = pred
    return JsonResponse(response)
@csrf_exempt
def postplates(request):
    response = {}
    if request.method != 'POST':
        return HttpResponse(status=400)
    if request.FILES.get("image"):
        content = request.FILES['image']
        #img = Image.open(content.read())
        img = Image.open(content)
        img = array(img)
        orgimg = img
        #img = img[:, :, ::-1].copy()
        img = cv2.resize(img, None, fx=1.2, fy=1.2, interpolation=cv2.INTER_CUBIC)
        height, width, depth = img.shape
        img = img[int(25 / 100 * height):int(85 / 100 * height), 0:width]
        img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        plate = plurality_vote([img])  # Just use one function for now
        response['lpn'] = plate
        response['state'] = state_inference(orgimg)
    return JsonResponse(response)
@csrf_exempt
def postvin(request):
    response = {}
    if request.method != 'POST':
        return HttpResponse(status=400)
    if request.FILES.get("image"):
        content = request.FILES['image']
        #img = Image.open(content.read())
        img = Image.open(content)
        img = array(img)
        config_str = "--oem 3 --psm 12 tessedit_char_whitelist=abcdefghijklmnopqrstuvwxyz"
        img = cv2.resize(img, None, fx=1.2, fy=1.2, interpolation=cv2.INTER_CUBIC)
        #img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        #cv2.fastNlMeansDenoising(img, None, 7, 15)
        #img = cv2.GaussianBlur(img, (3, 3), 0)
        cv2.imwrite("vin_test/failure.jpg",img)
        plate = image_to_string(img, lang='eng', config=config_str)
        targets = plate.split('\n')
        for i,s in enumerate(targets):
            targets[i] = sub(r'\W+', '', targets[i])
        response = {}
        response['vin'] = max(targets, key=lambda target: len(target))
        return JsonResponse(response)
