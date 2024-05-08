package com.mobatia.bisad.activity.school_trips

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class TripInvoiceListingActivity : AppCompatActivity() {
    var context: Context? = null
    var extras: Bundle? = null
    var tab_type: String? = null
    var invoiceArrayList: ArrayList<TripDetailsResponseModel.TripData.Invoice>? = null

    private val progressDialogP: ProgressBarDialog? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    private val pictureImagePath = ""

    var bannerImageView: ImageView? = null

    //    TextView descriptionTextView;
    var sendEmailImageView: ImageView? = null

    //    RecyclerView tripImagesRecycler;
    var recyclerViewLayoutManager: LinearLayoutManager? = null

    var categoriesList: ArrayList<TripCategoriesResponseModel.Data>? = null
    var tripInvoiceListAdapter: TripInvoiceListAdapter? = null
    var contactEmail = ""
    var back: ImageView? =
        null, var btn_history:android.widget.ImageView? = null, var home:android.widget.ImageView? = null

    //    LinearLayout mStudentSpinner;
    var studentName: TextView? = null
    var studImg: ImageView? = null
    var stud_id: String? = null
    var studClass = ""
    var orderId = ""
    var stud_img = ""
    var tripStatus = ""
    var paymentToken: String? = null
    var permissionSlip = ""

    var installmentID = ""
    var installmentAmount = ""

    var studentsModelArrayList: ArrayList<StudentDataModel.DataItem> =
        ArrayList<StudentDataModel.DataItem>()
    var studentList = ArrayList<String>()
    var tripMainBanner: ImageView? = null
    var tripImageRecycler: RecyclerView? = null
    var tripNameTextView: TextView? = null
    var tripAmountTextView: TextView? = null
    var dateTextView: TextView? = null
    var coordinatorNameTextView: TextView? = null
    var mActivity: Activity = this

    var coordinatorDetails: Button? = null
    var coordinatorPhoneTextView: TextView? = null
    var tripDescriptionTextView: TextView? = null
    var submitIntentionButton: Button? = null
    var submitDetailsButton: Button? = null

    var paymentButton: Button? = null
    var tripStatusTextView: TextView? = null
    var tripID = ""
    var tripName = ""
    private val flag = true
    var multipleInstallmentsArray: ArrayList<TripDetailsResponseModel.InstallmentDetail> =
        ArrayList<TripDetailsResponseModel.InstallmentDetail>()
    var singleInstallmentAmount = ""
    var coodName: String? = null
    var coodPhone:kotlin.String? = null
    var coodEmail:kotlin.String? = null
    var coodWhatsapp:kotlin.String? = null
    var passportFrontURI: Uri? = null
    var passportBackURI: Uri? = null
    var visaFrontURI: Uri? = null
    var visaBackURI: Uri? = null
    var eIDFrontURI: Uri? = null
    var eIDBackURI: Uri? = null
    var passportFrontFile: File? = null
    var PaymentToken = ""
    var OrderRef:kotlin.String? = ""
    var PayUrl:kotlin.String? = ""
    var AuthUrl:kotlin.String? = ""
    var merchantOrderReference:kotlin.String? = ""


    private val arrayList: ArrayList<String?> = ArrayList<Any?>()
    private val VISA_CAMERA_REQUEST = 3

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    private val PICK_IMAGE_FRONT_PASSPORT = 1
    private val PICK_IMAGE_BACK_PASSPORT = 2
    private val PICK_IMAGE_FRONT_VISA = 3
    private val PICK_IMAGE_BACK_VISA = 4

    private val PICK_IMAGE_FRONT_EID = 5
    private val PICK_IMAGE_BACK_EID = 6
    var currentPosition = 0
    var passportURIArray = ArrayList<Uri>()
    var visaURIArray = ArrayList<Uri>()
    var eIDURIArray = ArrayList<Uri>()
    var passportStatus = 0
    var visaStatus = 0
    var eIDStatus = 0
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
        TripInvoiceListingActivity.progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader)
        headermanager = HeaderManager(this@TripInvoiceListingActivity, "Invoices")
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
            AppUtils.hideKeyBoard(context)
            finish()
        }
        home = headermanager.getLogoButton()
        home.setOnClickListener(View.OnClickListener {
            val `in` = Intent(
                context,
                HomeListAppCompatActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        })
        if (AppUtils.checkInternet(context)) {
            getTripDetails(tripID)
        } else {
            AppUtils.showDialogAlertDismiss(
                context as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        tripImageRecycler.addOnItemTouchListener(
            RecyclerItemListener(context, tripImageRecycler,
                object : RecyclerTouchListener() {
                    fun onClickItem(v: View?, position: Int) {
                        val intent = Intent(
                            context,
                            TripInvoicePrintActivity::class.java
                        )
                        intent.putExtra("title", "$tripName Receipt")
                        intent.putExtra("tab_type", "Trip Invoice")
                        intent.putExtra("orderId", invoiceArrayList!![position].getInvoiceNumber())
                        intent.putExtra("invoice", invoiceArrayList!![position].getInvoiceNote())
                        intent.putExtra("amount", invoiceArrayList!![position].getPaidAmount())
                        intent.putExtra("paidby", invoiceArrayList!![position].getFirstName())
                        intent.putExtra(
                            "paidDate",
                            AppUtils.dateConversionddmmyyyytoddMMYYYY(invoiceArrayList!![position].getPaymentDate())
                        )
                        intent.putExtra(
                            "tr_no",
                            invoiceArrayList!![position].getTransactionNumber()
                        )
                        intent.putExtra(
                            "payment_type",
                            invoiceArrayList!![position].getPaymentType()
                        )
                        startActivity(intent)
                    }

                    fun onLongClickItem(v: View?, position: Int) {}
                })
        )
    }

    private fun getTripDetails(tripID: String) {
        TripInvoiceListingActivity.progressDialogP.show()
        val paramObject = JsonObject()
        // Log.e("tripID name", tripID);
        paramObject.addProperty("student_id", PreferenceManager.getStudIdForCCA(context))
        paramObject.addProperty("trip_id", tripID)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripDetailsResponseModel> =
            service.tripDetail("Bearer " + PreferenceManager.getAccessToken(context), paramObject)
        call.enqueue(object : Callback<TripDetailsResponseModel> {
            override fun onResponse(
                call: Call<TripDetailsResponseModel>,
                response: Response<TripDetailsResponseModel>
            ) {
                TripInvoiceListingActivity.progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponse().getStatusCode().equalsIgnoreCase("303")) {
                        if (response.body().getResponse().getData().getInvoices().size() > 0) {
                            invoiceArrayList = response.body().getResponse().getData().getInvoices()
                        }
                        tripInvoiceListAdapter = TripInvoiceListAdapter(context, invoiceArrayList)
                        tripImageRecycler!!.adapter = tripInvoiceListAdapter
                    } else {
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<TripDetailsResponseModel>, t: Throwable) {
                TripInvoiceListingActivity.progressDialogP.dismiss()
                // Log.e("error", t.getLocalizedMessage());
                AppUtils.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })
    }
}