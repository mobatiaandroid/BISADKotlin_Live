package com.mobatia.bisad.activity.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.payment.PaymentDetailsActivity
import com.mobatia.bisad.activity.payment.model.PaymentCategoryListModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PaymentItemListAdapter (var mcontext: Context, val catItemList: ArrayList<PaymentCategoryListModel>,
var title:String) :
    RecyclerView.Adapter<PaymentItemListAdapter.ViewHolder>() {
var dueDate:String=""

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_paymentcatlist_recycler, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(catItemList.size>1)
        {
            if (position==catItemList.size-1)
            {
                holder.viewLine.visibility=View.GONE
            }
            else{
                holder.viewLine.visibility=View.VISIBLE
            }

        }
        else{
            holder.viewLine.visibility=View.GONE
        }
        holder.listTitle.setText(catItemList[position].invoice_description)
        if (catItemList[position].due_date.isNotEmpty()){

           /* val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val output = formatter.format(parser.parse("2018-12-14T09:55:00"))*/

            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
            val inputDateStr = catItemList[position].due_date
            val date: Date = inputFormat.parse(inputDateStr)
            dueDate = outputFormat.format(date)
            holder.listDate.setText("Due Date: " + dueDate)
        }else{
            holder.listDate.setText("")
        }


             if (catItemList[position].status.equals("0")) {
                 holder.statusLayout.visibility = View.VISIBLE
                 holder.status.setBackgroundResource(R.color.rel_two)
                 holder.status.text = "Pay"

             } else   {
                 holder.statusLayout.visibility = View.GONE
                 holder.statusLayout.visibility = View.VISIBLE
                 holder.status.setBackgroundResource(R.drawable.paid)
                 //holder.status.text = "Paid"
             }

         holder.list.setOnClickListener(){
             var paidBy=""
             var paidDate=""
             if(catItemList[position].paid_by!=null)
             {
                 paidBy=catItemList[position].paid_by
             }
             else{
                 paidBy=""
             }
             if(catItemList[position].paid_date!=null)
             {
                 paidDate=catItemList[position].paid_date
             }
             else{
                 paidDate=""
             }
                 val intent = Intent(mcontext, PaymentDetailsActivity::class.java)
                 intent.putExtra("title",title)
                 intent.putExtra("status",catItemList[position].status)
                 intent.putExtra("id",catItemList[position].id)
                 intent.putExtra("student_name",catItemList[position].student_name)
                 intent.putExtra("account_code",catItemList[position].account_code)
                 intent.putExtra("pupil_code",catItemList[position].pupil_code)
                 intent.putExtra("academic_year",catItemList[position].academic_year)
                 intent.putExtra("invoice_ref",catItemList[position].invoice_ref)
                 intent.putExtra("invoice_description",catItemList[position].invoice_description)
                 intent.putExtra("current_amount",catItemList[position].current_amount)
                 intent.putExtra("vat_percentage",catItemList[position].vat_percentage)
                 intent.putExtra("vat_amount",catItemList[position].vat_amount)
                 intent.putExtra("total_amount",catItemList[position].total_amount)
                 intent.putExtra("due_date",catItemList[position].due_date)
                 intent.putExtra("paid_date",paidDate)
                 intent.putExtra("payment_type",catItemList[position].payment_type)
                 intent.putExtra("paid_by",paidBy)
                 intent.putExtra("thankyou_note",catItemList[position].thankyou_note)
                 mcontext.startActivity(intent)

         }


    }

    override fun getItemCount(): Int {
        return catItemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var listTitle: TextView
        var listDate:TextView
         lateinit var status: TextView
         lateinit var viewLine: ImageView
         lateinit var statusLayout: RelativeLayout
         lateinit var list: RelativeLayout



        init {

            listTitle = itemView.findViewById(R.id.listTxtTitle)
            listDate = itemView.findViewById(R.id.date)
             status = itemView.findViewById(R.id.status)
             statusLayout = itemView.findViewById(R.id.statusLayout)
             list=itemView.findViewById(R.id.relSub)
            viewLine=itemView.findViewById(R.id.viewLine)
        }
    }
}