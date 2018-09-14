package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.models.EventResponses
import com.neobyte.footbalschedule.models.LeagueResponse
import com.neobyte.footbalschedule.models.PlayerResponse
import com.neobyte.footbalschedule.models.SearchEventResponses
import com.neobyte.footbalschedule.models.TeamResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballMatchService {

  companion object {
    val instance: FootballMatchService by lazy {
      val retrofit = Retrofit.Builder()
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .baseUrl("https://www.thesportsdb.com/api/v1/json/4012917/")
          .build()
      retrofit.create(FootballMatchService::class.java)
    }
  }

  @GET("eventspastleague.php")
  fun getLastEvent(@Query("id") id: String): Observable<EventResponses>

  @GET("eventsnextleague.php")
  fun getNextEvent(@Query("id") id: String): Observable<EventResponses>

  @GET("lookupteam.php")
  fun getTeamDetail(@Query("id") id: String): Observable<TeamResponse>

  @GET("search_all_leagues.php?s=Soccer")
  fun getAllLeagues() : Observable<LeagueResponse>

  @GET("searchevents.php")
  fun searchEvents(@Query("e") team: String): Observable<SearchEventResponses>

  @GET("lookup_all_players.php")
  fun getAllPlayerTeam(@Query("id") teamId: String): Observable<PlayerResponse>

  @GET("lookup_all_teams.php")
  fun getAllTeams(@Query("id") leagueId: String): Observable<TeamResponse>

  @GET("searchteams.php")
  fun searchTeam(@Query("t") teamQuery: String): Observable<TeamResponse>

}