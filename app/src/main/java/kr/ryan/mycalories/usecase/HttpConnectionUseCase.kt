package kr.ryan.mycalories.usecase

import kr.ryan.mycalories.data.HttpLocalData
import kr.ryan.mycalories.data.HttpRemoteData
import kr.ryan.mycalories.data.HttpResponse
import kr.ryan.mycalories.repository.HttpRepository
import okhttp3.MultipartBody

/**
 * MyFoodCalories
 * Class: HttpConnectionUseCase
 * Created by pyg10.
 * Created On 2021-09-19.
 * Description:
 */
class HttpConnectionUseCase(private val repository: HttpRepository) {

    suspend fun execute(parameter: MultipartBody.Part): Any {
        return when (val result = repository.sendImage(parameter)){
            is HttpResponse.Success -> {
                HttpLocalData(result.data.foodName, result.data.manyPeople, result.data.calories, null)
            }
            is HttpResponse.Error -> {
                HttpLocalData("","", "", result.e)
            }
        }
    }
}