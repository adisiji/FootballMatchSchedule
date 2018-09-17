package com.neobyte.footbalschedule.search

import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.models.Event
import io.reactivex.Observable
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
            .flatMap {
              Observable.fromIterable(it.events)
            }
            .filter {t: Event ->
              t.strSport == "Soccer"
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                         searchMatchView.onSuccess(it)
                       }, {
                         searchMatchView.onFailed(it.localizedMessage)
                       })
    )
  }

  fun dispose() {
    compositeDisposable.clear()
  }

}