package com.mobatia.bisad.activity.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.print.PrintJob
import android.view.View
import android.view.animation.RotateAnimation
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import okhttp3.MediaType
import java.io.File

class PaymentDetailActivityInstallNew:AppCompatActivity() {
    lateinit var context: Context
    lateinit var titletext: TextView
    lateinit var back: RelativeLayout
    lateinit var home_icon: ImageView
    lateinit var descriptionTitle: TextView
    lateinit var descriptionTxt: TextView
    lateinit var closingTxt: TextView
    lateinit var installmentmainTxt: TextView
    lateinit var mEmiRecycler: RecyclerView

    var adapterSize = 0
    var Position = 0
    lateinit var amount: String
    lateinit var categoryId: String
    lateinit var paymentWeb: WebView
    lateinit  var orderId: String
    lateinit var trip_id: String
    lateinit  var merchantid: String
    lateinit var payment_url: String
    lateinit var merchant_name: String
    lateinit var auth_id: String
    lateinit var postUrl: String
    lateinit var title: String
    lateinit var instalTitle: String
    lateinit var totalAmtInst: String
    lateinit var titlePrint: String

    lateinit  var addToCalendar: LinearLayout
    lateinit var mainLinear: LinearLayout
    lateinit var printLinear: LinearLayout
    lateinit var emailLinear: LinearLayout
    lateinit var printLinearClick: LinearLayout
    lateinit var downloadLinear: LinearLayout
    lateinit var shareLinear: LinearLayout
    lateinit var orderIdPrint: String
    lateinit var text_content: TextView
    lateinit var text_dialog: TextView

    var tab_type = ""
    var stud_id = ""
    var billing_code = ""
    var tripDetails = ""
    var paidDate = ""
    var installFull = ""
//    var adapter: PaymentDetailAdapterNew? = null
    var postBody = ""
    var isShowPaymentView = false
    var sessionId: String? = null
    var email: String? = null
    var sessionVersion: String? = null
    var anim: RotateAnimation? = null
    var descriptionWeb: String? = null
    private var mProgressRelLayout: RelativeLayout? = null
    var fullHtml: String? = null
    var installFullHtml: String? = null
   // val JSON_MEDIA_TYPE: MediaType = MediaType.parse("application/octet-stream")
    var merchantpassword = "16496a68b8ac0fb9b6fde61274272457"
    var isFullPayment: Boolean? = null
    var payment_date = ""
    var installment_amount = ""
    var installment_id = ""
    var category_id = ""
    var payment_option = ""
    var installment = ""
    var status = ""
    var amountFull = ""
    var invoiceNote = ""
    var paidBy = ""
    var backStatus = "0"
    var isInstallPage = false
    var isFullPayPage = false
    lateinit var print: ImageView
    lateinit var download:ImageView
    lateinit var share:ImageView
    lateinit var printJob: PrintJob
    lateinit var completed_date: String
    var enablePosition = 0
    var paidStatusInstall = "0"
    var description = ""
    var isInvoiceInstall = false
    lateinit var pathFile: File
    lateinit var pdfUri: Uri
    lateinit var scroll: ScrollView
    var isDownload = false
    var isShareInstall = false
    var isDoenloadInstall = false
    var isPartiallyPaid = false
    lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymentlistinstallnew)
        init()
    }
    private fun init(){
        context=this
        activity=this
        titletext=findViewById(R.id.heading)
        back=findViewById(R.id.backRelative)
        home_icon=findViewById(R.id.logoClickImgView)
        mProgressRelLayout = findViewById(R.id.progressDialog)
        download = findViewById(R.id.download)
        share = findViewById(R.id.share)
        print = findViewById(R.id.print)
        installmentmainTxt = findViewById(R.id.installmentmainTxt)
        descriptionTitle = findViewById(R.id.descriptionTitle)
        printLinear = findViewById(R.id.printLinear)
        printLinearClick = findViewById(R.id.printLinearClick)
        emailLinear = findViewById(R.id.emailLinear)
        downloadLinear = findViewById(R.id.downloadLinear)
        shareLinear = findViewById(R.id.shareLinear)
        descriptionTxt = findViewById(R.id.description)
        mEmiRecycler = findViewById(R.id.mEmiRecycler)
        closingTxt = findViewById(R.id.closingTxt)
        paymentWeb = findViewById(R.id.paymentWeb)
        scroll = findViewById(R.id.scroll)
        mainLinear = findViewById(R.id.mainLinear)
        mainLinear.visibility = View.VISIBLE
        paymentWeb.visibility = View.GONE
        //        headermanager.setButtonRightSelector(R.drawable.add_calendar,
//                R.drawable.add_calendar);
        addToCalendar.visibility = View.GONE
        adapterSize = intent.getIntExtra("adapterSize",0)
        //AppController.Position = extras.getInt("pos")
        //orderId = extras.getString("orderId");
        //orderId = extras.getString("orderId");
        merchantid = intent.getStringExtra("merchant_id").toString()
        email = intent.getStringExtra("email").toString()
        merchant_name = intent.getStringExtra("merchant_name").toString()
        auth_id = intent.getStringExtra("auth_id").toString()
        postUrl = intent.getStringExtra("session_url").toString()
        payment_url = intent.getStringExtra("payment_url").toString()
        stud_id = intent.getStringExtra("stud_id").toString()
        categoryId = intent.getStringExtra("categoryId").toString()
        status = intent.getStringExtra("status").toString()
        title = "test title"
        tab_type = intent.getStringExtra("tab_type").toString()

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


        } else {
            InternetCheckClass.showSuccessInternetAlert(context)
        }
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