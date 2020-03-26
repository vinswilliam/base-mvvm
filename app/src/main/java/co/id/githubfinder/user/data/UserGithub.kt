package co.id.githubfinder.user.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/*
{
  "total_count": 12,
  "incomplete_results": false,
  "items": [
    {
      "login": "mojombo",
      "id": 1,
      "node_id": "MDQ6VXNlcjE=",
      "avatar_url": "https://secure.gravatar.com/avatar/25c7c18223fb42a4c6ae1c8db6f50f9b?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
      "gravatar_id": "",
      "url": "https://api.github.com/users/mojombo",
      "html_url": "https://github.com/mojombo",
      "followers_url": "https://api.github.com/users/mojombo/followers",
      "subscriptions_url": "https://api.github.com/users/mojombo/subscriptions",
      "organizations_url": "https://api.github.com/users/mojombo/orgs",
      "repos_url": "https://api.github.com/users/mojombo/repos",
      "received_events_url": "https://api.github.com/users/mojombo/received_events",
      "type": "User",
      "score": 105.47857
    }
  ]
}
 */

@Entity(tableName = "user_github")
data class UserGithub(
  @PrimaryKey
  @field:SerializedName("id")
  val id: Long,
  @field:SerializedName("login")
  val login: String,
  @field:SerializedName("node_id")
  var nodeId: String? = null,
  @field:SerializedName("avatar_url")
  var avatarUrl: String? = null,
  @field:SerializedName("gravatar_id")
  var gravatarId: String? = null,
  @field:SerializedName("url")
  var url: String? = null,
  @field:SerializedName("html_url")
  var htmlUrl: String? = null,
  @field:SerializedName("followers_url")
  var followersUrl: String? = null,
  @field:SerializedName("subscriptions_url")
  var subscriptionsUrl: String? = null,
  @field:SerializedName("organizations_url")
  var organizationsUrl: String? = null,
  @field:SerializedName("repos_url")
  var reposUrl: String? = null,
  @field:SerializedName("received_events_url")
  var receivedEventsUrl: String? = null,
  @field:SerializedName("type")
  var type: String? = null,
  @field:SerializedName("score")
  var score: Double? = null
) {
  override fun toString(): String {
    return login
  }
}