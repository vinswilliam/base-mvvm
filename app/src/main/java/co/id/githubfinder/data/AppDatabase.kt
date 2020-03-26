package co.id.githubfinder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.id.githubfinder.user.data.UserGithub
import co.id.githubfinder.user.data.UserGithubDao
import co.id.githubfinder.worker.SeedDatabaseWorker

@Database(entities = [UserGithub::class],
version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userGithubDao(): UserGithubDao

  companion object {
    @Volatile
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return instance ?: synchronized(this) {
        instance ?: buildDatabase(context).also { instance = it }
      }
    }

    private fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(context, AppDatabase::class.java, "userfinder-db")
        .addCallback(object : RoomDatabase.Callback() {
          override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
            WorkManager.getInstance(context).enqueue(request)
          }
        })
        .fallbackToDestructiveMigration()
        .build()
    }
  }
}