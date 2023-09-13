package com.mobatia.bisad.activity.payment.model.payment_submit

import com.google.gson.annotations.SerializedName

class PaymentSubmitApiModel (
    @SerializedName("student_id") var student_id:String,
    @SerializedName("payment_detail_id") var payment_detail_id:String,
    @SerializedName("order_reference") var order_reference:String
)