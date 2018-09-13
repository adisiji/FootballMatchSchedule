package com.neobyte.footbalschedule.matches

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.search.SearchMatchActivity
import kotlinx.android.synthetic.main.fragment_matches.tab_layout
import kotlinx.android.synthetic.main.fragment_matches.view_pager_matches

class MatchesFragment : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
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

  override fun onCreateOptionsMenu(menu: Menu,
                                   inflater: MenuInflater?) {
    inflater?.inflate(R.menu.menu_matches, menu)
    /*
    //super.onCreateOptionsMenu(menu, inflater)
    val searchItem = menu.findItem(R.id.search)
    val searchView = searchItem.actionView as SearchView
    val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

    searchView.setSearchableInfo(searchManager.getSearchableInfo(
        ComponentName(context, SearchMatchActivity::class.java)))
    searchView.setIconifiedByDefault(true)
    */
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.search -> {
        val intent = Intent(context, SearchMatchActivity::class.java)
        startActivity(intent)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}