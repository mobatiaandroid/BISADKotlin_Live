package com.mobatia.bisad.activity.parent_meetings

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.parent_meetings.model.PtaAllottedDatesApiModel
import com.mobatia.bisad.activity.parent_meetings.model.PtaAllottedDatesModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

class ParentsEveningCalendarActivity:AppCompatActivity () {
lateinit var mContext:Context
lateinit var title:TextView
    lateinit var sharedprefs: PreferenceData
    lateinit var progressDialog: RelativeLayout
    lateinit var context:Context
    lateinit var header:TextView
    lateinit var arrow_prev: ImageView
    lateinit var arrow_nxt: ImageView
    lateinit var monthlist:Array<String>
    lateinit var week_day:Array<String>
    lateinit var nums_Array:ArrayList<String>
    lateinit var back:ImageView
    lateinit var home_icon:ImageView
    var studentId:String=""
    var studentName:String=""
    var studentClass:String=""
    var staffId:String=""
    var staffName:String=""
    var month_total_days:Int?=null
    var count_month:Int?=null
    var count_year:Int?=null
    val dateTextView = arrayOfNulls<TextView>(42)
    lateinit var datesToPlot:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parentmeeting_calendar)
        init()
        headerfnc()
        daysinweek()
        onclick()
        //holiday()
    }
    private fun init(){
        mContext=this
        sharedprefs = PreferenceData()
        progressDialog =findViewById(R.id.progressDialog)
        studentId=intent.getStringExtra("studId").toString()
        studentName=intent.getStringExtra("studName").toString()
        studentClass=intent.getStringExtra("studClass").toString()
        staffId=intent.getStringExtra("staffId").toString()
        staffName=intent.getStringExtra("staffName").toString()
        back=findViewById(R.id.btn_left)
        home_icon=findViewById(R.id.logoClickImgView)
        title=findViewById(R.id.heading)
        title.text = "Parent Meetings"
        nums_Array= ArrayList()
        datesToPlot= ArrayList()
        header=findViewById(R.id.Header)
        arrow_prev=findViewById(R.id.arrow_back)
        arrow_nxt=findViewById(R.id.arrow_nxt)
        monthlist= resources.getStringArray(R.array.Months)
        week_day= resources.getStringArray(R.array.Weeks)
        back.setOnClickListener {
            finish()
        }
        home_icon.setOnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
       /* datesToPlot= ArrayList()
        var m1=Datelist("30/11/2022")
        var m2=Datelist("12/12/2022")
        var m3=Datelist("25/12/2022")
        var m4=Datelist("3/1/2023")
        datesToPlot.add(0,m1)
        datesToPlot.add(1,m2)
        datesToPlot.add(2,m3)
        datesToPlot.add(3,m4)*/
        setTextview()
        allotedDatedApi()

    }

    private fun setTextview(){
        for (i in 0..41)
        {
            val resID: Int = mContext.resources.getIdentifier("textview_$i", "id", mContext.packageName)
            dateTextView[i]=findViewById(resID)
            dateTextView[i]!!.setBackgroundColor(Color.WHITE)
            dateTextView[i]!!.setTextColor(Color.BLACK)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun headerfnc(){
        var x=9
        var currentMonth=monthlist[x]
        var month= Calendar.getInstance()
        var simpleDateFormat = SimpleDateFormat("MM")
        var s=SimpleDateFormat("MMMM")
        var month_tt=s.format(month.time)
        var dateMonth = simpleDateFormat.format(month.time).toString()
        var simpleDateFormat2 = SimpleDateFormat("yyyy")
        var dateYear = simpleDateFormat2.format(month.time).toString()
        count_month=dateMonth.toInt()-1
        count_year=dateYear.toInt()
        header.text = month_tt +" " +count_year

        arrow_prev.setOnClickListener {
            nums_Array= ArrayList()
            if (count_month!=0)
            {
                count_month= count_month!! -1
                val m = monthlist[count_month!!]
                header.text = m + count_year
                daysinweek()
                setTextview()
                allotedDatedApi()
                //holiday()
            }
            else
            {
                count_year= count_year!! -1
                val m = monthlist[11]
                count_month=11
                header.text = m + count_year
                daysinweek()
                setTextview()
                allotedDatedApi()
               // holiday()
            }
        }
        arrow_nxt.setOnClickListener {

            nums_Array= ArrayList()
            if (count_month!=11)
            {
                count_month= count_month!! +1
                val m = monthlist[count_month!!]
                header.text = m + count_year
                daysinweek()
                setTextview()
                allotedDatedApi()
                //holiday()
            }
            else
            {
                count_year= count_year!! +1
                val m = monthlist[0]
                count_month=0
                header.text = m + count_year
                daysinweek()
                setTextview()
                allotedDatedApi()
               // holiday()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysinweek(){

        var count_month2= count_month!!+1
        val yearMonthObject: YearMonth = YearMonth.of(count_year!!, count_month2)
        val firstday_date: LocalDate? =yearMonthObject.atDay(1)
        val day_name: DayOfWeek = firstday_date!!.dayOfWeek
        month_total_days=yearMonthObject.lengthOfMonth()


        if (day_name.toString().equals("SUNDAY"))
        {
            for (i in 1..month_total_days!!)
            {
                nums_Array.add(i.toString())
            }
        }

        else if (day_name.toString().equals("MONDAY"))
        {
            nums_Array.add("0")
            for (i in 1..month_total_days!!)
            {
                nums_Array.add(i.toString())
            }
        }

        else if (day_name.toString().equals("TUESDAY"))
        {
            nums_Array.add("0")
            nums_Array.add("0")
            for (i in 1..month_total_days!!)
            {
                nums_Array.add(i.toString())
            }
        }

        else if (day_name.toString().equals("WEDNESDAY"))
        {
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            for (i in 1..month_total_days!!)
            {
                nums_Array.add(i.toString())
            }
        }

        else if (day_name.toString().equals("THURSDAY"))
        {
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            for (i in 1..month_total_days!!)
            {
                nums_Array.add(i.toString())
            }
        }

        else if (day_name.toString().equals("FRIDAY"))
        {
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            for (i in 1..month_total_days!!)
            {
                nums_Array.add(i.toString())
            }
        }

        else if (day_name.toString().equals("SATURDAY"))
        {
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            nums_Array.add("0")
            for (i in 1..month_total_days!!)
            {
                nums_Array.add(i.toString())
            }
        }

        for (i in 0..41){
            if (i<=nums_Array.size-1)
            {
                dateTextView[i]!!.visibility= View.VISIBLE
                var value=nums_Array.get(i).toString()

                if (value.equals("0"))
                {
                    dateTextView[i]!!.visibility= View.INVISIBLE
                }
                else
                {
                    dateTextView[i]!!.visibility= View.VISIBLE
                    dateTextView[i]!!.text = value.toString()
                }
            }
            else
            {
                dateTextView[i]!!.visibility= View.INVISIBLE
            }
        }
    }
    private fun onclick(){

        for (i in 0..41) {
            dateTextView[i]!!.setBackgroundColor(Color.WHITE)
            dateTextView[i]!!.setOnClickListener {

                var c_day=nums_Array.get(i)
                var c_month= count_month!! +1
                var c_year=count_year
                var c_date=c_day+"/"+c_month+"/"+c_year

                for (i in 0..datesToPlot.size-1){

                    if (c_date.equals(datesToPlot[i])){
                        val intent = Intent(mContext, ParentMeetingDetailActivity::class.java)
                        intent.putExtra("date",c_date)
                        intent.putExtra("studentName",studentName)
                        intent.putExtra("studentId",studentId)
                        intent.putExtra("studentClass",studentClass)
                        intent.putExtra("staffName",staffName)
                        intent.putExtra("staffId",staffId)
                        startActivity(intent)
                        break
                    }

                }
            }
        }
    }
    private fun allotedDatedApi(){
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val listAllotedDates= PtaAllottedDatesApiModel(studentId,staffId)
        val call: Call<PtaAllottedDatesModel> = ApiClient.getClient.pta_allotted_dates(listAllotedDates,"Bearer "+token)
        call.enqueue(object : Callback<PtaAllottedDatesModel> {
            override fun onFailure(call: Call<PtaAllottedDatesModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)
            }
            override fun onResponse(call: Call<PtaAllottedDatesModel>, response: Response<PtaAllottedDatesModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.responsecode.equals("200")){
                    val datelistSize=response.body()!!.response.data.size-1
                    for (i in 0..datelistSize ){
                        var dates=response.body()!!.response.data[i]
                        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val outputFormat: DateFormat = SimpleDateFormat("d/M/yyyy")
                        val inputDateStr = dates
                        val date: Date = inputFormat.parse(inputDateStr)
                        val outputDateStr: String = outputFormat.format(date)
                        datesToPlot.add(i,outputDateStr)
                    }

                    //datesToPlot.addAll(response.body()!!.response.data)

                    for (i in 0..datesToPlot.size-1){
                        var days_s=datesToPlot[i]


                        for (i in 0..nums_Array.size-1) {

                            var c_day=nums_Array.get(i)
                            var c_month= count_month!! +1
                            var c_year=count_year
                            var c_date=c_day+"/"+c_month+"/"+c_year

                            if (days_s.equals(c_date)){

                                dateTextView[i]!!.setBackgroundResource(R.drawable.roundred)
                                dateTextView[i]!!.setTextColor(Color.WHITE)

                            }

                            else{

                            }
                        }
                    }
                }else if(response.body()!!.response.statuscode.equals("116"))
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(com.mobatia.bisad.fragment.home.mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                        allotedDatedApi()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.response.statuscode.toInt(),
                        com.mobatia.bisad.fragment.home.mContext
                    )
                }
                {


                }


            }

        })
    }
    /*private fun holiday(){

        for (i in 0..datesToPlot.size-1){
            var days_s=datesToPlot[i].date

            for (i in 0..nums_Array.size-1) {

                var c_day=nums_Array.get(i)
                var c_month= count_month!! +1
                var c_year=count_year
                var c_date=c_day+"/"+c_month+"/"+c_year

                if (days_s.equals(c_date)){

                    dateTextView[i]!!.setBackgroundResource(R.drawable.roundred)
                    dateTextView[i]!!.setTextColor(Color.WHITE)

                }

                else{

                }
            }
        }
    }*/
}