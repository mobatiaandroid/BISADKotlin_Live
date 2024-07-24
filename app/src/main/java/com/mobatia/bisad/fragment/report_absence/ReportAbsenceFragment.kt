package com.mobatia.bisad.fragment.report_absence

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.AbsenceDetailActivity
import com.mobatia.bisad.activity.absence.EarlyPickupDetailActivity
import com.mobatia.bisad.activity.absence.RequestPlannedLeavesActivity
import com.mobatia.bisad.activity.absence.RequestabsenceActivity
import com.mobatia.bisad.activity.absence.RequestearlypickupActivity
import com.mobatia.bisad.activity.absence.model.EarlyPickupListModel
import com.mobatia.bisad.activity.absence.model.PickupListApiModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.report_absence.adapter.PickuplistAdapter
import com.mobatia.bisad.fragment.report_absence.adapter.RequestAbsenceRecyclerAdapter
import com.mobatia.bisad.fragment.report_absence.model.*
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

class ReportAbsenceFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var sharedprefs: PreferenceData
    lateinit var studentSpinner: LinearLayout
    lateinit var studImg: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var newRequestAbsence: TextView
    lateinit var newRequestPlanned: TextView
    lateinit var newRequestPickup: TextView
    lateinit var plannedLeaves: TextView
    lateinit var mAbsenceListView: RecyclerView
    lateinit var mPickupListView: RecyclerView
    lateinit var mPlannedListView: RecyclerView
    lateinit var pickup_list:ArrayList<EarlyPickupListModel>
    lateinit var pickupListSort:ArrayList<EarlyPickupListModel>
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    var studentListArrayList = ArrayList<StudentList>()
    lateinit var studentInfoCopy :ArrayList<AbsenceRequestListDetailModel>
    var studentInfoArrayList = ArrayList<AbsenceRequestListDetailModel>()
    lateinit var progressDialog: RelativeLayout
    lateinit var mContext: Context
    lateinit var absence_btn:TextView
    lateinit var pickup_btn:TextView
    lateinit var heading:TextView

    var select_val:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report_absence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        selectCategory()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callStudentListApi()
        }
        else
        {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }

    }

    private fun initializeUI() {

        studentSpinner = requireView().findViewById(R.id.studentSpinner) as LinearLayout
        studImg = requireView().findViewById(R.id.studImg) as ImageView
        studentNameTxt = requireView().findViewById(R.id.studentName) as TextView
        newRequestAbsence = requireView().findViewById(R.id.newRequestAbsence)
        newRequestPlanned = requireView().findViewById(R.id.newRequestPlanned)
        newRequestPickup = requireView().findViewById(R.id.newRequestEarly)
        heading=requireView().findViewById(R.id.appregisteredHint)
        heading.text = "App Registered Absences"
        mAbsenceListView = requireView().findViewById(R.id.mAbsenceListView) as RecyclerView
        mPlannedListView = requireView().findViewById(R.id.mPlannedListView) as RecyclerView
        mPickupListView=requireView().findViewById(R.id.mPickupListView)
        pickup_list= ArrayList()
        pickupListSort=ArrayList()
        progressDialog = requireView().findViewById(R.id.progressDialog)
        absence_btn=requireView().findViewById(R.id.absenc_btn)
        plannedLeaves=requireView().findViewById(R.id.plannedLeaves)
        absence_btn.setBackgroundResource(R.color.colorAccent)
        pickup_btn=requireView().findViewById(R.id.earlypickup_btn)
        linearLayoutManager = LinearLayoutManager(mContext)
        mAbsenceListView.layoutManager = linearLayoutManager
        mAbsenceListView.itemAnimator = DefaultItemAnimator()
        absence_btn.setBackgroundResource(R.drawable.event_spinnerfill)
        studentSpinner.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //your implementation goes here
                showStudentList(mContext,studentListArrayList)
            }
        })
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        mAbsenceListView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                val intent =Intent(activity,AbsenceDetailActivity::class.java)
                intent.putExtra("studentName",sharedprefs.getStudentName(mContext))
                intent.putExtra("studentClass",sharedprefs.getStudentClass(mContext))
                intent.putExtra("fromDate",studentInfoArrayList.get(position).from_date)
                intent.putExtra("toDate",studentInfoArrayList.get(position).to_date)
                intent.putExtra("reason",studentInfoArrayList.get(position).reason)
                activity?.startActivity(intent)
            }
        })
        mPlannedListView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                val intent =Intent(activity,AbsenceDetailActivity::class.java)
                intent.putExtra("studentName",sharedprefs.getStudentName(mContext))
                intent.putExtra("studentClass",sharedprefs.getStudentClass(mContext))
                intent.putExtra("fromDate",studentInfoArrayList.get(position).from_date)
                intent.putExtra("toDate",studentInfoArrayList.get(position).to_date)
                intent.putExtra("reason",studentInfoArrayList.get(position).reason)
                activity?.startActivity(intent)
            }
        })
        mPickupListView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                val intent =Intent(activity,EarlyPickupDetailActivity::class.java)
                intent.putExtra("studentName",sharedprefs.getStudentName(mContext))
                intent.putExtra("studentClass",sharedprefs.getStudentClass(mContext))
                intent.putExtra("date",pickupListSort.get(position).pickup_date)
                intent.putExtra("time",pickupListSort.get(position).pickup_time)
                intent.putExtra("pickupby",pickupListSort.get(position).pickup_by_whom)
                intent.putExtra("reason",pickupListSort.get(position).reason)
                intent.putExtra("status",pickupListSort.get(position).status)
                intent.putExtra("reason_for_rejection",pickupListSort.get(position).reason_for_rejection)
                activity?.startActivity(intent)
            }
        })

        /*    val intent =Intent(activity,AbsenceDetailActivity::class.java)
                intent.putExtra("studentName",sharedprefs.getStudentName(mContext))
                intent.putExtra("studentClass",sharedprefs.getStudentClass(mContext))
                intent.putExtra("fromDate",studentInfoArrayList.get(position).from_date)
                intent.putExtra("toDate",studentInfoArrayList.get(position).to_date)
                intent.putExtra("reason",studentInfoArrayList.get(position).reason)
                activity?.startActivity(intent)*/
        newRequestAbsence.setOnClickListener(View.OnClickListener {
            showSuccessmailAlert(mContext,
                "For planned absences please email your head of school","Alert")


        })

        newRequestPlanned.setOnClickListener(View.OnClickListener {
            val intent =Intent(activity, RequestPlannedLeavesActivity::class.java)
            activity?.startActivity(intent)
        })
    }


    fun callStudentListApi()
    {
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel>{
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {

                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                progressDialog.visibility = View.GONE
                //val arraySize :Int = response.body()!!.responseArray.studentList.size
                if (response.body()!!.status==100)
                {
                    studentListArrayList.addAll(response.body()!!.responseArray.studentList)
                    if (sharedprefs.getStudentID(mContext).equals(""))
                    {
                        studentName=studentListArrayList.get(0).name
                        studentImg=studentListArrayList.get(0).photo
                        studentId=studentListArrayList.get(0).id
                        studentClass=studentListArrayList.get(0).section
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
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        callStudentLeaveInfo()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                    //callStudentInfoApi()
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
                progressDialog.visibility=View.VISIBLE
                val aniRotate: Animation =
                    AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
                progressDialog.startAnimation(aniRotate)

                studentName=studentListArrayList.get(position).name
                studentImg=studentListArrayList.get(position).photo
                studentId=studentListArrayList.get(position).id
                studentClass=studentListArrayList.get(position).section
                sharedprefs.setStudentID(mContext,studentId)
                sharedprefs.setStudentName(mContext,studentName)
                sharedprefs.setStudentPhoto(mContext,studentImg)
                sharedprefs.setStudentClass(mContext,studentClass)
                studentNameTxt.text=studentName
                studentInfoArrayList.clear()
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
                progressDialog.visibility = View.VISIBLE
                if (select_val==0){
                    callStudentLeaveInfo()
                }
               else if (select_val==1) {
                    callpickuplist_api()
                }
                else if (select_val==2)
                {
                    callPlannedLeaves()
                }
              //  Toast.makeText(activity, mStudentList.get(position).name, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
        dialog.show()
    }
    fun callStudentLeaveInfo()
    {
        progressDialog.visibility = View.VISIBLE
        studentInfoCopy=ArrayList<AbsenceRequestListDetailModel>()
        studentInfoArrayList.clear()
        mAbsenceListView.visibility=View.GONE
        mPlannedListView.visibility=View.GONE
        val studentInfoAdapter = RequestAbsenceRecyclerAdapter(studentInfoArrayList)
        mAbsenceListView.adapter = studentInfoAdapter
        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= AbsenceLeaveApiModel(sharedprefs.getStudentID(mContext)!!,0,20)
        val call: Call<AbsenceListModel> = ApiClient.getClient.absenceList(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<AbsenceListModel>{
            override fun onFailure(call: Call<AbsenceListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<AbsenceListModel>, response: Response<AbsenceListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    studentInfoCopy.addAll(response.body()!!.responseArray.requestList)
                    studentInfoArrayList=studentInfoCopy

                        if (studentInfoArrayList.size>0)
                        {
                            mAbsenceListView.visibility=View.VISIBLE
                            val studentInfoAdapter = RequestAbsenceRecyclerAdapter(studentInfoArrayList)
                            mAbsenceListView.adapter = studentInfoAdapter
                        }
                        else{
                            Toast.makeText(mContext, "No Registered Absence Found", Toast.LENGTH_SHORT).show()
                            mAbsenceListView.visibility=View.GONE
                        }



                }else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                        callStudentLeaveInfo()
                    } else {
                        if (response.body()!!.status == 132) {
                            studentInfoCopy=ArrayList<AbsenceRequestListDetailModel>()
                            studentInfoArrayList.clear()
                            val studentInfoAdapter = RequestAbsenceRecyclerAdapter(studentInfoArrayList)
                            mAbsenceListView.adapter = studentInfoAdapter
                            Toast.makeText(mContext, "No Registered Absence Found", Toast.LENGTH_SHORT).show()
                            //validation check error
                        } /*else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                        }*/
                    }

                }


            }

        })
    }
    private fun selectCategory(){

        absence_btn.setOnClickListener {
            select_val=0
            callStudentLeaveInfo()
            absence_btn.setBackgroundResource(R.drawable.event_spinnerfill)
            absence_btn.setTextColor(Color.BLACK)
            pickup_btn.setBackgroundResource(R.drawable.event_greyfill)
            pickup_btn.setTextColor(Color.BLACK)
            plannedLeaves.setBackgroundResource(R.drawable.event_greyfill)
            plannedLeaves.setTextColor(Color.BLACK)
            heading.text = "App Registered Absences"
            mAbsenceListView.visibility = VISIBLE
            newRequestAbsence.visibility = VISIBLE
            mPickupListView.visibility = GONE
            newRequestPlanned.visibility = GONE
            newRequestPickup.visibility = GONE

        }
        pickup_btn.setOnClickListener {
            select_val=1
            callpickuplist_api()
            absence_btn.setBackgroundResource(R.drawable.event_greyfill)
            absence_btn.setTextColor(Color.BLACK)
            plannedLeaves.setBackgroundResource(R.drawable.event_greyfill)
            plannedLeaves.setTextColor(Color.BLACK)
            pickup_btn.setBackgroundResource(R.drawable.event_spinnerfill)
            pickup_btn.setTextColor(Color.BLACK)
            heading.text = "App Registered Early Pickup"
            newRequestAbsence.visibility = GONE
            newRequestPlanned.visibility = GONE
            mPickupListView.visibility = VISIBLE
            mPlannedListView.visibility = GONE
            mAbsenceListView.visibility = GONE
            newRequestPickup.visibility = VISIBLE
            mPickupListView.layoutManager=LinearLayoutManager(mContext)
            var pickuplistAdapter= PickuplistAdapter(mContext,pickup_list)
            mPickupListView.adapter=pickuplistAdapter
        }

        plannedLeaves.setOnClickListener(View.OnClickListener {
            select_val=2
            callPlannedLeaves()
            absence_btn.setBackgroundResource(R.drawable.event_greyfill)
            absence_btn.setTextColor(Color.BLACK)
            pickup_btn.setBackgroundResource(R.drawable.event_greyfill)
            pickup_btn.setTextColor(Color.BLACK)
            plannedLeaves.setBackgroundResource(R.drawable.event_spinnerfill)
            plannedLeaves.setTextColor(Color.BLACK)
            heading.text = "App Registered Planned Leaves"
            mPickupListView.visibility = GONE
            mPlannedListView.visibility = VISIBLE
            mAbsenceListView.visibility = GONE
            newRequestAbsence.visibility = GONE
            newRequestPickup.visibility = GONE
            newRequestPlanned.visibility = VISIBLE
            mPlannedListView.layoutManager=LinearLayoutManager(mContext)
            val studentInfoAdapter = RequestAbsenceRecyclerAdapter(studentInfoArrayList)
            mPlannedListView.adapter = studentInfoAdapter
        })
        newRequestPickup.setOnClickListener {
            val intent =Intent(activity, RequestearlypickupActivity::class.java)
            activity?.startActivity(intent)
        }

    }
    private fun callpickuplist_api(){
        progressDialog.visibility = View.VISIBLE
        studentInfoCopy=ArrayList<AbsenceRequestListDetailModel>()
        studentInfoArrayList.clear()
        pickup_list= ArrayList()
        pickupListSort= ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val list_pickup= PickupListApiModel(sharedprefs.getStudentID(mContext)!!,"0","100")
        val call: Call<PickupModel> = ApiClient.getClient.pickupList(list_pickup,"Bearer "+token)
        call.enqueue(object : Callback<PickupModel>{
            override fun onFailure(call: Call<PickupModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<PickupModel>, response: Response<PickupModel>) {
               progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    pickup_list.addAll(response.body()!!.responseArray.requestList)
                    mPickupListView.visibility = VISIBLE
                    var list_size=pickup_list.size-1
                    pickupListSort=ArrayList()
                    for (i in pickup_list.indices){
                        pickupListSort.add(pickup_list[list_size-i])
                    }
if (pickupListSort.size>0){
    mPickupListView.layoutManager=LinearLayoutManager(mContext)
    var pickuplistAdapter= PickuplistAdapter(mContext,pickupListSort)
    mPickupListView.adapter=pickuplistAdapter
}else{
    mPickupListView.layoutManager=LinearLayoutManager(mContext)
    var pickuplistAdapter= PickuplistAdapter(mContext,pickupListSort)
    mPickupListView.adapter=pickuplistAdapter
    Toast.makeText(mContext, "No Registered Early Pickup Found", Toast.LENGTH_SHORT).show()
}


                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                        callpickuplist_api()
                    } else {
                        if (response.body()!!.status == 132) {
                            pickup_list= ArrayList()
                            pickupListSort= ArrayList()
                            mPickupListView.layoutManager=LinearLayoutManager(mContext)
                            var pickuplistAdapter= PickuplistAdapter(mContext,pickupListSort)
                            mPickupListView.adapter=pickuplistAdapter
                            Toast.makeText(mContext, "No Registered Early Pickup Found", Toast.LENGTH_SHORT).show()
                            //validation check error
                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                        }
                    }

                }


            }

        })

    }

    fun callPlannedLeaves()
    {
        progressDialog.visibility = View.VISIBLE
        studentInfoCopy=ArrayList()
        studentInfoArrayList=ArrayList()
       // studentInfoArrayList.clear()
        mAbsenceListView.visibility=View.GONE
        mPickupListView.visibility=View.GONE
        mPlannedListView.visibility=View.VISIBLE

        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= AbsenceLeaveApiModel(sharedprefs.getStudentID(mContext)!!,0,20)
        val call: Call<AbsenceListModel> = ApiClient.getClient.plannedList(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<AbsenceListModel>{
            override fun onFailure(call: Call<AbsenceListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<AbsenceListModel>, response: Response<AbsenceListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    studentInfoCopy.addAll(response.body()!!.responseArray.requestList)
                    studentInfoArrayList=studentInfoCopy
                    Log.e("ArraySize",studentInfoArrayList.size.toString())
                    if (studentInfoArrayList.size>0)
                    {
                        mPlannedListView.visibility=View.VISIBLE
                        val studentInfoAdapter = RequestAbsenceRecyclerAdapter(studentInfoArrayList)
                        mPlannedListView.adapter = studentInfoAdapter
                    }
                    else{
                        Toast.makeText(mContext, mContext.resources.getString(R.string.no_reg_planned_leaves), Toast.LENGTH_SHORT).show()
                        mPlannedListView.visibility=View.GONE
                    }



                }else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callStudentLeaveInfo()
                    } else {
                        if (response.body()!!.status == 132) {
                            studentInfoCopy=ArrayList<AbsenceRequestListDetailModel>()
                            studentInfoArrayList.clear()
                            val studentInfoAdapter = RequestAbsenceRecyclerAdapter(studentInfoArrayList)
                            mPlannedListView.adapter = studentInfoAdapter
                            Toast.makeText(mContext,  mContext.resources.getString(R.string.no_reg_planned_leaves), Toast.LENGTH_SHORT).show()
                            //validation check error
                        } /*else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                        }*/
                    }

                }


            }

        })
    }
    override fun onResume() {
        super.onResume()
        studentNameTxt.text = sharedprefs.getStudentName(mContext)
        studentId= sharedprefs.getStudentID(mContext).toString()
        studentImg= sharedprefs.getStudentPhoto(mContext)!!
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
        if (select_val==0){
            callStudentLeaveInfo()
        }
        else if (select_val==1){
            callpickuplist_api()
        }
        else if (select_val==2)
        {
            callPlannedLeaves()
        }
    }
    fun showSuccessmailAlert(context: Context, message: String, msgHead: String) {
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
            val intent =Intent(activity,RequestabsenceActivity::class.java)
            activity?.startActivity(intent)
        }
        dialog.show()
    }
}




