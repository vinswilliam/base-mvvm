package co.id.githubfinder.api

import com.google.gson.annotations.SerializedName

data class ResultsResponse<T>(

  @SerializedName("total_count")
  val totalCount: Int,
  @SerializedName("incomplete_result")
  val incompleteResult: Boolean,
  @SerializedName("items")
  var items: List<T>
)