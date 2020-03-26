package co.id.githubfinder.user.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import co.id.githubfinder.di.CoroutineScropeIO
import co.id.githubfinder.user.data.UserGithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class UserGithubViewModel @Inject constructor(
  private val repository: UserGithubRepository,
  @CoroutineScropeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {

  var connectivityAvailable: Boolean = false

  var queryText = MutableLiveData<String>()

  val users by lazy {
    Transformations.switchMap(queryText) { input ->
      repository.observePagedUserGithubs(connectivityAvailable, input, ioCoroutineScope)
    }
  }

  override fun onCleared() {
    super.onCleared()
    ioCoroutineScope.cancel()
  }
}