package com.mobatia.bisad.fragment.home.model.reenrollment

import com.google.gson.annotations.SerializedName

class UserDetails (
    @SerializedName("firstname") val firstname: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("email") val email: String
)