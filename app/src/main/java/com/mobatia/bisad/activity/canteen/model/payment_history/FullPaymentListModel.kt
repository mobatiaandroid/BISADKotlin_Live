package com.mobatia.bisad.activity.canteen.model.payment_history

import com.google.gson.annotations.SerializedName

class FullPaymentListModel (
    @SerializedName("invoiceNote") var invoiceNote:String,
    @SerializedName("last_payment_date") var last_payment_date:String,
    @SerializedName("paid_amount") var paid_amount:String,
    @SerializedName("paid_by") var paid_by:String,
    @SerializedName("payment_type") var payment_type:String,
    @SerializedName("paid_date") var paid_date:String,
    @SerializedName("order_id") var order_id:String,
    @SerializedName("InstallmentArray") var InstallmentArray:ArrayList<FullPaymentListModel>

)