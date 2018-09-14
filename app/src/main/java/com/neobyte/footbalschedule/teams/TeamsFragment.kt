package com.neobyte.footbalschedule.teams

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.neobyte.footbalschedule.Constants
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.RxSearchObservable
import com.neobyte.footbalschedule.league.SearchLeagueActivity
import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.teamdetail.TeamDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_teams.league_name
import kotlinx.android.synthetic.main.fragment_teams.rv_teams
import kotlinx.android.synthetic.main.fragment_teams.swipe_teams_layout
import kotlinx.android.synthetic.main.fragment_teams.tv_league
import java.util.concurrent.TimeUnit

class TeamsFragment : Fragment(), TeamsView {

  private lateinit var presenter: TeamsPresenter
  private lateinit var adapter: TeamsAdapter
  private val teams = mutableListOf<Team?>()
  private var leagueId = "4328"
  private var leagueName = "English Premiere League"
  private var searchView: SearchView? = null

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    presenter = TeamsPresenter(FootballMatchService.instance, this)
    setHasOptionsMenu(true)
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

  override fun onCreateOptionsMenu(menu: Menu?,
                                   inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_search, menu)
    val searchItem = menu?.findItem(R.id.search)
    searchView = searchItem?.actionView as SearchView
    searchView?.let {
      it.queryHint = "Search Team"
      it.setIconifiedByDefault(true)
      RxSearchObservable.fromView(it)
          .debounce(300, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .filter { text ->
            if (text.isEmpty()) {
              clearItems()
              false
            } else true
          }
          .distinctUntilChanged()
          .subscribe({ query ->
                       presenter.searchTeam(query)
                     }, { e ->
                       Log.e("RxSearchObservable", "Failed", e)
                     })
    }

    searchItem.setOnActionExpandListener(object : OnActionExpandListener {
      override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        league_name.visibility = GONE
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        league_name.visibility = VISIBLE
        presenter.getTeams(leagueId)
        return true
      }

    })

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

  private fun clearItems() {
    teams.clear()
    adapter.notifyDataSetChanged()
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
