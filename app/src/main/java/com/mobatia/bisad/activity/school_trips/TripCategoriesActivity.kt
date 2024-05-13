package com.mobatia.bisad.activity.school_trips

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.home.sharedprefs
import com.mobatia.bisad.activity.school_trips.adapter.TripsCategoryAdapter
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.ProgressBarDialog
import com.mobatia.bisad.fragment.canteen.model.CanteenSendEmailApiModel
import com.mobatia.bisad.fragment.school_trips.model.TripCategoriesResponseModel
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailModel
import com.mobatia.bisad.manager.HeaderManagerNoColorSpace
import com.mobatia.bisad.manager.ItemOffsetDecoration
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripCategoriesActivity : AppCompatActivity() {
    lateinit var context: Context
    var extras: Bundle? = null
    var tab_type: String? = null
    private val EMAIL_PATTERN =
        "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
    private val pattern = "^([a-zA-Z ]*)$"
    lateinit var text_content: TextView
    lateinit var text_dialog: TextView
    private lateinit var progressDialogP: ProgressBarDialog
    private val progressDialog: ProgressBar? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManagerNoColorSpace? = null
    lateinit var bannerImageView: ImageView
    lateinit var descriptionTextView: TextView
    lateinit var sendEmailImageView: ImageView
    lateinit var categoryListRecyclerView: RecyclerView
    lateinit var recyclerViewLayoutManager: GridLayoutManager

    var categoriesList: ArrayList<TripCategoriesResponseModel.Data>? = null
    var tripsCategoryAdapter: TripsCategoryAdapter? = null
    var contactEmail = ""
    lateinit var back: ImageView
    lateinit var btn_history:ImageView
    lateinit var home:ImageView

    //    LinearLayout mStudentSpinner;
    var studentName: TextView? = null
    var studImg: ImageView? = null
    var stud_id: String? = null
    var studClass = ""
    var orderId = ""
    var stud_img = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_trip)
        context = this
        initialiseUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(context)
        if (internetCheck)
        {
            getTripCategories()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

    }
    private fun initialiseUI() {
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")
        }
       progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        bannerImageView = findViewById<ImageView>(R.id.bannerImage)
        descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        sendEmailImageView = findViewById<ImageView>(R.id.sendEmailImageView)
        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader)
        headermanager = HeaderManagerNoColorSpace()
        headermanager!!.getHeader(relativeHeader, 6)
        back = headermanager.getLeftButton()
        btn_history = headermanager.getRightHistoryImage()
        btn_history!!.visibility = View.INVISIBLE
        categoryListRecyclerView = findViewById<RecyclerView>(R.id.categoryListRecycler)
        categoryListRecyclerView.setHasFixedSize(true)
        val spacing = 5 // 50px
        val itemDecoration = ItemOffsetDecoration(context, spacing)
        recyclerViewLayoutManager = GridLayoutManager(context, 2)
        //        categoryListRecyclerView.addItemDecoration(
//                new DividerItemDecoration(context.getResources().getDrawable(R.drawable.list_divider)));
        categoryListRecyclerView.addItemDecoration(itemDecoration)
        categoryListRecyclerView.setLayoutManager(recyclerViewLayoutManager)
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back)
        back!!.setOnClickListener {
            CommonFunctions.hideKeyBoard(context)
            finish()
        }
        home = headermanager.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(
                context,
                HomeActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
       studentName = findViewById<TextView>(R.id.studentName)
       studentName = findViewById<TextView>(R.id.studentName)
       studImg = findViewById<ImageView>(R.id.imagicon)
        sendEmailImageView.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
            val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
            text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
            text_content = dialog.findViewById<View>(R.id.text_content) as EditText

            dialogCancelButton.setOnClickListener { //   AppUtils.hideKeyBoard(mContext);
                val imm =
                    context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(text_dialog.getWindowToken(), 0)
                imm.hideSoftInputFromWindow(text_content.getWindowToken(), 0)
                dialog.dismiss()
            }
            submitButton.setOnClickListener {
                emailvalidationcheck( text_dialog.text.toString().trim(),
                    text_content.text.toString().trim(),
                   sharedprefs.getStudentID(context).toString(),
                    contactEmail,
                    dialog)

            }
            dialog.show()
        })
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
                context, context.getResources().getString(
                    com.mobatia.bisad.R.string.enter_subjects
                ), Toast.LENGTH_SHORT
            )
            toast.show()
        } else {
            if (message.equals("")) {
                val toast: Toast = Toast.makeText(
                    context, context.getResources().getString(
                        R.string.enter_contents
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            } else if (staffEmail.matches(EMAIL_PATTERN.toRegex())) {
                if (title.toString().trim().matches(pattern.toRegex())) {
                    if (title.toString().length>=500){
                        Toast.makeText(context, "Subject is too long", Toast.LENGTH_SHORT).show()

                    }else{
                        if (message.toString().trim().matches(pattern.toRegex())) {
                            if (message.length<=500){
                                if (InternetCheckClass.isInternetAvailable(context)) {
                                    sendEmailToStafftitle(title, message, contactEmail, dialog)
                                } else {
                                    CommonFunctions.faliurepopup(context)
                                }
                            }else{
                                Toast.makeText(context, "Message is too long", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            val toast: Toast = Toast.makeText(
                                context, context.getResources().getString(
                                    R.string.enter_valid_contents
                                ), Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }


                } else {
                    val toast: Toast = Toast.makeText(
                        context, context.getResources().getString(
                            R.string.enter_valid_subjects
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            } else {
                val toast: Toast = Toast.makeText(
                    context, context.getResources().getString(
                        R.string.enter_valid_mail
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }
    private fun getTripCategories() {
      progressDialogP.show()
        val call: Call<TripCategoriesResponseModel> =
            ApiClient.getClient.tripCategories("Bearer " + PreferenceData().getaccesstoken(context))
        call.enqueue(object : Callback<TripCategoriesResponseModel> {
            override fun onFailure(call: Call<TripCategoriesResponseModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<TripCategoriesResponseModel>, response: Response<TripCategoriesResponseModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    progressDialogP.dismiss()
                    if (response.body()!!.getResponseCode().equals("200")) {
                        if (response.body()!!.getResponseData()!!.statusCode.equals("303")) {
                            val bannerImageResponse: String =
                                response.body()!!.getResponseData()!!.bannerImage!!
                            if (bannerImageResponse != "") {
                                Glide.with(context!!).load(CommonFunctions.replace(bannerImageResponse))
                                    .centerCrop().placeholder(R.drawable.default_banner)
                                    .into(bannerImageView)
                            } else {
                                bannerImageView!!.setBackgroundResource(R.drawable.default_banner)
                            }
                            if (!response.body()!!.getResponseData()!!.bannerDescription
                                    .equals("")
                            ) {
                                descriptionTextView.setText(
                                    response.body()!!.getResponseData()!!.bannerDescription
                                )
                            } else descriptionTextView!!.visibility = View.GONE
                            if (!response.body()!!.getResponseData()!!.bannerContactEmail
                                    .equals("")
                            ) {
                                contactEmail = response.body()!!.getResponseData()!!.bannerContactEmail!!
                            } else sendEmailImageView!!.visibility = View.GONE
                            categoriesList = response.body()!!.getResponseData()!!.data
                            if (categoriesList!!.size > 0) {
                                // Log.e("Here","Here");
                                tripsCategoryAdapter = TripsCategoryAdapter(context,
                                    categoriesList!!
                                )
                                categoryListRecyclerView!!.adapter = tripsCategoryAdapter
                            } else {
                                // Log.e("Here","not");
                                Toast.makeText(
                                    this@TripCategoriesActivity,
                                    "No categories available.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(this@TripCategoriesActivity, "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@TripCategoriesActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })



    }


     fun sendEmailToStafftitle (title: String,
    message: String,
    staffEmail: String,
    dialog: Dialog) {
        val token = PreferenceData().getaccesstoken(context)
        val sendMailBody = CanteenSendEmailApiModel( staffEmail, title, message)
        val call: Call<SendStaffEmailModel> =
            ApiClient.getClient.sendEmailCanteen(sendMailBody, "Bearer " + token)
        call.enqueue(object : Callback<SendStaffEmailModel> {
            override fun onFailure(call: Call<SendStaffEmailModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<SendStaffEmailModel>, response: Response<SendStaffEmailModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {


                        if (response.body()!!.status==100) {
                            dialog.dismiss()
                            CommonFunctions.showSuccessAlert( context,
                                context.getString(R.string.mail_success_message),
                                context.getString(R.string.mail_alert_success) ,
                                dialog)

                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(context)
                                sendEmailToStafftitle(
                                    title,
                                    message,
                                    staffEmail,
                                    dialog
                                )
                            } else {
                                if (response.body()!!.status==103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, context)
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
}