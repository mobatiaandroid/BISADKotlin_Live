package com.mobatia.bisad.activity.message

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.home.context
import com.mobatia.bisad.activity.message.model.MessageDetailApiModel
import com.mobatia.bisad.activity.message.model.MessageDetailModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private lateinit var relativeHeader: RelativeLayout
private lateinit var backRelative: RelativeLayout
private lateinit var logoClickImgView: ImageView
private lateinit var btn_left: ImageView
private lateinit var heading: TextView
private lateinit var textcontent: WebView
private lateinit var webView: WebView
private lateinit var proWebView: ProgressBar
class VideoMessageActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    var id:String=""
    var title:String=""
    var idApi:String=""
    var titleApi:String=""
    var message:String=""
    var url:String=""
    var date:String=""
    lateinit var activity: Activity


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_message)
        mContext=this
        activity=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        id=intent.getStringExtra("id").toString()
        title=intent.getStringExtra("title").toString()
        initUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callMessageDetailAPI()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }
        getSettings()

    }
    fun initUI() {
        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        textcontent = findViewById(R.id.txtContent)
        webView = findViewById(R.id.webView)
        proWebView = findViewById(R.id.proWebView)
        backRelative = findViewById(R.id.backRelative)
        textcontent.visibility=View.INVISIBLE
        heading.text = "Messages"
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
//        val aniRotate: Animation =
//            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
//        progressDialog.startAnimation(aniRotate)
    }
    fun callMessageDetailAPI()
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= MessageDetailApiModel(id)
        val call: Call<MessageDetailModel> = ApiClient(mContext).getClient.notifictaionDetail(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<MessageDetailModel> {
            override fun onFailure(call: Call<MessageDetailModel>, t: Throwable) {
//                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(context)
            }
            override fun onResponse(call: Call<MessageDetailModel>, response: Response<MessageDetailModel>) {
//                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    idApi=id
                    titleApi=title
                    message=response.body()!!.responseArray.notificationArray.message
                    url=response.body()!!.responseArray.notificationArray.url
                    date=response.body()!!.responseArray.notificationArray.created_at
                    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
                    val outputFormatdate: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
                    val inputDateStr = date
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    val outputDateStr1: String = outputFormatdate.format(date)
                    var pushNotificationDetail="<!DOCTYPE html>\n"+
                            "<html>\n" +
                            "<head>\n" +
                            "<style>\n" +
                            "\n" +
                            "@font-face {\n" +
                            "font-family: SourceSansPro-Semibold;" +
                            "src: url(SourceSansPro-Semibold.ttf);" +

                            "font-family: SourceSansPro-Regular;" +
                            "src: url(SourceSansPro-Regular.ttf);" +
                            "}" +
                            ".title {" +
                            "font-family: SourceSansPro-Regular;" +
                            "font-size:16px;" +
                            "text-align:left;" +
                            "color:	#46C1D0;" +
                            "text-align: ####TEXT_ALIGN####;" +
                            "}" +
                            ".description {" +
                            "font-family: SourceSansPro-Semibold;" +
                            "text-align:justify;" +
                            "font-size:14px;" +
                            "color: #000000;" +
                            "text-align: ####TEXT_ALIGN####;" +
                            "}" +
                            "</style>\n" + "</head>" +
                            "<body>" +
                            "<p class='title'>"+title

                    pushNotificationDetail=pushNotificationDetail+ "<p class='description'>" +outputDateStr1 +" "+outputDateStr+  "</p>"
                    if (!url.equals(""))
                    {
                        pushNotificationDetail=pushNotificationDetail+"<center><img src='" + url + "'width='100%', height='auto'>"
                    }
                    pushNotificationDetail=pushNotificationDetail+"</body>\n</html>"
                    webView.webViewClient = HelloWebViewClient()
                    webView.settings.javaScriptEnabled = true
                    webView.settings.pluginState = PluginState.ON
                    webView.settings.builtInZoomControls = false
                    webView.settings.displayZoomControls = true
                    webView.webViewClient = HelloWebViewClient()
                    textcontent.settings.javaScriptEnabled = true
                    textcontent.settings.pluginState = PluginState.ON
                    textcontent.settings.builtInZoomControls = false
                    textcontent.settings.displayZoomControls = true
                    textcontent.loadData(
                        pushNotificationDetail,
                        "text/html; charset=utf-8",
                        "utf-8"
                    )
                    var frameVideo= "<html>" + "<br><iframe width=\"320\" height=\"250\" src=\""
                    var url_Video= frameVideo+url+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
                    var urlYoutube=url_Video.replace("watch?v=", "embed/")
                    webView.loadData(urlYoutube, "text/html", "utf-8")
                    proWebView.visibility = View.VISIBLE

                }

                else if (response.body()!!.status == 116) {
                    AccessTokenClass.getAccessToken(mContext)
                    callMessageDetailAPI()
                } else {
                    InternetCheckClass.checkApiStatusError(
                        response.body()!!.status,mContext
                    )
                }
            }

        })
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
                proWebView.visibility = View.VISIBLE
                if (newProgress == 100)
                {
                    proWebView.visibility = View.GONE

                }
            }
        }
    }

    private class HelloWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            proWebView.visibility = View.GONE
            textcontent.visibility = View.VISIBLE
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