package com.neobyte.footbalschedule.favorite

import com.neobyte.footbalschedule.HomeScreenState
import com.neobyte.footbalschedule.db.DatabaseHelper

class FavoriteMatchPresenter(private val favoriteMatchView: FavoriteMatchView,
                             private val databaseHelper: DatabaseHelper) {

  fun getFavMatch() {
    favoriteMatchView.setScreenState(HomeScreenState.Loading)
    val listEvent = databaseHelper.getAllFavMatches()
    favoriteMatchView.setScreenState(HomeScreenState.Data(listEvent))
  }

}