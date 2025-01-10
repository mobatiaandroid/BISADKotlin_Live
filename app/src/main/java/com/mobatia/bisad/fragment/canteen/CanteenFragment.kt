package com.mobatia.bisad.fragment.canteen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.CanteenPaymentActivity
import com.mobatia.bisad.activity.canteen.InformationActivity
import com.mobatia.bisad.activity.canteen.PreOrderActivity
import com.mobatia.bisad.activity.canteen.model.TimeExceedModel
import com.mobatia.bisad.activity.settings.re_enrollment.model.EnrollmentStatusModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.canteen.model.CanteenBannerResponseModel
import com.mobatia.bisad.fragment.canteen.model.CanteenSendEmailApiModel
import com.mobatia.bisad.fragment.home.sharedprefs
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailApiModel
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CanteenFragment  : Fragment() {
    lateinit var mContext: Context
    lateinit var progress: ProgressBarDialog
    lateinit var email_icon: ImageView
    lateinit var preorder_image: LinearLayout
    lateinit var information_image: LinearLayout
    lateinit var payment_image: LinearLayout
    lateinit var bannerImg:ImageView
    lateinit var title: TextView
    lateinit var description: TextView
    lateinit var contactEmail:String

     var walletTopUpLimit:Int=0
     var walletTopUpLimit_str:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.canteen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFn()
        onClick()
        callGetCanteenBanner()

    }
    private fun initFn(){
        mContext = requireContext()
        title=view?.findViewById(R.id.title)!!
        title.text = "Lunch Box"
        progress = ProgressBarDialog(mContext)

        email_icon = requireView().findViewById(R.id.email_icon)!!
        preorder_image = requireView().findViewById(R.id.preOrderLinear)!!
        information_image = requireView().findViewById(R.id.informationLinear)!!
        payment_image = requireView().findViewById(R.id.paymentLinear)!!
        bannerImg = requireView().findViewById(R.id.bannerimagecanteen)!!
        description = requireView().findViewById(R.id.description)!!


    }
    private fun onClick() {
        email_icon.setOnClickListener {
            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            val btn_submit = dialog.findViewById<Button>(R.id.submitButton)
            val btn_cancel = dialog.findViewById<Button>(R.id.cancelButton)
            val text_dialog = dialog.findViewById<EditText?>(R.id.text_dialog)
            val text_content = dialog.findViewById<EditText>(R.id.text_content)

            btn_cancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })

            btn_submit.setOnClickListener {
                if (text_dialog.text.toString().trim().equals("")) {
                    InternetCheckClass.showErrorAlert(
                        mContext,
                        "Please enter your subject",
                        "Alert"
                    )

                } else {
                    if (text_content.text.toString().trim().equals("")) {
                        InternetCheckClass.showErrorAlert(
                            mContext,
                            "Please enter your content",
                            "Alert"
                        )

                    } else {
                        // progressDialog.visibility = View.VISIBLE
                        val aniRotate: Animation =
                            AnimationUtils.loadAnimation(context, R.anim.linear_interpolator)
                        // progressDialog.startAnimation(aniRotate)
                        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                        if (internetCheck) {
                            emailvalidationcheck(text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),
                                sharedprefs.getStudentID(mContext).toString(),
                                contactEmail,
                                dialog)
                           /* sendEmail(
                                text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),
                                sharedprefs.getStudentID(mContext).toString(),
                                contactEmail,
                                dialog

                            )*/

                        } else {
                            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                        }
                    }
                }
            }
            dialog.show()
        }
        preorder_image.setOnClickListener {
            val i = Intent(mContext, PreOrderActivity::class.java)
            mContext.startActivity(i)
        }
        information_image.setOnClickListener {
            val i = Intent(mContext, InformationActivity::class.java)
            mContext.startActivity(i)
        }
        payment_image.setOnClickListener {
            val i = Intent(mContext, CanteenPaymentActivity::class.java)
            i.putExtra("WALLET_TOPUP_LIMIT",walletTopUpLimit_str.toString())
            mContext.startActivity(i)
        }
    }


    fun callGetCanteenBanner()
    {
        progress.show()
        val token = PreferenceData().getaccesstoken(mContext)
        val call: Call<CanteenBannerResponseModel> = ApiClient(mContext).getClient.get_canteen_banner("Bearer "+token)
        call.enqueue(object : Callback<CanteenBannerResponseModel>
        {
            override fun onFailure(call: Call<CanteenBannerResponseModel>, t: Throwable) {
                progress.hide()
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<CanteenBannerResponseModel>, response: Response<CanteenBannerResponseModel>) {
                val responsedata = response.body()
                if (responsedata!!.status==100) {
                    progress.hide()

                    contactEmail=response.body()!!.responseArray.data.contact_email
                    walletTopUpLimit_str=response.body()!!.responseArray.data.wallet_topup_limit
                    PreferenceData().setCanteenTopUpLimit(mContext,walletTopUpLimit_str)
                    walletTopUpLimit=walletTopUpLimit_str.toInt()
                    var banner_image=response.body()!!.responseArray.data.image
                    var trn_no=response.body()!!.responseArray.data.trn_no
                    PreferenceData().setTrnNo(mContext,trn_no)
                    if (contactEmail.equals(""))
                    {
                        email_icon.visibility=View.GONE
                    }
                    else{
                        email_icon.visibility=View.VISIBLE
                    }
                    if(response.body()!!.responseArray.data.description.equals(""))
                    {
                        description.visibility=View.GONE
                    }
                    else{
                        description.visibility=View.VISIBLE
                        description.text=response.body()!!.responseArray.data.description
                    }
                    if (banner_image != "") {

                        Glide.with(mContext) //1
                            .load(banner_image)
                            .into(bannerImg)
                    } else {
                        Glide.with(mContext)
                            .load(R.drawable.default_banner)
                            .into(bannerImg)
                    }

                }else if (response.body()!!.status == 116) {
                    progress.hide()
                } else {
                    if (response.body()!!.status == 103) {
                        progress.hide()
                        //validation check error
                    } else {
                        //check status code checks
                        progress.hide()
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                    }
                }
            }

        })

    }

    fun emailvalidationcheck( title: String,
                              message: String,
                              studentID: String,
                              staffEmail: String,
                              dialog: Dialog){
        val EMAIL_PATTERN :String=
            "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
        val pattern :String= "^([a-zA-Z ]*)$"

        if (title.equals("")) {
            val toast: Toast = Toast.makeText(
                mContext, mContext.getResources().getString(
                    com.mobatia.bisad.R.string.enter_subjects
                ), Toast.LENGTH_SHORT
            )
            toast.show()
        } else {
            if (message.equals("")) {
                val toast: Toast = Toast.makeText(
                    mContext, mContext.getResources().getString(
                        R.string.enter_contents
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            } else if (staffEmail.matches(EMAIL_PATTERN.toRegex())) {
                if (title.toString().trim().matches(pattern.toRegex())) {
                    if (title.toString().length>=500){
                        Toast.makeText(mContext, "Subject is too long", Toast.LENGTH_SHORT).show()

                    }else{
                        if (message.toString().trim().matches(pattern.toRegex())) {
                            if (message.length<=500) {
                                if (InternetCheckClass.isInternetAvailable(mContext)) {
                                    sendEmail(
                                        title,
                                        message,
                                        studentID,
                                        contactEmail,
                                        dialog

                                    )
                                } else {
                                    CommonFunctions.faliurepopup(mContext)
                                }
                            }else{
                                Toast.makeText(context, "Message is too long", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            val toast: Toast = Toast.makeText(
                                mContext, mContext.getResources().getString(
                                    R.string.enter_valid_contents
                                ), Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
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
    }
    fun sendEmail(title: String,
                          message: String,
                          studentID: String,
                          staffEmail: String,
                          dialog: Dialog
    ){
        progress.show()
        val token = sharedprefs.getaccesstoken(mContext)
        val sendMailBody = CanteenSendEmailApiModel( staffEmail, title, message)
        val call: Call<SendStaffEmailModel> =
            ApiClient(mContext).getClient.sendEmailCanteen(sendMailBody, "Bearer " + token)
        call.enqueue(object : Callback<SendStaffEmailModel> {
            override fun onFailure(call: Call<SendStaffEmailModel>, t: Throwable) {

                progress.hide()
                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(call: Call<SendStaffEmailModel>, response: Response<SendStaffEmailModel>) {
                val responsedata = response.body()
                progress.hide()
                if (responsedata != null) {
                    try {


                        if (response.body()!!.status==100) {
                            dialog.dismiss()
                            showSuccessAlert(
                                mContext,
                                "Email sent Successfully ",
                                "Success",
                                dialog
                            )
                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(mContext)
                                sendEmail(
                                    title,
                                    message,
                                    studentID,
                                    staffEmail,
                                    dialog
                                )
                            } else {
                                if (response.body()!!.status==103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
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
}