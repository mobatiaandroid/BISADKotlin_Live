package com.mobatia.bisad.fragment.school_trips.model

import com.google.gson.annotations.SerializedName

class TripCategoriesResponseModel {
    @SerializedName("responsecode")
     var responseCode: String? = null

    @SerializedName("response")
     var responseData: ResponseData? = null

    fun getResponseCode(): String? {
        return responseCode
    }

    fun setResponseCode(responseCode: String?) {
        this.responseCode = responseCode
    }

    fun getResponseData(): ResponseData? {
        return responseData
    }

    fun setResponseData(responseData: ResponseData?) {
        this.responseData = responseData
    }


    class ResponseData {
        @SerializedName("response")
        var response: String? = null

        @SerializedName("statuscode")
        var statusCode: String? = null

        @SerializedName("banner_image")
        var bannerImage: String? = null

        @SerializedName("banner_description")
        var bannerDescription: String? = null

        @SerializedName("banner_contact_email")
        var bannerContactEmail: String? = null

        @SerializedName("data")
        var data: ArrayList<Data>? = null
    }


    class Data {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("trip_category")
        var tripCategory: String? = null

        @SerializedName("image")
        var image: String? = null
    }

}