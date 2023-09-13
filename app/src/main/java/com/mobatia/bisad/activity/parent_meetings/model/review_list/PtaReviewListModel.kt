package com.mobatia.bisad.activity.parent_meetings.model.review_list

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.parent_meetings.model.insert_pta.ResponseInsertPta

class PtaReviewListModel (
    @SerializedName("responsecode") val responsecode: String,
@SerializedName("response") val response: ResponseReviewPta

)