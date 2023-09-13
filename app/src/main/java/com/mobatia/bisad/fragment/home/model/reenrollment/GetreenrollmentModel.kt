package com.mobatia.bisad.fragment.home.model.reenrollment

import com.google.gson.annotations.SerializedName

class GetreenrollmentModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: ResponseArray
)