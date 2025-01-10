package com.mobatia.bisad.activity.canteen

import android.app.Activity
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
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.adapter.DateAdapter
import com.mobatia.bisad.activity.canteen.adapter.ItemCategoriesAdapter
import com.mobatia.bisad.activity.canteen.adapter.PreorderItemsAdapter
import com.mobatia.bisad.activity.canteen.model.AllergyContentModel
import com.mobatia.bisad.activity.canteen.model.DateModel
import com.mobatia.bisad.activity.canteen.model.add_orders.*
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartApiModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartResModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CartItemsListModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Addorder_Activity : AppCompatActivity() {
    lateinit var nContext: Context
    private lateinit var logoClickImg: ImageView
    lateinit var recyclerview_category: RecyclerView
    lateinit var recyclerview_date: RecyclerView
    lateinit var back: ImageView
    lateinit var cart_list: ArrayList<CanteenCartResModel>
    lateinit var cart_items_list: ArrayList<CartItemsListModel>
    private var id: String? = null
    lateinit var date_title: TextView
    lateinit var date_list: ArrayList<DateModel>
    lateinit var recyclerview_item: RecyclerView

    //lateinit var selected:ImageView

    lateinit var title: TextView
    lateinit var cart_empty: ImageView
    //lateinit var progress:RelativeLayout
    lateinit var progressDialogM:ProgressBarDialog
    lateinit var category_list: ArrayList<CategoryListModel>
    lateinit var item_list:ArrayList<CatItemsListModel>
    lateinit var bottomview: LinearLayout
    lateinit var basketbtn: LinearLayout

    var cat_selected: String = ""
    var date_selected: String = ""
    var def_cat_id: String = ""
    var date_string: String = ""
    var apiCall: Int = 0
    var cartTotalAmount:Int=0
    var cartTotalItem:Int=0

    lateinit var total_items: TextView
    lateinit var total_price: TextView
    var firstVisit: Boolean? = null
    lateinit var allergycontentlist: java.util.ArrayList<AllergyContentModel>
    lateinit var activity: Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_addorder)
        firstVisit = true
        initfn()
        setdate()

        item_categories()
        getcanteen_cart()

    }

    private fun initfn() {
        nContext = this
        activity=this
        back = findViewById(R.id.back)
        logoClickImg=findViewById(R.id.logoclick)
        title = findViewById(R.id.titleTextView)
        id = PreferenceData().getStudentID(nContext).toString()
        date_title = findViewById(R.id.date_title)
        //progress=findViewById(R.id.progressDialog)
        progressDialogM= ProgressBarDialog(nContext)
        title.text = "Pre-Order"
        cart_empty = findViewById(R.id.item_empty)

        category_list = ArrayList()
        cart_list = ArrayList()
        cart_items_list = ArrayList()
        date_list = intent.getSerializableExtra("date_list") as ArrayList<DateModel>
        //date_list=PreferenceManager().getdate_list(nContext)
        var year = date_list[0].year
        var strCurrentDate = ""
        var format = SimpleDateFormat("MMM", Locale.ENGLISH)
        var newDate: Date? = null
        try {
            newDate = format.parse(date_list[0].month)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        format = SimpleDateFormat("MM", Locale.ENGLISH)
        strCurrentDate = format.format(newDate)
        var month=strCurrentDate
        //var month = CommonMethods.dateParsingTommm(date_list[0].month)
        var date = date_list[0].numberDate
        date_string = date_list[0].numberDate
        date_selected = date_string.toString()
        total_items = findViewById(R.id.itemCount)
        total_price = findViewById(R.id.totalAmount)
        bottomview = findViewById(R.id.cartLinear)
        basketbtn = findViewById(R.id.cartBtnLinear)

        recyclerview_category = findViewById(R.id.categoryRecyclerView)
        recyclerview_date = findViewById(R.id.dateRecyclerView)
        recyclerview_item = findViewById(R.id.itemRecyclerView)
logoClickImg.setOnClickListener {
    val intent = Intent(nContext, HomeActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
}


        back.setOnClickListener {

            finish()
        }
        basketbtn.setOnClickListener {
            val intent = Intent(nContext, Myorderbasket_Activity::class.java)
            intent.putExtra("date", date_selected)
            nContext.startActivity(intent)
        }

        bottomview.setOnClickListener(View.OnClickListener {
            val intent = Intent(nContext, Myorderbasket_Activity::class.java)
            intent.putExtra("date", date_selected)
            nContext.startActivity(intent)
        })

    }
    private fun setdate(){
        var one_date=date_list[0].day+","+date_list[0].date+" "+date_list[0].month+" "+date_list[0].year
        if (date_list.size==1){
            date_title.visibility = View.VISIBLE
            date_title.text = one_date
        }
        else {
            var first_date=date_list[0].numberDate
            date_title.visibility = View.GONE
            for (i in date_list.indices) {
                date_list.get(i).isDateSelected = date_list.get(i).numberDate.equals(first_date)
            }
            recyclerview_date.visibility = View.VISIBLE
            var llm2 = (LinearLayoutManager(nContext))
            llm2.orientation = LinearLayoutManager.HORIZONTAL
            recyclerview_date.layoutManager = llm2
            recyclerview_date.adapter = DateAdapter(date_list, nContext)

            recyclerview_date.addOnItemClickListener(object : OnItemClickListener {
                override fun onItemClicked(position: Int, view: View) {

                    date_list.get(0).isDateSelected=false
                    date_selected=date_list[position].numberDate
                    var foundPosition = -1
                    var isFound: Boolean = false


                    for (i in 0..date_list.size-1) {
                        if (date_list.get(i).isDateSelected) {
                            foundPosition = i
                            isFound = true
                            date_selected=date_list.get(position).numberDate
                            items_onclick()
                        }
                    }
                    if (isFound) {
                        if (position == foundPosition) {
                            date_list.get(foundPosition).isDateSelected = true
                            recyclerview_date.adapter = DateAdapter(date_list, nContext)

                        } else {
                            date_list.get(foundPosition).isDateSelected = false
                            date_list.get(position).isDateSelected = true
                            date_selected=date_list.get(position).numberDate
                            items_onclick()

                            recyclerview_date.adapter = DateAdapter(date_list, nContext)
                        }
                    } else {
                        date_list.get(position).isDateSelected = true
                        date_selected=date_list.get(position).numberDate
                        items_onclick()

                        recyclerview_date.adapter = DateAdapter(date_list, nContext)
                    }
                    //items_onclick()
                }
            })
        }
    }


    private fun item_categories(){
        item_list= ArrayList()
        category_list= ArrayList()
        recyclerview_item.visibility=View.GONE
        cart_empty.visibility=View.GONE
//progress.visibility=View.VISIBLE
        //progressDialogM.visibility=View.VISIBLE
        progressDialogM.show()
        val token = PreferenceData().getaccesstoken(nContext)
        val call: Call<CatListModel> = ApiClient(nContext).getClient.get_canteen_categories("Bearer "+token)
        call.enqueue(object : Callback<CatListModel> {
            override fun onFailure(call: Call<CatListModel>, t: Throwable) {
                CommonFunctions.faliurepopup(nContext)
            }
            override fun onResponse(call: Call<CatListModel>, response: Response<CatListModel>) {
                //progressDialogM.visibility=View.GONE
                progressDialogM.hide()
                recyclerview_item.visibility=View.VISIBLE
                val responsedata = response.body()
                if (responsedata!!.status==100) {
                    category_list= ArrayList()


                    category_list.addAll(response.body()!!.responseArray.data)
                    def_cat_id=category_list[0].id
                    cat_selected=def_cat_id
                    for (i in category_list.indices) {
                        category_list.get(i).isItemSelected = category_list.get(i).id.equals(def_cat_id)
                    }
                    var llm = (LinearLayoutManager(nContext))
                    llm.orientation = LinearLayoutManager.HORIZONTAL
                    recyclerview_category.layoutManager = llm
                    recyclerview_category.adapter = ItemCategoriesAdapter(category_list, nContext)
                    recyclerview_item.visibility=View.GONE
//progress.visibility=View.VISIBLE
                    items()


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


        recyclerview_category.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                //cat_selected= category_list.get(position).id

                var foundPosition = -1
                var isFound: Boolean = false
                getcanteen_cart()
                for (i in 0..category_list.size - 1) {
                    if (category_list.get(i).isItemSelected) {
                        foundPosition = i
                        isFound = true
                        cat_selected= category_list.get(position).id
                        items_onclick()
                    }
                }
                if (isFound) {
                    if (position == foundPosition) {
                        category_list.get(foundPosition).isItemSelected = true
                        recyclerview_category.adapter =
                            ItemCategoriesAdapter(category_list, nContext)
                    } else {
                        category_list.get(foundPosition).isItemSelected = false
                        category_list.get(position).isItemSelected = true
                        cat_selected= category_list.get(position).id

                        recyclerview_category.adapter =
                            ItemCategoriesAdapter(category_list, nContext)
                        items_onclick()
                    }
                } else {
                    category_list.get(position).isItemSelected = true
                    cat_selected= category_list.get(position).id

                    recyclerview_category.adapter = ItemCategoriesAdapter(category_list, nContext)
                    items_onclick()
                }
               // items_onclick()
                recyclerview_category.scrollToPosition(position)
            }
        })

    }
    private fun items(){
        item_list= ArrayList()
        recyclerview_item.visibility=View.GONE
//progress.visibility=View.VISIBLE
        //progressDialogM.visibility=View.VISIBLE
        progressDialogM.show()
        cart_empty.visibility=View.GONE
        val token = PreferenceData().getaccesstoken(nContext)
        var canteenItems= CanteenItemsApiModel(PreferenceData().getStudentID(nContext).toString(),def_cat_id,
            date_selected,"0","50")
        val call: Call<ItemsListModel> = ApiClient(nContext).getClient.get_canteen_items(canteenItems,"Bearer "+token)
        call.enqueue(object : Callback<ItemsListModel> {
            override fun onFailure(call: Call<ItemsListModel>, t: Throwable) {
               // progressDialogM.visibility=View.GONE

                progressDialogM.hide()
                recyclerview_item.visibility=View.VISIBLE
            }
            override fun onResponse(call: Call<ItemsListModel>, response: Response<ItemsListModel>) {
                //progress.visibility=View.GONE
                //progressDialogM.visibility=View.GONE
                progressDialogM.hide()
                recyclerview_item.visibility=View.VISIBLE

                val responsedata = response.body()
                if (responsedata!!.status==100) {

                    //bottomview.visibility=View.VISIBLE
                    cart_empty.visibility=View.GONE
                    item_list= ArrayList()
                    item_list.addAll(response.body()!!.responseArray.data)


                    for (i in item_list.indices){
                        var jId=item_list[i].id


                    var isFound:Boolean=false
                    var cartDatePos = 0
                    var cartItemPos = 0
                    if (cart_list.size > 0) {
                        for (n in cart_list.indices) {
                            if (cart_list.get(n).delivery_date.equals(date_selected))
                            {

                            for (m in 0 until cart_list.get(n).items.size) {



                                if (jId.equals(cart_list.get(n).items.get(m).item_id.toString())) {
                                    isFound = true
                                    cartDatePos = n
                                    cartItemPos = m
                                }
                            }
                        }
                        }
                    }else{
                    }
                    if (isFound) {

                        item_list[i].quantityCart=cart_list.get(cartDatePos).items.get(cartItemPos).quantity
                        item_list[i].cartId = cart_list.get(cartDatePos).items.get(cartItemPos).id.toString()
                        item_list[i].isItemCart=true

                    } else
                    {

                        item_list[i].quantityCart=0
                        item_list[i].cartId =""
                        item_list[i].isItemCart=false


                    }


                        allergycontentlist= java.util.ArrayList()
                        allergycontentlist.addAll(item_list.get(i).allergy_contents)
                        if(allergycontentlist.size>0)
                        {
                            item_list.get(i).isAllergic=true
                        } else {
                            item_list.get(i).isAllergic=false
                        }
                }

                    recyclerview_item.layoutManager=LinearLayoutManager(nContext)
                    var itemAdapter=PreorderItemsAdapter(item_list,nContext,date_selected,cart_list,cartTotalAmount,
                        total_items,total_price,bottomview,cart_empty,
                        progressDialogM,allergycontentlist)
                    recyclerview_item.adapter=itemAdapter

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

                        //validation check error
                        //bottomview.visibility=View.GONE
                        recyclerview_item.visibility=View.GONE
                        cart_empty.visibility=View.VISIBLE


                    } else {

                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
                    }
                }
            }

        })

    }
    private fun items_onclick(){
        item_list= ArrayList()
        recyclerview_item.visibility=View.GONE
        cart_empty.visibility=View.GONE
//progress.visibility=View.VISIBLE
        //progressDialogM.visibility=View.VISIBLE
        progressDialogM.show()
        val token = PreferenceData().getaccesstoken(nContext)
        var canteenItems= CanteenItemsApiModel(PreferenceData().getStudentID(nContext).toString(),cat_selected,
            date_selected,"0","200")
        val call: Call<ItemsListModel> = ApiClient(nContext).getClient.get_canteen_items(canteenItems,"Bearer "+token)
        call.enqueue(object : Callback<ItemsListModel> {
            override fun onFailure(call: Call<ItemsListModel>, t: Throwable) {

                //progressDialogM.visibility=View.GONE
                progressDialogM.hide()
                recyclerview_item.visibility=View.VISIBLE
            }
            override fun onResponse(call: Call<ItemsListModel>, response: Response<ItemsListModel>) {
//progress.visibility=View.GONE
                //progressDialogM.visibility=View.GONE
                progressDialogM.hide()
                cart_empty.visibility=View.GONE
                recyclerview_item.visibility=View.VISIBLE

                val responsedata = response.body()
                if (responsedata!!.status==100) {

                  //  bottomview.visibility=View.VISIBLE
                    cart_empty.visibility=View.GONE

                    item_list= ArrayList()
                    item_list.addAll(response.body()!!.responseArray.data)
                    for (i in item_list.indices){
                        var jId=item_list[i].id


                        var isFound:Boolean=false
                        var cartDatePos = 0
                        var cartItemPos = 0
                        if (cart_list.size > 0) {
                            for (n in cart_list.indices) {

                                if (cart_list.get(n).delivery_date.equals(date_selected))
                                {
                                for (m in 0 until cart_list.get(n).items.size) {



                                    if (jId.equals(cart_list.get(n).items.get(m).item_id.toString())) {
                                        isFound = true
                                        cartDatePos = n
                                        cartItemPos = m
                                    }
                                }
                            }
                            }
                        }else{
                        }
                        if (isFound) {

                            item_list[i].quantityCart=cart_list.get(cartDatePos).items.get(cartItemPos).quantity
                            item_list[i].cartId = cart_list.get(cartDatePos).items.get(cartItemPos).id.toString()
                            item_list[i].isItemCart=true

                        } else {
                            item_list[i].quantityCart=0
                            item_list[i].cartId =""
                            item_list[i].isItemCart=false


                        }

                        allergycontentlist= java.util.ArrayList()
                        allergycontentlist.addAll(item_list.get(i).allergy_contents)
                        if(allergycontentlist.size>0)
                        {
                            item_list.get(i).isAllergic=true
                        } else {
                            item_list.get(i).isAllergic=false
                        }
                    }
                    //progressDialog.visibility = View.GONE
                    recyclerview_item.layoutManager=LinearLayoutManager(nContext)
                    var itemAdapter=PreorderItemsAdapter(
                        item_list,
                        nContext,
                        date_selected,
                        cart_list,
                        cartTotalAmount,
                        total_items,
                        total_price,
                        bottomview,
                        cart_empty,
                        progressDialogM,
                        allergycontentlist
                    )
                    recyclerview_item.adapter=itemAdapter


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

                        //validation check error
                        //bottomview.visibility=View.GONE
                        recyclerview_item.visibility=View.GONE
                        cart_empty.visibility=View.VISIBLE


                    } else {

                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
                    }
                }
            }

        })
    }
    private fun getcanteen_cart(){
        //progress.visibility=View.VISIBLE
        //recyclerview_item.visibility=View.GONE
        cart_empty.visibility=View.GONE
        //progressDialogM.visibility=View.VISIBLE
        progressDialogM.show()
        cart_list= ArrayList()
        cartTotalAmount=0
        cartTotalItem=0
        val token = PreferenceData().getaccesstoken(nContext)
        var canteenCart= CanteenCartApiModel(PreferenceData().getStudentID(nContext).toString())
        val call: Call<CanteenCartModel> = ApiClient(nContext).getClient.get_canteen_cart(canteenCart,"Bearer "+token)
        call.enqueue(object : Callback<CanteenCartModel> {
            override fun onFailure(call: Call<CanteenCartModel>, t: Throwable) {

                //progressDialogM.visibility=View.GONE
                progressDialogM.show()
                recyclerview_item.visibility=View.VISIBLE
            }
            override fun onResponse(call: Call<CanteenCartModel>, response: Response<CanteenCartModel>) {
                //progress.visibility=View.GONE

                //recyclerview_item.visibility=View.VISIBLE
                //progressDialogM.visibility=View.GONE
                progressDialogM.hide()
                val responsedata = response.body()
                if (responsedata!!.status==100) {
                    bottomview.visibility=View.VISIBLE

                    cart_list=response!!.body()!!.responseArray.data
                    cartTotalAmount=0
                    for (i in cart_list.indices){

                        cartTotalAmount=cartTotalAmount + cart_list[i].total_amount
                    }
                    if (cartTotalAmount==0){
                        bottomview.visibility=View.GONE
                    }else{
                        bottomview.visibility=View.VISIBLE
                        cartTotalItem=0
                        for (i in cart_list.indices){

                            for (j in cart_list[i].items.indices){
                                cartTotalItem=cartTotalItem + cart_list[i].items[j].quantity
                            }
                        }
                        total_items.setText(cartTotalItem.toString() + "Items")
                        total_price.setText(cartTotalAmount.toString() + "AED")
                    }

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
                        bottomview.visibility=View.GONE
                       /* recyclerview_item.visibility=View.GONE
                        cart_empty.visibility=View.VISIBLE*/
                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
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

    override fun onResume() {
        item_categories()
        getcanteen_cart()
        if (!CommonFunctions.runMethod.equals("Dev")) {
            if (CommonFunctions.isDeveloperModeEnabled(nContext)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
        }
        super.onResume()
    }

    }
