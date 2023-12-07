package com.mobatia.bisad.activity.parent_meetings

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.parent_meetings.adapter.ReviewAdapter
import com.mobatia.bisad.activity.parent_meetings.model.confirm_pta.PtaConfirmationApiModel
import com.mobatia.bisad.activity.parent_meetings.model.confirm_pta.PtaConfirmationModel
import com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaListingModel
import com.mobatia.bisad.activity.parent_meetings.model.review_list.PtaReviewListModel
import com.mobatia.bisad.activity.parent_meetings.model.review_list.ReviewListModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewAppointmentsRecyclerViewActivity : AppCompatActivity() {

    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var progressDialog: RelativeLayout
    lateinit var title: TextView
    lateinit var review_rec: RecyclerView
    lateinit var review_list: ArrayList<ReviewListModel>
    lateinit var idList:ArrayList<Int>
    lateinit var back: ImageView
    lateinit var home_icon: ImageView
    lateinit var confirm_tv:TextView
    var confimVisibility:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parentmeeting_review)
        sharedprefs = PreferenceData()
        init()
    }

     fun init() {
        mContext = this
        progressDialog = findViewById<RelativeLayout>(R.id.progressDialog)
        title = findViewById(R.id.heading)
         title.text = "Review Appointments"
        confirm_tv=findViewById(R.id.confirmTV)
        review_list = ArrayList()
        review_rec = findViewById(R.id.recycler_review)
        back = findViewById(R.id.btn_left)
        home_icon = findViewById(R.id.logoClickImgView)

        idList= ArrayList()
        reviewlistcall()
        back.setOnClickListener {
            /*val intent = Intent(mContext, ParentMeetingDetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)*/
           finish()
        }
        home_icon.setOnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
confirm_tv.setOnClickListener {
    showerrorConfirm(mContext,"Do you want to confirm appointment?","Alert")
    //callConfirmPta()
}


    }

     fun reviewlistcall() {
        review_list=ArrayList()
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<PtaReviewListModel> =
            ApiClient.getClient.pta_review_list("Bearer " + token)
        call.enqueue(object : Callback<PtaReviewListModel> {
            override fun onFailure(call: Call<PtaReviewListModel>, t: Throwable) {

                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)
            }

            override fun onResponse(
                call: Call<PtaReviewListModel>,
                response: Response<PtaReviewListModel>
            ) {
                val responsedata = response.body()
                progressDialog.visibility = View.GONE

                if (responsedata!!.responsecode.equals("200")){
                    review_list.addAll(responsedata.response.data)
                    if (review_list.size>0){
                        review_rec.layoutManager=LinearLayoutManager(mContext)
                        var review_adapter=ReviewAdapter(mContext,review_list,ReviewAppointmentsRecyclerViewActivity(),
                        progressDialog,review_rec,idList,confirm_tv)
                        review_rec.adapter=review_adapter
                    }else{
                        showerror(mContext,"No Appointments Available.","Alert")
                    }

                    for (i in review_list.indices){
                        if (review_list[i].status.equals("2")&&review_list[i].booking_open.equals("y")){
                            idList.add(review_list[i].id.toInt())
                           // confirm_tv.visibility=View.VISIBLE
                            confimVisibility=true

                        }/*else{
                            confirm_tv.visibility=View.GONE
                        }*/
                    }
                    if (confimVisibility==true){
                        confirm_tv.visibility=View.VISIBLE
                    }else{
                        confirm_tv.visibility=View.GONE
                    }

                }else if(response.body()!!.response.statuscode.equals("116"))
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(com.mobatia.bisad.fragment.home.mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                        reviewlistcall()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.response.statuscode.toInt(),
                        com.mobatia.bisad.fragment.home.mContext
                    )
                }
            }


        })
    }


    private fun callConfirmPta(){
        val token = sharedprefs.getaccesstoken(mContext)
        var idString:String=idList.toString()
        var ptaConfirm= PtaConfirmationApiModel(idString)
        val call: Call<PtaConfirmationModel> =
            ApiClient.getClient.pta_confirmation(ptaConfirm,"Bearer " + token)
        call.enqueue(object : Callback<PtaConfirmationModel> {
            override fun onFailure(call: Call<PtaConfirmationModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)
            }

            override fun onResponse(
                call: Call<PtaConfirmationModel>,
                response: Response<PtaConfirmationModel>
            ) {
                val responsedata = response.body()
                // progressDialog.visibility = View.GONE
if (responsedata!!.statuscode.equals("303")){

    showSuccess(com.mobatia.bisad.fragment.home.mContext,"Successfully confirmed appointment.","Success")
    //reviewlistcall()
}else if(response.body()!!.statuscode.equals("116"))
{
    var internetCheck = InternetCheckClass.isInternetAvailable(com.mobatia.bisad.fragment.home.mContext)
    if (internetCheck)
    {
        AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
        callConfirmPta()
    }
    else{
        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
    }

}
else
{
    InternetCheckClass.checkApiStatusError(response.body()!!.statuscode.toInt(),
        com.mobatia.bisad.fragment.home.mContext
    )
}

            }


        })
    }
    private fun showSuccess(context: Context,message : String,msgHead : String)
    {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.tick)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            reviewlistcall()

        }
        dialog.show()
    }
    private fun showerror(context: Context,message : String,msgHead : String)
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
    private fun showerrorConfirm(context: Context,message : String,msgHead : String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.questionmark_icon)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            callConfirmPta()

        }
        btn_Cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}