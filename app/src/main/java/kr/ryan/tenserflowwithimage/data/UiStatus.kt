package kr.ryan.tenserflowwithimage.data

/**
 * TenserflowWithImage
 * Class: UiStatus
 * Created by pyg10.
 * Created On 2021-09-02.
 * Description:
 */
sealed class UiStatus {

    data class None(val value: Unit): UiStatus()
    data class Loading(val value: Unit): UiStatus()
    data class Success(val value: String): UiStatus()
    data class Error(val value: Throwable): UiStatus()

}