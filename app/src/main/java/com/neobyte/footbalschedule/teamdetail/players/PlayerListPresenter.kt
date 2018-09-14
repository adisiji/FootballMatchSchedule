package com.neobyte.footbalschedule.teamdetail.players

import com.neobyte.footbalschedule.FootballMatchService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PlayerListPresenter(private val footballMatchService: FootballMatchService,
                          private val playerListView: PlayerListView) {

  fun getAllPlayers(teamId: String) {
    playerListView.onLoading()
    footballMatchService.getAllPlayerTeam(teamId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
                      playerListView.onSuccess(it.player)
                   }, {
                      System.out.print(it)
                      playerListView.onFailed(it.localizedMessage)
                   })
  }
}
