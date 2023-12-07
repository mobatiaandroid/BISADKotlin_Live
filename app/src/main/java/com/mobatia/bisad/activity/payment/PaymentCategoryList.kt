package com.mobatia.bisad.activity.payment

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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.payment.adapter.PayCategoryListAdapter
import com.mobatia.bisad.activity.payment.model.PayCatDataList
import com.mobatia.bisad.activity.payment.model.PayCategoryModel
import com.mobatia.bisad.activity.payment.model.PaymentCategoriesApiModel
import com.mobatia.bisad.activity.payment.model.PaymentCategoryListModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrollEmailApiModel
import com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrollEmailModel
import com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipListModel
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

class PaymentCategoryList: AppCompatActivity() {
    lateinit var progressDialog: RelativeLayout
    lateinit var sharedprefs: PreferenceData
    lateinit var mContext: Context
    lateinit var titletext: TextView
    lateinit var back: RelativeLayout
    lateinit var home_icon: ImageView
    lateinit var studentSpinner: LinearLayout
    var studentListArrayList = ArrayList<StudentList>()

    lateinit var studImg: ImageView
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    lateinit var studentNameTxt: TextView
    lateinit var catListRec:RecyclerView
    lateinit var catList:ArrayList<PayCatDataList>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymentcategory)
        sharedprefs = PreferenceData()
        init()

    }

    private fun init(){
        mContext=this
        catList= ArrayList()
        titletext=findViewById(R.id.heading)
        back=findViewById(R.id.backRelative)
        home_icon=findViewById(R.id.logoClickImgView)
        studentSpinner = findViewById<LinearLayout>(R.id.studentSpinner)
        studImg = findViewById<ImageView>(R.id.imagicon)
        studentNameTxt =findViewById<TextView>(R.id.studentName)
        progressDialog = findViewById(R.id.progressDialog)
        catListRec=findViewById(R.id.mListView)
        titletext.setText("Payment")
        back.setOnClickListener {
            finish()
        }
        home_icon.setOnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck) {
            callStudentListApi()

        } else {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }
        studentSpinner.setOnClickListener(){
            showStudentList(mContext,studentListArrayList)
        }
    }
    fun callStudentListApi()
    {
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        studentListArrayList= ArrayList()
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                //val arraySize :Int = response.body()!!.responseArray.studentList.size
                if (response.body()!!.status==100)
                {
                    studentListArrayList=ArrayList()
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
                    //var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    catlistApi()

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
                catlistApi()


                //  Toast.makeText(activity, mStudentList.get(position).name, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
        dialog.show()
    }
    private fun catlistApi(){
        val token =sharedprefs.getaccesstoken(mContext)
        val paymentCategoriesBody = PaymentCategoriesApiModel( studentId)
        val call: Call<PayCategoryModel> =
            ApiClient.getClient.payment_categories(paymentCategoriesBody, "Bearer " + token)
        call.enqueue(object : Callback<PayCategoryModel> {
            override fun onFailure(call: Call<PayCategoryModel>, t: Throwable) {

                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)
            }

            override fun onResponse(call: Call<PayCategoryModel>, response: Response<PayCategoryModel>) {
                val responsedata = response.body()
                progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {

                        if (response.body()!!.status==100) {
                            var trn_payment=response.body()!!.responseArray.trn_no
                            PreferenceData().setTrnPayment(mContext,trn_payment)
                           catList=response.body()!!.responseArray.data
                            catListRec.layoutManager=LinearLayoutManager(mContext)
                            var categorytitle_adapter= PayCategoryListAdapter(mContext,catList)
                            catListRec.adapter=categorytitle_adapter

                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(mContext)
                                catlistApi()
                            } else {
                                if (response.body()!!.status==132) {
                                    catList=ArrayList()
                                    catListRec.layoutManager=LinearLayoutManager(mContext)
                                    var categorytitle_adapter= PayCategoryListAdapter(mContext,catList)
                                    catListRec.adapter=categorytitle_adapter
                                    showSuccessAlertnew(mContext,"No Data Found","Alert")
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })

    }

    override fun onResume() {
        super.onResume()
        callStudentListApi()
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
}