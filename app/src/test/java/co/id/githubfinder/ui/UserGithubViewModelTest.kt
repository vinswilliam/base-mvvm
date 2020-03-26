package co.id.githubfinder.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import co.id.githubfinder.user.data.UserGithubRepository
import co.id.githubfinder.user.ui.UserGithubViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class UserGithubViewModelTest {

  private val query = "mojombo"
  private val queryLiveData = MutableLiveData<String>()

  @Rule
  @JvmField
  val instantExecutorRule = InstantTaskExecutorRule()

  private val coroutineScope = CoroutineScope(Dispatchers.IO)

  private val repository = mock(UserGithubRepository::class.java)
  private var viewModel = UserGithubViewModel(repository, coroutineScope)

  @Test
  fun testNull() {
    assertThat(viewModel.queryText, notNullValue())
    assertThat(viewModel.connectivityAvailable, notNullValue())

    verify(repository, never()).observePagedUserGithubs(false, query, coroutineScope)
    verify(repository, never()).observePagedUserGithubs(true, query, coroutineScope)
  }

  @Test
  fun doNotFetchWithoutObservers() {
    viewModel.queryText = queryLiveData
    verify(repository, never()).observePagedUserGithubs(false, query, coroutineScope)
  }

  @Test
  fun doNotFetchWithoutObserverOnConnectionChange() {
    viewModel.queryText = queryLiveData
    viewModel.connectivityAvailable = true
    verify(repository, never()).observePagedUserGithubs(true, query, coroutineScope)
  }
}