package com.mobatia.bisad.activity.school_trips.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.school_trips.TripDetailActivity
import com.mobatia.bisad.activity.school_trips.model.TripHistoryResponseModel
import com.mobatia.bisad.constants.CommonFunctions


class TripHistoryListAdapter(
    var context: Context,
    tripListArray: ArrayList<TripHistoryResponseModel.Trip>
) :
    RecyclerView.Adapter<TripHistoryListAdapter.MyViewHolder>() {
    var tripList: ArrayList<TripHistoryResponseModel.Trip>

    init {
        tripList = tripListArray
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tripImageView: ImageView
        var tripNameTextView: TextView
        var tripPriceTextView: TextView
        var tripDateTextView: TextView
        var tripBookButton: Button
        var tripPreference: TextView

        init {
            tripImageView = view.findViewById<View>(R.id.tripImageView) as ImageView
            tripNameTextView = view.findViewById<View>(R.id.tripNameTextView) as TextView
            tripPriceTextView = view.findViewById<View>(R.id.priceTextView) as TextView
            tripDateTextView = view.findViewById<View>(R.id.estimateDateTextView) as TextView
            tripBookButton = view.findViewById<Button>(R.id.bookNowButton)
            tripPreference = view.findViewById<TextView>(R.id.choicePreference)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_trips_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tripNameTextView.setText(tripList[position].tripName)
        if (tripList[position].tripImageUrls!!.size > 0) {
            if (!tripList[position].tripImageUrls!!.get(0).equals("")) {
                Glide.with(context)
                    .load(CommonFunctions.replace(tripList[position].tripImageUrls!!.get(0)))
                    .fitCenter().placeholder(R.drawable.default_banner)
                    .into(holder.tripImageView)
            }
        }
        if (tripList[position].preference.equals("")) {
            holder.tripPreference.visibility = View.INVISIBLE
        } else {
            holder.tripPreference.visibility = View.VISIBLE
            holder.tripPreference.setText(tripList[position].preference)
        }
        if (tripList[position].trip_type.equals("international"))
        {
            if (tripList[position].tripStatus === 0) {
                holder.tripBookButton.text =  context.getString(R.string.book_now)
            } else if (tripList[position].tripStatus === 1) {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_1)
            } else if (tripList[position].tripStatus === 2) {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_2)
            } else if (tripList[position].tripStatus === 3) {
                holder.tripBookButton.text =  context.getString(R.string.trip_approved)
            } else if (tripList[position].tripStatus === 4) {
                holder.tripBookButton.text = context.getString(R.string.trip_cancelled)
            } else if (tripList[position].tripStatus === 5) {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_5)
            } else if (tripList[position].tripStatus === 6) {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_6)
            } else if (tripList[position].tripStatus === 7) {
                holder.tripBookButton.text =  context.getString(R.string.paid)
            } else {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_8)
            }
        }
        else if (tripList[position].trip_type.equals("domestic"))
        {
            if (tripList[position].tripStatus === 0) {
                holder.tripBookButton.text =  context.getString(R.string.book_now)
            } else if (tripList[position].tripStatus === 1) {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_1)
            } else if (tripList[position].tripStatus === 2) {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_2)
            } else if (tripList[position].tripStatus === 3) {
                holder.tripBookButton.text =  context.getString(R.string.book_now)
            } else if (tripList[position].tripStatus === 4) {
                holder.tripBookButton.text =  context.getString(R.string.trip_cancelled)
            } else if (tripList[position].tripStatus === 5) {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_5)
            } else if (tripList[position].tripStatus === 6) {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_6)
            } else if (tripList[position].tripStatus === 7) {
                holder.tripBookButton.text =  context.getString(R.string.paid)
            } else {
                holder.tripBookButton.text =  context.getString(R.string.trip_status_8)
            }
        }
        else
        {

        }
        holder.tripBookButton.setOnClickListener {
            val intent = Intent(
                context,
                TripDetailActivity::class.java
            )
            intent.putExtra("tripID", tripList[position].id)
            intent.putExtra("tripName", tripList[position].tripName)
            context.startActivity(intent)
            //                showIntentionPopUp();
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(
                context,
                TripDetailActivity::class.java
            )
            intent.putExtra("tripID", tripList[position].id)
            intent.putExtra("tripName", tripList[position].tripName)
            context.startActivity(intent)
            //                showIntentionPopUp();
        }
        holder.tripPriceTextView.text =
            "Trip Amount : " + tripList[position].totalPrice + " AED"
        holder.tripDateTextView.text =
            "Trip Date : " + CommonFunctions.dateParsingyyyyMMddToDdMmmYyyy(tripList[position].tripDate)
    }

    override fun getItemCount(): Int {
        return tripList.size
    }
}
