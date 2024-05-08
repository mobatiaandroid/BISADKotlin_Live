package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripChoicePreferenceResponseModel {
    @SerializedName("responsecode")
    private val responseCode: String? = null

    @SerializedName("response")
    private val response: InnerResponse? = null

    fun getResponseCode(): String? {
        return responseCode
    }

    fun getResponse(): InnerResponse? {
        return response
    }


    class InnerResponse {
        @SerializedName("response")
        val response: String? = null

        @SerializedName("statuscode")
        val statusCode: String? = null

        @SerializedName("choices")
        val choices: ArrayList<String>? = null
    }

}