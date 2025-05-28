package com.mobatia.bisad.manager

import android.app.Activity
import android.content.res.TypedArray
import android.os.Bundle
import android.view.WindowManager
import androidx.multidex.MultiDexApplication

class AppController : MultiDexApplication() {
    var listitemArrays: ArrayList<String> = ArrayList()
    var mListImgArrays: TypedArray? = null
    var mTitles: String? = null
    var trip_name: String? = null
    var reciver:String="0"

    override fun onCreate() {
        super.onCreate()

        // Register ActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // Apply FLAG_SECURE to prevent screenshots and screen recordings

//                activity.window.setFlags(
//                    WindowManager.LayoutParams.FLAG_SECURE,
//                    WindowManager.LayoutParams.FLAG_SECURE
//                )
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}