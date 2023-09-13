package com.mobatia.bisad.activity.canteen.model.canteen_cart

import com.google.gson.annotations.SerializedName

class CanteenCartListmodel (
    @SerializedName("delivery_date") val delivery_date: String,
    @SerializedName("total_amount") val total_amount: Int,
    @SerializedName("cart_items") val cart_items: ArrayList<CartItemsListModel>
)