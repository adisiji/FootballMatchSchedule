package com.neobyte.footbalschedule

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withSubstring
import android.support.test.runner.AndroidJUnit4
import com.neobyte.footbalschedule.MatchAdapter.TeamViewHolder
import com.neobyte.footbalschedule.R.id
import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.player.PlayerActivity
import com.neobyte.footbalschedule.teamdetail.TeamDetailActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerListFragmentTest {

  val team = Team(idTeam = "133604", strTeam = "Arsenal")

  @Rule
  @JvmField
  val mActivityRule: IntentsTestRule<TeamDetailActivity> =
    object : IntentsTestRule<TeamDetailActivity>(TeamDetailActivity::class.java) {

      override fun getActivityIntent(): Intent {
        val intent = Intent()
        intent.putExtra("extra_team", team)
        return intent
      }
    }

  @Test
  fun testShowDetailPlayers() {
    onView(withSubstring("Players")).perform(click())
    onView(withId(id.rv_team_players))
        .perform(RecyclerViewActions.actionOnItemAtPosition<TeamViewHolder>(0, click()))
    Intents.intended(IntentMatchers.hasComponent(PlayerActivity::class.java.name))
  }

}