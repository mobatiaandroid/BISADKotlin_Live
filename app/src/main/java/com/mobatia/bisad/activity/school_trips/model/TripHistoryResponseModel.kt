package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripHistoryResponseModel {
    @SerializedName("status")
    private var responseCode: Int? = null

    @SerializedName("responseArray")
    private var responseDetail: ResponseDetail? = null

    fun getResponseCode(): Int? {
        return responseCode
    }

    fun setResponseCode(responseCode: Int?) {
        this.responseCode = responseCode
    }

    fun getResponseDetail(): ResponseDetail? {
        return responseDetail
    }

    fun setResponseDetail(responseDetail: ResponseDetail?) {
        this.responseDetail = responseDetail
    }


    class ResponseDetail {
        @SerializedName("response")
        var response: String? = null

        @SerializedName("statuscode")
        var statusCode: String? = null

        // Getters and setters
        @SerializedName("data")
        var trips: ArrayList<Trip>? = null

    }


    class Trip {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("trip_category_id")
        var tripCategoryId: String? = null

        @SerializedName("trip_name")
        var tripName: String? = null

        @SerializedName("preference")
        var preference: String? = null

        @SerializedName("trip_start_date")
        var tripDate: String? = null

        @SerializedName("registration_start_date")
        var registrationStartDate: String? = null

        @SerializedName("registration_end_date")
        var registrationEndDate: String? = null

        @SerializedName("total_price")
        var totalPrice: String? = null

        @SerializedName("trip_status")
        var tripStatus = 0
        @SerializedName("trip_type")
        val trip_type: String? = null
        // Getters and setters
        @SerializedName("trip_image")
        var tripImageUrls: ArrayList<String>? = null

    }
}