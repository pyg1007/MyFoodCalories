package kr.ryan.mycalories.data

/**
 * MyFoodCalories
 * Class: HttpResponse
 * Created by pyg10.
 * Created On 2021-09-21.
 * Description:
 */
sealed class HttpResponse{

    data class Success(val data: HttpRemoteData) : HttpResponse()
    data class Error(val e: Throwable) : HttpResponse()

}
