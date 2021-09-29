package kr.ryan.myfoodcalorie.repository

import androidx.annotation.WorkerThread
import kr.ryan.myfoodcalorie.data.RootRemoteMachineLeaning
import kr.ryan.myfoodcalorie.retrofit.MachineLeaningService
import kr.ryan.retrofitmodule.NetWorkResult
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * MyFoodCalorie
 * Class: MachineLeaningRepository
 * Created by pyg10.
 * Created On 2021-09-26.
 * Description:
 */
class MachineLeaningRepository @Inject constructor(
    private val machineLeaningService: MachineLeaningService
) {

    @WorkerThread
    suspend fun provideMachineLeaningResult(param: MultipartBody.Part) : NetWorkResult<RootRemoteMachineLeaning>
       = machineLeaningService.machineLeaning(param)


}