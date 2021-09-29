package kr.ryan.myfoodcalorie.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.ryan.myfoodcalorie.util.MACHINE_LEANING_BASE_URL
import kr.ryan.retrofitmodule.ResponseAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * MyFoodCalorie
 * Class: MachineLeaningRetrofitModule
 * Created by pyg10.
 * Created On 2021-09-26.
 * Description:
 */
@Module
@InstallIn(SingletonComponent::class)
object MachineLeaningRetrofitModule {

    @Provides
    fun provideMachineLeaningRetrofit(
        client: OkHttpClient
    ): MachineLeaningService{
        return Retrofit.Builder()
            .baseUrl(MACHINE_LEANING_BASE_URL)
            .addCallAdapterFactory(ResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MachineLeaningService::class.java)
    }

}