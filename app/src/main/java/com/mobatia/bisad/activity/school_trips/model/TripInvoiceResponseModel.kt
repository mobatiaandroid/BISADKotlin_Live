package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName


data class TripInvoiceResponseModel(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: List<String>,
    @SerializedName("data")
    val data: ReceiptData
) {

    data class ReceiptData(
        @SerializedName("receipt")
        val receiptUrl: String
    )
}
