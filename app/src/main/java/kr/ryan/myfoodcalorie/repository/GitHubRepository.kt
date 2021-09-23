package kr.ryan.myfoodcalorie.repository

import androidx.annotation.WorkerThread
import dagger.assisted.Assisted
import kr.ryan.myfoodcalorie.data.RemoteGitHub
import kr.ryan.myfoodcalorie.retrofit.GitHubService
import kr.ryan.retrofitmodule.NetWorkResult
import javax.inject.Inject

/**
 * MyFoodCalorie
 * Class: GitHubRepository
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */
class GitHubRepository @Inject constructor(
    private val service: GitHubService
) {

    @WorkerThread
    suspend fun getGitHubRepository(owner: String) : NetWorkResult<RemoteGitHub>{
        return service.getGitHubRepos(owner)
    }

}