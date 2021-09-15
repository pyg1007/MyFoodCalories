package kr.ryan.tenserflowwithimage.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.ryan.tenserflowwithimage.repository.HttpRepository
import kr.ryan.tenserflowwithimage.viewmodel.HttpViewModel

/**
 * TenserflowWithImage
 * Class: HttpViewModelFactory
 * Created by pyg10.
 * Created On 2021-09-06.
 * Description:
 */
class HttpViewModelFactory(private val repository: HttpRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HttpViewModel::class.java)) return HttpViewModel(repository) as T
        throw IllegalStateException("Unknown ViewModel Class")
    }
}