package com.mobatia.bisad.activity.canteen.model.order_history

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.canteen.model.canteen_cart.ItemsModel

class OrderHistoryApiModel (
    @SerializedName("studentId") var student_id:String,
    @SerializedName("start") var start:String,
    @SerializedName("limit") var limit:String
)