package com.mobatia.bisad.activity.communication.magazine.model

import com.google.gson.annotations.SerializedName

class MagazineResponseListModel (
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("file") val file: String

)
