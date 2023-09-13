package com.mobatia.bisad.activity.canteen.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.model.order_history.OrderCanteenPreOrderItems

class OrderHistoryPreorderDetailsAdapter (var type_status:Int,val preorderhis_list: ArrayList<OrderCanteenPreOrderItems>, var mcontext: Context) :
    RecyclerView.Adapter<OrderHistoryPreorderDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_order_history_item_details, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.itemNameTxt.text=preorderhis_list.get(position).item_name
        viewHolder.itemDescription.text=preorderhis_list.get(position).item_description
        viewHolder.amountTxt.text=preorderhis_list.get(position).item_total.toString() +".00 AED"
        if (type_status==2){
            viewHolder.orderbadge.text="POS"

        }else{
            viewHolder.orderbadge.text="App"
        }
        if (preorderhis_list.get(position).quantity==1)
        {
            viewHolder.itemsCount.text=preorderhis_list.get(position).quantity.toString()+" item"
        }
        else
        {
            viewHolder.itemsCount.text=preorderhis_list.get(position).quantity.toString()+" items"
        }
        if (preorderhis_list.get(position).item_status==2)
        {
            viewHolder.status.setText("Delivered")
            viewHolder.status.setTextColor(mcontext.getResources().getColor(R.color.canteen_red))
        }
        else{
            viewHolder.status.setText("Cancelled")
            viewHolder.status.setTextColor(mcontext.getResources().getColor(R.color.red))
        }


    }

    override fun getItemCount(): Int {

        return preorderhis_list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemNameTxt: TextView
        var itemDescription: TextView
        var amountTxt: TextView
        var itemsCount: TextView
        var status: TextView
        var orderbadge: TextView

        init {
            itemNameTxt = itemView.findViewById(R.id.itemNameTxt)
            itemDescription = itemView.findViewById(R.id.itemDescription)
            amountTxt = itemView.findViewById(R.id.amountTxt)
            itemsCount = itemView.findViewById(R.id.itemsCount)
            status = itemView.findViewById(R.id.status)
            orderbadge=itemView.findViewById(R.id.orderbadge)

        }
    }

}