package com.neobyte.footbalschedule.models

import com.google.gson.annotations.SerializedName

data class SearchEventResponses(
    @SerializedName("event") val events: List<Event?>?
)