package co.id.githubfinder.util

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.test.espresso.IdlingResource
import androidx.test.rule.ActivityTestRule
import java.util.*

class DataBindingIdlingResource (
  private val activityTestRule: ActivityTestRule<*>
) : IdlingResource {
  private val idlingCallbacks = mutableListOf<IdlingResource.ResourceCallback>()
  private val id = UUID.randomUUID().toString()
  private var wasNotIdle = false

  override fun getName(): String {
    return "DataBinding $id"
  }

  override fun isIdleNow(): Boolean {
    val idle = !getBindings().any { it.hasPendingBindings() }
    @Suppress("LiftReturnOrAssignment")
    if (idle) {
      if (wasNotIdle) {
        // notify observers to avoid espresso race detector
        idlingCallbacks.forEach { it.onTransitionToIdle() }
      }
      wasNotIdle = false
    } else {
      wasNotIdle = true
      // check next frame
      activityTestRule.activity.findViewById<View>(android.R.id.content).postDelayed({
        isIdleNow
      }, 16)
    }
    return idle  }

  override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
    idlingCallbacks.add(callback)
  }

  /**
   * Find all binding classes in all currently available fragments.
   */
  private fun getBindings(): List<ViewDataBinding> {
    return (activityTestRule.activity as? FragmentActivity)
      ?.supportFragmentManager
      ?.fragments
      ?.mapNotNull {
        it.view?.let { view ->
          DataBindingUtil.getBinding<ViewDataBinding>(
            view
          )
        }
      } ?: emptyList()
  }
}