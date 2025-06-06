package com.mobatia.bisad.constants

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.mobatia.bisad.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InternetCheckClass {

    companion object {
        @Suppress("DEPRECATION")
        fun isInternetAvailable(context: Context): Boolean
        {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }

        //Email Pattern Check
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun checkApiStatusError(statusCode : Int,context: Context)
        {
            if (statusCode==101)
            {
             showErrorAlert(context,"Some error occured","Alert")
            }
            else if (statusCode==102)

            {
                showErrorAlert(context,"Internal server error","Alert")
            }

            else if (statusCode==110)
            {

                showErrorAlert(context,"Invalid username/password","Alert")
            }

            else if (statusCode==113)
            {
                showErrorAlert(context,"Verification code not match","Alert")
            }
            else if (statusCode==114)
            {
                showErrorAlert(context,"User not found in our database","Alert")
            }
            else if (statusCode==116)
            {
                showErrorAlert(context,"Token expired","Alert")
            }
            else if (statusCode==123)
            {
                showErrorAlert(context,"Invalid file access","Alert")
            }
            else if(statusCode==131)
            {
                showErrorAlert(context,"URL/Method Not found","Alert")
            }
            else if (statusCode==132)
            {
                showErrorAlert(context,"No records found","Alert")
            }
            else if (statusCode==133)
            {
                showErrorAlert(context,"Restricted access","Alert")
            }
            if (statusCode==104)
            {
                showErrorAlert(context,"field required","Alert")

            }
            else if (statusCode==105)
            {
                showErrorAlert(context,"Already exists","Alert")

            }
            else if (statusCode==103)
            {
                showErrorAlert(context,"Some error occur.","Alert")

            }
            else if (statusCode==106)
            {
                showErrorAlert(context,Resources.getSystem().getString(R.string.status_106),"Alert")

            } else if (statusCode==107)
            {
                showErrorAlert(context,Resources.getSystem().getString(R.string.status_107),"Alert")

            } else if (statusCode==108)
            {
                showErrorAlert(context,Resources.getSystem().getString(R.string.status_108),"Alert")

            } else if (statusCode==109)
            {
                showErrorAlert(context,Resources.getSystem().getString(R.string.status_109),"Alert")

            } else if (statusCode==111)
            {
                showErrorAlert(context,"No student are linking with that users","Alert")

            } else if (statusCode==115)
            {
                showErrorAlert(context,Resources.getSystem().getString(R.string.status_115),"Alert")

            } else if (statusCode==117)
            {
                showErrorAlert(context,Resources.getSystem().getString(R.string.status_117),"Alert")

            } else if (statusCode==120)
            {
                showErrorAlert(context,"Please enter a valid email address.","Alert")

            } else if (statusCode==121)
            {
                showErrorAlert(context,"The e-mail has already registered.","Alert")

            } else if (statusCode==122)
            {
                showErrorAlert(context,"Not a valid JSON","Alert")

            }
            else if (statusCode==126)
            {
                showErrorAlert(context,Resources.getSystem().getString(R.string.status_126),"Alert")

            }
            else if (statusCode==501)

            {
                showErrorAlert(context,"Too many login attempts.Please try After 1 minute.","Alert")
            }
        }

        fun showErrorAlert(context: Context,message : String,msgHead : String)
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
            btn_Ok.setOnClickListener()
            {
                dialog.dismiss()
            }
          dialog.show()
        }

        fun showSuccessInternetAlert(context: Context)
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
            text_dialog.text = "Network error occurred. Please check your internet connection and try again later"
            alertHead.text = "Alert"
            iconImageView.setBackgroundResource(R.drawable.roundred)
            iconImageView.setImageResource(R.drawable.nonetworkicon)
            btn_Ok.setOnClickListener()
            {
                dialog.dismiss()

            }
            dialog.show()
        }

        fun showSuccessAlert(context: Context,message : String,msgHead : String)
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
                dialog.dismiss()

            }
            dialog.show()
        }

        fun dateParsingToddMMMyyyy(date: String?): String? {
            var strCurrentDate = ""
            var format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            strCurrentDate = format.format(newDate)
            return strCurrentDate
        }
        fun dateParsingToddMMMyyyyBasket(date: String?): String? {
            var strCurrentDate = ""
            var format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            strCurrentDate = format.format(newDate)
            return strCurrentDate
        }
      /*  fun faliurepopup(context: Context) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_ok_layout)
            var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
            var alertHead = dialog.findViewById(R.id.alertHead) as TextView
            var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
            var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
            text_dialog.text ="Something went wrong.Please try again later"

            alertHead.text = "Alert"
            iconImageView.setImageResource(R.drawable.exclamationicon)
            btn_Ok.setOnClickListener()
            {
                dialog.dismiss()
            }
            dialog.show()
        }*/

    }

    var COMMUNICATIONS = "Communications"
    var PARENT_ESSENTIALS = "Parent Essentials"
    var EARLY_YEARS = "Early Years"
    var PRIMARY = "Primary"
    var SECONDARY = "Secondary"
    var IB_PROGRAMME = "IB Programme"
    var PERFORMING_ARTS = "Performing Arts"
    var CCAS = "CCAs"
    var NAE_PROGRAMMES = "NAE Programmes"
    var ABOUT_US = "About Us"
    var CONTACT_US = "Contact Us"


}


