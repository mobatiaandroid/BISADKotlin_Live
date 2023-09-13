package com.mobatia.bisad.fragment.payment.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.canteen.model.CanteenBannerDataModel

class PaymentBannerResponseArrayModel (
    @SerializedName("data") var data: PaymentBannerDataModel
)