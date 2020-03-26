package co.id.githubfinder.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.id.githubfinder.user.data.UserGithubDao
import co.id.githubfinder.util.getValue
import co.id.githubfinder.util.testQuery
import co.id.githubfinder.util.testUserGithubA
import co.id.githubfinder.util.testUserGithubB
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserGithubDaoTest : DbTest() {

  private lateinit var userGithubDao: UserGithubDao
  private val userGithubA = testUserGithubA.copy()
  private val userGithubB = testUserGithubB.copy()
  private val query = testQuery

  @get:Rule
  var instanceTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  fun createDb() {
    userGithubDao = db.userGithubDao()

    runBlocking {
      userGithubDao.insertAll(listOf(userGithubA, userGithubB))
    }
  }

  @Test
  fun testGetUserGithubs() {
    val list = getValue(userGithubDao.getUsersByLogin(query))
    assertThat(list.size, equalTo(1))

    assertThat(list[0], Matchers.equalTo(userGithubA))
  }

}