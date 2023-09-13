package com.mobatia.bisad.activity.canteen.model.add_to_cart

import com.google.gson.annotations.SerializedName

class CanteenCartUpdateApiModel (
    @SerializedName("studentId") var studentId:String,
    @SerializedName("order_date") var order_date:String,
    @SerializedName("quantity") var quantity:String,
    @SerializedName("item_id") var item_id:String,
    @SerializedName("canteen_cart_id") var canteen_cart_id:String
)