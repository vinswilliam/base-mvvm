package co.id.githubfinder.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.id.githubfinder.api.UserGithubService
import co.id.githubfinder.data.AppDatabase
import co.id.githubfinder.user.data.UserGithubDao
import co.id.githubfinder.user.data.UserGithubRemoteDataSource
import co.id.githubfinder.user.data.UserGithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class UserGithubRepositoryTest {
  private lateinit var repository: UserGithubRepository
  private val dao = mock(UserGithubDao::class.java)
  private val service = mock(UserGithubService::class.java)
  private val remoteDataSource = UserGithubRemoteDataSource(service)
  private val mockRemoteDataSource = spy(remoteDataSource)

  private val query = " mojomo"

  @Rule
  @JvmField
  val instantExecutorRule = InstantTaskExecutorRule()

  private val coroutineScope = CoroutineScope(Dispatchers.IO)

  @Before
  fun init() {
    val db = mock(AppDatabase::class.java)

    `when`(db.userGithubDao()).thenReturn(dao)
    `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
    repository = UserGithubRepository(dao, remoteDataSource)
  }

  @Test
  fun loadUserGithubFromNetwork() {
    runBlocking {
      repository.observePagedUserGithubs(true, query, coroutineScope)
      verify(dao, never()).getPagedUsersByLogin(query)
      verifyZeroInteractions(dao)
    }
  }
}