package com.neobyte.footbalschedule.teamdetail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.neobyte.footbalschedule.models.Team
import com.neobyte.footbalschedule.teamdetail.overview.OverviewTeamFragment
import com.neobyte.footbalschedule.teamdetail.players.PlayerListFragment

class TeamDetailPagerAdapter(supportFragmentManager: FragmentManager,
                             private val team: Team) : FragmentPagerAdapter(
    supportFragmentManager) {

  override fun getItem(p0: Int): Fragment? {
    return when (p0) {
      0 -> OverviewTeamFragment.newInstance(team)
      1 -> PlayerListFragment.newInstance(team)
      else -> null
    }
  }

  override fun getCount() = 2

  override fun getPageTitle(position: Int): CharSequence? {
    return when (position) {
      0 -> "Overview"
      1 -> "Players"
      else -> null
    }
  }
}