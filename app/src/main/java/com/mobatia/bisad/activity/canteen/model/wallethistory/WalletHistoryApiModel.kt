package com.mobatia.bisad.activity.canteen.model.wallethistory

import com.google.gson.annotations.SerializedName

class WalletHistoryApiModel (
    @SerializedName("studentId") var studentId:String,
    @SerializedName("start") var start:String,
    @SerializedName("limit") var limit:String
)