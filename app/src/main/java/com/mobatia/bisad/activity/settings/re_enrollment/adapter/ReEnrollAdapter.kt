package com.mobatia.bisad.activity.settings.re_enrollment.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.kyanogen.signatureview.SignatureView
import com.mobatia.bisad.R
import com.mobatia.bisad.WebviewActivity
import com.mobatia.bisad.activity.parent_meetings.ReviewAppointmentsRecyclerViewActivity
import com.mobatia.bisad.activity.parent_meetings.adapter.ReviewAdapter
import com.mobatia.bisad.activity.parent_meetings.model.review_list.ReviewListModel
import com.mobatia.bisad.activity.settings.re_enrollment.model.StudentsEnrollList
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.home.model.reenrollment.*
import com.mobatia.bisad.fragment.home.sharedprefs
import com.mobatia.bisad.fragment.home.studentName
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

internal class ReEnrollAdapter (var context: Context,var stud_enroll_list:ArrayList<StudentsEnrollList>
) :
    RecyclerView.Adapter<ReEnrollAdapter.MyViewHolder>() {
    lateinit var reEnrollOptionList:ArrayList<String>

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var student_name:TextView=view.findViewById(R.id.name)
        var student_class:TextView=view.findViewById(R.id.section)
        var status:TextView=view.findViewById(R.id.status)
        var studImg:ImageView=view.findViewById(R.id.studImg)
        var linear_list:LinearLayout=view.findViewById(R.id.linear)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_re_enroll, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
Log.e("ad",stud_enroll_list[position].name)
        holder.student_name.text = stud_enroll_list[position].name
        holder.student_class.text = stud_enroll_list[position].section
        var stud_image=stud_enroll_list[position].photo
        if (stud_enroll_list[position].status.equals("")){

            holder.status.text = "Not Submitted"
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.red))
        }else {

            holder.status.text = "Submitted"
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green))
        }
        if (stud_image.isNotEmpty()) {
            context.let {
                Glide.with(it)
                    .load(stud_image)
                    .into(holder.studImg)
            }
        }
        else
        {
            holder.studImg.setBackgroundResource(R.drawable.student)
        }

    }

    override fun getItemCount(): Int {

        return stud_enroll_list.size

    }
}