package com.mobatia.bisad.activity.absence

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.model.EarlyPickupModel
import com.mobatia.bisad.activity.absence.model.RequestPickupApiModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.InternetCheckClass.Companion.showErrorAlert
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

class RequestearlypickupActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    lateinit var studentNameTxt: TextView
    lateinit var enterStratDate: TextView
    lateinit var enterTime: TextView
    lateinit var pickupName:EditText
    lateinit var submitBtn: Button
    lateinit var enterMessage: EditText
    lateinit var studImg: ImageView
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    var fromDate: String=""
    var toDate: String =""
    var totime: String =""
    lateinit var fromDateAPI:String
    lateinit var toDateAPI:String
    lateinit var reasonAPI:String
    lateinit var studentSpinner: LinearLayout
    lateinit var submitLayout: LinearLayout
    lateinit var studentArrayList :ArrayList<StudentList>
    lateinit var myCalendar : Calendar
    lateinit var currentDate: Date
    lateinit var sdate: Date
    lateinit var edate: Date
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
        setContentView(R.layout.activity_request_earlypickup)
        mContext=this
        activity=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()

        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callStudentListApi()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }
        initUI()

    }
    fun initUI()
    {
        myCalendar= Calendar.getInstance()
        currentDate= Calendar.getInstance().time
        studentNameTxt = findViewById<TextView>(R.id.studentName)
        enterStratDate = findViewById<TextView>(R.id.enterStratDate)
        enterTime = findViewById<TextView>(R.id.enterEndDate)
        pickupName=findViewById(R.id.enterPickupname)
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
        heading.text = "Register new Early Pickup"
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
                    showerror(mContext,"Please select Date of Early Pickup","Alert")
                }
                else{
                    if (enterTime.text.equals("")){

                        showerror(mContext,"Please select your Pickup Time","Alert")
                    }else{
                        if (pickupName.text.isEmpty()){

                            showerror(mContext,"Please enter pickup person name","Alert")
                        }
                        else if (!CommonFunctions.validateEditText(pickupName))
                        {
                            showErrorAlert(mContext,"Please remove html content.","Alert")

                        }else{

                            if (enterMessage.text.isEmpty()){

                                showerror(mContext,"Please enter reason for early pickup","Alert")
                            }
                            else if (!CommonFunctions.validateEditText(enterMessage))
                            {
                                showErrorAlert(mContext,"Please remove html content.","Alert")

                            }else{
                                var date_entered=enterStratDate.text
                                var date=toDate
                                var time_entered=enterTime.text
                                var time=totime
                                var pickupname_entered=pickupName.text
                                var reason_entered=enterMessage.text

                                callPickupSubmitApi(date,time.toString(),pickupname_entered.toString(),
                                    reason_entered.toString()
                                )
                            }
                        }
                    }


                }


            }
        })
        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                fromDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
                if (toDate != "") {
                    val dateFormatt =
                        SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH)
                    try {
                        sdate = dateFormatt.parse(fromDate)
                        edate = dateFormatt.parse(toDate)
                        printDifference(sdate, edate)
                    } catch (e: Exception) {
                    }
                }
                if (elapsedDays < 0 && toDate != "") {
                    showerror(
                        mContext,
                        "Choose first day of absence(date) less than or equal to selected return to school(date)",
                        "Alert"
                    )

                } else {
                    updateLabel()
                }

            }
        val dateEnd =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
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
                        showerror(
                            mContext,
                            "Choose return to school(date) greater than or equal to first day of absence(date)",
                            "Alert"
                        )
                    } else {
                        updateLabelEnd()
                    }
                }
            }
        enterStratDate.setOnClickListener{
            cal()
        }/*(object : View.OnClickListener {
            override fun onClick(v: View?) {
                cal()
                *//*DatePickerDialog(
                    mContext, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()*//*
            }
        })*/
        enterTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
if (enterStratDate.text.equals("")){

    showerror(mContext,"Please select Date of Early Pickup","Alert")
}
                else{
                val mTimePicker: TimePickerDialog
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mcurrentTime.get(Calendar.MINUTE)
                //var am_pm = mcurrentTime.get(Calendar.AM_PM)
                var am_pm:String=""

                mTimePicker = TimePickerDialog(mContext, object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        var AM_PM: String
                        var hour=hourOfDay
                        var min=minute.toString()
                        if (minute<10){
                            min="0"+min
                        }

                        if(hour ==0) {
                            hour = 12
                            AM_PM="AM"
                        } else if(hour<12){
                            hour = hourOfDay
                            AM_PM = "AM"
                        }
                        else if (hour >12) {
                            hour -= 12
                            AM_PM = "PM"
                        } else if (hour == 12) {
                            hour = 12
                            AM_PM = "PM"
                        } else
                            AM_PM = "AM"
                        enterTime.text = hour.toString() + ":" + min + ":" + "00"+ AM_PM
                        totime=hour.toString() + ":" + min + ":" + "00"
                    }
                }, hour, minute,false)

                enterTime.setOnClickListener({ v ->
                    mTimePicker.show()
                })

            }
            }
        })

    }
    fun printDifference(startDate : Date, endDate: Date)
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
        enterTime.text = sdf.format(myCalendar.time)
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


    fun showStudentList(context: Context, mStudentList : ArrayList<StudentList>)
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
                else{
                    studImg.setImageResource(R.drawable.student)
                }
                dialog.dismiss()
            }
        })
        dialog.show()
    }
private fun cal() {
    val mcurrentTime = android.icu.util.Calendar.getInstance()
    val year = mcurrentTime.get(android.icu.util.Calendar.YEAR)
    val month = mcurrentTime.get(android.icu.util.Calendar.MONTH)
    val day = mcurrentTime.get(android.icu.util.Calendar.DAY_OF_MONTH)
    val minDate = android.icu.util.Calendar.getInstance()
    minDate.set(android.icu.util.Calendar.HOUR_OF_DAY, 0)
    minDate.set(android.icu.util.Calendar.MINUTE, 0)
    minDate.set(android.icu.util.Calendar.SECOND, 0)
    val dpd1 = DatePickerDialog(
        this,
        R.style.MyDatePickerStyle,
        object : DatePickerDialog.OnDateSetListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                var firstday:String? = String.format("%d-%d-%d", month + 1, dayOfMonth, year)


                var date_sel: String? = String.format("%d-%d-%d", dayOfMonth, month + 1, year)
                var date_temp = date_sel.toString()

                val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
                val inputDateStr = date_temp
                val date: Date = inputFormat.parse(inputDateStr)
                val outputDateStr: String = outputFormat.format(date)
                toDate = date_sel.toString()
                enterStratDate.text = outputDateStr

            }
        },
        year,
        month,
        day
    )
    //enterStratDate.setOnClickListener{
    dpd1.datePicker.minDate = android.icu.util.Calendar.getInstance().timeInMillis
    dpd1.show()
//}
}
    fun callPickupSubmitApi(date:String,time:String,pickupby:String,reason:String)
    {
        progressDialog.visibility = View.VISIBLE
        var devicename:String= (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT]
            .name)
        val token = sharedprefs.getaccesstoken(mContext)
        val requestPickupBody= RequestPickupApiModel(sharedprefs.getStudentID(mContext)!!,date,time,reason,pickupby,"2",devicename,"1.0")
        val call: Call<EarlyPickupModel> = ApiClient(mContext).getClient.pickupRequest(requestPickupBody,"Bearer "+token)
        call.enqueue(object : Callback<EarlyPickupModel> {
            override fun onFailure(call: Call<EarlyPickupModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)
                progressDialog.visibility = View.GONE
            }
            override fun onResponse(call: Call<EarlyPickupModel>, response: Response<EarlyPickupModel>) {
                val responsedata = response.body()
                progressDialog.visibility = View.GONE

                            if (responsedata!!.status.toString().equals("100")) {
                                showsuccessAlert(mContext,"Successfully submitted your earlypickup request.Please wait for Approval","Success")

                            } else if (responsedata.status.toString().equals("136")){
                                showerror(mContext,"Date already Registered","Alert")
                            }
            else {
                                if (responsedata.status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callPickupSubmitApi(date,time,pickupby,reason)
                                } else {
                                    if (responsedata.status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(responsedata.status, mContext)
                                    }
                                }

                            }
                        }

            })

    }
   private fun showerror(context: Context,message : String,msgHead : String)
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

        }
        dialog.show()
    }
    fun showsuccessAlert(context: Context, message: String, msgHead: String) {
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
            finish()

        }
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


