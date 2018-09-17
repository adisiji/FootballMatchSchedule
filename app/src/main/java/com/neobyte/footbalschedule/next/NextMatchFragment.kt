package com.neobyte.footbalschedule.next

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
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
import com.neobyte.footbalschedule.league.SearchLeagueActivity.Companion.SEARCH_LEAGUE_CODE
import com.neobyte.footbalschedule.models.Event
import kotlinx.android.synthetic.main.fragment_next_match.league_name
import kotlinx.android.synthetic.main.fragment_next_match.rv_next_match
import kotlinx.android.synthetic.main.fragment_next_match.swipe_next_layout
import kotlinx.android.synthetic.main.fragment_next_match.tv_league
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NextMatchFragment : Fragment(), NextMatchView {

  lateinit var nextMatchPresenter: NextMatchPresenter
  private var matches = mutableListOf<Event?>()
  private lateinit var adapter: MatchAdapter
  private var leagueId = "4328"
  private var leagueName = "English Premiere League"

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_next_match, container, false)
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

    nextMatchPresenter = NextMatchPresenter(this, FootballMatchService.instance)

    adapter = MatchAdapter(matches, { pos: Int ->
      val event = matches[pos]
      event?.let {
        val intent = Intent(context, MatchDetailActivity::class.java)
        intent.putExtra(Constants.EVENT, it)
        intent.putExtra(Constants.DONE_MATCH, false)
        startActivity(intent)
      }
    }) { pos ->
      addToCalendar(pos)
    }
    val layoutManager = LinearLayoutManager(context)
    rv_next_match.layoutManager = layoutManager
    rv_next_match.adapter = adapter
    swipe_next_layout.setOnRefreshListener {
      nextMatchPresenter.getNextMatches(leagueId)
    }

    league_name.setOnClickListener {
      startActivityForResult(Intent(context, SearchLeagueActivity::class.java), SEARCH_LEAGUE_CODE)
    }

  }

  private fun addToCalendar(pos: Int) {
    val match = matches[pos]
    match?.let {
      val sdf = SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss", Locale.getDefault())
      sdf.timeZone = TimeZone.getTimeZone("GMT")
      val time = it.strTime?.substringBefore("+")
      val myDate = sdf.parse("${it.dateEvent} T $time")

      val startTime = myDate.time
      val endTime = startTime + 90 * 60 * 1000

      val intent = Intent(Intent.ACTION_INSERT)
      intent.data = CalendarContract.Events.CONTENT_URI

      intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
      intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)

      intent.putExtra(Events.TITLE, it.strEvent)
      intent.putExtra(Events.DESCRIPTION, it.strFilename)
      intent.putExtra(Events.RRULE, "FREQ=DAILY;COUNT=1")

      startActivity(intent)
    }
  }

  override fun onResume() {
    super.onResume()
    nextMatchPresenter.getNextMatches(leagueId)
  }

  override fun onActivityResult(requestCode: Int,
                                resultCode: Int,
                                data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == SEARCH_LEAGUE_CODE) {
      if (resultCode == RESULT_OK && data != null) {
        leagueName = data.getStringExtra(LEAGUE_NAME)
        tv_league.text = leagueName
        leagueId = data.getStringExtra(LEAGUE_ID)
        nextMatchPresenter.getNextMatches(leagueId)
      }
    }
  }

  override fun setScreenState(homeScreenState: HomeScreenState) {
    when (homeScreenState) {
      is HomeScreenState.Error -> {
        swipe_next_layout.isRefreshing = false
        Toast.makeText(context, homeScreenState.message, Toast.LENGTH_SHORT)
            .show()
      }
      is HomeScreenState.Loading -> {
        swipe_next_layout.isRefreshing = true
      }
      is HomeScreenState.Data -> {
        matches.clear()
        matches.addAll(homeScreenState.eventResponse)
        adapter.notifyDataSetChanged()
        swipe_next_layout.isRefreshing = false
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(LEAGUE_NAME, leagueName)
    outState.putString(LEAGUE_ID, leagueId)
  }

  override fun onStop() {
    nextMatchPresenter.dispose()
    super.onStop()
  }
}
