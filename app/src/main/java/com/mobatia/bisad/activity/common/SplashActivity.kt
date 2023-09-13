package com.mobatia.bisad.activity.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

import com.mobatia.bisad.MainActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.common.model.DeviceRegModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    lateinit var mContext: Context
    private val SPLASH_TIME_OUT:Long = 3000
    lateinit var sharedprefs: PreferenceData
    var tokenM:String=""
    var firebaseid:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mContext=this
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

        Log.e("FIREBASE ID ANDROID:",firebaseid)
        sharedprefs.setFcmID(mContext,firebaseid)
        var re_enroll="1"
        sharedprefs.setreenrollvalue(mContext,re_enroll)


        Handler().postDelayed({
            if (sharedprefs.getUserCode(mContext).equals(""))
            {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else
            {
                var accessTokenValue=AccessTokenClass.getAccessToken(mContext)
                Log.e("AccessToken", accessTokenValue.toString())
                callDeviceRegistrationApi()
                sharedprefs.setSuspendTrigger(mContext,"0")
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }, SPLASH_TIME_OUT)
    }


    fun callDeviceRegistrationApi()
    {
        val token = sharedprefs.getaccesstoken(mContext)
        var androidID = Settings.Secure.getString(this.contentResolver,
            Settings.Secure.ANDROID_ID)
        System.out.println("LOGINRESPONSE:"+"devid:  "+androidID+" FCM ID : "+tokenM)
        var deviceReg= DeviceRegModel(2, tokenM,androidID)
        val call: Call<ResponseBody> = ApiClient.getClient.deviceregistration(deviceReg,"Bearer "+token)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)

            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()

                Log.e("Response Signup", responsedata.toString())
                if (responsedata != null) {
                    try {
                        Log.e("AccessToken","Sucess")
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
}