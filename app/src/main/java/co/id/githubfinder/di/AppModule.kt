package co.id.githubfinder.di

import android.app.Application
import co.id.githubfinder.api.UserGithubService
import co.id.githubfinder.data.AppDatabase
import co.id.githubfinder.user.data.UserGithubRemoteDataSource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

  @Singleton
  @Provides
  fun provideUserGithubService(
    @UserGithubAPI okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory
  ) = provideService(okHttpClient, converterFactory, UserGithubService::class.java)

  @Singleton
  @Provides
  fun provideUserGithubRemoteDataSource(userGithubService: UserGithubService) =
    UserGithubRemoteDataSource(userGithubService)

  @UserGithubAPI
  @Provides
  fun providePrivateOkHttpClient(
    upstreamClient: OkHttpClient
  ): OkHttpClient {
    return upstreamClient.newBuilder().build()
  }

  @Singleton
  @Provides
  fun provideDb(app: Application) = AppDatabase.getInstance(app)

  @Singleton
  @Provides
  fun provideUserGithubDao(db: AppDatabase) = db.userGithubDao()

  @CoroutineScropeIO
  @Provides
  fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

  private fun createRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(UserGithubService.ENDPOINT)
      .client(okHttpClient)
      .addConverterFactory(converterFactory)
      .build()
  }

  private fun <T> provideService(
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory, clazz: Class<T>
  ): T {
    return createRetrofit(okHttpClient, converterFactory).create(clazz)
  }
}