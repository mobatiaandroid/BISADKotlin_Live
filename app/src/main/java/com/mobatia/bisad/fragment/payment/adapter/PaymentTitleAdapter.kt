package com.mobatia.bisad.fragment.payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R

internal class PaymentTitleAdapter  (private var mContext: Context ) :
    RecyclerView.Adapter<PaymentTitleAdapter.MyViewHolder>() {

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var detaillist_rec: RecyclerView = view.findViewById(R.id.detail_rec)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_payment_title, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.detaillist_rec.layoutManager= LinearLayoutManager(mContext)
        var detail_adapter= PaymentDetailAdapter(mContext)
        holder.detaillist_rec.adapter=detail_adapter
    }
    override fun getItemCount(): Int {

        return 0

    }
}