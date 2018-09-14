package com.neobyte.footbalschedule.teams

import com.neobyte.footbalschedule.FootballMatchService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TeamsPresenter(private val matchService: FootballMatchService,
                     private val teamsView: TeamsView) {

  private val compositeDisposable = CompositeDisposable()

  fun getTeams(leagueId: String) {
    teamsView.onLoading()
    compositeDisposable.add(
        matchService.getAllTeams(leagueId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                         teamsView.onSuccess(it.teams)
                       }, {
                         System.out.print(it.message)
                         teamsView.onFailed(it.localizedMessage)
                       })
    )
  }

  fun searchTeam(teamQuery: String) {
    teamsView.onLoading()
    compositeDisposable.add(
        matchService.searchTeam(teamQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                         teamsView.onSuccess(it.teams)
                       }, {
                         System.out.print(it.message)
                         teamsView.onFailed(it.localizedMessage)
                       })
    )
  }

  fun dispose() {
    compositeDisposable.clear()
  }
}