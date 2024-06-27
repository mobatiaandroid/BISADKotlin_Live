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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.school_trips.TripCategoriesActivity
import com.mobatia.bisad.activity.school_trips.TripInfoActivity
import com.mobatia.bisad.activity.school_trips.TripPaymentsActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.canteen.model.CanteenSendEmailApiModel
import com.mobatia.bisad.fragment.home.sharedprefs
import com.mobatia.bisad.fragment.school_trips.model.TripBannerResponse
import com.mobatia.bisad.fragment.school_trips.model.TripsBannerResponseModel
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchoolTripsFragment :Fragment(),AdapterView.OnItemClickListener {
    private var mTitle: String? = null
    private var mTabId: String? = null
    lateinit var mContext: Context
   // private var mRootView: View? = null
    var extras: Bundle? = null
    lateinit var text_content: TextView
    lateinit var text_dialog: TextView
    var description = ""
    var contactEmail = ""
    var signRelative: RelativeLayout? = null
    lateinit var sendEmail: ImageView
    var tab_type: String? = null
    lateinit var signUpModule: TextView
    lateinit var descriptionTitle: TextView
    var bannerUrlImageArray: ArrayList<String>? = null
    lateinit var mtitle: RelativeLayout
    lateinit var bannerImagePager: ImageView
  /*  var parentAssociationEventsModelsArrayList: ArrayList<ParentAssociationEventsModel> =
        ArrayList<ParentAssociationEventsModel>()*/
    lateinit var paymentLinear: LinearLayout
    lateinit var registerTripLinear: LinearLayout
    lateinit var informationLinear: LinearLayout
    lateinit var progressDialogP: ProgressBarDialog
    lateinit var mTitleTextView:TextView


    var isemoji1Selected = false
    var isemoji2Selected = false
    var isemoji3Selected = false
    var survey_satisfation_status = 0
    var currentPage = 0
    var currentPageSurvey = 0
    private val surveySize = 0
    var isShown = false
    var pos = -1

    lateinit var studeLinear: ConstraintLayout
    private val EMAIL_PATTERN =
        "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
    private val pattern = "^([a-zA-Z ]*)$"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_school_trips_new, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireActivity()
      mTitleTextView = view!!.findViewById<View>(R.id.titleTextView) as TextView
       mTitleTextView.text = "Trips"
       progressDialogP =
            ProgressBarDialog(mContext!!)
        registerTripLinear =
            view!!.findViewById<View>(R.id.registerSchoolTripLinear) as LinearLayout
        informationLinear = view!!.findViewById<View>(R.id.informationLinear) as LinearLayout
        paymentLinear = view!!.findViewById<View>(R.id.paymentLinear) as LinearLayout
        studeLinear = view!!.findViewById<View>(R.id.studeLinear) as ConstraintLayout
        mtitle = view!!.findViewById<View>(R.id.title) as RelativeLayout
        bannerImagePager = view!!.findViewById<View>(R.id.bannerImagePager) as ImageView
       // signUpModule = mRootView!!.findViewById<View>(R.id.signUpModule) as TextView
        descriptionTitle = view!!.findViewById<View>(R.id.descriptionTitle) as TextView
        sendEmail = view!!.findViewById<View>(R.id.sendEmail) as ImageView
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            //callTripBanner()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }


            studeLinear!!.visibility = View.VISIBLE

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
            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            val btn_submit = dialog.findViewById<Button>(R.id.submitButton)
            val btn_cancel = dialog.findViewById<Button>(R.id.cancelButton)
            val text_dialog = dialog.findViewById<EditText?>(R.id.text_dialog)
            val text_content = dialog.findViewById<EditText>(R.id.text_content)

            btn_cancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })

            btn_submit.setOnClickListener {
                if (text_dialog.text.toString().trim().equals("")) {
                    InternetCheckClass.showErrorAlert(
                        mContext,
                        "Please enter your subject",
                        "Alert"
                    )

                } else {
                    if (text_content.text.toString().trim().equals("")) {
                        InternetCheckClass.showErrorAlert(
                            mContext,
                            "Please enter your content",
                            "Alert"
                        )

                    } else {
                        // progressDialog.visibility = View.VISIBLE
                        val aniRotate: Animation =
                            AnimationUtils.loadAnimation(context, R.anim.linear_interpolator)
                        // progressDialog.startAnimation(aniRotate)
                        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                        if (internetCheck) {
                            emailvalidationcheck( text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),
                                com.mobatia.bisad.fragment.home.sharedprefs.getStudentID(mContext).toString(),
                                contactEmail,
                                dialog)
                            /*sendEmail(
                                text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),
                                com.mobatia.bisad.fragment.home.sharedprefs.getStudentID(mContext).toString(),
                                contactEmail,
                                dialog

                            )*/

                        } else {
                            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                        }
                    }
                }
            }
            dialog.show()
        }

    }
    fun emailvalidationcheck( title: String,
                              message: String,
                              studentID: String,
                              staffEmail: String,
                              dialog: Dialog){
        val EMAIL_PATTERN :String=
            "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
        val pattern :String= "^([a-zA-Z ]*)$"

        if (title.equals("")) {
            val toast: Toast = Toast.makeText(
                mContext, mContext.getResources().getString(
                    com.mobatia.bisad.R.string.enter_subjects
                ), Toast.LENGTH_SHORT
            )
            toast.show()
        } else {
            if (message.equals("")) {
                val toast: Toast = Toast.makeText(
                    mContext, mContext.getResources().getString(
                        R.string.enter_contents
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            } else if (staffEmail.matches(EMAIL_PATTERN.toRegex())) {
                if (title.toString().trim().matches(pattern.toRegex())) {
                    if (title.toString().length>=500){
                        Toast.makeText(mContext, "Subject is too long", Toast.LENGTH_SHORT).show()

                    }else{
                        if (message.toString().trim().matches(pattern.toRegex())) {
                            if (message.length<=500){
                                if (InternetCheckClass.isInternetAvailable(mContext)) {
                                    callsendEmail(
                                        title,
                                        message,
                                        studentID,
                                        contactEmail,
                                        dialog

                                    )
                                } else {
                                    CommonFunctions.faliurepopup(mContext)
                                }
                            }else{
                                Toast.makeText(context, "Message is too long", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            val toast: Toast = Toast.makeText(
                                mContext, mContext.getResources().getString(
                                    R.string.enter_valid_contents
                                ), Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }


                } else {
                    val toast: Toast = Toast.makeText(
                        mContext, mContext.getResources().getString(
                            R.string.enter_valid_subjects
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            } else {
                val toast: Toast = Toast.makeText(
                    mContext, mContext.getResources().getString(
                        R.string.enter_valid_mail
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }

    fun callsendEmail(title: String,
                  message: String,
                  studentID: String,
                  staffEmail: String,
                  dialog: Dialog
    ){
        val token = com.mobatia.bisad.fragment.home.sharedprefs.getaccesstoken(mContext)
        val sendMailBody = CanteenSendEmailApiModel( staffEmail, title, message)
        val call: Call<SendStaffEmailModel> =
            ApiClient.getClient.sendEmailCanteen(sendMailBody, "Bearer " + token)
        call.enqueue(object : Callback<SendStaffEmailModel> {
            override fun onFailure(call: Call<SendStaffEmailModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<SendStaffEmailModel>, response: Response<SendStaffEmailModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {


                        if (response.body()!!.status==100) {
                            dialog.dismiss()
                            showSuccessAlert(
                                mContext,
                                mContext.getString(R.string.mail_success_message),
                                mContext.getString(R.string.mail_alert_success) ,
                                dialog
                            )
                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(mContext)
                                callsendEmail(
                                    title,
                                    message,
                                    studentID,
                                    staffEmail,
                                    dialog
                                )
                            } else {
                                if (response.body()!!.status==103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    fun showSuccessAlert(context: Context, message: String, msgHead: String, mdialog: Dialog) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        iconImageView.setImageResource(R.drawable.exclamationicon)
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            mdialog.dismiss()
        }
        dialog.show()
    }
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    private fun initialiseUI() {
        if (extras != null) {
        }
    }

    private fun callTripBanner() {
       progressDialogP.show()
        val call: Call<TripBannerResponse> =
            ApiClient.getClient.tripsBanner( "Bearer " + PreferenceData().getaccesstoken(mContext))
        call.enqueue(object : Callback<TripBannerResponse> {
            override fun onFailure(call: Call<TripBannerResponse>, t: Throwable) {


                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(call: Call<TripBannerResponse>, response: Response<TripBannerResponse>) {
                val responsedata = response.body()
                val apiResponse: TripBannerResponse = response.body()!!
                val response_code: Int? = response.body()!!.status
                if (response_code == 100) {



                        progressDialogP.hide()
                        val bannerImage: String = apiResponse.responseArray!!.data!!.banner_image!!
                        description = apiResponse.responseArray!!.data!!.description!!
                        contactEmail = apiResponse.responseArray!!.data!!.contact_email!!

                    if (bannerImage != "") {
                        Glide.with(mContext).load(CommonFunctions.replace(bannerImage))
                            .centerCrop().placeholder(R.drawable.default_banner)
                            .into(bannerImagePager)
                    } else {
                        bannerImagePager.setBackgroundResource(R.drawable.default_banner)
                    }
                    if (!response.body()!!.responseArray!!.data!!.description.equals("")) {
                        descriptionTitle.visibility = View.VISIBLE
                        descriptionTitle.text = response.body()!!.responseArray!!.data!!.description
                    }
                    else descriptionTitle.visibility = View.GONE
                    if (!response.body()!!.responseArray!!.data!!.contact_email.equals("")) {
                        sendEmail.visibility = View.VISIBLE
                        contactEmail = response.body()!!.responseArray!!.data!!.contact_email!!
                    } else sendEmail.visibility = View.GONE


                }

            }

        })

       /* val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
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
                            progressDialogP.hide()
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
                        callTripBanner()
                    } else if (response_code.equals("401", ignoreCase = true)) {
                        AppUtils.getToken(mContext, object : GetTokenSuccess() {
                            fun tokenrenewed() {
                                //callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        })
                        callTripBanner()
                    } else if (response_code.equals("402", ignoreCase = true)) {
                        AppUtils.getToken(mContext, object : GetTokenSuccess() {
                            fun tokenrenewed() {
                                // callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        })
                        callTripBanner()
                    } else {
                        *//*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*//*
                       progressDialogP.hide()
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                   progressDialogP.hide()
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
                progressDialogP.hide()
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })*/
    }
}



