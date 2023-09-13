package com.mobatia.bisad.fragment.payment.model

import com.google.gson.annotations.SerializedName

class PaymentBannerDataModel (
    @SerializedName("id") var id:String,
    @SerializedName("image") var image:String,
    @SerializedName("description") var description:String,
    @SerializedName("contact_email") var contact_email:String
)