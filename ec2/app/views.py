from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
import os, time
from django.conf import settings
from django.core.files.storage import FileSystemStorage
from django.views.decorators.csrf import csrf_exempt
from PIL import  Image
from torchvision import transforms
from torch import topk, load

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
