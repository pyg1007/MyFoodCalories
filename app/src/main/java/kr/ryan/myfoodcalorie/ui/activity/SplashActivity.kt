package kr.ryan.myfoodcalorie.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseActivity
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.ActivitySplashBinding

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    init {

        lifecycleScope.launch {

            whenCreated {

                Intent(this@SplashActivity, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }

            }

        }

    }


}