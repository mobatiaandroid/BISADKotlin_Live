package com.mobatia.bisad.activity.canteen.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
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
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.model.AllergyContentModel
import com.mobatia.bisad.activity.canteen.model.add_orders.CatItemsListModel
import com.mobatia.bisad.activity.canteen.model.add_to_cart.*
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartApiModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartResModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PreorderItemsAdapter(
    val itemlist: ArrayList<CatItemsListModel>,
    var mcontext: Context,
    var date: String,
    var cart_list: ArrayList<CanteenCartResModel>,
    var cartTotalAmount: Int,
    var totalItems: TextView,
    var totalPrice: TextView,
    var bottomView: LinearLayout,
    var cart_empty: ImageView,
    var progressDialogP: ProgressBarDialog,
   var allergycontentlist: ArrayList<AllergyContentModel>
) :
    RecyclerView.Adapter<PreorderItemsAdapter.ViewHolder>() {
  // lateinit var onBottomReachedListener: OnBottomReachedListener
    lateinit var homeBannerUrlImageArray:ArrayList<String>
    var currentPage = 0
    var canteen_cart_id = ""
    var quantity = ""
    var apiCall: Int = 0
    var cartTotalItems:Int=0

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.preorder_itemlist_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //onBottomReachedListener.onBottomReached(position)
        //bottomView.visibility=View.GONE
        if (itemlist.get(position).isAllergic === true){
            holder.allergy_info.visibility=View.VISIBLE
            holder.allergy_rec.visibility=View.VISIBLE
            var llm = (LinearLayoutManager(mcontext))
            llm.orientation = LinearLayoutManager.HORIZONTAL
            holder.allergy_rec.layoutManager = llm
            var allergy_adapter=AllergyContentsAdapter(allergycontentlist,mcontext)
            holder.allergy_rec.adapter=allergy_adapter

            /* holder.allergy_rec.addOnItemClickListener(object : OnItemClickListener {
                 override fun onItemClicked(position: Int, view: View) {
                     Toast.makeText(mcontext, allergycontentlist[position].name, Toast.LENGTH_SHORT).show()

                     //allergy_contents_popup(mcontext)

             }
         })*/
        }
        else{
            holder.allergy_rec.visibility=View.GONE
            holder.allergy_info.visibility=View.GONE
        }
        holder.allergy_info.setOnClickListener {
            allergy_contents_popup(mcontext,itemlist[position].item_name)
        }
        holder.itemNameTxt.text=itemlist[position].item_name
        holder.itemDescription.text = itemlist[position].description
        holder.amountTxt.text = itemlist[position].price.toString() + " AED"
        if (itemlist[position].item_already_ordered==0) {
            holder.confirmedTxt.visibility = View.GONE
        } else {
            holder.confirmedTxt.visibility = View.VISIBLE
        }
        if (itemlist[position].isItemCart) {
            holder.multiLinear.visibility = View.VISIBLE
            holder.addLinear.visibility = View.GONE
            holder.itemCount.setNumber(itemlist.get(position).quantityCart.toString())
            holder.itemCount.setRange(
                0,
                50
            )
        } else {
            holder.multiLinear.visibility = View.GONE
            holder.addLinear.visibility = View.VISIBLE
        }
        holder.addLinear.setOnClickListener {
            if (itemlist.get(position).isAllergic === true) {
                Toast.makeText(mcontext, "This item contains ingredients your child is allergic to and cannot be ordered.", Toast.LENGTH_SHORT).show()
                //allergypresentpopup( itemlist[position].id,itemlist[position].price,position, "1")
            } else {
                addToCart(itemlist[position].id,itemlist[position].price,position)
            }

        }


        //holder.itemCount.setNumber(itemlist.get(position).quantityCart.toString())
        holder.itemCount.setOnValueChangeListener { view, oldValue, newValue ->

            var cartPos:Int=0;
            for (i in 0.. cart_list.size-1)
            {
                if (cart_list.get(i).delivery_date.equals(date))
                {
                    cartPos=i
                }
            }
            for (i in cart_list[cartPos].items.indices)
            {
                if (itemlist.get(position).id.equals(cart_list.get(cartPos).items.get(i).item_id.toString()))
                {
                    canteen_cart_id= cart_list.get(cartPos).items.get(i).id.toString()
                }
            }
          //  canteen_cart_id = cart_list[cartPos].items.get(position).id
            quantity = newValue.toString()
            if (InternetCheckClass.isInternetAvailable(mcontext)) {
                if (newValue != 0) {
                    //progressDialogP.visibility=View.VISIBLE
                    updateCart(itemlist[position].id,position,quantity)

                }
                else {
                    //progressDialogP.visibility=View.VISIBLE
                    cancelCart(position)
                }
            } else {
                InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
            }
        }

    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var cartStatusImg: Button
        lateinit var itemNameTxt: TextView
        lateinit    var amountTxt:TextView
        lateinit    var notAvailableTxt:TextView
        lateinit    var itemDescription:TextView
        lateinit    var confirmedTxt:TextView
        lateinit var itemImage: ImageView
        lateinit var addLinear: LinearLayout
        lateinit var multiLinear:LinearLayout
        lateinit var itemCount: ElegantNumberButton
        lateinit var bannerImagePager: ViewPager
        lateinit var allergy_rec:RecyclerView
        lateinit var allergy_info:ImageView

        init {

//            cartStatusImg = (Button) view.findViewById(R.id.cartStatusImg);
            itemNameTxt =itemView.findViewById(R.id.itemNameTxt)
            amountTxt = itemView.findViewById(R.id.amountTxt)
            notAvailableTxt = itemView.findViewById(R.id.notAvailableTxt) as TextView
           // itemImage = itemView.findViewById(R.id.itemImage) as ImageView
            addLinear = itemView.findViewById(R.id.addLinear) as LinearLayout
            multiLinear = itemView.findViewById(R.id.multiLinear) as LinearLayout
            itemCount = itemView.findViewById(R.id.itemCount)
            itemDescription = itemView.findViewById(R.id.itemDescription)
            confirmedTxt = itemView.findViewById(R.id.confirmedTxt)
            bannerImagePager = itemView.findViewById(R.id.bannerImagePager) as ViewPager
            allergy_rec=itemView.findViewById(R.id.allergy_rec)
            allergy_info=itemView.findViewById(R.id.info_allergy)
        }
    }

    fun allergypresentpopup(id: String, price: String, position: Int, s: String) {
        val dialog = Dialog(mcontext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(R.drawable.round)
        icon.setImageResource(R.drawable.questionmark_icon)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = "This item contains ingredients your child is allergic to and cannot be ordered."
        textHead.text = "Confirm"
        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
            dialog.dismiss()
            addToCart(id,price,position)

        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
    private fun getcanteen_cart(){
        cart_list= ArrayList()
        cartTotalAmount=0
        cartTotalItems=0
        //progressDialogP.visibility=View.VISIBLE
        progressDialogP.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var canteenCart= CanteenCartApiModel(PreferenceData().getStudentID(mcontext).toString())
        val call: Call<CanteenCartModel> = ApiClient.getClient.get_canteen_cart(canteenCart,"Bearer "+token)
        call.enqueue(object : Callback<CanteenCartModel> {
            override fun onFailure(call: Call<CanteenCartModel>, t: Throwable) {

                progressDialogP.hide()
                CommonFunctions.faliurepopup(mcontext)

            }
            override fun onResponse(call: Call<CanteenCartModel>, response: Response<CanteenCartModel>) {
                val responsedata = response.body()
                //progressDialogP.visibility=View.GONE
                progressDialogP.hide()
                if (responsedata!!.status==100) {
                    bottomView.visibility=View.VISIBLE
                    //progress.visibility = View.GONE
                    cart_list=response!!.body()!!.responseArray.data
                    for (i in cart_list.indices){
                        cartTotalAmount=cartTotalAmount + cart_list[i].total_amount
                    }


                        for (i in cart_list.indices){

                            for (j in cart_list[i].items.indices){
                                cartTotalItems=cartTotalItems + cart_list[i].items[j].quantity
                            }
                        }

                        totalItems.setText(cartTotalItems.toString() + "Items")
                        totalPrice.setText(cartTotalAmount.toString() + "AED")
                        //progressDialogP.visibility=View.GONE
                    progressDialogP.hide()
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
                        bottomView.visibility=View.GONE
                        //cart_empty.visibility=View.VISIBLE
                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mcontext)
                    }
                }
            }

        })
    }
private fun addToCart(id:String,price:String,position: Int){
    //progressDialogP.visibility=View.VISIBLE
    progressDialogP.show()
    val token = PreferenceData().getaccesstoken(mcontext)
    var canteenadd= AddToCartCanteenApiModel(
        PreferenceData().getStudentID(mcontext).toString(),id,date,"1",price)
    val call: Call<AddToCartCanteenModel> = ApiClient.getClient.add_to_canteen_cart(canteenadd,"Bearer "+token)
    call.enqueue(object : Callback<AddToCartCanteenModel> {
        override fun onFailure(call: Call<AddToCartCanteenModel>, t: Throwable) {

            progressDialogP.hide()
            CommonFunctions.faliurepopup(mcontext)

        }
        override fun onResponse(call: Call<AddToCartCanteenModel>, response: Response<AddToCartCanteenModel>) {
            val responsedata = response.body()
            //progressDialogP.visibility=View.GONE
            if (responsedata!!.status==100) {
                itemlist[position].quantityCart=1
                itemlist[position].isItemCart=true


                //  Toast.makeText(mContext,"Item Successfully added to cart",Toast.LENGTH_SHORT).show();

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
    private fun updateCart(id:String,position: Int,quant:String){
        //progressDialogP.visibility=View.VISIBLE
        progressDialogP.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var canteenadd= CanteenCartUpdateApiModel(
            PreferenceData().getStudentID(mcontext).toString(),date,quant,
            itemlist[position].id,canteen_cart_id)
        val call: Call<CanteenCartUpdateModel> = ApiClient.getClient.update_canteen_cart(canteenadd,"Bearer "+token)
        call.enqueue(object : Callback<CanteenCartUpdateModel> {
            override fun onFailure(call: Call<CanteenCartUpdateModel>, t: Throwable) {

                progressDialogP.hide()
                CommonFunctions.faliurepopup(mcontext)

            }
            override fun onResponse(call: Call<CanteenCartUpdateModel>, response: Response<CanteenCartUpdateModel>) {
                val responsedata = response.body()
                //progressDialogP.visibility=View.GONE
                progressDialogP.hide()
                if (responsedata!!.status==100) {
                itemlist[position].quantityCart=quant.toInt()
                itemlist[position].isItemCart=true
                itemlist[position].cartId=canteen_cart_id

                    //Toast.makeText(mContext,"Item Successfully added to basket",Toast.LENGTH_SHORT).show();
                   /* itemlist.get(position).setQuantityCart(quantity)
                    mCartDetailArrayList.get(position).setItemCart(true)
                    mCartDetailArrayList.get(position).setCartItemId(canteen_cart_id)*/
                    //progressDialogP.visibility=View.VISIBLE
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
        //progressDialogP.visibility=View.VISIBLE
        progressDialogP.show()
        val token = PreferenceData().getaccesstoken(mcontext)
        var canteenadd= CanteenCartRemoveApiModel(
            PreferenceData().getStudentID(mcontext).toString(),canteen_cart_id)
        val call: Call<CanteenCartRemoveModel> = ApiClient.getClient.remove_canteen_cart(canteenadd,"Bearer "+token)
        call.enqueue(object : Callback<CanteenCartRemoveModel> {
            override fun onFailure(call: Call<CanteenCartRemoveModel>, t: Throwable) {

                progressDialogP.hide()
                CommonFunctions.faliurepopup(mcontext)

            }
            override fun onResponse(call: Call<CanteenCartRemoveModel>, response: Response<CanteenCartRemoveModel>) {
                val responsedata = response.body()
               // progressDialogP.visibility=View.GONE
                progressDialogP.hide()
                if (responsedata!!.status==100) {

                    itemlist[position].isItemCart=false
                    itemlist[position].quantityCart=0
                    itemlist[position].cartId=canteen_cart_id

                   /* mCartDetailArrayList.get(position).setItemCart(false)
                    mCartDetailArrayList.get(position).setQuantityCart("0")
                    mCartDetailArrayList.get(position).setCartItemId(canteen_cart_id)*/
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
    fun allergy_contents_popup(context: Context,item:String){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_allergy_contents)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var item_name:TextView=dialog.findViewById(R.id.item_name)
        var close:ImageView=dialog.findViewById(R.id.close)
        close.setOnClickListener {
            dialog.dismiss()
        }
        item_name.text=item

        var allergy_popup_rec=dialog.findViewById<RecyclerView>(R.id.allergy_popup_rec)
        allergy_popup_rec.layoutManager=LinearLayoutManager(context)
        var allergypopupAdapter=AllergyPopupAdapter(allergycontentlist,context)
        allergy_popup_rec.adapter=allergypopupAdapter
        dialog.show()
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