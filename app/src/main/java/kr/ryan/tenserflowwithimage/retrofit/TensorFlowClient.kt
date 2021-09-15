package kr.ryan.tenserflowwithimage.retrofit

import kr.ryan.tenserflowwithimage.BuildConfig
import kr.ryan.tenserflowwithimage.util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * TenserflowWithImage
 * Class: TensorFlowClient
 * Created by pyg10.
 * Created On 2021-09-03.
 * Description:
 */
object TensorFlowClient {

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    val tensorFlow =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
            .client(
                createOkHttpClient()
            ).build().create(TensorFlowService::class.java) ?: null

}