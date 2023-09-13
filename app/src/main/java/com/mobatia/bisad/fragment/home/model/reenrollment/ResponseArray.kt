package com.mobatia.bisad.fragment.home.model.reenrollment

import com.google.gson.annotations.SerializedName

class ResponseArray (
    @SerializedName("settings") val settingsdata: SettingsData,
    @SerializedName("user") val user: UserDetails,
    @SerializedName("current_date") val current_date: String,
    @SerializedName("students") val student: ArrayList<StudentReEnrollList>
)