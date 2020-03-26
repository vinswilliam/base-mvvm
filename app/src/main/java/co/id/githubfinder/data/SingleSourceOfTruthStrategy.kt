package co.id.githubfinder.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

fun <T, A> resultLiveData(databaseQuery: () -> LiveData<T>,
                          networkCAll: suspend () -> Result<A>,
                          saveCallResult: suspend (A) -> Unit) : LiveData<Result<T>> =
  liveData(Dispatchers.IO) {
    emit(Result.loading<T>())
  }
