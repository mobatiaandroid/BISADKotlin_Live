package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripCountResponseModel {
    @SerializedName("responsecode")
    private var responseCode: String? = null

    @SerializedName("response")
    private var response: Response? = null

    fun getResponseCode(): String? {
        return responseCode
    }

    fun setResponseCode(responseCode: String?) {
        this.responseCode = responseCode
    }

    fun getResponse(): Response? {
        return response
    }

    fun setResponse(response: Response?) {
        this.response = response
    }


    class Response {
        @SerializedName("statuscode")
        var statusCode: String? = null

        @SerializedName("response")
        var response: String? = null

        @SerializedName("trip_max_students")
        var trip_max_students: String? = null
    }

}