package com.neobyte.footbalschedule.player

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
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
import com.neobyte.footbalschedule.models.Player
import kotlinx.android.synthetic.main.activity_player.tv_description_content
import kotlinx.android.synthetic.main.activity_player.tv_height_content
import kotlinx.android.synthetic.main.activity_player.tv_position_content
import kotlinx.android.synthetic.main.activity_player.tv_weight_content
import kotlinx.android.synthetic.main.activity_team_detail.collapsing_toolbar
import kotlinx.android.synthetic.main.activity_team_detail.image
import kotlinx.android.synthetic.main.activity_team_detail.toolbar

class PlayerActivity : AppCompatActivity() {

  private lateinit var player: Player

  companion object {

    private const val EXTRA_IMAGE = "extra_image_player"
    private const val EXTRA_PLAYER = "extra_player"

    fun navigate(from: FragmentActivity,
                 transitionImage: View,
                 player: Player) {
      val intent = Intent(from, PlayerActivity::class.java).apply {
        putExtra(EXTRA_PLAYER, player)
      }

      val options =
        ActivityOptionsCompat.makeSceneTransitionAnimation(from, transitionImage, EXTRA_IMAGE)
      ActivityCompat.startActivity(from, intent, options.toBundle())
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initActivityTransitions()
    setContentView(R.layout.activity_player)

    ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE)
    supportPostponeEnterTransition()

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    player = intent.getParcelableExtra(EXTRA_PLAYER)

    collapsing_toolbar.title = player.strPlayer
    collapsing_toolbar.setExpandedTitleColor(
        ContextCompat.getColor(this, android.R.color.transparent))

    Glide.with(this)
        .asBitmap()
        .load(player.strThumb)
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

    tv_height_content.text = player.strHeight
    tv_weight_content.text = player.strWeight
    tv_position_content.text = player.strPosition
    tv_description_content.text = player.strDescriptionEN
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
    }
    supportStartPostponedEnterTransition()
    //presenter.getFavorite(team.idTeam ?: "0")
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
