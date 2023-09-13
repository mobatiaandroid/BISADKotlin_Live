package com.mobatia.bisad.activity.canteen.model.add_orders

import com.google.gson.annotations.SerializedName

class ItemsListModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:ItemsResponseModel
)