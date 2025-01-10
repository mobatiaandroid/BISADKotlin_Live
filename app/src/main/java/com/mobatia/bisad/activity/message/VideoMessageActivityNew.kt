package com.mobatia.bisad.activity.message

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
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


class VideoMessageActivityNew : AppCompatActivity() {
    private lateinit var mContext: Context
    private var id: String = ""
    private var title: String = ""
    private var message: String = ""
    private var url: String = ""
    private var date: String = ""
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var backRelative: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btnLeft: ImageView
    private lateinit var heading: TextView
    private lateinit var contentWebView: WebView
    private lateinit var youtubeWebView: WebView
    private lateinit var progressDialog: ProgressBar
    lateinit var activity: Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_message_new)
        mContext = this
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
        setupWebView()
    }

    private fun initUI() {
        progressDialog = findViewById(R.id.progressDialog)
        contentWebView = findViewById(R.id.txtContent)
        youtubeWebView = findViewById(R.id.webView)
        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        btnLeft = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        backRelative = findViewById(R.id.backRelative)

        heading.text = "Notification"
        youtubeWebView.settings.javaScriptEnabled = true
        youtubeWebView.settings.pluginState = PluginState.ON
        youtubeWebView.settings.builtInZoomControls = false
        youtubeWebView.settings.displayZoomControls = true
        youtubeWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressDialog.visibility = View.VISIBLE
                if (newProgress == 100) {
                    progressDialog.visibility = View.GONE

                }
            }
        }
        btnLeft.setOnClickListener { finish() }
        backRelative.setOnClickListener { finish() }
        logoClickImgView.setOnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun callMessageDetailAPI() {
        val token = sharedprefs.getaccesstoken(mContext)
        val studentBody = MessageDetailApiModel(id)
        val call: Call<MessageDetailModel> = ApiClient(mContext).getClient.notifictaionDetail(studentBody, "Bearer $token")

        call.enqueue(object : Callback<MessageDetailModel> {
            override fun onFailure(call: Call<MessageDetailModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<MessageDetailModel>, response: Response<MessageDetailModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()?.status == 100) {
                    val responseBody = response.body()!!.responseArray.notificationArray
                    message = responseBody.message
                    url = responseBody.url
                    date = responseBody.created_at

                    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val outputFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val outputFormatDate: DateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

                    val dateObj: Date = inputFormat.parse(date)!!
                    val outputTimeStr: String = outputFormat.format(dateObj)
                    val outputDateStr: String = outputFormatDate.format(dateObj)

                    val pushNotificationDetail = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                @font-face {
                                    font-family: SourceSansPro-Semibold;
                                    src: url(SourceSansPro-Semibold.ttf);
                                    font-family: SourceSansPro-Regular;
                                    src: url(SourceSansPro-Regular.ttf);
                                }
                                .date {
                                    font-family: SourceSansPro-Regular;
                                    font-size:12px;
                                    text-align:right;
                                    color: #908C86;
                                    text-align: ####TEXT_ALIGN####;
                                }
                                .title {
                                    font-family: SourceSansPro-Semibold;
                                    font-size:16px;
                                    text-align:left;
                                    color: #46C1D0;
                                    text-align: ####TEXT_ALIGN####;
                                }
                                .description {
                                    font-family: SourceSansPro-Semibold;
                                    text-align:justify;
                                    font-size:14px;
                                    color: #000000;
                                    text-align: ####TEXT_ALIGN####;
                                }
                            </style>
                        </head>
                        <body>
                            <p class='title'>$title</p>
                            <p class='date'>$outputDateStr $outputTimeStr</p>
                            <hr>
                            <p class='description'>$message</p>
                        </body>
                        </html>
                    """.trimIndent()

                    contentWebView.loadDataWithBaseURL("file:///android_asset/fonts/", pushNotificationDetail, "text/html; charset=utf-8", "utf-8", "about:blank")
                    var frameVideo = "<html>" + "<br><iframe width=\"320\" height=\"250\" src=\""
                    var url_Video =
                        frameVideo + url + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
                    var urlYoutube = url_Video.replace("watch?v=", "embed/")
                    youtubeWebView.loadData(urlYoutube, "text/html", "utf-8")
                }  else if (response.body()!!.status == 116) {
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

    private fun setupWebView() {
        contentWebView.settings.apply {
            javaScriptEnabled = true
            setSupportZoom(false)
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            databaseEnabled = true
            defaultTextEncodingName = "utf-8"
            loadsImagesAutomatically = true
            allowFileAccess = true
        }
        contentWebView.setBackgroundColor(Color.TRANSPARENT)
        contentWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)

        contentWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressDialog.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
            }
        }
    }
    fun getSettings() {
        youtubeWebView.settings.javaScriptEnabled = true
        youtubeWebView.settings.setSupportZoom(false)
        youtubeWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        youtubeWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        youtubeWebView.settings.domStorageEnabled = true
        youtubeWebView.settings.databaseEnabled = true
        youtubeWebView.settings.defaultTextEncodingName = "utf-8"
        youtubeWebView.settings.loadsImagesAutomatically = true
        youtubeWebView.settings.allowFileAccess = true
        youtubeWebView.setBackgroundColor(Color.TRANSPARENT)
        youtubeWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)


        youtubeWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressDialog.visibility = View.VISIBLE
                if (newProgress == 100) {
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
