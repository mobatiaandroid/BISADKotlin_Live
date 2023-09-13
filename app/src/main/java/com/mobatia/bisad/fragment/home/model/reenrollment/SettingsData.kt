package com.mobatia.bisad.fragment.home.model.reenrollment

import com.google.gson.annotations.SerializedName

class SettingsData (
    @SerializedName("heading") val heading: String,
    @SerializedName("description") val description: String,
    @SerializedName("question") val question: String,
    @SerializedName("options") val options: ArrayList<String>,
    @SerializedName("status") val status: Int,
    @SerializedName("t_and_c") val t_and_c: String,
    @SerializedName("statement_url") val statement_url: String
)