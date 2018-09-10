package com.neobyte.footbalschedule.matches

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.neobyte.footbalschedule.next.NextMatchFragment
import com.neobyte.footbalschedule.prev.PrevMatchFragment

class MatchesPagerAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(
    supportFragmentManager) {

  override fun getItem(p0: Int): Fragment? {
    return when (p0) {
      0 -> NextMatchFragment()
      1 -> PrevMatchFragment()
      else -> null
    }
  }

  override fun getCount() = 2

  override fun getPageTitle(position: Int): CharSequence? {
    return when(position) {
      0 -> "Next Match"
      1 -> "Prev Match"
      else -> null
    }
  }
}