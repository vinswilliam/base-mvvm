package co.id.githubfinder.user.data

import co.id.githubfinder.api.BaseDataSource
import co.id.githubfinder.api.UserGithubService
import co.id.githubfinder.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class UserGithubRemoteDataSource @Inject constructor(private val service: UserGithubService) :
  BaseDataSource() {

  suspend fun fetchSets(page: Int, query: String? = null) = getResult { service.getUser(query, page) }

}