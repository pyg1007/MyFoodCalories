package kr.ryan.myfoodcalorie.usecase

import kr.ryan.myfoodcalorie.data.RemoteMachineLeaning
import kr.ryan.myfoodcalorie.repository.MachineLeaningRepository
import kr.ryan.retrofitmodule.NetWorkResult
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * MyFoodCalorie
 * Class: MachineLeaningUseCase
 * Created by pyg10.
 * Created On 2021-09-26.
 * Description:
 */

class MachineLeaningUseCase @Inject constructor(
    private val repository: MachineLeaningRepository
){

    suspend fun provideMachineLeaningResult(param: MultipartBody.Part) : NetWorkResult<RemoteMachineLeaning>
       = repository.provideMachineLeaningResult(param)


}