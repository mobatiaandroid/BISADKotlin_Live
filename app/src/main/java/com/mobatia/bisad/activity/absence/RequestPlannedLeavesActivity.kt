package com.mobatia.bisad.activity.absence

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.model.RequestAbsenceApiModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RequestPlannedLeavesActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    lateinit var studentNameTxt: TextView
    lateinit var enterStratDate: TextView
    lateinit var enterEndDate: TextView
    lateinit var submitBtn: Button
    lateinit var enterMessage: EditText
    lateinit var studImg: ImageView
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
     var fromDate: String=""
     var toDate: String =""
    lateinit var fromDateAPI:String
    lateinit var toDateAPI:String
    lateinit var reasonAPI:String
    lateinit var studentSpinner: LinearLayout
    lateinit var submitLayout: LinearLayout
    lateinit var studentArrayList :ArrayList<StudentList>
    lateinit var myCalendar :Calendar
    lateinit var currentDate:Date
    lateinit var sdate:Date
    lateinit var edate:Date
    var elapsedDays:Long = 0
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    lateinit var progressDialog: RelativeLayout
    lateinit var backRelative: RelativeLayout
    lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?)
    { super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planned_absence)
        mContext=this
        activity=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()

        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callStudentListApi()
        }
        else
        {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }
        initUI()

    }
    fun initUI()
    {
        myCalendar= Calendar.getInstance()
        currentDate=Calendar.getInstance().time
        studentNameTxt = findViewById<TextView>(R.id.studentName)
        enterStratDate = findViewById<TextView>(R.id.enterStratDate)
        enterEndDate = findViewById<TextView>(R.id.enterEndDate)
        studImg = findViewById<ImageView>(R.id.studImg)
        enterMessage = findViewById<EditText>(R.id.enterMessage)
        relativeHeader = findViewById(R.id.relativeHeader)
        studentSpinner = findViewById<LinearLayout>(R.id.studentSpinner)
        submitLayout = findViewById<LinearLayout>(R.id.submitLayout)
        submitBtn = findViewById<Button>(R.id.submitBtn)
        relativeHeader = findViewById(R.id.relativeHeader)
        backRelative = findViewById(R.id.backRelative)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressDialog = findViewById<RelativeLayout>(R.id.progressDialog)
        heading.text = mContext.resources.getString(R.string.planned_leaves)
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })

        studentSpinner.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showStudentList(mContext,studentArrayList)
            }
        })
        submitBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if(enterStratDate.text.equals(""))
                {
                    InternetCheckClass. showErrorAlert(mContext,"Please select First day of absence","Alert")
                }
                else{
                    if (enterMessage.text.toString().trim().equals("")){
                        InternetCheckClass. showErrorAlert(mContext,"Please enter reason for your absence","Alert")
                    }
                    else if (!CommonFunctions.validateEditText(enterMessage))
                    {
                        InternetCheckClass.showErrorAlert(
                            mContext,
                            "Please remove html content.",
                            "Alert"
                        )

                    }
                    else{
                        val aniRotate: Animation =
                            AnimationUtils.loadAnimation(com.mobatia.bisad.fragment.home.mContext, R.anim.linear_interpolator)
                        progressDialog.startAnimation(aniRotate)
                        progressDialog.visibility=View.VISIBLE
                        reasonAPI=enterMessage.text.toString().trim()

                        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                        if (internetCheck)
                        {
                            val inputFormat2: DateFormat = SimpleDateFormat("d-m-yyyy")
                            val outputFormat2: DateFormat = SimpleDateFormat("d-m-yyyy")
                            val inputDateStr2 = fromDate
                            val date2: Date = inputFormat2.parse(inputDateStr2)
                            val f_date: String = outputFormat2.format(date2)

                            val inputFormat3: DateFormat = SimpleDateFormat("d-m-yyyy")
                            val outputFormat3: DateFormat = SimpleDateFormat("d-m-yyyy")
                            val inputDateStr3 = toDate
                            val date3: Date = inputFormat3.parse(inputDateStr3)
                            val t_date: String = outputFormat3.format(date3)

                            callAbsenceSubmitApi(f_date,t_date,reasonAPI)
                        }
                        else{
                            InternetCheckClass.showSuccessInternetAlert(mContext)
                        }
                    }
                }
            }
        })
        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            //fromDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            if (toDate != "") {
                val dateFormatt =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                try {
                    sdate = dateFormatt.parse(fromDate)
                    edate = dateFormatt.parse(toDate)
                    printDifference(sdate, edate)
                } catch (e: Exception) {
                }
            }
            if(elapsedDays < 0 && toDate != "")
            {
                InternetCheckClass. showErrorAlert(mContext,"Choose first day of absence(date) less than or equal to selected return to school(date)","Alert")

            }
            else
            {
                updateLabel()
            }

        }
        val dateEnd = OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            toDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            if (toDate != "") {
                val dateFormatt =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                try {
                    sdate = dateFormatt.parse(fromDate)
                    edate = dateFormatt.parse(toDate)
                    printDifference(sdate, edate)
                } catch (e: java.lang.Exception) {
                }
                if (elapsedDays < 0) {
                    InternetCheckClass. showErrorAlert(mContext,"Choose return to school(date) greater than or equal to first day of absence(date)","Alert")
                } else {
                    updateLabelEnd()
                }
            }
        }
        enterStratDate.setOnClickListener {
                cal()
                /*DatePickerDialog(
                    mContext, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()*/
            }

        enterEndDate.setOnClickListener{
               calToDate()
               /* DatePickerDialog(
                    mContext, dateEnd, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()*/

            }


    }
   fun printDifference(startDate :Date,endDate:Date)
   {
       var different = endDate.time - startDate.time
       val secondsInMilli: Long = 1000
       val minutesInMilli = secondsInMilli * 60
       val hoursInMilli = minutesInMilli * 60
       val daysInMilli = hoursInMilli * 24
       elapsedDays = different / daysInMilli
       different = different % daysInMilli
       val elapsedHours = different / hoursInMilli
       different = different % hoursInMilli
       val elapsedMinutes = different / minutesInMilli
       different = different % minutesInMilli
       val elapsedSeconds = different / secondsInMilli


   }
    fun updateLabel()
    {
        val myFormat = "dd MMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        enterStratDate.text = sdf.format(myCalendar.time)
    }
    fun updateLabelEnd()
    {
        val myFormat = "dd MMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        enterEndDate.text = sdf.format(myCalendar.time)
    }
    fun callStudentListApi()
    {
        studentArrayList=ArrayList<StudentList>()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient(mContext).getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                val arraySize :Int = response.body()!!.responseArray.studentList.size
                if (response.body()!!.status==100)
                {
                    studentArrayList.addAll(response.body()!!.responseArray.studentList)

                    if (sharedprefs.getStudentID(mContext).equals(""))
                    {
                        studentName=studentArrayList.get(0).name
                        studentImg=studentArrayList.get(0).photo
                        studentId=studentArrayList.get(0).id
                        studentClass=studentArrayList.get(0).section
                        sharedprefs.setStudentID(mContext,studentId)
                        sharedprefs.setStudentName(mContext,studentName)
                        sharedprefs.setStudentPhoto(mContext,studentImg)
                        sharedprefs.setStudentClass(mContext,studentClass)
                        studentNameTxt.text=studentName
                        if(!studentImg.equals(""))
                        {
                            Glide.with(mContext) //1
                                .load(studentImg)
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
                        studentName= sharedprefs.getStudentName(mContext)!!
                        studentImg= sharedprefs.getStudentPhoto(mContext)!!
                        studentId= sharedprefs.getStudentID(mContext)!!
                        studentClass= sharedprefs.getStudentClass(mContext)!!
                        studentNameTxt.text=studentName
                        if(!studentImg.equals(""))
                        {
                            Glide.with(mContext) //1
                                .load(studentImg)
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
                }
            }
        })
    }


    fun showStudentList(context: Context ,mStudentList : ArrayList<StudentList>)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_student_list)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var btn_dismiss = dialog.findViewById(R.id.btn_dismiss) as Button
        var studentListRecycler = dialog.findViewById(R.id.studentListRecycler) as RecyclerView
        iconImageView.setImageResource(R.drawable.boy)
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            btn_dismiss.setBackgroundDrawable(
                mContext.resources.getDrawable(R.drawable.button_new)
            )
        } else {
            btn_dismiss.background = mContext.resources.getDrawable(R.drawable.button_new)
        }

        studentListRecycler.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.layoutManager = llm
        if(mStudentList.size>0)
        {
            val studentAdapter = StudentListAdapter(mContext,mStudentList)
            studentListRecycler.adapter = studentAdapter
        }
        btn_dismiss.setOnClickListener()
        {
            dialog.dismiss()
        }
        studentListRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                studentName=studentArrayList.get(position).name
                studentImg=studentArrayList.get(position).photo
                studentId=studentArrayList.get(position).id
                studentClass=studentArrayList.get(position).section
                sharedprefs.setStudentID(mContext,studentId)
                sharedprefs.setStudentName(mContext,studentName)
                sharedprefs.setStudentPhoto(mContext,studentImg)
                sharedprefs.setStudentClass(mContext,studentClass)
                studentNameTxt.text=studentName
                if(!studentImg.equals(""))
                {
                    Glide.with(mContext) //1
                        .load(studentImg)
                        .placeholder(R.drawable.student)
                        .error(R.drawable.student)
                        .skipMemoryCache(true) //2
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                        .transform(CircleCrop()) //4
                        .into(studImg)
                }
                else
                {
                    studImg.setImageResource(R.drawable.student)
                }

                dialog.dismiss()
            }
        })
        dialog.show()
    }

    //Signup API Call
    fun callAbsenceSubmitApi(from:String,toDate:String,reason:String)
    {
        progressDialog.visibility = View.VISIBLE
        var devicename:String= (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT]
            .name)
        val token = sharedprefs.getaccesstoken(mContext)
        val requestLeaveBody= RequestAbsenceApiModel(sharedprefs.getStudentID(mContext)!!,from,toDate,reason,"2",devicename,"1.0")
        val call: Call<ResponseBody> = ApiClient(mContext).getClient.plannedLeaveRequest(requestLeaveBody,"Bearer "+token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)
                progressDialog.visibility = View.GONE
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if(jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            if (status == 100) {
                                showErrorAlert(mContext,mContext.resources.getString(R.string.success_planned_leave),"Success")

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callAbsenceSubmitApi(from,toDate,reason)
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
                                    }
                                }

                            }
                        }
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    fun showErrorAlert(context: Context,message : String,msgHead : String)
    {
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
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }
    private fun cal(){
        val mcurrentTime = android.icu.util.Calendar.getInstance()
        val year = mcurrentTime.get(android.icu.util.Calendar.YEAR)
        val month = mcurrentTime.get(android.icu.util.Calendar.MONTH)
        val day = mcurrentTime.get(android.icu.util.Calendar.DAY_OF_MONTH)
        val minDate = android.icu.util.Calendar.getInstance()
        minDate.set(android.icu.util.Calendar.HOUR_OF_DAY, 0)
        minDate.set(android.icu.util.Calendar.MINUTE, 0)
        minDate.set(android.icu.util.Calendar.SECOND, 0)
        val dpd1 = DatePickerDialog(this, R.style.MyDatePickerStyle,
            object : DatePickerDialog.OnDateSetListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                var firstday: String? =String.format("%d/%d/%d", month + 1,dayOfMonth , year)
                var date_sel: String? = String.format("%d-%d-%d", dayOfMonth, month + 1, year)
                var date_temp = date_sel.toString()

                val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
                val inputDateStr = date_temp
                val date: Date = inputFormat.parse(inputDateStr)
                val outputDateStr: String = outputFormat.format(date)
                fromDate=date_sel.toString()
                enterStratDate.text = outputDateStr

            }
        }, year, month, day)

            dpd1.datePicker.minDate = android.icu.util.Calendar.getInstance().timeInMillis
            dpd1.show()

    }
    private fun calToDate(){
        val mcurrentTime = android.icu.util.Calendar.getInstance()
        val year = mcurrentTime.get(android.icu.util.Calendar.YEAR)
        val month = mcurrentTime.get(android.icu.util.Calendar.MONTH)
        val day = mcurrentTime.get(android.icu.util.Calendar.DAY_OF_MONTH)
        val minDate = android.icu.util.Calendar.getInstance()
        minDate.set(android.icu.util.Calendar.HOUR_OF_DAY, 0)
        minDate.set(android.icu.util.Calendar.MINUTE, 0)
        minDate.set(android.icu.util.Calendar.SECOND, 0)
        val dpd1 = DatePickerDialog(this, R.style.MyDatePickerStyle, object : DatePickerDialog.OnDateSetListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                var firstday: String? =String.format("%d/%d/%d", month + 1,dayOfMonth , year)
                var date_sel: String? = String.format("%d-%d-%d", dayOfMonth, month + 1, year)
                var date_temp = date_sel.toString()

                val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
                val inputDateStr = date_temp
                val date: Date = inputFormat.parse(inputDateStr)
                val outputDateStr: String = outputFormat.format(date)
                toDate=date_sel.toString()
                enterEndDate.text = outputDateStr

            }
        }, year, month, day)

            dpd1.datePicker.minDate = android.icu.util.Calendar.getInstance().timeInMillis
            dpd1.show()

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

