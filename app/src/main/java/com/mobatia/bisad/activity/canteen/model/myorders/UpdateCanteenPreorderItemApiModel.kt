package com.mobatia.bisad.activity.canteen.model.myorders

import com.google.gson.annotations.SerializedName

class UpdateCanteenPreorderItemApiModel(
    @SerializedName("studentId") var student_id:String,
    @SerializedName("quantity") var quantity:String,
    @SerializedName("canteen_preorder_item_id") var canteen_preorder_item_id:String
)