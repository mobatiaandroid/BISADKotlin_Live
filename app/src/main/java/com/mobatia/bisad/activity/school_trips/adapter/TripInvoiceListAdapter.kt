package com.mobatia.bisad.activity.school_trips.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.school_trips.model.TripDetailsResponseModel
import com.mobatia.bisad.constants.CommonFunctions


class TripInvoiceListAdapter(
    private val mContext: Context,
    mnNewsLetterModelArrayList: ArrayList<TripDetailsResponseModel.TripData.Invoice>
) :
    RecyclerView.Adapter<TripInvoiceListAdapter.MyViewHolder>() {
    private val mnNewsLetterModelArrayList: ArrayList<TripDetailsResponseModel.TripData.Invoice>
    var dept: String? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //  ImageView imageIcon;
        var termname: TextView
        var status: TextView
        var statusLayout: RelativeLayout
        var tripsDateTxt: TextView

        init {

            //  imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
             termname = view.findViewById(R.id.listTxtTitle)
             status= view.findViewById(R.id.status)
             statusLayout = view.findViewById(R.id.statusLayout)
             tripsDateTxt = view.findViewById(R.id.tripsDateTxt)
        }
    }

    init {
        this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_payment_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tripsDateTxt.visibility=View.GONE
        holder.termname.text = mnNewsLetterModelArrayList[position].invoiceNumber

        holder.statusLayout.visibility = View.VISIBLE
        holder.status.text =  mContext.getString(R.string.paid)
        holder.statusLayout.setBackgroundResource(R.drawable.rect_trip_paid_blue)
        /*holder.pdfTitle.text = "Paid by " + mnNewsLetterModelArrayList[position].firstName
        // holder.imageIcon.setVisibility(View.GONE);
        // Log.e("date",mnNewsLetterModelArrayList.get(position).getPaymentDate());
        holder.tripsDateTxt.setText(
            CommonFunctions.dateConversionddmmyyyytoddMMYYYY(
                mnNewsLetterModelArrayList[position].paymentDate
            )
        )
        holder.tripsAmountTxt.setText(mnNewsLetterModelArrayList[position].paidAmount + " " + "AED")*/
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
