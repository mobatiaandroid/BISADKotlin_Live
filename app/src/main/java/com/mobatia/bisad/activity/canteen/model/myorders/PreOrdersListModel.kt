package com.mobatia.bisad.activity.canteen.model.myorders

import com.google.gson.annotations.SerializedName

class PreOrdersListModel (
    @SerializedName("id") val id: String,
    @SerializedName("delivery_date") val delivery_date: String,
    @SerializedName("total_amount") val total_amount: String,
    @SerializedName("status") val status: String,
    @SerializedName("cancellation_time_exceed") val cancellation_time_exceed: String,
    @SerializedName("section") val section: String,
    @SerializedName("canteen_preordered_items") val canteen_preordered_items: ArrayList<Preorderitems_list>,
)