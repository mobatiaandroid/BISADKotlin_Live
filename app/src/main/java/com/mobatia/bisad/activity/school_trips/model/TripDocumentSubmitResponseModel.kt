package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripDocumentSubmitResponseModel {
    @SerializedName("status")
    private val responseCode: Int? = null

    @SerializedName("response")
    private val responseData: ResponseData? = null

    fun getResponseCode(): Int? {
        return responseCode
    }

    fun getResponseData(): ResponseData? {
        return responseData
    }

    class ResponseData {
        @SerializedName("response")
        val response: String? = null

        @SerializedName("statuscode")
        val statusCode: String? = null

        @SerializedName("document_status")
        val documentStatus: DocumentStatus? = null
    }


    class DocumentStatus {
        @SerializedName("passport_status")
        val passportStatus = 0

        @SerializedName("visa_status")
        val visaStatus = 0

        @SerializedName("emirates_status")
        val emiratesStatus = 0

        @SerializedName("consent_status")
        val consentStatus = 0

        @SerializedName("document_completion_status")
        val documentCompletionStatus = 0
    }

}