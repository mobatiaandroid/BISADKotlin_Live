package com.mobatia.bisad.activity.school_trips.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.school_trips.model.TripDetailsResponseModel
import com.mobatia.bisad.constants.CommonFunctions


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
        var termname: TextView = view.findViewById(R.id.listTxtTitle)
        var status: TextView = view.findViewById(R.id.status)
        var statusLayout: RelativeLayout = view.findViewById(R.id.statusLayout)



        var tripsDateTxt: TextView = view.findViewById(R.id.tripsDateTxt)
        var arrowImgArab : ImageView = view.findViewById(R.id.arrowImgArab)
        var arrowImg : ImageView = view.findViewById(R.id.arrowImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_payment_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


            if (tripList[position].paidStatus == 1) {
                holder.statusLayout.visibility = View.VISIBLE
                holder.status.text =  context.getString(R.string.paid)
                holder.statusLayout.setBackgroundResource(R.drawable.rect_trip_paid_blue)
            } else {
                holder.statusLayout.visibility = View.VISIBLE
                holder.status.text =  context.getString(R.string.pay)
                holder.statusLayout.setBackgroundResource(R.drawable.rectangle_blue_update)
            }

        holder.termname.text =
            context.getString(R.string.installment_no) + (position + 1) + " - " + tripList[position].amount +  context.getString(R.string.aed)
        holder.tripsDateTxt.text= CommonFunctions.dateConversionddmmyyyytoddMMYYYY(tripList[position].dueDate)


    }

    override fun getItemCount(): Int {
        return tripList.size
    }
}
