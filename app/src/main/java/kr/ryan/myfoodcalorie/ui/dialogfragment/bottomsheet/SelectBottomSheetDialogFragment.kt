package kr.ryan.myfoodcalorie.ui.dialogfragment.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseBottomSheetDialogFragment
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.BottomSheetDialogSelectBinding
import kr.ryan.myfoodcalorie.viewmodel.SelectViewModel
import timber.log.Timber

/**
 * MyFoodCalorie
 * Class: SelectBottomSheetFragment
 * Created by pyg10.
 * Created On 2021-10-07.
 * Description:
 */
@AndroidEntryPoint
class SelectBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<BottomSheetDialogSelectBinding>(
        R.layout.bottom_sheet_dialog_select, R.style.NewDialog
    ) {

    companion object {

        private lateinit var INSTANCE: SelectBottomSheetDialogFragment
        private var cameraListener: (() -> Unit)? = null
        private var galleryListener: (() -> Unit)? = null

        fun newInstance(camera: () -> Unit, gallery: () -> Unit): SelectBottomSheetDialogFragment {
            return if (::INSTANCE.isInitialized) {
                INSTANCE
            } else {
                INSTANCE = SelectBottomSheetDialogFragment()
                cameraListener = camera
                galleryListener = gallery
                INSTANCE
            }
        }
    }

    private var selectViewModel: SelectViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Select Dialog View Created")
        observeCompleteView(view)
        initBinding()
        disableDrag()
        observeCameraStatus()
        observeGalleryStatus()
    }

    private fun observeCompleteView(view: View){
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                Timber.d("observe View")

                val size = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(requireActivity())
                val resizeLayout = binding.rootConstLayout.layoutParams
                resizeLayout.height = (size.bounds.height() * 0.3f).toInt()
                binding.rootConstLayout.layoutParams = resizeLayout
            }
        })
    }

    private fun initViewModel(){
        selectViewModel = ViewModelProvider(this)[SelectViewModel::class.java]
    }

    private fun initBinding() {
        binding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = selectViewModel
        }
    }

    private fun observeCameraStatus() = CoroutineScope(Dispatchers.Default).launch{
        selectViewModel?.chooseCamera?.collect {
            if (it){
                cameraListener?.invoke()
                dismiss()
            }
        }
    }

    private fun observeGalleryStatus() = CoroutineScope(Dispatchers.Default).launch{
        selectViewModel?.chooseGallery?.collect {
            if (it){
                galleryListener?.invoke()
                dismiss()
            }
        }
    }

    private fun disableDrag() {
        (dialog as BottomSheetDialog).behavior.isDraggable = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        INSTANCE.view?.viewTreeObserver?.addOnGlobalLayoutListener(null)
        selectViewModel = null
    }
}