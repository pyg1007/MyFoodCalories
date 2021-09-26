package kr.ryan.myfoodcalorie.retrofit

import kr.ryan.myfoodcalorie.data.RemoteMachineLeaning
import kr.ryan.retrofitmodule.NetWorkResult
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * MyFoodCalorie
 * Class: MachineLeaningService
 * Created by pyg10.
 * Created On 2021-09-26.
 * Description:
 */
interface MachineLeaningService {

    @Multipart
    @POST("api/test")
    suspend fun machineLeaning(@Part param: MultipartBody.Part): NetWorkResult<RemoteMachineLeaning>

}