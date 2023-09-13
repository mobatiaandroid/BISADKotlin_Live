package com.mobatia.bisad.activity.parent_meetings.model.listing_pta

import com.google.gson.annotations.SerializedName

class PtaTimeSlotList (
    @SerializedName("date") val date: String,
    @SerializedName("start_time") var start_time: String,
    @SerializedName("end_time") val end_time: String,
    @SerializedName("book_end_date") val book_end_date: String,
    @SerializedName("room") val room: String,
    @SerializedName("vpml") val vpml: String,
    @SerializedName("status") val status: String,
    @SerializedName("booking_open") val booking_open: String,
    @SerializedName("isSelected") var isSelected: Boolean


)