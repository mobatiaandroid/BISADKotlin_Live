package com.mobatia.bisad.activity.payment.model

import com.google.gson.annotations.SerializedName

class InfoPayListModel (
    @SerializedName("id") var id:String,
    @SerializedName("title") var title:String,
    @SerializedName("file") var file:String
)