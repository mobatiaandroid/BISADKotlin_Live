package com.mobatia.bisad.activity.canteen.model.information

import com.google.gson.annotations.SerializedName

class InfoResponseModel (
    @SerializedName("information") var information:ArrayList<InfoListModel>
)