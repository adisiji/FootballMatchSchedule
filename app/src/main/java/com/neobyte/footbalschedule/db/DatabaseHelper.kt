package com.neobyte.footbalschedule.db

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.Team
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.select

class DatabaseHelper(ctx: Context) : ManagedSQLiteOpenHelper(
    ctx, "SuppaAvengerTeam.db", null, 2) {

  companion object {
    const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
    const val EVENT_ID: String = "EVENT_ID"
    const val EVENT_CONTENT: String = "EVENT_CONTENT"
    const val TABLE_FAV_TEAM = "TABLE_FAV_TEAM"
    const val TEAM_ID: String = "TEAM_ID"
    const val TEAM_CONTENT: String = "TEAM_CONTENT"

    private var instance: DatabaseHelper? = null

    @Synchronized
    fun getInstance(ctx: Context): DatabaseHelper {
      if (instance == null) {
        instance = DatabaseHelper(ctx.applicationContext)
      }
      return instance as DatabaseHelper
    }
  }

  private val gson = Gson()

  override fun onCreate(db: SQLiteDatabase) {
    // Here you create tables
    db.createTable(
        TABLE_FAVORITE, true,
        EVENT_ID to TEXT + PRIMARY_KEY,
        EVENT_CONTENT to TEXT
    )
    db.createTable(TABLE_FAV_TEAM, true,
                   TEAM_ID to TEXT + PRIMARY_KEY,
                   TEAM_CONTENT to TEXT
    )
  }

  override fun onUpgrade(db: SQLiteDatabase,
                         oldVersion: Int,
                         newVersion: Int) {
    // Here you can upgrade tables, as usual
    db.dropTable(TABLE_FAVORITE, true)
    db.dropTable(TABLE_FAV_TEAM, true)
  }

  fun getAllFavMatches(): List<Event> {
    val listEvent = mutableListOf<Event>()
    instance?.use {
      val query = select(TABLE_FAVORITE, DatabaseHelper.EVENT_CONTENT)
      val stringEvent = query.exec {
        parseList(StringParser)
      }
      stringEvent.forEach {
        val event = gson.fromJson<Event>(it, Event::class.java)
        listEvent.add(event)
      }
    }
    return listEvent
  }

  fun insertFavMatch(event: Event,
                     listener: DatabaseListener?) {
    try {
      val content = gson.toJson(event)
      instance?.use {
        insert(TABLE_FAVORITE, EVENT_ID to event.idEvent, EVENT_CONTENT to content)
      }
      listener?.onSuccess()
    } catch (e: Throwable) {
      System.out.print(e.printStackTrace())
      listener?.onFailed(e.localizedMessage)
    }
  }

  fun removeFavMatch(eventId: String,
                     listener: DatabaseListener?) {
    try {
      instance?.use {
        val items = delete(TABLE_FAVORITE,
                           "$EVENT_ID = {eventId}", "eventId" to eventId)
        if (items > 0) {
          listener?.onSuccess()
        }
      }
    } catch (e: SQLiteConstraintException) {
      listener?.onFailed(e.localizedMessage)
    }
  }

  fun isMatchFavorite(eventId: String): Boolean {
    var result = false
    instance?.use {
      val query = select(TABLE_FAVORITE, EVENT_CONTENT)
          .whereArgs("$EVENT_ID = {eventId}",
                     "eventId" to eventId)
      val event = query.exec {
        count
      }
      result = event > 0
    }
    return result
  }

  fun insertFavTeam(team: Team,
                     listener: DatabaseListener?) {
    try {
      val content = gson.toJson(team)
      instance?.use {
        insert(TABLE_FAV_TEAM, TEAM_ID to team.idTeam, TEAM_CONTENT to content)
      }
      listener?.onSuccess()
    } catch (e: Throwable) {
      System.out.print(e.printStackTrace())
      listener?.onFailed(e.localizedMessage)
    }
  }

  fun isTeamFavorite(teamId: String): Boolean {
    var result = false
    instance?.use {
      val query = select(TABLE_FAV_TEAM, TEAM_CONTENT)
          .whereArgs("$TEAM_ID = {teamId}",
                     "teamId" to teamId)
      val event = query.exec {
        count
      }
      result = event > 0
    }
    return result
  }

  fun removeFavTeam(teamId: String,
                     listener: DatabaseListener?) {
    try {
      instance?.use {
        val items = delete(TABLE_FAV_TEAM,
                           "$TEAM_ID = {teamId}", "teamId" to teamId)
        if (items > 0) {
          listener?.onSuccess()
        }
      }
    } catch (e: SQLiteConstraintException) {
      listener?.onFailed(e.localizedMessage)
    }
  }

  fun getAllFavTeams(): List<Team> {
    val listTeams = mutableListOf<Team>()
    instance?.use {
      val query = select(TABLE_FAV_TEAM, TEAM_CONTENT)
      val stringEvent = query.exec {
        parseList(StringParser)
      }
      stringEvent.forEach {
        val event = gson.fromJson<Team>(it, Team::class.java)
        listTeams.add(event)
      }
    }
    return listTeams
  }

}

interface DatabaseListener {
  fun onSuccess()
  fun onFailed(message: String)
}