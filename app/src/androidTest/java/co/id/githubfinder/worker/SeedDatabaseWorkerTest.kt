package co.id.githubfinder.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.WorkManager
import androidx.work.testing.TestListenableWorkerBuilder
import org.junit.Assert.assertThat
import androidx.work.ListenableWorker.Result
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RefreshMainDataWorkTest {
  private lateinit var context: Context
  private lateinit var workManager: WorkManager

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    workManager = WorkManager.getInstance(context)
  }

  @Test
  fun testRefreshMainDataWork() {
    val worker = TestListenableWorkerBuilder<SeedDatabaseWorker>(context).build()
    val result = worker.startWork().get()

    assertThat(result, `is`(Result.success()))
  }
}