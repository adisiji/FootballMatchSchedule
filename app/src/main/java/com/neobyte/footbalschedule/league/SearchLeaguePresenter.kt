package com.neobyte.footbalschedule.league

import com.neobyte.footbalschedule.FootballMatchService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchLeaguePresenter(private val searchLeagueView: SearchLeagueView,
                            private val footballMatchService: FootballMatchService) {

  private val compositeDisposable = CompositeDisposable()

  fun getAllLeague() {
    searchLeagueView.onLoading()
    compositeDisposable.add(
        footballMatchService.getAllLeagues()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                         searchLeagueView.onSuccess(it.leagues)
                       }, {
                         it.printStackTrace()
                         searchLeagueView.onError(it.localizedMessage)
                       })
    )
  }

  fun dispose() {
    compositeDisposable.clear()
  }
}