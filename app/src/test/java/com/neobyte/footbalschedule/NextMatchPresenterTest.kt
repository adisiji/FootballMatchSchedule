package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.EventResponses
import com.neobyte.footbalschedule.next.NextMatchPresenter
import com.neobyte.footbalschedule.next.NextMatchView
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@RunWith(MockitoJUnitRunner::class)
class NextMatchPresenterTest : BasePresenterTest() {

  @Mock
  lateinit var nextMatchView: NextMatchView

  @Mock
  lateinit var footballMatchService: FootballMatchService

  private lateinit var nextMatchPresenter: NextMatchPresenter

  val id = "1234"

  @Before
  fun setup() {
    nextMatchPresenter =
        NextMatchPresenter(nextMatchView, footballMatchService)
  }

  @Test
  fun testGetNextMatch_Success() {
    val listEvent = mutableListOf<Event>().apply {
      add(Event())
    }
    val eventResponses = EventResponses(listEvent)
    whenever(footballMatchService.getNextEvent(id)).thenReturn(
        Observable.just(eventResponses)
    )

    val inorder = inOrder(nextMatchView)
    nextMatchPresenter.getNextMatches(id)
    inorder.verify(nextMatchView).setScreenState(HomeScreenState.Loading)
    inorder.verify(nextMatchView).setScreenState(HomeScreenState.Data(listEvent))
  }

  @Test
  fun testGetNextMatch_Failed() {
    val message = "Error get match"
    val error = Throwable(message)
    whenever(footballMatchService.getNextEvent(id)).thenReturn(
        Observable.error(error)
    )

    val inorder = inOrder(nextMatchView)
    nextMatchPresenter.getNextMatches(id)
    inorder.verify(nextMatchView).setScreenState(HomeScreenState.Loading)
    inorder.verify(nextMatchView).setScreenState(HomeScreenState.Error(any()))
  }
}