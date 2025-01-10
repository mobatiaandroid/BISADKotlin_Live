package com.mobatia.bisad.activity.school_trips

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenApiModel
import com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenModel
import com.mobatia.bisad.activity.school_trips.adapter.TripInstallmentsAdapter
import com.mobatia.bisad.activity.school_trips.model.PaymentGatewayApiModelTrip
import com.mobatia.bisad.activity.school_trips.model.TripDetailsResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripPaymentInitiateResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripPaymentSubmitModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.ProgressBarDialog
import com.mobatia.bisad.fragment.school_trips.model.TripCategoriesResponseModel
import com.mobatia.bisad.manager.AppController
import com.mobatia.bisad.manager.HeaderManager
import com.mobatia.bisad.manager.ItemOffsetDecoration
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.DividerItemDecoration
import com.mobatia.bisad.recyclermanager.RecyclerItemListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import payment.sdk.android.PaymentClient
import payment.sdk.android.SDKConfig
import payment.sdk.android.cardpayment.CardPaymentData
import payment.sdk.android.cardpayment.CardPaymentRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Arrays

class TripInstallmentActivity : AppCompatActivity() {
    lateinit var context: Context
    var extras: Bundle? = null
    var tab_type: String? = null
    lateinit var relativeHeader: RelativeLayout
    lateinit var headermanager: HeaderManager
    private val pictureImagePath = ""
    var bannerImageView: ImageView? = null

    //    TextView descriptionTextView;
    var sendEmailImageView: ImageView? = null

    //    RecyclerView tripImagesRecycler;
    lateinit var recyclerViewLayoutManager: LinearLayoutManager
    var categoriesList: ArrayList<TripCategoriesResponseModel.Data>? = null
    lateinit var tripsCategoryAdapter: TripInstallmentsAdapter
    var contactEmail = ""
    lateinit var back: ImageView
    lateinit var btn_history: ImageView
    lateinit var home: ImageView
    var stud_id: String? = null
    var studClass = ""
    var orderId = ""
    var tripStatus = 0
    var Tokenacesss: String? = null
    var permissionSlip = ""
    var installmentID = ""
    var installmentAmount = ""
    var studentList = ArrayList<String>()
    var tripMainBanner: ImageView? = null
    lateinit var tripImageRecycler: RecyclerView
    var tripNameTextView: TextView? = null
    var tripAmountTextView: TextView? = null
    var dateTextView: TextView? = null
    var coordinatorNameTextView: TextView? = null
    var mActivity: Activity = this
    var coordinatorDetails: Button? = null
    var coordinatorPhoneTextView: TextView? = null
    var tripDescriptionTextView: TextView? = null
    var submitIntentionButton: Button? = null
    var submitDetailsButton: Button? = null
    var paymentButton: Button? = null
    var tripStatusTextView: TextView? = null
    var tripID: String = ""
    var tripName = ""
    private val flag = true
    var multipleInstallmentsArray: ArrayList<TripDetailsResponseModel.InstallmentDetail> = ArrayList<TripDetailsResponseModel.InstallmentDetail>()
    var singleInstallmentAmount = ""
    var coodName: String? = null
    var coodPhone: String? = null
    var coodEmail: String? = null
    var coodWhatsapp: String? = null
    var passportFrontURI: Uri? = null
    var passportBackURI: Uri? = null
    var visaFrontURI: Uri? = null
    var visaBackURI: Uri? = null
    var eIDFrontURI: Uri? = null
    var eIDBackURI: Uri? = null
    var passportFrontFile: File? = null
    var PaymentToken = ""
    var OrderRef = ""
    var PayUrl = ""
    var AuthUrl = ""
    var merchantOrderReference = ""
   // private val arrayList: ArrayList<String?> = ArrayList<Any?>()
    var currentPosition = 0
    var passportURIArray = ArrayList<Uri>()
    var visaURIArray = ArrayList<Uri>()
    var eIDURIArray = ArrayList<Uri>()
    var passportStatus = 0
    var visaStatus = 0
    var eIDStatus = 0
    private lateinit var progressDialogP: ProgressBarDialog

    //    LinearLayout mStudentSpinner;
    var studentName: TextView? = null
    var studImg: ImageView? = null
    lateinit var mProgressRelLayout: ProgressBar

    var stud_img = ""
    lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_installment)
        context = this
        activity=this
        initialiseUI()
    }

    private fun initialiseUI() {
        extras = intent.extras
        if (extras != null) {
            tripID = extras!!.getString("tripID")!!
            tripName = extras!!.getString("tripName")!!
        }
        progressDialogP = ProgressBarDialog(context)
        mProgressRelLayout=findViewById(R.id.progressDialog)


        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader)
        headermanager = HeaderManager(this@TripInstallmentActivity, "Pay in Instalments")
        headermanager.getHeader(relativeHeader, 6)
        back = headermanager.getLeftButton()
        btn_history = headermanager.getRightHistoryImage()
        btn_history!!.visibility = View.INVISIBLE
        tripImageRecycler = findViewById<RecyclerView>(R.id.tripListRecycler)
        tripImageRecycler.setHasFixedSize(true)
        val spacing = 5 // 50px
        val itemDecoration = ItemOffsetDecoration(context, spacing)
        recyclerViewLayoutManager = LinearLayoutManager(context)
        tripImageRecycler.addItemDecoration(DividerItemDecoration(context!!.resources.getDrawable(R.drawable.list_divider)))
        tripImageRecycler.addItemDecoration(itemDecoration)
        tripImageRecycler.setLayoutManager(recyclerViewLayoutManager)
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back)
        back!!.setOnClickListener {
            CommonFunctions.hideKeyBoard(context)
            finish()
        }
        home = headermanager.getLogoButton()!!
        home!!.setOnClickListener {
            val `in` = Intent(
                context,
                HomeActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }

        var internetCheck = InternetCheckClass.isInternetAvailable(context)
        if (internetCheck)
        {
            getTripDetails(tripID)
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

        tripImageRecycler.addOnItemTouchListener(
            RecyclerItemListener(context, tripImageRecycler,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        installmentID =
                            java.lang.String.valueOf(multipleInstallmentsArray[position].id)
                        installmentAmount =
                            java.lang.String.valueOf(multipleInstallmentsArray[position].amount)
                        if (multipleInstallmentsArray[position].paidStatus === 1) {
//                            Toast.makeText(context, "Installment already paid.", Toast.LENGTH_SHORT).show();
                            val intent = Intent(
                                context,
                               TripInvoicePrintActivity::class.java
                            )
                            intent.putExtra("title", "$tripName Receipt")
                            intent.putExtra("tab_type", "Trip Invoice")
                            intent.putExtra(
                                "orderId",
                                multipleInstallmentsArray[position].invoice_no
                            )
                            intent.putExtra(
                                "invoice",
                                multipleInstallmentsArray[position].invoice_note
                            )
                            intent.putExtra(
                                "amount",
                                multipleInstallmentsArray[position].paid_amount
                            )
                            intent.putExtra(
                                "paidby",
                                multipleInstallmentsArray[position].firstname
                            )
                            intent.putExtra(
                                "paidDate", CommonFunctions.dateConversionddmmyyyytoddMMYYYY(
                                    multipleInstallmentsArray[position].payment_date
                                )
                            )
                            intent.putExtra(
                                "tr_no",
                                multipleInstallmentsArray[position].trn_no
                            )
                            intent.putExtra(
                                "payment_type",
                                multipleInstallmentsArray[position].payment_type
                            )
                            intent.putExtra(
                                "received_amount",
                                multipleInstallmentsArray!![position].received_amount
                            )
                            intent.putExtra(
                                "trip_amount",
                                multipleInstallmentsArray!![position].trip_amount
                            )
                            intent.putExtra(
                                "outstanding_amount",
                                multipleInstallmentsArray!![position].outstanding_amount
                            )
                            startActivity(intent)
                        } else {
                            initialisePayment(position)
                        }
                    }

                    override fun onLongClickItem(v: View?, position: Int) {}
                })
        )
    }

    private fun initialisePayment(position: Int) {
        progressDialogP.show()
        val paymentTokenBody = PaymentTokenApiModel( PreferenceData().getStudentID(context).toString())
        val call: Call<PaymentTokenModel> =
            ApiClient(context).getClient.payment_token_trip(paymentTokenBody,"Bearer " + PreferenceData().getaccesstoken(context))
        call.enqueue(object : Callback<PaymentTokenModel> {
            override fun onFailure(call: Call<PaymentTokenModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<PaymentTokenModel>, response: Response<PaymentTokenModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    progressDialogP.dismiss()
                    if (response.body()!!.status==100) {

                        Tokenacesss = response.body()!!.responseArray.access_token
                        val amountDouble = installmentAmount.toDouble() * 100
                        val amuntInt = amountDouble.toInt()
                        val strDouble = amuntInt.toString()
                        var arrayLength = 0
                        val array = strDouble.toCharArray()
                        arrayLength = array.size
                        var firstNonZeroAt = 0
                        for (i in array.indices) {
                            if (!array[i].toString().equals("0", ignoreCase = true)) {
                                firstNonZeroAt = i
                                break
                            }
                        }
                        val newArray = Arrays.copyOfRange(array, firstNonZeroAt, arrayLength)
                        val resultString = String(newArray)
                        // System.out.println("amount removed zero" + resultString);
                        val unixTime = System.currentTimeMillis() / 1000L
                        orderId = "BISAD_TRIP_AND_$unixTime"
                        callForPayment(strDouble,position)
                    }
                    else {

//                        progressBarDialog.hide();
                        CommonFunctions.showDialogAlertDismiss(
                            context as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }

                }
            }

        })





       /* val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<FeePayAccessTokenResponseModel> =
            service.accessTokenTripPayment("Bearer " + PreferenceManager.getAccessToken(context))
        call.enqueue(object : Callback<FeePayAccessTokenResponseModel> {
            override fun onResponse(
                call: Call<FeePayAccessTokenResponseModel>,
                response: Response<FeePayAccessTokenResponseModel>
            ) {
                if (response.isSuccessful()) {
                    TripInstallmentActivity.Companion.progressDialogP.hide()
                    val apiResponse: FeePayAccessTokenResponseModel? = response.body()
                    val response_code: String = apiResponse.getResponsecode()
                    if (response_code == "200") {
                        val statuscode: String = apiResponse.getResponse().getStatuscode()
                        val responseData: FeePayAccessTokenResponseModel.ResponseData =
                            apiResponse.getResponse()
                        if (statuscode == "303") {
                            paymentToken = response.body().getResponse().getAccess_token()
                            val amountDouble: Double = java.lang.String.valueOf(
                                multipleInstallmentsArray[position].getAmount()
                            ).toDouble() * 100
                            val amuntInt = amountDouble.toInt()
                            val strDouble = amuntInt.toString()
                            var arrayLength = 0
                            val array = strDouble.toCharArray()
                            arrayLength = array.size
                            var firstNonZeroAt = 0
                            for (i in array.indices) {
                                if (!array[i].toString().equals("0", ignoreCase = true)) {
                                    firstNonZeroAt = i
                                    break
                                }
                            }
                            val newArray = Arrays.copyOfRange(array, firstNonZeroAt, arrayLength)
                            val resultString = String(newArray)
                            //System.out.println("amount removed zero" + resultString);
                            val unixTime = System.currentTimeMillis() / 1000L
                            orderId = "NASDUBAI_TRIP_AND_$unixTime"
                            callForPayment(strDouble, position)
                        }
                    } else {

//                        progressBarDialog.hide();
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                }
            }

            override fun onFailure(call: Call<FeePayAccessTokenResponseModel>, t: Throwable) {
                TripInstallmentActivity.Companion.progressDialogP.hide()
                AppUtils.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    context!!.getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })*/
    }

    private fun callForPayment(strDouble: String, position: Int) {
        mProgressRelLayout.visibility=View.VISIBLE

        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        //   var mechantorderRef=invoice_ref+"-"+ts
        var mechantorderRef="BISTRIPAND$ts"

        val token = PreferenceData().getaccesstoken(context)
        val paymentGatewayBody = PaymentGatewayApiModelTrip(strDouble,PreferenceData().getUserEmail(context).toString(),
            mechantorderRef,PreferenceData().getStudentName(context)!!,"","BISAD","","Abu Dhabi",
            Tokenacesss!!,tripID)
        val call: Call<TripPaymentInitiateResponseModel> =
            ApiClient(context).getClient.payment_gateway_trip(paymentGatewayBody, "Bearer " + token)
        call.enqueue(object : Callback<TripPaymentInitiateResponseModel> {
            override fun onFailure(call: Call<TripPaymentInitiateResponseModel>, t: Throwable) {

                mProgressRelLayout.visibility=View.GONE
                CommonFunctions.faliurepopup(context)
            }

            override fun onResponse(call: Call<TripPaymentInitiateResponseModel>, response: Response<TripPaymentInitiateResponseModel>) {
                val responsedata = response.body()
                mProgressRelLayout.visibility=View.GONE
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {

                        if (response.body()!!.getResponsecode() == 100) {

                            OrderRef = responsedata.getResponse()!!.order_reference!!
                            var orderPageUrl = responsedata.getResponse()!!.order_paypage_url
                            var auth = responsedata.getResponse()!!.authorization
                            val Code: String = orderPageUrl!!.split("=").toTypedArray().get(1)

                            SDKConfig.shouldShowOrderAmount(true)
                            SDKConfig.shouldShowCancelAlert(true)
                            val request: CardPaymentRequest =
                                CardPaymentRequest.Builder().gatewayUrl(auth!!).code(Code).build()

                            val paymentClient = PaymentClient(mActivity, "fdhasfd")
                            paymentClient.launchCardPayment(request, 101)
                            /*if (responsedata.getResponse()!!.trip_max_students.equals("")) {
                                paymentClient.launchCardPayment(request, 101)
                            } else CommonFunctions.showDialogAlertDismiss(
                                context as Activity?,
                                "Alert",
                                "You can no longer pay for this trip, as all the slots have been filled.",
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )*/
                        } else {
                            if (response.body()!!.getResponsecode() == 116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                // getpaymenttoken()
                            } else {
                                if (response.body()!!.getResponsecode() == 103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(
                                        response.body()!!.getResponsecode()!!,
                                        context
                                    )
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            }
        })


        /*val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        merchantOrderReference = "NASTRIPAND$ts"

        // merchantOrderReference="MUSICACAT2-01-1610606134";
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val paramObject = JsonObject()
        paramObject.addProperty("access_token", paymentToken)
        paramObject.addProperty("amount", strDouble)
        paramObject.addProperty("merchantOrderReference", merchantOrderReference)
        paramObject.addProperty("firstName", PreferenceManager.getCanteenStudentName(context))
        paramObject.addProperty("lastName", "")
        paramObject.addProperty("address1", "NAS DUBAI")
        paramObject.addProperty("city", "DUBAI")
        paramObject.addProperty("countryCode", "UAE")
        paramObject.addProperty("emailAddress", PreferenceManager.getUserEmail(context))
        paramObject.addProperty("trip_id", tripID)
        val call: Call<TripPaymentInitiateResponseModel> = service.createTripPayment(
            "Bearer " + PreferenceManager.getAccessToken(context),
            paramObject
        )
        call.enqueue(object : Callback<TripPaymentInitiateResponseModel?> {
            override fun onResponse(
                call: Call<TripPaymentInitiateResponseModel?>,
                response: Response<TripPaymentInitiateResponseModel?>
            ) {
                if (response.isSuccessful()) {
                    TripInstallmentActivity.Companion.progressDialogP.hide()
                    val apiResponse: TripPaymentInitiateResponseModel? = response.body()
                    val response_code: String = apiResponse.getResponsecode()
                    if (response_code == "200") {
                        val statuscode: String = apiResponse.getResponse().getStatuscode()
                        val responseData: TripPaymentInitiateResponseModel.ResponseData =
                            apiResponse.getResponse()
                        if (statuscode == "303") {
                            OrderRef = apiResponse.getResponse().getOrder_reference()
                            PayUrl = apiResponse.getResponse().getOrder_paypage_url()
                            AuthUrl = apiResponse.getResponse().getAuthorization()
                            val Code = PayUrl.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()[1]
                            val request: CardPaymentRequest =
                                Builder().gatewayUrl(AuthUrl).code(Code).build()
                            val paymentClient = PaymentClient(mActivity, "fdhasfd")
                            paymentClient.launchCardPayment(request, 101)
                        }
                    } else {
                        *//*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*//*
                        TripInstallmentActivity.Companion.progressDialogP.hide()
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                }
            }

            override fun onFailure(call: Call<TripPaymentInitiateResponseModel?>, t: Throwable) {
                TripInstallmentActivity.Companion.progressDialogP.hide()
                AppUtils.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    context!!.getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                when (resultCode) {
                    Activity.RESULT_OK -> onCardPaymentResponse(
                        CardPaymentData.getFromIntent(data!!)
                    )
                    Activity.RESULT_CANCELED ->{
                        Toast.makeText(context, "Transaction Failed", Toast.LENGTH_SHORT).show();

                    }
                }
                /*val cardPaymentData = CardPaymentData.getFromIntent(data!!)
                if (cardPaymentData.code == 2) {
                    paymentSubmitAPI()
                } else {
                    Toast.makeText(context, "Transaction failed", Toast.LENGTH_SHORT).show()
                }*/
            } else Toast.makeText(context, "Transaction cancelled.", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(context, "Transaction cancelled.", Toast.LENGTH_SHORT).show()
    }
    fun onCardPaymentResponse(data: CardPaymentData) {
        when (data.code) {
            CardPaymentData.STATUS_PAYMENT_AUTHORIZED,
            CardPaymentData.STATUS_PAYMENT_CAPTURED -> {
                var internetCheck = InternetCheckClass.isInternetAvailable(context)
                if (internetCheck)
                {
                    paymentSubmitAPI()
                }
                else{
                    InternetCheckClass.showSuccessInternetAlert(context)
                }
            }
            CardPaymentData.STATUS_PAYMENT_FAILED -> {
                Toast.makeText(context, "Transaction Failed", Toast.LENGTH_SHORT).show();
            }
            CardPaymentData.STATUS_GENERIC_ERROR -> {
                Toast.makeText(context, data.reason, Toast.LENGTH_SHORT).show();
            }
            else -> IllegalArgumentException(
                "Unknown payment response (${data.reason})")
        }
    }
    private fun paymentSubmitAPI() {
        progressDialogP.show()

        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        //   var mechantorderRef=invoice_ref+"-"+ts
        merchantOrderReference="BISTRIPAND$ts"
        val paramObject = JsonObject()
        paramObject.addProperty("student_id", PreferenceData().getStudentID(context))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("order_reference", OrderRef)
        paramObject.addProperty("invoice_number", merchantOrderReference)
        paramObject.addProperty("paid_amount", installmentAmount)
        paramObject.addProperty("payment_type", "installment")
        paramObject.addProperty("installment_id", installmentID)
        paramObject.addProperty("device_type", "2")
        paramObject.addProperty("device_name", "Android")
        paramObject.addProperty("app_version", "1.0")
        val call: Call<TripPaymentSubmitModel> = ApiClient(context).getClient.paymentSubmit(
            "Bearer " + PreferenceData().getaccesstoken(context),paramObject)
        call.enqueue(object : Callback<TripPaymentSubmitModel> {
            override fun onResponse(
                call: Call<TripPaymentSubmitModel>,
                response: Response<TripPaymentSubmitModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {
                    Toast.makeText(context, "Payment Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(
                        context,
                        TripDetailActivity::class.java
                    )
                    intent.putExtra("tripID", tripID)
                    intent.putExtra("tripName", tripName)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@TripInstallmentActivity,
                        "Document submit failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<TripPaymentSubmitModel>, t: Throwable) {
                progressDialogP.dismiss()
            }

        })
       /* val paramObject = JsonObject()
        paramObject.addProperty("student_id", PreferenceManager.getStudIdForCCA(context))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("order_reference", OrderRef)
        paramObject.addProperty("invoice_number", merchantOrderReference)
        paramObject.addProperty("paid_amount", installmentAmount)
        paramObject.addProperty("payment_type", "installment")
        paramObject.addProperty("installment_id", installmentID)
        paramObject.addProperty("device_type", "2")
        paramObject.addProperty("device_name", "Android")
        paramObject.addProperty("app_version", "1.0")
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<MusicBaseResponseModel> = service.paymentSubmit(
            "Bearer " + PreferenceManager.getAccessToken(context),
            paramObject
        )
        TripInstallmentActivity.Companion.progressDialogP.show()
        call.enqueue(object : Callback<MusicBaseResponseModel?> {
            override fun onResponse(
                call: Call<MusicBaseResponseModel?>,
                response: Response<MusicBaseResponseModel?>
            ) {
                TripInstallmentActivity.Companion.progressDialogP.hide()
                if (response.body() != null) {
                    val responseCode = response.code().toString()
                    if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                        if (response.body().getResponse().getStatusCode().equalsIgnoreCase("303")) {

                        } else {
                            AppUtils.showNotifyAlertDismiss(
                                context as Activity?,
                                "Alert",
                                "Submission Failed",
                                R.drawable.remove,
                                R.drawable.round
                            )
                        }
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                    //                    studentDataList = new ArrayList<>();
                }
            }

            override fun onFailure(call: Call<MusicBaseResponseModel?>, t: Throwable) {
                TripInstallmentActivity.Companion.progressDialogP.hide()
                AppUtils.showNotifyAlertDismiss(
                    context as Activity?,
                    "Alert",
                    "Submission Failed",
                    R.drawable.remove,
                    R.drawable.round
                )
            }
        }
        )*/
    }

    private fun getTripDetails(tripID: String?) {
        progressDialogP.show()

        val paramObject = JsonObject()
        paramObject.addProperty("student_id", PreferenceData().getStudentID(context))
        paramObject.addProperty("trip_id", tripID)
        val call: Call<TripDetailsResponseModel> =
            ApiClient(context).getClient.tripDetail("Bearer " + PreferenceData().getaccesstoken(context),paramObject)
        call.enqueue(object : Callback<TripDetailsResponseModel> {
            override fun onFailure(call: Call<TripDetailsResponseModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<TripDetailsResponseModel>, response: Response<TripDetailsResponseModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    progressDialogP.dismiss()
                    if (response.body()!!.getResponseCode()==100) {
                        if (response.body()!!.getResponse()!!.data!!.installmentDetails!!.size> 0
                        ) {
                            multipleInstallmentsArray = response.body()!!.getResponse()!!.data!!.installmentDetails!!
                        }
                        singleInstallmentAmount = response.body()!!.getResponse()!!.data!!.totalPrice!!
                        tripStatus = response.body()!!.getResponse()!!.data!!.tripStatus!!
                        tripsCategoryAdapter =
                            TripInstallmentsAdapter(context, multipleInstallmentsArray)
                        tripImageRecycler!!.adapter = tripsCategoryAdapter
                    } else {
                    }
                    }


            }

        })





      /*  val paramObject = JsonObject()
        //Log.e("tripID name", tripID);
        paramObject.addProperty("student_id", PreferenceManager.getStudIdForCCA(context))
        paramObject.addProperty("trip_id", tripID)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripDetailsResponseModel> =
            service.tripDetail("Bearer " + PreferenceManager.getAccessToken(context), paramObject)
        call.enqueue(object : Callback<TripDetailsResponseModel> {
            override fun onResponse(
                call: Call<TripDetailsResponseModel>,
                response: Response<TripDetailsResponseModel>
            ) {
                TripInstallmentActivity.Companion.progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponse().getStatusCode().equalsIgnoreCase("303")) {

                } else {
                }
            }

            override fun onFailure(call: Call<TripDetailsResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                //Log.e("error", t.getLocalizedMessage());
                AppUtils.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })*/
    }

    companion object {


        private const val VISA_CAMERA_REQUEST = 3
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
        private const val PICK_IMAGE_FRONT_PASSPORT = 1
        private const val PICK_IMAGE_BACK_PASSPORT = 2
        private const val PICK_IMAGE_FRONT_VISA = 3
        private const val PICK_IMAGE_BACK_VISA = 4
        private const val PICK_IMAGE_FRONT_EID = 5
        private const val PICK_IMAGE_BACK_EID = 6
    }
    override fun onResume() {
        super.onResume()
        if (!CommonFunctions.runMethod.equals("Dev")) {
            if (CommonFunctions.isDeveloperModeEnabled(context)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
        }
    }
}