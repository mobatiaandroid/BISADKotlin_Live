package com.mobatia.bisad.activity.payment.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.adapter.Canteeninfo_adapter
import com.mobatia.bisad.activity.payment.model.InfoPayListModel
import com.mobatia.bisad.pdfviewer.PdfViewer

class PaymentInfo_adapter  (val informationlist: ArrayList<InfoPayListModel>, var mContext: Context) :
    RecyclerView.Adapter<PaymentInfo_adapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_absence_leave_recycelr, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var list_title=informationlist[position].title
        viewHolder.title.text = list_title

        viewHolder.title.setOnClickListener {
            if (informationlist[position].title.isEmpty()){
                Toast.makeText(mContext, "No Data Found", Toast.LENGTH_SHORT).show()

            }else{
                var pdf_url=informationlist[position].file
                mContext.startActivity(
                    Intent(mContext, PdfViewer::class.java).putExtra
                    ("Url",pdf_url).putExtra("title",informationlist[position].title))

            }
        }



    }

    override fun getItemCount(): Int {
        return informationlist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView


        init {
            title = itemView.findViewById(R.id.listDate)


        }
    }

}