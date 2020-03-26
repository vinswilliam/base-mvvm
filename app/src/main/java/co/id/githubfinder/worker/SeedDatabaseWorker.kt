package co.id.githubfinder.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.id.githubfinder.data.AppDatabase
import co.id.githubfinder.user.data.UserGithub
import co.id.githubfinder.util.DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber

class SeedDatabaseWorker(
  context: Context,
  workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

  override suspend fun doWork(): Result = coroutineScope {
    withContext(Dispatchers.IO) {
      try {
        applicationContext.assets.open(DATA_FILENAME).use { inputStream ->
          JsonReader(inputStream.reader()).use { jsonReader ->
            val type = object : TypeToken<List<UserGithub>>() {}.type
            val list: List<UserGithub> = Gson().fromJson(jsonReader, type)

            AppDatabase.getInstance(applicationContext).userGithubDao().insertAll(list)

            Result.success()
          }
        }
      } catch (e: Exception) {
        Timber.e(e, "Error seeding database")
        Result.failure()
      }
    }
  }

}