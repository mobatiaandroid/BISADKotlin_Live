package com.mobatia.bisad.fragment.forms.model

import com.google.gson.annotations.SerializedName

class FormsResponseArrayDetail (
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
)
