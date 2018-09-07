package com.neobyte.footbalschedule

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.neobyte.footbalschedule.detail.MatchDetailActivity
import com.neobyte.footbalschedule.models.Event
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MatchDetailActivityTest {

  private val date = "2018-09-21"
  val event = Event(dateEvent = date, intAwayScore = "3", intHomeScore = "4",
                    intHomeShots = "10", intAwayShots = "7",
                    strHomeFormation = "4-4-2", strAwayFormation = "5-3-2",
                    strHomeGoalDetails = "Bejo; Faisal; Joni; Beckhol",
                    strAwayGoalDetails = "Dodo; Bobo; Jojo",
                    strHomeLineupGoalkeeper = "Frencs Jk;Joko Frencha",
                    strAwayLineupGoalkeeper = "Frencs Jk;Joko Frencha")

  @Rule
  @JvmField
  val mActivityRule: ActivityTestRule<MatchDetailActivity> =
    object : ActivityTestRule<MatchDetailActivity>(MatchDetailActivity::class.java) {

      override fun getActivityIntent(): Intent {
        val intent = Intent()
        intent.putExtra(Constants.EVENT, event)
        return intent
      }
    }

  @Test
  fun testShowRightContent() {
    onView(withId(R.id.tv_date_detail)).check(matches(withText(containsString("2018"))))
    onView(withId(R.id.tv_skor_team_1)).check(matches(withText("4")))
    onView(withId(R.id.tv_skor_team_2)).check(matches(withText("3")))
    onView(withId(R.id.tv_shots_team_1)).check(matches(withText("10")))
    onView(withId(R.id.tv_shots_team_2)).check(matches(withText("7")))

    onView(withId(R.id.tv_formation_team_1)).check(matches(withText("4-4-2")))
    onView(withId(R.id.tv_formation_team_2)).check(matches(withText("5-3-2")))

    onView(withId(R.id.tv_goal_team_1)).check(matches(withText("Bejo\n Faisal\n Joni\n Beckhol")))
    onView(withId(R.id.tv_goal_team_2)).check(matches(withText("Dodo\n Bobo\n Jojo")))

    onView(withId(R.id.tv_goalkeeper_team_1)).check(matches(withText("Frencs Jk\nJoko Frencha")))
    onView(withId(R.id.tv_goalkeeper_team_2)).check(matches(withText("Frencs Jk\nJoko Frencha")))
  }

  @Test
  fun testClickFavorite_ShowSnackbar() {
    onView(withId(R.id.add_to_favorite)).perform(click())
    onView(allOf(withId(android.support.design.R.id.snackbar_text),
                 withText(containsString("favorite"))))
        .check(matches(isDisplayed()))
  }

}