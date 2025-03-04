package com.mobatia.bisad.activity.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.payment.model.payment_gateway.PaymentGatewayApiModel
import com.mobatia.bisad.activity.payment.model.payment_gateway.PaymentGatewayModel
import com.mobatia.bisad.activity.payment.model.payment_submit.PaymentSubmitApiModel
import com.mobatia.bisad.activity.payment.model.payment_submit.PaymentSubmitModel
import com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenApiModel
import com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import payment.sdk.android.PaymentClient
import payment.sdk.android.cardpayment.CardPaymentData
import payment.sdk.android.cardpayment.CardPaymentRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PaymentDetailsActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var titletext: TextView
    lateinit var back: RelativeLayout
    lateinit var home_icon: ImageView

    lateinit var addToCalendar: LinearLayout
    lateinit var totalLinear: LinearLayout
    lateinit var printLinear:LinearLayout
    lateinit var printLinearClick:LinearLayout
    lateinit var mainLinear:LinearLayout
    lateinit var downloadLinear:LinearLayout
    lateinit var shareLinear:LinearLayout
    lateinit var totalAmount: TextView
    lateinit var descriptionTitle:TextView
    lateinit var description:TextView
    lateinit var duedate:TextView
    lateinit var payTotalButton: Button
    lateinit var paymentWeb: WebView
    lateinit var bannerImageViewPager: ImageView
    lateinit var paidImg:ImageView
    lateinit var mProgressRelLayout:RelativeLayout
    var fullHtml: String? = null
    var handler = Handler()
    lateinit var printJob: PrintJob
    var runnable: Runnable? = null
    var status:String=""
    var id:String=""
    var student_name:String=""
    var account_code:String=""
    var pupil_code:String=""
    var academic_year:String=""
    var invoice_ref:String=""
    var invoice_description:String=""
    var current_amount:String=""
    var vat_percentage:String=""
    var vat_amount:String=""
    var total_amount:String=""
    var due:String=""
    var due_date:String=""
    var paid_date:String?=""
    var payment_type:String=""
    var paid_by:String?=""
    var title=""
    var thankyou_note=""


    var payment_type_print: String=""
    var order_id:String=""
    var backStatus = "0"
    var orderRef=""
    lateinit var anim: RotateAnimation
    private val STORAGE_PERMISSION_CODE: Int = 1000
    var pathFile: File? = null
    var pdfUri: Uri? = null

    lateinit var activity: Activity
    var pos = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymentdetails)
        init()
    }

    private fun init(){
        context=this
        activity=this
        titletext=findViewById(R.id.heading)
        titletext.setText("Payment Details")
        back=findViewById(R.id.backRelative)
        home_icon=findViewById(R.id.logoClickImgView)
        title=intent.getStringExtra("title").toString()
        status=intent.getStringExtra("status").toString()
        id=intent.getStringExtra("id").toString()
        student_name=intent.getStringExtra("student_name").toString()
        account_code=intent.getStringExtra("account_code").toString()
        pupil_code=intent.getStringExtra("pupil_code").toString()
        academic_year=intent.getStringExtra("academic_year").toString()
        invoice_ref=intent.getStringExtra("invoice_ref").toString()
        invoice_description=intent.getStringExtra("invoice_description").toString()
        current_amount=intent.getStringExtra("current_amount").toString()
        vat_percentage=intent.getStringExtra("vat_percentage").toString()
        vat_amount=intent.getStringExtra("vat_amount").toString()
        total_amount=intent.getStringExtra("total_amount").toString()
        due=intent.getStringExtra("due_date").toString()
        paid_date=intent.getStringExtra("paid_date").toString()
        payment_type=intent.getStringExtra("payment_type").toString()
        paid_by=intent.getStringExtra("paid_by").toString()
        thankyou_note=intent.getStringExtra("thankyou_note").toString()
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = due
        val date: Date = inputFormat.parse(inputDateStr)
        due_date = outputFormat.format(date)
        back.setOnClickListener {
            finish()
        }
        home_icon.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        totalLinear = findViewById(R.id.totalLinear)
        totalAmount = findViewById(R.id.totalAmount)
        totalAmount.setText(total_amount)
        printLinear = findViewById(R.id.printLinear)
        printLinearClick = findViewById(R.id.printLinearClick)
        paymentWeb = findViewById(R.id.paymentWeb)
        mainLinear = findViewById(R.id.mainLinear)
        descriptionTitle = findViewById(R.id.descriptionTitle)
        descriptionTitle.setText(title)
        description = findViewById(R.id.description)
        description.setText(invoice_description)
        duedate=findViewById(R.id.duedate)
        duedate.setText("Due Date : " + due_date)
        payTotalButton = findViewById(R.id.payTotalButton)
        downloadLinear = findViewById(R.id.downloadLinear)
        shareLinear = findViewById(R.id.shareLinear)
        paidImg = findViewById(R.id.paidImg)
        mProgressRelLayout = findViewById(R.id.progressDialog)
        bannerImageViewPager = findViewById(R.id.bannerImageViewPager)
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(context, R.anim.linear_interpolator)
        mProgressRelLayout.startAnimation(aniRotate)

        mainLinear.visibility = View.VISIBLE
        paymentWeb.visibility = GONE

        payment_type_print=payment_type
        //merchantpassword = PreferenceManager.getMerchantPassword(context)
//        if (payment_type.equals("1")) {
//            payment_type_print =  "Online"
//        } else if (payment_type.equals("2")) {
//            "Cash"
//        } else if (payment_type.equals("3")) {
//            payment_type_print = "Cheque"
//        } else if (payment_type.equals("4")) {
//            payment_type_print =  "Bank Transfer"
//        }
//        else if (payment_type.equals("5")) {
//            payment_type_print = "Online"
//        } else {
//            payment_type_print =  "Online"
//        }
if (status.equals("0")){
    payTotalButton.visibility = View.VISIBLE
    totalLinear.visibility = View.VISIBLE
    printLinear.visibility=View.GONE
    paidImg.visibility = GONE
    mainLinear.visibility = View.VISIBLE
    paymentWeb.visibility = GONE
    mProgressRelLayout.setVisibility(GONE)
    mProgressRelLayout.clearAnimation()
    duedate.visibility=View.VISIBLE
   // payment_date = Calendar.DATE.toString()
    runnable?.let { handler.removeCallbacks(it) }
}else{
    payTotalButton.visibility = GONE
    totalLinear.visibility = View.VISIBLE
    paidImg.visibility = View.VISIBLE
    mainLinear.visibility = View.VISIBLE
    printLinear.visibility=View.VISIBLE
    mProgressRelLayout.setVisibility(GONE)
    mProgressRelLayout.clearAnimation()
    paymentWeb.visibility = GONE
    duedate.visibility=View.GONE
    runnable?.let { handler.removeCallbacks(it) }
}
        printLinearClick.setOnClickListener(){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                paymentWeb.loadUrl("about:blank")
                setWebViewSettingsPrint()
                loadWebViewWithDataPrint()
                createWebPrintJob(paymentWeb)
            }  else {
                Toast.makeText(
                    context,
                    "Print is not supported below Android KITKAT Version",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        downloadLinear.setOnClickListener(){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                paymentWeb.loadUrl("about:blank")
                setWebViewSettingsPrint()
                loadWebViewWithDataPrint()
                createWebPrintJob(paymentWeb)
            } else {
                Toast.makeText(
                    context,
                    "Print is not supported below Android KITKAT Version",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        shareLinear.setOnClickListener(){

                //sharePdfFilePrint()
                shareFile()


        }
        payTotalButton.setOnClickListener(){
getpaymenttoken()
            /*mainLinear.visibility = GONE
            printLinear.visibility = GONE
            paymentWeb.visibility = View.VISIBLE
            paymentWeb.loadUrl("about:blank")
            //   setWebViewSettings();
            //   setWebViewSettings();
            mProgressRelLayout.visibility = View.VISIBLE
            anim = RotateAnimation(
                0F, 360F, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            anim.setInterpolator(context, android.R.interpolator.linear)
            anim.setRepeatCount(Animation.INFINITE)
            anim.setDuration(1000)
            mProgressRelLayout.animation = anim
            mProgressRelLayout.startAnimation(anim)
            backStatus = "1"*/
        }

    }
    private fun getpaymenttoken(){
        mProgressRelLayout.visibility=View.VISIBLE
        val token = PreferenceData().getaccesstoken(context)
        val paymentTokenBody = PaymentTokenApiModel( PreferenceData().getStudentID(context).toString())
        val call: Call<PaymentTokenModel> =
            ApiClient(context).getClient.payment_token(paymentTokenBody, "Bearer " + token)
        call.enqueue(object : Callback<PaymentTokenModel> {
            override fun onFailure(call: Call<PaymentTokenModel>, t: Throwable) {
                mProgressRelLayout.visibility = View.GONE
                CommonFunctions.faliurepopup(context)
            }

            override fun onResponse(call: Call<PaymentTokenModel>, response: Response<PaymentTokenModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE

                if (responsedata != null) {
                    try {

                        if (response.body()!!.status==100) {
                           var payment_token=responsedata.responseArray.access_token
                            val tsLong = System.currentTimeMillis() / 1000
                            val ts = tsLong.toString()
                            var mechantorderRef=invoice_ref+"-"+ts
                            val amountDouble: Double = total_amount.toDouble() * 100
                            val amuntInt = amountDouble.toInt()
                            val strDoubleAmount = amuntInt.toString()
                             //order_id= "BISAD" + id + "S" + studentId
                            callForPayment(payment_token,strDoubleAmount)


                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(context)
                                getpaymenttoken()
                            } else {
                                if (response.body()!!.status==103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, context)
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
    private fun callForPayment(payment_token:String,amount:String){
        mProgressRelLayout.visibility=View.VISIBLE
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        var mechantorderRef=invoice_ref+"-"+ts
        val token = PreferenceData().getaccesstoken(context)
        val paymentGatewayBody = PaymentGatewayApiModel(amount,PreferenceData().getUserEmail(context).toString(),
            mechantorderRef,student_name,"","BISAD","","Abu Dhabi",
        payment_token)
        val call: Call<PaymentGatewayModel> =
            ApiClient(context).getClient.payment_gateway(paymentGatewayBody, "Bearer " + token)
        call.enqueue(object : Callback<PaymentGatewayModel> {
            override fun onFailure(call: Call<PaymentGatewayModel>, t: Throwable) {

                mProgressRelLayout.visibility = View.GONE
                CommonFunctions.faliurepopup(context)
            }

            override fun onResponse(call: Call<PaymentGatewayModel>, response: Response<PaymentGatewayModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {

                        if (response.body()!!.status==100) {

                            orderRef=responsedata.responseArray.order_reference
                            var orderPageUrl=responsedata.responseArray.order_paypage_url
                            var auth=responsedata.responseArray.authorization
                            val Code: String = orderPageUrl.split("=").toTypedArray().get(1)

                            mProgressRelLayout.visibility = View.GONE
                            val request: CardPaymentRequest = CardPaymentRequest.Builder().gatewayUrl(auth).code(Code).build()

                            val paymentClient = PaymentClient(activity, "fdhasfd")
                            paymentClient.launchCardPayment(request, 101)


                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(context)
                                getpaymenttoken()
                            } else {
                                if (response.body()!!.status==103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, context)
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            when (resultCode) {
                Activity.RESULT_OK -> onCardPaymentResponse(
                    CardPaymentData.getFromIntent(data!!)
                )
                Activity.RESULT_CANCELED ->{
                    Toast.makeText(context, "Transaction Failed", Toast.LENGTH_SHORT).show();

                }
            }
        }

        /*if (data == null) {
            mProgressRelLayout.visibility=View.GONE
            Toast.makeText(context, "transaction cancelled", Toast.LENGTH_SHORT).show()
        } else {
            if (requestCode == 101) {
                mProgressRelLayout.visibility=View.GONE

                val cardPaymentData = CardPaymentData.getFromIntent(data)

                if (cardPaymentData.code == 2) {



                    paySuccessApi()


                } else {
                    Toast.makeText(context, "Transaction failed", Toast.LENGTH_SHORT).show()
                }
            }
        }*/
    }
    fun onCardPaymentResponse(data: CardPaymentData) {
        when (data.code) {
            CardPaymentData.STATUS_PAYMENT_AUTHORIZED,
            CardPaymentData.STATUS_PAYMENT_CAPTURED -> {
                var internetCheck = InternetCheckClass.isInternetAvailable(context)
                if (internetCheck)
                {
                    paySuccessApi()
                }
                else{
                    InternetCheckClass.showSuccessInternetAlert(context)
                }
            }
            CardPaymentData.STATUS_PAYMENT_FAILED -> {
                Toast.makeText(context, "Transaction Failed", Toast.LENGTH_SHORT).show();
            }
            CardPaymentData.STATUS_GENERIC_ERROR -> {
                Toast.makeText(context, data.reason, Toast.LENGTH_SHORT).show();
            }
            else -> IllegalArgumentException(
                "Unknown payment response (${data.reason})")
        }
    }
    private fun paySuccessApi(){
        mProgressRelLayout.visibility=View.VISIBLE
        val token = PreferenceData().getaccesstoken(context)
        val paymentSuccessBody = PaymentSubmitApiModel(PreferenceData().getStudentID(context).toString(),
            id,orderRef)
        val call: Call<PaymentSubmitModel> =
            ApiClient(context).getClient.submit_payment(paymentSuccessBody, "Bearer " + token)
        call.enqueue(object : Callback<PaymentSubmitModel> {
            override fun onFailure(call: Call<PaymentSubmitModel>, t: Throwable) {
                mProgressRelLayout.visibility=View.GONE
                CommonFunctions.faliurepopup(context)
            }

            override fun onResponse(call: Call<PaymentSubmitModel>, response: Response<PaymentSubmitModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {

                        if (response.body()!!.status==100) {
                            mProgressRelLayout.visibility=View.GONE
                            status=response.body()!!.payment_details.status.toString()
                            id=response.body()!!.payment_details.id.toString()
                            student_name=response.body()!!.payment_details.student_name
                            account_code=response.body()!!.payment_details.account_code
                            pupil_code=response.body()!!.payment_details.pupil_code
                            academic_year=response.body()!!.payment_details.academic_year
                            invoice_ref=response.body()!!.payment_details.invoice_ref
                            invoice_description=response.body()!!.payment_details.invoice_description
                            current_amount=response.body()!!.payment_details.current_amount
                            vat_percentage=response.body()!!.payment_details.vat_percentage
                            vat_amount=response.body()!!.payment_details.vat_amount
                            total_amount=response.body()!!.payment_details.total_amount
                            due=response.body()!!.payment_details.due_date
                            paid_date=response.body()!!.payment_details.paid_date
                            payment_type=response.body()!!.payment_details.payment_type
                            payment_type_print=payment_type
                            paid_by=response.body()!!.payment_details.paid_by
                            thankyou_note=response.body()!!.payment_details.thankyou_note
                            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
                            val inputDateStr = due
                            val date: Date = inputFormat.parse(inputDateStr)
                            due_date = outputFormat.format(date)
                            payTotalButton.visibility = GONE
                            totalLinear.visibility = View.VISIBLE
                            paidImg.visibility = View.VISIBLE
                            mainLinear.visibility = View.VISIBLE
                            printLinear.visibility = View.VISIBLE
                            Toast.makeText(context, "Payment Successfull,Your payment is complete.", Toast.LENGTH_SHORT).show()

                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(context)
                                getpaymenttoken()
                            } else {
                                if (response.body()!!.status==103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, context)
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
    private fun setWebViewSettingsPrint() {
        mProgressRelLayout.visibility = View.VISIBLE
        anim = RotateAnimation(
            0F, 360F, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.setInterpolator(context, android.R.interpolator.linear)
        anim.repeatCount = Animation.INFINITE
        anim.duration = 1000
        mProgressRelLayout.animation = anim
        mProgressRelLayout.startAnimation(anim)
        paymentWeb.settings.javaScriptEnabled = true
        paymentWeb.clearCache(true)
        paymentWeb.settings.domStorageEnabled = true
        paymentWeb.settings.javaScriptCanOpenWindowsAutomatically = true
        paymentWeb.settings.setSupportMultipleWindows(true)
        paymentWeb.webViewClient = MyPrintWebViewClient()
//        paymentWeb.setWebChromeClient(new MyWebChromeClient());
    }

    fun loadWebViewWithDataPrint() {
var trn_pay=PreferenceData().getTrnPayment(context)
        var br = BufferedReader(InputStreamReader(assets.open("receipfee.html")))
        PaymentReceiptActivity().loadWebViewWithDataPrint(paymentWeb,br,student_name,
       account_code,
        pupil_code,academic_year,invoice_ref,invoice_description,current_amount,vat_percentage,
            vat_amount,total_amount,due_date,paid_date,payment_type,paid_by,title,
            payment_type_print,order_id,orderRef,thankyou_note,trn_pay
        )

        /*var sb = StringBuffer()
        var eachLine = ""
        try {
            val br = BufferedReader(InputStreamReader(assets.open("receipfee.html")))
            sb = StringBuffer()
            eachLine = br.readLine()
            while (br.readLine() != null) {
                sb.append(eachLine)
                sb.append("\n")
                eachLine = br.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        fullHtml = sb.toString()
        if (fullHtml!!.length > 0) {
            fullHtml = fullHtml!!.replace("###amount###", total_amount)
            //            fullHtml = fullHtml.replace("###order_Id###", orderId);
            fullHtml = fullHtml!!.replace("###ParentName###", student_name)
            fullHtml = fullHtml!!.replace("###Date###", Calendar.DATE.toString())
            fullHtml = fullHtml!!.replace("###paidBy###", invoice_description)
            //fullHtml = fullHtml.replace("###billing_code###", isams_no.)
            // fullHtml = fullHtml.replace("###paidBy###", "Done");
            fullHtml = fullHtml!!.replace("###title###", invoice_description)
           // fullHtml = fullHtml.replace("###trn_no###", trn_no)
            //fullHtml = fullHtml.replace("###order_Id###", invoice_no)
            fullHtml = fullHtml!!.replace("###payment_type###", payment_type_print)
            fullHtml = fullHtml!!.replace("###percentageAmount###", vat_amount)
            //fullHtml = fullHtml.replace("###full-amount###", formated_amount)
            fullHtml = fullHtml!!.replace("###percent###", "($vat_percentage%)")
            backStatus = "2"

//            paymentWeb.loadDataWithBaseURL("", fullHtml, "text/html", "UTF-8", "");
            paymentWeb.loadDataWithBaseURL(
                "file:///android_asset/images/",
                fullHtml,
                "text/html; charset=utf-8",
                "utf-8",
                "about:blank"
            )
        }*/
    }

    private fun createWebPrintJob(webView: WebView) {
        mProgressRelLayout.clearAnimation()
        mProgressRelLayout.visibility = GONE
        paymentWeb.visibility = GONE
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager

        val printAdapter = webView.createPrintDocumentAdapter()
        val jobName = getString(R.string.app_name) + "_Pay" + "BISAD"
        val builder = PrintAttributes.Builder()
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
        if (printManager != null) {
            printJob = printManager.print(jobName, printAdapter, builder.build())
        }
        if (printJob!!.isCompleted()) {
//            Toast.makeText(getApplicationContext(), R.string.print_complete, Toast.LENGTH_LONG).show();
        } else if (printJob!!.isFailed()) {
            Toast.makeText(applicationContext, "Print failed", Toast.LENGTH_SHORT).show()
        }
    }
    private class MyPrintWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            //Calling a javascript function in html page

           //view.loadUrl(url);
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url,favicon)

        }
    }
    fun shareFile(){

        startdownloadingforshare()
        val aName = intent.getStringExtra("iName")
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM,  uriFromFile(context,File(this.getExternalFilesDir(pdfUri.toString()
        )?.absolutePath.toString(), "$aName")))
        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        shareIntent.type = "application/pdf"
        startActivity(Intent.createChooser(shareIntent, "share.."))
       /* val intentShareFile = Intent(Intent.ACTION_SEND)
        val fileWithinMyDir = File(getFilepath(title + "docs.pdf"))
        if (fileWithinMyDir.exists()) {
            intentShareFile.type = "application/pdf"
            intentShareFile.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("file://" + getFilepath(title + "docs.pdf"))
            )
            startActivity(Intent.createChooser(intentShareFile, "Share File"))
        } else {
            startdownloadingforshare()

            intentShareFile.type = "application/pdf"
            intentShareFile.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("file://" + getFilepath(title + "docs.pdf"))
            )
            startActivity(Intent.createChooser(intentShareFile, "Share File"))

        }*/
    }
    fun uriFromFile(context:Context, file:File):Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return FileProvider.getUriForFile(context, "com.mobatia.bisad" + ".provider", file)
        }
        else
        {
            return Uri.fromFile(file)
        }
    }
    private fun getFilepath(filename: String): String? {
        return File(
            Environment.getExternalStorageDirectory().absolutePath,
            "/Download/$filename"
        ).path
    }
    private fun startdownloadingforshare() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWeb.loadUrl("about:blank")
            setWebViewSettingsPrint()
            loadWebViewWithDataPrint()
            createWebPrintJob(paymentWeb)
        } else {
            Toast.makeText(
                context,
                "Print is not supported below Android KITKAT Version",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

   /* fun sharePdfFilePrint() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWeb.loadUrl("about:blank")
            setWebViewSettingsPrint()
            loadWebViewWithDataPrint()
            pathFile = File(
                Environment.getExternalStorageDirectory()
                    .absolutePath + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/"
            )
            pathFile!!.mkdirs()
            if (Build.VERSION.SDK_INT >= 23) {
                pdfUri = FileProvider.getUriForFile(
                    context,
                    "$packageName.provider", createWebPrintJobShare(paymentWeb, pathFile!!)!!
                )
            } else {
                pdfUri = Uri.fromFile(createWebPrintJobShare(paymentWeb, pathFile!!))
            }
            val intent = Intent(context, SharePdfHtmlViewActivity::class.java)
            intent.putExtra("url", fullHtml)
            intent.putExtra("tab_type", "Preview")
            intent.putExtra("orderId", "NASDUBAI")
            intent.putExtra("pdfUri", pdfUri.toString())
            startActivity(intent)
            paymentWeb.visibility = GONE
        } else {
//            Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
        }
    }
    private fun createWebPrintJobShare(webView: WebView, path: File): File? {
        val jobName = "Receipt_Share_" + "NASDUBAI" + ".pdf"
        mProgressRelLayout.clearAnimation()
        mProgressRelLayout.visibility = GONE
        paymentWeb.visibility = View.VISIBLE
        try {
            val printAdapter: PrintDocumentAdapter
            val attributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
            val pdfPrint = PdfPrint(attributes, context)
            printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                webView.createPrintDocumentAdapter(jobName)
            } else {
                webView.createPrintDocumentAdapter()
            }
            pdfPrint.printNew(printAdapter, path, jobName, context.getCacheDir().getPath())
            return File(path.absolutePath + "/" + jobName)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            paymentWeb.visibility = GONE
        }
        return null
    }*/


    override fun onResume() {
        super.onResume()
        if (!CommonFunctions.runMethod.equals("Dev")) {
            if (CommonFunctions.isDeveloperModeEnabled(context)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
        }
    }
}