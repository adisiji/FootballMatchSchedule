package com.neobyte.footbalschedule.models

import com.google.gson.annotations.SerializedName

data class LeagueResponse(
    @SerializedName("countrys") val leagues: List<League?>?
)