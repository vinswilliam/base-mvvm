package co.id.githubfinder.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import co.id.githubfinder.TestApp

class AppTestRunner : AndroidJUnitRunner() {
  override fun newApplication(
    cl: ClassLoader,
    className: String,
    context: Context
  ): Application {
    return super.newApplication(cl, TestApp::class.java.name, context)
  }
}