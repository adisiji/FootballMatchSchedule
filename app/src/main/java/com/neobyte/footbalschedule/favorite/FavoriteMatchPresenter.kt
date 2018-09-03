package com.neobyte.footbalschedule.favorite

import com.google.gson.Gson
import com.neobyte.footbalschedule.HomeScreenState
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.db.DatabaseHelper.Companion.TABLE_FAVORITE
import com.neobyte.footbalschedule.models.Event
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.select

class FavoriteMatchPresenter(private val favoriteMatchView: FavoriteMatchView,
    private val databaseHelper: DatabaseHelper) {

  private val gson = Gson()

  init {
    getFavMatch()
  }

  fun getFavMatch() {
    favoriteMatchView.setScreenState(HomeScreenState.Loading)
    databaseHelper.use {
      val query = select(TABLE_FAVORITE, DatabaseHelper.EVENT_CONTENT)
      val stringEvent = query.exec {
        parseList(StringParser)
      }
      val listEvent = mutableListOf<Event>()
      stringEvent.forEach {
        val event = gson.fromJson<Event>(it, Event::class.java)
        listEvent.add(event)
      }
      favoriteMatchView.setScreenState(HomeScreenState.Data(listEvent))
    }
  }
}