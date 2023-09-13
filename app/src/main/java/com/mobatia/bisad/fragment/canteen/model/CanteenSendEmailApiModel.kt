package com.mobatia.bisad.fragment.canteen.model

import com.google.gson.annotations.SerializedName

class CanteenSendEmailApiModel  (
    @SerializedName("email") var email:String,
    @SerializedName("subject") var subject:String,
    @SerializedName("message") var message:String
)