package com.mobatia.bisad.activity.canteen.model.add_orders

import com.google.gson.annotations.SerializedName

class CanteenItemsApiModel (
    @SerializedName("studentId") var studentId:String,
    @SerializedName("category_id") var category_id:String,
    @SerializedName("order_date") var order_date:String,
    @SerializedName("start") var start:String,
    @SerializedName("limit") var limit:String
)