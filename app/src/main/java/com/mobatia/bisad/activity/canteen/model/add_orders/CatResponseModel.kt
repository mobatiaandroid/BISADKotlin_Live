package com.mobatia.bisad.activity.canteen.model.add_orders

import com.google.gson.annotations.SerializedName

class CatResponseModel (
    @SerializedName("data") var data:ArrayList<CategoryListModel>
)