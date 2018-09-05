package com.neobyte.footbalschedule.next

import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.HomeScreenState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NextMatchPresenter(private val nextMatchView: NextMatchView,
                         private val footballMatchService: FootballMatchService) {

  fun getNextMatches() {
    nextMatchView.setScreenState(HomeScreenState.Loading)
    footballMatchService.getNextEvent()
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
  }

}