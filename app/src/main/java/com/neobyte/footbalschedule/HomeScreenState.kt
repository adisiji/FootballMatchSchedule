package com.neobyte.footbalschedule

import com.neobyte.footbalschedule.models.Event

sealed class HomeScreenState {
  class Error(val message: String?) : HomeScreenState()
  object Loading : HomeScreenState()
  data class Data(val eventResponse: List<Event?>) : HomeScreenState()
}