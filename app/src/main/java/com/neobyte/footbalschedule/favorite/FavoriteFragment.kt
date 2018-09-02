package com.neobyte.footbalschedule.favorite

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
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.detail.MatchDetailActivity
import com.neobyte.footbalschedule.models.Event
import kotlinx.android.synthetic.main.fragment_favorite.rv_fav_match
import kotlinx.android.synthetic.main.fragment_favorite.swipe_fav_layout

class FavoriteFragment : Fragment(), FavoriteMatchView {

  lateinit var favoriteMatchPresenter: FavoriteMatchPresenter
  private var matches = mutableListOf<Event?>()
  private lateinit var adapter: MatchAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_favorite, container, false)
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
    rv_fav_match.layoutManager = layoutManager
    rv_fav_match.adapter = adapter
    swipe_fav_layout.setOnRefreshListener {
      favoriteMatchPresenter.getFavMatch()
    }
  }

  override fun onResume() {
    super.onResume()
    val databaseHelper = DatabaseHelper(context!!)
    favoriteMatchPresenter = FavoriteMatchPresenter(this, databaseHelper)
  }

  override fun setScreenState(homeScreenState: HomeScreenState) {
    when (homeScreenState) {
      is HomeScreenState.Error -> {
        swipe_fav_layout.isRefreshing = false
        Toast.makeText(context, homeScreenState.message, Toast.LENGTH_SHORT)
            .show()
      }
      is HomeScreenState.Loading -> {
        swipe_fav_layout.isRefreshing = true
      }
      is HomeScreenState.Data -> {
        matches.clear()
        matches.addAll(homeScreenState.eventResponse)
        adapter.notifyDataSetChanged()
        swipe_fav_layout.isRefreshing = false
      }
    }
  }
}
