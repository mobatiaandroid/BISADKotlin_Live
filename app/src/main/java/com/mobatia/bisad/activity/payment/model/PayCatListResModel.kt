package com.mobatia.bisad.activity.payment.model

import com.google.gson.annotations.SerializedName

class PayCatListResModel (
    @SerializedName("data") var data:ArrayList<PayCatDataList>,
    @SerializedName("trn_no") var trn_no:String

)