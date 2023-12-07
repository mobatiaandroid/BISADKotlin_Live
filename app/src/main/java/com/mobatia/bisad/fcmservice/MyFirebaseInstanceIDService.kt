package com.mobatia.bisad.fcmservice

import android.content.Context
import android.util.Log

import com.mobatia.bisad.manager.PreferenceData

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
public class MyFirebaseInstanceIDService  {
    //var mContext: Context
    var sharedprefs: PreferenceData= PreferenceData()


   /* override fun onTokenRefresh() {

        //val refreshedToken = FirebaseInstanceId.getInstance().token

        val refreshedToken = FirebaseInstanceId.getInstance().token.toString()

        sendRegistrationToServer(refreshedToken)
        super.onTokenRefresh()
    }*/

    private fun sendRegistrationToServer(refreshedToken: String) {
       /* if (refreshedToken != null) {
            sharedprefs.setFcmID(mContext, refreshedToken)
        }*/

    }
}