package com.mobatia.bisad.activity.parent_meetings.model.review_list

import com.google.gson.annotations.SerializedName

class ReviewListModel (
    @SerializedName("id") val id: String,
    @SerializedName("student_id") val student_id: String,
    @SerializedName("student") val student: String,
    @SerializedName("student_photo") val student_photo: String,
    @SerializedName("staff") val staff: String,
    @SerializedName("staff_id") val staff_id: String,
    @SerializedName("date") val date: String,
    @SerializedName("start_time") val start_time: String,
    @SerializedName("end_time") val end_time: String,
    @SerializedName("room") val room: String,
    @SerializedName("vpml") val vpml: String,
    @SerializedName("class_name") val class_name: String,
    @SerializedName("status") val status: String,
    @SerializedName("book_end_date") val book_end_date: String,
    @SerializedName("booking_open") val booking_open: String
)