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
 * TenserflowWithImage
 * Class: BaseDialogFragment
 * Created by pyg10.
 * Created On 2021-09-02.
 * Description:
 */
abstract class BaseDialogFragment<VDB: ViewDataBinding>(@LayoutRes private val layoutRes: Int): DialogFragment() {

    private var _binding: VDB? = null
    protected val binding: VDB
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}