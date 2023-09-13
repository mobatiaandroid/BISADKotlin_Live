package com.mobatia.bisad.activity.canteen.model.add_orders

import com.google.gson.annotations.SerializedName

class CategoryListModel {
    @SerializedName("id")
    var id: String=""
    @SerializedName("category_name")
    var category_name: String=""
    @SerializedName("category_image")
    var category_image: String=""
    @SerializedName("isItemSelected")
    var isItemSelected: Boolean=false
}