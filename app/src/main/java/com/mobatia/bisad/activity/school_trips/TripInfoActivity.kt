package com.mobatia.bisad.activity.school_trips

import android.app.Activity
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
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripInfoActivity : AppCompatActivity() {
    private var mContext: Context? = null
    private val progressDialogP: ProgressBarDialog? = null
    var extras: Bundle? = null
    var tab_type: String? = null
    var relativeHeader: RelativeLayout? = null
    var mStudentSpinner: LinearLayout? = null
    var studImg: ImageView? = null
    var studName: TextView? = null
    var mnewsLetterListView: RecyclerView? = null
    var studentsModelArrayList: ArrayList<TeamModel>? = null
    var textViewYear: TextView? = null
    var stud_id = ""
    var stud_class = ""
    var stud_name = ""
    var stud_img = ""
    var section = ""
    private var mListViewArray: ArrayList<CanteenInfoModel>? = null
    var customStaffDirectoryAdapter: CanteenInfoRecyclerAdapter? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        mContext = this
        initUI()
        if (AppUtils.checkInternet(mContext)) {
            getList()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }
    private fun initUI() {
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")
        }
        TripInfoActivity.progressDialogP = ProgressBarDialog(mContext, R.drawable.spinner)
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        mStudentSpinner = findViewById<View>(R.id.studentSpinner) as LinearLayout
        studImg = findViewById<View>(R.id.imagicon) as ImageView
        studName = findViewById<View>(R.id.studentName) as TextView
        textViewYear = findViewById<View>(R.id.textViewYear) as TextView
        mnewsLetterListView = findViewById<View>(R.id.mnewsLetterListView) as RecyclerView
        mnewsLetterListView!!.setHasFixedSize(true)
        mnewsLetterListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider_teal)))
        headermanager = HeaderManager(this@TripInfoActivity, tab_type)
        headermanager.getHeader(relativeHeader, 0)
        back = headermanager.getLeftButton()
        headermanager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            AppUtils.hideKeyBoard(mContext)
            finish()
        }
        home = headermanager.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(
                mContext,
                HomeListAppCompatActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        mnewsLetterListView!!.layoutManager = llm
        mnewsLetterListView!!.addOnItemTouchListener(
            RecyclerItemListener(
                applicationContext, mnewsLetterListView,
                object : RecyclerTouchListener() {
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
        )
    }

    fun getList() {
        TripInfoActivity.progressDialogP.show()
        mListViewArray = java.util.ArrayList<CanteenInfoModel>()
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<CanteenInfoResponseModel> = service.tripInfo(
            "Bearer " + PreferenceManager.getAccessToken(mContext)
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
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })
    }
}