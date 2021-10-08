package kr.ryan.myfoodcalorie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MyFoodCalorie
 * Class: SelectViewModel
 * Created by pyg10.
 * Created On 2021-10-08.
 * Description:
 */
@HiltViewModel
class SelectViewModel @Inject constructor()  : ViewModel() {

    private val _chooseCamera = MutableStateFlow(false)
    val chooseCamera = _chooseCamera.asStateFlow()

    private val _chooseGallery = MutableStateFlow(false)
    val chooseGallery = _chooseGallery.asStateFlow()

    fun onClickCamera() = viewModelScope.launch {
        _chooseCamera.emit(true)
    }

    fun onClickGallery() = viewModelScope.launch {
        _chooseGallery.emit(true)
    }

}