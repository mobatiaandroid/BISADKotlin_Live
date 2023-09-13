package com.mobatia.bisad.activity.canteen.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.PaymentprintActivity
import com.mobatia.bisad.activity.canteen.model.payment_history.PaymentHistoryListModel
import com.mobatia.bisad.activity.canteen.model.wallethistory.CreditHisListModel
import com.mobatia.bisad.activity.home.HomeActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PaymentHistoryAdapter (val wallethistory_list: ArrayList<CreditHisListModel>,var mContext: Context) :
    RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>() {
var formt_fromtime:String=""
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.paymenthistory_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pdfTitle.setText("Paid by " + wallethistory_list[position].parent_name)
        // holder.imageIcon.setVisibility(View.GONE);
        // holder.imageIcon.setVisibility(View.GONE);
        var from_time= wallethistory_list[position].created_on
        val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = from_time
        val date: Date = inputFormat.parse(inputDateStr)
        formt_fromtime = outputFormat.format(date)
        holder.tripsDateTxt.setText(
            formt_fromtime
            )
        holder.tripsAmountTxt.setText(
            "Credit " + wallethistory_list[position].amount
                .toString() + " " + "AED"
        )
holder.linearClick.setOnClickListener(){
    val intent = Intent(mContext, PaymentprintActivity::class.java)
intent.putExtra("parent_name",wallethistory_list[position].parent_name)
intent.putExtra("email",wallethistory_list[position].email)
intent.putExtra("id",wallethistory_list[position].id)
intent.putExtra("student_id",wallethistory_list[position].student_id)
intent.putExtra("user_id",wallethistory_list[position].user_id)
intent.putExtra("amount",wallethistory_list[position].amount)
intent.putExtra("bill_no",wallethistory_list[position].bill_no)
intent.putExtra("order_reference",wallethistory_list[position].order_reference)
intent.putExtra("created_on",wallethistory_list[position].created_on)
intent.putExtra("invoice_note",wallethistory_list[position].invoice_note)
intent.putExtra("payment_type",wallethistory_list[position].payment_type)

    mContext.startActivity(intent)

}
       /* if (wallethistory_list[position].status.equals("0")) {
            holder.status.text = "(Pending)"
        } else {
            holder.status.text = " "
        }*/

    }

    override fun getItemCount(): Int {
        return wallethistory_list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //  ImageView imageIcon;
        var pdfTitle: TextView
        var tripsDateTxt: TextView
        var tripsAmountTxt: TextView
        var mainRelative: RelativeLayout
        var status: TextView
        var linearClick: RelativeLayout
        init {

            //  imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            pdfTitle = view.findViewById(R.id.pdfTitle)
            tripsDateTxt = view.findViewById(R.id.tripsDateTxt)
            tripsAmountTxt = view.findViewById(R.id.tripsAmountTxt)
            mainRelative = view.findViewById(R.id.mainRelative)
            status = view.findViewById(R.id.status)
            linearClick = view.findViewById(R.id.relSub)
            //statusLayout = view.findViewById(R.id.statusLayout)

        }
    }
}