package com.mobatia.bisad.fragment.school_trips

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchoolTripsFragment :AppCompatActivity(),AdapterView.OnItemClickListener {
    private var mTitle: String? = null
    private var mTabId: String? = null
    var mContext: Context? = null
    private var mRootView: View? = null
    var extras: Bundle? = null
    var text_content: TextView? = null
    var text_dialog: TextView? = null
    var description = ""
    var contactEmail = ""
    var signRelative: RelativeLayout? = null
    var sendEmail: ImageView? = null
    var tab_type: String? = null
    var signUpModule: TextView? = null
    var descriptionTitle: TextView? = null
    var bannerUrlImageArray: ArrayList<String>? = null
    var mtitle: RelativeLayout? = null
    var bannerImagePager: ImageView? = null
  /*  var parentAssociationEventsModelsArrayList: ArrayList<ParentAssociationEventsModel> =
        ArrayList<ParentAssociationEventsModel>()*/
    var paymentLinear: LinearLayout? = null
    var registerTripLinear: LinearLayout? = null
    var informationLinear: LinearLayout? = null

    var isemoji1Selected = false
    var isemoji2Selected = false
    var isemoji3Selected = false
    var survey_satisfation_status = 0
    var currentPage = 0
    var currentPageSurvey = 0
    private val surveySize = 0
    var isShown = false
    var pos = -1

    var studeLinear: ConstraintLayout? = null
    private val EMAIL_PATTERN =
        "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
    private val pattern = "^([a-zA-Z ]*)$"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_school_trips_new, container,
            false
        )
        mContext = activity
        val mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView.text = "Trips"
        SchoolTripsFragment.Companion.progressDialogP =
            ProgressBarDialog(mContext, R.drawable.spinner)
        registerTripLinear =
            mRootView!!.findViewById<View>(R.id.registerSchoolTripLinear) as LinearLayout
        informationLinear = mRootView!!.findViewById<View>(R.id.informationLinear) as LinearLayout
        paymentLinear = mRootView!!.findViewById<View>(R.id.paymentLinear) as LinearLayout
        studeLinear = mRootView!!.findViewById<View>(R.id.studeLinear) as ConstraintLayout
        mtitle = mRootView!!.findViewById<View>(R.id.title) as RelativeLayout
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImagePager) as ImageView
        signUpModule = mRootView!!.findViewById<View>(R.id.signUpModule) as TextView
        descriptionTitle = mRootView!!.findViewById<View>(R.id.descriptionTitle) as TextView
        sendEmail = mRootView!!.findViewById<View>(R.id.sendEmail) as ImageView
        if (AppUtils.isNetworkConnected(mContext)) {
            callStaffDirectoryListAPI(URL_CANTEEN_BANNER)
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        if (PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            studeLinear!!.visibility = View.VISIBLE
        } else {
            studeLinear!!.visibility = View.VISIBLE
        }
        registerTripLinear!!.setOnClickListener {
            val intent = Intent(
                mContext,
                TripCategoriesActivity::class.java
            )
            intent.putExtra("tab_type", "Register Trip")
            //                    intent.putExtra("email", contactEmail);
            startActivity(intent)
        }
        informationLinear!!.setOnClickListener {
            val intent = Intent(
                mContext,
                TripInfoActivity::class.java
            )
            intent.putExtra("tab_type", "Information")
            startActivity(intent)
        }
        paymentLinear!!.setOnClickListener {
            val intent = Intent(
                mContext,
                TripPaymentsActivity::class.java
            )
            intent.putExtra("tab_type", "Payment")
            intent.putExtra("email", contactEmail)
            intent.putExtra("isFrom", "payment")
            startActivity(intent)
        }
        sendEmail!!.setOnClickListener {
            val dialog = Dialog(mContext!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogCancelButton =
                dialog.findViewById<View>(R.id.cancelButton) as Button
            val submitButton =
                dialog.findViewById<View>(R.id.submitButton) as Button
            text_dialog =
                dialog.findViewById<View>(R.id.text_dialog) as EditText
            text_content =
                dialog.findViewById<View>(R.id.text_content) as EditText
            SchoolTripsFragment.Companion.progressDialog =
                dialog.findViewById<View>(R.id.progressdialogg) as ProgressBar
            dialogCancelButton.setOnClickListener { //   AppUtils.hideKeyBoard(mContext);
                val imm =
                    mContext!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(text_dialog!!.windowToken, 0)
                imm.hideSoftInputFromWindow(text_content!!.windowToken, 0)
                dialog.dismiss()
            }
            submitButton.setOnClickListener {
                if (text_dialog!!.text.toString().trim { it <= ' ' } == "") {
                    val toast = Toast.makeText(
                        mContext, mContext!!.resources.getString(
                            R.string.enter_subjects
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    if (text_content!!.text.toString().trim { it <= ' ' } == "") {
                        val toast = Toast.makeText(
                            mContext, mContext!!.resources.getString(
                                R.string.enter_contents
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else if (contactEmail.matches(EMAIL_PATTERN.toRegex())) {
                        if (text_dialog!!.text.toString().trim { it <= ' ' }
                                .matches(pattern.toRegex())) {
                            if (text_content!!.text.toString().trim { it <= ' ' }
                                    .matches(pattern.toRegex())) {
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF, dialog)
                                } else {
                                    AppUtils.showDialogAlertDismiss(
                                        mContext as Activity?,
                                        "Network Error",
                                        getString(R.string.no_internet),
                                        R.drawable.nonetworkicon,
                                        R.drawable.roundred
                                    )
                                }
                            } else {
                                val toast = Toast.makeText(
                                    mContext, mContext!!.resources.getString(
                                        R.string.enter_valid_contents
                                    ), Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }
                        } else {
                            val toast = Toast.makeText(
                                mContext, mContext!!.resources.getString(
                                    R.string.enter_valid_subjects
                                ), Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    } else {
                        val toast = Toast.makeText(
                            mContext, mContext!!.resources.getString(
                                R.string.enter_valid_email
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }
            dialog.show()
        }
        return mRootView
    }
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    private fun initialiseUI() {
        if (extras != null) {
        }
    }
    private fun sendEmailToStaff(URL: String, dialog: Dialog) {
        SchoolTripsFragment.Companion.progressDialog.setVisibility(View.VISIBLE)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val paramObject = JsonObject()
        paramObject.addProperty("email", contactEmail)
        paramObject.addProperty("title", text_dialog!!.text.toString())
        paramObject.addProperty("message", text_content!!.text.toString())
        val call: Call<SendMailStaffResponseModel> = service.sendmailtostaff(
            "Bearer " + PreferenceManager.getAccessToken(mContext),
            paramObject
        )
        call.enqueue(object : Callback<SendMailStaffResponseModel?> {
            override fun onResponse(
                call: Call<SendMailStaffResponseModel?>,
                response: Response<SendMailStaffResponseModel?>
            ) {
                if (response.isSuccessful()) {
                    SchoolTripsFragment.Companion.progressDialog.setVisibility(View.GONE)
                    val apiResponse: SendMailStaffResponseModel? = response.body()
                    val statuscode: String = apiResponse.getResponse().getStatuscode()
                    if (statuscode == "303") {
                        val toast = Toast.makeText(
                            mContext, mContext!!.resources.getString(
                                R.string.successfully_send_email_staff
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                        dialog.dismiss()
                    } else {
                        val toast = Toast.makeText(
                            mContext, mContext!!.resources.getString(
                                R.string.email_not_staff
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    SchoolTripsFragment.Companion.progressDialog.setVisibility(View.GONE)
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Alert",
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<SendMailStaffResponseModel?>, t: Throwable) {
                SchoolTripsFragment.Companion.progressDialog.setVisibility(View.GONE)
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })

    }
    private fun callStaffDirectoryListAPI(URL: String) {
        SchoolTripsFragment.Companion.progressDialogP.show()
        parentAssociationEventsModelsArrayList = ArrayList<ParentAssociationEventsModel>()
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripsBannerResponseModel> =
            service.tripBanner("Bearer " + PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<TripsBannerResponseModel?> {
            override fun onResponse(
                call: Call<TripsBannerResponseModel?>,
                response: Response<TripsBannerResponseModel?>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: TripsBannerResponseModel = response.body()!!
                    val response_code: String = apiResponse.getResponseCode()
                    if (response_code == "200") {
                        val statuscode: String = apiResponse.getResponseData().getStatusCode()
                        val responseData: TripsBannerResponseModel.ResponseData =
                            apiResponse.getResponseData()
                        if (statuscode == "303") {
                            SchoolTripsFragment.Companion.progressDialogP.hide()
                            val bannerImage: String = apiResponse.getResponseData().getBannerImage()
                            description = apiResponse.getResponseData().getBannerDescription()
                            contactEmail = apiResponse.getResponseData().getBannerContactEmail()

                        }
                    } else if (response_code.equals("500", ignoreCase = true)) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (response_code.equals("400", ignoreCase = true)) {
                        AppUtils.getToken(mContext, object : GetTokenSuccess() {
                            fun tokenrenewed() {}
                        })
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER)
                    } else if (response_code.equals("401", ignoreCase = true)) {
                        AppUtils.getToken(mContext, object : GetTokenSuccess() {
                            fun tokenrenewed() {
                                //callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        })
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER)
                    } else if (response_code.equals("402", ignoreCase = true)) {
                        AppUtils.getToken(mContext, object : GetTokenSuccess() {
                            fun tokenrenewed() {
                                // callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        })
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER)
                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        SchoolTripsFragment.Companion.progressDialogP.hide()
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    SchoolTripsFragment.Companion.progressDialogP.hide()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Alert",
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<TripsBannerResponseModel?>, t: Throwable) {
                SchoolTripsFragment.Companion.progressDialogP.hide()
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })
    }
}



