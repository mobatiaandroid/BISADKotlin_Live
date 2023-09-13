package com.mobatia.bisad.fragment.payment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.payment.PaymentCategoryList
import com.mobatia.bisad.activity.payment.PaymentInformationActivity
import com.mobatia.bisad.activity.social_media.SocialMediaDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.canteen.model.CanteenBannerResponseModel
import com.mobatia.bisad.fragment.canteen.model.CanteenSendEmailApiModel
import com.mobatia.bisad.fragment.payment.model.PaymentBannerResponseModel
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailModel
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentFragment: Fragment() {
    lateinit var titleTextView: TextView
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var text_content: TextView
    lateinit var text_dialog: TextView
     var description = ""
     var contactEmail = ""
    lateinit var signRelative: RelativeLayout
    lateinit var back: ImageView
    lateinit var home:ImageView
    lateinit var imageViewSlotInfo:ImageView
    lateinit var sendEmail:ImageView

    //RecyclerView mRecyclerView;
    lateinit var tab_type: String
    lateinit var signUpModule: TextView
    lateinit var descriptionTV: TextView
    lateinit var descriptionTitle: TextView
    lateinit var bannerUrlImageArray: ArrayList<String>
    lateinit var mtitle: RelativeLayout
    lateinit var progressDialog: ProgressBarDialog
    lateinit var informationRelative:RelativeLayout
    lateinit var bannerImagePager: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
    }
    private fun initializeUI(){
        titleTextView = requireView().findViewById(R.id.titleTextView) as TextView
        titleTextView.text = "Payment"
        signRelative = requireView().findViewById(R.id.paymentRelative)
        mtitle = requireView().findViewById(R.id.title)
        informationRelative = requireView().findViewById(R.id.informationRelative)
        bannerImagePager = requireView().findViewById(R.id.bannerImageViewPager)
        // mRecyclerView= (RecyclerView) mRootView.findViewById(R.id.mStaffDirectoryListView);
        // mRecyclerView= (RecyclerView) mRootView.findViewById(R.id.mStaffDirectoryListView);
        //signUpModule = view!!.findViewById(R.id.signUpModule)
        descriptionTV = requireView().findViewById(R.id.descriptionTV)
        progressDialog = ProgressBarDialog(mContext)
        descriptionTitle = requireView().findViewById(R.id.descriptionTitle)
        sendEmail = requireView().findViewById(R.id.sendEmail)

        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callGetPaymentBanner()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }
     signRelative.setOnClickListener(){
         sharedprefs.setStudentID(mContext, "")
         sharedprefs.setStudentName(mContext, "")
         sharedprefs.setStudentPhoto(mContext, "")
         sharedprefs.setStudentClass(mContext, "")
    val intent = Intent(activity, PaymentCategoryList::class.java)

    activity?.startActivity(intent)
}


        informationRelative.setOnClickListener(){
            val intent = Intent(activity, PaymentInformationActivity::class.java)

            activity?.startActivity(intent)
        }



        sendEmail.setOnClickListener {
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
                            sendEmail(
                                text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),
                                com.mobatia.bisad.fragment.home.sharedprefs.getStudentID(mContext).toString(),
                                contactEmail,
                                dialog

                            )

                        } else {
                            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                        }
                    }
                }
            }
            dialog.show()
        }

    }

    fun callGetPaymentBanner()
    {
        progressDialog.show()
        val token = PreferenceData().getaccesstoken(mContext)
        val call: Call<PaymentBannerResponseModel> = ApiClient.getClient.get_payment_banner("Bearer "+token)
        call.enqueue(object : Callback<PaymentBannerResponseModel>
        {
            override fun onFailure(call: Call<PaymentBannerResponseModel>, t: Throwable) {
                progressDialog.hide()
                Log.e("Failedbanner", t.localizedMessage)
            }
            override fun onResponse(call: Call<PaymentBannerResponseModel>, response: Response<PaymentBannerResponseModel>) {
                val responsedata = response.body()
                Log.e("Response", responsedata.toString())
                if (responsedata!!.status==100) {
                    progressDialog.hide()
                    contactEmail=response.body()!!.responseArray.data.contact_email
                    var banner_image=response.body()!!.responseArray.data.image
                    Log.e("mail",contactEmail)
                    Log.e("des",response.body()!!.responseArray.data.description)
                    Log.e("im",banner_image)
                    if (contactEmail.isEmpty())
                    {
                        sendEmail.visibility=View.GONE
                        mtitle.visibility=View.GONE
                    }
                    else{
                        sendEmail.visibility=View.VISIBLE
                        mtitle.visibility=View.VISIBLE
                    }
                    if(response.body()!!.responseArray.data.description.isEmpty())
                    {
                        descriptionTV.visibility=View.GONE
                    }
                    else{
                        descriptionTV.visibility=View.VISIBLE
                        descriptionTV.text=response.body()!!.responseArray.data.description
                    }
                    if (banner_image.isNotEmpty()) {
                        Log.e("bann","notemp")
                        Glide.with(mContext) //1
                            .load(banner_image)
                            .into(bannerImagePager)
                    } else {
                        Log.e("bann","emp")
                        Glide.with(mContext)
                            .load(R.drawable.default_banner)
                            .into(bannerImagePager)
                    }

                }else if (response.body()!!.status == 116) {
                    progressDialog.hide()
                } else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                    } else {
                        //check status code checks
                        progressDialog.hide()
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                    }
                }
            }

        })
   /* {
        val token = PreferenceData().getaccesstoken(mContext)
        val call: Call<PaymentBannerResponseModel> = ApiClient.getClient.get_payment_banner("Bearer "+token)
        call.enqueue(object : Callback<PaymentBannerResponseModel>
        {
            override fun onFailure(call: Call<PaymentBannerResponseModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
            }
            override fun onResponse(call: Call<PaymentBannerResponseModel>, response: Response<PaymentBannerResponseModel>) {
                val responsedata = response.body()
                Log.e("BannerRes", responsedata!!.status.toString())
                if (responsedata!!.status==100) {

                    contactEmail=response.body()!!.responseArray.data.contact_email
                    var banner_image=response.body()!!.responseArray.data.image
                    Log.e("mail",contactEmail)
                    Log.e("des",response.body()!!.responseArray.data.description)
                    Log.e("im",banner_image)
                    if (contactEmail.isEmpty())
                    {
                        sendEmail.visibility=View.GONE
                        mtitle.visibility=View.GONE
                    }
                    else{
                        sendEmail.visibility=View.VISIBLE
                        mtitle.visibility=View.VISIBLE
                    }
                    if(response.body()!!.responseArray.data.description.isEmpty())
                    {
                        descriptionTV.visibility=View.GONE
                    }
                    else{
                        descriptionTV.visibility=View.VISIBLE
                        descriptionTV.text=response.body()!!.responseArray.data.description
                    }
                    if (banner_image.isNotEmpty()) {
Log.e("bann","notemp")
                        Glide.with(mContext) //1
                            .load(banner_image)
                            .into(bannerImagePager)
                    } else {
                        Log.e("bann","emp")
                        Glide.with(mContext)
                            .load(R.drawable.default_banner)
                            .into(bannerImagePager)
                    }

                }else if (response.body()!!.status == 116) {

                } else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                    }
                }
            }

        })*/

    }


    fun sendEmail(title: String,
                  message: String,
                  studentID: String,
                  staffEmail: String,
                  dialog: Dialog
    ){
        val token = com.mobatia.bisad.fragment.home.sharedprefs.getaccesstoken(mContext)
        val sendMailBody = CanteenSendEmailApiModel( staffEmail, title, message)
        val call: Call<SendStaffEmailModel> =
            ApiClient.getClient.sendEmailCanteen(sendMailBody, "Bearer " + token)
        call.enqueue(object : Callback<SendStaffEmailModel> {
            override fun onFailure(call: Call<SendStaffEmailModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<SendStaffEmailModel>, response: Response<SendStaffEmailModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                Log.e("Response Signup", responsedata.toString())
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