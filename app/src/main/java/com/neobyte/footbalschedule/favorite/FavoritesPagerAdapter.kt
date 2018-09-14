package com.neobyte.footbalschedule.favorite

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.neobyte.footbalschedule.favorite.match.FavMatchFragment
import com.neobyte.footbalschedule.favorite.team.FavTeamFragment

class FavoritesPagerAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(
    supportFragmentManager) {

  override fun getItem(p0: Int): Fragment? {
    return when (p0) {
      0 -> FavMatchFragment()
      1 -> FavTeamFragment()
      else -> null
    }
  }

  override fun getCount() = 2

  override fun getPageTitle(position: Int): CharSequence? {
    return when(position) {
      0 -> "Matches"
      1 -> "Teams"
      else -> null
    }
  }
}