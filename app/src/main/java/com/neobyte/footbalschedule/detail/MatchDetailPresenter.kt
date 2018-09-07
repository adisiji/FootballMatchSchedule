package com.neobyte.footbalschedule.detail

import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.db.DatabaseListener
import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.Team
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MatchDetailPresenter(private val databaseHelper: DatabaseHelper,
                           private val matchDetailView: MatchDetailView,
                           private val footballMatchService: FootballMatchService) {

  var favState = false
  private val compositeDisposable = CompositeDisposable()

  fun getTeam(teamId: String,
              listener: GetTeamListener) {
    listener.onLoading()
    compositeDisposable.add(
        footballMatchService.getTeamDetail(teamId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                         it.teams?.let { list ->
                           list[0]?.let { team ->
                             listener.onSuccess(team)
                           }
                         }
                       }, {
                         //                         Log.e("Get Team", "Failed", it)
                         listener.onFailed(it.message)
                       })
    )
  }

  fun composeSubscriber() {
    compositeDisposable.dispose()
  }

  fun getFavorite(eventId: String) {
    val favState = databaseHelper.isMatchFavorite(eventId)
    matchDetailView.setAsFavourite(favState)
  }

  fun setFavorite(event: Event) {
    if (favState) {
      removeFromFavorite(event.idEvent ?: "")
    } else {
      addToFavorite(event)
    }
  }

  private fun removeFromFavorite(eventId: String) {
    databaseHelper.removeFavMatch(eventId, object : DatabaseListener {
      override fun onSuccess() {
        matchDetailView.setAsFavourite(false)
        matchDetailView.showSnackbar("Removed from favorite")
        favState = !favState
      }

      override fun onFailed(message: String) {
        matchDetailView.showSnackbar(message)
      }
    })
  }

  private fun addToFavorite(event: Event) {
    databaseHelper.insertFavMatch(event, object : DatabaseListener {
      override fun onSuccess() {
        matchDetailView.setAsFavourite(true)
        matchDetailView.showSnackbar("Added to favorite")
        favState = !favState
      }

      override fun onFailed(message: String) {
        matchDetailView.showSnackbar(message)
      }
    })
  }
}

interface GetTeamListener {
  fun onLoading()
  fun onSuccess(team: Team)
  fun onFailed(message: String?)
}