package com.mobatia.bisad.activity.payment.model

import com.google.gson.annotations.SerializedName

class PayCategoryModel (
    @SerializedName("status") var status:Int,
    @SerializedName("responseArray") var responseArray:PayCatListResModel
)