package co.id.githubfinder.user.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.id.githubfinder.binding.bindImageFromUrl
import co.id.githubfinder.databinding.ListItemUserBinding
import co.id.githubfinder.user.data.UserGithub

class UserGithubAdapter : PagedListAdapter<UserGithub, UserGithubAdapter.ViewHolder>(DiffCallback()) {

  private lateinit var recyclerView: RecyclerView

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ListItemUserBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val userDetail = getItem(position)
    userDetail?.let {
      holder.apply {
        bind(userDetail)
        itemView.tag = userDetail
      }
    }
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    super.onAttachedToRecyclerView(recyclerView)
    this.recyclerView = recyclerView
  }

  class ViewHolder(
    private val binding: ListItemUserBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserGithub) {
      binding.apply {
        user = item
        executePendingBindings()
      }
    }
  }
}

private class DiffCallback : DiffUtil.ItemCallback<UserGithub>() {
  override fun areItemsTheSame(oldItem: UserGithub, newItem: UserGithub): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: UserGithub, newItem: UserGithub): Boolean {
    return oldItem == newItem
  }

}