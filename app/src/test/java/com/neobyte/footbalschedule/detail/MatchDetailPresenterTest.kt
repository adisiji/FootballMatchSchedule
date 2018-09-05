package com.neobyte.footbalschedule.detail

import com.neobyte.footbalschedule.BasePresenterTest
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.Team
import com.nhaarman.mockitokotlin2.any
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
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
    matchDetailPresenter = MatchDetailPresenter(databaseHelper, matchDetailView)
  }

  @Test
  fun getTeam() {
    val teamId = "2323"
    val listResultTeam = mutableListOf<Team>().apply {
      add(fakeTeam)
    }
    whenever(footballMatchService.getTeamDetail(teamId)).thenReturn(
        Observable.error(Exception("adasd"))
    )
    matchDetailPresenter.getTeam(teamId, listener)
    verify(listener).onFailed(any())
  }

  @Test
  fun getFavorite() {

  }

  @Test
  fun setFavorite() {
    matchDetailPresenter.favState = true
    val event = Event()
    matchDetailPresenter.setFavorite(event)
    verify(matchDetailView).setAsFavourite(false)
  }

  @Test
  fun removeFromFavorite() {
  }

  @Test
  fun addToFavorite() {
  }
}