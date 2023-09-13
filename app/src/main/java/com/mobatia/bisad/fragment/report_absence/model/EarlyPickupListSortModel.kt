package com.mobatia.bisad.fragment.report_absence.model

import com.google.gson.annotations.SerializedName

class EarlyPickupListSortModel (
    @SerializedName("id") val id: Int,
    @SerializedName("pickup_date") val pickup_date: String,
    @SerializedName("pickup_time") val pickup_time: String,
    @SerializedName("pickup_by_whom") val pickup_by_whom: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("status") val status: Int,
    @SerializedName("class_name") val class_name: String
)