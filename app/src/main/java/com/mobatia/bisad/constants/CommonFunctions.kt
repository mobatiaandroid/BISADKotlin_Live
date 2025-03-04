package com.mobatia.bisad.constants


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartResModel
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CommonFunctions {
    companion object{

        lateinit var cart_list: ArrayList<CanteenCartResModel>
        var runMethod: String = "client"


        fun faliurepopup(context: Context) {
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
        }
        fun replace(str: String): String? {
            return str.replace(" ".toRegex(), "%20")
        }
        fun hideKeyBoard(context: Context) {
            val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(
                    (context as Activity).currentFocus!!.getWindowToken(), 0
                )
            }
        }

        fun showDialogAlertDismiss(
            activity: Activity?,
            msgHead: String?,
            msg: String?,
            ico: Int,
            bgIcon: Int
        ) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_ok_layout)
            val icon = dialog.findViewById<ImageView>(R.id.iconImageView)
            icon.setBackgroundResource(bgIcon)
            icon.setImageResource(ico)
            val text = dialog.findViewById<TextView>(R.id.text_dialog)
            val textHead = dialog.findViewById<TextView>(R.id.alertHead)
            text.text = msg
            textHead.text = msgHead
            val dialogButton = dialog.findViewById<Button>(R.id.btn_Ok)
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
        fun showSuccessAlert(context: Context, message: String, msgHead: String, mdialog: Dialog) {
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

        fun dateParsingyyyyMMddToDdMmmYyyy(date: String?): String? {
            var strCurrentDate = ""
            var format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            format = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            strCurrentDate = format.format(newDate)
            return strCurrentDate
        }
     /*   fun emailvalidation(mContext:Context,text_dialog:TextView,text_content:EditText,surveyEmail:String){
            val EMAIL_PATTERN :String=
                "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
            val pattern :String= "^([a-zA-Z ]*)$"

            if (text_dialog.equals("")) {
                val toast: Toast = Toast.makeText(
                    mContext, mContext.getResources().getString(
                        com.mobatia.bisad.R.string.enter_subjects
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                if (text_content.equals("")) {
                    val toast: Toast = Toast.makeText(
                        mContext, mContext.getResources().getString(
                            R.string.enter_contents
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (surveyEmail.matches(EMAIL_PATTERN.toRegex())) {
                    if (text_dialog.text.toString().trim().matches(pattern.toRegex())) {


                        if (text_content.getText().toString().trim().matches(pattern.toRegex())) {
                            if (InternetCheckClass.isInternetAvailable(mContext)) {
                                sendEmailToStaff(

                                    surveyEmail,
                                    dialogThank,
                                    dialog
                                )
                            } else {
                               *//* AppUtils.showDialogAlertDismiss(
                                    mContext as Activity?,
                                    "Network Error",
                                    getString(R.string.no_internet),
                                    android.R.drawable.nonetworkicon,
                                    android.R.drawable.roundred
                                )*//*
                            }
                        } else {
                            val toast: Toast = Toast.makeText(
                                mContext, mContext.getResources().getString(
                                    R.string.enter_valid_contents
                                ), Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    } else {
                        val toast: Toast = Toast.makeText(
                            mContext, mContext.getResources().getString(
                                R.string.enter_valid_subjects
                            ), Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                        mContext, mContext.getResources().getString(
                            R.string.enter_valid_mail
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
        }*/

        fun dateConversionddmmyyyytoddMMYYYY(inputDate: String?): String? {
            var mDate = ""
            try {
                val date: Date
                val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                date = formatter.parse(inputDate)
                //Subtracting 6 hours from selected time
                val time = date.time

                //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
                val formatterFullDate = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                mDate = formatterFullDate.format(time)
            } catch (e: Exception) {
//			Log.d("Exception", "" + e);
            }
            return mDate
        }
        fun isDeveloperModeEnabled(context: Context): Boolean {
            try {
                val devMode = Settings.Global.getInt(
                    context.contentResolver,
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED
                )
                return devMode == 1 // 1 indicates Developer Mode is ON
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
                return false // Developer Mode setting not found, assume it's off
            }
        }
        fun showDeviceIsDeveloperPopUp(activity: Activity) {
            val dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_ok_layout)
            val icon = dialog.findViewById<ImageView>(R.id.iconImageView)
            icon.setBackgroundResource(R.drawable.round)
            icon.setImageResource(R.drawable.exclamationicon)
            val text = dialog.findViewById<TextView>(R.id.text_dialog)
            val textHead = dialog.findViewById<TextView>(R.id.alertHead)
            text.text =
                "You have enabled Developer options/USB debugging on your phone. Please disable both to use the app for security reasons."
            textHead.text = "Disable Developer Option"

            val dialogButton = dialog.findViewById<Button>(R.id.btn_Ok)
            dialogButton.setOnClickListener {
                dialog.dismiss()
                activity.finish()
            }

            dialog.show()
        }
        fun validateEditText(editText: EditText): Boolean {
            val originalContent = editText.text.toString().trim { it <= ' ' }
            val cleanedContent = cleanHtmlContent(originalContent) // Removes HTML content

            return if (originalContent == cleanedContent) {
                // No HTML content found
                true
            } else {
                // HTML content was found
                false
            }
        }
        fun cleanHtmlContent(input: String?): String {
            // Convert HTML to Spanned
            val spanned = HtmlCompat.fromHtml(input!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
            // Convert Spanned back to plain text and trim it
            return spanned.toString().trim { it <= ' ' }
        }

    }
}