package com.neobyte.footbalschedule.prev

import com.neobyte.footbalschedule.BasePresenterTest
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.HomeScreenState
import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.EventResponses
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PrevMatchPresenterTest : BasePresenterTest() {

  @Mock
  lateinit var prevMatchView: PrevMatchView

  @Mock
  lateinit var footballMatchService: FootballMatchService

  lateinit var prevMatchPresenter: PrevMatchPresenter

  @Before
  fun setup() {
    prevMatchPresenter = PrevMatchPresenter(prevMatchView, footballMatchService)
  }

  @Test
  fun testGetprevMatch_Success() {
    val listEvent = mutableListOf<Event>().apply {
      add(Event())
    }
    val eventResponses = EventResponses(listEvent)
    Mockito.`when`(footballMatchService.getLastEvent())
        .thenReturn(Observable.just(eventResponses))

    val inorder = Mockito.inOrder(prevMatchView)
    prevMatchPresenter.getPrevMatches()
    inorder.verify(prevMatchView)
        .setScreenState(HomeScreenState.Loading)
    inorder.verify(prevMatchView)
        .setScreenState(HomeScreenState.Data(listEvent))
  }

  @Test
  fun testGetprevMatch_Failed() {
    val message = "Error get match"
    val error = Throwable(message)
    Mockito.`when`(footballMatchService.getLastEvent())
        .thenReturn(Observable.error(error))

    val inorder = Mockito.inOrder(prevMatchView)
    prevMatchPresenter.getPrevMatches()
    inorder.verify(prevMatchView)
        .setScreenState(HomeScreenState.Loading)
    inorder.verify(prevMatchView)
        .setScreenState(HomeScreenState.Error(any()))
  }
}