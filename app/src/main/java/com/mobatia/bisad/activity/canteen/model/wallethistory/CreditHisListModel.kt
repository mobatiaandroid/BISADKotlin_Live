package com.mobatia.bisad.activity.canteen.model.wallethistory

import com.google.gson.annotations.SerializedName

class CreditHisListModel (
    @SerializedName("parent_name") var parent_name:String,
    @SerializedName("email") var email:String,
    @SerializedName("id") var id:String,
    @SerializedName("student_id") var student_id:String,
    @SerializedName("user_id") var user_id:String,
    @SerializedName("amount") var amount:String,
    @SerializedName("bill_no") var bill_no:String,
    @SerializedName("order_reference") var order_reference:String,
    @SerializedName("created_on") var created_on:String,
    @SerializedName("invoice_note") var invoice_note:String,
    @SerializedName("payment_type") var payment_type:String
)