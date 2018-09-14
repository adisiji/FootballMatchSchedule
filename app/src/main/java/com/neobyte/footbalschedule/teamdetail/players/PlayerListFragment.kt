package com.neobyte.footbalschedule.teamdetail.players

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.models.Player
import com.neobyte.footbalschedule.models.Team
import kotlinx.android.synthetic.main.fragment_player_list.rv_team_players
import kotlinx.android.synthetic.main.fragment_player_list.swipe_player_list

class PlayerListFragment : Fragment(), PlayerListView {

  private lateinit var presenter: PlayerListPresenter
  private lateinit var adapter: PlayerListAdapter
  private val listPlayer = mutableListOf<Player?>()
  private var teamId = "0"

  companion object {

    private const val EXTRA_TEAM = "extra_team"

    fun newInstance(team: Team): PlayerListFragment {
      return PlayerListFragment().apply {
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_TEAM, team)
        arguments = bundle
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    presenter = PlayerListPresenter(FootballMatchService.instance, this)
  }

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val team = arguments?.getParcelable(EXTRA_TEAM) as Team
    teamId = team.idTeam ?: "0"

    return inflater.inflate(R.layout.fragment_player_list, container, false)
  }

  override fun onViewCreated(view: View,
                             savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = PlayerListAdapter(listPlayer)
    rv_team_players.layoutManager = LinearLayoutManager(context)
    rv_team_players.adapter = adapter

    swipe_player_list.setOnRefreshListener {
      presenter.getAllPlayers(teamId)
    }
  }

  override fun onResume() {
    super.onResume()
    presenter.getAllPlayers(teamId)
  }

  override fun onLoading() {
    swipe_player_list.isRefreshing = true
  }

  override fun onSuccess(list: List<Player?>?) {
    list?.let {
      listPlayer.clear()
      listPlayer.addAll(it)
      adapter.notifyDataSetChanged()
    }
    swipe_player_list.isRefreshing = false
  }

  override fun onFailed(message: String) {
    Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        .show()
    swipe_player_list.isRefreshing = false
  }

}
