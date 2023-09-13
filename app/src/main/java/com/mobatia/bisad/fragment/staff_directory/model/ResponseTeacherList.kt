package com.mobatia.bisad.fragment.staff_directory.model

import com.google.gson.annotations.SerializedName

class ResponseTeacherList  (
    @SerializedName("staff_list") val staff_list: ArrayList<StaffDetailList>

)