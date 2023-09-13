package com.mobatia.bisad.activity.payment.model.payment_gateway

import com.google.gson.annotations.SerializedName

class PaymentGatewayApiModel (
    @SerializedName("amount") var amount:String,
    @SerializedName("emailAddress") var emailAddress:String,
    @SerializedName("merchantOrderReference") var merchantOrderReference:String,
    @SerializedName("firstName") var firstName:String,
    @SerializedName("lastName") var lastName:String,
    @SerializedName("address1") var address1:String,
    @SerializedName("city") var city:String,
    @SerializedName("countryCode") var countryCode:String,
    @SerializedName("access_token") var access_token:String
)