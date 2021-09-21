package kr.ryan.mycalories.repository

import androidx.annotation.WorkerThread
import kr.ryan.mycalories.data.HttpRemoteData
import kr.ryan.mycalories.data.HttpResponse
import kr.ryan.mycalories.retrofit.Client
import kr.ryan.mycalories.retrofit.MachineLeaningClient
import kr.ryan.mycalories.retrofit.MachineLearningService
import kr.ryan.mycalories.util.BASE_URL
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * TenserflowWithImage
 * Class: HttpRepository
 * Created by pyg10.
 * Created On 2021-09-06.
 * Description:
 */
class HttpRepository @Inject constructor(
    @Client val okHttpClient: OkHttpClient,
    private val machineLearningClient: MachineLeaningClient
    ) {

    @WorkerThread
    suspend fun sendImage(param: MultipartBody.Part): HttpResponse{
        return try {
            val result = machineLearningClient.providerMachineLeaning(okHttpClient).applyTensorFlowToImage(param)
            HttpResponse.Success(result)
        }catch(e: Exception){
            HttpResponse.Error(e)
        }
    }


}