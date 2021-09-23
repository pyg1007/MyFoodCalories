package kr.ryan.retrofitmodule

/**
 * MyFoodCalorie
 * Class: NetWorkResult
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */
sealed class NetWorkResult<T>{

    class Init<T> : NetWorkResult<T>()
    class Success<T>(val data: T, val code: Int) : NetWorkResult<T>()
    class Loading<T> : NetWorkResult<T>()
    class ApiError<T>(val message: String, val code: Int) : NetWorkResult<T>()
    class NetWorkError<T>(val throwable: Throwable) : NetWorkResult<T>()
    class NullResult<T>: NetWorkResult<T>()

}
