package kr.ryan.myfoodcalorie.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
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
import kr.ryan.myfoodcalorie.ui.dialogfragment.LoadingDialogFragment
import kr.ryan.myfoodcalorie.viewmodel.FoodImageMachineLeaningViewModel
import kr.ryan.retrofitmodule.NetWorkResult
import kr.ryan.tedpermissionmodule.requireTedPermission
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object{
        const val FILE_NAME = "photo.jpg"
        const val APP_NAME = "Food"
    }

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
                initBinding()
                selectImage()
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                observeNetWorkResult()

            }

        }
    }

    private fun initBinding(){
        binding.apply {
            viewModel = foodImageMachineLeaningViewModel
            lifecycleOwner = this@MainActivity
        }
    }

    @SuppressLint("CheckResult")
    private fun selectImage() {
        binding.ivFoodImage.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                requireTedPermission({

                    saveBitmapToFile((ResourcesCompat.getDrawable(resources, R.drawable.babgook, null) as? BitmapDrawable)?.bitmap)?.let {
                        fileToMultipartBody(it)?.let {body->
                            foodImageMachineLeaningViewModel.requestMachineLeaning(body)
                        }
                    }

//                    TedRxBottomPicker.with(this@MainActivity)
//                        .show()
//                        .subscribe({ uri ->
//
//                            uriToFile(uri)?.let {
//                                fileToMultipartBody(it)?.let {body->
//                                    foodImageMachineLeaningViewModel.requestMachineLeaning(body)
//                                }
//                                showFoodImage(it)
//                                Timber.d(it.toString())
//                            }
//                        }, Throwable::printStackTrace)
                }, {

                }, *permissions)

            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap?): File?{
        return runCatching {
            val mediaFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_NAME)
            if (!mediaFile.exists() and !mediaFile.mkdirs()) {
                Timber.d("Fail Create File")
            }
            val file = File(mediaFile.path + File.separator + FILE_NAME)
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            file
        }.getOrNull()
    }

    private fun uriToFile(uri: Uri): File? {
        return runCatching {
            File(uri.path.toString())
        }.getOrNull()
    }

    private fun fileToMultipartBody(file: File): MultipartBody.Part? {
        return runCatching {
            MultipartBody.Part.createFormData(
                "data",
                file.name,
                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        }.getOrNull()
    }

    private fun showFoodImage(uri: File) = CoroutineScope(Dispatchers.Main).launch {
        Glide.with(this@MainActivity).load(uri).into(binding.ivFoodImage)
    }

    private suspend fun observeNetWorkResult() {
        foodImageMachineLeaningViewModel.networkStatus.collect {
            when (it) {
                is NetWorkResult.Loading -> {
                    showLoadingDialog()
                }
                is NetWorkResult.ApiError -> {
                    Timber.d("${it.code} ${it.message}")
                    dismissLoadingDialog()
                    showToastMessage(it.code, it.message)
                }
                is NetWorkResult.NetWorkError -> {
                    Timber.d(it.throwable.message.toString())
                    dismissLoadingDialog()
                    showToastMessage(it.throwable.message.toString())
                }
                is NetWorkResult.NullResult -> {
                    Timber.d("Result is Null")
                    dismissLoadingDialog()
                    showToastMessage("Result is Null")
                }
                is NetWorkResult.Success -> {
                    it.data.data?.forEach { machineLeaning->
                        Timber.d("${machineLeaning.name} ${machineLeaning.people} ${machineLeaning.calorie}")
                    }
                    dismissLoadingDialog()
                }
                else -> {
                }
            }
        }
    }

    private fun showLoadingDialog() {
        LoadingDialogFragment.newInstance().show(supportFragmentManager, "Loading")
    }

    private fun dismissLoadingDialog() {
        (supportFragmentManager.findFragmentByTag("Loading") as? LoadingDialogFragment)?.dismiss()
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun showToastMessage(code: Int, message: String) {
        Toast.makeText(applicationContext, "Error Code: $code Cause: $message", Toast.LENGTH_SHORT)
            .show()
    }

    private fun showLogMessage(message: String) {
        Timber.d(message)
    }
}