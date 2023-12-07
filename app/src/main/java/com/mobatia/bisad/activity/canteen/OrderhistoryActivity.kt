package com.mobatia.bisad.activity.canteen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.ProgressBarDialog
import com.mobatia.bisad.activity.canteen.adapter.PreorderDatesAdapter
import com.mobatia.bisad.activity.canteen.model.add_orders.CatItemsListModel
import com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryApiModel
import com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryDataModel
import com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryResponseModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderhistoryActivity : AppCompatActivity() {
    lateinit var nContext: Context
    private lateinit var logoClickImg: ImageView
    lateinit var recyclerview: RecyclerView
    lateinit var back: ImageView
    lateinit var progress: ProgressBarDialog
    private var id: String? = null
    lateinit var title: TextView
    lateinit var basket: ImageView
    lateinit var noItem: ImageView
    lateinit var preorderhis_list: ArrayList<OrderHistoryDataModel>
    lateinit var preorderhis_itemlist: ArrayList<String>
    var studentID:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_orderhistory)

        initfn()
        callOderHistory()

    }

    private fun initfn() {
        nContext = this
        studentID=intent.getStringExtra("StudentId").toString()

        logoClickImg=findViewById(R.id.logoclick)
        back = findViewById(R.id.back)
        progress=ProgressBarDialog(nContext)
        title = findViewById(R.id.textViewtitle)
        id = PreferenceData().getStudentID(nContext).toString()
        basket = findViewById(R.id.basket)
        noItem = findViewById(R.id.noItemTxt)
        preorderhis_list = ArrayList()
        preorderhis_itemlist = ArrayList()
        recyclerview = findViewById(R.id.date_rec)
        title.text = "Order History"
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
        recyclerview.layoutManager = LinearLayoutManager(nContext)

    }

    fun callOderHistory()
    {
        progress.show()
        val token = PreferenceData().getaccesstoken(nContext)
        var model= OrderHistoryApiModel(studentID,"0","100")
        val call: Call<OrderHistoryResponseModel> = ApiClient.getClient.canteen_order_history(model,"Bearer "+token)
        call.enqueue(object : Callback<OrderHistoryResponseModel> {
            override fun onFailure(call: Call<OrderHistoryResponseModel>, t: Throwable) {
                CommonFunctions.faliurepopup(nContext)
                progress.hide()
            }
            override fun onResponse(call: Call<OrderHistoryResponseModel>, response: Response<OrderHistoryResponseModel>) {
                val responsedata = response.body()
                progress.hide()

                if (responsedata!!.status==100) {

                    preorderhis_list=response.body()!!.responseArray.data
                    recyclerview.adapter = PreorderDatesAdapter(preorderhis_list, nContext)

                }
                else if (response.body()!!.status == 116)
                {


                }
                else {
                    if (response.body()!!.status == 132) {
                        recyclerview.visibility= View.GONE
                        noItem.visibility=View.VISIBLE
                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, nContext)
                    }
                }
            }

        })



    }
}