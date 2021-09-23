package kr.ryan.myfoodcalorie.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.ryan.myfoodcalorie.util.BASE_URL
import kr.ryan.retrofitmodule.ResponseAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * MyFoodCalorie
 * Class: RetrofitModule
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofitService(
        client: OkHttpClient
    ): GitHubService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(ResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(GitHubService::class.java)
    }


}