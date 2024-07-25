package com.mobatia.bisad.activity.school_trips

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintAttributes.Resolution
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
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
import com.github.barteksc.pdfviewer.PDFView
import com.gun0912.tedpermission.PermissionListener
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.payment.PdfPrint
import com.mobatia.bisad.manager.HeaderManager
import com.mobatia.bisad.manager.PreferenceData
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class TripInvoicePrintActivity : AppCompatActivity() {
     lateinit var mContext: Context
    private lateinit var mWebView: WebView
    private lateinit var paymentWebDummy: WebView
   // private lateinit var mProgressRelLayout: RelativeLayout
    private lateinit var mwebSettings: WebSettings
    private val mLoadUrl: String? = null
    var extras: Bundle? = null
    lateinit var relativeHeader: RelativeLayout
    lateinit var headermanager: HeaderManager
    lateinit var back: ImageView
    lateinit var home: ImageView
    var addToCalendar: LinearLayout? = null
    var tab_type = ""
    var orderId = ""
    var pdfUriFrom = ""
    lateinit var pathFile: File
    var pdfUri: Uri? = null
    var pdfView: PDFView? = null
    var fullHtml: String? = null
    var amount = ""
    var title = ""
    var invoice = ""
    var paidby = ""
    var paidDate = ""

    var received = ""
    var triptotal = ""
    var outsatnding = ""
    lateinit var mProgressRelLayout:RelativeLayout

    //    String billingCode="";
    var tr_no = ""
    var payment_type = ""
    lateinit var emailLinear: LinearLayout
    lateinit var printLinearClick: LinearLayout
    lateinit var downloadLinear: LinearLayout
    lateinit var shareLinear: LinearLayout
    lateinit var anim: RotateAnimation
    var printJob: PrintJob? = null
    var BackPage = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview_activity)
        mContext = this
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")!!
            orderId = extras!!.getString("orderId")!!
            amount = extras!!.getString("amount")!!
            title = extras!!.getString("title")!!
            invoice = extras!!.getString("invoice")!!
            paidby = extras!!.getString("paidby")!!
            paidDate = extras!!.getString("paidDate")!!
            //            billingCode = extras.getString("billingCode");
            tr_no = extras!!.getString("tr_no")!!
            payment_type = extras!!.getString("payment_type")!!
            received = extras!!.getString("received_amount")!!
            triptotal = extras!!.getString("trip_amount")!!
            outsatnding = extras!!.getString("outstanding_amount")!!
        }
        initialiseUI()
        getWebViewSettings()
    }
    private fun initialiseUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        mWebView = findViewById<View>(R.id.paymentWeb) as WebView
        paymentWebDummy = findViewById<View>(R.id.paymentWebDummy) as WebView
        mWebView.setVisibility(View.VISIBLE)
        paymentWebDummy.setVisibility(View.GONE)
        mProgressRelLayout=findViewById(R.id.progressDialog)

        //mProgressRelLayout = findViewById<View>(R.id.progressDialog) as RelativeLayout
      //  mProgressRelLayout.setVisibility(View.GONE)
        headermanager = HeaderManager(this@TripInvoicePrintActivity, "Trip Preview")
        headermanager.getHeader(relativeHeader, 0)
        back = headermanager.getLeftButton()
        emailLinear = findViewById<LinearLayout>(R.id.emailLinear)
        printLinearClick = findViewById<LinearLayout>(R.id.printLinearClick)
        downloadLinear = findViewById<LinearLayout>(R.id.downloadLinear)
        shareLinear = findViewById<LinearLayout>(R.id.shareLinear)
        home = headermanager.getLogoButton()!!
        home!!.setOnClickListener {
            val `in` = Intent(
                mContext,
                HomeActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        headermanager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener { finish() }
        printLinearClick.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                paymentWebDummy.loadUrl("about:blank")
                setWebViewSettingsPrint()
                loadWebViewWithDataPrint()
                createWebPrintJob(mWebView)
            }  else {
                Toast.makeText(
                    mContext,
                    "Print is not supported below Android KITKAT Version",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        downloadLinear.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                mWebView.loadUrl("about:blank")
                setWebViewSettingsPrint()
                loadWebViewWithDataPrint()
                createWebPrintJob(mWebView)
            } else {
                Toast.makeText(
                    mContext,
                    "Print is not supported below Android KITKAT Version",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        shareLinear.setOnClickListener(View.OnClickListener {
            startdownloadingforshare()
            val aName = intent.getStringExtra("iName")
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_STREAM,  uriFromFile(mContext,File(this.getExternalFilesDir(pdfUri.toString()
            )?.absolutePath.toString(), "$aName")))
            shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            shareIntent.type = "application/pdf"
            startActivity(Intent.createChooser(shareIntent, "share.."))
        })
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
    private fun getWebViewSettings() {
        mWebView.isFocusable = true
        mWebView.isFocusableInTouchMode = true
        mWebView.setBackgroundColor(0X00000000)
        mWebView.isVerticalScrollBarEnabled = false
        mWebView.isHorizontalScrollBarEnabled = false
        mWebView.webChromeClient = WebChromeClient()
        mwebSettings = mWebView.settings
        mwebSettings.setSaveFormData(true)
        mwebSettings.setBuiltInZoomControls(false)
        mwebSettings.setSupportZoom(false)
        mwebSettings.setPluginState(WebSettings.PluginState.ON)
        mwebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        mwebSettings.setJavaScriptCanOpenWindowsAutomatically(true)
        mwebSettings.setDomStorageEnabled(true)
        mwebSettings.setDatabaseEnabled(true)
        mwebSettings.setDefaultTextEncodingName("utf-8")
        mwebSettings.setLoadsImagesAutomatically(true)
        mwebSettings.setLoadsImagesAutomatically(true)
        mwebSettings.setUseWideViewPort(true)
        mWebView.setInitialScale(1)
        mwebSettings.setLoadWithOverviewMode(true)
        //        mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
//        mWebView.getSettings().setAppCachePath(
//                mContext.getCacheDir().getAbsolutePath());
        mWebView.settings.allowFileAccess = true
        //        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        loadWebViewWithDataPrint()
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
                mContext,
                "Print is not supported below Android KITKAT Version",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun loadWebViewWithDataPrint() {
        var trn_no= PreferenceData().getTrnNo(mContext)
        var br = BufferedReader(InputStreamReader(assets.open("tripreciept.html")))
        PayLostRecActivity().loadWebViewWithDataPrint(mWebView,br,
            PreferenceData().getUserCode(mContext),PreferenceData().getUserEmail(mContext),title,
            PreferenceData().getStudentID(mContext),PreferenceData().getUserID(mContext),amount,
            "bill_no",orderId,paidDate,invoice,payment_type,
            PreferenceData().getStudentName(mContext),trn_no,triptotal,received,outsatnding)
       /* var sb = StringBuffer()
        var eachLine = ""
        try {
            val br = BufferedReader(InputStreamReader(assets.open("tripreciept.html")))
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
        if (fullHtml!!.length > 0) {
            fullHtml = fullHtml!!.replace("###amount###", amount)
            fullHtml = fullHtml!!.replace("###order_Id###", orderId)
            fullHtml = fullHtml!!.replace("###ParentName###", paidby)
            // Log.e("date", paidDate);
            //  Log.e("date1", AppUtils.dateConversionddmmyyyytoddMMYYYY(paidDate));
            fullHtml = fullHtml!!.replace("###Date###", paidDate)
            fullHtml = fullHtml!!.replace("###paidBy###", invoice)
            //            fullHtml = fullHtml.replace("###billing_code###",billingCode );
            fullHtml = fullHtml!!.replace("###trn_no###", tr_no)
            fullHtml = fullHtml!!.replace("###payment_type###", payment_type)
            // fullHtml = fullHtml.replace("###paidBy###", "Done");
            fullHtml = fullHtml!!.replace("###title###", title)
            paymentWebDummy.loadDataWithBaseURL(
                "file:///android_asset/images/",
                fullHtml!!, "text/html; charset=utf-8", "utf-8", "about:blank"
            )
            mWebView.loadDataWithBaseURL(
                "file:///android_asset/images/",
                fullHtml!!, "text/html; charset=utf-8", "utf-8", "about:blank"
            )
        }*/
    }
    private fun setWebViewSettingsPrint() {
      //  mProgressRelLayout.setVisibility(View.VISIBLE)
        mProgressRelLayout.setVisibility(View.VISIBLE)
        anim = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.setInterpolator(mContext, android.R.interpolator.linear)
        anim.setRepeatCount(Animation.INFINITE)
        anim.setDuration(1000)
        mProgressRelLayout.setAnimation(anim)
        mProgressRelLayout.startAnimation(anim)
        paymentWebDummy.settings.javaScriptEnabled = true
        paymentWebDummy.clearCache(true)
        paymentWebDummy.settings.domStorageEnabled = true
        paymentWebDummy.settings.javaScriptCanOpenWindowsAutomatically = true
        paymentWebDummy.settings.setSupportMultipleWindows(true)
        paymentWebDummy.webViewClient =MyPrintWebViewClient()
//        paymentWeb.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyPrintWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            //Calling a javascript function in html page

//            view.loadUrl(url);
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            //   Log.d("WebView", "print webpage loading.." + url);
        }
    }

    private fun createWebPrintJob(webView: WebView) {
      //  mProgressRelLayout.clearAnimation()
      //  mProgressRelLayout.setVisibility(View.GONE)
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
    var permissionListenerStorage: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            sharePdfFilePrint()
        }

        override fun onPermissionDenied(deniedPermissions: ArrayList<String?>) {
            Toast.makeText(mContext, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT)
                .show()
        }
    }
    fun sharePdfFilePrint() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWebDummy.loadUrl("about:blank")
            setWebViewSettingsPrint()
            loadWebViewWithDataPrint()
            //                    createWebPrintJobShare(paymentWeb);
            pathFile = File(
                Environment.getExternalStorageDirectory()
                    .absolutePath + "/" + "NAS_DUBAI_CANTEEN/Payments" + "_" + "NASDUBAI" + "/"
            )
            println("Path file 5$pathFile")
            pathFile!!.mkdirs()
            //            if(!pathFile.exists())
//                pathFile.mkdirs();
            pdfUri = if (Build.VERSION.SDK_INT >= 23) {
                println("web view data$fullHtml")
                FileProvider.getUriForFile(
                    mContext,
                    "$packageName.provider", createWebPrintJobShare(paymentWebDummy, pathFile)!!
                )
            } else {
                println("Path file 6$pathFile")
                Uri.fromFile(createWebPrintJobShare(paymentWebDummy, pathFile))
            }
           /* val intent = Intent(mContext, SharePdfHtmlViewActivity::class.java)
            intent.putExtra("url", fullHtml)
            intent.putExtra("tab_type", "Preview")
            intent.putExtra("orderId", orderId)
            intent.putExtra("pdfUri", pdfUri.toString())
            startActivity(intent)*/
            paymentWebDummy.visibility = View.GONE
        } else {
//            Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
        }
    }

    private fun createWebPrintJobShare(webView: WebView, path: File): File? {
        val jobName = "$orderId.pdf"
      //  mProgressRelLayout.clearAnimation()
       // mProgressRelLayout.setVisibility(View.GONE)
        paymentWebDummy.visibility = View.VISIBLE
        try {
            val printAdapter: PrintDocumentAdapter
            val attributes = PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
            val pdfPrint = PdfPrint(attributes, mContext)
            printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                webView.createPrintDocumentAdapter(jobName)
                //Log.v("working", "1");
            } else {
                webView.createPrintDocumentAdapter()
                // Log.v("working", "2");
            }
            pdfPrint.printNew(printAdapter, path, jobName, mContext.cacheDir.path)
            //Log.v("pathfile", path.getAbsolutePath() + "/"  + jobName);
            return File(path.absolutePath + "/" + jobName)
        } catch (e: Exception) {
            e.printStackTrace()
            paymentWebDummy.visibility = View.GONE
        }
        return null
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (BackPage) {
            finish()
        } else {
            mWebView.visibility = View.VISIBLE
            mWebView.loadDataWithBaseURL(
                "file:///android_asset/images/",
                fullHtml!!, "text/html; charset=utf-8", "utf-8", "about:blank"
            )
            BackPage = true
        }
    }
}