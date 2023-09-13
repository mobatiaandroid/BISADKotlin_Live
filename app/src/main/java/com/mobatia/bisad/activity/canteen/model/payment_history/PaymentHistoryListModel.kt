package com.mobatia.bisad.activity.canteen.model.payment_history

import com.google.gson.annotations.SerializedName

class PaymentHistoryListModel (
    @SerializedName("id") var id:String,
    @SerializedName("date_time") var date_time:String,
    @SerializedName("name") var name:String,
    @SerializedName("amount") var amount:String,
    @SerializedName("status") var status:String,
    @SerializedName("keycode") var keycode:String,
    @SerializedName("bill_no") var bill_no:String,
    @SerializedName("invoice") var invoice:String,
    @SerializedName("trn_no") var trn_no:String,
    @SerializedName("payment_type") var payment_type:String

)