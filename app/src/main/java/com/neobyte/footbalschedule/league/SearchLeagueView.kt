package com.neobyte.footbalschedule.league

import com.neobyte.footbalschedule.models.League

interface SearchLeagueView {
  fun onLoading()

  fun onSuccess(list: List<League?>?)

  fun onError(message: String)
}