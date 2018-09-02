package com.neobyte.footbalschedule.models

import com.google.gson.annotations.SerializedName

data class TeamResponse(
  @SerializedName("teams") val teams: List<Team?>?
)