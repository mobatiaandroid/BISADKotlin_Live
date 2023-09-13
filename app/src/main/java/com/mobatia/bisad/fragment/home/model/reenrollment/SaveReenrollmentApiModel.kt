package com.mobatia.bisad.fragment.home.model.reenrollment

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

class SaveReenrollmentApiModel (
    @SerializedName("enrollment_data") val json: ArrayList<ReEnrollSubModel>
    )