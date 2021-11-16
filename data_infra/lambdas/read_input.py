import datetime
import os
from s3_helper import read_from_s3


SRC_BUCKET_NAME = os.environ['SRC_BUCKET_NAME']


def lambda_handler(event, context):
    file_name = event['file']
    depth = event['depth']
    print(f'Reading {file_name} from bucket')
    terms = read_from_s3(SRC_BUCKET_NAME, file_name).splitlines()
    run_time_str = str(datetime.datetime.now())

    return {
        'run_time': run_time_str,
        'terms': terms,
        'depth': depth
    }
