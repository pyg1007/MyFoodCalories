package kr.ryan.mycalories.data

/**
 * MyFoodCalories
 * Class: HttpLocalData
 * Created by pyg10.
 * Created On 2021-09-21.
 * Description:
 */

data class HttpLocalData(var foodName: String = "", var manyPeople: String = "", var calories: String = "", var error: Throwable? = null)
