package kr.ryan.baseui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * MyFoodCalorie
 * Class: BaseBottomSheetDialogFragment
 * Created by pyg10.
 * Created On 2021-10-07.
 * Description:
 */
abstract class BaseBottomSheetDialogFragment<VDB: ViewDataBinding>(@LayoutRes private val layoutRes: Int, private val customTheme: Int) : BottomSheetDialogFragment(){

    protected lateinit var binding: VDB
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), customTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}