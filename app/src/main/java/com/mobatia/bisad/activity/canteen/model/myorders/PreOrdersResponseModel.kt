package com.mobatia.bisad.activity.canteen.model.myorders

import com.google.gson.annotations.SerializedName

class PreOrdersResponseModel (
@SerializedName("whole_total") var whole_total:String,
@SerializedName("data") var data:ArrayList<PreOrdersListModel>
)