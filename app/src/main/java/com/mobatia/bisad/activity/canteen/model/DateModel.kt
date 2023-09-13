package com.mobatia.bisad.activity.canteen.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DateModel (
    @SerializedName("date") val date: String,
    @SerializedName("day") val day: String,
    @SerializedName("month") val month: String,
    @SerializedName("year") val year: String,
    @SerializedName("numberDate") val numberDate: String,
    @SerializedName("isItemSelected") var isItemSelected: Boolean,
    @SerializedName("isDateSelected") var isDateSelected: Boolean
):Serializable


