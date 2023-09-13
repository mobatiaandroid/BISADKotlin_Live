package com.mobatia.bisad.activity.payment.model

import com.google.gson.annotations.SerializedName

class PayCatDataList (
    @SerializedName("id") var id:String,
    @SerializedName("title") var title:String,
    @SerializedName("items") var items: ArrayList<PaymentCategoryListModel>

)