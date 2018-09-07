package com.neobyte.footbalschedule

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isClickable
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

  @Rule
  @JvmField
  val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

  @Test
  fun testAllBottomMenuIsClickable() {
    onView(withId(R.id.action_prev)).check(matches(isClickable()))
    onView(withId(R.id.action_next)).check(matches(isClickable()))
    onView(withId(R.id.action_favorite)).check(matches(isClickable()))
  }

  @Test
  fun testNextMatchIsDisplayed() {
    onView(withId(R.id.action_next)).perform(click())
    onView(withId(R.id.next_match_layout)).check(matches(isDisplayed()))
  }

  @Test
  fun testPrevMatchIsDisplayed() {
    onView(withId(R.id.action_prev)).perform(click())
    onView(withId(R.id.prev_match_layout)).check(matches(isDisplayed()))
  }

  @Test
  fun testFavMatchIsDisplayed() {
    onView(withId(R.id.action_favorite)).perform(click())
    onView(withId(R.id.fav_match_layout)).check(matches(isDisplayed()))
  }

}