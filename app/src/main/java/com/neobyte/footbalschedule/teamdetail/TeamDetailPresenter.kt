package com.neobyte.footbalschedule.teamdetail

import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.db.DatabaseListener
import com.neobyte.footbalschedule.models.Team

class TeamDetailPresenter(private val databaseHelper: DatabaseHelper,
                          private val matchDetailView: TeamDetailView) {

  var favState = false

  fun getFavorite(teamId: String) {
    favState = databaseHelper.isTeamFavorite(teamId)
    matchDetailView.setAsFavourite(favState)
  }

  fun setFavorite(team: Team) {
    if (favState) {
      removeFromFavorite(team.idTeam ?: "")
    } else {
      addToFavorite(team)
    }
  }

  private fun removeFromFavorite(teamId: String) {
    databaseHelper.removeFavTeam(teamId, object : DatabaseListener {
      override fun onSuccess() {
        favState = false
        matchDetailView.setAsFavourite(favState)
        matchDetailView.showSnackbar("Removed from favorite")
      }

      override fun onFailed(message: String) {
        matchDetailView.showSnackbar(message)
      }
    })
  }

  private fun addToFavorite(team: Team) {
    databaseHelper.insertFavTeam(team, object : DatabaseListener {
      override fun onSuccess() {
        favState = true
        matchDetailView.setAsFavourite(favState)
        matchDetailView.showSnackbar("Added to favorite")
      }

      override fun onFailed(message: String) {
        matchDetailView.showSnackbar(message)
      }
    })
  }
}