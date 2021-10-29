import cv2
import os
import paddlehub as hub
from PIL import Image
import numpy as np
import time
import concurrent.futures

start=time.time()

def clean(inpath, outpath):

	model = hub.Module(name='solov2', use_gpu=False)
	j=0
	for root, dirs, files in os.walk(inpath, topdown=False):
		for name in files:
			print(os.path.join(root, name))
			img=cv2.imread(os.path.join(root, name))
			output = model.predict(image=img, visualization=False)

			for i in range (output['segm'].shape[0]):
				if (output['label'][i]==2 and output['score'][i]>=0.65):
					img2=img.copy()
					mask=output['segm'][i]


					print(np.nonzero(mask))

					print("ffff",i)
	
					img2[...,0]=img2[...,0]*mask
					img2[...,1]=img2[...,1]*mask
					img2[...,2]=img2[...,2]*mask

	
					sum_x = np.sum(mask, axis=0)
					x = np.where(sum_x > 0.5)[0]
					sum_y = np.sum(mask, axis=1)
					y = np.where(sum_y > 0.5)[0]
					x0, x1, y0, y1 = x[0], x[-1], y[0], y[-1]


					img2=img2[y0:y1,x0:x1,:]
					print("rrrr",i)

					img2gray = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)
					imageVar = cv2.Laplacian(img2gray, cv2.CV_64F).var()
					print(imageVar)
				
					if (imageVar>=900 and imageVar<=3200):
						cv2.imwrite(outpath+'/car-'+str(j)+'---'+str(imageVar)+'.png',img2)
						j+=1
			
with concurrent.futures.ThreadPoolExecutor(max_workers=10) as executor:
  executor.submit(clean, "./tes","./cleaned")


end=time.time()
print(end-start)


