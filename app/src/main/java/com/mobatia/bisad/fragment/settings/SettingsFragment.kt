package com.mobatia.bisad.fragment.settings

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.common.LoginActivity
import com.mobatia.bisad.activity.home.model.HealthInsuranceDetailAPIModel
import com.mobatia.bisad.activity.settings.re_enrollment.ReEnrollmentActivity
import com.mobatia.bisad.activity.settings.termsofservice.TermsOfServiceActivity
import com.mobatia.bisad.activity.settings.tutorial.TutorialActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.model.StudentListDataCollection
import com.mobatia.bisad.fragment.home.model.datacollection.HealthInsuranceDetailModel
import com.mobatia.bisad.fragment.home.model.datacollection.KinDetailApiModel
import com.mobatia.bisad.fragment.home.model.datacollection.KinDetailsModel
import com.mobatia.bisad.fragment.home.model.datacollection.OwnContactModel
import com.mobatia.bisad.fragment.home.model.datacollection.OwnDetailsModel
import com.mobatia.bisad.fragment.home.model.datacollection.PassportApiModel
import com.mobatia.bisad.fragment.home.model.datacollection.PassportDetailModel
import com.mobatia.bisad.fragment.settings.adapter.SettingsRecyclerAdapter
import com.mobatia.bisad.fragment.settings.model.ChangePasswordApiModel
import com.mobatia.bisad.fragment.settings.model.DeleteAccountModel
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


class SettingsFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var sharedprefs: PreferenceData
    lateinit var mSettingsListView: RecyclerView
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var mSettingsArrayListRegistered : ArrayList<String>
    lateinit var mSettingsArrayListGuest: ArrayList<String>
    lateinit var progress: ProgressBar
    var start:Int=0
    var limit:Int=15
    lateinit var ownContactArrayList: ArrayList<OwnDetailsModel>
    lateinit var kinDetailArrayList: ArrayList<KinDetailsModel>
    lateinit var passportArrayList: ArrayList<PassportDetailModel>
    lateinit var healthDetailArrayList: ArrayList<HealthInsuranceDetailModel>
    lateinit var ownContactDetailSaveArrayList: ArrayList<OwnContactModel>
    lateinit var kinDetailSaveArrayList: ArrayList<KinDetailApiModel>
    lateinit var passportSaveArrayList: ArrayList<PassportApiModel>
    lateinit var healthSaveArrayList: ArrayList<HealthInsuranceDetailAPIModel>
    var apiCall: Int = 0
    var apiCallDetail: Int = 0
    private var previousTriggerType: Int = 0
    private val PASSWORD_PATTERN = "^" +
            "(?=.*[@#$%^&+=])" +  // at least 1 special character
            "(?=\\S+$)" +  // no white spaces
            ".{8,}" +  // at least 8 characters
            "$"
    var PASSWORD_PATTERN1="^" +
            "(?=.*[@#$%^&+=])" +
            "$"
    var PASSWORD_PATTERN2="^\\s*$"
    var PASSWORD_PATTERN3="^" +
            ".{8,}"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        mSettingsArrayListRegistered= ArrayList()
        mSettingsArrayListGuest=ArrayList()
        if(sharedprefs.getUserCode(mContext).equals(""))
        {
            mSettingsArrayListGuest.add("Change App Settings")
            mSettingsArrayListGuest.add("Terms of Service")
            mSettingsArrayListGuest.add("Feedback")
            mSettingsArrayListGuest.add("Tutorial")
            mSettingsArrayListGuest.add("Logout")
        }
        else{
            if (sharedprefs.getDataCollection(mContext)==1)
            {
                mSettingsArrayListRegistered.add("Change App Settings")
                mSettingsArrayListRegistered.add("Terms of Service")
                mSettingsArrayListRegistered.add("Feedback")
                mSettingsArrayListRegistered.add("Tutorial")
                mSettingsArrayListRegistered.add("Re Enrolment")
                mSettingsArrayListRegistered.add("Change Password")
                mSettingsArrayListRegistered.add("Delete my Account")
                mSettingsArrayListRegistered.add("Logout")
            }
            else
            {
                mSettingsArrayListRegistered.add("Change App Settings")
                mSettingsArrayListRegistered.add("Terms of Service")
                mSettingsArrayListRegistered.add("Feedback")
                mSettingsArrayListRegistered.add("Tutorial")
                mSettingsArrayListRegistered.add("Re Enrolment")
                mSettingsArrayListRegistered.add("Change Password")
                mSettingsArrayListRegistered.add("Delete my Account")
                mSettingsArrayListRegistered.add("Logout")
            }

        }
        initializeUI()
    }

    private fun initializeUI() {

        mSettingsListView = requireView().findViewById(R.id.mSettingsListView) as RecyclerView
        titleTextView = requireView().findViewById(R.id.titleTextView) as TextView
        progress = requireView().findViewById(R.id.progress) as ProgressBar
        titleTextView.text = "Settings"
        linearLayoutManager = LinearLayoutManager(mContext)
        mSettingsListView.layoutManager = linearLayoutManager
        mSettingsListView.itemAnimator = DefaultItemAnimator()
        mSettingsListView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                 if (sharedprefs.getUserCode(mContext).equals(""))
                 {
                     if(position==0)
                     {
                         val intent = Intent()
                         intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                         val uri = Uri.fromParts("package", activity!!.packageName, null)
                         intent.data = uri
                         mContext.startActivity(intent)
                     }
                    else if(position==1)
                     {
                         val intent =Intent(activity, TermsOfServiceActivity::class.java)
                         activity?.startActivity(intent)
                     }
                     else if (position==2)
                     {
                         val deliveryAddress =
                             arrayOf("comms@bisad.ae")
                         val emailIntent = Intent(Intent.ACTION_SEND)
                         emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                         emailIntent.type = "text/plain"
                         emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                         val pm: PackageManager = mContext.packageManager
                         val activityList = pm.queryIntentActivities(
                             emailIntent, 0
                         )
                         println("packge size" + activityList.size)
                         for (app in activityList) {
                             println("packge name" + app.activityInfo.name)
                             if (app.activityInfo.name.contains("com.google.android.gm")) {
                                 val activity = app.activityInfo
                                 val name = ComponentName(
                                     activity.applicationInfo.packageName, activity.name
                                 )
                                 emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                 emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                         or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                 emailIntent.component = name
                                 mContext.startActivity(emailIntent)
                                 break
                             }
                         }
//                         val to:String="communications@bisaddubai.com"
//                         val email = Intent(Intent.ACTION_SEND)
//                         email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//                         email.type = "message/rfc822"
//                         startActivity(Intent.createChooser(email, "Choose an Email client :"))
                     }
                     else if (position==3)
                     {
                         val intent =Intent(activity, TutorialActivity::class.java)
                         activity?.startActivity(intent)
                     }
                    else if (position==4)
                     {
                         sharedprefs.setUserCode(mContext,"")
                         sharedprefs.setUserID(mContext,"")
                         val mIntent = Intent(activity, LoginActivity::class.java)
                         mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                         activity!!.startActivity(mIntent)
                     }
                 }
                else{
                     if (sharedprefs.getDataCollection(mContext)==1)
                     {
                         if(position==0)
                         {
                             val intent = Intent()
                             intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                             val uri = Uri.fromParts("package", activity!!.packageName, null)
                             intent.data = uri
                             mContext.startActivity(intent)
                         }
                         else if(position==1)
                         {
                             val intent =Intent(activity, TermsOfServiceActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==2)
                         {
                             val deliveryAddress =
                                 arrayOf("communications@bisaddubai.com")
                             val emailIntent = Intent(Intent.ACTION_SEND)
                             emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                             emailIntent.type = "text/plain"
                             emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                             val pm: PackageManager = mContext.packageManager
                             val activityList = pm.queryIntentActivities(
                                 emailIntent, 0
                             )
                             println("packge size" + activityList.size)
                             for (app in activityList) {
                                 println("packge name" + app.activityInfo.name)
                                 if (app.activityInfo.name.contains("com.google.android.gm")) {
                                     val activity = app.activityInfo
                                     val name = ComponentName(
                                         activity.applicationInfo.packageName, activity.name
                                     )
                                     emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                     emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                             or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                     emailIntent.component = name
                                     mContext.startActivity(emailIntent)
                                     break
                                 }
                             }

//                             val to:String="communications@bisaddubai.com"
//                             val email = Intent(Intent.ACTION_SEND)
//                             email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//                             email.type = "message/rfc822"
//                             startActivity(Intent.createChooser(email, "Choose an Email client :"))
                         }
                         else if (position==3)
                         {
                             val intent =Intent(activity, TutorialActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==4)
                         {
                             val intent =Intent(activity, ReEnrollmentActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==5)
                         {
                             changePassword(mContext)
                         }
//                         else if (position==5)
//                         {
//                             showTriggerDataCollection(mContext,"Confirm?", "Select one or more areas to update", R.drawable.questionmark_icon, R.drawable.round)
//                         }
                         else if(position==6){
                             showDeleteaccountpopup(mContext,"Confirm?","Do you want to delete your Account?")
                         }
                         else if (position==7)
                         {
                             showSuccessAlert(mContext, "Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round)

                         }
                     }
                     else{
                         if(position==0)
                         {
                             val intent = Intent()
                             intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                             val uri = Uri.fromParts("package", activity!!.packageName, null)
                             intent.data = uri
                             mContext.startActivity(intent)
                         }
                         else if(position==1)
                         {
                             val intent =Intent(activity, TermsOfServiceActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==2)
                         {
                             val i = Intent(Intent.ACTION_SEND)
                             i.type = "message/rfc822"
                             i.putExtra(Intent.EXTRA_EMAIL, arrayOf("communications@bisaddubai.com"))
                             try {
                                 startActivity(Intent.createChooser(i, "Send mail..."))
                             } catch (ex: ActivityNotFoundException) {

                             }
                           /*  val emailIntent = Intent(Intent.ACTION_SEND)
                             emailIntent.type = "text/plain"
                             startActivity(emailIntent)*/
                             /* var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                             if (internetCheck) {
                                 val deliveryAddress =
                                     arrayOf("communications@bisaddubai.com")
                                 val emailIntent = Intent(Intent.ACTION_SEND)
                                 emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                                 emailIntent.type = "text/plain"
                                 emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                 val pm: PackageManager = mContext.packageManager
                                 val activityList = pm.queryIntentActivities(
                                     emailIntent, 0
                                 )
                                 for (app in activityList) {
                                     if (app.activityInfo.name.contains("com.google.android.gm")) {
                                         val activity = app.activityInfo
                                         val name = ComponentName(
                                             activity.applicationInfo.packageName, activity.name
                                         )
                                         emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                         emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                                 or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                         emailIntent.component = name
                                         mContext.startActivity(emailIntent)
                                         break
                                     }
                                 }
                             } else {
                                 InternetCheckClass.showSuccessInternetAlert(mContext)
                             }*/
                             /*val deliveryAddress =
                                 arrayOf("communications@bisaddubai.com")
                             val emailIntent = Intent(Intent.ACTION_SEND)
                             emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                             emailIntent.type = "text/plain"
                             emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                             val pm: PackageManager = mContext.packageManager
                             val activityList = pm.queryIntentActivities(
                                 emailIntent, 0
                             )
                             println("packge size" + activityList.size)
                             for (app in activityList) {
                                 println("packge name" + app.activityInfo.name)
                                 if (app.activityInfo.name.contains("com.google.android.gm")) {
                                     val activity = app.activityInfo
                                     val name = ComponentName(
                                         activity.applicationInfo.packageName, activity.name
                                     )
                                     emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                     emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                             or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                     emailIntent.component = name
                                     startActivity(emailIntent)
                                     break
                                 }
                             }*/
//                             val to:String="communications@bisaddubai.com"
//                             val email = Intent(Intent.ACTION_SEND)
//                             email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//                             email.type = "message/rfc822"
//                             startActivity(Intent.createChooser(email, "Choose an Email client :"))
                         }
                         else if (position==3)
                         {
                             val intent =Intent(activity, TutorialActivity::class.java)
                             activity?.startActivity(intent)
                         }else if (position==4)
                         {
                             val intent =Intent(activity, ReEnrollmentActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==5)
                         {
                             changePassword(mContext)
                         }
                         else if(position==6){
                             showDeleteaccountpopup(mContext,"Confirm?","Do you want to delete your Account?")
                         }
                         else if (position==7)
                         {
                             showSuccessAlert(mContext, "Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round)

                         }
                     }

                 }
            }
        })
        if (sharedprefs.getUserCode(mContext).equals(""))
        {
            val settingsAdapter = SettingsRecyclerAdapter(mContext,mSettingsArrayListGuest)
            mSettingsListView.adapter = settingsAdapter
        }
        else{
            val settingsAdapter = SettingsRecyclerAdapter(mContext,mSettingsArrayListRegistered)
            mSettingsListView.adapter = settingsAdapter
        }

    }


    fun showSuccessDataAlert(context: Context,msgHead:String,msg:String,ico:Int,bgIcon:Int)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setBackgroundResource(bgIcon)
        iconImageView.setImageResource(ico)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = msg
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {

            dialog.dismiss()


        }
        btn_Cancel.setOnClickListener()
        {
            dialog.dismiss()

        }
        dialog.show()
    }

    fun showSuccessAlert(context: Context,msgHead:String,msg:String,ico:Int,bgIcon:Int)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setBackgroundResource(bgIcon)
        iconImageView.setImageResource(ico)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = msg
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
            if (sharedprefs.getUserCode(mContext).equals("")) {
                sharedprefs.setUserCode(mContext,"")
                sharedprefs.setUserID(mContext,"")
                val mIntent = Intent(activity, LoginActivity::class.java)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                requireActivity().startActivity(mIntent)

            } else{
                callLogoutApi(dialog)
            }
            dialog.dismiss()
        }
        btn_Cancel.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDeleteaccountpopup(context: Context,msgHead:String,msg:String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.questionmark_icon)

        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = msg
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
           delAccountApi()
        }
        btn_Cancel.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun delAccountApi() {
        progress.visibility = View.VISIBLE

        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<DeleteAccountModel> = ApiClient.getClient.delete_account("Bearer "+token)
        call.enqueue(object : Callback<DeleteAccountModel> {
            override fun onFailure(call: Call<DeleteAccountModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<DeleteAccountModel>, response: Response<DeleteAccountModel>) {
                val responsedata = response.body()
                if (responsedata!!.status==100) {
                    progress.visibility = View.GONE
                    sharedprefs.setUserCode(mContext,"")
                    sharedprefs.setUserID(mContext,"")
                    sharedprefs.setUserEmail(mContext,"")
                    sharedprefs.setUserCode(mContext,"")
                    sharedprefs.setUserID(mContext,"")
                    val mIntent = Intent(activity, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    activity!!.startActivity(mIntent)


                }else if (response.body()!!.status == 116) {
                    if (apiCall != 4) {
                        apiCall = apiCall + 1
                        AccessTokenClass.getAccessToken(mContext)
                        delAccountApi()
                    } else {

                        showSuccessAlertnew(
                            mContext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )

                    }
                } else {
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


    private fun callLogoutApi(dialog:Dialog)
    {
        progress.visibility = View.VISIBLE

        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<ResponseBody> = ApiClient.getClient.logout("Bearer "+token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                if (responsedata != null) {
                    progress.visibility = View.GONE

                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if(jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            if (status == 100) {
                                sharedprefs.setUserCode(mContext,"")
                                sharedprefs.setUserID(mContext,"")

                                var dummyOwn=ArrayList<OwnContactModel>()
                                sharedprefs.setOwnContactDetailArrayList(mContext,dummyOwn)
                                var dummyKinPass=ArrayList<KinDetailApiModel>()
                                sharedprefs.setKinDetailPassArrayList(mContext,dummyKinPass)
                                var dummyKinShow=ArrayList<KinDetailApiModel>()
                                sharedprefs.setKinDetailArrayList(mContext,dummyKinShow)
                                var dummyHealth=ArrayList<HealthInsuranceDetailAPIModel>()
                                sharedprefs.setHealthDetailArrayList(mContext,dummyHealth)
                                var dummyPassport=ArrayList<PassportApiModel>()
                                sharedprefs.setPassportDetailArrayList(mContext,dummyPassport)
                                var dummyStudent=ArrayList<StudentListDataCollection>()
                                sharedprefs.setStudentArrayList(mContext,dummyStudent)
                                sharedprefs.setUserEmail(mContext,"")
                                sharedprefs.setUserCode(mContext,"")
                                sharedprefs.setUserID(mContext,"")
                                val mIntent = Intent(activity, LoginActivity::class.java)
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                activity!!.startActivity(mIntent)
                                dialog.dismiss()

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callLogoutApi(dialog)
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
                                    }
                                }

                            }
                        }
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
    fun String.onlyLetters() = all { it.isLetter() }
    fun String.withoutWhitespace() = require(none { it.isWhitespace() }) {  }
    fun changePassword(context: Context)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog_changepassword)
        var text_currentpassword = dialog.findViewById(R.id.text_currentpassword) as EditText
        var text_currentnewpassword = dialog.findViewById(R.id.text_currentnewpassword) as EditText
        var text_confirmpassword = dialog.findViewById(R.id.text_confirmpassword) as EditText
        var btn_changepassword = dialog.findViewById(R.id.btn_changepassword) as Button
        var btn_cancel = dialog.findViewById(R.id.btn_cancel) as Button
        btn_cancel.isClickable=true
        btn_cancel.setOnClickListener()
        {

            dialog.dismiss()
        }

        btn_changepassword.setOnClickListener()
        {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(text_currentpassword.windowToken, 0)
            val imn= context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imn.hideSoftInputFromWindow(text_currentnewpassword.windowToken, 0)
            val imo= context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imo.hideSoftInputFromWindow(text_confirmpassword.windowToken, 0)
            if (text_currentpassword.text.toString().trim().equals("")) {
                InternetCheckClass. showErrorAlert(context,"Please enter Current Password","Alert")
            } else{
                if (text_currentnewpassword.text.toString().trim().equals("")) {
                    InternetCheckClass. showErrorAlert(context,"Please enter New Password","Alert")
                } else{
                    if (text_confirmpassword.text.toString().trim().equals("")) {
                        InternetCheckClass. showErrorAlert(context,"Please enter Confirm Password","Alert")
                    } else{
                        if (text_currentnewpassword.getText().toString().trim { it <= ' ' }
                                .matches(PASSWORD_PATTERN.toRegex()) && text_confirmpassword.getText()
                                .toString().trim { it <= ' ' }
                                .matches(PASSWORD_PATTERN.toRegex())
                        ) {

                            callChangePasswordApi(text_currentpassword.text.toString().trim(),text_currentnewpassword.text.toString().trim(),text_confirmpassword.text.toString(),dialog)

                        } else {
                            if (!text_currentnewpassword.getText().toString().onlyLetters()&&
                                !text_confirmpassword.getText()
                                    .toString().onlyLetters())
                            {

                                if (!text_currentnewpassword.text.toString()
                                        .contains(" ") &&
                                    !text_confirmpassword.getText()
                                        .toString()
                                        .contains(" "))
                                {

                                    if (text_currentnewpassword.text.toString().trim()
                                            .matches(PASSWORD_PATTERN3.toRegex()) &&
                                        text_confirmpassword.getText()
                                            .toString().trim { it <= ' ' }
                                            .matches(PASSWORD_PATTERN3.toRegex())){
                                        Log.e("password","correct")
                                    }else{
                                        Toast.makeText(context, "Password must contain atleast 8 characters", Toast.LENGTH_SHORT).show()

                                    }

                                }else{
                                    Toast.makeText(context, "Password must not contain white spaces", Toast.LENGTH_SHORT).show()

                                }

                            }else{
                                Toast.makeText(context, "Password must contain atleast 1 special character", Toast.LENGTH_SHORT).show()

                        }
                        }
                    }
                }
            }
            //dialog.dismiss()
        }


        dialog.show()
    }
    private fun callChangePasswordApi(currentPassword:String,newPassword:String,confirmPassword:String,dialog:Dialog) {
        val token = sharedprefs.getaccesstoken(mContext)
        val changePasswordBody =
            ChangePasswordApiModel(newPassword, confirmPassword, currentPassword)
        val call: Call<ResponseBody> =
            ApiClient.getClient.changePassword(changePasswordBody, "Bearer " + token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if (jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            if (status == 100) {
                                InternetCheckClass.showErrorAlert(
                                    mContext,
                                    "Password successfully changed",
                                    "Alert"
                                )
                                dialog.dismiss()

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callChangePasswordApi(
                                        currentPassword,
                                        newPassword,
                                        confirmPassword,
                                        dialog
                                    )
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
                                    }
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

}




