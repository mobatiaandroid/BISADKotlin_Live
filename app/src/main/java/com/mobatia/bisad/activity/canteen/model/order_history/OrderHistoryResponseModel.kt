package com.mobatia.bisad.activity.canteen.model.order_history

import com.google.gson.annotations.SerializedName

class OrderHistoryResponseModel(
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:OrderHstoryResponseArrayModel

)