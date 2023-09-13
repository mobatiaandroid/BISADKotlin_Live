package com.mobatia.bisad.activity.parent_meetings.model.listing_pta

import com.google.gson.annotations.SerializedName

class PtaListingModel (
    @SerializedName("responsecode") val responsecode: String,
    @SerializedName("response") val response: PtaListingResponse
)