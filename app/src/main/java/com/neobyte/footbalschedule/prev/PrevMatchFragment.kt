package com.neobyte.footbalschedule.prev

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
import kotlinx.android.synthetic.main.fragment_prev_match.rv_next_match
import kotlinx.android.synthetic.main.fragment_prev_match.swipe_prev_layout

class PrevMatchFragment : Fragment(), PrevMatchView {

  lateinit var prevMatchPresenter: PrevMatchPresenter
  private var matches = mutableListOf<Event?>()
  private lateinit var adapter: MatchAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_prev_match, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter = MatchAdapter(matches) { pos ->
      val event = matches[pos]
      event?.let {
        val intent = Intent(context, MatchDetailActivity::class.java)
        intent.putExtra(Constants.EVENT, it)
        startActivity(intent)
      }
    }
    val layoutManager = LinearLayoutManager(context)
    rv_next_match.layoutManager = layoutManager
    rv_next_match.adapter = adapter
    swipe_prev_layout.setOnRefreshListener {
      prevMatchPresenter.getPrevMatches()
    }
  }

  override fun onResume() {
    super.onResume()
    prevMatchPresenter = PrevMatchPresenter(this)
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
}
