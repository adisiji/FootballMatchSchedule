package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.EventResponses
import com.neobyte.footbalschedule.prev.PrevMatchPresenter
import com.neobyte.footbalschedule.prev.PrevMatchView
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@RunWith(MockitoJUnitRunner::class)
class PrevMatchPresenterTest : BasePresenterTest() {

  @Mock
  lateinit var prevMatchView: PrevMatchView

  @Mock
  lateinit var footballMatchService: FootballMatchService

  private lateinit var prevMatchPresenter: PrevMatchPresenter

  @Before
  fun setup() {
    prevMatchPresenter =
        PrevMatchPresenter(prevMatchView, footballMatchService)
  }

  @Test
  fun testGetPrevMatch_Success() {
    val listEvent = mutableListOf<Event>().apply {
      add(Event())
    }
    val eventResponses = EventResponses(listEvent)
    whenever(footballMatchService.getLastEvent())
        .thenReturn(Observable.just(eventResponses))

    val inorder = Mockito.inOrder(prevMatchView)
    prevMatchPresenter.getPrevMatches()
    inorder.verify(prevMatchView)
        .setScreenState(HomeScreenState.Loading)
    inorder.verify(prevMatchView)
        .setScreenState(HomeScreenState.Data(listEvent))
  }

  @Test
  fun testGetPrevMatch_Failed() {
    val message = "Error get match"
    val error = Throwable(message)
    whenever(footballMatchService.getLastEvent())
        .thenReturn(Observable.error(error))

    val inorder = Mockito.inOrder(prevMatchView)
    prevMatchPresenter.getPrevMatches()
    inorder.verify(prevMatchView)
        .setScreenState(HomeScreenState.Loading)
    inorder.verify(prevMatchView)
        .setScreenState(HomeScreenState.Error(any()))
  }
}