package kr.ryan.tenserflowwithimage.data

/**
 * TenserflowWithImage
 * Class: HttpResult
 * Created by pyg10.
 * Created On 2021-09-06.
 * Description:
 */
sealed class HttpResult {

    data class CommunicationSuccess(val result: String) : HttpResult()
    data class CommunicationFailure(val error: Throwable) : HttpResult()

}
