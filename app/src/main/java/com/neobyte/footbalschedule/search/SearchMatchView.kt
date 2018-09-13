package com.neobyte.footbalschedule.search

import com.neobyte.footbalschedule.models.Event

interface SearchMatchView {
  fun onLoading()
  fun onSuccess(list: List<Event?>?)
  fun onFailed(message: String)
}