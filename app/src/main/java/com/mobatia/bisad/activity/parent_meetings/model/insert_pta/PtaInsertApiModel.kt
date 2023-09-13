package com.mobatia.bisad.activity.parent_meetings.model.insert_pta

import com.google.gson.annotations.SerializedName

class PtaInsertApiModel (

    @SerializedName("student_id") val student_id: String,
    @SerializedName("staff_id") val staff_id: String,
    @SerializedName("date") val date: String,
    @SerializedName("start_time") val start_time: String,
    @SerializedName("end_time") val end_time: String,
    @SerializedName("book_end_date") val book_end_date: String,
    @SerializedName("vpml") val vpml: String,
    @SerializedName("room") val room: String
)