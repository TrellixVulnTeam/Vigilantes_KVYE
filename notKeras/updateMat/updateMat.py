"""
    - Script to extract cars from image
"""
from scipy import io as mat_io
from skimage import io as img_io
import argparse
import logging
import glob
import numpy as np
from skimage import io as img_io

_logger = logging.getLogger(__name__)
_logger.setLevel(0)


if __name__ == '__main__':

    labels_meta = mat_io.loadmat("old_cars_test_annos_withlabels.mat")
    np_images = labels_meta['annotations'][0]
    images_count = 0
    for directory in glob.glob("scraped_images"):
        for image in glob.glob(directory):
            images_count += 1
    fresh_annotations = np.empty((images_count, 5, 1, 1))

    current_file = 0
    current_directory = 1
    for directory in glob.glob("scraped_images"):
        for image in glob.glob(directory):
            if current_file % 2 == 0:
                current_file += 1
                continue
            try:
                img = img_io.imread(image)
            except:
                print('Failure to read image')
                current_file += 1
            fresh_annotations[current_file][0][0][0] = 0  # x min
            fresh_annotations[current_file][1][0][0] = 0  # y min
            fresh_annotations[current_file][2][0][0] = img.shape[1]
            fresh_annotations[current_file][3][0][0] = img.shape[0]
            fresh_annotations[current_file][4][0][0] = current_directory + 196  # Label number
    np.append(np_images,fresh_annotations)
    mat_io.savemat("new_cars_test_annos_withlabels.mat")

