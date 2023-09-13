package com.mobatia.bisad.activity.absence.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.report_absence.model.AbsenceResponseArray

data class EarlyPickupModel  (
    @SerializedName("status") val status: Int

)