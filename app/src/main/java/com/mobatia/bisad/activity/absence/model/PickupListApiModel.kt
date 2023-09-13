package com.mobatia.bisad.activity.absence.model

import com.google.gson.annotations.SerializedName

class PickupListApiModel (

    @SerializedName("studentId") val student_id: String,
    @SerializedName("start") val start: String,
    @SerializedName("limit") val limit: String

)