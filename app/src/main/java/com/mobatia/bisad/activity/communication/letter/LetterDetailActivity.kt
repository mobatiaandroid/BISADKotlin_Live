package com.mobatia.bisad.activity.communication.letter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.settings.termsofservice.model.TermsOfServiceModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LetterDetailActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    var message:String=""
    var url:String=""
    var date:String=""
    var termsTitle:String=""
    var termsDescription:String=""
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var backRelative: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    private lateinit var webView: WebView
    var myFormatCalende:String="yyyy-MM-dd HH:mm:ss"
    private lateinit var progressDialog: RelativeLayout
    lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_detail)
        mContext=this
        activity=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        initUI()


    }
    fun initUI() {
        termsTitle=intent.getStringExtra("title").toString()
        termsDescription=intent.getStringExtra("message").toString()
        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        backRelative = findViewById(R.id.backRelative)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressDialog = findViewById(R.id.progressDialog)
        webView = findViewById(R.id.webView)
        getSettings()
        heading.text = termsTitle
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
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)

        var pushNotificationDetail="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "\n" +
                "@font-face {\n" +
                "font-family: SourceSansPro-Semibold;"+
                "src: url(SourceSansPro-Semibold.ttf);"+

                "font-family: SourceSansPro-Regular;"+
                "src: url(SourceSansPro-Regular.ttf);"+
                "}"+
                ".title {"+
                "font-family: SourceSansPro-Regular;"+
                "font-size:16px;"+
                "text-align:left;"+
                "color:	#46C1D0;"+
                "text-align: ####TEXT_ALIGN####;"+
                "}"+

                ".description {"+
                "font-family: SourceSansPro-Light;"+
                "text-align:justify;"+
                "font-size:14px;"+
                "color: #000000;"+
                "text-align: ####TEXT_ALIGN####;"+
                "}"+
                "</style>\n"+"</head>"+
                "<body>"+
                "<p class='description'>"+termsDescription+"</p>"+
                "</body>\n</html>"
        var htmlData=pushNotificationDetail

        //  webView.loadData(htmlData,"text/html; charset=utf-8","utf-8")
        webView.loadDataWithBaseURL("file:///android_asset/fonts/",htmlData,"text/html; charset=utf-8", "utf-8", "about:blank")
    }

    fun getSettings()
    {
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(false)
        webView.settings.cacheMode=WebSettings.LOAD_NO_CACHE
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.defaultTextEncodingName = "utf-8"
        webView.settings.loadsImagesAutomatically = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.allowFileAccess = true
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)


        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressDialog.visibility = View.VISIBLE
                if (newProgress == 100)
                {
                    progressDialog.visibility = View.GONE

                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (!CommonFunctions.runMethod.equals("Dev")) {
            if (CommonFunctions.isDeveloperModeEnabled(mContext)) {
                CommonFunctions.showDeviceIsDeveloperPopUp(activity)
            }
        }
    }
}


