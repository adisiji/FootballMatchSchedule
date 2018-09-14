package com.neobyte.footbalschedule.teams

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.neobyte.footbalschedule.Constants
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.league.SearchLeagueActivity
import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.teamdetail.TeamDetailActivity
import kotlinx.android.synthetic.main.fragment_next_match.tv_league
import kotlinx.android.synthetic.main.fragment_prev_match.league_name
import kotlinx.android.synthetic.main.fragment_teams.rv_teams
import kotlinx.android.synthetic.main.fragment_teams.swipe_teams_layout

class TeamsFragment : Fragment(), TeamsView {

  private lateinit var presenter: TeamsPresenter
  private lateinit var adapter: TeamsAdapter
  private val teams = mutableListOf<Team?>()
  private var leagueId = "4328"
  private var leagueName = "English Premiere League"

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    presenter = TeamsPresenter(FootballMatchService.instance, this)
    return inflater.inflate(R.layout.fragment_teams, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if (savedInstanceState != null) {
      leagueName = savedInstanceState.getString(Constants.LEAGUE_NAME, "English Premiere League")
      leagueId = savedInstanceState.getString(Constants.LEAGUE_ID, "4328")
    }
  }

  override fun onViewCreated(view: View,
                             savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter = TeamsAdapter(teams) { t, v ->
      TeamDetailActivity.navigate(activity!!, v, t)
    }

    rv_teams.layoutManager = LinearLayoutManager(context)
    rv_teams.adapter = adapter

    swipe_teams_layout.setOnRefreshListener {
      presenter.getTeams(leagueId)
    }

    league_name.setOnClickListener {
      startActivityForResult(Intent(context, SearchLeagueActivity::class.java),
                             SearchLeagueActivity.SEARCH_LEAGUE_CODE)
    }

    presenter.getTeams(leagueId)
  }

  override fun onActivityResult(requestCode: Int,
                                resultCode: Int,
                                data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == SearchLeagueActivity.SEARCH_LEAGUE_CODE) {
      if (resultCode == Activity.RESULT_OK && data != null) {
        leagueName = data.getStringExtra(Constants.LEAGUE_NAME)
        tv_league.text = leagueName
        leagueId = data.getStringExtra(Constants.LEAGUE_ID)
        presenter.getTeams(leagueId)
      }
    }
  }

  override fun onLoading() {
    swipe_teams_layout.isRefreshing = true
  }

  override fun onSuccess(list: List<Team?>?) {
    list?.let {
      teams.clear()
      teams.addAll(list)
      adapter.notifyDataSetChanged()
    }
    swipe_teams_layout.isRefreshing = false
  }

  override fun onFailed(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
    swipe_teams_layout.isRefreshing = false
  }

  override fun onStop() {
    presenter.dispose()
    super.onStop()
  }
}
