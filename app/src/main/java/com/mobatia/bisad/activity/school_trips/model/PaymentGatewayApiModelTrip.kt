package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class PaymentGatewayApiModelTrip (
    @SerializedName("amount") var amount:String,
    @SerializedName("emailAddress") var emailAddress:String,
    @SerializedName("merchantOrderReference") var merchantOrderReference:String,
    @SerializedName("firstName") var firstName:String,
    @SerializedName("lastName") var lastName:String,
    @SerializedName("address1") var address1:String,
    @SerializedName("city") var city:String,
    @SerializedName("countryCode") var countryCode:String,
    @SerializedName("access_token") var access_token:String,
    @SerializedName("trip_id") var trip_id:String,
    @SerializedName("module") var module :String,
    @SerializedName("payment_id") var payment_id:String


)
