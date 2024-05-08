package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripConsentResponseModel {
    @SerializedName("responsecode")
    private var responseCode: String? = null

    @SerializedName("response")
    private var responseDetail: ResponseDetail? = null

    fun getResponseCode(): String? {
        return responseCode
    }

    fun setResponseCode(responseCode: String?) {
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
        @SerializedName("response")
        var response: String? = null

        @SerializedName("statuscode")
        var statusCode: String? = null

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