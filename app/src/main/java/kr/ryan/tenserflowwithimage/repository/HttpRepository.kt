package kr.ryan.tenserflowwithimage.repository

import android.util.Log
import androidx.annotation.WorkerThread
import kr.ryan.tenserflowwithimage.data.HttpResult
import kr.ryan.tenserflowwithimage.retrofit.TensorFlowClient.tensorFlow
import kr.ryan.tenserflowwithimage.retrofit.TensorFlowService
import kr.ryan.tenserflowwithimage.util.BASE_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * TenserflowWithImage
 * Class: HttpRepository
 * Created by pyg10.
 * Created On 2021-09-06.
 * Description:
 */
class HttpRepository {

    @WorkerThread
    suspend fun sendImage(param: MultipartBody.Part, result: (HttpResult) -> Unit){

        runCatching {
            Log.e("Repository", "Active")
            Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build().create(TensorFlowService::class.java).applyTensorFlowToImage(param)
        }.onSuccess { response ->
            Log.e("Repository", "onSuccess")
            result(HttpResult.CommunicationSuccess(response.data))
        }.onFailure {
            Log.e("Repository", "onFailure $it")
            result(HttpResult.CommunicationFailure(it))
        }

    }


}