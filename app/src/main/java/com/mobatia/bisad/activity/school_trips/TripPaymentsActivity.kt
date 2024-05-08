package com.mobatia.bisad.activity.school_trips

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripPaymentsActivity : AppCompatActivity() {

    var mContext: Context = this
    private val progressDialogP: ProgressBarDialog? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var extras: Bundle? = null
    var tab_type: String? = null
    var email: String? = null
    var relativeHeader: RelativeLayout? = null
    var studentsModelArrayList: ArrayList<StudentDataModel.DataItem>? = null
    var studentName: TextView? = null
    var textViewYear: TextView? = null
    var stud_id = ""
    var stud_class = ""
    var stud_name = ""
    var stud_img = ""
    var section = ""
    var mStudentSpinner: LinearLayout? = null
    var studImg: ImageView? = null
    var mNewsLetterListView: RecyclerView? = null
    var tripHistoryList: ArrayList<TripHistoryResponseModel.Trip> =
        ArrayList<TripHistoryResponseModel.Trip>()
    var studentDataList: ArrayList<StudentDataModel.DataItem> =
        ArrayList<StudentDataModel.DataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_payments)
        initUI()
        if (AppUtils.isNetworkConnected(mContext)) {
            getStudentsListAPI()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }
    private fun getStudentsListAPI() {
        TripPaymentsActivity.progressDialogP.show()
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<StudentDataModel> =
            service.postStudentList("Bearer " + PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<StudentDataModel?> {
            override fun onResponse(
                call: Call<StudentDataModel?>,
                response: Response<StudentDataModel?>
            ) {
                if (response.body() != null) {
                    val responseCode: String = response.body().getResponsecode()
                    val statusCode: String = response.body().getResponse().getStatuscode()
                    // studentDataList = new ArrayList<>();
                    if (responseCode.equals("200", ignoreCase = true)) {
                        studentsModelArrayList = java.util.ArrayList<StudentDataModel.DataItem>()
                        if (statusCode.equals("303", ignoreCase = true)) {
                            TripPaymentsActivity.progressDialogP.hide()
                            if (response.body().getResponse().getData().size() > 0) {
                                for (dataItem in response.body().getResponse().getData()) {
                                    val item: StudentDataModel.DataItem = DataItem()
                                    item.setId(dataItem.getId())
                                    item.setName(dataItem.getName())
                                    item.setClassName(dataItem.getClassName())
                                    item.setPhoto(dataItem.getPhoto())
                                    item.setSection(dataItem.getSection())
                                    //studentDataList.add(item);
                                    studentsModelArrayList!!.add(item)
                                }
                                if (PreferenceManager.getStudIdForCCA(mContext)
                                        .equalsIgnoreCase("")
                                ) {
                                    studentName.setText(studentsModelArrayList!![0].getName())
                                    stud_name = studentsModelArrayList!![0].getName()
                                    stud_class = studentsModelArrayList!![0].getClassName()
                                    stud_img = studentsModelArrayList!![0].getPhoto()
                                    section = studentsModelArrayList!![0].getSection()
                                    if (stud_img != "") {
                                        Picasso.with(mContext).load(AppUtils.replace(stud_img))
                                            .placeholder(R.drawable.boy).fit().into(studImg)
                                    } else {
                                        studImg!!.setImageResource(R.drawable.boy)
                                    }
                                    textViewYear!!.text =
                                        "Class : " + studentsModelArrayList!![0].getClassName()
                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0")
                                    val studentSelectPosition: Int = Integer.valueOf(
                                        PreferenceManager.getCCAStudentIdPosition(mContext)
                                    )
                                    TripPaymentsActivity.stud_id =
                                        studentsModelArrayList!![studentSelectPosition].getId()
                                    PreferenceManager.setStudIdForCCA(
                                        mContext,
                                        TripPaymentsActivity.stud_id
                                    )
                                } else {
                                    val studentSelectPosition: Int = Integer.valueOf(
                                        PreferenceManager.getCCAStudentIdPosition(mContext)
                                    )
                                    studentName.setText(studentsModelArrayList!![studentSelectPosition].getName())
                                    TripPaymentsActivity.stud_id =
                                        studentsModelArrayList!![studentSelectPosition].getId()
                                    stud_name =
                                        studentsModelArrayList!![studentSelectPosition].getName()
                                    stud_class =
                                        studentsModelArrayList!![studentSelectPosition].getClassName()
                                    stud_img =
                                        studentsModelArrayList!![studentSelectPosition].getPhoto()
                                    PreferenceManager.setStudIdForCCA(
                                        mContext,
                                        TripPaymentsActivity.stud_id
                                    )
                                    System.out.println("selected student image" + studentsModelArrayList!![studentSelectPosition].getPhoto())
                                    if (stud_img != "") {
                                        Picasso.with(mContext).load(AppUtils.replace(stud_img))
                                            .placeholder(R.drawable.boy).fit().into(studImg)
                                    } else {
                                        studImg!!.setImageResource(R.drawable.boy)
                                    }
                                    textViewYear!!.text =
                                        "Class : " + studentsModelArrayList!![studentSelectPosition].getClassName()
                                }
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    getEventsListApi(TripPaymentsActivity.stud_id)
                                    //                                getReportListAPI(URL_GET_SPORTS_TEAMS_LIST);
                                } else {
                                    AppUtils.showDialogAlertDismiss(
                                        mContext as Activity,
                                        "Network Error",
                                        getString(R.string.no_internet),
                                        R.drawable.nonetworkicon,
                                        R.drawable.roundred
                                    )
                                }
                            } else {
                                AppUtils.showDialogAlertDismiss(
                                    mContext as Activity,
                                    "Alert",
                                    getString(R.string.nodatafound),
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            }
                        } else {
                            TripPaymentsActivity.progressDialogP.hide()
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity,
                                "Alert",
                                getString(R.string.common_error),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }
                    } else {
                        TripPaymentsActivity.progressDialogP.hide()
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    TripPaymentsActivity.progressDialogP.hide()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity,
                        "Alert",
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<StudentDataModel?>, t: Throwable) {
                TripPaymentsActivity.progressDialogP.hide()
            }
        })
    }

    private fun getEventsListApi(stud_id: String) {
        TripPaymentsActivity.progressDialogP.show()
        val paramObject = JsonObject()
        paramObject.addProperty("student_id", stud_id)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripHistoryResponseModel> = service.tripHistory(
            "Bearer " + PreferenceManager.getAccessToken(mContext), paramObject
        )
        call.enqueue(object : Callback<TripHistoryResponseModel> {
            override fun onResponse(
                call: Call<TripHistoryResponseModel>,
                response: Response<TripHistoryResponseModel>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: TripHistoryResponseModel? = response.body()
                    val response_code: String = apiResponse.getResponseCode()
                    if (response_code == "200") {
                        val statuscode: String = apiResponse.getResponseDetail().getStatusCode()
                        if (statuscode == "303") {
                            TripPaymentsActivity.progressDialogP.hide()
                            if (response.body().getResponseDetail().getTrips().size() > 0) {

//                                newsLetterModelArrayList=new ArrayList<>();
//                                for (int i = 0; i < apiResponse.getResponseDetail().getTrips().size(); i++) {
//                                    TripHistoryResponseModel.Trip item = apiResponse.getResponseDetail().getTrips().get(i);
//                                    Gson gson = new Gson();
//                                    String eventJson = gson.toJson(item);
//                                    try {
//                                        JSONObject dataObject = new JSONObject(eventJson);
//
//                                        newsLetterModelArrayList.add(addEventsDetails(dataObject));
//                                        newsLetterModelArrayList.clear();
//                                        //  Log.e("array", String.valueOf(mCCAmodelArrayList));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
                                tripHistoryList = response.body().getResponseDetail().getTrips()
                                mNewsLetterListView!!.visibility = View.VISIBLE
                                val newsLetterAdapter =
                                    TripHistoryListAdapter(mContext, tripHistoryList)
                                mNewsLetterListView!!.adapter = newsLetterAdapter
                            } else {
                                mNewsLetterListView!!.visibility = View.GONE
                                AppUtils.showDialogAlertDismiss(
                                    mContext as Activity,
                                    "Alert",
                                    "No data available",
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            }
                        }
                    } else {
                        TripPaymentsActivity.progressDialogP.hide()
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    TripPaymentsActivity.progressDialogP.hide()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity,
                        "Alert",
                        mContext.getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<TripHistoryResponseModel>, t: Throwable) {
                TripPaymentsActivity.progressDialogP.hide()
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    "Alert",
                    mContext.getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })
    }

    fun initUI() {
        TripPaymentsActivity.progressDialogP = ProgressBarDialog(mContext, R.drawable.spinner)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        headermanager = HeaderManager(this@TripPaymentsActivity, "Trip Payments")
        headermanager.getHeader(relativeHeader, 0)
        back = headermanager.getLeftButton()
        headermanager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            AppUtils.hideKeyBoard(mContext)
            finish()
        }
        home = headermanager.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(
                mContext,
                HomeListAppCompatActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        PreferenceManager.setStudIdForCCA(mContext, "")
        PreferenceManager.setCCAStudentIdPosition(mContext, "0")
        mStudentSpinner = findViewById<LinearLayout>(R.id.studentSpinner)
        studentName = findViewById<TextView>(R.id.studentName)
        studImg = findViewById<ImageView>(R.id.imagicon)
        textViewYear = findViewById<TextView>(R.id.textViewYear)
        mNewsLetterListView = findViewById<View>(R.id.mListView) as RecyclerView
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        mNewsLetterListView!!.layoutManager = llm
        mNewsLetterListView!!.setHasFixedSize(true)
        mNewsLetterListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider_teal)))
        mStudentSpinner.setOnClickListener(View.OnClickListener {
            showSocialmediaList(
                studentsModelArrayList
            )
        })

    }

    fun showSocialmediaList(mStudentArray: java.util.ArrayList<StudentDataModel.DataItem>) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_student_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<Button>(R.id.btn_dismiss)
        val iconImageView = dialog.findViewById<ImageView>(R.id.iconImageView)
        iconImageView.setImageResource(R.drawable.boy)
        val socialMediaList = dialog.findViewById<RecyclerView>(R.id.recycler_view_social_media)
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.button))
        } else {
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.button)
        }
        socialMediaList.addItemDecoration(DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider_teal)))
        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm
        val studentAdapter = StudentSpinnerAdapter(mContext, mStudentArray)
        socialMediaList.adapter = studentAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
        socialMediaList.addOnItemTouchListener(
            RecyclerItemListener(mContext, socialMediaList,
                object : RecyclerTouchListener() {
                    fun onClickItem(v: View?, position: Int) {
                        dialog.dismiss()
                        studentName.setText(mStudentArray[position].getName())
                        TripPaymentsActivity.stud_id = mStudentArray[position].getId()
                        PreferenceManager.setStudIdForCCA(mContext, TripPaymentsActivity.stud_id)
                        stud_name = mStudentArray[position].getName()
                        stud_class = mStudentArray[position].getClassName()
                        stud_img = mStudentArray[position].getPhoto()
                        section = mStudentArray[position].getSection()
                        textViewYear!!.text = "Class : " + mStudentArray[position].getClassName()
                        if (stud_img != "") {
                            Picasso.with(mContext).load(AppUtils.replace(stud_img))
                                .placeholder(R.drawable.boy).fit().into(studImg)
                        } else {
                            studImg!!.setImageResource(R.drawable.boy)
                        }
                        if (AppUtils.isNetworkConnected(mContext)) {
                            getEventsListApi(TripPaymentsActivity.stud_id)
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity,
                                "Network Error",
                                getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }
                    }

                    fun onLongClickItem(v: View?, position: Int) {
                        println("On Long Click Item interface")
                    }
                })
        )
        dialog.show()
    }
}