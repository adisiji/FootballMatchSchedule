package com.neobyte.footbalschedule.detail

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.google.gson.Gson
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.db.DatabaseHelper.Companion.EVENT_CONTENT
import com.neobyte.footbalschedule.db.DatabaseHelper.Companion.EVENT_ID
import com.neobyte.footbalschedule.db.DatabaseHelper.Companion.TABLE_FAVORITE
import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.Team
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

open class MatchDetailPresenter(private val databaseHelper: DatabaseHelper,
  private val matchDetailView: MatchDetailView) {

  var favState = false
  private val compositeDisposable = CompositeDisposable()
  private val gson: Gson by lazy {
    Gson()
  }

  fun getTeam(teamId: String, listener: GetTeamListener) {
    listener.onLoading()
    compositeDisposable.add(FootballMatchService.instance.getTeamDetail(teamId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          it.teams?.let { list ->
            list[0]?.let { team -> listener.onSuccess(team) }
          }
        }, {
          Log.e("Get Team", "Failed", it)
          listener.onFailed(it.message)
        })
    )
  }

  fun composeSubscriber() {
    compositeDisposable.dispose()
  }

  fun getFavorite(eventId: String) {
    databaseHelper.use {
      val query = select(TABLE_FAVORITE, EVENT_CONTENT).whereArgs(
          "$EVENT_ID = {eventId}", "eventId" to eventId
      )
      val event = query.exec {
        count
      }
      favState = event > 0
      matchDetailView.setAsFavourite(favState)
    }
  }

  fun setFavorite(event: Event) {
    if (favState) {
      removeFromFavorite(event.idEvent ?: "")
    } else {
      addToFavorite(event)
    }
  }

  fun removeFromFavorite(eventId: String) {
    try {
      databaseHelper.use {
        val items = delete(TABLE_FAVORITE, "$EVENT_ID = {eventId}",
                           "eventId" to eventId)
        if (items > 0) {
          matchDetailView.setAsFavourite(false)
          matchDetailView.showSnackbar("Removed from favorite")
          favState = !favState
        }
      }
    } catch (e: SQLiteConstraintException) {
      matchDetailView.showSnackbar(e.localizedMessage)
    }
  }

  fun addToFavorite(event: Event) {
    val content = gson.toJson(event)
    try {
      databaseHelper.use {
        insert(
            TABLE_FAVORITE, EVENT_ID to event.idEvent, EVENT_CONTENT to content
        )
      }
      matchDetailView.setAsFavourite(true)
      matchDetailView.showSnackbar("Added to favorite")
      favState = !favState
    } catch (e: SQLiteConstraintException) {
      matchDetailView.showSnackbar(e.localizedMessage)
    }
  }
}

interface GetTeamListener {
  fun onLoading()
  fun onSuccess(team: Team)
  fun onFailed(message: String?)
}