package kr.ryan.myfoodcalorie.util

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.window.layout.WindowMetricsCalculator

/**
 * MyFoodCalorie
 * Class: dialogResize
 * Created by pyg10.
 * Created On 2021-09-27.
 * Description:
 */

fun Activity.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
    val size = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    val window = dialogFragment.dialog?.window

    val customWidth = (size.bounds.width() * width).toInt()
    val customHeight = (size.bounds.height() * height).toInt()
    window?.setLayout(customWidth, customHeight)
}