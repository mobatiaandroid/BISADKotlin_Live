package com.mobatia.bisad.fragment.home.model.reenrollment

import com.google.gson.annotations.SerializedName

class StudentReEnrollList (
    @SerializedName("id") val id: String,
    @SerializedName("unique_id") val unique_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("class_name") val stud_cls: String,
    @SerializedName("section") val section: String,
    @SerializedName("house") val house: String,
    @SerializedName("photo") val photo: String
)