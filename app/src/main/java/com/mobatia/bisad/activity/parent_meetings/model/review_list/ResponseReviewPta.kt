package com.mobatia.bisad.activity.parent_meetings.model.review_list

import com.google.gson.annotations.SerializedName

class ResponseReviewPta (
    @SerializedName("response") val response: String,
    @SerializedName("statuscode") val statuscode: String,
    @SerializedName("data") val data: ArrayList<ReviewListModel>

)