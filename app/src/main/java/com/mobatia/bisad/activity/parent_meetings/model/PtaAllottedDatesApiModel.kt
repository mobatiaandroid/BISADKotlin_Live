package com.mobatia.bisad.activity.parent_meetings.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrollSubModel

class PtaAllottedDatesApiModel(
    @SerializedName("student_id") val student_id: String,
    @SerializedName("staff_id") val staff_id: String
)