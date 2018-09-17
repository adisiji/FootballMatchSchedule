package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.models.TeamResponse
import com.neobyte.footbalschedule.teams.TeamsPresenter
import com.neobyte.footbalschedule.teams.TeamsView
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TeamsPresenterTest : BasePresenterTest() {

  @Mock
  lateinit var teamsView: TeamsView

  @Mock
  lateinit var footballMatchService: FootballMatchService

  private lateinit var teamsPresenter: TeamsPresenter

  private val leagueId = "1234"
  private val teamQuery = "AbSb"

  @Before
  fun setup() {
    teamsPresenter = TeamsPresenter(footballMatchService, teamsView)
  }

  @Test
  fun testGetAllTeams_Success() {
    val listTeam = mutableListOf<Team>().apply {
      add(Team())
    }
    val teamsResponse = TeamResponse(listTeam)
    whenever(footballMatchService.getAllTeams(leagueId)).thenReturn(Observable.just(teamsResponse))
    teamsPresenter.getTeams(leagueId)
    verify(teamsView).onLoading()
    verify(teamsView).onSuccess(listTeam)
  }

  @Test
  fun testGetAllTeams_Failed() {
    val message = "Error Get Teams"

    whenever(footballMatchService.getAllTeams(leagueId)).thenReturn(Observable.error(
        Throwable(message)))
    teamsPresenter.getTeams(leagueId)
    verify(teamsView).onLoading()
    verify(teamsView).onFailed(message)
  }

  @Test
  fun testSearchTeam_Success() {
    val listTeam = mutableListOf<Team>().apply {
      add(Team())
    }
    val teamsResponse = TeamResponse(listTeam)
    whenever(footballMatchService.searchTeam(teamQuery)).thenReturn(Observable.just(teamsResponse))
    teamsPresenter.searchTeam(teamQuery)
    verify(teamsView).onLoading()
    verify(teamsView).onSuccess(listTeam)
  }

  @Test
  fun testSearchTeam_Failed() {
    val message = "Error Search Teams"

    whenever(footballMatchService.searchTeam(teamQuery)).thenReturn(Observable.error(
        Throwable(message)))
    teamsPresenter.searchTeam(teamQuery)
    verify(teamsView).onLoading()
    verify(teamsView).onFailed(message)
  }
}