package com.neobyte.footbalschedule.prev

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
import com.neobyte.footbalschedule.Constants.LEAGUE_ID
import com.neobyte.footbalschedule.Constants.LEAGUE_NAME
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.HomeScreenState
import com.neobyte.footbalschedule.MatchAdapter
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.detail.MatchDetailActivity
import com.neobyte.footbalschedule.league.SearchLeagueActivity
import com.neobyte.footbalschedule.models.Event
import kotlinx.android.synthetic.main.fragment_next_match.tv_league
import kotlinx.android.synthetic.main.fragment_prev_match.rv_prev_match
import kotlinx.android.synthetic.main.fragment_prev_match.swipe_prev_layout

class PrevMatchFragment : Fragment(), PrevMatchView {

  lateinit var prevMatchPresenter: PrevMatchPresenter
  private var matches = mutableListOf<Event?>()
  private lateinit var adapter: MatchAdapter
  private var leagueId = "4328"
  private var leagueName = "English Premiere League"

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_prev_match, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if (savedInstanceState != null) {
      leagueName = savedInstanceState.getString(LEAGUE_NAME, "English Premiere League")
      leagueId = savedInstanceState.getString(LEAGUE_ID, "4328")
    }
  }

  override fun onViewCreated(view: View,
                             savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    prevMatchPresenter = PrevMatchPresenter(this, FootballMatchService.instance)

    adapter = MatchAdapter(matches) { pos ->
      val event = matches[pos]
      event?.let {
        val intent = Intent(context, MatchDetailActivity::class.java)
        intent.putExtra(Constants.EVENT, it)
        startActivity(intent)
      }
    }
    val layoutManager = LinearLayoutManager(context)
    rv_prev_match.layoutManager = layoutManager
    rv_prev_match.adapter = adapter
    swipe_prev_layout.setOnRefreshListener {
      prevMatchPresenter.getPrevMatches(leagueId)
    }

    tv_league.setOnClickListener {
      startActivityForResult(Intent(context, SearchLeagueActivity::class.java),
                             SearchLeagueActivity.SEARCH_LEAGUE_CODE)
    }
  }

  override fun onResume() {
    super.onResume()
    prevMatchPresenter.getPrevMatches(leagueId)
  }

  override fun onActivityResult(requestCode: Int,
                                resultCode: Int,
                                data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == SearchLeagueActivity.SEARCH_LEAGUE_CODE) {
      if (resultCode == Activity.RESULT_OK && data != null) {
        leagueName = data.getStringExtra(LEAGUE_NAME)
        tv_league.text = leagueName
        leagueId = data.getStringExtra(LEAGUE_ID)
        prevMatchPresenter.getPrevMatches(leagueId)
      }
    }
  }

  override fun setScreenState(homeScreenState: HomeScreenState) {
    when (homeScreenState) {
      is HomeScreenState.Error -> {
        swipe_prev_layout.isRefreshing = false
        Toast.makeText(context, homeScreenState.message, Toast.LENGTH_SHORT)
            .show()
      }
      is HomeScreenState.Loading -> {
        swipe_prev_layout.isRefreshing = true
      }
      is HomeScreenState.Data -> {
        matches.clear()
        matches.addAll(homeScreenState.eventResponse)
        adapter.notifyDataSetChanged()
        swipe_prev_layout.isRefreshing = false
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(LEAGUE_NAME, leagueName)
    outState.putString(LEAGUE_ID, leagueId)
  }
}
