package kr.ryan.mycalories.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * MyFoodCalories
 * Class: CoroutineUseCase
 * Created by pyg10.
 * Created On 2021-09-19.
 * Description:
 */
//abstract class CoroutineUseCase<in P, R> {
//
//    protected abstract suspend fun execute(parameter: P) : HttpResult<R?>
//
//    suspend operator fun invoke(parameter: P): HttpResult<R?>?{
//        return runCatching {
//            withContext(Dispatchers.IO){
//                execute(parameter)
//            }
//        }.onSuccess {
//            HttpResult.Success(it)
//        }.onFailure {
//            HttpResult.Error(it)
//        }.getOrNull()
//    }
//
//}