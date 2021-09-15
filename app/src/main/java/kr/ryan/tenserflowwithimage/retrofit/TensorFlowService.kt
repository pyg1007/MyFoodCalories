package kr.ryan.tenserflowwithimage.retrofit

import kr.ryan.tenserflowwithimage.data.HttpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

/**
 * TenserflowWithImage
 * Class: TensorFlowService
 * Created by pyg10.
 * Created On 2021-09-03.
 * Description:
 */
interface TensorFlowService {

    @Multipart
    @POST("api/test")
    suspend fun applyTensorFlowToImage(@PartMap param: HashMap<String, RequestBody>) : HttpResponse

    @Multipart
    @POST("api/test")
    suspend fun applyTensorFlowToImage(@Part param: MultipartBody.Part) : HttpResponse

}