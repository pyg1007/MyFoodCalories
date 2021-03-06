package kr.ryan.myfoodcalorie.ui.dialogfragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.lifecycle.whenResumed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.ryan.baseui.BaseDialogFragment
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.DialogLoadingBinding
import kr.ryan.myfoodcalorie.util.dialogFragmentResize

/**
 * MyFoodCalorie
 * Class: LoadingDialogFragment
 * Created by pyg10.
 * Created On 2021-09-26.
 * Description:
 */
@AndroidEntryPoint
class LoadingDialogFragment : BaseDialogFragment<DialogLoadingBinding>(R.layout.dialog_loading) {

    companion object{
        @JvmStatic
        fun newInstance(): LoadingDialogFragment{
            return LoadingDialogFragment()
        }
    }

    init {

        lifecycleScope.launch {

            whenCreated {
                initDialog()
            }

            whenResumed {
                requireActivity().dialogFragmentResize(this@LoadingDialogFragment, 0.3f, 0.2f)
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.NewDialog)
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