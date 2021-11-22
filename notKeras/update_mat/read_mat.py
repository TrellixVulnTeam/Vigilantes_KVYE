from scipy import io as mat_io
from skimage import io as img_io
import argparse
import logging
import glob
import numpy as np
from skimage import io as img_io
import json


if __name__ == '__main__':

    labels_meta = mat_io.loadmat("new_cars_test_annos_withlabels.mat")
    print(labels_meta)
    for img_ in labels_meta['annotations'][0]:
        x_min = img_[0][0][0]
        y_min = img_[1][0][0]

        x_max = img_[2][0][0]
        y_max = img_[3][0][0]
        # label = [4][0][0]
        print(img_)

