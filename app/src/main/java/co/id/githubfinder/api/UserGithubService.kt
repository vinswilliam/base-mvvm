package co.id.githubfinder.api

import co.id.githubfinder.user.data.UserGithub
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserGithubService {

  companion object {
    const val ENDPOINT = "https://api.github.com/"
  }

  @GET("search/users")
  suspend fun getUser(
    @Query("q") q: String? = null,
    @Query("page") page: Int): Response<ResultsResponse<UserGithub>>
}