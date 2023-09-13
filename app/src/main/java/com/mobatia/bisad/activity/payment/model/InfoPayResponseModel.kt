package com.mobatia.bisad.activity.payment.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.canteen.model.information.InfoListModel

class InfoPayResponseModel (
    @SerializedName("information") var information:ArrayList<InfoPayListModel>
)