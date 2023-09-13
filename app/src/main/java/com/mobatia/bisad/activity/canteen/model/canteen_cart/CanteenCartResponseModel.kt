package com.mobatia.bisad.activity.canteen.model.canteen_cart

import com.google.gson.annotations.SerializedName

class CanteenCartResponseModel (
    @SerializedName("data") var data:ArrayList<CanteenCartResModel>
)