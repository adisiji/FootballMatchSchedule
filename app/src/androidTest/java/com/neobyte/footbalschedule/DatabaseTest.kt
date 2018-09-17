package com.neobyte.footbalschedule

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.db.DatabaseListener
import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.Team
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

  @Rule
  @JvmField
  val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

  private lateinit var databaseHelper: DatabaseHelper

  @Before
  fun setup() {
    databaseHelper = DatabaseHelper.getInstance(mActivityRule.activity)
  }

  @Test
  fun testInsertDeleteFavMatch() {
    var startEventCount = databaseHelper.getAllFavMatches().size
    val eventId = System.currentTimeMillis()
    val event = Event(idEvent = "$eventId")
    databaseHelper.insertFavMatch(event, object : DatabaseListener {
      override fun onSuccess() {
        startEventCount++
      }

      override fun onFailed(message: String) {
        System.out.print(message)
      }

    })

    val resultFavMatches = databaseHelper.getAllFavMatches()
    assertEquals(startEventCount, resultFavMatches.size)

    // remove fake match
    databaseHelper.removeFavMatch("$eventId", null)
  }

  @Test
  fun testEventMatchIsFavorite() {
    var startEventCount = databaseHelper.getAllFavMatches().size
    val eventId = System.currentTimeMillis()
    val event = Event(idEvent = "$eventId")
    databaseHelper.insertFavMatch(event, object : DatabaseListener {
      override fun onSuccess() {
        startEventCount++
      }

      override fun onFailed(message: String) {
        System.out.print(message)
      }
    })

    val favEvent = databaseHelper.isMatchFavorite("$eventId")

    assertTrue(favEvent)

    // remove fake match
    databaseHelper.removeFavMatch("$eventId", null)
  }

  @Test
  fun testEventMatchIsNotFavorite() {
    val eventId = System.currentTimeMillis()
    val favEvent = databaseHelper.isMatchFavorite("$eventId")

    assertFalse(favEvent)
  }

  @Test
  fun testInsertDeleteFavTeam() {
    var startEventCount = databaseHelper.getAllFavTeams().size
    val teamId = System.currentTimeMillis()
    val team = Team(idTeam = "$teamId")
    databaseHelper.insertFavTeam(team, object : DatabaseListener {
      override fun onSuccess() {
        startEventCount++
      }

      override fun onFailed(message: String) {
        System.out.print(message)
      }

    })

    val resultFavTeams = databaseHelper.getAllFavTeams()
    assertEquals(startEventCount, resultFavTeams.size)

    // remove fake team
    databaseHelper.removeFavTeam("$teamId", null)
  }

  @Test
  fun testTeamIsFavorite() {
    var startEventCount = databaseHelper.getAllFavTeams().size
    val teamId = System.currentTimeMillis()
    val team = Team(idTeam = "$teamId")
    databaseHelper.insertFavTeam(team, object : DatabaseListener {
      override fun onSuccess() {
        startEventCount++
      }

      override fun onFailed(message: String) {
        System.out.print(message)
      }

    })

    val favTeam = databaseHelper.isTeamFavorite("$teamId")

    assertTrue(favTeam)

    // remove fake team
    databaseHelper.removeFavTeam("$teamId", null)
  }

  @Test
  fun testTeamIsNotFavorite() {
    val teamId = System.currentTimeMillis()
    val favTeam = databaseHelper.isTeamFavorite("$teamId")

    assertFalse(favTeam)
  }

}