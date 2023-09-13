package com.mobatia.bisad.activity.canteen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.adapter.DatesBasketAdapter
import com.mobatia.bisad.activity.canteen.model.DateModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.*
import com.mobatia.bisad.activity.canteen.model.preorder.CanteenPreorderApiModel
import com.mobatia.bisad.activity.canteen.model.preorder.CanteenPreorderModel
import com.mobatia.bisad.activity.canteen.model.wallet.WalletBalanceApiModel
import com.mobatia.bisad.activity.canteen.model.wallet.WalletBalanceModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Myorderbasket_Activity : AppCompatActivity() {
    lateinit var nContext: Context
    lateinit var tab_type: String
    lateinit var relativeHeader: RelativeLayout
    lateinit var progress: ProgressBarDialog
    var ordered_user_type = ""
    var student_id = ""
    var parent_id = ""
    var staff_id = ""
    var cartTotalAmount = 0
    var apiCall: Int = 0
    var cartTotalItem = 0
    lateinit var mDateArrayList: ArrayList<DateModel>
    lateinit var cart_list: ArrayList<CanteenCartResModel>
    lateinit var cart_items_list: ArrayList<CartItemsListModel>
    lateinit var dateRecyclerView: RecyclerView
    lateinit var noItemTxt: ImageView
    lateinit var itemLinear: LinearLayout
    lateinit var amountTxt: TextView
    lateinit var itemTxt: TextView
    lateinit var itemArray: ArrayList<ItemsModel>
    lateinit var orderArray: ArrayList<OrdersModel?>
    lateinit var homeBannerUrlImageArray: ArrayList<String>
    var walletAmountString = "0"
    var WalletAmount = 0
    //var CartTotalAmount = 0
    var BalanceWalletAmount = 0
    var BalanceConfirmWalletAmount = 0
    var TotalOrderedAmount = 0

    private lateinit var logoClickImg: ImageView
    lateinit var back: ImageView
    lateinit var id:String
    lateinit var title:TextView
    //lateinit var progressDialogAdd:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_myorderbasket)

        initfn()

    }

    private fun initfn() {
        //date = intent.getStringExtra("date").toString()
        nContext = this
        progress=ProgressBarDialog(nContext)
        logoClickImg=findViewById(R.id.logoclick)
        back = findViewById(R.id.back)
        title = findViewById(R.id.textViewtitle)
       dateRecyclerView =
            findViewById(R.id.dateRecyclerView)
        noItemTxt =
            findViewById(R.id.noItemTxt)
        amountTxt =
            findViewById(R.id.amountTxt)
        itemTxt =
            findViewById(R.id.itemTxt)
        itemLinear =
            findViewById(R.id.itemLinear)
        /*progressDialogAdd =
            findViewById(R.id.progressDialogAdd)*/
        mDateArrayList=ArrayList()
        cart_list=ArrayList()
        cart_items_list=ArrayList()
        itemArray=ArrayList()
        orderArray=ArrayList()
        id = PreferenceData().getStudentID(nContext).toString()
        noItemTxt = findViewById(R.id.noItemTxt)

        title.text = "Basket"

        back.setOnClickListener {
            finish()
        }
        logoClickImg.setOnClickListener {
            val intent = Intent(nContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        //progressDialogAdd.visibility=View.VISIBLE

        wallet_details()
        getcanteen_cart()

        dateRecyclerView.layoutManager = LinearLayoutManager(nContext)
        dateRecyclerView.adapter =
            DatesBasketAdapter(cart_list, nContext,ordered_user_type,student_id,parent_id,staff_id,
                itemTxt,amountTxt,itemLinear,noItemTxt,dateRecyclerView,progress)
        itemLinear.setOnClickListener(
            View.OnClickListener {
                //var isNoItemAvailableFound = false
                //var basketPos = 0
                //var basketDetailPos = 0


                    if (WalletAmount >= cartTotalAmount) {


                            itemArray = ArrayList()
                            for (i in cart_list.indices) {
                                val mModel = ItemsModel()
                                mModel.delivery_date=cart_list[i].delivery_date

                                orderArray = ArrayList()
                                for (j in 0 until cart_list.get(
                                    i
                                ).items.size) {
                                    val nModel = OrdersModel()
                                    nModel.id=cart_list[i].items[j].id.toString()
                                    nModel.item_id=cart_list[i].items[j].item_id.toString()
                                    nModel.quantity=cart_list[i].items[j].quantity.toString()
                                    nModel.price=cart_list[i].items[j].price.toString()

                                    orderArray.add(nModel)
                                }
                                mModel.items=orderArray
                               // mModel.setItems(orderArray)
                                itemArray.add(mModel)
                            }
                            val gson = Gson()
                            val Data = gson.toJson(itemArray)
                            val JSON =
                                "{\"student_id\":\"" + PreferenceData().getStudentID(nContext) + "\"," +
                                        "\"orders\":" + Data + "}"
                            Log.e("JSON:", JSON)
                            //get pre order
                        //progressDialogAdd.visibility=View.VISIBLE
                        progress.show()
                        getPreOrder( JSON,itemArray)
                    } else {
                        showInsufficientBal(
                            nContext,
                            "Alert",
                            "Insufficient balance please top up wallet",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )

                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert","You don't have enough balance in your wallet, Please topup your wallet.", R.drawable.exclamationicon, R.drawable.round);
                    }

            })

    }
    private fun wallet_details(){
        val token = PreferenceData().getaccesstoken(nContext)
        var canteenCart= WalletBalanceApiModel(PreferenceData().getStudentID(nContext).toString())
        val call: Call<WalletBalanceModel> = ApiClient.getClient.get_wallet_balance(canteenCart,"Bearer "+token)
        call.enqueue(object : Callback<WalletBalanceModel> {
            override fun onFailure(call: Call<WalletBalanceModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
            }
            override fun onResponse(call: Call<WalletBalanceModel>, response: Response<WalletBalanceModel>) {
                val responsedata = response.body()
                Log.e("Response", responsedata.toString())
                if (responsedata!!.status==100) {
                    WalletAmount=response!!.body()!!.responseArray.wallet_balance

                }else if (response.body()!!.status == 116) {
                    if (apiCall != 4) {
                        apiCall = apiCall + 1
                        AccessTokenClass.getAccessToken(nContext)

                    } else {

                        showSuccessAlertnew(
                            nContext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )

                    }
                } else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
                    }
                }
            }

        })
    }
    fun getcanteen_cart(){
        cart_list= java.util.ArrayList()
        cartTotalAmount=0
        cartTotalItem=0
        //progressDialogAdd.visibility=View.VISIBLE
        progress.show()
        val token = PreferenceData().getaccesstoken(nContext)
        var canteenCart= CanteenCartApiModel(PreferenceData().getStudentID(nContext).toString())
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
                Log.e("Response", responsedata.toString())
                if (responsedata!!.status==100) {
                    //progress.visibility = View.GONE
                    cart_list=response!!.body()!!.responseArray.data
                    for (i in cart_list.indices){

                        cartTotalAmount=cartTotalAmount + cart_list[i].total_amount
                    }
                    if (cartTotalAmount==0){
                        //bottomview.visibility=View.GONE
                    }else{
                        dateRecyclerView.setVisibility(
                            View.VISIBLE
                        )
                        itemLinear.setVisibility(
                            View.VISIBLE
                        )
                        noItemTxt.setVisibility(
                            View.GONE
                        )
                        //bottomview.visibility=View.VISIBLE
                        for (i in cart_list.indices){

                            for (j in cart_list[i].items.indices){
                                cartTotalItem=cartTotalItem + cart_list[i].items[j].quantity
                            }
                        }
                        itemTxt.setText(cartTotalItem.toString() + "Items")
                        amountTxt.setText(cartTotalAmount.toString() + "AED")
                    }
                    dateRecyclerView.layoutManager = LinearLayoutManager(nContext)
                    dateRecyclerView.adapter =
                        DatesBasketAdapter(cart_list, nContext,ordered_user_type,student_id,parent_id,staff_id,
                        itemTxt,amountTxt,itemLinear,noItemTxt,dateRecyclerView,progress)

                }else if (response.body()!!.status == 116) {
                    if (apiCall != 4) {
                        apiCall = apiCall + 1
                        AccessTokenClass.getAccessToken(nContext)

                    } else {

                        showSuccessAlertnew(
                            nContext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )

                    }
                } else {
                    if (response.body()!!.status == 132) {
                        dateRecyclerView.setVisibility(
                            View.GONE
                        )
                        itemLinear.setVisibility(
                            View.GONE
                        )
                        noItemTxt.setVisibility(
                            View.VISIBLE
                        )
                        //bottomview.visibility=View.GONE
                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
                    }
                }
            }

        })
    }
    private fun getPreOrder(data:String,itemArray:ArrayList<ItemsModel>){
        val token = PreferenceData().getaccesstoken(nContext)
        //progressDialogAdd.visibility=View.VISIBLE
        progress.show()
        var canteenCart= CanteenPreorderApiModel(PreferenceData().getStudentID(nContext).toString(),itemArray)
        Log.e("cc",canteenCart.toString())
        val call: Call<CanteenPreorderModel> = ApiClient.getClient.canteen_preorder(canteenCart,"Bearer "+token)
        call.enqueue(object : Callback<CanteenPreorderModel> {
            override fun onFailure(call: Call<CanteenPreorderModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                //progressDialogAdd.visibility=View.GONE
                progress.hide()
            }
            override fun onResponse(call: Call<CanteenPreorderModel>, response: Response<CanteenPreorderModel>) {
                val responsedata = response.body()
                //progressDialogAdd.visibility=View.GONE
                progress.hide()
                if (responsedata!!.status==100) {
                    showDialogAlertDismiss(
                        nContext,
                        "Thank You!",
                        "Your order has been successfully placed",
                        R.drawable.tick,
                        R.drawable.round
                    )

                   dateRecyclerView.setVisibility(
                        View.GONE
                    )
                    itemLinear.setVisibility(
                        View.GONE
                    )
                    noItemTxt.setVisibility(
                        View.VISIBLE
                    )

                }else if (response.body()!!.status == 116) {
                    if (apiCall != 4) {
                        apiCall = apiCall + 1
                        AccessTokenClass.getAccessToken(nContext)

                    } else {

                        showSuccessAlertnew(
                            nContext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )

                    }
                } else if (response.body()!!.status == 132) {
                        Log.e("status","132")
                        dateRecyclerView.visibility=View.GONE
                        itemLinear.visibility=View.GONE
                        noItemTxt.visibility=View.GONE
                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
                    }
                }


        })
    }


    fun showInsufficientBal(
        activity: Context?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
            val intent = Intent(
                nContext,
                CanteenPaymentActivity::class.java
            )
            dialog.dismiss()
            finish()
            startActivity(intent)
            /*if (PreferenceManager.getUserId(com.mobatia.naisapp.activities.canteen_new.BasketListActivity.mContext)
                    .equalsIgnoreCase("")
            ) {
                val intent = Intent(
                    com.mobatia.naisapp.activities.canteen_new.BasketListActivity.mContext,
                    CanteenFirstStaffActivity::class.java
                )
                intent.putExtra("tab_type", "Canteen")
                intent.putExtra("email", "")
                intent.putExtra("isFrom", "Preorder")
                dialog.dismiss()
                finish()
                startActivity(intent)
            } else {
                val intent = Intent(
                    com.mobatia.naisapp.activities.canteen_new.BasketListActivity.mContext,
                    CanteenFirstActivity::class.java
                )
                intent.putExtra("tab_type", "Canteen")
                intent.putExtra("email", "")
                intent.putExtra("isFrom", "Preorder")
                dialog.dismiss()
                finish()
                startActivity(intent)
            }*/
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_Cancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
    fun showDialogAlertDismiss(
       context: Context,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener { dialog.dismiss() }
        //		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
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