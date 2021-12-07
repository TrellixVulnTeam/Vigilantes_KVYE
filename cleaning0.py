from imageai.Detection import ObjectDetection
import os
import cv2
import paddlehub as hub
import numpy as np

execution_path = os.getcwd()

detector = ObjectDetection()
detector.setModelTypeAsRetinaNet()
detector.setModelPath( os.path.join(execution_path , "resnet50_coco_best_v2.1.0.h5"))
detector.loadModel()

detections, objects_path = detector.detectObjectsFromImage(input_image=os.path.join(execution_path , "tes6.jpg"), output_image_path=os.path.join(execution_path , "tes6n3.jpg"), minimum_percentage_probability=70,  extract_detected_objects=True,display_percentage_probability=False, display_object_name=False)


for eachObject, eachObjectPath in zip(detections, objects_path):
    print(eachObject["name"] , " : " , eachObject["percentage_probability"], " : ", eachObject["box_points"] )
    print("Object's image saved in " + eachObjectPath)
    print("--------------------------------")





for root, dirs, files in os.walk(execution_path, topdown=False):
    for name in files:
        print(os.path.join(root, name))
    for name in dirs:
        print(os.path.join(root, name))



img = cv2.imread('/PATH/TO/IMAGE')
model = hub.Module(name='solov2', use_gpu=False)
output = model.predict(image=img, visualization=False)