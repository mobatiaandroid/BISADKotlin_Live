package com.mobatia.bisad.activity.payment.model.payment_gateway

import com.google.gson.annotations.SerializedName

class PaymentGatewayModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:PayGateResModel
)