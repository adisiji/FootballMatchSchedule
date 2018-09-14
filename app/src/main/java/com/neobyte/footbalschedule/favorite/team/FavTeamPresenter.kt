package com.neobyte.footbalschedule.favorite.team

import com.neobyte.footbalschedule.db.DatabaseHelper

class FavTeamPresenter(private val databaseHelper: DatabaseHelper,
                       private val favTeamView: FavTeamView) {

  fun getFavTeams() {
    favTeamView.onLoading()
    favTeamView.onSuccess(databaseHelper.getAllFavTeams())
  }

}