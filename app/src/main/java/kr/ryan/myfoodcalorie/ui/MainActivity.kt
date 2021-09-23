package kr.ryan.myfoodcalorie.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.viewmodel.GitHubViewModel
import kr.ryan.retrofitmodule.NetWorkResult
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val gitHubViewModel by viewModels<GitHubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            gitHubViewModel.netWorkResult.collect { result ->
                when(result){
                    is NetWorkResult.Init -> {
                        showLogMessage("Init")
                    }
                    is NetWorkResult.Success -> {
                        result.data.also {
                            showLogMessage("${it.date} ${it.id} ${it.date} ${it.url}")
                        }
                    }
                    is NetWorkResult.ApiError -> {
                        showLogMessage("Api Error ${result.message} code : ${result.code}")
                    }
                    is NetWorkResult.NullResult -> {
                        showLogMessage("Null Error")
                    }
                    is NetWorkResult.NetWorkError -> {
                        showLogMessage("NetWork Error ${result.throwable.message}")
                    }
                    is NetWorkResult.Loading -> {
                        showLogMessage("Loading")
                    }
                }
            }

        }

    }

    private fun showLogMessage(message: String){
        Timber.d(message)
    }
}