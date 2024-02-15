package com.mobatia.bisad.activity.canteen.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.model.AllergyContentModel
import com.mobatia.bisad.activity.canteen.model.myorders.*
import com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryApiModel
import com.mobatia.bisad.activity.canteen.model.preorder.CanteenPreorderModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MyorderItemsAdapter (val itemlist: ArrayList<Preorderitems_list>, var mcontext: Context,
                          var ordered_user_type: String ,var student_id:String ,
                          var parent_id: String ,var staff_id:String ,var totalAmount:String ,
                           var  WalletAmount:Int, var dateRecyclerView: RecyclerView,var bottom:LinearLayout,
                           var item:LinearLayout,var noitem: ImageView,var progress:ProgressBarDialog
) :
    RecyclerView.Adapter<MyorderItemsAdapter.ViewHolder>() {

    var quantity = ""
    var canteen_cart_id = ""
    var homeBannerUrlImageArray: ArrayList<String>? = null
    var currentPage = 0
    var BalanceWalletAmount = 0f
    var BalanceConfirmWalletAmount = 0f
    var CartTotalAmount = 0f
     lateinit var allergycontentlist: ArrayList<AllergyContentModel>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.myorder_items_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        allergycontentlist = ArrayList()
        if (itemlist.get(position).allergy_contents.size > 0) {
            allergycontentlist.addAll(itemlist.get(position).allergy_contents)
        }
        holder.bannerImagePager.visibility = View.GONE
        holder.itemNameTxt.text = itemlist[position].item_name
        holder.itemDescription.text = itemlist[position].item_description
        holder.amountExceedTxt.text = itemlist[position].price.toString() + " AED "
        holder.cartitemcount.number = itemlist[position].quantity
//        holder.cartitemcount.setRange(
//            0,
//            Integer.valueOf(itemlist[position].available_quantity)
//        )
        if (itemlist[position].cancellation_time_exceed==0
        ) {
            if (itemlist[position].quantity.equals("1")) {
                holder.itemsCount.text = itemlist[position].quantity.toString() + " item"
            } else {
                holder.itemsCount.text = itemlist[position].quantity.toString() + " items"
            }
            if (itemlist[position].item_status.equals("0"))
            {
                holder.statusExceed.text = "Cancelled"
                holder.statusExceed.setTextColor(mcontext.resources.getColor(R.color.red))
                holder.itemsCount.visibility = View.VISIBLE
                holder.multiLinear.visibility = View.GONE
                holder.linearlayout.setBackgroundColor(
                    mcontext.resources.getColor(R.color.light_grey)
                )
            }
            else if (itemlist[position].item_status
                    .equals("1")
            ) {
                holder.statusExceed.text = "Active"
                holder.statusExceed.setTextColor(
                    mcontext.resources.getColor(R.color.orange_circle)
                )
                holder.itemsCount.visibility = View.GONE
                holder.multiLinear.visibility = View.VISIBLE
                holder.linearlayout.setBackgroundColor(
                    mcontext.resources.getColor(R.color.canteen_item_bg)
                )
            } else {
                holder.statusExceed.text = "Delivered"
                holder.statusExceed.setTextColor(mcontext.resources.getColor(R.color.green))
                holder.itemsCount.visibility = View.VISIBLE
                holder.multiLinear.visibility = View.GONE
                holder.linearlayout.setBackgroundColor(
                    mcontext.resources.getColor(R.color.canteen_item_bg)
                )
            }
        } else {
            holder.itemsCount.visibility = View.VISIBLE
            holder.multiLinear.visibility = View.GONE
            if (itemlist[position].quantity.equals("1")) {
                holder.itemsCount.text = itemlist[position].quantity.toString() + " Item"
            } else {
                holder.itemsCount.text = itemlist[position].quantity.toString() + " Items"
            }
            if (itemlist[position].item_status.equals("0")) {
                holder.statusExceed.text = "Cancelled"
                holder.statusExceed.setTextColor(mcontext.resources.getColor(R.color.red))
            } else if (itemlist[position].item_status
                    .equals("1")
            ) {
                holder.statusExceed.text = "Active"
                holder.statusExceed.setTextColor(
                    mcontext.resources.getColor(R.color.orange_circle)
                )
            } else {
                holder.statusExceed.text = "Delivered"
                holder.statusExceed.setTextColor(mcontext.resources.getColor(R.color.green))
            }
        }


//        if (itemlist[position].portal.equals("2")) {
//            holder.portalTxt.visibility = View.VISIBLE
//        } else {
//            holder.portalTxt.visibility = View.GONE
//        }
        holder.cartitemcount.setOnValueChangeListener { view, oldValue, newValue ->
            canteen_cart_id = itemlist[position].id
            quantity = newValue.toString()
            if (InternetCheckClass.isInternetAvailable(mcontext)) {
                if (newValue != 0) {

                    // Quantity Change
                     UpdatePreOrderQuantity(canteen_cart_id,newValue.toString())
                    //getCartUpdate(URL_CANTEEN_CONFIRMED_ORDER_EDIT,canteen_cart_id,quantity);
                    //  getAddToCart(URL_CANTEEN_ADD_TO_CART,position,String.valueOf(newValue));
                    //cart update
                }
                else {

                    // PreOrder Item Cancel

                    callCancelPreOrderItem(canteen_cart_id)
                    //getCartCancel(URL_CANTEEN_CONFIRMED_ORDER_ITEM_CELL_CANCEL,canteen_cart_id);
                    //cart cancel
                }
               // callCanteenPreOrderUpdate(position)


            } else {
                InternetCheckClass.showSuccessInternetAlert(mcontext)
            }
        }

        if (allergycontentlist.size > 0) {
            holder.allergy_rec.setVisibility(View.VISIBLE)
            holder.allergy_info.setVisibility(View.VISIBLE)
            val llc = LinearLayoutManager(mcontext)
            llc.orientation = LinearLayoutManager.HORIZONTAL
            holder.allergy_rec.setLayoutManager(llc)
            val allergy_adapter = AllergyContentsAdapter(allergycontentlist, mcontext)
            holder.allergy_rec.setAdapter(allergy_adapter)
        } else {
            holder.allergy_rec.setVisibility(View.GONE)
            holder.allergy_info.setVisibility(View.GONE)
        }
        holder.allergy_info.setOnClickListener(View.OnClickListener {
            allergy_contents_popup(
                mcontext,
                itemlist.get(position).item_name
            )
        })

    }
    override fun getItemCount(): Int {
        return itemlist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemNameTxt: TextView
        var itemDescription:TextView
        lateinit var amountTxt:TextView
//        var status:TextView
        var amountExceedTxt:TextView
        var itemsCount:TextView
        var statusExceed:TextView
        var portalTxt:TextView
        var cartitemcount: ElegantNumberButton
        var multiLinear: LinearLayout
        var linearlayout: LinearLayout
        var exceedLinear:LinearLayout
        var bannerImagePager: ViewPager
        var allergy_rec: RecyclerView
        var allergy_info: ImageView
        init {
            itemNameTxt = itemView.findViewById(R.id.itemNameTxt)
            itemDescription = itemView.findViewById(R.id.itemDescription)
            cartitemcount = itemView.findViewById(R.id.cartitemcount)
            multiLinear = itemView.findViewById(R.id.multiLinear)
//            status = itemView.findViewById(R.id.statusExceed)
            linearlayout = itemView.findViewById(R.id.linearlayout)
            amountExceedTxt = itemView.findViewById(R.id.amountExceedTxt)
            itemsCount = itemView.findViewById(R.id.itemsCount)
            statusExceed = itemView.findViewById(R.id.statusExceed)
            exceedLinear = itemView.findViewById(R.id.exceedLinear)
            portalTxt = itemView.findViewById(R.id.portalTxt)
            bannerImagePager = itemView.findViewById(R.id.bannerImagePager)
            allergy_rec = itemView.findViewById<RecyclerView>(R.id.allergy_rec)
            allergy_info = itemView.findViewById<ImageView>(R.id.info_allergy)

        }
    }

    fun allergy_contents_popup(context: Context, itemname: String?) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_allergy_contents)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val item = dialog.findViewById<TextView>(R.id.item_name)
        val close_btn = dialog.findViewById<ImageView>(R.id.close)
        val allergypopuprec = dialog.findViewById<RecyclerView>(R.id.allergy_popup_rec)
        item.text = itemname
        close_btn.setOnClickListener { dialog.dismiss() }
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        allergypopuprec.layoutManager = llm
        val allergypopupAdapter = AllergyPopupAdapter( allergycontentlist ,context)
        allergypopuprec.adapter = allergypopupAdapter
        dialog.show()
    }
    fun callCancelPreOrderItem(order_id:String)
    {
        progress.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var model= CancelCanteenPreorderItemId(student_id,order_id.toString())
        val call: Call<CanteenPreorderModel> = ApiClient.getClient.cancelCanteenPreOrderItem(model,"Bearer "+token)
        call.enqueue(object : Callback<CanteenPreorderModel> {
            override fun onFailure(call: Call<CanteenPreorderModel>, t: Throwable) {
                progress.hide()
                CommonFunctions.faliurepopup(mcontext)

            }
            override fun onResponse(call: Call<CanteenPreorderModel>, response: Response<CanteenPreorderModel>) {
                val responsedata = response.body()
                progress.hide()
                if (responsedata!!.status==100) {

                    getMyOrderDetails()
                }
                else if (response.body()!!.status == 116)
                {


                }
                else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mcontext)
                    }
                }
            }

        })
    }


    fun UpdatePreOrderQuantity(orderID:String,quantity:String)
    {
        progress.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var model= UpdateCanteenPreorderItemApiModel(student_id,quantity,orderID)
        val call: Call<CanteenPreorderModel> = ApiClient.getClient.updateCanteenPreOrderItem(model,"Bearer "+token)
        call.enqueue(object : Callback<CanteenPreorderModel> {
            override fun onFailure(call: Call<CanteenPreorderModel>, t: Throwable) {

                progress.hide()
                CommonFunctions.faliurepopup(mcontext)

            }
            override fun onResponse(call: Call<CanteenPreorderModel>, response: Response<CanteenPreorderModel>) {
                val responsedata = response.body()
                progress.hide()
                if (responsedata!!.status==100) {

                    getMyOrderDetails()
                }
                else if (response.body()!!.status == 116)
                {


                }
                else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mcontext)
                    }
                }
            }

        })
    }
    private fun getMyOrderDetails(){

        progress.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var model= OrderHistoryApiModel(student_id,"0","100")
        val call: Call<PreOrdersModel> = ApiClient.getClient.canteen_myorder_history(model,"Bearer "+token)
        call.enqueue(object : Callback<PreOrdersModel> {
            override fun onFailure(call: Call<PreOrdersModel>, t: Throwable) {

                progress.hide()
                CommonFunctions.faliurepopup(mcontext)

            }
            override fun onResponse(call: Call<PreOrdersModel>, response: Response<PreOrdersModel>) {
                val responsedata = response.body()
                progress.hide()
                if (responsedata!!.status==100) {

                    dateRecyclerView.layoutManager = LinearLayoutManager(mcontext)
                    dateRecyclerView.adapter = MyorderDatesAdapter(response.body()!!.responseArray.data, mcontext,student_id,
                        dateRecyclerView,bottom,item,noitem,progress)

                }
                else if (response.body()!!.status == 116)
                {


                }
                else {
                    if (response.body()!!.status == 132) {
                        bottom.visibility= View.GONE
                        item.visibility= View.GONE
                        dateRecyclerView.visibility=View.GONE
                        noitem.visibility=View.VISIBLE
                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mcontext)
                    }
                }
            }

        })

        //dateRecyclerView.adapter = MyorderDatesAdapter(mMyOrderArrayList, nContext)

    }
}