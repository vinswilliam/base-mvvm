package co.id.githubfinder.user.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class UserGithubPageDataSourceFactory @Inject constructor(
  private val query: String? = null,
  private val dataSource: UserGithubRemoteDataSource,
  private val dao: UserGithubDao,
  private val scope: CoroutineScope) : DataSource.Factory<Int, UserGithub>() {

  private val liveData = MutableLiveData<UserGithubPageDataSource>()

  override fun create(): DataSource<Int, UserGithub> {
    val source = UserGithubPageDataSource(query, dataSource, dao, scope)
    liveData.postValue(source)
    return source
  }

  companion object {
    private const val PAGE_SIZE = 100

    fun pageListConfig() = PagedList.Config.Builder()
      .setInitialLoadSizeHint(PAGE_SIZE)
      .setPageSize(PAGE_SIZE)
      .setEnablePlaceholders(true)
      .build()
  }

}