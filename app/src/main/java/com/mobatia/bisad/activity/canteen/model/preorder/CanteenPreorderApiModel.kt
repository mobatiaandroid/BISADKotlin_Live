package com.mobatia.bisad.activity.canteen.model.preorder

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.canteen.model.canteen_cart.ItemsModel

class CanteenPreorderApiModel (
    @SerializedName("studentId") var student_id:String,
    @SerializedName("orders") var orders:ArrayList<ItemsModel>
)