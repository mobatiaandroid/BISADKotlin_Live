package com.mobatia.bisad.activity.school_trips

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.payment.adapter.PaymentInfo_adapter
import com.mobatia.bisad.activity.payment.model.InfoPaymentModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.ProgressBarDialog
import com.mobatia.bisad.manager.HeaderManager
import com.mobatia.bisad.manager.HeaderManagerNoColorSpace
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.DividerItemDecoration
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripInfoActivity : AppCompatActivity() {
     lateinit var mContext: Context
    lateinit var  progressDialogP: ProgressBarDialog
    var extras: Bundle? = null
    var tab_type: String? = null
    lateinit var relativeHeader: RelativeLayout
    lateinit var mStudentSpinner: LinearLayout
    var studImg: ImageView? = null
    var studName: TextView? = null
    lateinit var mnewsLetterListView: RecyclerView
    lateinit var textViewYear: TextView
    var stud_id = ""
    var stud_class = ""
    var stud_name = ""
    var stud_img = ""
    var section = ""
   // private var mListViewArray: ArrayList<CanteenInfoModel>? = null
    var customStaffDirectoryAdapter: PaymentInfo_adapter? = null
  //  lateinit var headermanager: HeaderManager
    var back: ImageView? = null
    var home: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canteen_information)
        mContext = this
        initUI()

        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            getList()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }


    }
    private fun initUI() {
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")
        }
        progressDialogP = ProgressBarDialog(mContext)
        home = findViewById(R.id.logoclick)
        back = findViewById(R.id.back)

        textViewYear = findViewById<View>(R.id.textViewtitle) as TextView
        mnewsLetterListView = findViewById<View>(R.id.canteen_info_list) as RecyclerView
        mnewsLetterListView!!.setHasFixedSize(true)
        mnewsLetterListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider)))
      //  headermanager = HeaderManager(this@TripInfoActivity, "Trip Information")
      //  headermanager.getHeader(relativeHeader, 0)
      //  back = headermanager.getLeftButton()
    /*    headermanager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )*/
        textViewYear.setText("Trip Information")
        back!!.setOnClickListener {
            CommonFunctions.hideKeyBoard(mContext)
            finish()
        }
      //  home = headermanager.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(
                mContext,
                HomeActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        mnewsLetterListView!!.layoutManager = llm
      /*  mnewsLetterListView!!.addOnItemTouchListener(
            RecyclerItemListener(
                applicationContext, mnewsLetterListView,
                object : RecyclerItemListener.RecyclerTouchListener() {
                    fun onClickItem(v: View?, position: Int) {
// checking
                        if (mListViewArray!![position].getFile().endsWith(".pdf")) {
                            val intent = Intent(
                                mContext,
                                PdfReaderActivity::class.java
                            )
                            intent.putExtra("pdf_url", mListViewArray[position].getFile())
                            startActivity(intent)
                        } else {
                            val intent = Intent(
                                mContext,
                                LoadUrlWebViewActivity::class.java
                            )
                            intent.putExtra("url", mListViewArray[position].getFile())
                            intent.putExtra("tab_type", mListViewArray[position].getTitle())
                            mContext!!.startActivity(intent)
                        }
                    }

                    fun onLongClickItem(v: View?, position: Int) {}
                })
        )*/
    }

    fun getList() {
        progressDialogP.show()
      //  mListViewArray = java.util.ArrayList<CanteenInfoModel>()

        val call: Call<InfoPaymentModel> = ApiClient.getClient.tripInformation(
            "Bearer "+PreferenceData().getaccesstoken(mContext))
        call.enqueue(object : Callback<InfoPaymentModel> {
            override fun onFailure(call: Call<InfoPaymentModel>, t: Throwable) {
                progressDialogP.hide()
                CommonFunctions.faliurepopup(mContext)
            }
            override fun onResponse(call: Call<InfoPaymentModel>, response: Response<InfoPaymentModel>) {
                val responsedata = response.body()
                progressDialogP.hide()
                if (responsedata!!.status==100) {

                    if(response.body()!!.responseArray.information.size>0)
                    {
                        mnewsLetterListView.layoutManager = LinearLayoutManager(mContext)
                        mnewsLetterListView.adapter = PaymentInfo_adapter(response.body()!!.responseArray.information, mContext)
                    }



                }else if (response.body()!!.status == 116) {

                } else {
                    if (response.body()!!.status == 103) {

                        //validation check error
                    } else {
                        //check status code checks
                        InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                    }
                }
            }

        })

        /*val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<CanteenInfoResponseModel> = service.tripInfo(
            "Bearer " +
        )
        call.enqueue(object : Callback<CanteenInfoResponseModel> {
            override fun onResponse(
                call: Call<CanteenInfoResponseModel>,
                response: Response<CanteenInfoResponseModel>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: CanteenInfoResponseModel? = response.body()
                    val responsCode: String = apiResponse.getResponsecode()
                    if (responsCode == "200") {
                        val statuscode: String = apiResponse.getResponse().getStatuscode()
                        val responseData: CanteenInfoResponseModel.ResponseData =
                            apiResponse.getResponse()
                        if (statuscode == "303") {
                            TripInfoActivity.progressDialogP.hide()
                            if (response.body().getResponse().getData().size() > 0) {
                                for (i in 0 until apiResponse.getResponse().getData().size()) {
                                    val item: CanteenInfoModel =
                                        apiResponse.getResponse().getData().get(i)
                                    val gson = Gson()
                                    val eventJson = gson.toJson(item)
                                    try {
                                        val jsonObject = JSONObject(eventJson)
                                        mListViewArray!!.add(getSearchValues(jsonObject))
                                        //  Log.e("array", String.valueOf(mCCAmodelArrayList));
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                                customStaffDirectoryAdapter =
                                    CanteenInfoRecyclerAdapter(mContext, mListViewArray)
                                mnewsLetterListView!!.adapter = customStaffDirectoryAdapter
                            } else {
                                AppUtils.showDialogAlertDismiss(
                                    mContext as Activity?,
                                    "Alert",
                                    "No data available.",
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            }
                        }
                    } else if (responsCode.equals(
                            RESPONSE_ACCESSTOKEN_MISSING,
                            ignoreCase = true
                        ) ||
                        responsCode.equals(RESPONSE_ACCESSTOKEN_EXPIRED, ignoreCase = true) ||
                        responsCode.equals(RESPONSE_INVALID_TOKEN, ignoreCase = true)
                    ) {
                        AppUtils.postInitParam(mContext, object : GetAccessTokenInterface() {
                            val accessToken: Unit
                                get() {}
                        })
                        getList()
                    } else if (responsCode == RESPONSE_ERROR) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    TripInfoActivity.progressDialogP.hide()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Alert",
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<CanteenInfoResponseModel>, t: Throwable) {
                TripInfoActivity.progressDialogP.hide()
                CommonFunctions.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })*/
    }
}