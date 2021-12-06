# MyFoodCalories

## Kotilin Android Client

인스타그램, 페이스북에 올릴 음식사진들을 많이 찍게되는데, 해당 음식들의 칼로리를 알아보기위해서 제작된 어플리케이션입니다.  

<img src = https://user-images.githubusercontent.com/8927650/141714481-97f75129-5166-405b-9b47-f20da251ebb1.jpg width = "30%" height = "30%">
<img src = https://user-images.githubusercontent.com/8927650/141714510-890bc486-55f4-47f1-bcb9-013c621cea70.jpg width = "30%" height = "30%">

사진을 촬영하거나, 갤러리에 존재하는 사진을 선택하게되면 딥러닝을 통해 결과를 리턴받아 출력해줍니다.

### 사용 기술
>- DI(Hilt)
>- Retrofit2
>- Mvvm(with repository, usecase)
    
      
     
-------------------------------
    

## Python Server(with DL)

Flask 기반 Rest-api 서버로 객체 탐지 및 분류 하여 결과를 리턴해줍니다.

### 사용기술
>- Flask
>- Pytorch
>   + Yolov3 (객체 탐지 및 분류)
>   + ResNet (음식양추정)
>- OpenCV