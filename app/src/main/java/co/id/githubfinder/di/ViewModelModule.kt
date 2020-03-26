package co.id.githubfinder.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.id.githubfinder.user.ui.UserGithubViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(UserGithubViewModel::class)
  abstract fun bindUserGithubViewModel(viewModel: UserGithubViewModel): ViewModel

  @Binds
  abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}