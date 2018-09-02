package com.neobyte.footbalschedule.next

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.neobyte.footbalschedule.Constants
import com.neobyte.footbalschedule.HomeScreenState
import com.neobyte.footbalschedule.MatchAdapter
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.detail.MatchDetailActivity
import com.neobyte.footbalschedule.models.Event
import kotlinx.android.synthetic.main.fragment_next_match.rv_next_match
import kotlinx.android.synthetic.main.fragment_next_match.swipe_next_layout

class NextMatchFragment : Fragment(), NextMatchView {

  lateinit var nextMatchPresenter: NextMatchPresenter
  private var matches = mutableListOf<Event?>()
  private lateinit var adapter: MatchAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_next_match, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter = MatchAdapter(matches) { pos ->
      val event = matches[pos]
      event?.let {
        val intent = Intent(context, MatchDetailActivity::class.java)
        intent.putExtra(Constants.EVENT, it)
        intent.putExtra(Constants.DONE_MATCH, false)
        startActivity(intent)
      }
    }
    val layoutManager = LinearLayoutManager(context)
    rv_next_match.layoutManager = layoutManager
    rv_next_match.adapter = adapter
    swipe_next_layout.setOnRefreshListener {
      nextMatchPresenter.getNextMatches()
    }
  }

  override fun onResume() {
    super.onResume()
    nextMatchPresenter = NextMatchPresenter(this)
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
}
