package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.models.Player
import com.neobyte.footbalschedule.models.PlayerResponse
import com.neobyte.footbalschedule.teamdetail.players.PlayerListPresenter
import com.neobyte.footbalschedule.teamdetail.players.PlayerListView
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlayerListPresenterTest : BasePresenterTest() {

  private lateinit var playerListPresenter: PlayerListPresenter

  @Mock
  lateinit var playerListView: PlayerListView

  @Mock
  lateinit var footballMatchService: FootballMatchService

  val teamId = "1234"

  @Before
  fun setup() {
    playerListPresenter = PlayerListPresenter(footballMatchService, playerListView)
  }

  @Test
  fun testGetAllPlayers_Success() {
    val listPlayer = mutableListOf<Player>().apply {
      add(Player())
    }
    val playerResponse = PlayerResponse(listPlayer)
    whenever(footballMatchService.getAllPlayerTeam(teamId)).thenReturn(
        Observable.just(playerResponse))
    playerListPresenter.getAllPlayers(teamId)
    verify(playerListView).onLoading()
    verify(playerListView).onSuccess(listPlayer)
  }

  @Test
  fun testGetAllPlayers_Failed() {
    val message = "Failed Get All Players"
    whenever(footballMatchService.getAllPlayerTeam(teamId)).thenReturn(
        Observable.error(Throwable(message)))
    playerListPresenter.getAllPlayers(teamId)
    verify(playerListView).onLoading()
    verify(playerListView).onFailed(message)
  }

}