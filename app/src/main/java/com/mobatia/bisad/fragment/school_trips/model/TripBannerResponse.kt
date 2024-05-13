package com.mobatia.bisad.fragment.school_trips.model

import com.google.gson.annotations.SerializedName

class TripBannerResponse {
    @SerializedName("status")
     var status: Int? = null

    @SerializedName("responseArray")
     var responseArray: ResponseArrayTrip? = null


    class ResponseArrayTrip {
        @SerializedName("data")
         var data: DataArray? = null


    }
    class DataArray {
        @SerializedName("banner_image")
         val banner_image: String? = null

        @SerializedName("description")
         val description: String? = null

        @SerializedName("contact_email")
         val contact_email: String? = null



    }
}