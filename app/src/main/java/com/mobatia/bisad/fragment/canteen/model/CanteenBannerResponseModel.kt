package com.mobatia.bisad.fragment.canteen.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.canteen.model.information.InfoResponseModel

class CanteenBannerResponseModel(
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray: CanteenBannerResponseArrayModel
)