package co.id.githubfinder.user.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import co.id.githubfinder.testing.OpenForTesting
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class UserGithubRepository @Inject constructor(
  private val dao: UserGithubDao,
  private val userGithubRemoteDataSource: UserGithubRemoteDataSource
) {
  fun observePagedUserGithubs(
    connectivityAvailable: Boolean, query: String? = null,
    coroutineScope: CoroutineScope
  ) =
    if (connectivityAvailable) {
      observeRemotePagedSets(query, coroutineScope)
    } else {
      observeLocalPagedSets(query)
    }

  private fun observeLocalPagedSets(query: String? = null): LiveData<PagedList<UserGithub>> {
    val dataSourceFactory = dao.getPagedUsersByLogin(query ?: "")

    return LivePagedListBuilder(
      dataSourceFactory,
      UserGithubPageDataSourceFactory.pageListConfig()
    ).build()
  }

  private fun observeRemotePagedSets(
    query: String? = null,
    ioCoroutineScope: CoroutineScope
  ): LiveData<PagedList<UserGithub>> {
    val dataSourceFactory = UserGithubPageDataSourceFactory(
      query, userGithubRemoteDataSource,
      dao, ioCoroutineScope
    )
    return LivePagedListBuilder(
      dataSourceFactory,
      UserGithubPageDataSourceFactory.pageListConfig()
    ).build()
  }

  companion object {
    @Volatile
    private var instance: UserGithubRepository? = null
    fun getInstance(dao: UserGithubDao, userGithubRemoteDataSource: UserGithubRemoteDataSource) =
      instance ?: synchronized(this) {
        instance ?: UserGithubRepository(dao, userGithubRemoteDataSource).also {
          instance = it
        }
      }
  }
}