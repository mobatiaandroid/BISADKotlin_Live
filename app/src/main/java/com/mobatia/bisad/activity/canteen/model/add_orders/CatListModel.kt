package com.mobatia.bisad.activity.canteen.model.add_orders

import com.google.gson.annotations.SerializedName

class CatListModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:CatResponseModel
)