package com.neobyte.footbalschedule.teams

import com.neobyte.footbalschedule.models.Team

interface TeamsView {
  fun onLoading()

  fun onSuccess(list: List<Team?>?)

  fun onFailed(message: String)
}