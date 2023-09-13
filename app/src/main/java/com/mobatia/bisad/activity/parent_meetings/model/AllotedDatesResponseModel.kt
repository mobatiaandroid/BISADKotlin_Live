package com.mobatia.bisad.activity.parent_meetings.model

import com.google.gson.annotations.SerializedName

class AllotedDatesResponseModel (
    @SerializedName("response") val response: String,
    @SerializedName("statuscode") val statuscode: String,
    @SerializedName("data") val data: ArrayList<String>
)