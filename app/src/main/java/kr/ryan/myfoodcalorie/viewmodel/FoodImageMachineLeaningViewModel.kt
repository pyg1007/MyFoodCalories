package kr.ryan.myfoodcalorie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ryan.myfoodcalorie.data.RootRemoteMachineLeaning
import kr.ryan.myfoodcalorie.usecase.MachineLeaningUseCase
import kr.ryan.retrofitmodule.NetWorkResult
import okhttp3.MultipartBody
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

    private val _foodTitle = MutableStateFlow("")
    val foodTitle = _foodTitle.asStateFlow()

    private val _foodPeople = MutableStateFlow("")
    val foodPeople = _foodPeople.asStateFlow()

    private val _foodCalorie = MutableStateFlow("")
    val foodCalorie = _foodCalorie.asStateFlow()

    private val _networkStatus = MutableStateFlow<NetWorkResult<RootRemoteMachineLeaning>>(NetWorkResult.Init())
    val networkStatus = _networkStatus.asStateFlow()

    fun requestMachineLeaning(param: MultipartBody.Part) = viewModelScope.launch{
        _networkStatus.emit(NetWorkResult.Loading())
        val result = machineLeaningUseCase.provideMachineLeaningResult(param)
        _networkStatus.emit(result)
    }

    fun changeFoodTitle(title: String) = viewModelScope.launch {
        _foodTitle.emit(title)
    }

    fun changeFoodPeople(people: String) = viewModelScope.launch {
        _foodPeople.emit(people)
    }

    fun changeFoodCalorie(calorie: String) = viewModelScope.launch {
        _foodCalorie.emit(calorie)
    }

}