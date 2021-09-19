package kr.ryan.tenserflowwithimage.ui.activity

import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenCreated
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kr.ryan.baseui.BaseActivity
import kr.ryan.tenserflowwithimage.R
import kr.ryan.tenserflowwithimage.data.UiStatus
import kr.ryan.tenserflowwithimage.databinding.ActivityMainBinding
import kr.ryan.tenserflowwithimage.repository.HttpRepository
import kr.ryan.tenserflowwithimage.ui.dialog.LoadingDialogFragment
import kr.ryan.tenserflowwithimage.util.BASE_URL
import kr.ryan.tenserflowwithimage.viewmodel.HttpViewModel
import kr.ryan.tenserflowwithimage.viewmodel.factory.HttpViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var captureImage: ActivityResultLauncher<Intent>
    private var imageFile: File? = null

    private val dialogFragment by lazy {
        LoadingDialogFragment.newInstance()
    }

    private val httpViewModel by viewModels<HttpViewModel> {
        HttpViewModelFactory(
            HttpRepository()
        )
    }

    init {
        lifecycleScope.launch {

            whenCreated {
                captureImage =
                    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                        if (result.resultCode == RESULT_OK) {
                            imageFile?.let { file ->
                                CoroutineScope(Dispatchers.Default).launch {
                                    val body = MultipartBody.Part.createFormData("data", file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
                                    httpViewModel.sendImage(body)
                                }
                            }
                        }
                    }

                takeCapture()

            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                httpViewModel.uiStatus.collect {
                    when (it) {
                        is UiStatus.None -> {
                            //binding.ivCaptureButton.visibility = View.VISIBLE
                        }
                        is UiStatus.Error -> {
                            Log.e(TAG, "Error ${it.value}")
                            dismissDialog()
                            showShortToast(it.value)
                            httpViewModel.clearEvent()
                        }
                        is UiStatus.Success -> {
//                            binding.ivCaptureButton.visibility = View.INVISIBLE
//                            Glide.with(this@MainActivity).load(BASE_URL.plus("result/" + it.value)).error(R.drawable.loading).into(binding.ivCaptureImage)
                            dismissDialog()
                        }
                        is UiStatus.Loading -> {
                            Log.e(TAG, "Loading")
                            showDialog()
                        }
                    }
                }

            }

        }

    }

    private fun takeCapture() {
        binding.ivFoodImage.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
                pictureIntent.resolveActivity(packageManager)?.also {
                    imageFile = runCatching {
                        getFileUri()
                    }.getOrNull()

                    imageFile?.let {
                        val provider = FileProvider.getUriForFile(
                            this@MainActivity,
                            "kr.ryan.tenserflowwithimage.fileprovider",
                            it
                        )
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, provider)
                    }
                    captureImage.launch(pictureIntent)
                }
            }
        }
    }

    private fun getFileUri(): File? {
        return runCatching {
            val mediaFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_NAME)
            if (!mediaFile.exists() and !mediaFile.mkdirs()) {
                Log.e(TAG, "Fail Create File")
                return null
            }

            File(mediaFile.path + File.separator + CAPTURE_IMAGE_NAME)
        }.getOrNull()
    }

    private fun showShortToast(throwable: Throwable) {
        Toast.makeText(applicationContext, "${throwable.message}", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog() {
        dialogFragment.show(supportFragmentManager, "Loading")
    }

    private fun dismissDialog() {
        dialogFragment.dismiss()
    }

    companion object {
        const val TAG = "MainActivity"
        const val CAPTURE_IMAGE_NAME = "photo.jpg"
        const val APP_NAME = "TensorFlow"
    }

}