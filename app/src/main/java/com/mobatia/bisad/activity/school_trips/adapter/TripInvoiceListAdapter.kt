package com.mobatia.bisad.activity.school_trips.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TripInvoiceListAdapter(
    private val mContext: Context,
    mnNewsLetterModelArrayList: ArrayList<TripDetailsResponseModel.TripData.Invoice>
) :
    RecyclerView.Adapter<TripInvoiceListAdapter.MyViewHolder>() {
    private val mnNewsLetterModelArrayList: ArrayList<TripDetailsResponseModel.TripData.Invoice>
    var dept: String? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //  ImageView imageIcon;
        var pdfTitle: TextView
        var tripsDateTxt: TextView
        var tripsAmountTxt: TextView
        var mainRelative: RelativeLayout
        var status: TextView
        var statusLayout: RelativeLayout

        init {

            //  imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            pdfTitle = view.findViewById<View>(R.id.pdfTitle) as TextView
            tripsDateTxt = view.findViewById<View>(R.id.tripsDateTxt) as TextView
            tripsAmountTxt = view.findViewById<View>(R.id.tripsAmountTxt) as TextView
            mainRelative = view.findViewById<View>(R.id.mainRelative) as RelativeLayout
            status = view.findViewById<TextView>(R.id.status)
            statusLayout = view.findViewById<RelativeLayout>(R.id.statusLayout)
        }
    }

    init {
        this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_wallet_history_recycler_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.pdfTitle.text = "Paid by " + mnNewsLetterModelArrayList[position].getFirstName()
        // holder.imageIcon.setVisibility(View.GONE);
        // Log.e("date",mnNewsLetterModelArrayList.get(position).getPaymentDate());
        holder.tripsDateTxt.setText(
            AppUtils.dateConversionddmmyyyytoddMMYYYY(
                mnNewsLetterModelArrayList[position].getPaymentDate()
            )
        )
        holder.tripsAmountTxt.setText(mnNewsLetterModelArrayList[position].getPaidAmount() + " " + "AED")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mnNewsLetterModelArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
