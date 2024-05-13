package com.mobatia.bisad.constants


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.mobatia.bisad.R


class CommonFunctions {
    companion object{
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

    }
}