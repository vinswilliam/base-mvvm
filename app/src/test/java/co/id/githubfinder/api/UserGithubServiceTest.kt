package co.id.githubfinder.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class UserGithubServiceTest {

  @Rule
  @JvmField
  val instantExecutorRule = InstantTaskExecutorRule()

  private lateinit var service: UserGithubService

  private lateinit var mockWebServer: MockWebServer

  @Before
  fun createService() {
    mockWebServer = MockWebServer()
    service = Retrofit.Builder()
      .baseUrl(mockWebServer.url(""))
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(UserGithubService::class.java)
  }

  @After
  fun stopService() {
    mockWebServer.shutdown()
  }

  @Test
  fun requestUserGithub() {
    runBlocking {
      enqueueResponse("user_github_sets.json")
      val resultResponse = service.getUser("mojombo", 1)
      val request = mockWebServer.takeRequest()
      assertNotNull(resultResponse)
      assertThat(request.path, `is`("/search/users?q=mojombo&page=1"))
    }
  }

  @Test
  fun getUserGithubResponse() {
    runBlocking {
      enqueueResponse("user_github_sets.json")
      val resultResponse = service.getUser("mojombo", 1).body()
      val userGithubSet = resultResponse!!.items

      assertThat(resultResponse.totalCount, `is`(1))
      assertThat(userGithubSet.size, `is`(1))
    }
  }

  @Test
  fun getUserGithubItem() {
    runBlocking {
      enqueueResponse("user_github_sets.json")
      val resultResponse = service.getUser("mojombo", 1).body()
      val userGithubSet = resultResponse!!.items
      val userGithub = userGithubSet[0]
      assertThat(userGithub.id, `is`(1L))
      assertThat(userGithub.login, `is`("mojombo"))
      assertThat(userGithub.nodeId, `is`("MDQ6VXNlcjE="))
      assertThat(userGithub.avatarUrl, `is`("https://secure.gravatar.com/avatar/25c7c18223fb42a4c6ae1c8db6f50f9b?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png"))
      assertThat(userGithub.gravatarId, `is`(""))
      assertThat(userGithub.url, `is`("https://api.github.com/users/mojombo"))
      assertThat(userGithub.htmlUrl, `is`("https://github.com/mojombo"))
      assertThat(userGithub.followersUrl, `is`("https://api.github.com/users/mojombo/followers"))
      assertThat(userGithub.subscriptionsUrl, `is`("https://api.github.com/users/mojombo/subscriptions"))
      assertThat(userGithub.organizationsUrl, `is`("https://api.github.com/users/mojombo/orgs"))
      assertThat(userGithub.reposUrl, `is`("https://api.github.com/users/mojombo/repos"))
      assertThat(userGithub.receivedEventsUrl, `is`("https://api.github.com/users/mojombo/received_events"))
      assertThat(userGithub.type, `is`("User"))
      assertThat(userGithub.score, `is`(105.47857))
    }
  }

  private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
    val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
    val source = Okio.buffer(Okio.source(inputStream))
    val mockResponse = MockResponse()
    for ((key, value) in headers) {
      mockResponse.addHeader(key, value)
    }
    mockWebServer.enqueue(mockResponse.setBody(
      source.readString(Charsets.UTF_8))
    )
  }
}