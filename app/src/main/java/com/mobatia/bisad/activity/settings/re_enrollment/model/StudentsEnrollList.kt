package com.mobatia.bisad.activity.settings.re_enrollment.model

import com.google.gson.annotations.SerializedName

class StudentsEnrollList (
    @SerializedName("id") var id:String,
    @SerializedName("unique_id") var unique_id:String,
    @SerializedName("name") var name:String,
    @SerializedName("class_name") var class_name:String,
    @SerializedName("section") var section:String,
    @SerializedName("house") var house:String,
    @SerializedName("photo") var photo:String,
    @SerializedName("parent_name") var parent_name:String,
    @SerializedName("parent_email") var parent_email:String,
    @SerializedName("enrollment_status") var enrollment_status:String,
    @SerializedName("status") var status:String
)