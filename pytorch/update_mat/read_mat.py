from scipy import io as mat_io
from skimage import io as img_io
import argparse
import logging
import glob
import numpy as np
from skimage import io as img_io
import json
import torch


if __name__ == '__main__':

    labels_meta = mat_io.loadmat("new_cars_train_annos.mat")
    print(labels_meta)
    weights = {}
    for img_ in labels_meta['annotations'][0]:
        label = img_[4][0][0]
        if label not in weights:
            weights[label] = 1
        else:
            weights[label] += 1
    for label,weight in weights.items():
        weights[label] = (5294/397) / weights[label]
    weights[392] = (5294/397)
    weights[293] = (5294/397)
    weights = sorted(weights.items())
    tensor_weights = torch.zeros(397)
    for i in range(len(weights)):
        tensor_weights[i] = weights[i][1]


