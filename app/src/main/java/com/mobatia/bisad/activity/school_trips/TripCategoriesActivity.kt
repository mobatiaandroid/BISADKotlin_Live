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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripCategoriesActivity : AppCompatActivity() {
    var context: Context? = null
    var extras: Bundle? = null
    var tab_type: String? = null
    private val EMAIL_PATTERN =
        "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
    private val pattern = "^([a-zA-Z ]*)$"
    var text_content: TextView? = null
    lateinit var text_dialog: TextView
    private val progressDialogP: ProgressBarDialog? = null
    private val progressDialog: ProgressBar? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var bannerImageView: ImageView? = null
    var descriptionTextView: TextView? = null
    var sendEmailImageView: ImageView? = null
    var categoryListRecyclerView: RecyclerView? = null
    var recyclerViewLayoutManager: GridLayoutManager? = null

    var categoriesList: ArrayList<TripCategoriesResponseModel.Data>? = null
    var tripsCategoryAdapter: TripsCategoryAdapter? = null
    var contactEmail = ""
    var back: ImageView? = null
    var btn_history:android.widget.ImageView? = null
    var home:android.widget.ImageView? = null

    //    LinearLayout mStudentSpinner;
    var studentName: TextView? = null
    var studImg: ImageView? = null
    var stud_id: String? = null
    var studClass = ""
    var orderId = ""
    var stud_img = ""
    var studentsModelArrayList: ArrayList<StudentDataModel.DataItem> =
        ArrayList<StudentDataModel.DataItem>()
    var studentList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_trip)
        context = this
        initialiseUI()
        if (AppUtils.checkInternet(context)) {
            getTripCategories()
        } else {
            AppUtils.showDialogAlertDismiss(
                context as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }
    private fun initialiseUI() {
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")
        }
        TripCategoriesActivity.progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        bannerImageView = findViewById<ImageView>(R.id.bannerImage)
        descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        sendEmailImageView = findViewById<ImageView>(R.id.sendEmailImageView)
        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader)
        headermanager = HeaderManager(this@TripCategoriesActivity, tab_type)
        headermanager.getHeader(relativeHeader, 6)
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
            AppUtils.hideKeyBoard(context)
            finish()
        }
        home = headermanager.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(
                context,
                HomeListAppCompatActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        TripCategoriesActivity.studentName = findViewById<TextView>(R.id.studentName)
        TripCategoriesActivity.studentName = findViewById<TextView>(R.id.studentName)
        TripCategoriesActivity.studImg = findViewById<ImageView>(R.id.imagicon)
        sendEmailImageView.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
            val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
            text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
            text_content = dialog.findViewById<View>(R.id.text_content) as EditText
            TripCategoriesActivity.progressDialog =
                dialog.findViewById<View>(R.id.progressdialogg) as ProgressBar
            dialogCancelButton.setOnClickListener { //   AppUtils.hideKeyBoard(mContext);
                val imm =
                    context!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(text_dialog.getWindowToken(), 0)
                imm.hideSoftInputFromWindow(text_content.getWindowToken(), 0)
                dialog.dismiss()
            }
            submitButton.setOnClickListener {
                if (text_dialog.getText().toString().trim { it <= ' ' } == "") {
                    val toast = Toast.makeText(
                        context, context!!.resources.getString(
                            R.string.enter_subjects
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    if (text_content.getText().toString().trim { it <= ' ' } == "") {
                        val toast = Toast.makeText(
                            context, context!!.resources.getString(
                                R.string.enter_contents
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else if (contactEmail.matches(EMAIL_PATTERN.toRegex())) {
                        if (text_dialog.getText().toString().trim { it <= ' ' }
                                .matches(pattern.toRegex())) {
                            if (text_content.getText().toString().trim { it <= ' ' }
                                    .matches(pattern.toRegex())) {
                                if (AppUtils.isNetworkConnected(context)) {
                                    sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF, dialog)
                                } else {
                                    AppUtils.showDialogAlertDismiss(
                                        context as Activity?,
                                        "Network Error",
                                        getString(R.string.no_internet),
                                        R.drawable.nonetworkicon,
                                        R.drawable.roundred
                                    )
                                }
                            } else {
                                val toast = Toast.makeText(
                                    context, context!!.resources.getString(
                                        R.string.enter_valid_contents
                                    ), Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }
                        } else {
                            val toast = Toast.makeText(
                                context, context!!.resources.getString(
                                    R.string.enter_valid_subjects
                                ), Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    } else {
                        val toast = Toast.makeText(
                            context, context!!.resources.getString(
                                R.string.enter_valid_email
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }
            dialog.show()
        })
    }
    private fun getTripCategories() {
        TripCategoriesActivity.progressDialogP.show()
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripCategoriesResponseModel> =
            service.tripCategories("Bearer " + PreferenceManager.getAccessToken(context))
        call.enqueue(object : Callback<TripCategoriesResponseModel> {
            override fun onResponse(
                call: Call<TripCategoriesResponseModel>,
                response: Response<TripCategoriesResponseModel>
            ) {
                TripCategoriesActivity.progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponseData().getStatusCode().equalsIgnoreCase("303")) {
                        val bannerImageResponse: String =
                            response.body().getResponseData().getBannerImage()
                        if (bannerImageResponse != "") {
                            Glide.with(context!!).load(AppUtils.replace(bannerImageResponse))
                                .centerCrop().placeholder(R.drawable.default_banner)
                                .into(bannerImageView)
                        } else {
                            bannerImageView!!.setBackgroundResource(R.drawable.default_banner)
                        }
                        if (!response.body().getResponseData().getBannerDescription()
                                .equalsIgnoreCase("")
                        ) {
                            descriptionTextView.setText(
                                response.body().getResponseData().getBannerDescription()
                            )
                        } else descriptionTextView!!.visibility = View.GONE
                        if (!response.body().getResponseData().getBannerContactEmail()
                                .equalsIgnoreCase("")
                        ) {
                            contactEmail = response.body().getResponseData().getBannerContactEmail()
                        } else sendEmailImageView!!.visibility = View.GONE
                        categoriesList = response.body().getResponseData().getData()
                        if (categoriesList!!.size > 0) {
                            // Log.e("Here","Here");
                            tripsCategoryAdapter = TripsCategoryAdapter(context, categoriesList)
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

            override fun onFailure(call: Call<TripCategoriesResponseModel>, t: Throwable) {
                TripCategoriesActivity.progressDialogP.dismiss()
                AppUtils.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })
    }


    private fun sendEmailToStaff(URL: String, dialog: Dialog) {
        TripCategoriesActivity.progressDialog.setVisibility(View.VISIBLE)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val paramObject = JsonObject()
        paramObject.addProperty("email", contactEmail)
        paramObject.addProperty("title", text_dialog!!.text.toString())
        paramObject.addProperty("message", text_content!!.text.toString())
        val call: Call<SendMailStaffResponseModel> = service.sendmailtostaff(
            "Bearer " + PreferenceManager.getAccessToken(context),
            paramObject
        )
        call.enqueue(object : Callback<SendMailStaffResponseModel?> {
            override fun onResponse(
                call: Call<SendMailStaffResponseModel?>,
                response: Response<SendMailStaffResponseModel?>
            ) {
                if (response.isSuccessful()) {
                    TripCategoriesActivity.progressDialog.setVisibility(View.GONE)
                    val apiResponse: SendMailStaffResponseModel? = response.body()
                    val statuscode: String = apiResponse.getResponse().getStatuscode()
                    if (statuscode == "303") {
                        val toast = Toast.makeText(
                            context, context!!.resources.getString(
                                R.string.successfully_send_email_staff
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                        dialog.dismiss()
                    } else {
                        val toast = Toast.makeText(
                            context, context!!.resources.getString(
                                R.string.email_not_staff
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    TripCategoriesActivity.progressDialog.setVisibility(View.GONE)
                    AppUtils.showDialogAlertDismiss(
                        context as Activity?,
                        "Alert",
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<SendMailStaffResponseModel?>, t: Throwable) {
                TripCategoriesActivity.progressDialog.setVisibility(View.GONE)
                AppUtils.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })

    }
}