"""
    - Script to update metadata for new set
"""
from scipy import io as mat_io
from skimage import io as img_io
import argparse
import logging
import glob
import numpy as np
import random
from skimage import io as img_io
import json
import time
import copy

_logger = logging.getLogger(__name__)
_logger.setLevel(0)


def invertJSON(mappings):
    switched = {}
    for num,car in mappings.items():
        switched[car] = num
    return switched

def genFilename(current_file):
    file = str(current_file + 30000) + '.jpg'
    return file

if __name__ == '__main__':
    train = 0 # Set to 1 if train set... Don't have enough time for arg parsing
    labels_meta = mat_io.loadmat("old_cars_train_annos.mat")
    # Update our label json. Will need to assign labels later
    with open("easy_labels.json",'r') as f:
        mappings = json.load(f)
    mappings = invertJSON(mappings)
    np_images = labels_meta['annotations'][0]
    images_count = 0
    label_count = 196
    for image in glob.glob("cleaned/*"):
        dir, image = image.split('\\')
        year,model = image.split(' ',1)
        model = model.split('-')[0]
        key = f'{model} {year}'
        if key not in mappings:
            mappings[key] = str(label_count)
            label_count += 1
        images_count += 1
    with open('updated_labels.json','w+') as fp:
        json.dump(mappings,fp)
    # Create a numpy array to assign to end of other numpy array
    num_images = int(images_count/2)
    old_data = labels_meta['annotations']
    fresh_annotations = copy.deepcopy((old_data[0][0:num_images]))
    print(fresh_annotations.shape)
    current_file = 0
    split = 0
    for image in glob.glob("cleaned/*"):
        if split % 2 == train: # For validation data
            # current_file += 1
            split += 1
            continue
        try:
            img = img_io.imread(image)
            """if train == 1:
                #start = time.time()
                black = [0,0,0]
                indices = np.where(np.any(img == black,axis=2))
                #middle = time.time()
                for i in range(len(indices[0])):
                    img[indices[0][i]][indices[1][i]][0] = int(random.random()*256)
                    img[indices[0][i]][indices[1][i]][1] = int(random.random()*256)
                    img[indices[0][i]][indices[1][i]][2] = int(random.random()*256)
                #end = time.time()"""
        except:
            print('Failure to read image')
            exit(1)
        fresh_annotations[current_file][0][0][0] = 0  # x min
        fresh_annotations[current_file][1][0][0] = 0  # y min
        fresh_annotations[current_file][2] = fresh_annotations[current_file][2].astype(np.uint16)
        fresh_annotations[current_file][3] = fresh_annotations[current_file][3].astype(np.uint16)
        fresh_annotations[current_file][2][0][0] = img.shape[1] # x max
        fresh_annotations[current_file][3][0][0] = img.shape[0] # y max
        dir, image = image.split('\\')
        year,model = image.split(' ',1)
        model = model.split('-')[0]
        key = f'{model} {year}'
        fresh_annotations[current_file][4] = fresh_annotations[current_file][4].astype(np.uint16)
        fresh_annotations[current_file][4][0][0] = int(mappings[key]) + 1
        save_spot = genFilename(current_file)
        fresh_annotations[current_file][5][0] = save_spot
        #img_io.imsave("train_images\\" + save_spot,img)
        print(fresh_annotations[current_file])
        current_file += 1
        split += 1
    np_images = np.append(np_images,fresh_annotations)
    np_images = np.expand_dims(np_images,axis=0)
    long_dict = {
        '__header__': b'MATLAB 5.0 MAT-file, Platform: GLNXA64, Created on: Sat Dec 14 14:13:07 2013', # Copy of old
         '__version__': '1.0',
        '__globals__': [],
        'annotations': np_images
    }
    mat_io.savemat("new_cars_train_annos.mat",long_dict)

