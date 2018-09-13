package com.neobyte.footbalschedule.prev

import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.HomeScreenState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PrevMatchPresenter(private val prevMatchView: PrevMatchView,
                         private val footballMatchService: FootballMatchService) {

  private val compositeDisposable = CompositeDisposable()

  fun getPrevMatches(leagueId: String) {
    prevMatchView.setScreenState(HomeScreenState.Loading)
    compositeDisposable.add(
        footballMatchService.getLastEvent(leagueId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ eventResponses ->
                         eventResponses.events?.let {
                           prevMatchView.setScreenState(HomeScreenState.Data(it))
                         }
                       }, {
                         prevMatchView.setScreenState(HomeScreenState.Error(it.message))
                       })
    )
  }

  fun dispose() {
    compositeDisposable.clear()
  }
}