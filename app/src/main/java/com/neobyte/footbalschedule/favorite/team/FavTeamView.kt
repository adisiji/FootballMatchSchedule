package com.neobyte.footbalschedule.favorite.team

import com.neobyte.footbalschedule.models.Team

interface FavTeamView {
  fun onLoading()

  fun onSuccess(list: List<Team?>?)

  fun onError(message: String)
}