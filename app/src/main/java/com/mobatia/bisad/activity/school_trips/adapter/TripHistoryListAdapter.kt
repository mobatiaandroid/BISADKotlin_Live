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
        holder.tripNameTextView.setText(tripList[position].getTripName())
        if (tripList[position].getTripImageUrls().size() > 0) {
            if (!tripList[position].getTripImageUrls().get(0).equalsIgnoreCase("")) {
                Glide.with(context)
                    .load(AppUtils.replace(tripList[position].getTripImageUrls().get(0)))
                    .fitCenter().placeholder(R.drawable.default_banner)
                    .into(holder.tripImageView)
            }
        }
        if (tripList[position].getPreference().equalsIgnoreCase("")) {
            holder.tripPreference.visibility = View.INVISIBLE
        } else {
            holder.tripPreference.visibility = View.VISIBLE
            holder.tripPreference.setText(tripList[position].getPreference())
        }
        if (tripList[position].getTripStatus() === 0) {
            holder.tripBookButton.text = "Book your Trip"
        } else if (tripList[position].getTripStatus() === 1) {
            holder.tripBookButton.text = "Pending"
        } else if (tripList[position].getTripStatus() === 2) {
            holder.tripBookButton.text = "Rejected"
        } else if (tripList[position].getTripStatus() === 3) {
            holder.tripBookButton.text = "Continue"
        } else if (tripList[position].getTripStatus() === 4) {
            holder.tripBookButton.text = "Cancelled"
        } else if (tripList[position].getTripStatus() === 5) {
            holder.tripBookButton.text = "Pay now"
        } else if (tripList[position].getTripStatus() === 6) {
            holder.tripBookButton.text = "Pay now"
        } else if (tripList[position].getTripStatus() === 7) {
            holder.tripBookButton.text = "View Invoice"
        } else if (tripList[position].getTripStatus() === 9) {
            holder.tripBookButton.text = "Expired"
        } else {
            holder.tripBookButton.text = "Not Available"
        }
        holder.tripBookButton.setOnClickListener {
            val intent = Intent(
                context,
                TripDetailActivity::class.java
            )
            intent.putExtra("tripID", tripList[position].getId())
            intent.putExtra("tripName", tripList[position].getTripName())
            context.startActivity(intent)
            //                showIntentionPopUp();
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(
                context,
                TripDetailActivity::class.java
            )
            intent.putExtra("tripID", tripList[position].getId())
            intent.putExtra("tripName", tripList[position].getTripName())
            context.startActivity(intent)
            //                showIntentionPopUp();
        }
        holder.tripPriceTextView.text =
            "Trip Amount : " + tripList[position].getTotalPrice() + " AED"
        holder.tripDateTextView.text =
            "Trip Date : " + AppUtils.dateParsingyyyyMMddToDdMmmYyyy(tripList[position].getTripDate())
    }

    override fun getItemCount(): Int {
        return tripList.size
    }
}
