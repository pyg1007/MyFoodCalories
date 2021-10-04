package kr.ryan.myfoodcalorie.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.customview.FoodDescriptionView

/**
 * MyFoodCalorie
 * Class: FoodDescriptionBinding
 * Created by pyg10.
 * Created On 2021-10-02.
 * Description:
 */
object FoodDescriptionBinding {

    @JvmStatic
    @BindingAdapter("Visible")
    fun visible(view: View, isVisible: Boolean){
        view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

}