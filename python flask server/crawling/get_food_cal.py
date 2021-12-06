from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import pandas as pd


df = pd.read_excel("./food.xlsx")

food_ids = df.iloc[1:,-2]
food_names = df.iloc[1:,-1]

driver = webdriver.Chrome("chromedriver.exe")

cal_url = "https://www.dietshin.com/calorie/calorie_main.asp"

driver.get(cal_url)

food_cals = {}
error_list = []

for item in food_names:
    flag = 1
    item = item.replace(" ","")


    search_path = "/html/body/div[1]/div[1]/div[1]/div[2]/form/fieldset/input"
    search_el = driver.find_element_by_xpath(search_path)
    search_el.send_keys(item)

    search_el.send_keys(Keys.ENTER)

    tbody_el = driver.find_element_by_xpath("/html/body/div[1]/div[1]/div[1]/table/tbody")
    for el in tbody_el.find_elements_by_tag_name("tr"):
        name_el,cal_el = el.find_elements_by_tag_name('td')
        name = name_el.text
        name.replace(" ","")
        if item in name:
            cal = cal_el.text
            print(name, cal)
            food_cals[name] = cal
            flag = 0
            break
            
    if flag:
        error_list.append(item)
        print(item, "not found")
        
cal_list= []
for food in food_names:
    cal_list.append(food_cals.get(food,"not found"))
    
new_df = pd.DataFrame({"fid":food_ids.to_list(),"name":food_names.to_list(), "cal":cal_list})

        
new_df.to_csv("food_cals.csv")

driver.close()