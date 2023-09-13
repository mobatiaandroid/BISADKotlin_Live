package com.mobatia.bisad.activity.permission_slip.model

import com.google.gson.annotations.SerializedName

class PermissionResApiModel (

    @SerializedName("permissionSlipId") val permissionSlipId: Int,
    @SerializedName("studentId") val student_id: String,
    @SerializedName("status") val status: String,
    @SerializedName("deviceType") val deviceType: String,
    @SerializedName("deviceName") val deviceName: String,
    @SerializedName("appVersion") val appVersion: String
)