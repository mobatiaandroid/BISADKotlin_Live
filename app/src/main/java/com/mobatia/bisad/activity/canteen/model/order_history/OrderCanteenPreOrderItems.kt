package com.mobatia.bisad.activity.canteen.model.order_history

import com.google.gson.annotations.SerializedName

class OrderCanteenPreOrderItems (
    @SerializedName("id") var id:Int,
    @SerializedName("item_id") var item_id:Int,
    @SerializedName("quantity") var quantity:Int,
    @SerializedName("item_status") var item_status:Int,
    @SerializedName("item_total") var item_total:Int,
    @SerializedName("total_amount") var total_amount:String,
    @SerializedName("item_name") var item_name:String,
    @SerializedName("price") var price:String,
    @SerializedName("item_description") var item_description:String
        )