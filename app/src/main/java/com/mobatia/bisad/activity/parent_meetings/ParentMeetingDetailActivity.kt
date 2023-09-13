package com.mobatia.bisad.activity.parent_meetings

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.parent_meetings.adapter.ParentsEveningRoomListAdapter
import com.mobatia.bisad.activity.parent_meetings.adapter.TimeslotAdapter
import com.mobatia.bisad.activity.parent_meetings.model.insert_pta.PtaInsertApiModel
import com.mobatia.bisad.activity.parent_meetings.model.insert_pta.PtaInsertModel
import com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaListingApiModel
import com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaListingModel
import com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaTimeSlotList
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ParentMeetingDetailActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var progressDialog: RelativeLayout
    lateinit var title: TextView
    lateinit var context: Context
    lateinit var date_header: TextView
    var firstVisit:Boolean=true
    var dateString:String=""
    var date_sel:String=""
    var studName:String=""
    var studId:String=""
    var studClass:String=""
    var staff_name:String=""
    var staff_id:String=""
    lateinit var studentName:TextView
    lateinit var studentClass:TextView
    lateinit var staffName:TextView
    lateinit var confirm:TextView
    lateinit var cancel:TextView
    lateinit var recyclerView:RecyclerView
    lateinit var info_img:ImageView
    lateinit var back: ImageView
    lateinit var home_icon:ImageView
    lateinit var timeSlotList:ArrayList<PtaTimeSlotList>
lateinit var timeSlotListPost: ArrayList<PtaTimeSlotList>
    var alreadyslotBookedByUser:Boolean=false
    var confirmedslotBookedByUser:Boolean=false
    var confirmed_link:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parentmeeting_detail)
        sharedprefs = PreferenceData()
        init()

    }

    private fun init(){
        mContext=this
        progressDialog = findViewById<RelativeLayout>(R.id.progressDialog)
        title=findViewById(R.id.heading)
        title.text = "Parent Meetings"
        dateString=intent.getStringExtra("date").toString()
        studName=intent.getStringExtra("studentName").toString()
        studId=intent.getStringExtra("studentId").toString()
        studClass=intent.getStringExtra("studentClass").toString()
        staff_name=intent.getStringExtra("staffName").toString()
        staff_id=intent.getStringExtra("staffId").toString()
        studentName=findViewById(R.id.studentNameTV)
        studentClass=findViewById(R.id.studentclassTV)
        staffName=findViewById(R.id.staffTV)
        date_header=findViewById(R.id.dateheader)
        studentName.text = studName
        studentClass.text = studClass
        staffName.text = staff_name
        val inputFormat: DateFormat = SimpleDateFormat("d/M/yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val inputDateStr = dateString
        val date: Date = inputFormat.parse(inputDateStr)
        date_sel = outputFormat.format(date)
        Log.e("dt",date_sel)
        date_header.text = date_sel
        confirm=findViewById(R.id.confirmTextView)
        cancel=findViewById(R.id.cancelTextView)
        recyclerView=findViewById(R.id.recycler_timeslot)
        info_img=findViewById(R.id.infoRoomImg)
        timeSlotList= ArrayList()
        timeSlotListPost= ArrayList()
        timeslotList()

        info_img.setOnClickListener {
            showRoomList()
        }
        back=findViewById(R.id.btn_left)
        home_icon=findViewById(R.id.logoClickImgView)
        back.setOnClickListener {
            finish()
        }
        home_icon.setOnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        confirm.setOnClickListener {
            val intent = Intent(mContext, ReviewAppointmentsRecyclerViewActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        cancel.setOnClickListener {
            if (timeSlotListPost.get(0).booking_open.equals("y")) {
                showApiAlert(mContext,"Do you want to cancel this appointment?","Confirm")

            } else {
                showerror(mContext,"Booking and cancellation date is over.","Alert")

            }
        }

    }
    private fun showRoomList(){
        Log.e("roomlist","true")
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_room_slot_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        val socialMediaList =
            dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        //if(mSocialMediaArray.get())

        //if(mSocialMediaArray.get())
        val divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.list_divider)!!)
        socialMediaList.addItemDecoration(divider)

        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm

        Log.e("tmsie",timeSlotList.size.toString())
        val socialMediaAdapter = ParentsEveningRoomListAdapter(mContext,timeSlotList)
        socialMediaList.adapter = socialMediaAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
    private fun timeslotList() {
        timeSlotList= ArrayList()
        timeSlotListPost= ArrayList()
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val inputDateStr = date_sel
        val date: Date = inputFormat.parse(inputDateStr)
        val outputDateStr: String = outputFormat.format(date)
        Log.e("dt", outputDateStr)
        val timeslotBody = PtaListingApiModel(studId, staff_id, outputDateStr)
        val call: Call<PtaListingModel> =
            ApiClient.getClient.listing_pta(timeslotBody, "Bearer " + token)
        call.enqueue(object : Callback<PtaListingModel> {
            override fun onFailure(call: Call<PtaListingModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                progressDialog.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<PtaListingModel>,
                response: Response<PtaListingModel>
            ) {
                val responsedata = response.body()
                progressDialog.visibility = View.GONE

                if (responsedata!!.responsecode.toString().equals("200")) {
                    if(responsedata.response.statuscode.equals("303")){
                    Log.e("STATUS LOGIN", responsedata.response.statuscode)
                        if (response.body()!!.response.data.isNotEmpty()) {
                            timeSlotList.addAll(response.body()!!.response.data)

                            for (i in timeSlotList.indices) {
                                if (timeSlotList[i].status.equals("2")) {
                                    alreadyslotBookedByUser = true
                                    timeSlotListPost.add(timeSlotList[i])

                                    /* confirm.setVisibility(GONE)
                             cancel.setVisibility(GONE)*/
                                }
                            }
                            for (i in timeSlotList.indices) {
                                if (timeSlotList[i].status.equals("3")) {
                                    confirmedslotBookedByUser = true
                                    confirmed_link = timeSlotList[i].vpml
                                    /* confirm.setVisibility(GONE)
                             cancel.setVisibility(GONE)*/
                                }
                            }
                            if (confirmedslotBookedByUser) {
                                if (confirmed_link.equals("", ignoreCase = true)) {
                                    //vpmlBtn.setVisibility(View.GONE)
                                } else {
                                    // vpmlBtn.setVisibility(View.VISIBLE)
                                }
                                cancel.visibility = View.INVISIBLE
                                confirm.visibility = View.INVISIBLE
                            } else if (alreadyslotBookedByUser) {

                                cancel.visibility = View.VISIBLE
                                confirm.visibility = View.VISIBLE
                            } else {
                                cancel.visibility = View.INVISIBLE
                                confirm.visibility = View.INVISIBLE
                            }
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
                            recyclerView.setHasFixedSize(true)
                            var recyclerViewLayoutManager: RecyclerView.LayoutManager =
                                GridLayoutManager(mContext, 3)
                            val spacing = 5 // 50px


                            recyclerView.layoutManager = recyclerViewLayoutManager

                            //recyclerView.layoutManager=LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)

                            var timeslot_adapter =
                                TimeslotAdapter(mContext, timeSlotList, confirm, cancel)
                            recyclerView.adapter = timeslot_adapter
                        }else{

                            //CustomStatusDialog();
                            Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show()

                        }
                    }else if (responsedata.response.statuscode.equals("310")){
                           showerror(mContext,"Slot is already booked by an another user.","Alert")
                    }else if(response.body()!!.response.statuscode.equals("116"))
                    {
                        var internetCheck = InternetCheckClass.isInternetAvailable(com.mobatia.bisad.fragment.home.mContext)
                        if (internetCheck)
                        {
                            AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                            timeslotList()
                        }
                        else{
                            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                        }

                    }
                    recyclerView.addOnItemClickListener(object : OnItemClickListener {
                        override fun onItemClicked(position: Int, view: View) {

                            if (timeSlotList.get(position).status.equals("1")) {
                                showerror(mContext,"This slot is not available.","Alert")

                            } else if (confirmedslotBookedByUser) {
                                showerror(mContext,"Your time slot is already confirmed.","Alert")

                            } else if (timeSlotList.get(position).status.equals("0")) {
                                timeSlotListPost=ArrayList()
                                timeSlotListPost.add(timeSlotList[position])
                                if (timeSlotListPost.get(0).booking_open
                                        .equals("y")
                                ) {
                                    val inputFormat: DateFormat = SimpleDateFormat("hh:mm:ss")
                                    val outputFormat: DateFormat = SimpleDateFormat("hh:mm aa")
                                    val inputDateStr = timeSlotListPost[0].start_time
                                    val date: Date = inputFormat.parse(inputDateStr)
                                    val formt_fromtime= outputFormat.format(date)


                                    val inputFormat2: DateFormat = SimpleDateFormat("hh:mm:ss")
                                    val outputFormat2: DateFormat = SimpleDateFormat("hh:mm aa")
                                    val inputDateStr2 = timeSlotListPost[0].end_time
                                    val date2: Date = inputFormat2.parse(inputDateStr2)
                                    val formt_totime: String = outputFormat2.format(date2)
                                    Log.e("dt",formt_totime)

                                    showApiAlert(mContext,"Do you want to reserve your appointment on "+ date_sel +" , "+
                                            formt_fromtime+" - "+formt_totime,"Alert"
                                    )

                                } else {
                                    showerror(mContext,"Booking and cancellation date is over","Alert")
                                }
                            } else {
                                if (timeSlotList.get(position).status.equals("2")
                                ) {
                                    showerror(mContext,"This slot is reserved by you for the Parents' Meeting. Click 'Cancel' option to cancel this appointment","Alert")

                                } else {
                                    showerror(mContext,"Another Slot is already booked by you. If you want to take appointment on this time, please cancel earlier appointment and try","Alert")
                                }
                            }
                        }

                        })

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
    private fun showApiAlert(context: Context,message : String,msgHead : String){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_cancel=dialog.findViewById<Button>(R.id.btn_Cancel)
        text_dialog.text = message
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
            postSelectedSlot()
            dialog.dismiss()

        }
        btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun postSelectedSlot(){
        val token = sharedprefs.getaccesstoken(mContext)
        var insertPta= PtaInsertApiModel(studId.toString(),
        staff_id,timeSlotListPost[0].date,timeSlotListPost[0].start_time,
            timeSlotListPost[0].end_time,timeSlotListPost[0].book_end_date,
            timeSlotListPost[0].vpml,timeSlotListPost[0].room)

        val call: Call<PtaInsertModel> =
            ApiClient.getClient.insert_pta(insertPta, "Bearer " + token)
        call.enqueue(object : Callback<PtaInsertModel> {
            override fun onFailure(call: Call<PtaInsertModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                progressDialog.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<PtaInsertModel>,
                response: Response<PtaInsertModel>
            ) {
                val responsedata = response.body()
                progressDialog.visibility = View.GONE
if (responsedata!!.response.statuscode.equals("303")){
    //success
    showerror(mContext,"Reserved Only – Please review and confirm booking","Alert")
    timeslotList()
}else if (responsedata.response.statuscode.equals("311")){
    //cancel
    showerror(mContext,"Request cancelled successfully","Alert")
    timeslotList()
}else if (responsedata.response.statuscode.equals("310")){

    showerror(mContext,"Slot is already booked by an another user","Alert")
}else if (responsedata.response.statuscode.equals("315")){

    showerror(mContext,"Timeslot not found","Alert")
}else if(response.body()!!.response.statuscode.equals("116"))
{
    var internetCheck = InternetCheckClass.isInternetAvailable(com.mobatia.bisad.fragment.home.mContext)
    if (internetCheck)
    {
        AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
        postSelectedSlot()
    }
    else{
        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
    }

} else {

    Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show()
}

            }
        })
    }

    override fun onResume() {
        super.onResume()
        //other stuff
        if (firstVisit) {
            //do stuff for first visit only
            firstVisit = false
        } else {
            timeslotList()

        }
    }
   /* override fun onResume() {
        super.onResume()
        timeSlotList= ArrayList()
timeslotList()
    }*/
}