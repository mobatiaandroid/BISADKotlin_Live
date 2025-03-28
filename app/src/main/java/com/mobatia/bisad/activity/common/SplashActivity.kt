package com.mobatia.bisad.activity.common

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.mobatia.bisad.BuildConfig
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.common.model.DeviceRegModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.DebuggingChecker
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import com.scottyab.rootbeer.RootBeer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.Socket


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
        val isDebuggingEnabled: Boolean = DebuggingChecker().isUsbDebuggingEnabled(mContext)
        val rootBeer = RootBeer(mContext)

        if (isDeviceRootedOrEmulator(mContext)) {
            showDeviceIsRootedPopUp(mContext)
        } else if (rootBeer.isRooted()) {
            Toast.makeText(this, "Root detected! App will close.", Toast.LENGTH_LONG).show()
            finish()
        }
        /*else if (isDebuggingEnabled) {

           Toast.makeText(mContext, "This app does not support debugging", Toast.LENGTH_SHORT).show();
       }*/
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

    /*  boolean isDebuggerConnected() {
        return Debug.isDebuggerConnected();
    }*/
    /*  boolean isDebuggerConnected() {
        return Debug.isDebuggerConnected();
    }*/
    fun isDeviceRootedOrEmulator(mContext: Context): Boolean {
        return (SplashActivity().isDeviceRooted()
                || SplashActivity().isEmulator()
                || SplashActivity().hasEmulatorFiles()
                || SplashActivity().isMagiskInstalled()
                || SplashActivity().isFridaRunning()
                || SplashActivity().isFridaLibraryLoaded()
                || SplashActivity().isSuspiciousProcessRunning())
                ||SplashActivity(). checkForRootBinaries()
                ||SplashActivity(). checkForRootApps(mContext)
                ||SplashActivity(). detectJavaDebugger()
                || SplashActivity().detect_threadCpuTimeNanos()

    }
    fun isDeviceRooted(): Boolean {
        val paths = arrayOf(
            "/system/xbin/su",
            "/system/bin/su",
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/.ext/.su"
        )

        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }
    fun isMagiskInstalled(): Boolean {
        val magiskPaths = arrayOf(
            "/sbin/magisk",
            "/sbin/.magisk",
            "/data/adb/magisk",
            "/data/adb/modules",
            "/cache/magisk.log",
            "/data/magisk.img"
        )

        for (path in magiskPaths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }
    fun hasEmulatorFiles(): Boolean {
        val paths = arrayOf(
            "/dev/qemu_pipe",
            "/dev/socket/qemud",
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu_props"
        )

        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }
    fun isEmulator(): Boolean {
        val buildFingerprint = Build.FINGERPRINT
        val model = Build.MODEL
        val manufacturer = Build.MANUFACTURER
        val brand = Build.BRAND
        val device = Build.DEVICE
        val product = Build.PRODUCT

        return (buildFingerprint.startsWith("generic")
                || buildFingerprint.startsWith("unknown")
                || model.contains("google_sdk")
                || model.contains("Emulator")
                || model.contains("Android SDK built for x86")
                || manufacturer.contains("Genymotion")
                || (brand.startsWith("generic") && device.startsWith("generic"))) || product == "google_sdk"
    }
    fun isFridaRunning(): Boolean {
        try {
            // Check for Frida default port
            val socket = Socket("127.0.0.1", 27042)
            socket.close()
            return true // Frida detected
        } catch (e: java.lang.Exception) {
            return false // No Frida
        }
    }

    fun isFridaLibraryLoaded(): Boolean {
        try {
            val process = Runtime.getRuntime().exec("cat /proc/self/maps")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var maps = reader.readLine()
            while (maps != null) {
                if (maps.contains("frida") || maps.contains("gadget")) {
                    reader.close()
                    return true // Frida library detected
                }
                maps = reader.readLine()
            }
            reader.close()
        } catch (e: java.lang.Exception) {
            // Ignore exception
        }
        return false
    }

    fun isSuspiciousProcessRunning(): Boolean {
        val suspiciousProcesses = arrayOf("frida", "objection", "gadget")
        try {
            val process = Runtime.getRuntime().exec("ps")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String
            while ((reader.readLine().also { line = it }) != null) {
                for (suspicious in suspiciousProcesses) {
                    if (line.contains(suspicious!!)) {
                        reader.close()
                        return true // Suspicious process detected
                    }
                }
            }
            reader.close()
        } catch (e: java.lang.Exception) {
            // Exception ignored
        }
        return false
    }
    fun checkForRootBinaries(): Boolean {
        val rootBinariesPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )


        for (path in rootBinariesPaths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }


    // New method to check for root apps
    fun checkForRootApps(context: Context): Boolean {
        val knownRootAppsPackages = arrayOf(
            "com.noshufou.android.su",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.topjohnwu.magisk"
        )


        val pm = context.packageManager
        for (packageName in knownRootAppsPackages) {
            try {
                pm.getPackageInfo(packageName!!, 0)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                // Continue checking
            }
        }
        return false
    }
    fun detectJavaDebugger(): Boolean {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger()
    }


    // New method to detect thread CPU time for debugging
    fun detect_threadCpuTimeNanos(): Boolean {
        val start: Long = Debug.threadCpuTimeNanos()
        for (i in 0..999999) continue
        val stop: Long = Debug.threadCpuTimeNanos()
        return (stop - start >= 10000000)
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