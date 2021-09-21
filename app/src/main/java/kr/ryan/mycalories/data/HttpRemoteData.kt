package kr.ryan.mycalories.data

/**
 * MyFoodCalories
 * Class: RemoteData
 * Created by pyg10.
 * Created On 2021-09-21.
 * Description:
 */
data class HttpRemoteData(
    val responseCode: Int,
    val foodName: String,
    val manyPeople: String,
    val calories: String
)
