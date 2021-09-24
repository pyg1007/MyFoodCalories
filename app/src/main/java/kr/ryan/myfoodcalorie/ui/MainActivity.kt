package kr.ryan.myfoodcalorie.ui

import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenCreated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseActivity
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.ActivityMainBinding
import kr.ryan.myfoodcalorie.viewmodel.GitHubViewModel
import kr.ryan.retrofitmodule.NetWorkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var captureImage: ActivityResultLauncher<Intent>
    private var imageFile: File? = null
    private val gitHubViewModel by viewModels<GitHubViewModel>()

    init {

        lifecycleScope.launch {

            whenCreated {
                captureImage =
                    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                        if (result.resultCode == RESULT_OK) {
                            imageFile?.let { file ->
                                CoroutineScope(Dispatchers.Default).launch {
                                    val body = MultipartBody.Part.createFormData(
                                        "data",
                                        file.name,
                                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    )
                                    // httpViewModel.sendImage(body)
                                }
                            }
                        }
                    }

            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                observeNetWorkResult()

            }

        }
    }

//    private fun takeCapture() {
//        binding.ivFoodImage.setOnClickListener {
//            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
//                pictureIntent.resolveActivity(packageManager)?.also {
//                    imageFile = runCatching {
//                        getFileUri()
//                    }.getOrNull()
//
//                    imageFile?.let {
//                        val provider = FileProvider.getUriForFile(
//                            this@MainActivity,
//                            "kr.ryan.tenserflowwithimage.fileprovider",
//                            it
//                        )
//                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, provider)
//                    }
//                    captureImage.launch(pictureIntent)
//                }
//            }
//        }
//    }
//
//    private fun getFileUri(): File? {
//        return runCatching {
//            val mediaFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_NAME)
//            if (!mediaFile.exists() and !mediaFile.mkdirs()) {
//                return null
//            }
//
//            File(mediaFile.path + File.separator + CAPTURE_IMAGE_NAME)
//        }.getOrNull()
//    }

    private suspend fun observeNetWorkResult(){
        gitHubViewModel.netWorkResult.collect { result ->
            when (result) {
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

    private fun showLogMessage(message: String) {
        Timber.d(message)
    }
}