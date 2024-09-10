package com.mobatia.bisad.activity.canteen.model.canteen_cart

import com.google.gson.annotations.SerializedName

class CanteenCartResponseModel (
    @SerializedName("cart_total") var cart_totoal:Int,
    @SerializedName("previous_orders_total") var previous_orders_total:Int,
    @SerializedName("data") var data:ArrayList<CanteenCartResModel>
)