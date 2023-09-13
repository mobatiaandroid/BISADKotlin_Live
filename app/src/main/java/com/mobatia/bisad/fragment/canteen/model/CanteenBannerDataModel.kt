package com.mobatia.bisad.fragment.canteen.model

import com.google.gson.annotations.SerializedName

class CanteenBannerDataModel (
    @SerializedName("id") var id:Int,
    @SerializedName("image") var image:String,
    @SerializedName("description") var description:String,
    @SerializedName("contact_email") var contact_email:String,
    @SerializedName("wallet_topup_limit") var wallet_topup_limit: String,
    @SerializedName("trn_no") var trn_no: String
)