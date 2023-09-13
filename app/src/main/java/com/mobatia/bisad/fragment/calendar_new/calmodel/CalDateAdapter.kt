package com.mobatia.bisad.fragment.calendar_new.calmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.calendar_new.model.CalendarDateModel
import com.mobatia.bisad.fragment.calendar_new.adapter.detailArray
import com.mobatia.bisad.fragment.calendar_new.adapter.linearLayoutManager
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalDateAdapter (
    private var mContext: Context,
    private var calendarArrayList: ArrayList<CalendarDateModel>


) :
    RecyclerView.Adapter<CalDateAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dateTxt: TextView = view.findViewById(R.id.dateTxt)
        var detailRecycler: RecyclerView = view.findViewById(R.id.detailRecycler)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(mContext)
            .inflate(R.layout.adapter_date_recycler, parent, false)
        return MyViewHolder(itemView)
    }


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.e("date","adptr")
        val summary = calendarArrayList[position]



        if (summary.startDate.length != 0) {


            if (summary.startDate.length == 20) {

                val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                val outputFormat: DateFormat = SimpleDateFormat("EEEE dd MMMM")
                val startdate: Date = inputFormat.parse(summary.startDate)
                var outputDateStrstart: String = outputFormat.format(startdate)
                holder.dateTxt.text = outputDateStrstart


            }
            else if (summary.startDate.length == 11)
            {
                val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                val outputFormat: DateFormat = SimpleDateFormat("EEEE dd MMMM")
                val startdate: Date? = inputFormat.parse(summary.startDate)
                //   val enddate: Date = inputFormat.parse(summary.endDate)
                var outputDateStrstart: String = outputFormat.format(startdate)

                holder.dateTxt.text = outputDateStrstart

            }
            else if (summary.startDate.length==10)
            {
                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val inputFormatend: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                val outputFormat: DateFormat = SimpleDateFormat("EEEE dd MMMM")
                val startdate: Date? = inputFormat.parse(summary.startDate)
                //val enddate: Date = inputFormatend.parse(summary.endDate)
                var outputDateStrstart: String = outputFormat.format(startdate)
                //  var outputDateStrend: String = outputFormat.format(enddate)
                var outputDateStrstartupdated: String = inputFormat.format(startdate)
                //   var outputDateStrendupdated: String = inputFormat.format(enddate)
                holder.dateTxt.text = outputDateStrstart
            }
            else {

                holder.dateTxt.text = summary.startDate


            }


        }

        linearLayoutManager = LinearLayoutManager(mContext)
        holder.detailRecycler.layoutManager = linearLayoutManager
        holder.detailRecycler.itemAnimator = DefaultItemAnimator()
        if (calendarArrayList.size > 0) {
            detailArray = summary.detailList
            val calendarListAdapter = CalDetailAdapter(mContext, detailArray)
            holder.detailRecycler.adapter = calendarListAdapter
        }

    }


    override fun getItemCount(): Int {

        return calendarArrayList.size

    }

    @SuppressLint("SimpleDateFormat")
    private fun getDates(
        dateString1: String,
        dateString2: String
    ): ArrayList<String>? {
        val dates = java.util.ArrayList<String>()
        val df1: DateFormat = SimpleDateFormat("MMM dd,yyyy")
        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        while (!cal1.after(cal2)) {
            dates.add(cal1.time.toString())
            cal1.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dates
    }
}