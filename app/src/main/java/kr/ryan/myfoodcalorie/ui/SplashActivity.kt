package kr.ryan.myfoodcalorie.ui

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseActivity
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.ActivitySplashBinding

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    init {

        lifecycleScope.launch {


            whenCreated {

                delay(1 * 1000L)
                Intent(this@SplashActivity, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }

            }


        }

    }


}