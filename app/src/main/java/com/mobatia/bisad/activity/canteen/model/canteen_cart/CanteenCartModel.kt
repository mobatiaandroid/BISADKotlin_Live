package com.mobatia.bisad.activity.canteen.model.canteen_cart

import com.google.gson.annotations.SerializedName

class CanteenCartModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:CanteenCartResponseModel
)