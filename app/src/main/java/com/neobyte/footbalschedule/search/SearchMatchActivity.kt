package com.neobyte.footbalschedule.search

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import com.neobyte.footbalschedule.Constants
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.MatchAdapter
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.RxSearchObservable
import com.neobyte.footbalschedule.detail.MatchDetailActivity
import com.neobyte.footbalschedule.models.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search_match.rv_search_match
import kotlinx.android.synthetic.main.activity_search_match.swipe_search_match_layout
import kotlinx.android.synthetic.main.activity_search_match.tv_no_data
import java.util.concurrent.TimeUnit

class SearchMatchActivity : AppCompatActivity(), SearchMatchView {

  private lateinit var adapter: MatchAdapter
  private var searchView: SearchView? = null
  private val matches = mutableListOf<Event?>()
  private lateinit var presenter: SearchMatchPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search_match)

    presenter = SearchMatchPresenter(this, FootballMatchService.instance)

    supportActionBar?.let {
      it.title = "Search Match"
      it.setDisplayHomeAsUpEnabled(true)
      it.setDisplayShowHomeEnabled(true)
    }

    adapter = MatchAdapter(matches) { pos ->
      val event = matches[pos]
      event?.let {
        val intent = Intent(this, MatchDetailActivity::class.java)
        intent.putExtra(Constants.EVENT, it)
        intent.putExtra(Constants.DONE_MATCH, true)
        startActivity(intent)
      }
    }

    rv_search_match.layoutManager = LinearLayoutManager(this)
    rv_search_match.adapter = adapter

    swipe_search_match_layout.setOnRefreshListener {
      searchView?.let {
        presenter.searchTeamMatch(it.query.toString())
      }
    }

  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_search, menu)
    val searchItem = menu?.findItem(R.id.search)
    var before = false
    searchView = searchItem?.actionView as SearchView
    searchView?.let {
      it.queryHint = "Search Match"
      it.setIconifiedByDefault(true)
      RxSearchObservable.fromView(it)
          .debounce(300, TimeUnit.MILLISECONDS)
          .observeOn(AndroidSchedulers.mainThread())
          .filter { text ->
            if (text.isEmpty()) {
              clearItems()
              false
            } else {
              tv_no_data.visibility = GONE
              true
            }
          }
          .distinctUntilChanged()
          .subscribe({
                       presenter.searchTeamMatch(it)
                     }, {
                       Log.e("RxSearchObservable", "Failed", it)
                     })
    }

    return true
  }

  private fun clearItems() {
    matches.clear()
    adapter.notifyDataSetChanged()
    tv_no_data.visibility = VISIBLE
  }

  override fun onLoading() {
    swipe_search_match_layout.isRefreshing = true
  }

  override fun onSuccess(list: List<Event?>?) {
    list?.let {
      matches.clear()
      matches.addAll(it)
      adapter.notifyDataSetChanged()
      if (matches.isEmpty()) tv_no_data.visibility = VISIBLE
    }
    if (list == null) tv_no_data.visibility = VISIBLE
    swipe_search_match_layout.isRefreshing = false
  }

  override fun onFailed(message: String) {
    swipe_search_match_layout.isRefreshing = false
  }

  override fun onDestroy() {
    presenter.dispose()
    super.onDestroy()
  }
}
