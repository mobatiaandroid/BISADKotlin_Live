package com.mobatia.bisad.activity.school_trips.model

class TripPaymentInitiateResponseModel {
    private var responsecode: String? = null
    private var response: ResponseData? = null

    fun getResponsecode(): String? {
        return responsecode
    }

    fun setResponsecode(responsecode: String?) {
        this.responsecode = responsecode
    }

    fun getResponse(): ResponseData? {
        return response
    }

    fun setResponse(response: ResponseData?) {
        this.response = response
    }


    class ResponseData {
        var response: String? = null
        var statuscode: String? = null
        var order_reference: String? = null
        var order_paypage_url: String? = null
        var authorization: String? = null
        var trip_max_students: String? = null
    }

}