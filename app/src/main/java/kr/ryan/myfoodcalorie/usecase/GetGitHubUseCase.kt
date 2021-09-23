package kr.ryan.myfoodcalorie.usecase

import kr.ryan.myfoodcalorie.data.RemoteGitHub
import kr.ryan.myfoodcalorie.repository.GitHubRepository
import kr.ryan.retrofitmodule.NetWorkResult
import javax.inject.Inject

/**
 * MyFoodCalorie
 * Class: GetGitHubUseCase
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */

class GetGitHubUseCase @Inject constructor(
    private val repository: GitHubRepository
) {

    suspend fun getGitHubInformation(owner: String) : NetWorkResult<RemoteGitHub> {
        return repository.getGitHubRepository(owner)
    }

}