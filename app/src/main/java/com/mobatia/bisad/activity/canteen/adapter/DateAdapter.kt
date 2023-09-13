package com.mobatia.bisad.activity.canteen.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.model.DateModel
import java.util.ArrayList

class DateAdapter (val date_list: ArrayList<DateModel>, var mcontext: Context) :
    RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.date_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.date.text = date_list[position].date
        viewHolder.day.text = date_list[position].day
        viewHolder.month.text = date_list[position].month
        viewHolder.selected.setBackgroundResource(R.drawable.date_selected)

        if (date_list.get(position).isDateSelected) {
            viewHolder.linear.setBackgroundResource(R.drawable.date_selected)
        } else {
            viewHolder.linear.setBackgroundResource(R.drawable.date)
        }
        for (i in date_list.indices) {
            if (i == 0) {
                date_list.get(0).isDateSelected=true

            }
        }
    }
    override fun getItemCount(): Int {
        return date_list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var day: TextView
        var date: TextView
        var month: TextView
        var linear: LinearLayout
        var selected: ImageView
        init {
            day = itemView.findViewById(R.id.dayTxt)
            date = itemView.findViewById(R.id.dateTxt)
            month = itemView.findViewById(R.id.monthTxt)
            linear=itemView.findViewById(R.id.bgLinearInner)
            selected=itemView.findViewById(R.id.dateSelectedImg)

        }
    }

}