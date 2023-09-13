package com.mobatia.bisad.activity.canteen

import android.content.Context
import android.os.Bundle
import android.view.animation.RotateAnimation
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.model.payment_history.PayListModel

class CanteenActviity:AppCompatActivity() {
    var orderId: String? = null
    var tab_type = ""
    var mContext: Context? = null
    var merchantpassword = "16496a68b8ac0fb9b6fde61274272457"
    var postUrl: String? = null
    var postBody = ""
    var fullHtml: String? = null
    var relativeHeader: RelativeLayout? = null
    lateinit var back: ImageView
    lateinit var home: ImageView
    var sessionId = ""
    var sessionVersion = ""
    var paymentWeb: WebView? = null
    private val mProgressRelLayout: RelativeLayout? = null
    lateinit var  mListViewArray: ArrayList<PayListModel>
    var anim: RotateAnimation? = null
    //val JSON_MEDIA_TYPE: MediaType = parse.parse("application/octet-stream")
    var tripDetails = ""
    var stud_id = ""
    var amount = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_pay_activity)

        initfn()

    }
    private fun initfn(){
        paymentWeb = findViewById(R.id.paymentWeb)
        back = findViewById(R.id.back)
        home = findViewById(R.id.logoclick)


    }
}