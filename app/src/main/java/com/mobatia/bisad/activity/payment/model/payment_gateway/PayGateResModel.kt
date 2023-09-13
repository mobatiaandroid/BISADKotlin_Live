package com.mobatia.bisad.activity.payment.model.payment_gateway

import com.google.gson.annotations.SerializedName

class PayGateResModel (
    @SerializedName("order_reference") var order_reference:String,
    @SerializedName("order_paypage_url") var order_paypage_url:String,
    @SerializedName("authorization") var authorization:String,
    @SerializedName("data") var data:PayGatewayData
)