package com.neobyte.footbalschedule.next

import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.HomeScreenState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NextMatchPresenter(private val nextMatchView: NextMatchView,
                         private val footballMatchService: FootballMatchService) {

  private val compositeDisposable = CompositeDisposable()

  fun getNextMatches(leagueId: String) {
    nextMatchView.setScreenState(HomeScreenState.Loading)
    compositeDisposable.add(
        footballMatchService.getNextEvent(leagueId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ eventResponses ->
                         eventResponses.events?.let {
                           nextMatchView.setScreenState(
                               HomeScreenState.Data(it)
                           )
                         }
                       }, {
                         nextMatchView.setScreenState(
                             HomeScreenState.Error(it.message)
                         )
                       })
    )
  }

  fun dispose() {
    compositeDisposable.clear()
  }

}