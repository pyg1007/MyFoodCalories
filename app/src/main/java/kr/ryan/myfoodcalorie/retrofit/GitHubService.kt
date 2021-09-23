package kr.ryan.myfoodcalorie.retrofit

import kr.ryan.myfoodcalorie.data.RemoteGitHub
import kr.ryan.retrofitmodule.NetWorkResult
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * MyFoodCalorie
 * Class: GitHubService
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */
interface GitHubService {

    @GET("users/{owner}/repos")
    suspend fun getGitHubRepos(@Path("owner") owner: String) : NetWorkResult<RemoteGitHub>
}