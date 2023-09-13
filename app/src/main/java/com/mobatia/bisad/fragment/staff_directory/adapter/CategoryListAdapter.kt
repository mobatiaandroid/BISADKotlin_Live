package com.mobatia.bisad.fragment.staff_directory.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.home.sharedprefs
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailApiModel
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailModel
import com.mobatia.bisad.fragment.staff_directory.model.StaffDetailList
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class CategoryListAdapter (var context:Context, var detailList:ArrayList<StaffDetailList>, var  searchbtn: ImageView,
                                    var searchtxt: EditText
) :
    RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>() {
    var staffEmail:String=""
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.staffName)
        var role: TextView = view.findViewById(R.id.staffRole)
        var image:ImageView=view.findViewById(R.id.staffImg)
        var linear:LinearLayout=view.findViewById(R.id.linear)
        var mail_icon:ImageView=view.findViewById(R.id.mailImage)
        var mail_linear:LinearLayout=view.findViewById(R.id.linear_mail)

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_stafftitle_list, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = detailList[position].name
        holder.role.text = detailList[position].department
        var staff_image=detailList[position].staff_photo



        if (staff_image.isNotEmpty()) {
            context.let {
                Glide.with(it)
                    .load(staff_image)
                    .into(holder.image)
            }
        }
        else
        {
            holder.image.setBackgroundResource(R.drawable.teacher_icon)
        }
        if (detailList[position].email.isEmpty()){
            holder.mail_icon.visibility = GONE
        }
        else{
            holder.mail_icon.visibility = VISIBLE
           staffEmail=detailList[position].email
        }
        holder.mail_linear.setOnClickListener {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
            val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
            var text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
            var text_content = dialog.findViewById<View>(R.id.text_content) as EditText
                    staffEmail=detailList[position].email
            submitButton.setOnClickListener {
                if (text_dialog.text.toString().trim().equals("")) {
                    InternetCheckClass.showErrorAlert(
                        context,
                        "Please enter your subject",
                        "Alert"
                    )

                } else {
                    if (text_content.text.toString().trim().equals("")) {
                        InternetCheckClass.showErrorAlert(
                            context,
                            "Please enter your content",
                            "Alert"
                        )

                    } else {
                       // progressDialog.visibility = View.VISIBLE
                        val aniRotate: Animation =
                            AnimationUtils.loadAnimation(context, R.anim.linear_interpolator)
                       // progressDialog.startAnimation(aniRotate)
                        var internetCheck = InternetCheckClass.isInternetAvailable(context)
                        if (internetCheck) {
                            teachercontactApi(
                                text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),
                                sharedprefs.getStudentID(context).toString(),
                                staffEmail,
                                dialog

                            )

                        } else {
                            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                        }
                    }
                }
            }
            dialogCancelButton.setOnClickListener { dialog.dismiss()
            }
            dialog.show()
        }
        holder.linear.setOnClickListener {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_staffbio)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            var name=dialog.findViewById<TextView>(R.id.nametxt)
            name.text = detailList[position].name
            var department=dialog.findViewById<TextView>(R.id.depttxt)
            department.text = detailList[position].department
            var role=dialog.findViewById<TextView>(R.id.roletxt)
            role.text = detailList[position].role
            var linear_bio:LinearLayout=dialog.findViewById(R.id.linear_bio)
            var linear_nodatae:TextView=dialog.findViewById(R.id.bioTxxttxt)
            var linear_bio_descr:ScrollView=dialog.findViewById(R.id.linearBiodescrptn)
            var about=dialog.findViewById<TextView>(R.id.abouttxt)
            var icon_image=dialog.findViewById<ImageView>(R.id.iconImageView)

            var staffImage=detailList[position].staff_photo



            if (staffImage.isNotEmpty()) {
                context.let {
                    Glide.with(it)
                        .load(staffImage)
                        .into(icon_image)
                }
            }
            else
            {
                icon_image.setBackgroundResource(R.drawable.teacher_icon)
            }

            if (detailList[position].biography.length==0){
               /* linear_bio.visibility=View.GONE
                linear_bio_descr.visibility=View.GONE*/
                linear_bio.visibility=View.GONE
                linear_bio_descr.visibility=View.GONE
                linear_nodatae.visibility=View.GONE
                linear_nodatae.text = "No data Found"
            }else{
                linear_bio.visibility=View.VISIBLE
                linear_bio_descr.visibility=View.VISIBLE
                linear_nodatae.visibility=View.GONE
                about.text = detailList[position].biography

            }

             //dialog.dismiss()
            dialog.show()

        }

    }
    override fun getItemCount(): Int {

        return detailList.size

    }
    private fun teachercontactApi(title: String,
                                  message: String,
                                  studentID: String,
                                  staffEmail: String,
                                  dialog: Dialog
                                  ){
        val token = sharedprefs.getaccesstoken(context)
        val sendMailBody = SendStaffEmailApiModel( staffEmail, title, message)
        val call: Call<SendStaffEmailModel> =
            ApiClient.getClient.send_email_to_staff(sendMailBody, "Bearer " + token)
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
                                context,
                                "Email sent Successfully ",
                                "Success",
                                dialog
                            )
                            }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                teachercontactApi(
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
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, context)
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