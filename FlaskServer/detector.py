from yolov3.models import *
from yolov3.utils.utils import *
import cv2
import pandas as pd
    
import torch
import numpy as np
from PIL import Image
from torchvision import datasets, models, transforms
from torch.autograd import Variable
import torch.nn.functional as F


class Detector:
    def __init__(self):
        self.cfg = "data/cfg/yolov3-spp-403cls.cfg"
        self.weights = "data/weights/best_403food_e200b150v2.pt"
        self.names = "data/cfg/403food.names"
        self.imgsz = (320, 192)
        
        df = pd.read_excel("data/excel/food_name.xlsx")
        food_dict = {'00000000':"dish"}
        food_ids = list(df.iloc[1:,-2])
        food_names = list(df.iloc[1:,-1])
        for i,n in zip(food_ids, food_names):
            food_dict[i] = n.replace(" ","")


        cal_df = pd.read_csv("data/excel/food_cals.csv",index_col=0)
        self.cal_dict = {}
        
        for n,c in zip(cal_df.name, cal_df.kcal):
            self.cal_dict[n] = c
        
        
        
        self.model = Darknet(self.cfg,self.imgsz)
        self.model.load_state_dict(torch.load(self.weights)['model'], strict=False)
        self.model.eval()
        
        
        with open(self.names, "r") as f:
            ff = f.read().split("\n")
            self.names_dict = {i:food_dict.get(j.replace(" ",""),j) for i,j in enumerate(ff)}
        
        print("load done")
        
    def detect(self, img):
        im0 = img.copy()
        img_height, img_width,_ = img.shape
        gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  #  normalization gain whwh

        small_img = cv2.resize(img, self.imgsz)

        img = small_img[:, :, ::-1].transpose(2, 0, 1)  # BGR to RGB, to 3x416x416

        img = np.ascontiguousarray(img)

        img = torch.from_numpy(img)
        img = (img.float()/255).unsqueeze(0)
        print(img.shape)
        pred = self.model(img)[0]
        pred = non_max_suppression(pred,0.3, 0.45)


        result = []
        if pred[0] != None:
            for lx,ly,rx,ry,s,c in pred[0]:
                lx = int(img_width/self.imgsz[0]*lx)
                rx = int(img_width/self.imgsz[0]*rx)
                ly = int(img_height/self.imgsz[1]*ly)
                ry = int(img_height/self.imgsz[1]*ry)
                n = self.names_dict[int(c)]
                cal = self.cal_dict.get(n,0)
                result.append([lx,ly,rx,ry,float(s),n, cal])
        return result
                
                
                
                


class Quantity:
    def __init__(self, weight_path="data/weights/new_opencv_ckpt_b84_e200.pth"):
        checkpoint = torch.load(weight_path,map_location="cpu")
        model = checkpoint['model_ft']
        model.load_state_dict(checkpoint['state_dict'], strict=False)

        for param in model.parameters():
            param.requires_grad = False
            
        self.model = model
        self.device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

        
        image_size = 224
        norm_mean = [0.485, 0.456, 0.406]
        norm_std = [0.229, 0.224, 0.225]
    
    def predict(self, img):
        img = Image.fromarray(img)
        img = self.process_image(img)
        img = np.expand_dims(img, 0)

        img = torch.from_numpy(img)

        self.model.eval()
        inputs = Variable(img).to(self.device)
        logits = self.model.forward(inputs)

        ps = F.softmax(logits, dim=1)
        topk = ps.cpu().topk(5)

        probs, classes = (e.data.numpy().squeeze().tolist() for e in topk)
        class_names = ['Q1', 'Q2', 'Q3', 'Q4', 'Q5']
        print(classes, probs)
        return classes[0]+1, probs[0]
    
    

        
    def process_image(self,image):
        preprocess = transforms.Compose([
            transforms.Resize(256),
            transforms.CenterCrop(224),
            transforms.ToTensor(),
            transforms.Normalize(mean=[0.485, 0.456, 0.406],
                                 std=[0.229, 0.224, 0.225])])
        image = preprocess(image)
        return image
             