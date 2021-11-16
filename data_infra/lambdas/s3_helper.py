import boto3

s3 = boto3.resource('s3')

def read_from_s3(bucket_name, file_name):
    obj = s3.Object(bucket_name, file_name)
    return obj.get()['Body'].read().decode('utf-8')

def write_to_s3(bucket_name, file_name, content):
    obj = s3.Object(bucket_name, file_name)
    obj.put(Body=content)