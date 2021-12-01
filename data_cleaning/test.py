import cv2
import paddlehub as hub

img = cv2.imread('./tes7.jpg')
model = hub.Module(name='solov2', use_gpu=False)
output = model.predict(image=img, visualization=True)