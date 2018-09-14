package com.neobyte.footbalschedule.teamdetail.overview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.models.Team
import kotlinx.android.synthetic.main.fragment_overview_team.tv_description_content
import kotlinx.android.synthetic.main.fragment_overview_team.tv_established
import kotlinx.android.synthetic.main.fragment_overview_team.tv_location
import kotlinx.android.synthetic.main.fragment_overview_team.tv_manager
import kotlinx.android.synthetic.main.fragment_overview_team.tv_stadium

class OverviewTeamFragment : Fragment() {

  companion object {

    private val EXTRA_TEAM = "extra_team"

    fun newInstance(team: Team): OverviewTeamFragment {
      return OverviewTeamFragment().apply {
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_TEAM, team)
        arguments = bundle
      }
    }
  }

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_overview_team, container, false)
  }

  override fun onViewCreated(view: View,
                             savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val team = arguments?.getParcelable(EXTRA_TEAM) as Team

    Log.d("Team", team.idTeam)

    tv_description_content.text = team.strDescriptionEN
    tv_established.text = team.intFormedYear
    tv_manager.text = team.strManager
    tv_stadium.text = team.strStadium
    tv_location.text = team.strStadiumLocation
  }
}
