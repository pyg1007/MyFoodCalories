package kr.ryan.myfoodcalorie.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseActivity
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.ActivityMainBinding
import kr.ryan.myfoodcalorie.ui.dialogfragment.LoadingDialogFragment
import kr.ryan.myfoodcalorie.ui.dialogfragment.bottomsheet.SelectBottomSheetDialogFragment
import kr.ryan.myfoodcalorie.viewmodel.FoodImageMachineLeaningViewModel
import kr.ryan.retrofitmodule.NetWorkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import kotlin.math.ceil
import kotlin.math.floor

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object {
        const val FILE_NAME = "photo.jpg"
        const val APP_NAME = "Food"
    }

    private val foodImageMachineLeaningViewModel by viewModels<FoodImageMachineLeaningViewModel>()

    private lateinit var captureImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var bringGalleryImageLauncher: ActivityResultLauncher<Intent>
    private var imageFile: File? = null

    init {

        lifecycleScope.launch {

            whenCreated {
                initCaptureLauncher()
                initBringLauncher()
                initBinding()
                selectImage()
                observeFoodName()
                observeFoodPeople()
                observeFoodCalorie()
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                observeNetWorkResult()

            }

        }
    }

    private fun initCaptureLauncher() {
        captureImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    imageFile?.let { file ->
                        val param = MultipartBody.Part.createFormData(
                            "data",
                            file.name,
                            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        )
                        foodImageMachineLeaningViewModel.requestMachineLeaning(param)
                    }
                }
            }
    }

    private fun convertContentUriToFileUri(contentUri: Uri): File? {

        imageFile = getFileUri()

        return runCatching {
            val inputStream = contentResolver.openInputStream(contentUri)
            val fileOutputStream = FileOutputStream(imageFile)
            val buffer = ByteArray(4096)
            inputStream?.let {
                var length = it.read(buffer)
                while (length > 0) {
                    fileOutputStream.write(buffer, 0, length)
                    length = it.read(buffer)
                }
                fileOutputStream.flush()
            }
            imageFile
        }.onFailure {
            showLogMessage(it.message.toString())
        }.getOrNull()

    }

    private fun initBringLauncher() {
        bringGalleryImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.data?.let {
                        showLogMessage(it.path!!)
                        convertContentUriToFileUri(it)?.let { file ->
                            val param = MultipartBody.Part.createFormData(
                                "data",
                                file.name,
                                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            )
                            foodImageMachineLeaningViewModel.requestMachineLeaning(param)
                        }
                    }
                }
            }
    }

    private fun initBinding() {
        binding.apply {
            viewModel = foodImageMachineLeaningViewModel
            lifecycleOwner = this@MainActivity
        }
    }

    private fun checkCameraHardware(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    private fun selectImage() {
        binding.ivFoodImage.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                takeCaptureOrSelectImage()
            }
        }
    }

    private fun takeCaptureOrSelectImage() {
        if (checkCameraHardware()) {
            showSelectBottomSheetDialog()
        } else {
            showLogMessage("this Device haven't CameraHardware")
        }
    }

    private fun showSelectBottomSheetDialog() {
        SelectBottomSheetDialogFragment.newInstance({
            openCamera()
            showLogMessage("Camera")
        }, {
            openGallery()
            showLogMessage("Gallery")
        }).show(supportFragmentManager, "Select")
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { captureIntent ->
            captureIntent.resolveActivity(packageManager)?.also {
                imageFile = getFileUri()
                imageFile?.let {
                    val provider = FileProvider.getUriForFile(
                        this@MainActivity,
                        "kr.ryan.myfoodcalorie.provider",
                        it
                    )

                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, provider)
                    captureImageLauncher.launch(captureIntent)
                }
            }
        }
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            bringGalleryImageLauncher.launch(it)
        }
    }

    private fun getFileUri(): File? {
        return runCatching {
            val mediaFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_NAME)
            if (!mediaFile.exists() and !mediaFile.mkdirs()) {
                showLogMessage("Fail Create File")
            }
            File(mediaFile.path + File.separator + FILE_NAME)
        }.getOrNull()
    }

    private fun showFoodImage(file: File) {
        Glide.with(this@MainActivity).load(file).into(binding.ivFoodImage)
    }

    private fun observeFoodName() = CoroutineScope(Dispatchers.Main).launch {
        foodImageMachineLeaningViewModel.foodTitle.collect {
            binding.fdName.setDescription(resources.getString(R.string.title), it)
        }
    }

    private fun observeFoodPeople() = CoroutineScope(Dispatchers.Main).launch {
        foodImageMachineLeaningViewModel.foodPeople.collect {
            binding.fdPeople.setDescription(resources.getString(R.string.people), it)
        }
    }

    private fun observeFoodCalorie() = CoroutineScope(Dispatchers.Main).launch {
        foodImageMachineLeaningViewModel.foodCalorie.collect {
            binding.fdCalorie.setDescription(resources.getString(R.string.calorie), it)
        }
    }

    private suspend fun observeNetWorkResult() {
        foodImageMachineLeaningViewModel.networkStatus.collect {
            when (it) {
                is NetWorkResult.Loading -> {
                    showLogMessage("Loading")
                    showLoadingDialog()
                }
                is NetWorkResult.ApiError -> {
                    showLogMessage("${it.code} ${it.message}")
                    showToastMessage(it.code, it.message)
                    dismissLoadingDialog()
                }
                is NetWorkResult.NetWorkError -> {
                    showLogMessage(it.throwable.message.toString())
                    showToastMessage(it.throwable.message.toString())
                    dismissLoadingDialog()
                }
                is NetWorkResult.NullResult -> {
                    showLogMessage("Result is null")
                    showToastMessage("Result is null")
                    dismissLoadingDialog()
                }
                is NetWorkResult.Success -> {
                    showLogMessage("Success")
                    it.data.data?.let { machineLeaning ->
                        foodImageMachineLeaningViewModel.run {
                            var people = 0.0
                            var calorie = 0
                            machineLeaning.forEach { remote ->
                                people += remote.people
                                remote.calorie?.let { cal ->
                                    runCatching {
                                        calorie += cal.replace("kcal", "").toInt()
                                    }.onFailure { throwable->
                                        showLogMessage(throwable.message.toString())
                                    }
                                }
                            }

                            people /= machineLeaning.size

                            changeFoodTitle(machineLeaning.joinToString(", ") { join -> join.name })
                            if (floor(people) == ceil(people))
                                changeFoodPeople("${people.toInt()} 인분")
                            else
                                changeFoodPeople("${floor(people).toInt()} ~ ${ceil(people).toInt()} 인분")
                            changeFoodCalorie(calorie.toString() + "kcal")
                        }
                    }

                    imageFile?.let { file ->
                        showFoodImage(file)
                    }

                    foodImageMachineLeaningViewModel.changeFoodInfoVisible(true)
                    dismissLoadingDialog()
                }
                else -> {
                    showLogMessage("Init")
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