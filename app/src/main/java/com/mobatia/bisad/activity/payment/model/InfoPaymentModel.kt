package com.mobatia.bisad.activity.payment.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.canteen.model.information.InfoResponseModel

class InfoPaymentModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray: InfoPayResponseModel
)