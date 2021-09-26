package kr.ryan.myfoodcalorie.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenCreated
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedRxBottomPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseActivity
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.ActivityMainBinding
import kr.ryan.myfoodcalorie.viewmodel.FoodImageMachineLeaningViewModel
import kr.ryan.myfoodcalorie.viewmodel.GitHubViewModel
import kr.ryan.retrofitmodule.NetWorkResult
import kr.ryan.tedpermissionmodule.requireTedPermission
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val foodImageMachineLeaningViewModel by viewModels<FoodImageMachineLeaningViewModel>()

    private val permissions by lazy {

        when {
            Build.VERSION.SDK_INT <= 29 -> {
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            Build.VERSION.SDK_INT >= 30 -> {
                arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }
            else -> {
                throw IllegalStateException("Unknown Sdk Version")
            }
        }

    }

    init {

        lifecycleScope.launch {

            whenCreated {
                selectImage()
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                observeNetWorkResult()

            }

        }
    }

    @SuppressLint("CheckResult")
    private fun selectImage() {
        binding.ivFoodImage.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                requireTedPermission({
                    TedRxBottomPicker.with(this@MainActivity)
                        .show()
                        .subscribe({ uri ->
                            uriToFile(uri)?.let {
//                                fileToMultipartBody(it)?.let {body->
//                                    foodImageMachineLeaningViewModel.requestMachineLeaning(body)
//                                }
                                showFoodImage(it)
                                Timber.d(it.toString())
                            }
                        }, Throwable::printStackTrace)
                }, {

                }, *permissions)

            }
        }
    }

    private fun uriToFile(uri: Uri): File?{
        return runCatching {
            File(uri.path.toString())
        }.getOrNull()
    }

    private fun fileToMultipartBody(file: File): MultipartBody.Part?{
        return runCatching {
            MultipartBody.Part.createFormData("image", file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
        }.getOrNull()
    }

    private fun showFoodImage(uri: File) = CoroutineScope(Dispatchers.Main).launch {
        Glide.with(this@MainActivity).load(uri).into(binding.ivFoodImage)
    }

    private suspend fun observeNetWorkResult(){
        foodImageMachineLeaningViewModel.networkStatus.collect {
            when(it){
                is NetWorkResult.Loading -> {}
                is NetWorkResult.ApiError -> {}
                is NetWorkResult.NetWorkError -> {}
                is NetWorkResult.NullResult -> {}
                is NetWorkResult.Success -> {}
                else -> {}
            }
        }
    }

    private fun showLogMessage(message: String) {
        Timber.d(message)
    }
}