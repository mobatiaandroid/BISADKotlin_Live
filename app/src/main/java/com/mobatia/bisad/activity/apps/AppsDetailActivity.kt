package com.mobatia.bisad.activity.apps

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    var permissions = arrayOf(
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.CAMERA
    )
    val MULTIPLE_PERMISSIONS = 10
    var uploadMessage: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    val REQUEST_SELECT_FILE = 100
    private val FILECHOOSER_RESULTCODE = 123



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
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.loadsImagesAutomatically = true
        webView.setClickable(true);
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
        webView.webViewClient = MyWebViewClient(this)

     //   webView.loadUrl(url)
        webView.loadUrl(url!!)
        webView?.webChromeClient = object:WebChromeClient() {

            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                Log.d("alert", message)
                val dialogBuilder = AlertDialog.Builder(this@AppsDetailActivity)

                dialogBuilder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK") { _, _ ->
                        result.confirm()
                    }

                val alert = dialogBuilder.create()
                alert.show()

                return true
            }

            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            fun openFileChooser(uploadMsg : ValueCallback<Uri>, acceptType:String) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE)
            }

            // For Lollipop 5.0+ Devices
            override fun onShowFileChooser(mWebView:WebView, filePathCallback:ValueCallback<Array<Uri>>, fileChooserParams:WebChromeClient.FileChooserParams):Boolean {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    if (uploadMessage != null) {
                        uploadMessage?.onReceiveValue(null)
                        uploadMessage = null
                    }
                    uploadMessage = filePathCallback
                    val intent = fileChooserParams.createIntent()
                    try {
                        startActivityForResult(intent, REQUEST_SELECT_FILE)
                    } catch (e: ActivityNotFoundException) {
                        uploadMessage = null
                        Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show()
                        return false
                    }
                    return true
                }else{
                    return false
                }
            }

            //For Android 4.1 only
            fun openFileChooser(uploadMsg:ValueCallback<Uri>, acceptType:String, capture:String) {
                mUploadMessage = uploadMsg
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE)
            }

            fun openFileChooser(uploadMsg:ValueCallback<Uri>) {
                // checkSelfPermission()
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE)
            }
        }
       /* webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
               // progressDialogAdd.progress = newProgress
                if (newProgress == 100) {
                  //  progressDialogAdd.visibility = View.GONE
                    // back.visibility = View.VISIBLE

                }
            }
        }*/

        progressDialog.visibility=View.GONE
    }
    private fun CheckPermission(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = java.util.ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(mContext, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this@AppsDetailActivity,
                listPermissionsNeeded.toTypedArray<String>(), MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if(requestCode == REQUEST_SELECT_FILE){
                if(uploadMessage != null){
                    uploadMessage?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,data))
                    uploadMessage = null
                }
            }
        }else if(requestCode == FILECHOOSER_RESULTCODE){
            if(mUploadMessage!=null){
                var result = data?.data
                mUploadMessage?.onReceiveValue(result)
                mUploadMessage = null
            }
        }else{
            Toast.makeText(this,"Failed to open file uploader, please check app permissions.",Toast.LENGTH_LONG).show()
            super.onActivityResult(requestCode, resultCode, data)
        }


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