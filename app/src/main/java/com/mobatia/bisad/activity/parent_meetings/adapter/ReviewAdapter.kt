package com.mobatia.bisad.activity.parent_meetings.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.parent_meetings.ReviewAppointmentsRecyclerViewActivity
import com.mobatia.bisad.activity.parent_meetings.model.cancel.PtaCancelApiModel
import com.mobatia.bisad.activity.parent_meetings.model.cancel.PtaCancelModel
import com.mobatia.bisad.activity.parent_meetings.model.confirm_pta.PtaConfirmationApiModel
import com.mobatia.bisad.activity.parent_meetings.model.confirm_pta.PtaConfirmationModel
import com.mobatia.bisad.activity.parent_meetings.model.review_list.PtaReviewListModel
import com.mobatia.bisad.activity.parent_meetings.model.review_list.ReviewListModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.home.sharedprefs
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


internal class ReviewAdapter (var context: Context,var review_list:ArrayList<ReviewListModel>,
                              var reviewActivity:ReviewAppointmentsRecyclerViewActivity,
                              var progressDialog: RelativeLayout,var review_rec: RecyclerView,
                              var idList:ArrayList<Int>,var confirm_tv:TextView
) :
    RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {
    lateinit var id_list:ArrayList<Int>
    var confimVisibility:Boolean=false
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var student_name:TextView=view.findViewById(R.id.studNameTV)
        var student_class:TextView=view.findViewById(R.id.classNameTV)
        var staff_name:TextView=view.findViewById(R.id.staffNameTV)
        var start_date:TextView=view.findViewById(R.id.reserveDateTimeTextView)
        var end_date:TextView=view.findViewById(R.id.expireDateTimeTextView)
        var student_image:ImageView=view.findViewById(R.id.imgView)
        var cancel_img:ImageView=view.findViewById(R.id.cancelAppointment)
        var confirm_img:ImageView=view.findViewById(R.id.confirmAppointment)
        var confirm_imgview:ImageView=view.findViewById(R.id.confirmationImageview)
        var addtocalendar_img:ImageView=view.findViewById(R.id.addTocalendar)
        var vpml_img:Button=view.findViewById(R.id.vpml)
         var sharedprefs: PreferenceData= PreferenceData()

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_review_list, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

       // reviewActivity.fnctiondemo()
        holder.student_name.text = review_list[position].student
        holder.student_class.text = review_list[position].class_name
        holder.staff_name.text = review_list[position].staff
var date_sel=review_list[position].date
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = date_sel
        val date: Date = inputFormat.parse(inputDateStr)
       var date_frmt = outputFormat.format(date)
        var st_time=review_list[position].start_time
        val inputFormat2: DateFormat = SimpleDateFormat("hh:mm:ss")
        val outputFormat2: DateFormat = SimpleDateFormat("hh:mm aa")
        val inputDateStr2 = st_time
        val date2: Date = inputFormat2.parse(inputDateStr2)
        var st_date_frmt = outputFormat2.format(date2)
        var end_date=review_list[position].end_time
        val inputFormat3: DateFormat = SimpleDateFormat("hh:mm:ss")
        val outputFormat3: DateFormat = SimpleDateFormat("hh:mm aa")
        val inputDateStr3 = end_date
        val date3: Date = inputFormat3.parse(inputDateStr3)
        var end_date_frmt = outputFormat3.format(date3)
        var book_endDate=review_list[position].book_end_date
        val inputFormat4: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat4: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr4 = book_endDate
        val date4: Date = inputFormat4.parse(inputDateStr4)
        var bookend_date_frmt = outputFormat4.format(date4)

        var st_date=date_frmt + " "+st_date_frmt+" - "+end_date_frmt
        holder.start_date.text = st_date
        var en_date=bookend_date_frmt
        holder.end_date.text = "Confirm/Cancellation closes at "+ en_date

        var photo=review_list[position].student_photo
        if (review_list[position].student_photo.isEmpty()){
            holder.student_image.setImageResource(R.drawable.student)
        }
        else{
            Glide.with(context) //1
                .load(photo)
                .placeholder(R.drawable.student)
                .error(R.drawable.student)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .transform(CircleCrop()) //4
                .into(holder.student_image)
        }
        if(review_list[position].status.equals("3")&&review_list[position].booking_open.equals("y")){
            holder.confirm_img.visibility = GONE
            holder.cancel_img.visibility = VISIBLE
            holder.addtocalendar_img.visibility = VISIBLE
            if(review_list[position].vpml.equals("")){
                holder.vpml_img.visibility = GONE
            }
            else{
                holder.vpml_img.visibility = VISIBLE
            }
        }else if (review_list[position].status.equals("2")&&review_list[position].booking_open.equals("y")){
            holder.confirm_imgview.setBackgroundResource(R.drawable.doubtinparticipatingsmallicon)
            holder.confirm_img.visibility = VISIBLE
            holder.confirm_img.setBackgroundResource(R.drawable.confirm)
            holder.cancel_img.visibility = VISIBLE
            holder.addtocalendar_img.visibility = GONE
            holder.vpml_img.visibility = GONE
            }
        else if(review_list[position].status.equals("3")&&review_list[position].booking_open.equals("n")){
            holder.confirm_imgview.setBackgroundResource(R.drawable.tick_icon)
            holder.addtocalendar_img.visibility = VISIBLE
            holder.confirm_img.visibility = GONE
            holder.cancel_img.visibility = VISIBLE
            holder.confirm_img.setImageResource(R.drawable.confirm_dis)
            holder.cancel_img.setImageResource(R.drawable.cancel_dis)
            if (review_list[position].vpml.equals("")) {
                holder.vpml_img.visibility = GONE
            } else {
                holder.vpml_img.visibility = VISIBLE
            }
        }
        else if(review_list[position].status.equals("2")&&review_list[position].booking_open.equals("n")){
            holder.confirm_imgview.setBackgroundResource(R.drawable.doubtinparticipatingsmallicon)
            holder.addtocalendar_img.visibility = GONE
            holder.confirm_img.visibility = VISIBLE
            holder.cancel_img.visibility = VISIBLE
            holder.confirm_img.setImageResource(R.drawable.confirm_dis)
            holder.cancel_img.setImageResource(R.drawable.cancel_dis)
            holder.vpml_img.visibility = GONE
        }
        holder.confirm_img.setOnClickListener {
            id_list= ArrayList()
            var id_sel=review_list[position].id.toInt()
            id_list.add(id_sel)
            showerrorConfirm(context,"Do you want to confirm appointment?","Alert",id_list)

            //confirmPtaApi(id_list)
        }
        holder.cancel_img.setOnClickListener {
            id_list= ArrayList()
            var id_sel=review_list[position].id.toInt()
            id_list.add(id_sel)
            showerrorCancel(context,"Do you want to cancel appointment?","Alert",id_list)
        }
        holder.vpml_img.setOnClickListener {
            if(review_list[position].vpml.equals("")){

            }else {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(review_list[position].vpml))
                context.startActivity(i)
            }
        }
        holder.addtocalendar_img.setOnClickListener {
            var startTime: Long = 0
            var endTime: Long = 0
            var vpmlURL = ""
            try {
//                    Date dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mVideosModelArrayList.get(0).getDateAppointment());
                val dateStart = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(
                    review_list[position].date
                        .toString() + " " + review_list[position].start_time
                )
                startTime = dateStart.time
                val dateEnd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(
                    review_list[position].date
                        .toString() + " " + review_list[position].end_time
                )
                endTime = dateEnd.time
                vpmlURL = if (review_list[position].vpml.equals("")) {
                    ""
                } else {
                    review_list[position].vpml
                }
            } catch (e: Exception) {
            }

            val cal = Calendar.getInstance()
            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra("beginTime", startTime)
            intent.putExtra("allDay", true)
            intent.putExtra("rrule", "FREQ=YEARLY")
            intent.putExtra("endTime", endTime)
            intent.putExtra("title", "PARENT MEETING")
            intent.putExtra("description", vpmlURL)
            context.startActivity(intent)
        }


    }
    override fun getItemCount(): Int {

        return review_list.size

    }
    private fun confirmPtaApi(id_sel:ArrayList<Int>){

        val token = sharedprefs.getaccesstoken(context)
        var idString:String=id_sel.toString()
        var ptaConfirm= PtaConfirmationApiModel(idString)
        val call: Call<PtaConfirmationModel> =
            ApiClient(context).getClient.pta_confirmation(ptaConfirm,"Bearer " + token)
        call.enqueue(object : Callback<PtaConfirmationModel> {
            override fun onFailure(call: Call<PtaConfirmationModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)
            }

            override fun onResponse(
                call: Call<PtaConfirmationModel>,
                response: Response<PtaConfirmationModel>
            ) {
                val responsedata = response.body()
               // progressDialog.visibility = View.GONE
if (responsedata!!.statuscode.equals("303")){
    showSuccess(context,"Successfully confirmed appointment.","Success")

}else if(response.body()!!.statuscode.equals("116"))
{
    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
    if (internetCheck)
    {
        AccessTokenClass.getAccessToken(mContext)
        confirmPtaApi(id_sel)
    }
    else{
        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
    }

}
else
{
    InternetCheckClass.checkApiStatusError(response.body()!!.statuscode.toInt(),mContext)
}
            }


        })

    }

    private fun cancelPtaApi(id_sel:ArrayList<Int>){

        val token = sharedprefs.getaccesstoken(context)
        var idString:String=id_sel.toString()
        var ptaCancel= PtaCancelApiModel(idString)
        val call: Call<PtaCancelModel> =
            ApiClient(context).getClient.pta_cancel(ptaCancel,"Bearer " + token)
        call.enqueue(object : Callback<PtaCancelModel> {
            override fun onFailure(call: Call<PtaCancelModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)
            }

            override fun onResponse(
                call: Call<PtaCancelModel>,
                response: Response<PtaCancelModel>
            ) {
                val responsedata = response.body()
                // progressDialog.visibility = View.GONE
                if (responsedata!!.statuscode.equals("303")){
                    showSuccess(context,"Successfully cancelled appointment.","Success")


                }else if(response.body()!!.statuscode.equals("116"))
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        cancelPtaApi(id_sel)
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.statuscode.toInt(),mContext)
                }
            }


        })

    }

    private fun showSuccess(context: Context,message : String,msgHead : String)
    {
        val dialog = Dialog(context)
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
           /* reviewActivity.init()
            reviewActivity.reviewlistcall()*/
           /* val intent = Intent(context, ReviewAppointmentsRecyclerViewActivity::class.java)
            context.startActivity(intent)*/


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
        iconImageView.setImageResource(R.drawable.questionmark_icon)
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
    private fun showerrorCancel(context: Context,message : String,msgHead : String,id_list:ArrayList<Int>)
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
            cancelPtaApi(id_list)

        }
        btn_Cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun showerrorConfirm(context: Context,message : String,msgHead : String,id_list:ArrayList<Int>)
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
            confirmPtaApi(id_list)

        }
        btn_Cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun reviewlistcall() {
        //progressDialog.visibility = View.VISIBLE
        review_list= ArrayList()
        review_rec.layoutManager= LinearLayoutManager(context)
        var review_adapter=ReviewAdapter(context,review_list,ReviewAppointmentsRecyclerViewActivity(),
            progressDialog,review_rec,idList,confirm_tv)
        review_rec.adapter=review_adapter
        val token = sharedprefs.getaccesstoken(context)
        val call: Call<PtaReviewListModel> =
            ApiClient(context).getClient.pta_review_list("Bearer " + token)
        call.enqueue(object : Callback<PtaReviewListModel> {
            override fun onFailure(call: Call<PtaReviewListModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)
            }

            override fun onResponse(
                call: Call<PtaReviewListModel>,
                response: Response<PtaReviewListModel>
            ) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE

                if (responsedata!!.responsecode.equals("200")){
                    review_list.addAll(responsedata.response.data)
                    if (review_list.size>0){
                        notifyDataSetChanged()
                        review_rec.layoutManager= LinearLayoutManager(context)
                        var review_adapter=ReviewAdapter(context,review_list,ReviewAppointmentsRecyclerViewActivity(),
                            progressDialog,review_rec,idList,confirm_tv)
                        review_rec.adapter=review_adapter

                    }else{
                        showerror(context,"No Appointments Available.","Alert")
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
                    if (confimVisibility){
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
}