package com.mobatia.bisad.activity.school_trips

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.school_trips.adapter.TripInvoiceListAdapter
import com.mobatia.bisad.activity.school_trips.model.TripDetailsResponseModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.ProgressBarDialog
import com.mobatia.bisad.fragment.school_trips.model.TripCategoriesResponseModel
import com.mobatia.bisad.manager.HeaderManager
import com.mobatia.bisad.manager.HeaderManagerNoColorSpace
import com.mobatia.bisad.manager.ItemOffsetDecoration
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.DividerItemDecoration
import com.mobatia.bisad.recyclermanager.RecyclerItemListener
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripInvoiceListingActivity : AppCompatActivity() {
    lateinit var context: Context
    var extras: Bundle? = null
    var tab_type: String? = null
    var invoiceArrayList: ArrayList<TripDetailsResponseModel.TripData.Invoice>? = null

    lateinit var  progressDialogP: ProgressBarDialog
    lateinit var relativeHeader: RelativeLayout
    lateinit var headermanager: HeaderManager
    private val pictureImagePath = ""

    var bannerImageView: ImageView? = null

    //    TextView descriptionTextView;
    var sendEmailImageView: ImageView? = null

    //    RecyclerView tripImagesRecycler;
    lateinit var recyclerViewLayoutManager: LinearLayoutManager

    var categoriesList: ArrayList<TripCategoriesResponseModel.Data>? = null
    lateinit var tripInvoiceListAdapter: TripInvoiceListAdapter
    var contactEmail = ""
    lateinit var back: ImageView
    lateinit var btn_history:android.widget.ImageView
    lateinit var home:android.widget.ImageView

    //    LinearLayout mStudentSpinner;
    lateinit var studentName: TextView
    lateinit var studImg: ImageView
    var stud_id: String? = null
    var studClass = ""
    var orderId = ""
    var stud_img = ""
    var tripStatus = ""
    var paymentToken: String? = null
    var permissionSlip = ""

    var installmentID = ""
    var installmentAmount = ""


    var studentList = ArrayList<String>()
    var tripMainBanner: ImageView? = null
    lateinit var tripImageRecycler: RecyclerView

    var tripID = ""
    var tripName = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_invoice_listing)
        context = this
        initialiseUI()
    }
    private fun initialiseUI() {
        extras = intent.extras
        if (extras != null) {
            tripID = extras!!.getString("tripID")!!
            tripName = extras!!.getString("tripName")!!
        }
        progressDialogP = ProgressBarDialog(context)

        //  TripInvoiceListingActivity.progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader)
        headermanager = HeaderManager(this@TripInvoiceListingActivity, "Trip Categories")
        headermanager.getHeader(relativeHeader, 6)
        back = headermanager.getLeftButton()
        btn_history = headermanager.getRightHistoryImage()
        btn_history.setVisibility(View.INVISIBLE)
        tripImageRecycler = findViewById<RecyclerView>(R.id.tripListRecycler)
        tripImageRecycler.setHasFixedSize(true)
        val spacing = 5 // 50px
        val itemDecoration = ItemOffsetDecoration(context, spacing)
        recyclerViewLayoutManager = LinearLayoutManager(context)
        tripImageRecycler.addItemDecoration(DividerItemDecoration(context!!.resources.getDrawable(R.drawable.list_divider)))
        tripImageRecycler.addItemDecoration(itemDecoration)
        tripImageRecycler.setLayoutManager(recyclerViewLayoutManager)
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back)
        back!!.setOnClickListener {
            CommonFunctions.hideKeyBoard(context)
            finish()
        }
        home = headermanager.getLogoButton()!!
        home.setOnClickListener(View.OnClickListener {
            val `in` = Intent(
                context,
                HomeActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        })

        var internetCheck = InternetCheckClass.isInternetAvailable(context)
        if (internetCheck)
        {
            getTripDetails(tripID)        }
        else{
            InternetCheckClass.showSuccessInternetAlert(context)
        }

        tripImageRecycler.addOnItemTouchListener(
            RecyclerItemListener(context, tripImageRecycler,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        val intent = Intent(
                            context,
                            TripInvoicePrintActivity::class.java
                        )
                        intent.putExtra("title", "$tripName Receipt")
                        intent.putExtra("tab_type", "Trip Invoice")
                        intent.putExtra("orderId", invoiceArrayList!![position].invoiceNumber)
                        intent.putExtra("invoice", invoiceArrayList!![position].invoiceNote)
                        intent.putExtra("amount", invoiceArrayList!![position].paidAmount)
                        intent.putExtra("paidby", invoiceArrayList!![position].firstName)
                        intent.putExtra(
                            "paidDate",
                            CommonFunctions.dateConversionddmmyyyytoddMMYYYY(invoiceArrayList!![position].paymentDate)
                        )
                        intent.putExtra(
                            "tr_no",
                            invoiceArrayList!![position].transactionNumber
                        )
                        intent.putExtra(
                            "payment_type",
                            invoiceArrayList!![position].paymentType
                        )
                        startActivity(intent)
                    }

                    override fun onLongClickItem(v: View?, position: Int) {}
                })
        )
    }

    private fun getTripDetails(tripID: String) {
        progressDialogP.show()
        val paramObject = JsonObject()
        // Log.e("tripID name", tripID);
        paramObject.addProperty("student_id", PreferenceData().getStudentID(context))
        paramObject.addProperty("trip_id", tripID)
        val call: Call<TripDetailsResponseModel> =
            ApiClient.getClient.tripDetail("Bearer " + PreferenceData().getaccesstoken(context),paramObject)
        call.enqueue(object : Callback<TripDetailsResponseModel> {
            override fun onFailure(call: Call<TripDetailsResponseModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<TripDetailsResponseModel>, response: Response<TripDetailsResponseModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    progressDialogP.dismiss()
                    if (response.body()!!.getResponseCode()==100) {
                        if (response.body()!!.getResponse()!!.data!!.invoices!!.size > 0) {
                            invoiceArrayList = response.body()!!.getResponse()!!.data!!.invoices
                        }
                        tripInvoiceListAdapter = TripInvoiceListAdapter(context, invoiceArrayList!!)
                        tripImageRecycler!!.adapter = tripInvoiceListAdapter
                    }

                }
            }

        })

    }
}