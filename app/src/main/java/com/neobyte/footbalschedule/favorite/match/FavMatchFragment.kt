package com.neobyte.footbalschedule.favorite.match

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neobyte.footbalschedule.Constants
import com.neobyte.footbalschedule.MatchAdapter

import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.detail.MatchDetailActivity
import com.neobyte.footbalschedule.models.Event
import kotlinx.android.synthetic.main.fragment_fav_match.rv_fav_match
import kotlinx.android.synthetic.main.fragment_fav_match.swipe_fav_match

class FavMatchFragment : Fragment(), FavMatchView {

  private val matches = mutableListOf<Event?>()
  private lateinit var adapter: MatchAdapter
  private lateinit var presenter: FavMatchPresenter

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_fav_match, container, false)
  }

  override fun onViewCreated(view: View,
                             savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val databaseHelper = DatabaseHelper.getInstance(view.context)

    presenter = FavMatchPresenter(databaseHelper, this)

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
    swipe_fav_match.setOnRefreshListener {
      presenter.getFavMatches()
    }
  }

  override fun onResume() {
    super.onResume()
    presenter.getFavMatches()
  }

  override fun onLoading() {
    swipe_fav_match.isRefreshing = true
  }

  override fun onSuccess(list: List<Event?>?) {
    list?.let {
      matches.clear()
      matches.addAll(list)
      adapter.notifyDataSetChanged()
    }
    swipe_fav_match.isRefreshing = false
  }

  override fun onError(message: String) {
    swipe_fav_match.isRefreshing = false
  }
}
