package kr.ryan.retrofitmodule

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * MyFoodCalorie
 * Class: ResponseAdapter
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */
class ResponseAdapter<T> constructor(
    private val successType: Type
) : CallAdapter<T, Call<NetWorkResult<T>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<NetWorkResult<T>> = NetWorkResponseCall(call)
}