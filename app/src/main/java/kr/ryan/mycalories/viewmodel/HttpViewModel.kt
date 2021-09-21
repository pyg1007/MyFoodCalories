package kr.ryan.mycalories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ryan.mycalories.data.HttpResponse
import kr.ryan.mycalories.data.UiStatus
import kr.ryan.mycalories.usecase.HttpConnectionUseCase
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * TenserflowWithImage
 * Class: HttpViewModel
 * Created by pyg10.
 * Created On 2021-09-06.
 * Description:
 */
class HttpViewModel @Inject constructor(private val userCase: HttpConnectionUseCase) : ViewModel() {

    private val _uiStatus = MutableStateFlow<UiStatus>(UiStatus.None(Unit))
    val uiStatus = _uiStatus.asStateFlow()

    fun sendImage(param: MultipartBody.Part) = viewModelScope.launch {
        emitEvent(UiStatus.Loading(Unit))


        when(val result = userCase.execute(param)){
            is HttpResponse.Success -> {
                emitEvent(UiStatus.Success(result.data.foodName))
            }
            is HttpResponse.Error -> {
                emitEvent(UiStatus.Error(result.e))
            }
        }

    }

    fun clearEvent() {
        emitEvent(UiStatus.None(Unit))
    }

    private fun emitEvent(value: UiStatus) = viewModelScope.launch {
        _uiStatus.emit(value)
    }

}