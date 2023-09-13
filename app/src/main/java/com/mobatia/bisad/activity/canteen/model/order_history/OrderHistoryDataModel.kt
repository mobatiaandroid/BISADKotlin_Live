package com.mobatia.bisad.activity.canteen.model.order_history

import com.google.gson.annotations.SerializedName

class OrderHistoryDataModel (

    @SerializedName("id") var id:Int,
    @SerializedName("type") var type:String,
    @SerializedName("type_status") var type_status:Int,
    @SerializedName("delivery_date") var delivery_date:String,
    @SerializedName("total_amount") var total_amount:String,
    @SerializedName("status") var status:String,
    @SerializedName("section") var section:String,
    @SerializedName("canteen_preordered_items") var canteen_preordered_items:ArrayList<OrderCanteenPreOrderItems>


)