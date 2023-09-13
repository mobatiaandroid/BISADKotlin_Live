package com.mobatia.bisad.activity.payment.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.adapter.MyorderItemsAdapter
import com.mobatia.bisad.activity.payment.PaymentDetailsActivity
import com.mobatia.bisad.activity.payment.PaymentListActivityNew
import com.mobatia.bisad.activity.payment.model.PayCatDataList
import com.mobatia.bisad.activity.payment.model.PaymentCategoryListModel

class PayCategoryListAdapter (var mcontext: Context,val cat_list: ArrayList<PayCatDataList> ) :
    RecyclerView.Adapter<PayCategoryListAdapter.ViewHolder>() {
    lateinit var mAdapter: MyorderItemsAdapter
    lateinit var itemlist:ArrayList<PaymentCategoryListModel>
    var ordered_user_type = ""
    var student_id = ""
    var parent_id = ""
    var staff_id = ""
    var totalAmount = ""
    var WalletAmount = 0
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_paycattitle_list, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemlist= ArrayList()
        holder.title.setText(cat_list[position].title.trim())
        var itemlist=cat_list[position].items
        holder.item_rec.layoutManager=LinearLayoutManager(mcontext)
        var itemAdapter=PaymentItemListAdapter(mcontext,itemlist,cat_list[position].title)
        holder.item_rec.adapter=itemAdapter
        

       /* for (i in itemlist.indices){
            if (itemlist[i].status.equals("0")) {
                holder.statusLayout.visibility = View.VISIBLE
                holder.status.setBackgroundResource(R.drawable.rectangle_red)
                holder.status.text = "New"
            } else if (itemlist[i].status.equals("1") || itemlist[position].status.equals("")
            ) {
                holder.statusLayout.visibility = View.GONE
            } else if (itemlist[i].status.equals("2")) {
                holder.statusLayout.visibility = View.VISIBLE
                holder.status.setBackgroundResource(R.drawable.rectangle_orange)
                holder.status.text = "Updated"
            }
        }
        holder.list.setOnClickListener(){
            if (itemlist[position].status.equals("0")){
                val intent = Intent(mcontext, PaymentDetailsActivity::class.java)
                mcontext.startActivity(intent)
            }else {
                val intent = Intent(mcontext, PaymentListActivityNew::class.java)
                intent.putExtra("itemlist",itemlist)
                mcontext.startActivity(intent)
            }
        }*/


    }

    override fun getItemCount(): Int {
        return cat_list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

       /*lateinit var listTxtTitle: TextView
        lateinit var status: TextView
        lateinit var statusLayout: RelativeLayout
        lateinit var list: RelativeLayout*/
var title:TextView
var item_rec:RecyclerView


        init {
            title=itemView.findViewById(R.id.title)
            item_rec=itemView.findViewById(R.id.reportRecycler)
           /* listTxtTitle = itemView.findViewById(R.id.listTxtTitle)
            status = itemView.findViewById(R.id.status)
            statusLayout = itemView.findViewById(R.id.statusLayout)
            list=itemView.findViewById(R.id.relSub)*/
        }
    }
}