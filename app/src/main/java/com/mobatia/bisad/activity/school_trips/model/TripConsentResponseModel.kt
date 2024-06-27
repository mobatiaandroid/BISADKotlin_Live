package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripConsentResponseModel {
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

    // Getters and setters
    class ResponseDetail {

        // Getters and setters
        @SerializedName("data")
        var permissionData: PermissionData? = null

    }


    class PermissionData {
        // Getters and setters
        @SerializedName("permission_content")
        var permissionContent: String? = null

    }

}