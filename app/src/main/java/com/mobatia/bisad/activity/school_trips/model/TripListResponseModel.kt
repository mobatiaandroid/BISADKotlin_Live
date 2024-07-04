package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripListResponseModel {
    @SerializedName("status")
    private var responseCode: Int? = null

    @SerializedName("responseArray")
    private var response: ResponseData? = null


    fun getResponseCode(): Int? {
        return responseCode
    }

    fun setResponseCode(responseCode: Int?) {
        this.responseCode = responseCode
    }

    fun getResponse(): ResponseData? {
        return response
    }

    fun setResponse(response: ResponseData?) {
        this.response = response
    }


    class ResponseData {
        @SerializedName("response")
        var response: String? = null

        @SerializedName("statuscode")
        var statusCode: String? = null

        @SerializedName("data")
        var data: ArrayList<TripItem>? = null
    }


    class TripItem {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("trip_name")
        var tripName: String? = null

        @SerializedName("trip_start_date")
        var tripDate: String? = null

        @SerializedName("trip_end_date")
        var tripEndDate: String? = null

        @SerializedName("preference")
        var preference: String? = null

        @SerializedName("registration_start_date")
        var registrationStartDate: String? = null

        @SerializedName("registration_end_date")
        var registrationEndDate: String? = null

        @SerializedName("total_price")
        var totalPrice: String? = null

        @SerializedName("trip_status")
        var tripStatus = 0

        @SerializedName("trip_exceed")
        var trip_exceed: String? = null

        @SerializedName("no_of_trips_exceed")
        var no_of_trips_exceed: String? = null
        @SerializedName("trip_type")
        val triptype: String? = null
        @SerializedName("trip_image")
        var tripImage: ArrayList<String>? = null
    }

}