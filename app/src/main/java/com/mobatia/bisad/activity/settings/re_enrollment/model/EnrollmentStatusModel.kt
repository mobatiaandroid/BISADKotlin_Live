package com.mobatia.bisad.activity.settings.re_enrollment.model

import com.google.gson.annotations.SerializedName

class EnrollmentStatusModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:ResponseArrayStatus
)