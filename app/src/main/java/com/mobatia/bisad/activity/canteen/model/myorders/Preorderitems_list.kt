package com.mobatia.bisad.activity.canteen.model.myorders

import com.google.gson.annotations.SerializedName

class Preorderitems_list (
    @SerializedName("id") val id: String,
    @SerializedName("item_id") val item_id: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("item_status") val item_status: String,
    @SerializedName("item_total") val item_total: Int,
    @SerializedName("item_name") val item_name: String,
    @SerializedName("price") val price: String,
    @SerializedName("available_quantity") val available_quantity: String,
    @SerializedName("item_description") val item_description: String,
    @SerializedName("cancellation_time_exceed") val cancellation_time_exceed: Int
)