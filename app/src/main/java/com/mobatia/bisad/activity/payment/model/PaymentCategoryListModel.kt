package com.mobatia.bisad.activity.payment.model

import com.google.gson.annotations.SerializedName

class PaymentCategoryListModel {
    @SerializedName("id") var id:String=""
    @SerializedName("student_name") var student_name:String=""
    @SerializedName("account_code") var account_code:String=""
    @SerializedName("pupil_code") var pupil_code:String=""
    @SerializedName("academic_year") var academic_year:String=""
    @SerializedName("invoice_ref") var invoice_ref:String=""
    @SerializedName("invoice_description") var invoice_description:String=""
    @SerializedName("current_amount") var current_amount:String=""
    @SerializedName("vat_percentage") var vat_percentage:String=""
    @SerializedName("vat_amount") var vat_amount:String=""
    @SerializedName("total_amount") var total_amount:String=""
    @SerializedName("due_date") var due_date:String=""
    @SerializedName("status") var status:String=""
    @SerializedName("paid_date") var paid_date:String=""
    @SerializedName("payment_type") var payment_type:String=""
    @SerializedName("paid_by") var paid_by:String=""
    @SerializedName("thankyou_note") var thankyou_note:String=""

}