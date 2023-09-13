package com.mobatia.bisad.activity.absence.model

import com.google.gson.annotations.SerializedName

class RequestPickupApiModel (

    @SerializedName("studentId") val student_id: String,
    @SerializedName("pickupDate") val from_date: String,
    @SerializedName("pickupTime") val to_date: String,
    @SerializedName("getEarlyPickupReason") val reason: String,
    @SerializedName("pickupByWhom") val pickup_by: String,
    @SerializedName("deviceType") val device_type: String,
    @SerializedName("deviceName") val device_name: String,
    @SerializedName("appVersion") val app_version: String

)