package com.mobatia.bisad.activity.canteen.model.wallethistory

import com.google.gson.annotations.SerializedName

class WalletHisResModel (
@SerializedName("credit_history") var credit_history:ArrayList<CreditHisListModel>
)