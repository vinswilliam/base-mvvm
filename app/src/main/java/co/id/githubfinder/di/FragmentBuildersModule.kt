package co.id.githubfinder.di

import co.id.githubfinder.user.ui.UserGithubFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
  @ContributesAndroidInjector
  abstract fun contributeUserGithubFragment(): UserGithubFragment
}