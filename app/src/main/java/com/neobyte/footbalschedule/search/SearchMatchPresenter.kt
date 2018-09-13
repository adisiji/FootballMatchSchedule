package com.neobyte.footbalschedule.search

import com.neobyte.footbalschedule.FootballMatchService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchMatchPresenter(private val searchMatchView: SearchMatchView,
                           private val apiService: FootballMatchService) {

  private val compositeDisposable = CompositeDisposable()

  fun searchTeamMatch(query: String?) {
    searchMatchView.onLoading()
    compositeDisposable.add(
        apiService.searchEvents(query ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                         searchMatchView.onSuccess(it.events)
                       }, {
                         searchMatchView.onFailed(it.localizedMessage)
                       })
    )
  }

  fun dispose() {
    compositeDisposable.clear()
  }

}