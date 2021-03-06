package co.id.githubfinder.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.R
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import co.id.githubfinder.testing.SingleFragmentActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4::class)
class DataBindingIdlingResourcesTest {
  @Rule
  @JvmField
  val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

  private val idlingResource = DataBindingIdlingResource(activityRule)
  private val fragment = TestFragment()

  @Before
  fun init() {
    IdlingRegistry.getInstance().register(idlingResource)
    activityRule.activity.replaceFragment(fragment)
    Espresso.onIdle()
  }

  @After
  fun unregister() {
    IdlingRegistry.getInstance().unregister(idlingResource)
  }

  @Test
  fun alreadyIdle() {
    setHasPendingBindings(false)
    MatcherAssert.assertThat(isIdle(), CoreMatchers.`is`(true))
  }

  @Test
  fun alreadyIdle_dontCallCallbacks() {
    setHasPendingBindings(false)
    val callback = registerIdleCallback()
    isIdle()
    Mockito.verify(callback, Mockito.never()).onTransitionToIdle()
  }

  @Test
  fun notIdle() {
    setHasPendingBindings(true)
    MatcherAssert.assertThat(isIdle(), CoreMatchers.`is`(false))
  }

  @Test
  fun callback_becomeIdle() {
    setHasPendingBindings(true)
    val callback = registerIdleCallback()
    isIdle()
    setHasPendingBindings(false)
    isIdle()
    Mockito.verify(callback).onTransitionToIdle()
  }

  @Test
  fun callback_becomeIdle_withoutIsIdle() {
    setHasPendingBindings(true)
    val idle = CountDownLatch(1)
    idlingResource.registerIdleTransitionCallback {
      idle.countDown()
    }
    MatcherAssert.assertThat(idlingResource.isIdleNow, CoreMatchers.`is`(false))
    setHasPendingBindings(false)
    MatcherAssert.assertThat(idle.await(5, TimeUnit.SECONDS), CoreMatchers.`is`(true))
  }

  private fun setHasPendingBindings(hasPendingBindings : Boolean) {
    fragment.fakeBinding.hasPendingBindings.set(hasPendingBindings)
  }

  class TestFragment : Fragment() {
    lateinit var fakeBinding: FakeBinding
    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View {
      val view = View(container!!.context)
      fakeBinding = FakeBinding(view)
      return view
    }
  }

  class FakeBinding(view: View) : ViewDataBinding(mockClass<DataBindingComponent>(), view, 0) {
    val hasPendingBindings = AtomicBoolean(false)

    init {
      view.setTag(R.id.dataBinding, this)
    }

    override fun setVariable(variableId: Int, value: Any?) = false

    override fun executeBindings() {

    }

    override fun onFieldChange(localFieldId: Int, `object`: Any?, fieldId: Int) = false

    override fun invalidateAll() {
    }

    override fun hasPendingBindings() = hasPendingBindings.get()
  }

  private fun isIdle(): Boolean {
    val task = FutureTask<Boolean> {
      return@FutureTask idlingResource.isIdleNow
    }
    InstrumentationRegistry.getInstrumentation().runOnMainSync(task)
    return task.get()
  }

  private fun registerIdleCallback(): IdlingResource.ResourceCallback {
    val task = FutureTask<IdlingResource.ResourceCallback> {
      val callback = mockClass<IdlingResource.ResourceCallback>()
      idlingResource.registerIdleTransitionCallback(callback)
      return@FutureTask callback
    }
    InstrumentationRegistry.getInstrumentation().runOnMainSync(task)
    return task.get()
  }
}

inline fun <reified T> mockClass(): T = Mockito.mock(T::class.java)
