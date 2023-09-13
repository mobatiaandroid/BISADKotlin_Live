package com.mobatia.bisad.fragment.canteen.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.canteen.model.information.InfoListModel

class CanteenBannerResponseArrayModel(
    @SerializedName("data") var data:CanteenBannerDataModel
)