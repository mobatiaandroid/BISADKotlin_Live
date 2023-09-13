package com.mobatia.bisad.activity.canteen.model.payment_history

import com.google.gson.annotations.SerializedName

class PaymentHisResponseModel (
    @SerializedName("history_list") var history_list:PaymentHistoryListModel
        )