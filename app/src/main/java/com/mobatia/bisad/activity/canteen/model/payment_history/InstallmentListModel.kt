package com.mobatia.bisad.activity.canteen.model.payment_history

import com.google.gson.annotations.SerializedName

class InstallmentListModel (
    @SerializedName("installment_id") var installment_id:String,
    @SerializedName("invoiceNote") var invoiceNote:String,
    @SerializedName("paid_amount") var paid_amount:String,
    @SerializedName("inst_amount") var inst_amount:String,
    @SerializedName("paid_status") var paid_status:String,
    @SerializedName("last_payment_date") var last_payment_date:String,
    @SerializedName("paid_by") var paid_by:String,
    @SerializedName("payment_type") var payment_type:String,
    @SerializedName("paid_date") var paid_date:String,
    @SerializedName("order_id") var order_id:String,
    @SerializedName("inst_date_status") var inst_date_status:String,
    @SerializedName("payment_option") var payment_option:String,
    @SerializedName("enable") var enable:String,
    @SerializedName("enablePosition") var enablePosition:Int,
    @SerializedName("isPaid") var isPaid:Boolean,

)