package com.mobatia.bisad.activity.school_trips.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TripInstallmentsAdapter(
    var context: Context,
    tripListArray: ArrayList<TripDetailsResponseModel.InstallmentDetail>
) :
    RecyclerView.Adapter<TripInstallmentsAdapter.MyViewHolder>() {
    var tripList: ArrayList<TripDetailsResponseModel.InstallmentDetail>

    init {
        tripList = tripListArray
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var pdfTitle: TextView
        var tripsDateTxt: TextView
        var tripsAmountTxt: TextView
        var mainRelative: RelativeLayout
        var status: TextView
        var statusLayout: RelativeLayout

        init {
            pdfTitle = view.findViewById<View>(R.id.pdfTitle) as TextView
            tripsDateTxt = view.findViewById<View>(R.id.tripsDateTxt) as TextView
            tripsAmountTxt = view.findViewById<View>(R.id.tripsAmountTxt) as TextView
            mainRelative = view.findViewById<View>(R.id.mainRelative) as RelativeLayout
            status = view.findViewById<TextView>(R.id.status)
            statusLayout = view.findViewById<RelativeLayout>(R.id.statusLayout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_trip_installments, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.pdfTitle.text = String.format("Installment No:%d", position + 1)
        holder.tripsDateTxt.setText(AppUtils.dateConversionddmmyyyytoddMMYYYY(tripList[position].getDueDate()))
        holder.tripsAmountTxt.setText(tripList[position].getAmount() + " " + "AED")
        if (tripList[position].getDueDate().equalsIgnoreCase("0")) {
            holder.mainRelative.setBackgroundColor(context.resources.getColor(R.color.list_bg))
        } else {
            if (tripList[position].getPaidStatus() === 0) {
                holder.mainRelative.setBackgroundColor(context.resources.getColor(R.color.rel_nine))
            } else {
                holder.mainRelative.setBackgroundColor(context.resources.getColor(R.color.list_bg))
            }
        }
        if (tripList[position].getPaidStatus() === 0) {
            // holder.imageIcon.setVisibility(View.VISIBLE);
            //holder.imageIcon.setBackgroundResource(R.drawable.shape_circle_red);
            holder.statusLayout.visibility = View.VISIBLE
            holder.status.setBackgroundResource(R.drawable.rectangle_red)
            holder.status.text = "New"
        } else if (tripList[position].getPaidStatus() === 1 || java.lang.String.valueOf(tripList[position].getPaidStatus())
                .equals("", ignoreCase = true)
        ) {
            //holder.imageIcon.setVisibility(View.GONE);
            holder.statusLayout.visibility = View.GONE
        } else if (tripList[position].getPaidStatus() === 2) {
            // holder.imageIcon.setVisibility(View.VISIBLE);
            // holder.imageIcon.setBackgroundResource(R.drawable.shape_circle_navy);
            holder.statusLayout.visibility = View.VISIBLE
            holder.status.setBackgroundResource(R.drawable.rectangle_orange)
            holder.status.text = "Updated"
        }
    }

    override fun getItemCount(): Int {
        return tripList.size
    }
}
