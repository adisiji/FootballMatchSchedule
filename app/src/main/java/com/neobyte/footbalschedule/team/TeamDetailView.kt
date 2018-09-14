package com.neobyte.footbalschedule.team

interface TeamDetailView {
  fun showSnackbar(message: String)

  fun setAsFavourite(favourite: Boolean)
}