package com.mobatia.bisad.activity.parent_meetings.model.insert_pta

import com.google.gson.annotations.SerializedName

class PtaInsertModel (
    @SerializedName("responsecode") val responsecode: String,
    @SerializedName("response") val response: ResponseInsertPta
)