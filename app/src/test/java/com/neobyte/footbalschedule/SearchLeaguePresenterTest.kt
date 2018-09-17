package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.league.SearchLeaguePresenter
import com.neobyte.footbalschedule.league.SearchLeagueView
import com.neobyte.footbalschedule.models.League
import com.neobyte.footbalschedule.models.LeagueResponse
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchLeaguePresenterTest : BasePresenterTest() {

  private lateinit var presenter : SearchLeaguePresenter

  @Mock
  lateinit var searchLeagueView: SearchLeagueView

  @Mock
  lateinit var footballMatchService: FootballMatchService

  @Before
  fun setup () {
    presenter = SearchLeaguePresenter(searchLeagueView, footballMatchService)
  }

  @Test
  fun testGetAllLeague_Success() {
    val listLeagues = mutableListOf<League>().apply {
      add(League())
    }
    val leagueResponse = LeagueResponse(listLeagues)
    whenever(footballMatchService.getAllLeagues()).thenReturn(Observable.just(leagueResponse))
    presenter.getAllLeague()
    verify(searchLeagueView).onLoading()
    verify(searchLeagueView).onSuccess(listLeagues)
  }

  @Test
  fun testGetAllLeague_Failed() {
    val message = "Failed Get All Leagues"
    whenever(footballMatchService.getAllLeagues()).thenReturn(Observable.error(Throwable(message)))
    presenter.getAllLeague()
    verify(searchLeagueView).onLoading()
    verify(searchLeagueView).onError(message)
  }
}