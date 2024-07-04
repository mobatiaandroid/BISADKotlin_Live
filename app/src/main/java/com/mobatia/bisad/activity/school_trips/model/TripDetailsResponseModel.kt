package com.mobatia.bisad.activity.school_trips.model

import com.google.gson.annotations.SerializedName

class TripDetailsResponseModel {
    @SerializedName("status")
    private var status: Int? = null

    @SerializedName("responseArray")
    private var response: ResponseData? = null

    // Getters and setters

    // Getters and setters
    fun getResponseCode(): Int? {
        return status
    }

    fun setResponseCode(responseCode: Int?) {
        this.status = responseCode
    }

    fun getResponse(): ResponseData? {
        return response
    }

    fun setResponse(response: ResponseData?) {
        this.response = response
    }


    class ResponseData {
        // Getters and setters


        @SerializedName("data")
        var data: TripData? = null

        @SerializedName("trip_exceed")
        var trip_exceed: String? = null

        @SerializedName("no_of_trips_exceed")
        var no_of_trips_exceed: String? = null


    }


    class TripData {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("trip_name")
        var tripName: String? = null

        @SerializedName("trip_start_date")
        var tripDate: String? = null

        @SerializedName("trip_end_date")
        var tripEndDate: String? = null

        @SerializedName("registration_start_date")
        var registrationStartDate: String? = null

        @SerializedName("registration_end_date")
        var registrationEndDate: String? = null

        @SerializedName("total_price")
        var totalPrice: String? = null

        //        public void setTripImage(String tripImage) {
        //            this.tripImage = tripImage;
        //        }
        //
        //        public String getTripImage() {
        //            return tripImage;
        //        }
        @SerializedName("trip_image")
        lateinit var tripImage: ArrayList<String>

        @SerializedName("coordinator_name")
        var coordinatorName: String? = null

        @SerializedName("coordinator_email")
        var coordinatorEmail: String? = null

        @SerializedName("coordinator_phone")
        var coordinatorPhone: String? = null

        @SerializedName("coordinator_wp")
        var coordinatorWhatsApp: String? = null

        @SerializedName("description")
        var description: String? = null
        @SerializedName("medical_consent_question")
        val medicalconsentquestion: String? = null
        @SerializedName("trip_status")
        var tripStatus: Int? = null

        @SerializedName("trip_type")
        var trip_type: String? = null

        @SerializedName("installment_details")
        var installmentDetails: ArrayList<InstallmentDetail>? = null

        @SerializedName("document_upload_status")
        var documentUploadStatus: DocumentUploadStatus? = null

        @SerializedName("documents_required")
        var documents_required: DocumentsRequired? = null

        @SerializedName("invoices")
        var invoices: ArrayList<Invoice>? = null

        class Invoice {
            @SerializedName("id")
            var id: String? = null

            @SerializedName("firstname")
            var firstName: String? = null

            @SerializedName("email")
            var email: String? = null

            @SerializedName("paid_amount")
            var paidAmount: String? = null

            @SerializedName("invoice_no")
            var invoiceNumber: String? = null

            @SerializedName("payment_date")
            var paymentDate: String? = null

            @SerializedName("invoice_note")
            var invoiceNote: String? = null

            @SerializedName("trn_no")
            var transactionNumber: String? = null

            @SerializedName("payment_type")
            var paymentType: String? = null
        }

    }


    class InstallmentDetail {
        // Define other fields and getters/setters here
        @SerializedName("id")
        var id = 0

        @SerializedName("amount")
        var amount: String? = null

        @SerializedName("due_date")
        var dueDate: String? = null

        @SerializedName("firstname")
        var firstname: String? = null

        @SerializedName("email")
        var email: String? = null

        @SerializedName("paid_amount")
        var paid_amount: String? = null

        @SerializedName("balance_amount")
        var balance_amount: String? = null

        @SerializedName("invoice_no")
        var invoice_no: String? = null

        @SerializedName("payment_date")
        var payment_date: String? = null

        @SerializedName("invoice_note")
        var invoice_note: String? = null

        @SerializedName("trn_no")
        var trn_no: String? = null

        @SerializedName("payment_type")
        var payment_type: String? = null

        @SerializedName("paid_status")
        var paidStatus = 0
    }


    class DocumentUploadStatus {
        @SerializedName("passport_status")
        var passportStatus = 0

        @SerializedName("visa_status")
        var visaStatus = 0

        @SerializedName("emirates_status")
        var emiratesStatus = 0

        @SerializedName("consent_status")
        var consentStatus = 0
        @SerializedName("medical_consent_status")
        val medicalconsentStatus = 0

        // Define getters and setters here
        @SerializedName("document_completion_status")
        var documentCompletionStatus = 0

    }


    class DocumentsRequired (
        @SerializedName("passport_doc")
        var passport_doc: Int,

        @SerializedName("visa_doc")
        var visa_doc: Int,

        @SerializedName("emirates_doc")
    var emirates_doc: Int,

    @SerializedName("consent_doc")
    var consent_doc: Int,

    @SerializedName("medical_consent_doc")
    var medicalconsentDoc: Int
    )



}