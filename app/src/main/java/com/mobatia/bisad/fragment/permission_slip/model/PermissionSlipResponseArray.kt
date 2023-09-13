package com.mobatia.bisad.fragment.permission_slip.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaDetailModel

class PermissionSlipResponseArray (
    @SerializedName("request") val request: ArrayList<PermissionSlipListModel>
)