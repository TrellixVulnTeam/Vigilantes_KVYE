import scipy.io
import shutil
import os
import cv2
#from torchvision import transforms
import json


mat = scipy.io.loadmat('data/devkit/cars_train_annos.mat')
"""labels = scipy.io.loadmat('data/devkit/cars_meta.mat')
easy_labels = {}
print(labels['class_names'][0])
count = 0
for car in labels['class_names'][0]:
    easy_labels[count] = labels['class_names'][0][count][0]
    count += 1
print(easy_labels)
with open('easy_labels.json','w') as f:
    json.dump(easy_labels,f)
exit(1)"""
training_class = mat['annotations']['class']
training_fname = mat['annotations']['fname']
training_x1 = mat['annotations']['bbox_x1']
training_y1 = mat['annotations']['bbox_y1']
training_x2 = mat['annotations']['bbox_x2']
training_y2 = mat['annotations']['bbox_y2']
"""print(training_x1[0][0][0][0])
print(training_x2[0][0][0][0])
print(training_y1[0][0][0][0])
print(training_y2[0][0][0][0])"""
mat = scipy.io.loadmat('data/devkit/cars_test_annos_withlabels.mat')
testing_class = mat['annotations']['class']
testing_fname = mat['annotations']['fname']
testing_x1 = mat['annotations']['bbox_x1']
testing_y1 = mat['annotations']['bbox_y1']
testing_x2 = mat['annotations']['bbox_x2']
testing_y2 = mat['annotations']['bbox_y2']

training_source = 'data/cars_train/' # specify source training image path
training_output = 'train/' # specify target trainig image path (trainig images need to be orgnized to specific structure)
counter = 0
for idx, cls in enumerate(training_class[0]):
    cls = cls[0][0]
    fname = training_fname[0][idx][0]
    img = cv2.imread(training_source+str(fname))
    img = img[training_y1[0][idx][0][0]:training_y2[0][idx][0][0], training_x1[0][idx][0][0]:training_x2[0][idx][0][0]]
    output_path = os.path.join(training_output, str(cls))
    output_path = os.path.abspath(output_path)
    if not os.path.exists(output_path):
        os.mkdir(output_path)
    #shutil.copy(os.path.join(training_source, fname), os.path.join(output_path, fname))
    cv2.imwrite(output_path + '\\' + str(fname), img)

testing_source = 'data/cars_test/' # specify source testing image path
testing_output = 'test/' # specify target testing image path (testing images need to be orgnized to specific structure)
for idx, cls in enumerate(testing_class[0]):
    cls = cls[0][0]
    fname = testing_fname[0][idx][0]
    img = cv2.imread(testing_source+str(fname))
    img = img[testing_y1[0][idx][0][0]:testing_y2[0][idx][0][0],testing_x1[0][idx][0][0]:testing_x2[0][idx][0][0]]
    output_path = os.path.join(testing_output, str(cls))
    if not os.path.exists(output_path):
        os.mkdir(output_path)
    #shutil.copy(os.path.join(testing_source, fname), os.path.join(output_path, fname))
    cv2.imwrite(output_path + '\\' + str(fname), img)