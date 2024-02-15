package com.mobatia.bisad.activity.canteen.model.add_orders

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.canteen.model.AllergyContentModel

class CatItemsListModel (
    @SerializedName("id") val id: String,
    @SerializedName("bar_code") val bar_code: String,
    @SerializedName("item_name") val item_name: String,
    @SerializedName("description") val description: String,
    @SerializedName("item_image") val item_image: String,
    @SerializedName("initial_price") val initial_price: String,
    @SerializedName("vat_percentage") val vat_percentage: String,
    @SerializedName("vat_amount") val vat_amount: String,
    @SerializedName("price") val price: String,
    @SerializedName("isItemCart") var isItemCart:Boolean,
    @SerializedName("cartId") var cartId:String,
    @SerializedName("quantityCart") var quantityCart:Int,
    @SerializedName("item_already_ordered") var item_already_ordered:Int,
    @SerializedName("student_allergy") var student_allergy:Int,
    @SerializedName("allergy_contents") var allergy_contents:ArrayList<AllergyContentModel>,

    var isAllergic: Boolean = false
    /*
     @SerializedName("available_date") val available_date: String,
     @SerializedName("available_quantity") val available_quantity: String,
     @SerializedName("unit") val unit: String,
     @SerializedName("bar_code") val bar_code: String,
     @SerializedName("profit_margin") val profit_margin: String,
     @SerializedName("price") val price: String,
     @SerializedName("barcode_qty") val barcode_qty: String,
     @SerializedName("description") val description: String,
     @SerializedName("item_category_id") val item_category_id: String,
     @SerializedName("status") val status: String,
     @SerializedName("quantityCart") val quantityCart: String,
     @SerializedName("item_already_ordered") val item_already_ordered: String,
     @SerializedName("item_image") val item_image:ArrayList<String>,
     @SerializedName("isItemCart") val isItemCart:Boolean*/
)