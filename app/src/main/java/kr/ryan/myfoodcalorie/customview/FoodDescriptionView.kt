package kr.ryan.myfoodcalorie.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import kr.ryan.myfoodcalorie.R
import kr.ryan.myfoodcalorie.databinding.ViewFoodDescriptionBinding

/**
 * MyFoodCalorie
 * Class: FoodDescriptionView
 * Created by pyg10.
 * Created On 2021-10-02.
 * Description:
 */
class FoodDescriptionView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    private var binding: ViewFoodDescriptionBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.view_food_description,
        this,
        true
    )

    fun setDescription(title: String, description: String) {

        binding.run {
            tvTitle.text = title
            tvDescription.text = description
        }
    }

}