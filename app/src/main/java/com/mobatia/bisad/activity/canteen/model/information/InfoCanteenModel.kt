package com.mobatia.bisad.activity.canteen.model.information

import com.google.gson.annotations.SerializedName

class InfoCanteenModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:InfoResponseModel
)