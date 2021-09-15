package kr.ryan.baseui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * TenserflowWithImage
 * Class: BaseActivity
 * Created by pyg10.
 * Created On 2021-09-01.
 * Description:
 */

abstract class BaseActivity<VDB: ViewDataBinding>(@LayoutRes private val layoutRes: Int): AppCompatActivity() {

    protected lateinit var binding: VDB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

}