package com.mobatia.bisad.activity.canteen

import android.app.Activity
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
import com.mobatia.bisad.activity.canteen.adapter.PaymentHistoryAdapter
import com.mobatia.bisad.activity.canteen.model.payment_history.PaymentHistoryListModel
import com.mobatia.bisad.activity.canteen.model.topup.WalletAmountApiModel
import com.mobatia.bisad.activity.canteen.model.topup.WalletAmountModel
import com.mobatia.bisad.activity.canteen.model.wallethistory.CreditHisListModel
import com.mobatia.bisad.activity.canteen.model.wallethistory.WalletHistoryApiModel
import com.mobatia.bisad.activity.canteen.model.wallethistory.WalletHistoryModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
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
import java.util.*
import kotlin.collections.ArrayList

class PaymentHistory_Activity : AppCompatActivity() {
    lateinit var sharedprefs: PreferenceData
    lateinit var wallethistory_list: ArrayList<CreditHisListModel>
    lateinit var nContext: Context
    private lateinit var logoClickImg: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var back: ImageView
    //lateinit var amount: EditText
    //lateinit var addToWallet: Button
    lateinit var progress: ProgressBar
    lateinit var studentlist: ArrayList<String>
    //lateinit var studentname: TextView
    lateinit var dropdown: LinearLayout
    lateinit var title: TextView

    lateinit var studImg: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var studentSpinner: LinearLayout
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    var studentListArrayList = ArrayList<StudentList>()
    var payAmount = ""
    var merchantOrderReference = ""
    var canteen_response = ""
    var Error = ""
    var topup_limit = ""
    var order_id = ""
    var stud_id: String=""
    var studClass = ""
    var orderId = ""
    lateinit var activity: Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_paymenthistory)
        sharedprefs = PreferenceData()
        initfn()
        callStudentListApi()


    }

    private fun initfn() {
        nContext = this
        activity=this
        logoClickImg = findViewById(R.id.logoclick)
        back = findViewById(R.id.back)
        //addToWallet = findViewById(R.id.addToWallet)
       // amount = findViewById(R.id.et_amount)
        studentlist = ArrayList()
        wallethistory_list = ArrayList()
        studentSpinner = findViewById(R.id.studentSpinner)
        studentNameTxt = findViewById(R.id.studentName)!!
        studImg = findViewById(R.id.imagicon)
        recyclerView = findViewById(R.id.history_rec)
        progress = findViewById(R.id.progressDialog)
        title = findViewById(R.id.titleTextView)

        title.text = "Payment History"
        /*val aniRotate: Animation =
            AnimationUtils.loadAnimation(nContext, R.anim.linear_interpolator)
        progress.startAnimation(aniRotate)*/
        back.setOnClickListener {
            finish()
        }
        logoClickImg.setOnClickListener {
            val intent = Intent(nContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        studentSpinner.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                showStudentList(nContext, studentListArrayList)

            }
        })
       // walletHistory()

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
                //progressDialog.visibility=View.VISIBLE
                val aniRotate: Animation =
                    AnimationUtils.loadAnimation(nContext, R.anim.linear_interpolator)
                //progressDialog.startAnimation(aniRotate)

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
                //progressDialog.visibility = View.VISIBLE
                walletHistory()
                //  Toast.makeText(activity, mStudentList.get(position).name, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
        dialog.show()
    }
    fun callStudentListApi()
    {
        progress.visibility=View.VISIBLE
        val token = sharedprefs.getaccesstoken(nContext)
        val call: Call<StudentListModel> = ApiClient(nContext).getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                progress.visibility=View.GONE
                CommonFunctions.faliurepopup(nContext)
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                progress.visibility=View.GONE
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
                    var internetCheck = InternetCheckClass.isInternetAvailable(nContext)
                    if (internetCheck)
                    {
                        walletHistory()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                    //callStudentInfoApi()
                }


            }

        })
    }
    private fun walletHistory(){


        val token = PreferenceData().getaccesstoken(nContext)
        val paymentSuccessBody = WalletHistoryApiModel(PreferenceData().getStudentID(nContext).toString(),"0","50")
        val call: Call<WalletHistoryModel> =
            ApiClient(nContext).getClient.get_wallet_history(paymentSuccessBody, "Bearer " + token)
        call.enqueue(object : Callback<WalletHistoryModel> {
            override fun onFailure(call: Call<WalletHistoryModel>, t: Throwable) {
               // progress.visibility=View.GONE

                CommonFunctions.faliurepopup(nContext)
            }

            override fun onResponse(call: Call<WalletHistoryModel>, response: Response<WalletHistoryModel>) {
               // progress.visibility=View.GONE
                val responsedata = response.body()

                if (responsedata != null) {
                    try {

                        if (response.body()!!.status==100) {

                           // progress.visibility=View.GONE
                            wallethistory_list= ArrayList()
                            wallethistory_list.addAll(response.body()!!.responseArray.credit_history)
                            if (wallethistory_list.size>0)
                            {
                                recyclerView.visibility=View.VISIBLE
                                recyclerView.layoutManager = LinearLayoutManager(nContext)
                                recyclerView.adapter = PaymentHistoryAdapter(wallethistory_list,nContext)
                            }
                            else
                            {
                                recyclerView.visibility=View.GONE
                                showSuccessAlertnew(nContext,"No history available","Alert")
                            }


                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(nContext)
                                walletHistory()
                            } else {
                                if (response.body()!!.status==132) {
                                    //validation check error
                                    recyclerView.visibility=View.GONE
                                    showSuccessAlertnew(nContext,"No data available","Alert")
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
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
    override fun onResume() {
        super.onResume()
        if (!CommonFunctions.runMethod.equals("Dev")) {
            if (CommonFunctions.isDeveloperModeEnabled(nContext)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
        }
    }
}