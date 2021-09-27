package kr.ryan.baseui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

/**
 * MyFoodCalorie
 * Class: BaseDialogFragment
 * Created by pyg10.
 * Created On 2021-09-26.
 * Description:
 */

abstract class BaseDialogFragment<VDB: ViewDataBinding>(@LayoutRes val layoutRes: Int) : DialogFragment() {

    protected lateinit var binding: VDB
        private set

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