package com.mobatia.bisad.activity.common.model

import com.google.gson.annotations.SerializedName

class DeviceRegModel  (
    @SerializedName("devicetype") val devicetype: Int,
    @SerializedName("fcm_id") val fcm_id: String,
    @SerializedName("deviceid") val deviceid: String
)

