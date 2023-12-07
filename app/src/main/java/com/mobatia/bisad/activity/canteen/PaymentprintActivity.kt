package com.mobatia.bisad.activity.canteen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.home.studentName
import com.mobatia.bisad.activity.payment.PaymentReceiptActivity
import com.mobatia.bisad.manager.PreferenceData
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PaymentprintActivity:AppCompatActivity() {
    lateinit var sharedprefs: PreferenceData
    lateinit var nContext: Context
    private lateinit var logoClickImg: ImageView
    lateinit var back: RelativeLayout
    lateinit var progress: RelativeLayout
    lateinit private var mWebView: WebView
    lateinit private var paymentWebDummy: WebView
    lateinit private var mwebSettings: WebSettings
    //lateinit var relativeHeader: RelativeLayout
    lateinit var printLinearClick: LinearLayout
    lateinit var downloadLinear: LinearLayout
    lateinit var shareLinear: LinearLayout
    lateinit var anim: RotateAnimation
    lateinit var mProgressRelLayout: RelativeLayout
    var printJob: PrintJob? = null
    var pdfUri:String=""

   lateinit var fullHtml: String
   var parent_name:String=""
   var email:String=""
   var id:String=""
   var student_id:String?=""
   var user_id:String?=""
   var amount:String=""
   var bill_no:String=""
   var order_reference:String=""
   var created_on:String=""
   var invoice_note:String=""
   var payment_type:String=""
   var from_time:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview_activity)
        sharedprefs = PreferenceData()
        initfn()
        getWebViewSettings()

    }
    private fun initfn(){
        nContext = this
        parent_name=intent.getStringExtra("parent_name").toString()
        email=intent.getStringExtra("email").toString()
        id=intent.getStringExtra("id").toString()
        student_id=intent.getStringExtra("student_id").toString()
        user_id=intent.getStringExtra("user_id").toString()
        amount=intent.getStringExtra("amount").toString()
        from_time=intent.getStringExtra("created_on").toString()
        amount=intent.getStringExtra("amount").toString()
        invoice_note=intent.getStringExtra("invoice_note").toString()
        payment_type=intent.getStringExtra("payment_type").toString()
        bill_no=intent.getStringExtra("bill_no").toString()
        order_reference=intent.getStringExtra("order_reference").toString()


        val inputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = from_time
        val date: Date = inputFormat.parse(inputDateStr)
        created_on = outputFormat.format(date)

        printLinearClick = findViewById(R.id.printLinearClick)
        mProgressRelLayout=findViewById(R.id.progressDialog)
        downloadLinear = findViewById(R.id.downloadLinear)
        shareLinear = findViewById(R.id.shareLinear)
       logoClickImg = findViewById(R.id.logoClickImgView)
        back = findViewById(R.id.backRelative)
        //relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        mWebView = findViewById(R.id.paymentWeb)
        paymentWebDummy = findViewById(R.id.paymentWebDummy)
        mWebView.setVisibility(View.VISIBLE)
        paymentWebDummy.setVisibility(View.GONE)

        back.setOnClickListener {
            finish()
        }
        logoClickImg.setOnClickListener {
            val intent = Intent(nContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        printLinearClick.setOnClickListener(){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                paymentWebDummy.loadUrl("about:blank")
                setWebViewSettingsPrint()
                loadWebViewWithDataPrint()
                createWebPrintJob(mWebView)
            }  else {
                Toast.makeText(
                    nContext,
                    "Print is not supported below Android KITKAT Version",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        downloadLinear.setOnClickListener(){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                mWebView.loadUrl("about:blank")
                setWebViewSettingsPrint()
                loadWebViewWithDataPrint()
                createWebPrintJob(mWebView)
            } else {
                Toast.makeText(
                    nContext,
                    "Print is not supported below Android KITKAT Version",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        shareLinear.setOnClickListener(){

            //sharePdfFilePrint()
            shareFile()


        }



    }
    private fun getWebViewSettings() {
        mWebView.isFocusable = true
        mWebView.isFocusableInTouchMode = true
        mWebView.setBackgroundColor(0X00000000)
        mWebView.isVerticalScrollBarEnabled = false
        mWebView.isHorizontalScrollBarEnabled = false
        mWebView.webChromeClient = WebChromeClient()
        mwebSettings = mWebView.settings
        mwebSettings.saveFormData = true
        mwebSettings.builtInZoomControls = false
        mwebSettings.setSupportZoom(false)
        mwebSettings.pluginState = WebSettings.PluginState.ON
        mwebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        mwebSettings.javaScriptCanOpenWindowsAutomatically = true
        mwebSettings.domStorageEnabled = true
        mwebSettings.databaseEnabled = true
        mwebSettings.defaultTextEncodingName = "utf-8"
        mwebSettings.loadsImagesAutomatically = true
        mwebSettings.loadsImagesAutomatically = true
        mwebSettings.useWideViewPort = true
        mWebView.setInitialScale(1)
        mwebSettings.loadWithOverviewMode = true
       /* mWebView.settings.setAppCacheMaxSize((10 * 1024 * 1024).toLong()) // 5MB
        mWebView.settings.setAppCachePath(
            nContext.getCacheDir().getAbsolutePath()
        )*/
        mWebView.settings.allowFileAccess = true
        mWebView.settings.cacheMode=WebSettings.LOAD_NO_CACHE
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        loadWebViewWithDataPrint()
    }
    fun loadWebViewWithDataPrint() {
        var trn_no=PreferenceData().getTrnNo(nContext)
        var br = BufferedReader(InputStreamReader(assets.open("paycanteenreciept.html")))
        PayCanteenRecActivity().loadWebViewWithDataPrint(mWebView,br,parent_name,email,id,
            student_id,user_id,amount,bill_no,order_reference,created_on,invoice_note,payment_type,
            sharedprefs.getStudentName(nContext),trn_no)

        /*var sb = StringBuffer()
        var eachLine = ""
        try {
            val br = BufferedReader(InputStreamReader(assets.open("paycanteenreciept.html")))
            sb = StringBuffer()
            eachLine = br.readLine()
            while (eachLine != null) {
                sb.append(eachLine)
                sb.append("\n")
                eachLine = br.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        fullHtml = sb.toString()
        if (fullHtml.length > 0) {
            fullHtml = fullHtml.replace("###amount###", amount)
            fullHtml = fullHtml.replace("###order_Id###", id)
            fullHtml = fullHtml.replace("###ParentName###", parent_name)
            fullHtml = fullHtml.replace("###Date###", created_on)
            fullHtml = fullHtml.replace("###paidBy###", invoice_note)
            //fullHtml = fullHtml.replace("###billing_code###", billingCode)
            //fullHtml = fullHtml.replace("###trn_no###", tr_no)
            fullHtml = fullHtml.replace("###payment_type###", payment_type)
            // fullHtml = fullHtml.replace("###paidBy###", "Done");
            fullHtml = fullHtml.replace("###title###", "title")
            paymentWebDummy.loadDataWithBaseURL(
                "file:///android_asset/images/",
                fullHtml,
                "text/html; charset=utf-8",
                "utf-8",
                "about:blank"
            )
            mWebView.loadDataWithBaseURL(
                "file:///android_asset/images/",
                fullHtml,
                "text/html; charset=utf-8",
                "utf-8",
                "about:blank"
            )
        }*/
    }
    private fun setWebViewSettingsPrint() {
        mProgressRelLayout.setVisibility(View.VISIBLE)
        anim = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.setInterpolator(nContext, android.R.interpolator.linear)
        anim.setRepeatCount(Animation.INFINITE)
        anim.setDuration(1000)
        mProgressRelLayout.setAnimation(anim)
        mProgressRelLayout.startAnimation(anim)
        paymentWebDummy.settings.javaScriptEnabled = true
        paymentWebDummy.clearCache(true)
        paymentWebDummy.settings.domStorageEnabled = true
        paymentWebDummy.settings.javaScriptCanOpenWindowsAutomatically = true
        paymentWebDummy.settings.setSupportMultipleWindows(true)
        paymentWebDummy.webViewClient = MyPrintWebViewClient()
//        paymentWeb.setWebChromeClient(new MyWebChromeClient());
    }
    private class MyPrintWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            //Calling a javascript function in html page

//            view.loadUrl(url);
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

        }
    }

    private fun createWebPrintJob(webView: WebView) {
        mProgressRelLayout.clearAnimation()
        mProgressRelLayout.visibility = View.GONE
        paymentWebDummy.visibility = View.GONE
        val printManager = this.getSystemService(PRINT_SERVICE) as PrintManager
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


    fun shareFile(){
        startdownloadingforshare()
        val aName = intent.getStringExtra("iName")
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM,  uriFromFile(nContext,File(this.getExternalFilesDir(pdfUri.toString()
        )?.absolutePath.toString(), "$aName")))
        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        shareIntent.type = "application/pdf"
        startActivity(Intent.createChooser(shareIntent, "share.."))

       /* val intentShareFile = Intent(Intent.ACTION_SEND)
        val fileWithinMyDir = File(getFilepath(payment_type + "docs.pdf"))
        if (fileWithinMyDir.exists()) {
            intentShareFile.type = "application/pdf"
            intentShareFile.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("file://" + getFilepath(payment_type + "docs.pdf"))
            )
            startActivity(Intent.createChooser(intentShareFile, "Share File"))
        } else {
            startdownloadingforshare()

            intentShareFile.type = "application/pdf"
            intentShareFile.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("file://" + getFilepath(payment_type + "docs.pdf"))
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
            paymentWebDummy.loadUrl("about:blank")
            setWebViewSettingsPrint()
            loadWebViewWithDataPrint()
            createWebPrintJob(mWebView)
        }
        else {
            Toast.makeText(
                nContext,
                "Print is not supported below Android KITKAT Version",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}