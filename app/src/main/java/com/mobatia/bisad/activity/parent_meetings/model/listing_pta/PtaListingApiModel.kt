package com.mobatia.bisad.activity.parent_meetings.model.listing_pta

import com.google.gson.annotations.SerializedName

class PtaListingApiModel (
    @SerializedName("student_id") val student_id: String,
    @SerializedName("staff_id") val staff_id: String,
    @SerializedName("date") val date: String
)