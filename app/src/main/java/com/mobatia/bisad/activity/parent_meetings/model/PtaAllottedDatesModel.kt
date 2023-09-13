package com.mobatia.bisad.activity.parent_meetings.model

import com.google.gson.annotations.SerializedName

class PtaAllottedDatesModel (
    @SerializedName("responsecode") val responsecode: String,
    @SerializedName("response") val response: AllotedDatesResponseModel
)