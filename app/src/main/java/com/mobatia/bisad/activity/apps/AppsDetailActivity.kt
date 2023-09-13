package com.mobatia.bisad.activity.apps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity

private lateinit var progressDialog: RelativeLayout
class AppsDetailActivity : AppCompatActivity() {
    lateinit var mContext: Context
    private lateinit var webView: WebView

    private lateinit var relativeHeader: RelativeLayout
    private lateinit var backRelative: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    var url:String?=""
    var headingValue:String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_media_detail)
        url=intent.getStringExtra("url")
        headingValue=intent.getStringExtra("heading")
        initializeUI()
        getWebViewSettings()


    }
    private fun getWebViewSettings() {
        progressDialog.visibility
        val settings = webView.settings
        settings.domStorageEnabled = true
    }
    private fun initializeUI() {
        // headermanager=HeaderManagerNoColorSpace(SocialMediaDetailActivity.this, "FACEBOOK");
        mContext=this
        webView = findViewById(R.id.webView)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressDialog = findViewById(R.id.progressDialog)
        relativeHeader = findViewById(R.id.relativeHeader)
        backRelative = findViewById(R.id.backRelative)
        heading = findViewById(R.id.heading)

        heading.text = headingValue
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient(this)

     //   webView.loadUrl(url)
        webView.loadUrl(url!!)
        progressDialog.visibility=View.GONE
    }



    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            view?.loadUrl(url)

            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            progressDialog.visibility=View.GONE
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            progressDialog.visibility=View.GONE
            Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }
}