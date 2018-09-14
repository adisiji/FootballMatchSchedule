package com.neobyte.footbalschedule

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.neobyte.footbalschedule.favorite.FavoriteFragment
import com.neobyte.footbalschedule.matches.MatchesFragment
import com.neobyte.footbalschedule.teams.TeamsFragment
import kotlinx.android.synthetic.main.activity_main.nav_bottom

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    nav_bottom.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.action_matches -> {
          val fragment = MatchesFragment()
          changeHomeFragment(fragment)
          true
        }
        R.id.action_teams -> {
          val fragment = TeamsFragment()
          changeHomeFragment(fragment)
          true
        }
        R.id.action_favorite -> {
          val fragment = FavoriteFragment()
          changeHomeFragment(fragment)
          true
        }
        else -> false
      }
    }

    nav_bottom.selectedItemId = R.id.action_matches
  }

  fun changeHomeFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.frame_content, fragment)
        .commit()
  }

}
