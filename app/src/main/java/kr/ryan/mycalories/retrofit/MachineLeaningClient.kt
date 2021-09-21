package kr.ryan.mycalories.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.ryan.mycalories.util.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Class: TensorFlowClient
 * Created by pyg10.
 * Created On 2021-09-03.
 * Description:
 */


@Module
@InstallIn(SingletonComponent::class)
object MachineLeaningClient {

    @Provides
    fun providerMachineLeaning(
        @Client okHttpClient: OkHttpClient
    ): MachineLearningService {

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(
                okHttpClient
            ).build().create(MachineLearningService::class.java)

    }

}