package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.detail.GetTeamListener
import com.neobyte.footbalschedule.detail.MatchDetailPresenter
import com.neobyte.footbalschedule.detail.MatchDetailView
import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.models.TeamResponse
import com.nhaarman.mockitokotlin2.any
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as whenever

class MatchDetailPresenterTest : BasePresenterTest() {

  @Mock
  lateinit var listener: GetTeamListener

  @Mock
  lateinit var footballMatchService: FootballMatchService

  @Mock
  lateinit var databaseHelper: DatabaseHelper

  @Mock
  lateinit var matchDetailView: MatchDetailView

  lateinit var matchDetailPresenter: MatchDetailPresenter

  private val fakeTeam = Team(strDivision = null, strTeamShort = null)

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)
    matchDetailPresenter =
        MatchDetailPresenter(databaseHelper, matchDetailView,
                                                                footballMatchService)
  }

  @Test
  fun getTeam_Success() {
    val teamId = "2323"
    val listResultTeam = mutableListOf<Team>().apply {
      add(fakeTeam)
    }
    val teamResponses = TeamResponse(listResultTeam)
    whenever(footballMatchService.getTeamDetail(teamId)).thenReturn(
        Observable.just(teamResponses))
    val inorder = inOrder(listener)
    matchDetailPresenter.getTeam(teamId, listener)
    inorder.verify(listener).onLoading()
    inorder.verify(listener).onSuccess(any())
  }

  @Test
  fun getTeam_Failed() {
    val teamId = "2323"
    whenever(footballMatchService.getTeamDetail(teamId)).thenReturn(
        Observable.error(Exception("adasd"))
    )
    val inorder = inOrder(listener)
    matchDetailPresenter.getTeam(teamId, listener)
    inorder.verify(listener).onLoading()
    inorder.verify(listener).onFailed(anyString())
  }
}