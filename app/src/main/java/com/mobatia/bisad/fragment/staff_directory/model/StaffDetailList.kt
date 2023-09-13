package com.mobatia.bisad.fragment.staff_directory.model

import com.google.gson.annotations.SerializedName

class StaffDetailList (
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("department") val department: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("staff_photo") val staff_photo: String,
    @SerializedName("biography") val biography: String

)