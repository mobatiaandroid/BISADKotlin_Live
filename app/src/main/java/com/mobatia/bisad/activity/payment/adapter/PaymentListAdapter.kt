package com.mobatia.bisad.activity.payment.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.adapter.MyorderItemsAdapter
import com.mobatia.bisad.activity.canteen.model.payment_history.PayListModel

class PaymentListAdapter (var mContext: Context, val pay_list: ArrayList<PayListModel> ) :
    RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {
    lateinit var mAdapter: MyorderItemsAdapter

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_paymentcatlist_recycler, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pdfTitle.setText(pay_list[position].title)
        // holder.imageIcon.setVisibility(View.GONE);
        // holder.imageIcon.setVisibility(View.GONE);
        holder.tripsDateTxt.setText(pay_list[position].trip_date)
        holder.tripsAmountTxt.setText(
            pay_list[position].amount.toString() + " " + "AED"
        )
       if (pay_list[position].payment_date_status
                .equals("0")
        ) {
            holder.mainRelative.setBackgroundColor(
                mContext.getResources().getColor(R.color.list_bg)
            )
        } else {
            if (pay_list[position].payment_status
                    .equals("0")
            ) {
                holder.mainRelative.setBackgroundColor(
                    mContext.getResources().getColor(R.color.rel_nine)
                )
            } else {
                holder.mainRelative.setBackgroundColor(
                    mContext.getResources().getColor(R.color.list_bg)
                )
            }
        }
        if (pay_list[position].status.equals("0")) {

            holder.statusLayout.visibility = View.VISIBLE
            holder.status.setBackgroundResource(R.drawable.rectangle_red)
            holder.status.text = "New"
        } else if (pay_list[position].status
                .equals("1") || pay_list[position].status
                .equals("")
        ) {

            holder.statusLayout.visibility = View.GONE
        } else if (pay_list[position].status.equals("2")) {

            holder.statusLayout.visibility = View.VISIBLE
            holder.status.setBackgroundResource(R.drawable.rectangle_orange)
            holder.status.text = "Updated"
        }

    }

    override fun getItemCount(): Int {
        return pay_list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //  ImageView imageIcon;
        var pdfTitle: TextView
        var tripsDateTxt: TextView
        var tripsAmountTxt: TextView
        var mainRelative: RelativeLayout
        var status: TextView
        var statusLayout: RelativeLayout



        init {

            //  imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            pdfTitle = view.findViewById(R.id.pdfTitle)
            tripsDateTxt = view.findViewById(R.id.tripsDateTxt)
            tripsAmountTxt = view.findViewById(R.id.tripsAmountTxt)
            mainRelative = view.findViewById(R.id.mainRelative)
            status = view.findViewById(R.id.status)
            statusLayout = view.findViewById(R.id.statusLayout)
        }
    }
}