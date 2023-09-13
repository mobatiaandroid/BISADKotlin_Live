package com.mobatia.bisad.fragment.forms.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel
import com.mobatia.bisad.fragment.messages.model.MessageResponseArrayModel

class FormsResponseModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArrayList: List<FormsResponseArrayDetail>
)
