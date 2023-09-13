package com.mobatia.bisad.activity.canteen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.adapter.Canteeninfo_adapter
import com.mobatia.bisad.activity.canteen.model.information.InfoCanteenModel
import com.mobatia.bisad.activity.canteen.model.information.InfoListModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.settings.re_enrollment.model.EnrollmentStatusModel
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationActivity : AppCompatActivity() {
    lateinit var mContext: Context
    private lateinit var logoClickImg: ImageView
    lateinit var recyclerview: RecyclerView
    lateinit var back: ImageView
    lateinit var informationlist: ArrayList<InfoListModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_information)

        initfn()
        callCanteenInformation()
    }

    private fun initfn() {
        mContext = this
        logoClickImg=findViewById(R.id.logoclick)
        informationlist = ArrayList()
        recyclerview = findViewById(R.id.canteen_info_list)
        back = findViewById(R.id.back)
        val text1 = findViewById<TextView>(R.id.textViewtitle)
        text1.text = "Information"
        back.setOnClickListener {
            finish()
        }
        logoClickImg.setOnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }
    private fun callCanteenInformation(){


        val token = PreferenceData().getaccesstoken(mContext)

        val call: Call<InfoCanteenModel> = ApiClient.getClient.getCanteenInformation("Bearer "+token)
        call.enqueue(object : Callback<InfoCanteenModel> {
            override fun onFailure(call: Call<InfoCanteenModel>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
            }
            override fun onResponse(call: Call<InfoCanteenModel>, response: Response<InfoCanteenModel>) {
                val responsedata = response.body()
                Log.e("Response", responsedata.toString())
                if (responsedata!!.status==100) {

                    if(response.body()!!.responseArray.information.size>0)
                    {
                        recyclerview.layoutManager = LinearLayoutManager(mContext)
                        recyclerview.adapter = Canteeninfo_adapter(response.body()!!.responseArray.information, mContext)
                    }



                }else if (response.body()!!.status == 116) {

                } else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                    }
                }
            }

        })


    }
}