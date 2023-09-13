package com.mobatia.bisad.fragment.parents_meeting

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
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.parent_meetings.ParentsEveningCalendarActivity
import com.mobatia.bisad.activity.parent_meetings.ParentsEveninginfoActivity
import com.mobatia.bisad.activity.parent_meetings.ReviewAppointmentsRecyclerViewActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.fragment.teacher_contact.adapter.StaffListAdapter
import com.mobatia.bisad.fragment.teacher_contact.model.StaffInfoDetail
import com.mobatia.bisad.fragment.teacher_contact.model.StaffListApiModel
import com.mobatia.bisad.fragment.teacher_contact.model.StaffListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParentsMeetingFragment:Fragment() {
    //lateinit var progressDialog: RelativeLayout
    lateinit var titleTextView: TextView
    lateinit var infoImg:ImageView
    lateinit var studentName: String
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var studentrelatv:RelativeLayout
    lateinit var student_image:ImageView
    lateinit var studImg: ImageView
    lateinit var studentImg: String
    var student_class:String=""
    lateinit var staffName: String
    lateinit var staffrole: String
    lateinit var staffId: String
    lateinit var staffImg: String
    lateinit var staffEmail: String
    lateinit var staffRelative:RelativeLayout
    lateinit var staff_image:ImageView
    lateinit var staffNameTV: TextView
    lateinit var studentname:TextView
    var studentListArrayList = ArrayList<StudentList>()
    lateinit var staffListArray: ArrayList<StaffInfoDetail>
    lateinit var nxtbtn:ImageView
    lateinit var review_img:ImageView
    var studentId: String=""
    var apiCall:Int=0
    var apiCallDetail:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parentsmeeting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedprefs = PreferenceData()
        mContext = requireContext()

        initializeUI()


        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck) {
            //callCategoryListApi()
        } else {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }
    }
    private fun initializeUI(){
        mContext=requireContext()
        titleTextView = view!!.findViewById(R.id.titleTextView) as TextView
        titleTextView.text = "Parents Meeting"
        infoImg = view!!.findViewById(R.id.infoImg)
        //progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        studentrelatv=view!!.findViewById(R.id.studentRelative)
        student_image=view!!.findViewById(R.id.selectStudentImgView)
        staff_image=view!!.findViewById(R.id.selectStaffImgView)
        studentname=view!!.findViewById(R.id.studentNameTV)
        staffRelative=view!!.findViewById(R.id.staffRelative)
        staffListArray= ArrayList()
        staffNameTV=view!!.findViewById(R.id.staffNameTV)
        nxtbtn=view!!.findViewById(R.id.next)
        review_img=view!!.findViewById(R.id.reviewImageView)

        onclick()
        callStudentListApi()

Log.e("time", sharedprefs.getIsFirstTimeInPE(mContext).toString())
        if (sharedprefs.getIsFirstTimeInPE(mContext)) {
            sharedprefs.setIsFirstTimeInPE(mContext, false)
            val mintent = Intent(mContext, ParentsEveninginfoActivity::class.java)
            mintent.putExtra("type", 1)
            mContext.startActivity(mintent)
            Log.e("timeafter",sharedprefs.getIsFirstTimeInPE(mContext).toString())
        }
    }
private fun onclick(){

    infoImg.setOnClickListener(View.OnClickListener {
        val mIntent = Intent(mContext, ParentsEveninginfoActivity::class.java)
        mContext.startActivity(mIntent)
    })
    student_image.setOnClickListener {
        showStudentList(mContext, studentListArrayList)

    }
    staff_image.setOnClickListener {
        showStaffList(mContext, staffListArray)
    }
    nxtbtn.setOnClickListener {
        val mIntent = Intent(mContext, ParentsEveningCalendarActivity::class.java)
        mIntent.putExtra("studId",studentId)
        mIntent.putExtra("studName",studentName)
        mIntent.putExtra("studClass",student_class)
        mIntent.putExtra("staffId",staffId)
        mIntent.putExtra("staffName",staffName)
        mContext.startActivity(mIntent)
    }
    review_img.setOnClickListener {
        val mIntent = Intent(mContext, ReviewAppointmentsRecyclerViewActivity::class.java)
        mContext.startActivity(mIntent)
    }
}
   private fun callStudentListApi() {
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer " + token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<StudentListModel>,
                response: Response<StudentListModel>
            ) {
                if (response.body()!!.status == 100) {
                    studentListArrayList.addAll(response.body()!!.responseArray.studentList)

                    }

                }
            })
        }
  private fun showStudentList(context: Context, mStudentList: ArrayList<StudentList>) {
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
//        val studentAdapter = StudentListAdapter(mContext,mStudentList)
//        studentListRecycler.adapter = studentAdapter
      btn_dismiss.setOnClickListener()
      {
          dialog.dismiss()
      }

      studentListRecycler.addOnItemClickListener(object : OnItemClickListener {
          override fun onItemClicked(position: Int, view: View) {
              // Your logic
              studentName = studentListArrayList[position].name
              studentId = studentListArrayList[position].id
              studentImg = studentListArrayList[position].photo
              student_class=studentListArrayList[position].studentClass
              studentname.text = studentName
              staffRelative.visibility = VISIBLE
              staff_image.setImageResource(R.drawable.addiconinparentsevng)
              staffNameTV.text = "Staff Name:-"
              nxtbtn.visibility = View.GONE
              stafflistcall(studentId)
              if (studentImg != "") {
                  Glide.with(mContext) //1
                      .load(studentImg)
                      .placeholder(R.drawable.student)
                      .error(R.drawable.student)
                      .skipMemoryCache(true) //2
                      .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                      .transform(CircleCrop()) //4
                      .into(student_image)
              } else {
                  student_image.setImageResource(R.drawable.student)
              }
              dialog.dismiss()
          }
      })
      dialog.show()
  }

    private fun stafflistcall(studentId:String){
        staffListArray = ArrayList<StaffInfoDetail>()
        val token = sharedprefs.getaccesstoken(mContext)
        var staffBody = StaffListApiModel(studentId)
        val call: Call<StaffListModel> = ApiClient.getClient.staffList(staffBody, "Bearer " + token)
        call.enqueue(object : Callback<StaffListModel> {
            override fun onFailure(call: Call<StaffListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<StaffListModel>,
                response: Response<StaffListModel>
            ) {
                //        val arraySize: Int = response.body()!!.responseArray.staff_info.size
                if (response.body()!!.status == 100) {
                    if (response.body()!!.responseArray.staff_info.size > 0) {
                        staffListArray.addAll(response.body()!!.responseArray.staff_info)


                    }else{
                        Toast.makeText(mContext, "No Staffs Found", Toast.LENGTH_SHORT).show()
                    }
                }  else {
                    if (response.body()!!.status == 103) {
                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                    }
                }
            }
        })

    }
    fun showStaffList(context: Context, mStaffList: ArrayList<StaffInfoDetail>) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_student_list)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var btn_dismiss = dialog.findViewById(R.id.btn_dismiss) as Button
        var studentListRecycler = dialog.findViewById(R.id.studentListRecycler) as RecyclerView
        iconImageView.setImageResource(R.drawable.staff)
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
        val staffAdapter = StaffListAdapter(mStaffList, mContext)
        studentListRecycler.adapter = staffAdapter
        btn_dismiss.setOnClickListener()
        {
            dialog.dismiss()
        }
        studentListRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                dialog.dismiss()
                staffName = mStaffList.get(position).name
                staffrole = mStaffList.get(position).role
                staffImg = mStaffList.get(position).staff_photo
                staffId = mStaffList.get(position).id
                staffEmail = mStaffList.get(position).email
                staffNameTV.text = staffName
                nxtbtn.visibility = VISIBLE
                if (!staffImg.equals("")) {
                    Glide.with(mContext) //1
                        .load(staffImg)
                        .placeholder(R.drawable.staff)
                        .error(R.drawable.staff)
                        .skipMemoryCache(true) //2
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                        .transform(CircleCrop()) //4
                        .into(staff_image)
                    dialog.dismiss()
                } else {
                    staff_image.setImageResource(R.drawable.staff)
                    dialog.dismiss()
                }
                dialog.dismiss()
            }
        })
        dialog.show()
    }
}
