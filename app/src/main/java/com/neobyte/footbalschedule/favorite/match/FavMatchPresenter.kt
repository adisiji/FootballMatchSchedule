package com.neobyte.footbalschedule.favorite.match

import com.neobyte.footbalschedule.db.DatabaseHelper

class FavMatchPresenter(private val databaseHelper: DatabaseHelper, private val favMatchView: FavMatchView) {

  fun getFavMatches() {
    favMatchView.onLoading()
    favMatchView.onSuccess(databaseHelper.getAllFavMatches())
  }

}