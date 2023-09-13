package com.mobatia.bisad.fragment.payment.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.canteen.model.CanteenBannerResponseArrayModel

class PaymentBannerResponseModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray: PaymentBannerResponseArrayModel
)