package com.mobatia.bisad.activity.canteen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.adapter.MyorderDatesAdapter
import com.mobatia.bisad.activity.canteen.model.AllergyContentModel
import com.mobatia.bisad.activity.canteen.model.myorders.PreOrdersListModel
import com.mobatia.bisad.activity.canteen.model.myorders.PreOrdersModel
import com.mobatia.bisad.activity.canteen.model.myorders.Preorderitems_list
import com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryApiModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyorderActivity:AppCompatActivity() {
    lateinit var nContext: Context
    lateinit var tab_type: String
    lateinit var relativeHeader: RelativeLayout
    //var headermanager: HeaderManager? = null
    lateinit var back: ImageView
    lateinit var btn_history:android.widget.ImageView
    lateinit var home:android.widget.ImageView
    lateinit var extras: Bundle
    var ordered_user_type = ""
    var student_id = ""
    var parent_id = ""
    var staff_id = ""
    var totalAmount = ""
    var studentID:String=""
    var totalItems = 0
    lateinit var mMyOrderArrayList: java.util.ArrayList<PreOrdersListModel>
    lateinit var mMyOrderDetailArrayList: java.util.ArrayList<Preorderitems_list>
    lateinit var dateRecyclerView: RecyclerView
    lateinit var noItemTxt: ImageView
    lateinit var bottomLinear: LinearLayout
    lateinit var amountTxt: TextView
    lateinit var itemTxt: TextView
    lateinit var itemLinear: LinearLayout
    lateinit var homeBannerUrlImageArray: ArrayList<String>
    var walletAmountString = "0"
    var WalletAmount = 0
    lateinit var logoClickImg:ImageView
    var id:String=""
    lateinit var title:TextView
    lateinit var basket:ImageView
    lateinit var progressDialogAdd:ProgressBarDialog
    lateinit var allergycontentlist:ArrayList<AllergyContentModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_myorder)

        initfn()
        progressDialogAdd.show()
        getMyOrderDetails()
    }
    private fun initfn(){
        nContext=this
        studentID=intent.getStringExtra("StudentId").toString()
        logoClickImg=findViewById(R.id.logoclick)
        back=findViewById(R.id.back)
        id = PreferenceData().getStudentID(nContext).toString()
        dateRecyclerView =
            findViewById(R.id.dateRecyclerView)
        dateRecyclerView.layoutManager=LinearLayoutManager(nContext)
        noItemTxt =
            findViewById(R.id.noItemTxt)
        bottomLinear =
            findViewById(R.id.bottomLinear)
        amountTxt = findViewById(R.id.amountTxt)
        progressDialogAdd = ProgressBarDialog(nContext)
        itemTxt =
            findViewById(R.id.itemTxt)
        itemLinear =
            findViewById(R.id.itemLinear)
        title=findViewById(R.id.textViewtitle)
        title.text = "My Orders"
        mMyOrderArrayList= ArrayList()
        basket=findViewById(R.id.basket)
        back.setOnClickListener {
            finish()
        }
        logoClickImg.setOnClickListener {
            val intent = Intent(nContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        basket.setOnClickListener {
            val i = Intent(nContext, Myorderbasket_Activity::class.java)
            nContext.startActivity(i)
        }

    }
    private fun getMyOrderDetails(){

        //progressDialogAdd.visibility=View.VISIBLE
        progressDialogAdd.show()
        val token = PreferenceData().getaccesstoken(nContext)
        var model= OrderHistoryApiModel(studentID,"0","100")
        val call: Call<PreOrdersModel> = ApiClient.getClient.canteen_myorder_history(model,"Bearer "+token)
        call.enqueue(object : Callback<PreOrdersModel> {
            override fun onFailure(call: Call<PreOrdersModel>, t: Throwable) {
                CommonFunctions.faliurepopup(nContext)
                progressDialogAdd.hide()
            }
            override fun onResponse(call: Call<PreOrdersModel>, response: Response<PreOrdersModel>) {
                val responsedata = response.body()
                //progressDialogAdd.visibility=View.GONE
                progressDialogAdd.hide()

                if (responsedata!!.status==100) {
                    dateRecyclerView.visibility=View.VISIBLE
                    noItemTxt.visibility=View.GONE


                    dateRecyclerView.layoutManager = LinearLayoutManager(nContext)

                    dateRecyclerView.adapter = MyorderDatesAdapter(response.body()!!.responseArray.data,
                        nContext,studentID,dateRecyclerView,
                        bottomLinear,itemLinear,noItemTxt,progressDialogAdd,response.body()!!.responseArray.whole_total,amountTxt)
                    bottomLinear.visibility= View.VISIBLE
                    itemLinear.visibility= View.VISIBLE
                    amountTxt.text=response.body()!!.responseArray.whole_total.toString()+" AED"

                }
                else if (response.body()!!.status == 116)
                {
                    bottomLinear.visibility= View.GONE
                    itemLinear.visibility= View.GONE

                }
                else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                        bottomLinear.visibility= View.GONE
                        itemLinear.visibility= View.GONE
                    }
                    else {
                        if (response.body()!!.status == 132) {
                            bottomLinear.visibility= View.GONE
                            itemLinear.visibility= View.GONE
                            dateRecyclerView.visibility=View.GONE
                            noItemTxt.visibility=View.VISIBLE
                            /* recyclerview_item.visibility=View.GONE
                             cart_empty.visibility=View.VISIBLE*/
                            //validation check error
                        } else {
                            //check status code checks
                            bottomLinear.visibility= View.GONE
                            itemLinear.visibility= View.GONE
                            InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
                        }
                }
            }

        }

        //dateRecyclerView.adapter = MyorderDatesAdapter(mMyOrderArrayList, nContext)

    })
}
}