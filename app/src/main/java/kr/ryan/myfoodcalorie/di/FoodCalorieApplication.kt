package kr.ryan.myfoodcalorie.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kr.ryan.myfoodcalorie.BuildConfig
import timber.log.Timber

/**
 * MyFoodCalorie
 * Class: FoodCalorieApplication
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */
@HiltAndroidApp
class FoodCalorieApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}