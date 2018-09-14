package com.neobyte.footbalschedule.favorite.match

import com.neobyte.footbalschedule.models.Event

interface FavMatchView {
  fun onLoading()

  fun onSuccess(list: List<Event?>?)

  fun onError(message: String)
}