package com.mobatia.bisad.activity.communication.magazine

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
import com.mobatia.bisad.activity.communication.letter.LetterDetailActivity
import com.mobatia.bisad.activity.communication.letter.adapter.LetterListAdapter
import com.mobatia.bisad.activity.communication.letter.model.LetterResponseListModel
import com.mobatia.bisad.activity.communication.letter.model.LetterResponseModel
import com.mobatia.bisad.activity.communication.magazine.adapter.MagazineListAdapter
import com.mobatia.bisad.activity.communication.magazine.model.MagazineResponseListModel
import com.mobatia.bisad.activity.communication.magazine.model.MagazineResponseModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.pdfviewer.PdfViewer
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MagazineListActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    private lateinit var mNewsletterRecycler: RecyclerView
    private lateinit var progressDialog: RelativeLayout
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    lateinit var newsLetterArrayList :ArrayList<MagazineResponseListModel>
    lateinit var newsLetterShowArrayList :ArrayList<MagazineResponseListModel>
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var newsLetterRecycler: RecyclerView
    var apiCall:Int=0

    var isLoading:Boolean=false
    var stopLoading=false
    var startValue:Int=0
    var limit:Int=20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_newsletter)
        initializeUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            startValue=0
            limit=20
            newsLetterShowArrayList=ArrayList()
            callNewLetterListAPI(startValue,limit)
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
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
        heading.text = "Magazine"
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

                val intent = Intent(mContext, PdfViewer::class.java)
                intent.putExtra("title",newsLetterArrayList.get(position).title)
                intent.putExtra("Url",newsLetterArrayList.get(position).file)
                startActivity(intent)
            }
        })

        newsLetterRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == newsLetterShowArrayList.size - 1) {
                        //bottom of list!
                        if (!stopLoading)
                        {
                            startValue=startValue+limit
                            callNewLetterListAPI(startValue,limit)
                            isLoading = true
                        }

                    }
                }
            }
        })
    }


    fun callNewLetterListAPI(start:Int,limit:Int)
    {
        progressDialog.visibility = View.VISIBLE
        newsLetterArrayList= ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val notificationBody= MessageListApiModel(start,limit)
        val call: Call<MagazineResponseModel> = ApiClient.getClient.magazineList(notificationBody,"Bearer "+token)
        call.enqueue(object : Callback<MagazineResponseModel> {
            override fun onFailure(call: Call<MagazineResponseModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)
            }
            override fun onResponse(call: Call<MagazineResponseModel>, response: Response<MagazineResponseModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    progressDialog.visibility = View.GONE
                    newsLetterArrayList.addAll(response.body()!!.responseArrayList)
                    stopLoading = newsLetterArrayList.size!=20
                    newsLetterShowArrayList.addAll(newsLetterArrayList)

                    if (newsLetterShowArrayList.size>0)
                    {
                        newsLetterRecycler.visibility= View.VISIBLE
                        val newsLetterAdapter = MagazineListAdapter(newsLetterArrayList)
                        newsLetterRecycler.adapter = newsLetterAdapter
                        if(newsLetterShowArrayList.size>20)
                        {
                            newsLetterRecycler.scrollToPosition(start)
                        }

                        isLoading=false
                    }
                    else{
                        newsLetterRecycler.visibility= View.GONE
                        showSuccessAlert(mContext,"No Letters Available.","Alert")
                    }
                }
                else if (response.body()!!.status == 132)
                {
                    showSuccessAlert(mContext,"No Letters Available.","Alert")
                }
                else if (response.body()!!.status == 116) {
                    apiCall=apiCall+1
                    if (apiCall<3)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        callNewLetterListAPI(start,limit)
                    }
                    else{
                        showSuccessAlert(mContext,"Something went wrong","Alert")
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext
                    )
                }


            }

        })
    }

    fun showSuccessAlert(context: Context, message : String, msgHead : String)
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