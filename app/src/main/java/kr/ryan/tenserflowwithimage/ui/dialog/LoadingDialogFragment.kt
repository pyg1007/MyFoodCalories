package kr.ryan.tenserflowwithimage.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.lifecycle.whenResumed
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseDialogFragment
import kr.ryan.tenserflowwithimage.R
import kr.ryan.tenserflowwithimage.databinding.DialogLoadingBinding
import kr.ryan.tenserflowwithimage.util.dialogFragmentResize

/**
 * TenserflowWithImage
 * Class: LoadingDialogFragment
 * Created by pyg10.
 * Created On 2021-09-02.
 * Description:
 */
class LoadingDialogFragment: BaseDialogFragment<DialogLoadingBinding>(R.layout.dialog_loading) {

    companion object{
        @JvmStatic
        fun newInstance() = LoadingDialogFragment()
    }

    init {

        lifecycleScope.launch {

            whenCreated {
                initDialog()
            }

            whenResumed {
                requireContext().dialogFragmentResize(this@LoadingDialogFragment, 0.3f, 0.2f)
            }

        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.NewDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAnimation()
    }

    private fun startAnimation(){
        binding.ivLoading.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.loading))
    }

    private fun stopAnimation(){
        binding.ivLoading.clearAnimation()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        stopAnimation()
    }

    private fun initDialog() {
        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.5f
        }

        dialog?.window?.apply {
            attributes = layoutParams
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }

        isCancelable = false
    }


}