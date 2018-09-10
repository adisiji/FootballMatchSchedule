package com.neobyte.footbalschedule.league

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.neobyte.footbalschedule.Constants
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.models.League
import kotlinx.android.synthetic.main.activity_search_league.rv_all_league
import kotlinx.android.synthetic.main.activity_search_league.swipe_league_layout

class SearchLeagueActivity : AppCompatActivity(), SearchLeagueView {

  companion object {
    const val SEARCH_LEAGUE_CODE = 2109
  }

  private lateinit var presenter: SearchLeaguePresenter
  private lateinit var adapter: SearchLeagueAdapter
  private val leagueList = mutableListOf<League?>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search_league)

    presenter = SearchLeaguePresenter(this, FootballMatchService.instance)
    adapter = SearchLeagueAdapter(leagueList) {
      val intent = Intent().apply {
        putExtra(Constants.LEAGUE_NAME, it.strLeague)
        putExtra(Constants.LEAGUE_ID, it.idLeague)
      }
      setResult(Activity.RESULT_OK, intent)
      finish()
    }
    val linearLayoutManager = LinearLayoutManager(this)
    rv_all_league.layoutManager = linearLayoutManager
    rv_all_league.adapter = adapter
  }

  override fun onResume() {
    super.onResume()
    presenter.getAllLeague()
  }

  override fun onLoading() {
    swipe_league_layout.isRefreshing = true
  }

  override fun onSuccess(list: List<League?>?) {
    swipe_league_layout.isRefreshing = false
    list?.let {
      leagueList.clear()
      leagueList.addAll(list)
      adapter.notifyDataSetChanged()
    }
  }

  override fun onError(message: String) {
    swipe_league_layout.isRefreshing = false
    val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
    snackbar.setAction("Retry") {
      presenter.getAllLeague()
    }
    snackbar.show()
  }
}
