package kr.ryan.mycalories.ui.activity

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseActivity
import kr.ryan.mycalories.R
import kr.ryan.mycalories.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    init {

        lifecycleScope.launch {

            whenCreated {

                delay(2 * 1000L)
                goMainActivity()

            }

        }

    }

    private fun goMainActivity(){
        Intent(this@SplashActivity, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }


}