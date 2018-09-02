package com.neobyte.footbalschedule.detail

import android.util.Log
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.models.Team
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MatchDetailPresenter {

  private val compositeDisposable = CompositeDisposable()

  fun getTeam(teamId: String, listener: GetTeamListener) {
    listener.onLoading()
    compositeDisposable.add(FootballMatchService.instance.getTeamDetail(teamId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          it.teams?.let { list ->
            list[0]?.let { team -> listener.onSuccess(team) }
          }
        }, {
          Log.e("Get Team", "Failed", it)
          listener.onFailed(it.message)
        })
    )
  }

  fun composeSubscriber() {
    compositeDisposable.dispose()
  }
}

interface GetTeamListener {
  fun onLoading()
  fun onSuccess(team: Team)
  fun onFailed(message: String?)
}