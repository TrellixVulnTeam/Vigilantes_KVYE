import cv2
import os
import paddlehub as hub
from PIL import Image
import numpy as np
import time
import concurrent.futures
import boto3


def read_from_s3(bucket_name, file_name):
    s3 = boto3.resource('s3')
    obj = s3.Object(bucket_name, file_name)
    return obj.get()['Body'].read()


def write_to_s3(bucket_name, file_name, content):
    s3 = boto3.resource('s3')
    obj = s3.Object(bucket_name, file_name)
    obj.put(Body=content)


def clean(confidence, res_low, res_up, source_bucket_name, source_file_name_list , target_bucket_name):
	print("uuuuuuuuu")
	model = hub.Module(name='solov2', use_gpu=True)
	j=0
	print("iiiiii")
	for name in source_file_name_list:
		print("rrrrrddd")
		imgin=read_from_s3(source_bucket_name, name)
		print("st111111")
		#img=cv2.imread(os.path.join(root, name))
		print("st2222222")
		imgin=np.asarray(bytearray(imgin), dtype="uint8")
		print("st3333333")
		img=cv2.imdecode(imgin, cv2.IMREAD_COLOR)
		print("st4444444")
			#reading image from s3 and transform it to array and then read as image


		output = model.predict(image=img, visualization=False)
			#predicting with pretrained model
		print("ffff",i)
		for i in range (output['segm'].shape[0]):
			if (output['label'][i]==2 and output['score'][i]>=confidence-0.01):
				img2=img.copy()
				mask=output['segm'][i]
					
				print(np.nonzero(mask))

				
				img2[...,0]=img2[...,0]*mask
				img2[...,1]=img2[...,1]*mask
				img2[...,2]=img2[...,2]*mask
				print("tttt",i)
					#apply the output segmentation on the original picture to clean the background 				


				sum_x = np.sum(mask, axis=0)
				x = np.where(sum_x > 0.5)[0]
				sum_y = np.sum(mask, axis=1)
				y = np.where(sum_y > 0.5)[0]
					#calculate the cropping window size

				print("ssss",i)
				x0, x1, y0, y1 = x[0], x[-1], y[0], y[-1]


				img2=img2[y0:y1,x0:x1,:]
					#cropping
				print("rrrr",i)

				img2gray = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)
				imageVar = cv2.Laplacian(img2gray, cv2.CV_64F).var()
				print(imageVar)
					#calcualting clarity of the image

				if (imageVar>=res_low and imageVar<=res_up):
					#cv2.imwrite(outpath+'/car-'+str(j)+'---'+str(imageVar)+'.png',img2)							
					img_encode = cv2.imencode('.png', img2)[1]
					data_encode = np.array(img_encode)
					str_encode = data_encode.tostring()
					write_to_s3(target_bucket_name, 'car-'+str(j)+'---'+str(imageVar)+'.png', str_encode)
					j+=1
							#if the clearity is between the required values encode the image and save it to s3




def lambda_handler(event, context):
	print("ppppppppppp")
	for root, dirs, files in os.walk("/tmp", topdown=False):
    		for name in files:
        		print(os.path.join(root, name))
    		for name in dirs:
        		print(os.path.join(root, name))

	for root, dirs, files in os.walk("/modules", topdown=False):
    		for name in files:
        		print(os.path.join(root, name))
    		for name in dirs:
        		print(os.path.join(root, name))
	clean(0.65, 400, 5000, "vigilantes-scraped-images", event["images"], "vigilantes-post-process")





