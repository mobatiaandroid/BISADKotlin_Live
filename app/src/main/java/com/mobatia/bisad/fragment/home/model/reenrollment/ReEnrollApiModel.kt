package com.mobatia.bisad.fragment.home.model.reenrollment

import com.google.gson.annotations.SerializedName

class ReEnrollEmailApiModel (

    @SerializedName("title") var title:String,
    @SerializedName("message") var message:String
)