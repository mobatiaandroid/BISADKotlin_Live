package com.mobatia.bisad.activity.canteen.model.payment_history

import com.google.gson.annotations.SerializedName

class PaymentHisModel (
    @SerializedName("status") var status:String,
    @SerializedName("responseArray") var responseArray:PaymentHisResponseModel
)