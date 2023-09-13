package com.mobatia.bisad.activity.canteen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
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
import com.applandeo.materialcalendarview.CalendarView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.model.DateModel
import com.mobatia.bisad.activity.canteen.model.TimeExceedModel
import com.mobatia.bisad.activity.canteen.model.add_orders.CatListModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartApiModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PreOrderActivity : AppCompatActivity(){
    lateinit var nContext: Context
    private lateinit var logoClickImg: ImageView
    lateinit var recyclerview: RecyclerView
    lateinit var back: ImageView
   // lateinit var progressDialog: RelativeLayout
    lateinit var sharedprefs: PreferenceData
    lateinit var studImg: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var studentSpinner: LinearLayout
    lateinit var buttonLinear: LinearLayout
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    var studentListArrayList = ArrayList<StudentList>()
    lateinit var studentlist: ArrayList<String>
    lateinit var studentname: TextView
    lateinit var dropdown: LinearLayout
    lateinit var title: TextView
    lateinit var add_order: RelativeLayout
    lateinit var my_orders: RelativeLayout
    lateinit var order_history: RelativeLayout
    lateinit var progressDialog: ProgressBar
    lateinit var progressDialogAdd: ProgressBar
    var time_exeed: String = ""
    var datetime: String = ""
    var apiCall:Int=0
    var mDateArrayList = ArrayList<DateModel>()
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_preorder)
        sharedprefs = PreferenceData()
        initfn()
        var internetCheck = InternetCheckClass.isInternetAvailable(nContext)
        if (internetCheck)
        {
            callStudentListApi()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

        onclick()
    }
    private fun initfn() {
        nContext = this
        logoClickImg=findViewById(R.id.logoclick)
        back = findViewById(R.id.back)
        studImg = findViewById<ImageView>(R.id.imagicon)
        studentNameTxt = findViewById<TextView>(R.id.studentName)
        studentSpinner = findViewById<LinearLayout>(R.id.studentSpinner)
        studentlist = ArrayList()
        dropdown = findViewById(R.id.studentSpinner)
        progressDialog = findViewById(R.id.progressDialog)
        progressDialogAdd = findViewById(R.id.progressDialogAdd)
        add_order = findViewById(R.id.addOrderRelative)
        my_orders = findViewById(R.id.myOrderRelative)
        order_history = findViewById(R.id.orderHistoryRelative)
        buttonLinear = findViewById(R.id.buttonLinear)
        title = findViewById(R.id.textViewtitle)
        title.text = "Pre-Order"
        back.setOnClickListener {
            finish()
        }
        logoClickImg.setOnClickListener {
            val intent = Intent(nContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
//        val aniRotate: Animation =
//            AnimationUtils.loadAnimation(nContext, R.anim.linear_interpolator)
//        progressDialog.startAnimation(aniRotate)
        studentSpinner.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                showStudentList(nContext,studentListArrayList)

            }
        })

    }
    private fun onclick() {
        add_order.setOnClickListener {
            progressDialogAdd.visibility=View.VISIBLE
            time_exeed()

        }
        my_orders.setOnClickListener {
            val intent = Intent(nContext, MyorderActivity::class.java)
            intent.putExtra("StudentId",studentId)
            nContext.startActivity(intent)
        }
        order_history.setOnClickListener {
            val intent = Intent(nContext, OrderhistoryActivity::class.java)
            intent.putExtra("StudentId",studentId)
            nContext.startActivity(intent)
        }
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
                nContext.resources.getDrawable(R.drawable.button_new)
            )
        } else {
            btn_dismiss.background = nContext.resources.getDrawable(R.drawable.button_new)
        }

        studentListRecycler.setHasFixedSize(true)
        val llm = LinearLayoutManager(nContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.layoutManager = llm
        if(mStudentList.size>0)
        {
            val studentAdapter = StudentListAdapter(nContext,mStudentList)
            studentListRecycler.adapter = studentAdapter
        }

        btn_dismiss.setOnClickListener()
        {
            dialog.dismiss()
        }
        studentListRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                progressDialog.visibility=View.VISIBLE
                val aniRotate: Animation =
                    AnimationUtils.loadAnimation(nContext, R.anim.linear_interpolator)
                progressDialog.startAnimation(aniRotate)

                studentName=studentListArrayList.get(position).name
                studentImg=studentListArrayList.get(position).photo
                studentId=studentListArrayList.get(position).id
                studentClass=studentListArrayList.get(position).section
                sharedprefs.setStudentID(nContext,studentId)
                sharedprefs.setStudentName(nContext,studentName)
                sharedprefs.setStudentPhoto(nContext,studentImg)
                sharedprefs.setStudentClass(nContext,studentClass)
                studentNameTxt.text=studentName
                //studentInfoArrayList.clear()
                if(!studentImg.equals(""))
                {
                    Glide.with(nContext) //1
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
                add_order.visibility=View.VISIBLE
                buttonLinear.visibility=View.VISIBLE
                progressDialog.visibility = View.VISIBLE

                //  Toast.makeText(activity, mStudentList.get(position).name, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
        dialog.show()
    }
    fun callStudentListApi()
    {
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(nContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
                progressDialog.visibility = View.GONE
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                progressDialog.visibility = View.GONE
                //val arraySize :Int = response.body()!!.responseArray.studentList.size
                if (response.body()!!.status==100)
                {
                    studentListArrayList.addAll(response.body()!!.responseArray.studentList)
                    if (sharedprefs.getStudentID(nContext).equals(""))
                    {
                        studentName=studentListArrayList.get(0).name
                        studentImg=studentListArrayList.get(0).photo
                        studentId=studentListArrayList.get(0).id
                        studentClass=studentListArrayList.get(0).section
                        sharedprefs.setStudentID(nContext,studentId)
                        sharedprefs.setStudentName(nContext,studentName)
                        sharedprefs.setStudentPhoto(nContext,studentImg)
                        sharedprefs.setStudentClass(nContext,studentClass)
                        studentNameTxt.text=studentName
                        if(!studentImg.equals(""))
                        {
                            Glide.with(nContext) //1
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
                        progressDialog.visibility = View.GONE
                    }
                    else{
                        studentName= sharedprefs.getStudentName(nContext)!!
                        studentImg= sharedprefs.getStudentPhoto(nContext)!!
                        studentId= sharedprefs.getStudentID(nContext)!!
                        studentClass= sharedprefs.getStudentClass(nContext)!!
                        studentNameTxt.text=studentName
                        if(!studentImg.equals(""))
                        {
                            Glide.with(nContext) //1
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
                    add_order.visibility=View.VISIBLE
                    buttonLinear.visibility=View.VISIBLE
                    var internetCheck = InternetCheckClass.isInternetAvailable(nContext)
                    if (internetCheck)
                    {
                        //callStudentLeaveInfo()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                    //callStudentInfoApi()
                }


            }

        })
    }
    private fun calendarpopup() {
        mDateArrayList= ArrayList()
        val dialog = Dialog(nContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.calendar_canteen_popup)
        var close_img = dialog.findViewById(R.id.imgClose) as? ImageView
        var dummyClose = dialog.findViewById(R.id.dummyClose) as? ImageView
        var btn_submit = dialog.findViewById(R.id.GetDate) as Button
        val calendarView = dialog.findViewById<CalendarView>(R.id.MCalendar)
        close_img!!.setOnClickListener {
            dialog.dismiss()
        }
        dummyClose!!.setOnClickListener()
        {
            dialog.dismiss()
        }
        if (time_exeed.equals("1")) {
            val c = Calendar.getInstance()
            calendarView.setMinimumDate(c)
        } else {
            val c = Calendar.getInstance()
            c.add(Calendar.DATE, -1)
            calendarView.setMinimumDate(c)
        }
        calendarView.setPreviousButtonImage(
            nContext.resources.getDrawable(R.drawable.arrow_list_back)
        )
        calendarView.setForwardButtonImage(
            nContext.resources.getDrawable(R.drawable.arrow_list)
        )



        btn_submit.setOnClickListener()
        {
            for (calendar in calendarView.selectedDates) {
                /* println("GetDate: " + calendar.time.toString())
                 println("GetDate: " + calendar.timeInMillis)*/
                val DateF: String = calendar.get(Calendar.DATE).toString()
                val Day: String = calendar.time.toString()
                var strCurrentDate = ""
                var format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
                var newDate: Date? = null
                try {
                    newDate = format.parse(calendar.time.toString())
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                format = SimpleDateFormat("EEE", Locale.ENGLISH)
                strCurrentDate = format.format(newDate)
                val DayF:String=strCurrentDate
                //val DayF: String = CommonMethods.dateParsingToEEE(calendar.time.toString()).toString()

                var strCurrentMonth = ""
                var format2 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
                var newDate2: Date? = null
                try {
                    newDate2 = format2.parse(calendar.time.toString())
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                format2 = SimpleDateFormat("MMM", Locale.ENGLISH)
                strCurrentMonth = format2.format(newDate2)

                val MonthF:String=strCurrentMonth
               // val MonthF: String = CommonMethods.dateParsingTomm(calendar.time.toString()).toString()

                val Year: String = calendar.time.toString()
                var strCurrentYear = ""
                var format3 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
                var newDate3: Date? = null
                try {
                    newDate3 = format3.parse(calendar.time.toString())
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                format3 = SimpleDateFormat("yyyy", Locale.ENGLISH)
                strCurrentYear = format3.format(newDate)
                val YearF:String=strCurrentYear
                //val YearF: String = CommonMethods.dateParsingToyyy(calendar.time.toString()).toString()

                var strCurrentMmyear = ""
                var format4 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
                var newDate4: Date? = null
                try {
                    newDate4 = format4.parse(calendar.time.toString())
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                format4 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                strCurrentMmyear = format4.format(newDate)
                val numberDate:String=strCurrentMmyear
               // val numberDate: String = CommonMethods.dateParsingToyymmdd(calendar.time.toString()).toString()
                val model = DateModel(DateF, DayF, MonthF, YearF, numberDate, false, false)

                mDateArrayList.add(model)
                // PreferenceManager().setdate_list(nContext,mDateArrayList)
            }
            if (mDateArrayList.size == 0) {
                alert_validemail(nContext,"Alert","Please select any date")
            } else {
                var isFound = false
                var foundPosition = -1
                for (i in mDateArrayList.indices) {
                    if (datetime.equals(mDateArrayList.get(i).date)
                    ) {
                        // val timeExceed: String = time_exeed()
                        if (time_exeed.equals("1")) {
                            isFound = true
                            foundPosition = i
                        }
                    }
                }
                if (isFound) {
                    Log.e(
                        "timexeed",
                        "Sorry pre-ordering for the day closes at 7.30am .Please remove todays date"
                    )

                } else {
                    dialog.dismiss()
                    val intent = Intent(nContext, Addorder_Activity::class.java)
                    intent.putExtra("date_list",mDateArrayList)
                    startActivity(intent)
                }
            }



        }

        dialog.dismiss()

        close_img.setOnClickListener()
        {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun time_exeed() {
        val token = PreferenceData().getaccesstoken(nContext)
        progressDialogAdd.visibility=View.VISIBLE
        val call: Call<TimeExceedModel> = ApiClient.getClient.time_exceed_status("Bearer "+token)
        call.enqueue(object : Callback<TimeExceedModel> {
            override fun onFailure(call: Call<TimeExceedModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                progressDialogAdd.visibility=View.GONE
            }
            override fun onResponse(call: Call<TimeExceedModel>, response: Response<TimeExceedModel>) {
                val responsedata = response.body()
                if (responsedata!!.status==100) {

                    time_exeed=response!!.body()!!.responseArray.time_exceed.toString()
                    progressDialogAdd.visibility=View.GONE
                    calendarpopup()

                }else if (response.body()!!.status == 116) {
                    if (apiCall != 4) {
                        apiCall = apiCall + 1
                        AccessTokenClass.getAccessToken(nContext)

                    } else {

                        showSuccessAlertnew(
                            nContext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )

                    }
                } else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
                    }
                }
            }

        })



    }
    fun showSuccessAlertnew(context: Context, message: String, msgHead: String) {
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
        }
        dialog.show()
    }
    fun alert_validemail(context: Context, title: String, description: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val btn_Ok = dialog.findViewById<Button>(R.id.btn_Ok)
        val descriptionTxt = dialog.findViewById<TextView>(R.id.text_dialog)
        val titleTxt = dialog.findViewById<TextView>(R.id.alertHead)
        titleTxt.text = title
        descriptionTxt.text = description
        btn_Ok.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()


    }
}