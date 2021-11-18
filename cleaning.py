import cv2
import os
import paddlehub as hub
from PIL import Image
import numpy as np
import time
import concurrent.futures

start=time.time()

def clean(inpath, outpath, confidence, res_low, res_up, ):

	model = hub.Module(name='solov2', use_gpu=False)
	j=0
	for root, dirs, files in os.walk(inpath, topdown=False):
		for name in files:
		
			img=cv2.imread(os.path.join(root, name))
			output = model.predict(image=img, visualization=True)
			print(name)
			for i in range (output['segm'].shape[0]):
				if (output['label'][i]==2 and output['score'][i]>=confidence-0.01):
					img2=img.copy()
					mask=output['segm'][i]
					

					print(np.nonzero(mask))

					print("ffff",i)
					print(img2[...,0].shape)
					print(mask.shape)
					if (img2[...,0].shape != mask.shape):
						continue
					img2[...,0]=img2[...,0]*mask
					img2[...,1]=img2[...,1]*mask
					img2[...,2]=img2[...,2]*mask
					print("tttt",i)
	
					sum_x = np.sum(mask, axis=0)
					x = np.where(sum_x > 0.5)[0]
					sum_y = np.sum(mask, axis=1)
					y = np.where(sum_y > 0.5)[0]


					print("ssss",i)
					x0, x1, y0, y1 = x[0], x[-1], y[0], y[-1]


					img2=img2[y0:y1,x0:x1,:]
					print("rrrr",i)

					img2gray = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)
					imageVar = cv2.Laplacian(img2gray, cv2.CV_64F).var()
					print(imageVar)
				
					if (imageVar>=res_low and imageVar<=res_up):
						cv2.imwrite(outpath+'/car-'+str(j)+'---'+str(imageVar)+'.png',img2)						
						j+=1







clean("./tes","./cleaned", 0.65, 400, 5000)


end=time.time()
print(end-start)


