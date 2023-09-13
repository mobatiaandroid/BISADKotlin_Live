package com.mobatia.bisad.fragment.permission_slip.model

import com.google.gson.annotations.SerializedName

class PermissionSlipListModel (
    @SerializedName("id")val id:Int,
    @SerializedName("title")val title:String,
    @SerializedName("event_date")val event_date:String,
    @SerializedName("consent")val consent:String,
    @SerializedName("status")val status:String
)