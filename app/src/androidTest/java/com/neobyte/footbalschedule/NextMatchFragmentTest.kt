package com.neobyte.footbalschedule

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withSubstring
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.neobyte.footbalschedule.MatchAdapter.TeamViewHolder
import com.neobyte.footbalschedule.detail.MatchDetailActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Random

@RunWith(AndroidJUnit4::class)
@LargeTest
class NextMatchFragmentTest {

  @Rule
  @JvmField
  val mainActivityRule = IntentsTestRule(MainActivity::class.java)

  @Test
  fun testClickNextMenuItem() {
    onView(withId(R.id.action_matches)).perform(click())
    onView(withSubstring("Next")).perform(click())
    onView(withId(R.id.next_match_layout)).check(matches(isDisplayed()))

    val rand = Random()
    val itemPos = rand.nextInt(14)
    onView(withId(R.id.rv_next_match))
        .perform(RecyclerViewActions.actionOnItemAtPosition<TeamViewHolder>(itemPos, click()))
    intended(IntentMatchers.hasComponent(MatchDetailActivity::class.java.name))
  }
}
