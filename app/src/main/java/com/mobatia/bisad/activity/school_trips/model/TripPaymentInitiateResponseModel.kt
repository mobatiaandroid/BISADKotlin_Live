package com.mobatia.bisad.activity.school_trips.model

class TripPaymentInitiateResponseModel {
    private var status: Int? = null
    private var responseArray: ResponseData? = null

    fun getResponsecode(): Int? {
        return status
    }

    fun setResponsecode(responsecode: Int?) {
        this.status = responsecode
    }

    fun getResponse(): ResponseData? {
        return responseArray
    }

    fun setResponse(response: ResponseData?) {
        this.responseArray = response
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