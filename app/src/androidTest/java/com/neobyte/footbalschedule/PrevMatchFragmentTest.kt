package com.neobyte.footbalschedule

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.neobyte.footbalschedule.MatchAdapter.TeamViewHolder
import com.neobyte.footbalschedule.detail.MatchDetailActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PrevMatchFragmentTest {

  @Rule
  @JvmField
  val mainActivityRule = IntentsTestRule(MainActivity::class.java)

  @Test
  fun testClickPrevMenuItem() {
    onView(withId(R.id.action_prev)).perform(click())
    onView(withId(R.id.prev_match_layout)).check(matches(isDisplayed()))

    onView(withId(R.id.rv_prev_match))
        .perform(RecyclerViewActions.actionOnItemAtPosition<TeamViewHolder>(0, click()))
    intended(hasComponent(MatchDetailActivity::class.java.name))
  }

}