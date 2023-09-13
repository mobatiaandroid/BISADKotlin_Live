package com.mobatia.bisad.activity.settings.re_enrollment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.kyanogen.signatureview.SignatureView
import com.mobatia.bisad.R
import com.mobatia.bisad.WebviewActivity
import com.mobatia.bisad.activity.common.LoginActivity
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.parent_meetings.ReviewAppointmentsRecyclerViewActivity
import com.mobatia.bisad.activity.parent_meetings.adapter.ReviewAdapter
import com.mobatia.bisad.activity.settings.re_enrollment.adapter.ReEnrollAdapter
import com.mobatia.bisad.activity.settings.re_enrollment.model.EnrollmentStatusModel
import com.mobatia.bisad.activity.settings.re_enrollment.model.StudentsEnrollList
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.home.model.reenrollment.*
import com.mobatia.bisad.fragment.settings.model.DeleteAccountModel
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
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class ReEnrollmentActivity: AppCompatActivity() {
    lateinit var context: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var heading: TextView
    lateinit var back: RelativeLayout
    lateinit var home_icon: ImageView
    lateinit var enroll_rec:RecyclerView
    lateinit var progress:RelativeLayout
    lateinit var reEnrollOptionList:ArrayList<String>
    lateinit var stud_enroll_list:ArrayList<StudentsEnrollList>
    var apiCall: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_re_enrollment)
        context = this
        sharedprefs = PreferenceData()

        initializeUI()

    }
    private fun initializeUI(){

        heading=findViewById(R.id.heading)
        heading.text = "Re Enrolment"
        back=findViewById(R.id.backRelative)
        home_icon=findViewById(R.id.logoClickImgView)
        enroll_rec=findViewById(R.id.enroll_rec)
        progress=findViewById(R.id.progressDialog)
        stud_enroll_list= ArrayList()
        /*val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progress.startAnimation(aniRotate)*/

        back.setOnClickListener {
            finish()
        }
        home_icon.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        setting_re_api()


        enroll_rec.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (stud_enroll_list[position].status.equals("")){

                    if (stud_enroll_list[position].enrollment_status.equals("1")){
                        reEnrollApi(stud_enroll_list,position)
                        //showReEnroll()

                    }else{
                        showPopup(context,"Re Enrollment is currently not enabled.Please wait for further update","Alert")
                    }

                }else{
                    val dialog = Dialog(context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.re_enroll_sub_alert)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    var stud_name=dialog.findViewById<TextView>(R.id.stud_name)
                    stud_name.text = stud_enroll_list[position].name
                    var stud_section=dialog.findViewById<TextView>(R.id.section)
                    stud_section.text = stud_enroll_list[position].section
                    var name=dialog.findViewById<TextView>(R.id.nametxt)
                    name.text = stud_enroll_list[position].parent_name
                    var department=dialog.findViewById<TextView>(R.id.mailtxt)
                    department.text = stud_enroll_list[position].parent_email
                    var role=dialog.findViewById<TextView>(R.id.statustxt)
                    role.text =stud_enroll_list[position].status

                    var icon_image=dialog.findViewById<ImageView>(R.id.iconImageView)

                    var staffImage=stud_enroll_list[position].photo



                    if (staffImage.isNotEmpty()) {
                        context.let {
                            Glide.with(it)
                                .load(staffImage)
                                .into(icon_image)
                        }
                    }
                    else
                    {
                        icon_image.setBackgroundResource(R.drawable.student)
                    }



                    //dialog.dismiss()
                    dialog.show()
                }

            }

        })



    }
    private fun reEnrollApi(stud_enroll_list:ArrayList<StudentsEnrollList>,position:Int){
        Log.e("api","tr")
        reEnrollOptionList= ArrayList()
        val token = com.mobatia.bisad.fragment.home.sharedprefs.getaccesstoken(com.mobatia.bisad.fragment.home.mContext)
        var descrptn_reenroll:String=""
        val form_reenroll= GetreenrollmentApiModel(
            com.mobatia.bisad.fragment.home.sharedprefs.getUserID(
                com.mobatia.bisad.fragment.home.mContext
            )!!)
        val call: Call<GetreenrollmentModel> = ApiClient.getClient.getreenrollmentForm(form_reenroll,"Bearer "+token)
        call.enqueue(object : Callback<GetreenrollmentModel> {
            override fun onFailure(call: Call<GetreenrollmentModel>, t: Throwable) {
                //progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<GetreenrollmentModel>, response: Response<GetreenrollmentModel>) {
                //progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    var heading_reenroll=response.body()!!.responseArray.settingsdata.heading
                    if(response.body()!!.responseArray.settingsdata.description.isEmpty()){
                        Log.e("des","null")
                    }else{
                        descrptn_reenroll=response.body()!!.responseArray.settingsdata.description
                    }
                    // var descrptn_reenroll=response.body()!!.responseArray.settingsdata.description
                    var tandc_reenroll=response.body()!!.responseArray.settingsdata.t_and_c
                    var user_firstname=response.body()!!.responseArray.user.firstname
                    var user_lastname=response.body()!!.responseArray.user.last_name
                    var person_info_url=response.body()!!.responseArray.settingsdata.statement_url
                    var parent_name=user_firstname+" "+user_lastname
                    var user_email=response.body()!!.responseArray.user.email
                    var question=response.body()!!.responseArray.settingsdata.question
                    var reenrolldetail: ReEnrolldetail =
                        ReEnrolldetail(heading_reenroll,descrptn_reenroll,
                            tandc_reenroll,person_info_url,parent_name,user_email,question)
                    Log.e("reenrolldetail",reenrolldetail.parent_name)

                    reEnrollOptionList.addAll(response.body()!!.responseArray.settingsdata.options)
                    //showSuccessAlertnew(context,"alertre","suc")
                    showReEnroll(reenrolldetail,reEnrollOptionList,stud_enroll_list,position)


                }else if(response.body()!!.status.equals("116"))
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(com.mobatia.bisad.fragment.home.mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                        reEnrollApi(stud_enroll_list,position)
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status.toInt(),
                        com.mobatia.bisad.fragment.home.mContext
                    )
                }

            }

        })

    }
    private fun showReEnroll(reEnrolldetail: ReEnrolldetail, reEnrollOptionList: ArrayList<String>,
                             stud_enroll_list:ArrayList<StudentsEnrollList>,position: Int){
        Log.e("dial","show")
        val d = Dialog(context)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        d.setCancelable(false)
        d.setContentView(R.layout.dialogue_re_enrollment)

        // var total_count:Int=studDetailList.size-1
        var page_count:Int=0

        var check:Int=0
        var question=d.findViewById<TextView>(R.id.question)
        var scrollView=d.findViewById<ScrollView>(R.id.scroll)
        var close_img=d.findViewById<ImageView>(R.id.close_img)
        var header=d.findViewById<TextView>(R.id.header)
        var prev_btn=d.findViewById<LinearLayout>(R.id.prev_linear)
        var nxt_btn=d.findViewById<LinearLayout>(R.id.nxt_linear)
        var sub_btn=d.findViewById<Button>(R.id.sub_btn)
        var mailIcon=d.findViewById<LinearLayout>(R.id.linear_mail)
        var terms_and_condtns=d.findViewById<Button>(R.id.terms_conditions)
        var personal_info=d.findViewById<Button>(R.id.personal_info)
        var save=d.findViewById<TextView>(R.id.save)
        var image_view=d.findViewById<ImageView>(R.id.image_view)
        var stud_img=d.findViewById<ImageView>(R.id.stud_img)
        var stud_name=d.findViewById<TextView>(R.id.stud_name)
        var stud_class=d.findViewById<TextView>(R.id.stud_class)
        var date_field=d.findViewById<EditText>(R.id.textField_date)
        var descrcrptn=d.findViewById<TextView>(R.id.descrptn_txt)
        var parent_name=d.findViewById<EditText>(R.id.textField_parentName)
        var parent_email=d.findViewById<EditText>(R.id.textField_parentEmail)
        var spinnerList=d.findViewById<Spinner>(R.id.spinnerlist)
        var option_txt=d.findViewById<TextView>(R.id.option_txt)
        var clear=d.findViewById<TextView>(R.id.clear)
        var sign_linear=d.findViewById<ConstraintLayout>(R.id.sign_linear)
        var signatureView=d.findViewById<SignatureView>(R.id.signature_view)
        var dropdown_btn=d.findViewById<ImageView>(R.id.dropdown_btn)
        var sign_btn=d.findViewById<Button>(R.id.signature_btn)
        var dropdownList:ArrayList<String>
        dropdownList= ArrayList()
        // var studDetailList:ArrayList<StudentReEnrollList>
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val current = formatter.format(time)
        Log.e("curnttime",current.toString())

        var myCalendar= Calendar.getInstance()
        var currentDate= Calendar.getInstance().time
        var year=myCalendar[java.util.Calendar.YEAR]
        var month=myCalendar[java.util.Calendar.MONTH]

        var day=myCalendar[java.util.Calendar.DAY_OF_MONTH]
        var radioButton=d.findViewById<RadioButton>(R.id.check_btn)
        var radioButton_info=d.findViewById<RadioButton>(R.id.check_btn_info)
        var crnt_date=day.toString()+"/"+month+1+"/"+year
        var header_txt= year.toString() + " Re-enrolment and NAE Terms & Conditions"
        var reEnrollsave:ArrayList<ReEnrollSaveModel>
        reEnrollsave=ArrayList()
        var reEnrollsubmit:ArrayList<ReEnrollSubModel>
        reEnrollsubmit=ArrayList()
        var m1=ReEnrollSaveModel("","")
        reEnrollsave.add(0,m1)

        header.text = reEnrolldetail.heading
        descrcrptn.text = reEnrolldetail.description
        date_field.setText(crnt_date)


        stud_name.text = stud_enroll_list[position].name
        stud_class.text = stud_enroll_list[position].section
        var stud_photo= stud_enroll_list[position].photo
        var stud_id=stud_enroll_list[position].id
        dropdownList.add(0,"Please Select")
        for (i in 1..reEnrollOptionList.size) {

            dropdownList.add(i, reEnrollOptionList[i-1])
        }

        nxt_btn.visibility=View.GONE
        prev_btn.visibility=View.GONE
        sub_btn.visibility=View.VISIBLE

        var sp_adapter = ArrayAdapter(
            context,
            R.layout.spinner_textview, dropdownList)
        spinnerList.adapter = sp_adapter
        spinnerList.setSelection(0)

        spinnerList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                var optionlistSize=dropdownList.size-1
                for (i in 1..optionlistSize){
                    Log.e("opt",dropdownList[i])
                    if (selectedItem==dropdownList[i].toString()){
                        Log.e("setcheck","1")
                        reEnrollsave[page_count].status=dropdownList[i]
                        check=1
                    }else if (selectedItem==dropdownList[0]){
                        Log.e("setcheck","0")
                        check=0
                        reEnrollsave[page_count].status=""
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        if(!stud_photo.equals(""))
        {
            Glide.with(context)
                .load(stud_photo)
                .placeholder(R.drawable.student)
                .error(R.drawable.student)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(CircleCrop())
                .into(stud_img)
        }
        else{
            stud_img.setImageResource(R.drawable.student)
        }

        if (reEnrolldetail.parent_name.isNotEmpty()){
            parent_name.setText(reEnrolldetail.parent_name)
        }
        if (reEnrolldetail.parent_email.isNotEmpty()){
            parent_email.setText(reEnrolldetail.parent_email)
        }
        if (reEnrolldetail.question.isNotEmpty()){
            question.text = reEnrolldetail.question
        }
        radioButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!radioButton.isSelected) {
                    Log.e("rad","sel")
                    radioButton.isChecked = true
                    // radioButton.isSelected = true

                } else {
                    Log.e("rad","sel_false")
                    radioButton.isChecked = false
                    radioButton.isSelected = false

                }
                if (!radioButton.isChecked){
                    radioButton.isChecked = false
                    radioButton.isSelected = false
                }else{
                    radioButton.isSelected = true
                }
            }
        })
        radioButton_info.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!radioButton_info.isSelected) {
                    radioButton_info.isChecked = true
                    //radioButton_info.isSelected = true

                } else {
                    radioButton_info.isChecked = false
                    radioButton_info.isSelected = false

                }
                if (!radioButton_info.isChecked){
                    radioButton_info.isChecked = false
                    radioButton_info.isSelected = false
                }else{
                    radioButton_info.isSelected = true
                }
            }
        })
        dropdown_btn.setOnClickListener {

            sign_linear.visibility = View.GONE
            dropdown_btn.visibility = View.GONE
        }
        option_txt.setOnClickListener {
            option_txt.visibility = View.GONE
            spinnerList.visibility = View.VISIBLE
        }
        terms_and_condtns.setOnClickListener {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("Url",reEnrolldetail.tandc)
            context.startActivity(intent)

        }
        personal_info.setOnClickListener {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("Url",reEnrolldetail.pers_info_url)
            context.startActivity(intent)
        }
        mailIcon.setOnClickListener {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
            val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
            var text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
            var text_content = dialog.findViewById<View>(R.id.text_content) as EditText

            submitButton.setOnClickListener {
                if (text_dialog.text.toString().trim().equals("")) {
                    InternetCheckClass.showErrorAlert(
                        context,
                        "Please enter your subject",
                        "Alert"
                    )

                } else {
                    if (text_content.text.toString().trim().equals("")) {
                        InternetCheckClass.showErrorAlert(
                            context,
                            "Please enter your content",
                            "Alert"
                        )

                    } else {
                        // progressDialog.visibility = View.VISIBLE
                        val aniRotate: Animation =
                            AnimationUtils.loadAnimation(context, R.anim.linear_interpolator)
                        // progressDialog.startAnimation(aniRotate)
                        var internetCheck = InternetCheckClass.isInternetAvailable(context)
                        if (internetCheck) {
                            reEnrollMailApi( text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),dialog)

                        } else {
                            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                        }
                    }
                }
            }
            dialogCancelButton.setOnClickListener { dialog.dismiss()
            }
            dialog.show()
        }
        sub_btn.setOnClickListener {

            if (check==0){
                Log.e("check","plsselct")
                radioButton.isChecked = false
                radioButton.isSelected=false
                radioButton_info.isChecked=false
                radioButton_info.isSelected=false

                if (reEnrollsave[0].status.isNotEmpty()){
                    reEnrollsave[0].student_id=stud_enroll_list[position].id
                }else{
                    Log.e("nostud","id")
                }
                if (reEnrollsave[0].student_id.isNotEmpty()&&reEnrollsave[0].status.isNotEmpty()){
                    var r1 =
                        ReEnrollSubModel(reEnrollsave[0].student_id, reEnrollsave[0].status)
                    reEnrollsubmit.add(0, r1)
                }

                if (reEnrollsubmit.size>0){
                    showerror(context,"Do you want to Submit?","Alert",reEnrollsubmit,d)
                }else{
                    showRenrollnoData(
                        context,"You didn't enter any data of your child.Please Enter data and Submit",
                        "Alert",d)
                    //showSuccessAlert(mContext,"No Options Selected","Alert")
                }

                // saveReenrollApi(reEnrollsubmit,d)
            }else{
                if (radioButton.isChecked) {
                    if (radioButton_info.isChecked){
                    Log.e("check", "checked")
                    if (reEnrollsave[0].status.isNotEmpty()) {
                        reEnrollsave[0].student_id = stud_enroll_list[position].id
                    } else {
                        Log.e("nostud", "id")
                    }
                    if (reEnrollsave[0].student_id.isNotEmpty() && reEnrollsave[0].status.isNotEmpty()) {
                        var r1 =
                            ReEnrollSubModel(reEnrollsave[0].student_id, reEnrollsave[0].status)
                        reEnrollsubmit.add(0, r1)
                    }

                    showerror(context, "Do you want to Submit?", "Alert", reEnrollsubmit, d)
                    // saveReenrollApi(reEnrollsubmit, d)
                }else{
                        InternetCheckClass.showErrorAlert(context,"Make sure you have confirmed all the declarations","Alert")

                    }
                }else{
                    InternetCheckClass.showErrorAlert(context,"Make sure you have confirmed all the declarations","Alert")
                }

            }



        }

        close_img.setOnClickListener {
            com.mobatia.bisad.fragment.home.sharedprefs.setreenrollvalue(context,"2")
            d.dismiss()
        }
        Log.e("upto","d")
        d.show()
        Log.e("upto","daftr")
    }
private fun setting_re_api(){
    stud_enroll_list= ArrayList()
    progress.visibility = View.VISIBLE

    val token = sharedprefs.getaccesstoken(context)
    val call: Call<EnrollmentStatusModel> = ApiClient.getClient.get_enrollment_status("Bearer "+token)
    call.enqueue(object : Callback<EnrollmentStatusModel> {
        override fun onFailure(call: Call<EnrollmentStatusModel>, t: Throwable) {
            Log.e("Failed", t.localizedMessage)
        }
        override fun onResponse(call: Call<EnrollmentStatusModel>, response: Response<EnrollmentStatusModel>) {
            val responsedata = response.body()
            Log.e("Response Signup", responsedata.toString())
            if (responsedata!!.status==100) {
                progress.visibility = View.GONE

Log.e("st","100")
                stud_enroll_list.addAll(responsedata.responseArray.students)
                enroll_rec.layoutManager= LinearLayoutManager(context)
                var re_enroll_adapter=
                    ReEnrollAdapter(context,stud_enroll_list)
                enroll_rec.adapter=re_enroll_adapter

            }else if (response.body()!!.status == 116) {
                if (apiCall != 4) {
                    apiCall = apiCall + 1
                    AccessTokenClass.getAccessToken(context)
                    //delAccountApi()
                } else {

                    showSuccessAlertnew(
                        context,
                        "Something went wrong.Please try again later",
                        "Alert"
                    )

                }
            } else {
                if (response.body()!!.status == 103) {
                    //validation check error
                } else {
                    //check status code checks
                    InternetCheckClass.checkApiStatusError(response.body()!!.status, context)
                }
            }
        }

    })
}
    private fun reEnrollMailApi(title: String,
                                message: String,
                                dialog: Dialog
    ){
        val token = com.mobatia.bisad.fragment.home.sharedprefs.getaccesstoken(mContext)
        val sendMailBody = ReEnrollEmailApiModel( title, message)
        val call: Call<ReEnrollEmailModel> =
            ApiClient.getClient.re_enrollment_mailhelp(sendMailBody, "Bearer " + token)
        call.enqueue(object : Callback<ReEnrollEmailModel> {
            override fun onFailure(call: Call<ReEnrollEmailModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<ReEnrollEmailModel>, response: Response<ReEnrollEmailModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                Log.e("Response Signup", responsedata.toString())
                if (responsedata != null) {
                    try {


                        if (response.body()!!.status==100) {
                            dialog.dismiss()
                            showSuccessmailAlert(
                                context,
                                "Email sent Successfully ",
                                "Success",
                                dialog
                            )
                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                reEnrollMailApi( title,
                                    message,dialog)
                            } else {
                                if (response.body()!!.status==103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, context)
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
    fun showSuccessmailAlert(context: Context, message: String, msgHead: String, mdialog: Dialog) {
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
            mdialog.dismiss()
        }
        dialog.show()
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
    private fun showPopup(context: Context,message : String,msgHead : String){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.exclamationicon)
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
    private fun showerror(context: Context,message : String,msgHead : String,
                          reEnrollsubmit:ArrayList<ReEnrollSubModel>,d:Dialog)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
            saveReenrollApi(reEnrollsubmit,dialog,d)
            dialog.dismiss()

        }
        btn_Cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun showRenrollnoData(context: Context,message : String,msgHead : String,d:Dialog
    )
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.exclamationicon)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        // var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {

            //getReenrollForm()
            dialog.dismiss()
            //d.dismiss()

        }
        /* btn_Cancel.setOnClickListener {
             dialog.dismiss()
         }*/
        dialog.show()
    }
    fun  saveReenrollApi(reEnrollsubmit:ArrayList<ReEnrollSubModel>,dlg:Dialog,d:Dialog){

        val token = com.mobatia.bisad.fragment.home.sharedprefs.getaccesstoken(mContext)
        Log.e("size",reEnrollsubmit.size.toString())
        val save_reenroll: SaveReenrollmentApiModel? = SaveReenrollmentApiModel(reEnrollsubmit)
        Log.e("save",save_reenroll!!.json.toString())

        val call: Call<SavereenrollmentModel> = ApiClient.getClient.savereenrollmentForm(save_reenroll,"Bearer "+token)
        call.enqueue(object : Callback<SavereenrollmentModel>{
            override fun onFailure(call: Call<SavereenrollmentModel>, t: Throwable) {
                //progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<SavereenrollmentModel>, response: Response<SavereenrollmentModel>) {
                //progressDialog.visibility = View.GONE

                if (response.body()!!.status.equals("100"))
                {
                    Log.e("re","save")

                    showSuccessReenrollAlert(context, "Successfully submitted the Re Enrollment", "Thankyou",dlg,d)
                    setting_re_api()

                }else if(response.body()!!.status.equals("116"))
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(context)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(context)
                        saveReenrollApi(reEnrollsubmit,dlg,d)
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status.toInt(),mContext)
                }

            }

        })

    }
    fun showSuccessReenrollAlert(context: Context, message: String, msgHead: String,dlg:Dialog,d:Dialog) {
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
        iconImageView.setImageResource(R.drawable.tick)
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            dlg.dismiss()
            d.dismiss()


        }
        dialog.show()
    }
}