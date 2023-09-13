package com.mobatia.bisad.fragment.permission_slip.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaResponseArray

class PermissionSlipModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: PermissionSlipResponseArray

)