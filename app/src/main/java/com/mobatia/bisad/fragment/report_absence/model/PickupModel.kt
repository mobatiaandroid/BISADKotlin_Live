package com.mobatia.bisad.fragment.report_absence.model

import com.google.gson.annotations.SerializedName

class PickupModel(

 @SerializedName("status") val status: Int,
@SerializedName("responseArray") val responseArray: EarlyPickupResponseArray
)