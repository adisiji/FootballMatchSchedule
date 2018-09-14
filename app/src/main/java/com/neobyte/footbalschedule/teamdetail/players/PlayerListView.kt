package com.neobyte.footbalschedule.teamdetail.players

import com.neobyte.footbalschedule.models.Player

interface PlayerListView {
  fun onLoading()

  fun onSuccess(list: List<Player?>?)

  fun onFailed(message: String)
}