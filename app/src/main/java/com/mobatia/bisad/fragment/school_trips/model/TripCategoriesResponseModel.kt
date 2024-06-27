package com.mobatia.bisad.fragment.school_trips.model

import com.google.gson.annotations.SerializedName

class TripCategoriesResponseModel {
    @SerializedName("status")
     var status: Int? = null

    @SerializedName("responseArray")
     var responseData: ResponseData? = null






    class ResponseData {

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