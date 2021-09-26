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
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseActivity
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.ActivityMainBinding
import kr.ryan.myfoodcalorie.viewmodel.GitHubViewModel
import kr.ryan.tedpermissionmodule.requireTedPermission
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
                selectImage()
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //observeNetWorkResult()

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
                            showFoodImage(uri)
                            Timber.d(uri.toString())
                        }, Throwable::printStackTrace)
                }, {

                }, *permissions)

            }
        }
    }

    private fun showFoodImage(uri: Uri) = CoroutineScope(Dispatchers.Main).launch {
        Glide.with(this@MainActivity).load(uri).into(binding.ivFoodImage)
    }


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

//    private suspend fun observeNetWorkResult() {
//        gitHubViewModel.netWorkResult.collect { result ->
//            when (result) {
//                is NetWorkResult.Init -> {
//                    showLogMessage("Init")
//                }
//                is NetWorkResult.Success -> {
//                    result.data.also {
//                        showLogMessage("${it.date} ${it.id} ${it.date} ${it.url}")
//                    }
//                }
//                is NetWorkResult.ApiError -> {
//                    showLogMessage("Api Error ${result.message} code : ${result.code}")
//                }
//                is NetWorkResult.NullResult -> {
//                    showLogMessage("Null Error")
//                }
//                is NetWorkResult.NetWorkError -> {
//                    showLogMessage("NetWork Error ${result.throwable.message}")
//                }
//                is NetWorkResult.Loading -> {
//                    showLogMessage("Loading")
//                }
//            }
//
//        }
//    }

    private fun showLogMessage(message: String) {
        Timber.d(message)
    }
}