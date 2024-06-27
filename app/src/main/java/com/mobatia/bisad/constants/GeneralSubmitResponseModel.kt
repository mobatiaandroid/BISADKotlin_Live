package com.mobatia.bisad.constants

import com.google.gson.annotations.SerializedName

data class GeneralSubmitResponseModel (
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("validation_errors")
    val validationErrors: List<String>,
    @SerializedName("data")
    val data: Data
)



data class Data(
    @SerializedName("choices")
    val choices: ArrayList<String>
)