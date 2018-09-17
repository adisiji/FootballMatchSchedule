package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.SearchEventResponses
import com.neobyte.footbalschedule.search.SearchMatchPresenter
import com.neobyte.footbalschedule.search.SearchMatchView
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchMatchPresenterTest : BasePresenterTest() {

  private lateinit var searchMatchPresenter: SearchMatchPresenter

  @Mock
  lateinit var searchMatchView: SearchMatchView

  @Mock
  lateinit var matchService: FootballMatchService

  @Before
  fun setup() {
    searchMatchPresenter = SearchMatchPresenter(searchMatchView, matchService)
  }

  @Test
  fun testSearchTeamMatch_Success() {
    val listEvents = mutableListOf<Event>().apply {
      add(Event())
    }
    val eventResponse = SearchEventResponses(listEvents)
    val query = "anything"
    whenever(matchService.searchEvents(query)).thenReturn(Observable.just(eventResponse))
    searchMatchPresenter.searchTeamMatch(query)
    verify(searchMatchView).onLoading()
    verify(searchMatchView).onSuccess(listEvents)
  }

  @Test
  fun testSearchTeamMatch_Failed() {
    val query = "anything"
    val message = "Error search team match"
    whenever(matchService.searchEvents(query)).thenReturn(Observable.error(Throwable(message)))
    searchMatchPresenter.searchTeamMatch(query)
    verify(searchMatchView).onLoading()
    verify(searchMatchView).onFailed(message)
  }
}