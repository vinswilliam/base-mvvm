package co.id.githubfinder.user.data

import androidx.paging.PageKeyedDataSource
import co.id.githubfinder.data.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UserGithubPageDataSource @Inject constructor(
  private val query: String? = null,
  private val dataSource: UserGithubRemoteDataSource,
  private val dao: UserGithubDao,
  private val scope: CoroutineScope) : PageKeyedDataSource<Int, UserGithub>() {

  override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UserGithub>
  ) {
    fetchData(1, params.requestedLoadSize) {
      callback.onResult(it, null, 2)
    }
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserGithub>) {
    val page = params.key
    fetchData(page, params.requestedLoadSize) {
      callback.onResult(it, page + 1)
    }
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserGithub>) {
    val page = params.key
    fetchData(page, params.requestedLoadSize) {
      callback.onResult(it, page - 1)
    }
  }

  private fun fetchData(page: Int, pageSize: Int, callback: (List<UserGithub>) -> Unit) {
    scope.launch(getJobErrorHandler()) {
      val response = dataSource.fetchSets(page, query)
      if (response.status == Result.Status.SUCCESS) {
        val results = response.data!!.items
        dao.insertAll(results)
        callback(results)
      } else if (response.status == Result.Status.ERROR) {
        postError(response.message!!)
      }
    }
  }

  private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
    postError(e.message ?: e.toString())
  }

  private fun postError(message: String) {
    Timber.e("An error happened: $message")
  }
}

