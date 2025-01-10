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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.school_trips.adapter.TripHistoryListAdapter
import com.mobatia.bisad.activity.school_trips.model.TripHistoryResponseModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.ProgressBarDialog
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.manager.HeaderManager
import com.mobatia.bisad.manager.HeaderManagerNoColorSpace
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.DividerItemDecoration
import com.mobatia.bisad.recyclermanager.RecyclerItemListener
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripPaymentsActivity : AppCompatActivity() {

    lateinit var mContext: Context
    lateinit var  progressDialogP: ProgressBarDialog
    lateinit var headermanager: HeaderManager
    lateinit var back: ImageView
    lateinit var home: ImageView
    var extras: Bundle? = null
    var tab_type: String? = null
    var email: String? = null
    lateinit var relativeHeader: RelativeLayout
    lateinit var studentsModelArrayList: ArrayList<StudentList>
    lateinit var studentName: TextView
    lateinit var textViewYear: TextView
    var stud_id = ""
    var stud_class = ""
    var stud_name = ""
    var stud_img = ""
    var section = ""
    lateinit var mStudentSpinner: LinearLayout
    lateinit var studImg: ImageView
    lateinit var mNewsLetterListView: RecyclerView
    var tripHistoryList: ArrayList<TripHistoryResponseModel.Trip> =
        ArrayList<TripHistoryResponseModel.Trip>()
    lateinit var activity: Activity



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_payments)
        mContext=this
        activity=this
        initUI()

        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            getStudentsListAPI()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }

    }
    private fun getStudentsListAPI() {


        progressDialogP.show()
        studentsModelArrayList=ArrayList<StudentList>()
        val token = PreferenceData().getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient(mContext).getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                val arraySize :Int = response.body()!!.responseArray.studentList.size
                if (response.body()!!.status==100)
                {
                    studentsModelArrayList.addAll(response.body()!!.responseArray.studentList)

                    if (PreferenceData().getStudentID(mContext).equals(""))
                    {
                        stud_name=studentsModelArrayList.get(0).name
                        stud_img=studentsModelArrayList.get(0).photo
                        stud_id=studentsModelArrayList.get(0).id
                        stud_class=studentsModelArrayList.get(0).section
                        PreferenceData().setStudentID(mContext,stud_id)
                        PreferenceData().setStudentName(mContext,stud_name)
                        PreferenceData().setStudentPhoto(mContext,stud_img)
                        PreferenceData().setStudentClass(mContext,stud_class)
                        studentName.text=stud_name
                        if(!stud_img.equals(""))
                        {
                            Glide.with(mContext) //1
                                .load(studImg)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        }
                        else{
                            studImg.setImageResource(R.drawable.student)
                        }

                    }
                    else{
                        stud_name= PreferenceData().getStudentName(mContext)!!
                        stud_img= PreferenceData().getStudentPhoto(mContext)!!
                        stud_id= PreferenceData().getStudentID(mContext)!!
                        stud_class= PreferenceData().getStudentClass(mContext)!!
                        studentName.text=stud_name
                        if(!stud_img.equals(""))
                        {
                            Glide.with(mContext) //1
                                .load(stud_img)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        }
                        else{
                            studImg.setImageResource(R.drawable.student)
                        }


                    }
                    getEventsListApi(stud_id)
                }
            }
        })

        /* TripPaymentsActivity.progressDialogP.show()
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
         })*/
    }

    private fun getEventsListApi(stud_id: String) {
       progressDialogP.show()
        val paramObject = JsonObject()
        paramObject.addProperty("student_id", stud_id)

        val call: Call<TripHistoryResponseModel> =
            ApiClient(mContext).getClient.tripHistory("Bearer " + PreferenceData().getaccesstoken(mContext),paramObject)
        call.enqueue(object : Callback<TripHistoryResponseModel> {
            override fun onFailure(call: Call<TripHistoryResponseModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)
                progressDialogP.hide()
                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<TripHistoryResponseModel>, response: Response<TripHistoryResponseModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    if (response.body()!!.getResponseCode()==100) {
                        progressDialogP.hide()
                        if (response.body()!!.getResponseDetail()!!.trips!!.size > 0) {
                            tripHistoryList = response.body()!!.getResponseDetail()!!.trips!!
                            mNewsLetterListView!!.visibility = View.VISIBLE
                            val newsLetterAdapter =
                                TripHistoryListAdapter(mContext, tripHistoryList)
                            mNewsLetterListView!!.adapter = newsLetterAdapter
                        } else {
                            mNewsLetterListView!!.visibility = View.GONE
                            CommonFunctions.showDialogAlertDismiss(
                                mContext as Activity,
                                "Alert",
                                "No data available",
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }

                    } else {
                        progressDialogP.hide()
                        CommonFunctions.showDialogAlertDismiss(
                            mContext as Activity,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }

        })

/*
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
        })*/
    }

    fun initUI() {
        progressDialogP = ProgressBarDialog(mContext)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        headermanager = HeaderManager(this@TripPaymentsActivity, "Trip Payments")
        headermanager.getHeader(relativeHeader, 0)
        back = headermanager.getLeftButton()
        headermanager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            CommonFunctions.hideKeyBoard(mContext)
            finish()
        }
        home = headermanager.getLogoButton()!!
        home!!.setOnClickListener {
            val `in` = Intent(
                mContext,
                HomeActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }

        PreferenceData().setStudentID(mContext, "")
       // PreferenceData().se(mContext, "0")
        mStudentSpinner = findViewById<LinearLayout>(R.id.studentSpinner)
        studentName = findViewById<TextView>(R.id.studentName)
        studImg = findViewById<ImageView>(R.id.imagicon)
        textViewYear = findViewById<TextView>(R.id.textViewYear)
        mNewsLetterListView = findViewById<View>(R.id.mListView) as RecyclerView
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        mNewsLetterListView!!.layoutManager = llm
        mNewsLetterListView!!.setHasFixedSize(true)
        mNewsLetterListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider)))
        mStudentSpinner.setOnClickListener(View.OnClickListener {
            showSocialmediaList(studentsModelArrayList)
        })

    }

    fun showSocialmediaList(mStudentArray: java.util.ArrayList<StudentList>) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialogue_student_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<Button>(R.id.btn_dismiss)
        val iconImageView = dialog.findViewById<ImageView>(R.id.iconImageView)
        iconImageView.setImageResource(R.drawable.boy)
        val socialMediaList = dialog.findViewById<RecyclerView>(R.id.studentListRecycler)
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.button_new))
        } else {
            dialogDismiss.background = mContext.resources.getDrawable(R.drawable.button_new)
        }
        socialMediaList.addItemDecoration(DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider)))
        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm
        val studentAdapter = StudentListAdapter(mContext, mStudentArray)
        socialMediaList.adapter = studentAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
        socialMediaList.addOnItemTouchListener(
            RecyclerItemListener(mContext, socialMediaList,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        dialog.dismiss()
                        studentName.setText(mStudentArray[position].name)
                       stud_id = mStudentArray[position].id
                        PreferenceData().setStudentID(mContext, stud_id)
                        stud_name = mStudentArray[position].name
                        stud_class = mStudentArray[position].studentClass
                        stud_img = mStudentArray[position].photo
                        section = mStudentArray[position].section
                        textViewYear!!.text = "Class : " + mStudentArray[position].studentClass
                        if (stud_img != "") {
                            Glide.with(mContext).load(CommonFunctions.replace(stud_img))
                                .placeholder(R.drawable.boy).into(studImg)
                        } else {
                            studImg!!.setImageResource(R.drawable.boy)
                        }

                        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                        if (internetCheck)
                        {
                            getEventsListApi(stud_id)
                        }
                        else{
                            InternetCheckClass.showSuccessInternetAlert(mContext)
                        }

                    }

                    override fun onLongClickItem(v: View?, position: Int) {
                    }
                })
        )
        dialog.show()
    }
    override fun onResume() {
        super.onResume()
        if (!CommonFunctions.runMethod.equals("Dev")) {
            if (CommonFunctions.isDeveloperModeEnabled(mContext)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
        }
    }
}