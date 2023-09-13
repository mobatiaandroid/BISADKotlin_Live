package com.mobatia.bisad.activity.payment.model.payment_submit

import com.google.gson.annotations.SerializedName

class PaymentSubmitModel (
    @SerializedName("status") var status:Int,
    @SerializedName("payment_details") var payment_details:PaySuccDetailResModel
)