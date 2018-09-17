package com.neobyte.footbalschedule.models

import com.google.gson.annotations.SerializedName

data class League(
    @SerializedName("idLeague") val idLeague: String? = "",
    @SerializedName("idSoccerXML") val idSoccerXML: String? = "",
    @SerializedName("strSport") val strSport: String? = "",
    @SerializedName("strLeague") val strLeague: String? = "",
    @SerializedName("strLeagueAlternate") val strLeagueAlternate: String? = "",
    @SerializedName("idCup") val idCup: String? = "",
    @SerializedName("intFormedYear") val intFormedYear: String? = "",
    @SerializedName("dateFirstEvent") val dateFirstEvent: String? = "",
    @SerializedName("strGender") val strGender: String? = "",
    @SerializedName("strCountry") val strCountry: String? = "",
    @SerializedName("strWebsite") val strWebsite: String? = "",
    @SerializedName("strFacebook") val strFacebook: String? = "",
    @SerializedName("strTwitter") val strTwitter: String? = "",
    @SerializedName("strYoutube") val strYoutube: String? = "",
    @SerializedName("strRSS") val strRSS: String? = "",
    @SerializedName("strDescriptionEN") val strDescriptionEN: String? = "",
    @SerializedName("strDescriptionFR") val strDescriptionFR: String? = "",
    @SerializedName("strFanart1") val strFanart1: String? = "",
    @SerializedName("strFanart2") val strFanart2: String? = "",
    @SerializedName("strFanart3") val strFanart3: String? = "",
    @SerializedName("strFanart4") val strFanart4: String? = "",
    @SerializedName("strBanner") val strBanner: String? = "",
    @SerializedName("strBadge") val strBadge: String? = "",
    @SerializedName("strLogo") val strLogo: String? = "",
    @SerializedName("strPoster") val strPoster: String? = "",
    @SerializedName("strTrophy") val strTrophy: String? = "",
    @SerializedName("strNaming") val strNaming: String? = "",
    @SerializedName("strLocked") val strLocked: String? = ""
)