package com.neobyte.footbalschedule

import android.content.Intent
import android.support.design.R
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.neobyte.footbalschedule.R.id
import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.teamdetail.TeamDetailActivity
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TeamDetailActivityTest {

  val team = Team(idTeam = "133604", strTeam = "Arsenal")

  @Rule
  @JvmField
  val mActivityRule: ActivityTestRule<TeamDetailActivity> =
    object : ActivityTestRule<TeamDetailActivity>(TeamDetailActivity::class.java) {

      override fun getActivityIntent(): Intent {
        val intent = Intent()
        intent.putExtra("extra_team", team)
        return intent
      }
    }

  @Test
  fun testClickFavorite_ShowSnackbar() {
    onView(withId(id.fab)).perform(click())
    onView(Matchers.allOf(withId(R.id.snackbar_text),
                                   withText(Matchers.containsString("favorite"))))
        .check(ViewAssertions.matches(isDisplayed()))
  }

  @After
  fun deleteFakeMatch() {
    onView(withId(id.fab)).perform(click())
  }
}