package com.neobyte.footbalschedule.teamdetail

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.transition.Slide
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.neobyte.footbalschedule.R
import com.neobyte.footbalschedule.db.DatabaseHelper
import com.neobyte.footbalschedule.models.Team
import kotlinx.android.synthetic.main.activity_team_detail.collapsing_toolbar
import kotlinx.android.synthetic.main.activity_team_detail.fab
import kotlinx.android.synthetic.main.activity_team_detail.image
import kotlinx.android.synthetic.main.activity_team_detail.tab_team
import kotlinx.android.synthetic.main.activity_team_detail.toolbar
import kotlinx.android.synthetic.main.activity_team_detail.view_pager_team

class TeamDetailActivity : AppCompatActivity(), TeamDetailView {

  private lateinit var team: Team
  private lateinit var presenter: TeamDetailPresenter

  companion object {

    private const val EXTRA_IMAGE = "extra_image_team"
    private const val EXTRA_TEAM = "extra_team"

    fun navigate(from: FragmentActivity,
                 transitionImage: View,
                 team: Team) {
      val intent = Intent(from, TeamDetailActivity::class.java).apply {
        putExtra(EXTRA_TEAM, team)
      }

      val options =
        ActivityOptionsCompat.makeSceneTransitionAnimation(from, transitionImage, EXTRA_IMAGE)
      ActivityCompat.startActivity(from, intent, options.toBundle())
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initActivityTransitions()
    setContentView(R.layout.activity_team_detail)

    presenter = TeamDetailPresenter(DatabaseHelper.getInstance(this), this)
    ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE)
    supportPostponeEnterTransition()

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    team = intent.getParcelableExtra(EXTRA_TEAM)

    collapsing_toolbar.title = team.strTeam
    collapsing_toolbar.setExpandedTitleColor(
        ContextCompat.getColor(this, android.R.color.transparent))

    Glide.with(this)
        .asBitmap()
        .load(team.strTeamLogo)
        .listener(object : RequestListener<Bitmap> {
          override fun onLoadFailed(e: GlideException?,
                                    model: Any?,
                                    target: Target<Bitmap>?,
                                    isFirstResource: Boolean): Boolean {
            return false
          }

          override fun onResourceReady(resource: Bitmap,
                                       model: Any?,
                                       target: Target<Bitmap>?,
                                       dataSource: DataSource?,
                                       isFirstResource: Boolean): Boolean {
            Palette.from(resource)
                .generate { palette -> applyPalette(palette) }
            return false
          }

        })
        .into(image)

    val viewpagerAdapter = TeamDetailPagerAdapter(supportFragmentManager, team)
    view_pager_team.adapter = viewpagerAdapter
    view_pager_team.currentItem = 0

    tab_team.setupWithViewPager(view_pager_team)

    fab.setOnClickListener {
      presenter.setFavorite(team)
    }
  }

  override fun setAsFavourite(favourite: Boolean) {
    fab.hide()
    if (favourite) {
      fab.setImageResource(R.drawable.ic_star_white_24dp)
    } else {
      fab.setImageResource(R.drawable.ic_star_border_white_24dp)
    }
    fab.show()
  }

  override fun showSnackbar(message: String) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        .show()
  }

  private fun initActivityTransitions() {
    val transition = Slide()
    transition.excludeTarget(android.R.id.statusBarBackground, true)
    window.enterTransition = transition
    window.returnTransition = transition
  }

  private fun applyPalette(palette: Palette?) {
    val primaryDark = ContextCompat.getColor(this, R.color.primary_dark)
    val primary = ContextCompat.getColor(this, R.color.primary)
    palette?.let {
      collapsing_toolbar.setContentScrimColor(palette.getMutedColor(primary))
      collapsing_toolbar.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark))
      updateBackground(findViewById<View>(R.id.fab) as FloatingActionButton, palette)
    }
    supportStartPostponedEnterTransition()
    presenter.getFavorite(team.idTeam ?: "0")
  }

  private fun updateBackground(fab: FloatingActionButton,
                               palette: Palette) {
    val lightVibrantColor =
      palette.getLightVibrantColor(ContextCompat.getColor(this, android.R.color.white))
    val vibrantColor = palette.getVibrantColor(ContextCompat.getColor(this, R.color.accent))

    fab.rippleColor = lightVibrantColor
    fab.backgroundTintList = ColorStateList.valueOf(vibrantColor)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
    }

    return super.onOptionsItemSelected(item)
  }
}
