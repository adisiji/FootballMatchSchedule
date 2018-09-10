package com.neobyte.footbalschedule.league

import com.neobyte.footbalschedule.FootballMatchService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchLeaguePresenter(private val searchLeagueView: SearchLeagueView,
                            private val footballMatchService: FootballMatchService) {

  fun getAllLeague() {
    searchLeagueView.onLoading()
    footballMatchService.getAllLeagues()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          searchLeagueView.onSuccess(it.leagues)
                   }, {
          it.printStackTrace()
          searchLeagueView.onError(it.localizedMessage)
        })
  }
}