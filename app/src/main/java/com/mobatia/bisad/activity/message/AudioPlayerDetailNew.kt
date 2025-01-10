package com.mobatia.bisad.activity.message

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import com.mobatia.bisad.R

import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
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

private var mediaplayer: MediaPlayer? = null
private var handler2 = Handler()
private var seebbar: SeekBar? = null
class AudioPlayerDetailNew : AppCompatActivity(), Player.EventListener{
    lateinit var mContext: Context
    var id:String=""
    var title:String=""
    var idApi:String=""
    var titleApi:String=""
    var message:String=""
    var url:String=""
    var date:String=""
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    lateinit var playerView: PlayerView
    var playbackPosition: Long = 0
    lateinit var mediaDataSourceFactory: DataSource.Factory
    lateinit var playbutton_audio: ImageView
    lateinit var duration_time: TextView
    lateinit var textcurrent_time: TextView
    var duration: String = ""
    var currenrdr: String = ""

    lateinit var audio_title: String
    lateinit var audio_id: String
    lateinit var audio_updated: String
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var extras: Bundle
    var alert_type: String = ""
    var created_at: String = ""

    var updated_at: String = ""
    var flag: Boolean = true
    lateinit var activity: Activity


    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player_detail)
        mContext=this
        activity=this
        extras = intent.extras!!
        audio_id = extras.getString("audio_id")!!
        audio_title = extras.getString("audio_title")!!
        audio_updated = extras.getString("audio_updated")!!
        sharedprefs= PreferenceData()
        initUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            audiodetails()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }



    }

    fun initUI() {
        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        heading.text = "Notification"
        seebbar = findViewById(R.id.seebbar)
        playbutton_audio = findViewById(R.id.playbutton)
        textcurrent_time = findViewById(R.id.textcurrent_time)
        duration_time = findViewById(R.id.duration_time)
        mediaplayer = MediaPlayer()
        seebbar!!.max = 100
        heading.text = "Notification"
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        playbutton_audio.setOnClickListener {
            if (flag) {

                playbutton_audio.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                mediaplayer!!.start()
            } else {
                playbutton_audio.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                mediaplayer!!.pause()
            }
            flag = !flag
        }

    }

    fun audiodetails() {
        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= MessageDetailApiModel(audio_id)
       // progressDialog.visibility = View.VISIBLE
        val call: Call<MessageDetailModel> = ApiClient(mContext).getClient.notifictaionDetail(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<MessageDetailModel> {
            override fun onFailure(call: Call<MessageDetailModel>, t: Throwable) {

               // progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(context)
            }
            override fun onResponse(call: Call<MessageDetailModel>, response: Response<MessageDetailModel>) {
              //  progressDialog.visibility = View.GONE
                // progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)

                {
                    title = response.body()!!.responseArray.notificationArray.title
                    message = response.body()!!.responseArray.notificationArray.message
                    alert_type = response.body()!!.responseArray.notificationArray.alert_type
                    created_at = response.body()!!.responseArray.notificationArray.created_at
                    updated_at = response.body()!!.responseArray.notificationArray.updated_at
                    url = response.body()!!.responseArray.notificationArray.url
                    if (mediaplayer!!.isPlaying()) {
                        handler2.removeCallbacks(updater)
                        mediaplayer!!.pause()
                        playbutton_audio.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                    } else {
                        mediaplayer!!.start()
                        playbutton_audio.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                        try {
                            updateseekbar()
                        } catch (e: Exception) {

                        }
                    }
                    setUpMediaPlayer(url)
                  //  player.play(url)
                } else if (response.body()!!.status == 116) {
                    AccessTokenClass.getAccessToken(mContext)
                    audiodetails()
                } else {
                    InternetCheckClass.checkApiStatusError(
                        response.body()!!.status,mContext
                    )
                }


            }

        })

    }
    /*fun callMessageDetailAPI()
    {
        val token = PreferenceManager.getUserCode(mContext)
        val studentbody= NotificationDetailApiModel(id,PreferenceManager().getLanguage(mContext!!)!!)
        val call: Call<MessageDetailResponse> = ApiClient.getClient.notificationdetail(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<MessageDetailResponse> {
            override fun onFailure(call: Call<MessageDetailResponse>, t: Throwable) {

                //Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<MessageDetailResponse>, response: Response<MessageDetailResponse>) {

                if (response.body()!!.status==100)
                {
                    idApi=id
                    titleApi=title
                    message=response.body()!!.data.message
                    url=response.body()!!.data.url
                    date=response.body()!!.data.time_stamp
                    if (mediaplayer!!.isPlaying()) {
                        handler2.removeCallbacks(updater)
                        mediaplayer!!.pause()
                        playbutton_audio.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                    } else {
                        mediaplayer!!.start()
                        playbutton_audio.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                        try {
                            updateseekbar()
                        } catch (e: Exception) {

                        }
                    }
                    setUpMediaPlayer(url)
                }
                else if(response.body()!!.status==116)
                {
                    PreferenceManager.setUserCode(mContext,"")
                    PreferenceManager.setUserEmail(mContext,"")
                    val mIntent = Intent(this@AudioNotification, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    mContext.startActivity(mIntent)

                }


            }

        })
    }*/




    fun setUpMediaPlayer(filename: String) {


        try {

            mediaplayer!!.setDataSource(filename)

            mediaplayer!!.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp -> mp.start() })
            mediaplayer!!.prepare()
            duration_time.setText("/" + milliseconds(mediaplayer!!.getDuration().toLong()))
            duration = milliseconds(mediaplayer!!.getDuration().toLong())!!
            /* if(currenrdr.equals(duration))
             {
                 playbutton_audio.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
             }*/
        } catch (exception: Exception) {
            //Toast.makeText(this, "failed to load audio" + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    fun milliseconds(milliscnd: Long): String? {
        var timer = ""
        val secondString: String
        val hour = (milliscnd / (1000 * 60 * 60)).toInt()
        val min = (milliscnd % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val sec = (milliscnd % (1000 * 60 * 60)).toInt() % (1000 * 60) / 1000
        if (hour > 0) {
            timer = "$hour;"
        }
        secondString = if (sec < 10) {
            "0$sec"
        } else {
            "" + sec
        }
        timer = "$timer$min:$secondString"
        return timer
    }
    val updater = Runnable {
        try {
            updateseekbar()
            val currentduration: Long = mediaplayer!!.getCurrentPosition().toLong()
            textcurrent_time.text = (milliseconds(currentduration).toString())
            currenrdr = (milliseconds(currentduration).toString())
        } catch (e: Exception) {

        }


    }
    fun updateseekbar() {

        try {
            seebbar!!.setProgress(
                (mediaplayer!!.getCurrentPosition()
                    .toFloat() / mediaplayer!!.getDuration() * 100).toInt()
            )
            // Log.e("")

            handler2.postDelayed(updater, 1000)
        } catch (e: InterruptedException) {
            e.printStackTrace();
        }

        /* Toast.makeText(this, "successs", Toast.LENGTH_SHORT).show();*/

    }

    override fun onPause() {
        super.onPause()
        mediaplayer!!.pause()
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaplayer!!.stop()
        mediaplayer!!.release()
    }


    override fun onBackPressed() {

        finish()

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




