package kr.ryan.mycalories.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.ryan.mycalories.repository.HttpRepository
import kr.ryan.mycalories.usecase.HttpConnectionUseCase
import kr.ryan.mycalories.viewmodel.HttpViewModel

/**
 * TenserflowWithImage
 * Class: HttpViewModelFactory
 * Created by pyg10.
 * Created On 2021-09-06.
 * Description:
 */
class HttpViewModelFactory(private val useCase: HttpConnectionUseCase): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HttpViewModel::class.java)) return HttpViewModel(useCase) as T
        throw IllegalStateException("Unknown ViewModel Class")
    }
}