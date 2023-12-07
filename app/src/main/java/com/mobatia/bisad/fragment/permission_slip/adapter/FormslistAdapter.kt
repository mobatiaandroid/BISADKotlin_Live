package com.mobatia.bisad.fragment.permission_slip.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.permission_slip.FormDetailActivity
import com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipListModel

internal class FormslistAdapter (private var mContext: Context, var formslist:ArrayList<PermissionSlipListModel> ) :
    RecyclerView.Adapter<FormslistAdapter.MyViewHolder>() {
    var sts:String=""
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: TextView = view.findViewById(R.id.itemName)
        var linear: LinearLayout =view.findViewById(R.id.linear)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_communication_recycler, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemName.text = formslist[position].title

        holder.linear.setOnClickListener {
            if (formslist[position].status==null){
                sts="0"
            }
            else{
                sts=formslist[position].status
            }
            val intent = Intent(mContext, FormDetailActivity::class.java)
            intent.putExtra("title",formslist[position].title)
            intent.putExtra("description",formslist[position].consent)
            intent.putExtra("status",sts)
            intent.putExtra("slip_id",formslist[position].id)
            mContext.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {

        return formslist.size

    }
}