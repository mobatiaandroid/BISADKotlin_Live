package com.mobatia.bisad.activity.school_trips.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.school_trips.TripListingActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.fragment.school_trips.model.TripCategoriesResponseModel


class TripsCategoryAdapter(
    var context: Context,
    categoriesList: ArrayList<TripCategoriesResponseModel.Data>
) :
    RecyclerView.Adapter<TripsCategoryAdapter.MyViewHolder>() {
    var categoryList: ArrayList<TripCategoriesResponseModel.Data>

    init {
        categoryList = categoriesList
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryItemImageView: ImageView
        var categoryNameTextView: TextView

        init {
            categoryItemImageView = view.findViewById<View>(R.id.imageView) as ImageView
            categoryNameTextView = view.findViewById<View>(R.id.textView) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_trips_category_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.categoryNameTextView.setText(categoryList[position].tripCategory)
        if (!categoryList[position].image.equals("")) {
            Glide.with(context).load(CommonFunctions.replace(categoryList[position].image!!))
                .fitCenter().placeholder(R.drawable.default_banner)
                .into(holder.categoryItemImageView)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(
                context,
                TripListingActivity::class.java
            )
            intent.putExtra("trip_category_id", categoryList[position].id)
            intent.putExtra("trip_category_name", categoryList[position].tripCategory)
            context.startActivity(intent)
            //                showIntentionPopUp();
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}
