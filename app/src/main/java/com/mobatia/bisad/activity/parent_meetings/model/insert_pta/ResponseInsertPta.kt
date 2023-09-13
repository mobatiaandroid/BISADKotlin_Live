package com.mobatia.bisad.activity.parent_meetings.model.insert_pta

import com.google.gson.annotations.SerializedName

class ResponseInsertPta (
    @SerializedName("response") val response: String,
    @SerializedName("statuscode") val statuscode: String
)