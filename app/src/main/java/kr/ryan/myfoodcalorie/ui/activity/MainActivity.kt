package kr.ryan.myfoodcalorie.ui.activity

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
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

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object {
        const val FILE_NAME = "photo.jpg"
        const val APP_NAME = "Food"
    }

    private val foodImageMachineLeaningViewModel by viewModels<FoodImageMachineLeaningViewModel>()

    private val permissions by lazy {
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private lateinit var captureImageLauncher: ActivityResultLauncher<Intent>
    private var imageFile: File? = null

    init {

        lifecycleScope.launch {

            whenCreated {
                initLauncher()
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

    private fun initLauncher() {
        captureImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    imageFile?.let { file ->
                        CoroutineScope(Dispatchers.Default).launch {
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
        runCatching {
            val cursor = getImage()
            showLogMessage("$cursor")
            cursor?.let {
                if (it.moveToFirst()) {
                    val id =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID))
                    showLogMessage("${ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)}")
                } else {
                    showLogMessage("Empty Image File")
                }
            }
            cursor?.close()
        }.onFailure {
            showLogMessage("$it")
        }
    }

    private fun getImage(): Cursor? = contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendQueryParameter("limit", "1")
            .build(), arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.TITLE,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        ), null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
    )

    private fun getFileUri(): File? {
        return runCatching {
            val mediaFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_NAME)
            if (!mediaFile.exists() and !mediaFile.mkdirs()) {
                showLogMessage("Fail Create File")
            }
            File(mediaFile.path + File.separator + FILE_NAME)
        }.getOrNull()
    }

    private fun showFoodImage(uri: File) = CoroutineScope(Dispatchers.Main).launch {
        Glide.with(this@MainActivity).load(uri).into(binding.ivFoodImage)
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
                    dismissLoadingDialog()
                }
                is NetWorkResult.NetWorkError -> {
                    showLogMessage(it.throwable.message.toString())
                    dismissLoadingDialog()
                }
                is NetWorkResult.NullResult -> {
                    dismissLoadingDialog()
                }
                is NetWorkResult.Success -> {
                    it.data.data?.forEach { machineLeaning ->
                        foodImageMachineLeaningViewModel.run {
                            changeFoodTitle(machineLeaning.name)
                            changeFoodPeople(machineLeaning.people.toString())
                            changeFoodCalorie(machineLeaning.calorie.toString())
                        }
                        foodImageMachineLeaningViewModel.changeFoodInfoVisible(true)
                        showLogMessage("${machineLeaning.name} ${machineLeaning.people} ${machineLeaning.calorie}")
                    }
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