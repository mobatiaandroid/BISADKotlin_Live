package com.mobatia.bisad.activity.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.mobatia.bisad.BuildConfig
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(),View.OnTouchListener{
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    var TAG :String="FIREBASE"
    var deviceId : String=""
    lateinit var dialog : Dialog
    var tokenM:String=""
    private lateinit var progressDialog: ProgressBar
    lateinit var jsonConstans: JsonConstants
    lateinit var userEditText :TextView
    lateinit var passwordEditText:TextView
    lateinit var loginBtn:Button
    lateinit var helpButton:Button
    lateinit var guestButton:Button
    lateinit var signUpButton:Button
    lateinit var forgotPasswordButton:Button
    lateinit var activity: Activity


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext=this
        activity=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        initUI()

    }
    @SuppressLint("ClickableViewAccessibility")
    fun initUI()
    {
        progressDialog = findViewById(R.id.progressDialog)

         userEditText = findViewById<EditText>(R.id.userEditText)
         passwordEditText = findViewById<EditText>(R.id.passwordEditText)
         loginBtn = findViewById<Button>(R.id.loginBtn)
         helpButton = findViewById<Button>(R.id.helpButton)
         guestButton = findViewById<Button>(R.id.guestButton)
         signUpButton = findViewById<Button>(R.id.signUpButton)
         forgotPasswordButton = findViewById<Button>(R.id.forgotPasswordButton)
        userEditText.setOnTouchListener(this)
        passwordEditText.setOnTouchListener(this)


        //Keyboard done button click username
        userEditText.setOnEditorActionListener { v, actionId, event ->
			if(actionId == EditorInfo.IME_ACTION_DONE){
                userEditText?.isFocusable=false
                userEditText?.isFocusableInTouchMode=false
				false
			} else {
                userEditText?.isFocusable=false
                userEditText?.isFocusableInTouchMode=false
				false
			}
		}

        //Keyboard done button click password
        passwordEditText.setOnEditorActionListener { v, actionId, event ->
			if(actionId == EditorInfo.IME_ACTION_DONE){
                passwordEditText.isFocusable =false
                passwordEditText.isFocusableInTouchMode =false
				false
			} else {
                userEditText?.isFocusable=false
                userEditText?.isFocusableInTouchMode=false
				false
			}
		}

        //Signup Button Click
        signUpButton.setOnClickListener()
        {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(userEditText.windowToken, 0)
            val immq = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            immq?.hideSoftInputFromWindow(passwordEditText.windowToken, 0)
            showSignupDialogue(mContext)
        }
        guestButton.setOnClickListener()
        {
            startActivity(Intent(mContext,HomeActivity::class.java))
        }
        helpButton.setOnClickListener()
        {
            var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
            if (internetCheck) {
                val intent = Intent(Intent.ACTION_SEND)
                val recipients = arrayOf("comms@bisad.ae")
                intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                intent.type = "text/html"
                intent.setPackage("com.google.android.gm")
                startActivity(Intent.createChooser(intent, "Send mail"))

              /*  val deliveryAddress =
                    arrayOf("communications@bisaddubai.com")
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                emailIntent.type = "text/plain"
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val pm: PackageManager = helpButton.context.packageManager
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
                        helpButton.context.startActivity(emailIntent)
                        break
                    }
                }*/
            } else {
                InternetCheckClass.showSuccessInternetAlert(mContext)
            }


        }

        //Forget Password Button Click
        forgotPasswordButton.setOnClickListener()
        {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(userEditText.windowToken, 0)
            val immq = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            immq?.hideSoftInputFromWindow(passwordEditText.windowToken, 0)
            showForgetPassword(mContext)
        }


        //Login Button Click
        loginBtn.setOnClickListener()
        {
            if(userEditText.text.toString().trim().equals("")) {
                InternetCheckClass.showSuccessAlert(mContext,"Please enter Email.","Alert")
            } else {
                var emailPattern = InternetCheckClass.isEmailValid(userEditText.text.toString().trim())
                if (!emailPattern) {
                    InternetCheckClass.showSuccessAlert(mContext,"Please enter a vaild Email.","Alert")
                } else {
                    if(passwordEditText.text.toString().trim().equals("")) {
                        InternetCheckClass.showSuccessAlert(mContext,"Please enter Password.","Alert")
                    } else{
                        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                        if (internetCheck) {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                            imm?.hideSoftInputFromWindow(userEditText.windowToken, 0)
                            val immq = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                            immq?.hideSoftInputFromWindow(passwordEditText.windowToken, 0)
                            var emailTxt = userEditText.text.toString().trim()
                            var passwordTxt = passwordEditText.text.toString().trim()
                            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                if (task.isComplete) {
                                    val token: String = task.getResult().toString()
                                    tokenM = token
                                    callLoginApi(emailTxt,passwordTxt,deviceId)
                                    //callChangePasswordStaffAPI(URL_STAFF_CHANGE_PASSWORD, token)
                                }
                            }

                        } else {
                            InternetCheckClass.showSuccessInternetAlert(mContext)
                        }
                    }
                }
            }
        }
}

    //Signup API Call
    @SuppressLint("HardwareIds")
    fun callLoginApi(email: String, password : String, deviceID : String)
    {

        progressDialog.visibility=View.VISIBLE
        var devicename:String= (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT]
            .name)
        val version: String = BuildConfig.VERSION_NAME
        var androidID = Settings.Secure.getString(this.contentResolver,
            Settings.Secure.ANDROID_ID)
        val call: Call<ResponseBody> = ApiClient(mContext).getClient.login(
            email,password,2,androidID, tokenM,devicename,version
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)
                progressDialog.visibility=View.GONE
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                progressDialog.visibility=View.GONE
                if (responsedata != null) {
                    try {
                        progressDialog.visibility=View.GONE
                        val jsonObject = JSONObject(responsedata.string())
                        if(jsonObject.has(jsonConstans.STATUS))
                        {
                            val status : Int=jsonObject.optInt(jsonConstans.STATUS)
                            if (status==100)
                            {
                                //Success API response
                                sharedprefs.setUserEmail(mContext,email)
                                if (jsonObject.has(jsonConstans.USER_CODE))
                                {
                                    val userCode :String =jsonObject.optString(jsonConstans.USER_CODE)
                                    sharedprefs.setUserCode(mContext,userCode)
                                }
                                if (jsonObject.has(jsonConstans.TOKEN))
                                {
                                    val token : String = jsonObject.optString(jsonConstans.TOKEN)
                                    sharedprefs.setaccesstoken(mContext,token)
                                }
                                showSuccessAlert(mContext,"Successfully Logged in.","Success")
//                                startActivity(Intent(mContext,HomeActivity::class.java))
//                                finish()
                            }
                            else if (status==501) {
                                InternetCheckClass.checkApiStatusError(status,mContext)
                                //DialogFunctions.commonErrorAlertDialog(mContext.resources.getString(R.string.alert), ConstantFunctions.commonErrorString(response.body()!!.status), mContext)


//						startCountdown(60000L);
                            }
                            else{
                                if (status==116)
                                {
                                    //call Token Expired
                                }
                                else
                                {
                                    if (status==103)
                                    {
                                        //validation check error
                                        InternetCheckClass.checkApiStatusError(status,mContext)
                                    }
                                    else
                                    {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status,mContext)
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

    // touch listener for edittexts
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (view) {
            userEditText -> {
                when (motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        userEditText?.isFocusable=true
                        userEditText?.isFocusableInTouchMode=true
                    }
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        userEditText?.isFocusable=true
                        userEditText?.isFocusableInTouchMode=true
                    }
                }
            }
            passwordEditText -> {
                when (motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        passwordEditText?.isFocusable=true
                        passwordEditText?.isFocusableInTouchMode=true
                    }
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        passwordEditText?.isFocusable=true
                        passwordEditText?.isFocusableInTouchMode=true
                    }
                }
            }
        }
        return false
    }



    //Sign up popup
    @SuppressLint("ClickableViewAccessibility")
    fun showSignupDialogue(context: Context)
    {
        dialog=Dialog(mContext,R.style.NewDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout_signup)
        var text_dialog = dialog.findViewById(R.id.text_dialog) as EditText
        var btn_maybelater = dialog.findViewById(R.id.btn_maybelater) as Button
        var progressDialog1 = dialog.findViewById(R.id.progressDialog2) as ProgressBar
        var btn_signup = dialog.findViewById(R.id.btn_signup) as Button
        /*val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)*/
        text_dialog.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                text_dialog.isFocusable =false
                text_dialog.isFocusableInTouchMode =false
                false
            } else {
                text_dialog.isFocusable =false
                text_dialog.isFocusableInTouchMode =false
                false
            }
        }

        text_dialog.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                // Perform tasks here
                text_dialog.isFocusable =true
                text_dialog.isFocusableInTouchMode =true
                return false
            }
        })
        btn_signup.setOnClickListener()
        {
            if (text_dialog.text.toString().trim().equals("")) {
                InternetCheckClass.showSuccessAlert(mContext,"Please enter Email.","Alert")
            } else {
                var emailPattern = InternetCheckClass.isEmailValid(text_dialog.text.toString().trim())
                if (!emailPattern) {
                    InternetCheckClass.showSuccessAlert(mContext,"Please enter a valid Email.","Alert")
                } else {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(text_dialog.windowToken, 0)
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck) {
                        callSignUpApi(text_dialog.text.toString().trim(),dialog,progressDialog1)
                    } else {
                        InternetCheckClass.showSuccessInternetAlert(mContext)
                    }

                }
            }
        }
        btn_maybelater.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()

    }


    //Signup API Call
    fun callSignUpApi(email: String, dialog: Dialog, progress: ProgressBar)
    {
        progress.visibility=View.VISIBLE
        val call: Call<ResponseBody> = ApiClient(mContext).getClient.signup(
           email,2, sharedprefs.getFcmID(mContext)
        )
        progress.visibility=View.GONE
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)
                progress.visibility=View.GONE
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                progress.visibility=View.GONE

                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if(jsonObject.has(jsonConstans.STATUS))
                        {
                            val status : Int=jsonObject.optInt(jsonConstans.STATUS)
                            if (status==100)
                            {

                                if (jsonObject.has(jsonConstans.USER_CODE))
                                {
                                    val userCode :String =jsonObject.optString(jsonConstans.USER_CODE)
                                    sharedprefs.setUserCode(mContext,userCode)
                                }
                                if (jsonObject.has(jsonConstans.TOKEN))
                                {
                                    val token : String = jsonObject.optString(jsonConstans.TOKEN)
                                    sharedprefs.setaccesstoken(mContext,token)
                                }
                                showSuccessSignupAlert(mContext,"Successfully registered.Please check your email for further details.","Success",dialog)
//                                startActivity(Intent(mContext,HomeActivity::class.java))
//                                finish()
                            }
                            else if(status==111)
                            {
                                 if (jsonObject.has(jsonConstans.USER_CODE))
                                 {
                                     val userCode :String =jsonObject.optString(jsonConstans.USER_CODE)
                                     sharedprefs.setUserCode(mContext,userCode)
                                 }
                                if (jsonObject.has(jsonConstans.TOKEN))
                                {
                                    val token : String = jsonObject.optString(jsonConstans.TOKEN)
                                    sharedprefs.setaccesstoken(mContext,token)
                                }


                            }
                            else{
                                if (status==116)
                                {
                                    //call Token Expired
                                }
                                else
                                {
                                    if (status==103)
                                    {
                                        //validation check error
                                        val errorObj :JSONObject?= jsonObject.optJSONObject(jsonConstans.ERROR)
                                        if (errorObj!!.has(jsonConstans.EMAIL))
                                        {
                                            val emailArray = errorObj.optJSONArray(jsonConstans.EMAIL)
                                            val list = arrayListOf<Int>()
                                            if(emailArray.length()>0)
                                            {

                                            }
                                            InternetCheckClass.checkApiStatusError(status,mContext)
                                        }
                                    }
                                    else
                                    {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status,mContext)
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

    //Forget Password popup
    @SuppressLint("ClickableViewAccessibility")
    fun showForgetPassword(context: Context)
    {
        dialog=Dialog(mContext,R.style.NewDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_forgot_password)
        var text_dialog = dialog.findViewById(R.id.text_dialog) as EditText
        var btn_maybelater = dialog.findViewById(R.id.btn_maybelater) as Button
        var btn_signup = dialog.findViewById(R.id.btn_signup) as Button
        var progressd = dialog.findViewById(R.id.progressDialog1) as ProgressBar
        text_dialog.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                text_dialog.isFocusable =false
                text_dialog.isFocusableInTouchMode =false
                false
            } else {
                text_dialog.isFocusable =false
                text_dialog.isFocusableInTouchMode =false
                false
            }
        }

        btn_signup.setOnClickListener()
        {
            if (text_dialog.text.toString().trim().equals("")) {
                InternetCheckClass.showSuccessAlert(mContext,"Please enter Email.","Alert")
            } else {
                var emailPattern = InternetCheckClass.isEmailValid(text_dialog.text.toString().trim())
                if (!emailPattern) {
                    InternetCheckClass.showSuccessAlert(mContext,"Please enter a valid Email.","Alert")
                } else {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(text_dialog.windowToken, 0)
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck) {
                        callForgetPassword(text_dialog.text.toString().trim(),dialog,progressd)
                    } else {
                        InternetCheckClass.showSuccessInternetAlert(mContext)
                    }


                }
            }
        }

        text_dialog.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                // Perform tasks here
                text_dialog.isFocusable =true
                text_dialog.isFocusableInTouchMode =true
                return false
            }
        })

        btn_maybelater.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()

    }
    
    // Call Forget Password API
    fun callForgetPassword(email: String, dialog: Dialog, progressd: ProgressBar)
    {
        progressd.visibility=View.VISIBLE
        val call: Call<ResponseBody> = ApiClient(mContext).getClient.forgetPassword(
            email
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressd.visibility=View.GONE
                CommonFunctions.faliurepopup(mContext)
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressd.visibility=View.GONE
                val responsedata = response.body()

                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if(jsonObject.has(jsonConstans.STATUS))
                        {
                            val status : Int=jsonObject.optInt(jsonConstans.STATUS)
                            if (status==100)
                            {
                                showSuccessSignupAlert(mContext,"Password is successfully sent to your email.Please check.","Alert",dialog)

                            }

                            else{
                                if (status==116)
                                {
                                    //call Token Expired
                                }
                                else
                                {
                                    if (status==103)
                                    {
                                        //validation check error
                                        val errorObj :JSONObject?= jsonObject.optJSONObject(jsonConstans.ERROR)
                                        if (errorObj!!.has(jsonConstans.EMAIL))
                                        {
                                            val emailArray = errorObj.optJSONArray(jsonConstans.EMAIL)
                                            val list = arrayListOf<Int>()
                                            if(emailArray.length()>0)
                                            {

                                            }
                                            InternetCheckClass.checkApiStatusError(status,mContext)
                                        }
                                    }
                                    else
                                    {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status,mContext)
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



    fun showSuccessAlert(context: Context,message : String,msgHead : String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_success_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        iconImageView.setImageResource(R.drawable.tick)
        btn_Ok.setOnClickListener()
        {
            startActivity(Intent(context,HomeActivity::class.java))
            dialog.dismiss()
            finish()

        }
        dialog.show()
    }

    fun showSuccessAlertForgot(context: Context,message : String,msgHead : String,dialogPassword:Dialog)
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
        iconImageView.setImageResource(R.drawable.exclamationicon)
        btn_Ok.setOnClickListener()
        {
            dialogPassword.dismiss()
            dialog.dismiss()


        }
        dialog.show()
    }

    fun showSuccessSignupAlert(context: Context,message : String,msgHead : String,dialogSignup:Dialog)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_success_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        iconImageView.setImageResource(R.drawable.tick)
        btn_Ok.setOnClickListener()
        {
            dialogSignup.dismiss()
            dialog.dismiss()

        }
        dialog.show()
    }
    override fun onResume() {
        super.onResume()
        if (!CommonFunctions.runMethod.equals("Dev")) {
            if (CommonFunctions.isDeveloperModeEnabled(mContext)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
        }
    }
}