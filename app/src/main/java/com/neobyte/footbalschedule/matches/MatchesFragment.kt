package com.neobyte.footbalschedule.matches

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neobyte.footbalschedule.R
import kotlinx.android.synthetic.main.fragment_matches.tab_layout
import kotlinx.android.synthetic.main.fragment_matches.view_pager_matches

class MatchesFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_matches, container, false)
  }

  override fun onViewCreated(view: View,
                             savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val viewpagerAdapter = MatchesPagerAdapter(childFragmentManager)
    view_pager_matches.adapter = viewpagerAdapter
    view_pager_matches.currentItem = 0

    tab_layout.setupWithViewPager(view_pager_matches)
  }
}