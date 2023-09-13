package com.mobatia.bisad.activity.parent_meetings.model.listing_pta

import com.google.gson.annotations.SerializedName

class PtaListingResponse (
    @SerializedName("response") val response: String,
    @SerializedName("statuscode") val statuscode: String,
    @SerializedName("data") val data: ArrayList<PtaTimeSlotList>
)