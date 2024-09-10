package com.mobatia.bisad.activity.canteen.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartListmodel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartResModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CartItemsListModel
import com.mobatia.bisad.constants.InternetCheckClass

class DatesBasketAdapter (var cartdate_list:ArrayList<CanteenCartResModel>, var nContext: Context
                          , var ordered_user_type:String, var student_id:String, var parent_id:String,
                          var staff_id:String,var itemtxt:TextView,var amnttxt:TextView,
                          var itemLinear:LinearLayout,var noItemTxt:ImageView,
                          var dateRec:RecyclerView,var progress:ProgressBarDialog,  var  TotalOrderedAmount: Int,
                          var cart_totoal: Int) :
    RecyclerView.Adapter<DatesBasketAdapter.ViewHolder>() {
    lateinit var itemslist:ArrayList<CartItemsListModel>
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.itemsbasket_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        itemslist=ArrayList()
        itemslist=cartdate_list[position].items
        var deliverydate:String=""

        holder.closeImg.visibility = View.GONE
        holder.cartItemRecycler.setHasFixedSize(true)
        val llm = LinearLayoutManager(nContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        val spacing = 5 // 50px


        holder.totalAmountTxt.text = "Total      " + cartdate_list[position].total_amount.toString() + "AED"
        holder.itemDateTxt.text = InternetCheckClass.dateParsingToddMMMyyyyBasket(cartdate_list[position].delivery_date)
        holder.closeImg.setOnClickListener(View.OnClickListener { })
        holder.cartItemRecycler.layoutManager=LinearLayoutManager(nContext)
        var adptr=BasketItemsAdapter(itemslist,
            nContext, ordered_user_type,
            student_id,
            parent_id,
            staff_id,
            cartdate_list[position].delivery_date,itemtxt,amnttxt,itemLinear,noItemTxt,dateRec,progress,TotalOrderedAmount,cart_totoal)
        holder.cartItemRecycler.adapter=adptr



    }
    override fun getItemCount(): Int {
        return cartdate_list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemDateTxt: TextView
        var totalAmountTxt:TextView
        var closeImg: ImageView
        var cartItemRecycler: RecyclerView


        init {
            cartItemRecycler = itemView.findViewById<RecyclerView>(R.id.cartItemRecycler)
            itemDateTxt = itemView.findViewById<TextView>(R.id.itemDateTxt)
            totalAmountTxt = itemView.findViewById<TextView>(R.id.totalAmountTxt)
            closeImg = itemView.findViewById<ImageView>(R.id.imgClose)
        }
    }

}