package com.mobatia.bisad.activity.canteen.model.topup

import com.google.gson.annotations.SerializedName

class WalletAmountModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:WalletTopResModel
)