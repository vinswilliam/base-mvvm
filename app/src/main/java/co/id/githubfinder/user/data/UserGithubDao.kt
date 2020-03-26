package co.id.githubfinder.user.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserGithubDao {

  @Query("SELECT * FROM user_github WHERE login LIKE :login || '%' ORDER BY id ASC")
  fun getPagedUsersByLogin(login: String): DataSource.Factory<Int, UserGithub>

  @Query("SELECT * FROM user_github WHERE login LIKE :login || '%' ORDER BY id ASC")
  fun getUsersByLogin(login: String): LiveData<List<UserGithub>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(user: List<UserGithub>)
}