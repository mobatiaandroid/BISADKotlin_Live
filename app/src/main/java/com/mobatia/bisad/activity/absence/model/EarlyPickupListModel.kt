package com.mobatia.bisad.activity.absence.model

import com.google.gson.annotations.SerializedName

class EarlyPickupListModel {
    @SerializedName("id")
    val id: Int=0
    @SerializedName("pickup_date")
    val pickup_date: String=""
    @SerializedName("pickup_time")
    var pickup_time: String=""
    @SerializedName("pickup_by_whom")
    val pickup_by_whom: String=""
    @SerializedName("reason")
    val reason: String=""
    @SerializedName("status")
    val status: Int=0
    @SerializedName("class_name")
    val class_name: String=""
    @SerializedName("reason_for_rejection")
    val reason_for_rejection: String=""
}