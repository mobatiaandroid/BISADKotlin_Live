package com.mobatia.bisad.activity.parent_meetings.model.confirm_pta

import com.google.gson.annotations.SerializedName

class PtaConfirmationModel (
    @SerializedName("response") val response: String,
    @SerializedName("statuscode") val statuscode: String

)