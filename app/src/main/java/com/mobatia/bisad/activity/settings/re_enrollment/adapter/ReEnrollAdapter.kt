package com.mobatia.bisad.activity.settings.re_enrollment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.settings.re_enrollment.model.StudentsEnrollList

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