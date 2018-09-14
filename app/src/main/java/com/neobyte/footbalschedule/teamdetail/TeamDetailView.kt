package com.neobyte.footbalschedule.teamdetail

interface TeamDetailView {
  fun showSnackbar(message: String)

  fun setAsFavourite(favourite: Boolean)
}