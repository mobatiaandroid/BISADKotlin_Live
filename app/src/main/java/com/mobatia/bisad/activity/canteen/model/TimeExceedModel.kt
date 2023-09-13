package com.mobatia.bisad.activity.canteen.model

import com.google.gson.annotations.SerializedName

class TimeExceedModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:TimeExceedResModel,
)