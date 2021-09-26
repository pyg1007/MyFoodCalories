package kr.ryan.myfoodcalorie.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ryan.myfoodcalorie.usecase.MachineLeaningUseCase
import javax.inject.Inject

/**
 * MyFoodCalorie
 * Class: FoodImageMachineLeaningViewModel
 * Created by pyg10.
 * Created On 2021-09-26.
 * Description:
 */

@HiltViewModel
class FoodImageMachineLeaningViewModel @Inject constructor(
    private val machineLeaningUseCase: MachineLeaningUseCase
) : ViewModel() {


    

}