package com.mobatia.bisad.activity.canteen.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.downloader.Progress
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.Myorderbasket_Activity
import com.mobatia.bisad.activity.canteen.model.add_to_cart.CanteenCartRemoveApiModel
import com.mobatia.bisad.activity.canteen.model.add_to_cart.CanteenCartRemoveModel
import com.mobatia.bisad.activity.canteen.model.add_to_cart.CanteenCartUpdateApiModel
import com.mobatia.bisad.activity.canteen.model.add_to_cart.CanteenCartUpdateModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartApiModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartResModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CartItemsListModel
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class BasketItemsAdapter (
    var items_list: ArrayList<CartItemsListModel>, var mcontext: Context, var ordered_user_type:String,
    var student_id:String,
    var parent_id:String,
    var staff_id:String,
    var delivery_date:String,
var itemtxt:TextView,var amnttxt:TextView,var itemLinear:LinearLayout,var noItemTxt:ImageView,
var dateRec:RecyclerView,var progress:ProgressBarDialog) :

    RecyclerView.Adapter<BasketItemsAdapter.ViewHolder>() {
    lateinit var cart_list: ArrayList<CanteenCartResModel>
    var cartTotalAmount:Int=0
    var cartTotalItems:Int=0
    var quantity = ""
    var canteen_cart_id = ""
    var apiCall:Int = 0
    var homeBannerUrlImageArray: java.util.ArrayList<String>? = null
    var currentPage = 0
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.basket_itemlist_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNameTxt.text = items_list[position].item_name
        holder.itemDescription.text = items_list[position].description
        holder.amountTxt.text = items_list[position].price.toString() + "AED"
        holder.notAvailableTxt.visibility = View.GONE
        holder.removeTxt.visibility = View.GONE
        holder.multiLinear.visibility = View.VISIBLE
        holder.cartitemcount.setNumber(items_list.get(position).quantity.toString())
        holder.cartitemcount.setRange(
            0,
            50)
        holder.cartitemcount.setOnValueChangeListener { view, oldValue, newValue ->
            canteen_cart_id = items_list[position].id.toString()
            quantity = newValue.toString()

            if (InternetCheckClass.isInternetAvailable(mcontext)) {
                if (newValue != 0) {
                    //progressDialogAdd.visibility=View.VISIBLE
                    progress.show()
                    updateCart(items_list[position].id.toString(),position,quantity)
                    notifyDataSetChanged()
                } else {
                    //progressDialogAdd.visibility=View.VISIBLE
                    progress.show()
                    cancelCart(position)
                    notifyDataSetChanged()
                }
            } else {
                InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
            }
        }






       /* if (items_list[position].available_quantity.equals("0")) {
            holder.notAvailableTxt.visibility = View.VISIBLE
            holder.removeTxt.visibility = View.VISIBLE
            holder.multiLinear.visibility = View.GONE
        } else {
            if (items_list[position].portal.equals("1")) {
                holder.portalTxt.visibility = View.GONE
            } else {
                holder.portalTxt.visibility = View.VISIBLE
            }
            holder.notAvailableTxt.visibility = View.GONE
            holder.removeTxt.visibility = View.GONE
            holder.multiLinear.visibility = View.VISIBLE
            holder.cartitemcount.number = items_list[position].quantity
            holder.cartitemcount.setRange(
                0,
                Integer.valueOf(items_list[position].available_quantity)
            )
        }*/
        holder.removeTxt.setOnClickListener {
            //cart cancel
        }
       /* holder.cartitemcount.setOnValueChangeListener { view, oldValue, newValue ->
            canteen_cart_id = items_list[position].item_id
            quantity = newValue.toString()
            println("Click works")
            if (InternetCheckClass.isInternetAvailable(mcontext)) {
                if (newValue != 0) {
                    if (newValue == Integer.valueOf(
                            items_list[position].available_quantity
                        )
                    ) {
                        Toast.makeText(
                            mcontext,
                            "You have reached the Pre-Order limit for this item",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    //getCartUpdate(URL_CANTEEN_CONFIRMED_ORDER_EDIT,canteen_cart_id,quantity);
                    //  getAddToCart(URL_CANTEEN_ADD_TO_CART,position,String.valueOf(newValue));
                    //cart update
                } else {
                    //getCartCancel(URL_CANTEEN_CONFIRMED_ORDER_ITEM_CELL_CANCEL,canteen_cart_id);
                    //cart cancel
                }
            } else {
                InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
            }
        }*/
       /* homeBannerUrlImageArray = items_list[position].item_image
        if (homeBannerUrlImageArray != null) {
            val handler = Handler()
            val Update = Runnable {
                if (currentPage == homeBannerUrlImageArray!!.size) {
                    currentPage = 0
                    holder.bannerImagePager.setCurrentItem(
                        currentPage,
                        false
                    )
                } else {
                    holder.bannerImagePager
                        .setCurrentItem(currentPage++, true)
                }
            }
            val swipeTimer = Timer()
            swipeTimer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(Update)
                }
            }, 100, 3000)
            holder.bannerImagePager.adapter =
                ImagePagerDrawableAdapter(homeBannerUrlImageArray!!, mcontext)
        } else {
            holder.bannerImagePager.setBackgroundResource(R.drawable.noitemscanteen)
        }*/

    }



    override fun getItemCount(): Int {
        return items_list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

       // var itemImage: ImageView
        var itemNameTxt: TextView
        var itemDescription:TextView
        var amountTxt:TextView
        var notAvailableTxt:TextView
        var removeTxt:TextView
        var portalTxt:TextView
        var cartitemcount: ElegantNumberButton
        var multiLinear: LinearLayout
        var linearlayout: LinearLayout
        var bannerImagePager: ViewPager
        // var Datas:String=""

        init {
            //itemImage = view.findViewById<ImageView>(R.id.itemImage)
            itemNameTxt = view.findViewById<TextView>(R.id.itemNameTxt)
            itemDescription = view.findViewById<TextView>(R.id.itemDescription)
            amountTxt = view.findViewById<TextView>(R.id.amountTxt)
            cartitemcount = view.findViewById<ElegantNumberButton>(R.id.cartitemcount)
            multiLinear = view.findViewById<LinearLayout>(R.id.multiLinear)
            linearlayout = view.findViewById<LinearLayout>(R.id.linearlayout)
            notAvailableTxt = view.findViewById<TextView>(R.id.notAvailableTxt)
            removeTxt = view.findViewById<TextView>(R.id.removeTxt)
            portalTxt = view.findViewById<TextView>(R.id.portalTxt)
            bannerImagePager = view.findViewById(R.id.bannerImagePager)
        }
    }
    private fun getcanteen_cart(){
        cart_list= ArrayList()
        cartTotalAmount=0
        cartTotalItems=0
        //progressDialogAdd.visibility=View.VISIBLE
        progress.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var canteenCart= CanteenCartApiModel(PreferenceData().getStudentID(mcontext).toString())
        val call: Call<CanteenCartModel> = ApiClient.getClient.get_canteen_cart(canteenCart,"Bearer "+token)
        call.enqueue(object : Callback<CanteenCartModel> {
            override fun onFailure(call: Call<CanteenCartModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                //progressDialogAdd.visibility=View.GONE
                progress.hide()
            }
            override fun onResponse(call: Call<CanteenCartModel>, response: Response<CanteenCartModel>) {
                val responsedata = response.body()
                //progressDialogAdd.visibility=View.GONE
                progress.hide()
                if (responsedata!!.status==100) {
                    //progress.visibility = View.GONE
                    cart_list=response!!.body()!!.responseArray.data
                    for (i in cart_list.indices){
                        Log.e("ta",cart_list[i].total_amount.toString())
                        cartTotalAmount=cartTotalAmount + cart_list[i].total_amount
                    }
                    if (cartTotalAmount==0){
                        //bottomView.visibility=View.GONE
                    }else{
                        //bottomView.visibility=View.VISIBLE
                        for (i in cart_list.indices){

                            for (j in cart_list[i].items.indices){
                                cartTotalItems=cartTotalItems + cart_list[i].items[j].quantity
                            }
                        }
                        Log.e("items",cartTotalItems.toString())
                        Log.e("price",cartTotalAmount.toString())
                        itemtxt.setText(cartTotalItems.toString() + "Items")
                        amnttxt.setText(cartTotalAmount.toString() + "AED")
                    }
                    dateRec.layoutManager = LinearLayoutManager(mcontext)
                    dateRec.adapter =
                        DatesBasketAdapter(cart_list, mcontext,ordered_user_type,student_id,parent_id,staff_id,
                            itemtxt,amnttxt,itemLinear,noItemTxt,dateRec,progress)
                    notifyDataSetChanged()
                }else if (response.body()!!.status == 116) {
                    if (apiCall != 4) {
                        apiCall = apiCall + 1
                        AccessTokenClass.getAccessToken(mcontext)

                    } else {

                        showSuccessAlertnew(
                            mcontext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )

                    }
                } else {
                    if (response.body()!!.status == 132) {
                        itemLinear.visibility=View.GONE
                        noItemTxt.visibility=View.VISIBLE
                        dateRec.visibility=View.GONE
                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mcontext)
                    }
                }
            }

        })
    }
    private fun updateCart(id:String,position: Int,quant:String){
        //progressDialogAdd.visibility=View.VISIBLE
        progress.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var canteenadd= CanteenCartUpdateApiModel(
            PreferenceData().getStudentID(mcontext).toString(),delivery_date,quant,
            items_list[position].item_id.toString(),items_list[position].id.toString())
        val call: Call<CanteenCartUpdateModel> = ApiClient.getClient.update_canteen_cart(canteenadd,"Bearer "+token)
        call.enqueue(object : Callback<CanteenCartUpdateModel> {
            override fun onFailure(call: Call<CanteenCartUpdateModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                //progressDialogAdd.visibility=View.GONE
                progress.hide()
            }
            override fun onResponse(call: Call<CanteenCartUpdateModel>, response: Response<CanteenCartUpdateModel>) {
                val responsedata = response.body()
                //progressDialogAdd.visibility=View.GONE
                progress.hide()
                if (responsedata!!.status==100) {
                    items_list[position].quantity=quant.toInt()

                    items_list[position].id=canteen_cart_id.toInt()
                    //Toast.makeText(mContext,"Item Successfully added to basket",Toast.LENGTH_SHORT).show();
                    /* itemlist.get(position).setQuantityCart(quantity)
                     mCartDetailArrayList.get(position).setItemCart(true)
                     mCartDetailArrayList.get(position).setCartItemId(canteen_cart_id)*/

                   // Myorderbasket_Activity().getcanteen_cart()
                   getcanteen_cart()
                    notifyDataSetChanged()

                }else if (response.body()!!.status == 116) {
                    if (apiCall != 4) {
                        apiCall = apiCall + 1
                        AccessTokenClass.getAccessToken(mcontext)

                    } else {

                        showSuccessAlertnew(
                            mcontext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )

                    }
                } else {
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
    private fun cancelCart(position: Int){
        //progressDialogAdd.visibility=View.VISIBLE
        progress.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var canteenadd= CanteenCartRemoveApiModel(
            PreferenceData().getStudentID(mcontext).toString(),items_list[position].id.toString())
        val call: Call<CanteenCartRemoveModel> = ApiClient.getClient.remove_canteen_cart(canteenadd,"Bearer "+token)
        call.enqueue(object : Callback<CanteenCartRemoveModel> {
            override fun onFailure(call: Call<CanteenCartRemoveModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                //progressDialogAdd.visibility=View.GONE
                progress.hide()
            }
            override fun onResponse(call: Call<CanteenCartRemoveModel>, response: Response<CanteenCartRemoveModel>) {
                val responsedata = response.body()
                //progressDialogAdd.visibility=View.GONE
                progress.hide()
                if (responsedata!!.status==100) {
                    items_list[position].quantity=0

                    items_list[position].id=canteen_cart_id.toInt()
                  /*  items_list[position].isItemCart=false
                    items_list[position].quantityCart=0
                    itemlist[position].cartId=canteen_cart_id*/

                    /* mCartDetailArrayList.get(position).setItemCart(false)
                     mCartDetailArrayList.get(position).setQuantityCart("0")
                     mCartDetailArrayList.get(position).setCartItemId(canteen_cart_id)*/
                    //Myorderbasket_Activity().getcanteen_cart()
                    getcanteen_cart()
                    notifyDataSetChanged()

                }else if (response.body()!!.status == 116) {
                    if (apiCall != 4) {
                        apiCall = apiCall + 1
                        AccessTokenClass.getAccessToken(mcontext)

                    } else {

                        showSuccessAlertnew(
                            mcontext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )

                    }
                } else {
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
    fun showSuccessAlertnew(context: Context, message: String, msgHead: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        iconImageView.setImageResource(R.drawable.exclamationicon)
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()
    }
}