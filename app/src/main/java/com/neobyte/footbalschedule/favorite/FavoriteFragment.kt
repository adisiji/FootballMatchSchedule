package com.neobyte.footbalschedule.favorite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neobyte.footbalschedule.R
import kotlinx.android.synthetic.main.fragment_favorite.tab_layout
import kotlinx.android.synthetic.main.fragment_favorite.view_pager_favorite

class FavoriteFragment : Fragment(){

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_favorite, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val pagerAdapter = FavoritesPagerAdapter(childFragmentManager)
    view_pager_favorite.adapter = pagerAdapter
    view_pager_favorite.currentItem = 0
    tab_layout.setupWithViewPager(view_pager_favorite)
  }

}
