package com.neobyte.footbalschedule.models

import com.google.gson.annotations.SerializedName

data class EventResponses(
  @SerializedName("events") val events: List<Event?>?
)