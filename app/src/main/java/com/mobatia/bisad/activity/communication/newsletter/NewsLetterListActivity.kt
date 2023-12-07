package com.mobatia.bisad.activity.communication.newsletter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.communication.newsletter.adapter.NewsLetterRecyclerAdapter
import com.mobatia.bisad.activity.communication.newsletter.model.NewLetterListDetailModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsLetterListActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    private lateinit var mNewsletterRecycler: RecyclerView
    private lateinit var progressDialog: RelativeLayout
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    lateinit var newsLetterArrayList :ArrayList<NewLetterListDetailModel>
    lateinit var newsLetterShowArrayList :ArrayList<NewLetterListDetailModel>
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var newsLetterRecycler: RecyclerView
    var apiCall:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_newsletter)
        initializeUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            newsLetterArrayList=ArrayList()
            callNewLetterListAPI()
        }
        else
        {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }
    }

    private fun initializeUI() {
        mContext=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressDialog = findViewById(R.id.progressDialog)
        relativeHeader = findViewById(R.id.relativeHeader)
        linearLayoutManager = LinearLayoutManager(mContext)
        newsLetterRecycler = findViewById<RecyclerView>(R.id.newsLetterRecycler)
        newsLetterRecycler.layoutManager = linearLayoutManager
        newsLetterRecycler.itemAnimator = DefaultItemAnimator()
        heading = findViewById(R.id.heading)
        heading.text = "Newsletters"
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        newsLetterRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                val intent =Intent(mContext, NewsLetterDetailActivity::class.java)
                intent.putExtra("id",newsLetterArrayList.get(position).id)
                intent.putExtra("title",newsLetterArrayList.get(position).title)
                startActivity(intent)
            }
        })

    }


    fun callNewLetterListAPI()
    {
        progressDialog.visibility = View.VISIBLE
        newsLetterShowArrayList= ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<NewsLetterListModel> = ApiClient.getClient.newsletters("Bearer "+token)
        call.enqueue(object : Callback<NewsLetterListModel> {
            override fun onFailure(call: Call<NewsLetterListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE

            }
            override fun onResponse(call: Call<NewsLetterListModel>, response: Response<NewsLetterListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    progressDialog.visibility = View.GONE
                    newsLetterArrayList.addAll(response.body()!!.responseArray.campaignsList)
                    if (newsLetterArrayList.size>0)
                    {
                        newsLetterRecycler.visibility=View.VISIBLE
                        val newsLetterAdapter = NewsLetterRecyclerAdapter(newsLetterArrayList)
                        newsLetterRecycler.adapter = newsLetterAdapter

                    }
                    else
                    {
                        newsLetterRecycler.visibility=View.GONE
                        showSuccessAlert(mContext,"Newsletters is not available.","Alert")

                    }
                }
                else if (response.body()!!.status == 132)
                {
                    showSuccessAlert(mContext,"Newsletters is not available.","Alert")
                }
                else if (response.body()!!.status == 116) {
                    apiCall=apiCall+1
                    if (apiCall<3)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        callNewLetterListAPI()
                    }
                    else{
                        showSuccessAlert(mContext,"Something went wrong","Alert")
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status,mContext)
                }


            }

        })
    }

    fun showSuccessAlert(context: Context,message : String,msgHead : String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        iconImageView.setImageResource(R.drawable.exclamationicon)
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()

        }
        dialog.show()
    }

}