package com.mobatia.bisad.fragment.staff_directory.model

import com.google.gson.annotations.SerializedName

class SendStaffEmailApiModel (
    @SerializedName("staff_email") var staffEmail:String,
    @SerializedName("title") var title:String,
    @SerializedName("message") var message:String
)