package kr.ryan.myfoodcalorie.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ryan.myfoodcalorie.data.RemoteGitHub
import kr.ryan.myfoodcalorie.usecase.GetGitHubUseCase
import kr.ryan.retrofitmodule.NetWorkResult
import javax.inject.Inject

/**
 * MyFoodCalorie
 * Class: GetHubViewModel
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */
@HiltViewModel
class GitHubViewModel @Inject constructor(
    private val useCase: GetGitHubUseCase
) : ViewModel() {

    private val _netWorkResult = MutableStateFlow<NetWorkResult<RemoteGitHub>>(NetWorkResult.Loading())
    val netWorkResult = _netWorkResult.asStateFlow()

    init {

        CoroutineScope(Dispatchers.Main).launch {
            _netWorkResult.emit(useCase.getGitHubInformation())
        }

    }
}