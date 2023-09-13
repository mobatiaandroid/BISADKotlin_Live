package com.mobatia.bisad.activity.parent_meetings.model.cancel

import com.google.gson.annotations.SerializedName

class PtaCancelModel (
    @SerializedName("response") val response: String,
    @SerializedName("statuscode") val statuscode: String

)