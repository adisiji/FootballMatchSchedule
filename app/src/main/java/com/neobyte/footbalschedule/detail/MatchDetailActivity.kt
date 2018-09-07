package com.neobyte.footbalschedule.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.neobyte.footbalschedule.Constants
import com.neobyte.footbalschedule.FootballMatchService
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.models.Event
import com.neobyte.footbalschedule.models.Team
import kotlinx.android.synthetic.main.activity_match_detail.iv_logo_team1
import kotlinx.android.synthetic.main.activity_match_detail.iv_logo_team2
import kotlinx.android.synthetic.main.activity_match_detail.progress_logo_1
import kotlinx.android.synthetic.main.activity_match_detail.progress_logo_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_date_detail
import kotlinx.android.synthetic.main.activity_match_detail.tv_defense_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_defense_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_formation_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_formation_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_forward_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_forward_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_goal_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_goal_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_goalkeeper_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_goalkeeper_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_midfield_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_midfield_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_shots_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_shots_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_skor_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_skor_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_substitutes_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_substitutes_team_2
import kotlinx.android.synthetic.main.activity_match_detail.tv_team_1
import kotlinx.android.synthetic.main.activity_match_detail.tv_team_2
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class MatchDetailActivity : AppCompatActivity(), MatchDetailView {

  lateinit var presenter: MatchDetailPresenter
  lateinit var event: Event
  lateinit var menu: Menu

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_match_detail)

    event = intent.getParcelableExtra(Constants.EVENT)

    supportActionBar?.title = "Match Detail"
  }

  override fun onResume() {
    super.onResume()
    val databaseHelper = DatabaseHelper.getInstance(this)
    presenter = MatchDetailPresenter(databaseHelper, this, FootballMatchService.instance)
    setupView(event)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.detail_menu, menu)
    this.menu = menu
    presenter.getFavorite(event.idEvent ?: "")
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        finish()
        true
      }
      R.id.add_to_favorite -> {
        presenter.setFavorite(event)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun showSnackbar(message: String) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
  }

  override fun setAsFavourite(favourite: Boolean) {
    if (favourite) {
      menu.getItem(0).icon = ContextCompat.getDrawable(this,
                                                       R.drawable.ic_star_white_24dp)
    } else {
      menu.getItem(0).icon = ContextCompat.getDrawable(this,
                                                       R.drawable.ic_star_border_white_24dp)
    }
  }

  fun setupView(event: Event) {
    val matchIsDone = intent.getBooleanExtra(Constants.DONE_MATCH, true)

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    try {
      val myDate = sdf.parse(event.dateEvent)
      sdf.applyPattern("EEE, d MMM yyyy")
      tv_date_detail.text = sdf.format(myDate)
    } catch (e: ParseException) {
      e.printStackTrace()
    }
    tv_team_1.text = event.strHomeTeam
    presenter.getTeam(event.idHomeTeam ?: "", object : GetTeamListener {
      override fun onLoading() {
        progress_logo_1.visibility = View.VISIBLE
      }

      override fun onSuccess(team: Team) {
        Glide.with(this@MatchDetailActivity).load(team.strTeamLogo).into(iv_logo_team1)
        progress_logo_1.visibility = View.GONE
      }

      override fun onFailed(message: String?) {
        progress_logo_1.visibility = View.GONE
        Toast.makeText(this@MatchDetailActivity, message, Toast.LENGTH_SHORT).show()
      }
    })
    tv_skor_team_1.text = event.intHomeScore
    tv_formation_team_1.text = event.strHomeFormation

    tv_team_2.text = event.strAwayTeam
    presenter.getTeam(event.idAwayTeam ?: "", object : GetTeamListener {
      override fun onLoading() {
        progress_logo_2.visibility = View.VISIBLE
      }

      override fun onSuccess(team: Team) {
        Glide.with(this@MatchDetailActivity).load(team.strTeamLogo).into(iv_logo_team2)
        progress_logo_2.visibility = View.GONE
      }

      override fun onFailed(message: String?) {
        progress_logo_2.visibility = View.GONE
        Toast.makeText(this@MatchDetailActivity, message, Toast.LENGTH_SHORT).show()
      }
    })
    tv_skor_team_2.text = event.intAwayScore
    tv_formation_team_2.text = event.strAwayFormation

    if (matchIsDone) {
      tv_goal_team_1.text = event.strHomeGoalDetails.changeNewLine()
      tv_shots_team_1.text = event.intHomeShots

      tv_goal_team_2.text = event.strAwayGoalDetails.changeNewLine()
      tv_shots_team_2.text = event.intAwayShots

      // Lineups
      tv_goalkeeper_team_1.text = event.strHomeLineupGoalkeeper.changeNewLine()
      tv_goalkeeper_team_2.text = event.strAwayLineupGoalkeeper.changeNewLine()

      tv_defense_team_1.text = event.strHomeLineupDefense.changeNewLine()
      tv_defense_team_2.text = event.strAwayLineupDefense.changeNewLine()

      tv_midfield_team_1.text = event.strHomeLineupMidfield.changeNewLine()
      tv_midfield_team_2.text = event.strAwayLineupMidfield.changeNewLine()

      tv_forward_team_1.text = event.strHomeLineupForward.changeNewLine()
      tv_forward_team_2.text = event.strAwayLineupForward.changeNewLine()

      tv_substitutes_team_1.text = event.strHomeLineupSubstitutes.changeNewLine()
      tv_substitutes_team_2.text = event.strAwayLineupSubstitutes.changeNewLine()
    }

  }

  private fun String?.changeNewLine(): String? {
    return this?.replace(";", "\n")
  }

  override fun onDestroy() {
    presenter.composeSubscriber()
    super.onDestroy()
  }
}
