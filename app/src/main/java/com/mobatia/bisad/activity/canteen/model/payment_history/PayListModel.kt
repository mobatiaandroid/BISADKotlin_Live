package com.mobatia.bisad.activity.canteen.model.payment_history

import com.google.gson.annotations.SerializedName

class PayListModel (
        @SerializedName("id") var id:String,
        @SerializedName("title") var title:String,
        @SerializedName("description") var description:String,
        @SerializedName("trip_date") var trip_date:String,
        @SerializedName("amount") var amount:String,
        @SerializedName("status") var status:String,
        @SerializedName("last_payment_date") var last_payment_date:String,
        @SerializedName("completed_date") var completed_date:String,
        @SerializedName("last_payment_status") var last_payment_status:String,
        @SerializedName("trip_date_staus") var trip_date_staus:String,
        @SerializedName("studentName") var studentName:String,
        @SerializedName("order_id") var order_id:String,
        @SerializedName("parentName") var parentName:String,
        @SerializedName("paidby") var paidby:String,
        @SerializedName("payment_type") var payment_type:String,
        @SerializedName("invoiceNote") var invoiceNote:String,
        @SerializedName("installment") var installment:String,
        @SerializedName("payment_option") var payment_option:String,
        @SerializedName("remaining_amount") var remaining_amount:String,
        @SerializedName("payment_status") var payment_status:String,
        @SerializedName("closing_date") var closing_date:String,
        @SerializedName("last_paid_date") var last_paid_date:String,
        @SerializedName("payment_date_status") var payment_date_status:String,
        @SerializedName("billingCode") var billingCode:String,
        @SerializedName("isEmiAvailable") var isEmiAvailable:Boolean,
        @SerializedName("isfullPaid") var isfullPaid:Boolean,
        @SerializedName("installmentArrayList") var installmentArrayList:ArrayList<InstallmentListModel>,
        @SerializedName("fullpaymentArrayList") var fullpaymentArrayList:ArrayList<FullPaymentListModel>

        )