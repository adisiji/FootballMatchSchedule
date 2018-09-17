package com.neobyte.footbalschedule.favorite.match

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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

    adapter = MatchAdapter(matches, { pos ->
      val event = matches[pos]
      event?.let {
        val intent = Intent(context, MatchDetailActivity::class.java)
        intent.putExtra(Constants.EVENT, it)
        intent.putExtra(Constants.DONE_MATCH, false)
        startActivity(intent)
      }
    }, {
      addToCalendar(it)
    })

    val layoutManager = LinearLayoutManager(context)
    rv_fav_match.layoutManager = layoutManager
    rv_fav_match.adapter = adapter
    swipe_fav_match.setOnRefreshListener {
      presenter.getFavMatches()
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
