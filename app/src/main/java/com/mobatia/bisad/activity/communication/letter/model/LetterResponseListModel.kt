package com.mobatia.bisad.activity.communication.letter.model

import com.google.gson.annotations.SerializedName

class LetterResponseListModel (
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String

)
