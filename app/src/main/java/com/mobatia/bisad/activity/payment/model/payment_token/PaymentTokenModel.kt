package com.mobatia.bisad.activity.payment.model.payment_token

import com.google.gson.annotations.SerializedName

class PaymentTokenModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:PayTokenResModel
)