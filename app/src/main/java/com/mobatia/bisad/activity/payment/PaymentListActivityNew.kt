package com.mobatia.bisad.activity.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.model.payment_history.PayListModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.payment.adapter.PaymentListAdapter
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener

class PaymentListActivityNew:AppCompatActivity() {
    lateinit var context: Context
    lateinit var titletext: TextView
    lateinit var back: RelativeLayout
    lateinit var home_icon: ImageView

    lateinit var extras: Bundle
    lateinit var tab_type: String
    lateinit var categoryId: String
    lateinit var email: String
    lateinit var stud_id: String
    lateinit var merchant_id: String
    lateinit var auth_id: String
    lateinit var session_url: String
    lateinit var payment_url: String
    lateinit var merchant_name: String
    var adapterSize = 0
    lateinit var mRecycleView: RecyclerView
    lateinit var payList:ArrayList<PayListModel>
    lateinit var activity: Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymentlistnew)
        init()
    }
    private fun init(){
        context=this
        activity=this
        titletext=findViewById(R.id.heading)
        back=findViewById(R.id.backRelative)
        home_icon=findViewById(R.id.logoClickImgView)
        mRecycleView = findViewById(R.id.recycler_view_list)
        payList= ArrayList()

        back.setOnClickListener {
            finish()
        }
        home_icon.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        var internetCheck = InternetCheckClass.isInternetAvailable(context)
        if (internetCheck) {
           getreportlist()

        } else {
            InternetCheckClass.showSuccessInternetAlert(context)
        }
    }
    private fun getreportlist(){
        mRecycleView.layoutManager=LinearLayoutManager(context)
        mRecycleView.adapter= PaymentListAdapter(context,payList)

        mRecycleView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val intent = Intent(context, PaymentDetailActivityInstallNew::class.java)
               /*AppController.Position =
                    position
                AppController.stud_id = stud_id
                AppController.categoryId = categoryId*/
                if (payList[position].installment
                        .equals("1")
                ) {
                    if (payList[position].installmentArrayList
                            .size == 1
                    ) {
                        adapterSize = 1
                    } else {
                        adapterSize =
                            payList[position].installmentArrayList
                                .size + 1
                    }
                } else {
                    adapterSize = 1
                }
                intent.putExtra("key", payList)
                intent.putExtra("adapterSize", adapterSize)
var pos=payList[position].toString()
                intent.putExtra("pos", pos)
                //  intent.putExtra("orderId", "NASDUBAI" + mListViewArray.get(position).getId() + "S" + stud_id);
                //intent.putExtra("orderId", mListViewArray.get(position).getOrder_id());
                //  intent.putExtra("orderId", "NASDUBAI" + mListViewArray.get(position).getId() + "S" + stud_id);
                //intent.putExtra("orderId", mListViewArray.get(position).getOrder_id());
                intent.putExtra("merchant_id", merchant_id)
                intent.putExtra("auth_id", auth_id)
                intent.putExtra("session_url", session_url)
                intent.putExtra("payment_url", payment_url)
                intent.putExtra("merchant_name", merchant_name)
                intent.putExtra("tab_type", "Payment Details")
                intent.putExtra("stud_id", stud_id)
                intent.putExtra("email", email)
                intent.putExtra("status",payList[position].status)

                intent.putExtra("categoryId", categoryId)
                startActivity(intent)

            }
            })
            }
    override fun onResume() {
        super.onResume()
        if (!CommonFunctions.runMethod.equals("Dev")) {
            if (CommonFunctions.isDeveloperModeEnabled(context)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
        }
    }
    }
