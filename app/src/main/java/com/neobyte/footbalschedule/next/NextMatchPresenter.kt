package com.neobyte.footbalschedule.next

import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.HomeScreenState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NextMatchPresenter(private val nextMatchView: NextMatchView) {

  init {
    getNextMatches()
  }

  fun getNextMatches(){
    nextMatchView.setScreenState(HomeScreenState.Loading)
    FootballMatchService.instance.getNextEvent()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ eventResponses ->
          eventResponses.events?.let {
            nextMatchView.setScreenState(HomeScreenState.Data(it))
          }
        }, {
          nextMatchView.setScreenState(HomeScreenState.Error(it.message))
        })
  }

}