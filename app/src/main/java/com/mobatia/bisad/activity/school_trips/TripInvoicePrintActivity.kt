package com.mobatia.bisad.activity.school_trips

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.print.PrintJob
import android.view.View
import android.view.animation.RotateAnimation
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class TripInvoicePrintActivity : AppCompatActivity() {
    private var mContext: Context? = null
    private var mWebView: WebView? = null
    private var paymentWebDummy: WebView? = null
    private var mProgressRelLayout: RelativeLayout? = null
    private val mwebSettings: WebSettings? = null
    private val mLoadUrl: String? = null
    var extras: Bundle? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var addToCalendar: LinearLayout? = null
    var tab_type = ""
    var orderId = ""
    var pdfUriFrom = ""
    var pathFile: File? = null
    var pdfUri: Uri? = null
    var pdfView: PDFView? = null
    var fullHtml: String? = null
    var amount = ""
    var title = ""
    var invoice = ""
    var paidby = ""
    var paidDate = ""

    //    String billingCode="";
    var tr_no = ""
    var payment_type = ""
    var emailLinear: LinearLayout? = null
    var printLinearClick: LinearLayout? = null
    var downloadLinear: LinearLayout? = null
    var shareLinear: LinearLayout? = null
    var anim: RotateAnimation? = null
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
        mProgressRelLayout = findViewById<View>(R.id.progressDialog) as RelativeLayout
        mProgressRelLayout.setVisibility(View.GONE)
        headermanager = HeaderManager(this@TripInvoicePrintActivity, "Preview")
        headermanager.getHeader(relativeHeader, 0)
        back = headermanager.getLeftButton()
        emailLinear = findViewById<LinearLayout>(R.id.emailLinear)
        printLinearClick = findViewById<LinearLayout>(R.id.printLinearClick)
        downloadLinear = findViewById<LinearLayout>(R.id.downloadLinear)
        shareLinear = findViewById<LinearLayout>(R.id.shareLinear)
        home = headermanager.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(
                mContext,
                HomeListAppCompatActivity::class.java
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
            BackPage = false
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                //  mWebView.loadUrl("about:blank");
                paymentWebDummy.loadUrl("about:blank")
                setWebViewSettingsPrint()
                loadWebViewWithDataPrint()
                createWebPrintJob(paymentWebDummy)
            } else {
                Toast.makeText(
                    mContext,
                    "Print is not supported below Android KITKAT Version",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        downloadLinear.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                paymentWebDummy.loadUrl("about:blank")
                setWebViewSettingsPrint()
                loadWebViewWithDataPrint()
                createWebPrintJob(paymentWebDummy)
            } else {
                Toast.makeText(
                    mContext,
                    "Print is not supported below Android KITKAT Version",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        shareLinear.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                println("share function sharePdfFilePrint permission")
                TedPermission.with(mContext)
                    .setPermissionListener(permissionListenerStorage)
                    .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .check()
            } else {
                println("share function sharePdfFilePrint")
                sharePdfFilePrint()
            }
        })
    }
}