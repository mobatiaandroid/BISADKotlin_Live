package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.payment.model.payment_submit.PaySuccDetailResModel

class TripPaymentSubmitModel (
    @SerializedName("status") var status:Int,
    @SerializedName("payment_details") var payment_details: PaySuccDetailResModel
)