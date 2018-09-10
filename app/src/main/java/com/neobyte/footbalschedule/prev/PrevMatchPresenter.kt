package com.neobyte.footbalschedule.prev

import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.HomeScreenState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PrevMatchPresenter(private val prevMatchView: PrevMatchView,
                         private val footballMatchService: FootballMatchService) {

  fun getPrevMatches(leagueId: String) {
    prevMatchView.setScreenState(HomeScreenState.Loading)
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
  }
}