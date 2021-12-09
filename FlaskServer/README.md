# Python Flask Server

## How to use
0. data/weights 에 yolov3 와 resnet weights를 넣는다. (.pt파일)
1. python server.py
    > server.py의 url과 port를 변경하면 됩니다.
2. http://url:port/predict 로 files를 post 하면 연산이 시작되고 칼로리와 음식종류 음식 인분수가 리턴됩니다. 
    >**post : files = {'data':file}**
    >
    >**return json : {'code' : 0, 'data' : [{'calorie' : '100kcal', 'name' : '김치', 'people' : 5}, 'file_name' : 1209_13400.jpg']**
3. http://url:port/recieve/file_name 에서 보낸 원본파일을 확인할 수 있습니다.
4. http://url:port/remove/file_name 으로 저장된 원본파일을 삭제할 수 있습니다.

## Requirements
- torch
- PIL
- OpenCV
- Flask
- numpy
- pandas
