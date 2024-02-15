package com.mobatia.bisad.activity.canteen.model.canteen_cart

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.canteen.model.AllergyContentModel

class CartItemsListModel (
    @SerializedName("id") var id: Int,
    @SerializedName("item_id") val item_id: Int,
    @SerializedName("delivery_date") val delivery_date: String,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("price") val price: String,
    @SerializedName("item_name") val item_name: String,
    @SerializedName("item_image") val item_image: String,
    @SerializedName("item_total") val item_total: Int,
    @SerializedName("description") val description: String,
    @SerializedName("allergy_contents") val allergy_contents: ArrayList<AllergyContentModel>


)