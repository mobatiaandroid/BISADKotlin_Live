package com.mobatia.bisad.activity.canteen.model.add_to_cart

import com.google.gson.annotations.SerializedName

class AddToCartCanteenApiModel (
    @SerializedName("studentId") var studentId:String,
    @SerializedName("item_id") var item_id:String,
    @SerializedName("order_date") var order_date:String,
    @SerializedName("quantity") var quantity:String,
    @SerializedName("price") var price:String
)