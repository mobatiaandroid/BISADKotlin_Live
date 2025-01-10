package com.mobatia.bisad.activity.common

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.mobatia.bisad.BuildConfig
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.common.model.DeviceRegModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import com.scottyab.rootbeer.RootBeer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    lateinit var mContext: Context
    private val SPLASH_TIME_OUT:Long = 3000
    lateinit var sharedprefs: PreferenceData
    var tokenM:String=""
    var firebaseid:String=""
    lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mContext = this
        activity=this
        sharedprefs = PreferenceData()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isComplete) {
                val token: String = task.getResult().toString()
                tokenM = token
                //callLoginApi(emailTxt,passwordTxt,deviceId)
                //callChangePasswordStaffAPI(URL_STAFF_CHANGE_PASSWORD, token)
            }
        }
        firebaseid = tokenM


        sharedprefs.setFcmID(mContext, firebaseid)
        var re_enroll = "1"
        sharedprefs.setreenrollvalue(mContext, re_enroll)

        val rootBeer = RootBeer(mContext)
        if (rootBeer.isRooted()) {
            showDeviceIsRootedPopUp(mContext)
        }
        else if (!CommonFunctions.runMethod.equals("Dev"))
        {
            if (CommonFunctions.isDeveloperModeEnabled(mContext)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
            else{
                Handler().postDelayed({
                    if (sharedprefs.getUserCode(mContext).equals("")) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        var accessTokenValue = AccessTokenClass.getAccessToken(mContext)

                        callDeviceRegistrationApi()
                        sharedprefs.setSuspendTrigger(mContext, "0")
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                }, SPLASH_TIME_OUT)
            }
        }
        else{
            Handler().postDelayed({
                if (sharedprefs.getUserCode(mContext).equals("")) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    var accessTokenValue = AccessTokenClass.getAccessToken(mContext)

                    callDeviceRegistrationApi()
                    sharedprefs.setSuspendTrigger(mContext, "0")
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }, SPLASH_TIME_OUT)
        }
    }
    private fun showDeviceIsRootedPopUp(mContext: Context) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(R.drawable.round)
        icon.setImageResource(R.drawable.exclamationicon)
        val text = dialog.findViewById(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById(R.id.alertHead) as TextView
        text.text = "This app does not support rooted devices for security reasons."
        textHead.text = "Rooted Device Detected"
        val dialogButton = dialog.findViewById(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener { dialog.dismiss() }
        //		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show()
    }


    fun callDeviceRegistrationApi()
    {
        var devicename:String= (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT]
            .name)
        val version: String = BuildConfig.VERSION_NAME
        val token = sharedprefs.getaccesstoken(mContext)
        var androidID = Settings.Secure.getString(this.contentResolver,
            Settings.Secure.ANDROID_ID)
        var deviceReg= DeviceRegModel(2, tokenM,androidID,devicename,version)
        val call: Call<ResponseBody> = ApiClient(mContext).getClient.deviceregistration(deviceReg,"Bearer "+token)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()


                if (responsedata != null) {
                    try {

                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
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