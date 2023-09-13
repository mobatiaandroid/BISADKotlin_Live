package com.mobatia.bisad.fragment.staff_directory.model

import com.google.gson.annotations.SerializedName

class TeachingStaffListModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: ResponseTeacherList
)