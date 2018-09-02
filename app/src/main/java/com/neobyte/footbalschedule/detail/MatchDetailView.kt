package com.neobyte.footbalschedule.detail

interface MatchDetailView {
  fun showSnackbar(message: String)

  fun setAsFavourite(favourite: Boolean)
}