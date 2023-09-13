package com.mobatia.bisad.fragment.report_absence.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.absence.model.EarlyPickupListModel

class EarlyPickupResponseArray (
    @SerializedName("request") val requestList: List<EarlyPickupListModel>

)