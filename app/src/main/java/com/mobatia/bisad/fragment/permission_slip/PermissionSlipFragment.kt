package com.mobatia.bisad.fragment.permission_slip

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.model.PickupListApiModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.permission_slip.adapter.FormslistAdapter
import com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipListApiModel
import com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipListModel
import com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipModel
import com.mobatia.bisad.fragment.report_absence.adapter.PickuplistAdapter
import com.mobatia.bisad.fragment.report_absence.model.AbsenceRequestListDetailModel
import com.mobatia.bisad.fragment.report_absence.model.PickupModel
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

class PermissionSlipFragment : Fragment(){
    lateinit var progressDialog: RelativeLayout
    lateinit var titleTextView: TextView
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var studentSpinner: LinearLayout
    var studentListArrayList = ArrayList<StudentList>()
    lateinit var formslist:ArrayList<PermissionSlipListModel>
    lateinit var studImg: ImageView
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    lateinit var studentNameTxt: TextView
    lateinit var forms_recycler:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_permission_slip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedprefs = PreferenceData()
        mContext = requireContext()

        initializeUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck) {
            callStudentListApi()

        } else {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }
    }
    private fun initializeUI(){
        mContext=requireContext()
        titleTextView = view!!.findViewById(R.id.titleTextView) as TextView
        titleTextView.text = "Permission Slips"
        formslist= ArrayList()
        studentSpinner = view!!.findViewById<LinearLayout>(R.id.studentSpinner)
        studImg = view!!.findViewById<ImageView>(R.id.studImg)
        studentNameTxt = view!!.findViewById<TextView>(R.id.studentName)
        forms_recycler=view!!.findViewById(R.id.forms_rec)
        progressDialog = view!!.findViewById(R.id.progressDialog)
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)



        studentSpinner.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showStudentList(mContext,studentListArrayList)

            }
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
                //val arraySize :Int = response.body()!!.responseArray.studentList.size
                if (response.body()!!.status==100)
                {
                    progressDialog.visibility = View.GONE
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
                    formslistApi()
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
                formslistApi()

                //  Toast.makeText(activity, mStudentList.get(position).name, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
        dialog.show()
    }
    private fun formslistApi(){
        progressDialog.visibility = View.VISIBLE
        formslist=ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val list_permissionSlip= PermissionSlipListApiModel("0","20",sharedprefs.getStudentID(mContext).toString())
        val call: Call<PermissionSlipModel> = ApiClient.getClient.permissnslipList(list_permissionSlip,"Bearer "+token)
        call.enqueue(object : Callback<PermissionSlipModel>{
            override fun onFailure(call: Call<PermissionSlipModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<PermissionSlipModel>, response: Response<PermissionSlipModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    formslist.addAll(response.body()!!.responseArray.request)
                    if (response.body()!!.responseArray.request.size > 0){
                        forms_recycler.layoutManager=LinearLayoutManager(mContext)
                        var forms_adapter= FormslistAdapter(mContext,formslist)
                        forms_recycler.adapter=forms_adapter
                    }else{
                        formslist=ArrayList()
                        forms_recycler.layoutManager=LinearLayoutManager(mContext)
                        var forms_adapter= FormslistAdapter(mContext,formslist)
                        forms_recycler.adapter=forms_adapter
                        //showerror(mContext,"No Data Found","Alert")
                        Toast.makeText(mContext, "No Permission Slips Found", Toast.LENGTH_SHORT).show()
                    }


                }else if(response.body()!!.status.equals("116"))
                    {
                        var internetCheck = InternetCheckClass.isInternetAvailable(com.mobatia.bisad.fragment.home.mContext)
                        if (internetCheck){
                            AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)

                            formslistApi()
                        }else{
                            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else {
                    if (response.body()!!.status == 132) {
                        formslist=ArrayList()
                        forms_recycler.layoutManager=LinearLayoutManager(mContext)
                        var forms_adapter= FormslistAdapter(mContext,formslist)
                        forms_recycler.adapter=forms_adapter
                        Toast.makeText(mContext, "No Permission Slips Found", Toast.LENGTH_SHORT).show()
                        //validation check error
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
    override fun onResume() {
        super.onResume()

        formslistApi()
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

    }
}