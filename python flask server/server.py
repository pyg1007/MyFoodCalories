from flask import Flask, request, send_file
import numpy as np
import cv2
import datetime
import time
import glob
import os
from ml_modules import detector


# Initialize the Flask application
app = Flask(__name__)
count = 0
cat = cv2.imread("../socket/small.jpg")


yolo = detector.Detector()
quantity = detector.Quantity()

@app.route('/')
def hello():
    return 'hello'

# route http posts to this method
@app.route('/api/test', methods=['POST'])
def test():
    global count
    count += 1
    print(f"{count} post [{datetime.datetime.now()}]")
    
    st = time.time()
    r = request
    
    try:
        file = r.files['data']
        
        M = datetime.datetime.now().month
        D = datetime.datetime.now().day
        h = datetime.datetime.now().hour
        m = datetime.datetime.now().minute
        s = datetime.datetime.now().second
        
        
        img_name = f"{M:02}{D:02}_{h:02}{m:02}{s:02}.jpg"
        file.save(f"recieve/{img_name}")
        print(f"save file {img_name}")
        
        img = cv2.imread(f"recieve/{img_name}")
        h,w,c = img.shape
        print(f"{count} done {time.time() - st:.2f}s [{datetime.datetime.now()}]")
        return {'data' : f"{img_name}"}
    
    except Exception as e:
        print(e)
        return {'data' : "fuck"}
    

@app.route('/predict', methods=['POST'])
def predict():
    global yolo
    global count
    global quantity
    
    count += 1
    print("predict start")
    
    st = time.time()
    r = request
    data = []
    code = 200
    try:
        file = r.files['data']
        
        M = datetime.datetime.now().month
        D = datetime.datetime.now().day
        h = datetime.datetime.now().hour
        m = datetime.datetime.now().minute
        s = datetime.datetime.now().second
        
        
        img_name = f"{M:02}{D:02}_{h:02}{m:02}{s:02}.jpg"
        file.save(f"recieve/{img_name}")
        print(f"save file {img_name}")
        
        img = cv2.imread(f"recieve/{img_name}")
        result = yolo.detect(img)
        
        
        for lx,ly,rx,ry,s,n,cal in result:
            if n == "dish" or n =="spoon":
                continue
            cv2.rectangle(img, (lx,ly),(rx,ry),(255,0,255),2)
            food = img[ly:ry, lx:rx]
            q,s = quantity.predict(food)
            
            d_dict = {'name':n, 'people':q, 'calorie':str(cal*q)+"kcal"}
            data.append(d_dict)
        
        cv2.imwrite(f"result/{img_name}",img)
        
        print(f"{count} done {time.time() - st:.2f}s [{datetime.datetime.now()}]")
    
    except Exception as e:
        code = 500
        print(e)
    
    return {"code":0, "data":data }
    
@app.route("/recieve/<string:img_name>")
def show_user_profile(img_name):
    return send_file(f"./recieve/{img_name}")


@app.route("/result/<string:img_name>")
def img_process(img_name):
    global cat    
    img = cv2.imread(f"./recieve/{img_name}")
    img = cv2.putText(img, "Return IMG", (max(0,int(img.shape[1]/2-200)), int(img.shape[0]/2)),cv2.FONT_HERSHEY_TRIPLEX, 3,(0,0,0),3)
    ih,iw,_ = img.shape
    
    cat_h = int(ih/5)
    cat_w = int(iw/5)
    resize_cat = cv2.resize(cat,(cat_w,cat_h))
    
    img[:cat_h,:cat_w] = resize_cat
    cv2.imwrite(f"./result/{img_name}",img)
    return send_file(f"./result/{img_name}")

@app.route("/remove/<string:img_name>")
def remove(img_name):
    for i in glob.glob(f"./*/{img_name}"):
        os.remove(i)
    return f"remove {img_name} sucess"


# start flask app
app.run(host="192.168.0.5", port=9000)