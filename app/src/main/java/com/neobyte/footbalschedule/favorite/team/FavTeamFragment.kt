package com.neobyte.footbalschedule.favorite.team

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.teamdetail.TeamDetailActivity
import com.neobyte.footbalschedule.teams.TeamsAdapter
import kotlinx.android.synthetic.main.fragment_fav_team.rv_fav_team
import kotlinx.android.synthetic.main.fragment_fav_team.swipe_fav_team

class FavTeamFragment : Fragment(), FavTeamView {

  private val teams = mutableListOf<Team?>()
  private lateinit var adapter: TeamsAdapter
  private lateinit var presenter: FavTeamPresenter

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_fav_team, container, false)
  }

  override fun onViewCreated(view: View,
                             savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val databaseHelper = DatabaseHelper.getInstance(view.context)

    presenter = FavTeamPresenter(databaseHelper, this)

    adapter = TeamsAdapter(teams) { t,v ->
      TeamDetailActivity.navigate(activity!!, v, t)
    }

    val layoutManager = LinearLayoutManager(context)
    rv_fav_team.layoutManager = layoutManager
    rv_fav_team.adapter = adapter
    swipe_fav_team.setOnRefreshListener {
      presenter.getFavTeams()
    }
  }

  override fun onResume() {
    super.onResume()
    presenter.getFavTeams()
  }

  override fun onLoading() {
    swipe_fav_team.isRefreshing = true
  }

  override fun onSuccess(list: List<Team?>?) {
    list?.let {
      teams.clear()
      teams.addAll(list)
      adapter.notifyDataSetChanged()
    }
    swipe_fav_team.isRefreshing = false
  }

  override fun onError(message: String) {
    swipe_fav_team.isRefreshing = false
  }

}
