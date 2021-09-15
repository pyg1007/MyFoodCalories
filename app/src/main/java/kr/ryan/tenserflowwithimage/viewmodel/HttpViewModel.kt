package kr.ryan.tenserflowwithimage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ryan.tenserflowwithimage.data.HttpResult
import kr.ryan.tenserflowwithimage.data.UiStatus
import kr.ryan.tenserflowwithimage.repository.HttpRepository
import okhttp3.MultipartBody

/**
 * TenserflowWithImage
 * Class: HttpViewModel
 * Created by pyg10.
 * Created On 2021-09-06.
 * Description:
 */
class HttpViewModel(private val repository: HttpRepository) : ViewModel() {

    private val _uiStatus = MutableStateFlow<UiStatus>(UiStatus.None(Unit))
    val uiStatus = _uiStatus.asStateFlow()

    fun sendImage(param: MultipartBody.Part) = viewModelScope.launch {
        emitEvent(UiStatus.Loading(Unit))

        repository.sendImage(param){
            when(it){
                is HttpResult.CommunicationSuccess -> {
                    emitEvent(UiStatus.Success(it.result))
                }
                is HttpResult.CommunicationFailure -> {
                    emitEvent(UiStatus.Error(it.error))
                }
            }
        }
    }

    fun clearEvent(){
        emitEvent(UiStatus.None(Unit))
    }

    private fun emitEvent(value: UiStatus) = viewModelScope.launch{
        _uiStatus.emit(value)
    }

}