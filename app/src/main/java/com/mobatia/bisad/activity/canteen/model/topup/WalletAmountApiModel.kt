package com.mobatia.bisad.activity.canteen.model.topup

import com.google.gson.annotations.SerializedName

class WalletAmountApiModel (
    @SerializedName("studentId") var studentId:String,
    @SerializedName("order_reference") var order_reference:String,
    @SerializedName("amount") var amount:String,
    @SerializedName("device_type") var device_type:String,
    @SerializedName("device_name") var device_name:String,
    @SerializedName("app_version") var app_version:String
)