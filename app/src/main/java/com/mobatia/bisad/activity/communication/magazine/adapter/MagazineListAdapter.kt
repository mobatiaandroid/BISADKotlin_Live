package com.mobatia.bisad.activity.communication.magazine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.communication.letter.model.LetterResponseListModel
import com.mobatia.bisad.activity.communication.magazine.model.MagazineResponseListModel
import com.mobatia.bisad.activity.communication.newsletter.adapter.NewsLetterRecyclerAdapter
import com.mobatia.bisad.activity.communication.newsletter.model.NewLetterListDetailModel

internal class MagazineListAdapter (private var newsLetterList: List<MagazineResponseListModel>) :
    RecyclerView.Adapter<MagazineListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_newletter_recycler, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = newsLetterList[position]

        holder.title.text = summary.title

    }
    override fun getItemCount(): Int {

        return newsLetterList.size

    }
}