package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripCountResponseModel {
    @SerializedName("status")
    private var responseCode: Int? = null

    @SerializedName("responseArray")
    private var response: Response? = null

    fun getResponseCode(): Int? {
        return responseCode
    }

    fun setResponseCode(responseCode: Int?) {
        this.responseCode = responseCode
    }

    fun getResponse(): Response? {
        return response
    }

    fun setResponse(response: Response?) {
        this.response = response
    }


    class Response {
        @SerializedName("trip_max_students")
        var trip_max_students: String? = null
    }

}