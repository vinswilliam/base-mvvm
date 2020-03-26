package co.id.githubfinder.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.id.githubfinder.R
import co.id.githubfinder.databinding.ListUserFragmentBinding
import co.id.githubfinder.di.Injectable
import co.id.githubfinder.di.injectViewModel
import co.id.githubfinder.ui.VerticalItemDecoration
import co.id.githubfinder.util.ConnectivityUtil
import javax.inject.Inject

class UserGithubFragment : Fragment(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: UserGithubViewModel

  private lateinit var binding: ListUserFragmentBinding

  private val adapterUserGithub: UserGithubAdapter by lazy { UserGithubAdapter() }

  private lateinit var linearLayoutManager: LinearLayoutManager

  private val linearDecoration: RecyclerView.ItemDecoration by lazy {
    VerticalItemDecoration(resources.getDimension(R.dimen.default_margin).toInt())
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewModel = injectViewModel(viewModelFactory)
    viewModel.connectivityAvailable = ConnectivityUtil.isConnected(requireContext())

    binding = ListUserFragmentBinding.inflate(inflater, container, false)
    context ?: return binding.root

    linearLayoutManager = LinearLayoutManager(activity)

    binding.recyclerViewUser.apply {
      layoutManager = linearLayoutManager
      adapter = adapterUserGithub
      addItemDecoration(linearDecoration)
    }

    binding.editTextQuery.addTextChangedListener {
      viewModel.queryText.value = it.toString()
    }

    subscribeUi(adapterUserGithub)
    return binding.root
  }

  private fun subscribeUi(adapter: UserGithubAdapter) {
//    viewModel.userGithubs.observe(viewLifecycleOwner) {
//      adapter.submitList(it)
//    }

    viewModel.users.observe(viewLifecycleOwner) {
      adapter.submitList(it)
    }

  }
}