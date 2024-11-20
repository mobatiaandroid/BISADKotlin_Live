package com.mobatia.bisad.activity.school_trips

import android.Manifest
import android.R.attr.path
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenApiModel
import com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenModel
import com.mobatia.bisad.activity.school_trips.adapter.ChoicePreferenceAdapter
import com.mobatia.bisad.activity.school_trips.adapter.ImagePagerDrawableAdapter
import com.mobatia.bisad.activity.school_trips.adapter.TripImageAdapter
import com.mobatia.bisad.activity.school_trips.adapter.TripsCategoryAdapter
import com.mobatia.bisad.activity.school_trips.model.ChoicePreferenceModel
import com.mobatia.bisad.activity.school_trips.model.PaymentGatewayApiModelTrip
import com.mobatia.bisad.activity.school_trips.model.SubmitDocResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripChoicePreferenceResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripConsentResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripCountResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripDetailsResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripDocumentSubmitResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripPaymentInitiateResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripPaymentSubmitModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.GeneralSubmitResponseModel
import com.mobatia.bisad.constants.GridSpacingItemDecoration
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.ProgressBarDialog
import com.mobatia.bisad.fragment.school_trips.model.TripCategoriesResponseModel
import com.mobatia.bisad.manager.HeaderManager
import com.mobatia.bisad.manager.ItemOffsetDecoration
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.DividerItemDecoration
import com.mobatia.bisad.recyclermanager.RecyclerItemListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import payment.sdk.android.PaymentClient
import payment.sdk.android.SDKConfig
import payment.sdk.android.cardpayment.CardPaymentData
import payment.sdk.android.cardpayment.CardPaymentRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Arrays
import java.util.Timer
import java.util.TimerTask
import kotlin.properties.Delegates


class TripDetailActivity : AppCompatActivity() ,ChoicePreferenceAdapter.OnItemSelectedListener{
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    private val PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE = 2
    private val REQUEST_PERMISSION_EXTERNAL_STORAGE = 102
    private val VISA_CAMERA_REQUEST = 3
    private val PICK_IMAGE_FRONT_PASSPORT = 1
    private val PICK_IMAGE_BACK_PASSPORT = 2
    private val PICK_IMAGE_FRONT_VISA = 3
    private val PICK_IMAGE_BACK_VISA = 4
    private val PICK_IMAGE_FRONT_EID = 5
    private val PICK_IMAGE_BACK_EID = 6

    //    LinearLayout mStudentSpinner;
    var studentName: TextView? = null
    var studImg: ImageView? = null
    var stud_img = ""

    private lateinit var progressDialogP: ProgressBarDialog
    private val pictureImagePath = ""
    lateinit var context: Context
    var extras: Bundle? = null
    var tab_type: String? = null
    lateinit var relativeHeader: RelativeLayout
    lateinit var headermanager: HeaderManager
    var bannerImageView: ImageView? = null

    //    TextView descriptionTextView;
    var sendEmailImageView: ImageView? = null

    //    RecyclerView tripImagesRecycler;
    var recyclerViewLayoutManager: LinearLayoutManager? = null
    var categoriesList: ArrayList<TripCategoriesResponseModel.Data>? = null
    var tripsCategoryAdapter: TripsCategoryAdapter? = null
    var contactEmail = ""
    var back: ImageView? = null
    lateinit var btn_history:android.widget.ImageView
    lateinit var home:android.widget.ImageView
    var stud_id: String? = null
    var selectedChoice = ""
    var studClass = ""
    var orderId = ""
    var tripStatus =0
    var acessToken: String? = null
    var permissionSlip = ""
    var studentList = ArrayList<String>()
    lateinit var tripMainBanner: ViewPager
    lateinit var tripImageRecycler: RecyclerView
    lateinit var tripNameTextView: TextView
    lateinit var tripAmountTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var coordinatorNameTextView: TextView
    var mActivity: Activity = this
    lateinit var coordinatorDetails: TextView
    lateinit var coordinatorPhoneTextView: TextView
    lateinit var tripDescriptionTextView: TextView
    lateinit var submitIntentionButton: Button
    lateinit var submitDetailsButton: Button
    lateinit var viewInvoice: Button
    lateinit var paymentButton: Button
    lateinit var tripStatusTextView: TextView
    var tripID = ""
    var tripName = ""
    var currentPage = 0
    var multipleInstallmentsArray: ArrayList<TripDetailsResponseModel.InstallmentDetail> =
        ArrayList<TripDetailsResponseModel.InstallmentDetail>()
    var singleInstallmentAmount = ""
    var coodName: String? = null
    var coodPhone:kotlin.String? = null
    var coodEmail:kotlin.String? = null
    var coodWhatsapp:kotlin.String? = null
    lateinit var medicalquestion : String

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
    var merchantOrderReference:kotlin.String = ""
    var tripChoiceExceed = ""
    var tripPaymentExceed = ""
    var invoiceArrayList: ArrayList<TripDetailsResponseModel.TripData.Invoice>? = null
    var currentPosition = 0
    var passportURIArray = ArrayList<Uri>()
    var visaURIArray = ArrayList<Uri>()
    var eIDURIArray = ArrayList<Uri>()
    lateinit var imagesArray: ArrayList<String>
    var passportStatus = 0
    var visaStatus = 0
    var eIDStatus = 0
    var permissionStatus = 0
    var medicalconsentstatus = 0


    var tripImageAdapter: TripImageAdapter? = null
    var tripQuestion: String? = null
    var tripType = ""
    var permissionsRequiredCalendar = arrayOf(
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR
    )
    var permissionsRequiredExternalStorage = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var permissionsRequiredLocation = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    var choicePreferenceArray: ArrayList<String> = ArrayList()
    var choicePreferenceSorted: ArrayList<ChoicePreferenceModel> =
        ArrayList<ChoicePreferenceModel>()
    private var permissionFlag = true
    private var studentDetailsFLag = true
    private var passportDetailsFLag = true
    private var visaDetailFlag = true
    private var eIDDetailFLag = true
    private var medicalconsentFlag = true
    private var externalStorageToSettings = false
    private val calendarPermissionStatus: SharedPreferences? = null
    private var externalStoragePermissionStatus: SharedPreferences? = null
    private val locationPermissionStatus: SharedPreferences? = null

    var passportRequired by Delegates.notNull<Int>()
    var visaRequired by Delegates.notNull<Int>()
    var eIDRequired by Delegates.notNull<Int>()
    var consentRequired by Delegates.notNull<Int>()
    var medicalconsentRequired by Delegates.notNull<Int>()
    lateinit var chooseFilenameVisa:TextView
    lateinit var chooseFilenameEIDBack:TextView
    lateinit var chooseFilenameEIDFront:TextView
    lateinit var chooseFilenamePassportBack:TextView
    lateinit var chooseFilenamePassportFront:TextView
    var permissions = arrayOf(
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.CAMERA
    )
    val MULTIPLE_PERMISSIONS = 10
    lateinit var mProgressRelLayout: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_detail)
        context = this
        externalStoragePermissionStatus =
            context.getSharedPreferences("externalStoragePermissionStatus", MODE_PRIVATE)
        Log.e("version", Build.VERSION.SDK_INT.toString())
        if (Build.VERSION.SDK_INT >= 33) {

            /*  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                  != PackageManager.PERMISSION_GRANTED
              ) {
                  // Permission is not granted, request it
                  requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
              } else {
                  // Permission is granted, load your WebView content here
                 // loadWebViewContent()
              }*/
            CheckPermission()
        }
        initialiseUI()
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
                    imagesArray = java.util.ArrayList()
                    if (response.body()!!.getResponseCode()==100) {
                        if (response.body()!!.getResponse()!!.data!!.tripImage!!.size > 0) {
                            Log.e("Trip_Image",
                                response.body()!!.getResponse()!!.data!!.tripImage!!.size.toString()
                            )
//                            Glide.with(context).load(AppUtils.replace(response.body().getResponse().getData().getTripImage().get(0))).placeholder(R.drawable.default_banner).into(tripMainBanner);
                            for (i in 0 until response.body()!!.getResponse()!!.data!!.tripImage!!.size) {
                                imagesArray.add(
                                    response.body()!!.getResponse()!!.data!!.tripImage!!.get(i))
                            }
                            tripMainBanner!!.adapter =
                                ImagePagerDrawableAdapter(imagesArray, context)
                            tripImageAdapter = TripImageAdapter(context, imagesArray)
                            tripImageRecycler!!.adapter = tripImageAdapter
                        } else {
//                            if (response.body().getResponse().getData().getTripImage().size() > 0) {
//                                Glide.with(cofntext).load(AppUtils.replace(response.body().getResponse().getData().getTripImage().get(0))).placeholder(R.drawable.default_banner).into(tripMainBanner);
//
//                            }
                        }
                        passportRequired =
                            response.body()!!.getResponse()!!.data!!.documents_required!!.passport_doc
                        visaRequired =
                            response.body()!!.getResponse()!!.data!!.documents_required!!.visa_doc
                        eIDRequired =
                            response.body()!!.getResponse()!!.data!!.documents_required!!.emirates_doc
                        consentRequired =
                            response.body()!!.getResponse()!!.data!!.documents_required!!.consent_doc
                        medicalconsentRequired =
                            response.body()!!.getResponse()!!.data!!.documents_required!!.consent_doc
                      //  tripType = response.body()!!.getResponse()!!.data!!.trip_type!!
                        tripChoiceExceed = response.body()!!.getResponse()!!.trip_exceed!!
                        tripPaymentExceed = response.body()!!.getResponse()!!.no_of_trips_exceed!!
                        if (imagesArray != null) {
                            val handler = Handler()
                            val Update = Runnable {
                                if (currentPage == imagesArray!!.size) {
                                    currentPage = 0
                                    tripMainBanner!!.setCurrentItem(currentPage, false)
                                } else {
                                    tripMainBanner!!.setCurrentItem(currentPage++, true)
                                }
                            }
                            val swipeTimer = Timer()
                            swipeTimer.schedule(object : TimerTask() {
                                override fun run() {
                                    handler.post(Update)
                                }
                            }, 100, 3000)
                        }
                        if (imagesArray!!.size > 0) {
                            tripMainBanner!!.adapter =
                                ImagePagerDrawableAdapter(imagesArray!!, context)
                        } else {
                            //CustomStatusDialog();
//											bannerImagePager.setBackgroundResource(R.drawable.banner);
                            tripMainBanner!!.setBackgroundResource(R.drawable.default_banner)
                            //											Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                        }
                        //                        tripQuestion = response.body().getResponse().getData().q
                        tripNameTextView.setText(
                            response.body()!!.getResponse()!!.data!!.tripName
                        )
                        tripAmountTextView!!.text =
                            "Trip Amount : " + response.body()!!.getResponse()!!.data!!.totalPrice + " AED"
                        dateTextView!!.text =
                            "Trip Date : " + CommonFunctions.dateParsingyyyyMMddToDdMmmYyyy(
                                response.body()!!.getResponse()!!.data!!.tripDate
                            ) + " To " + CommonFunctions.dateParsingyyyyMMddToDdMmmYyyy(
                                response.body()!!.getResponse()!!.data!!.tripEndDate
                            )
                        coordinatorNameTextView.setText(
                            response.body()!!.getResponse()!!.data!!.coordinatorName
                        )
                        coodName = response.body()!!.getResponse()!!.data!!.coordinatorName
                        coodPhone = response.body()!!.getResponse()!!.data!!.coordinatorPhone
                        coodEmail = response.body()!!.getResponse()!!.data!!.coordinatorEmail
                        coodWhatsapp =
                            response.body()!!.getResponse()!!.data!!.coordinatorWhatsApp
                        medicalquestion = response.body()!!.getResponse()!!.data!!.medicalconsentquestion!!

                        passportStatus =
                            response.body()!!.getResponse()!!.data!!.documentUploadStatus!!.passportStatus
                        visaStatus =
                            response.body()!!.getResponse()!!.data!!.documentUploadStatus!!.visaStatus
                        eIDStatus =
                            response.body()!!.getResponse()!!.data!!.documentUploadStatus!!.emiratesStatus
                        permissionStatus =
                            response.body()!!.getResponse()!!.data!!.documentUploadStatus!!.consentStatus
                        medicalconsentstatus =
                            response.body()!!.getResponse()!!.data!!.documentUploadStatus!!.medicalconsentStatus
                        //                        coordinatorDetails.setText(response.body().getResponse().getData().getCoordinatorEmail());
//                        coordinatorPhoneTextView.setText(response.body().getResponse().getData().getCoordinatorPhone());
//                        tripDescriptionTextView.setText(response.body().getResponse().getData().getDescription());
                        tripDescriptionTextView.setClickable(true);
                        tripDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
                        tripDescriptionTextView!!.text =
                            Html.fromHtml(response.body()!!.getResponse()!!.data!!.description)
                        headermanager = HeaderManager(
                            this@TripDetailActivity,
                            "Trip Categories"
                        )
                        if (response.body()!!.getResponse()!!.data!!.installmentDetails!!.size > 0
                        ) {
                            multipleInstallmentsArray = response.body()!!.getResponse()!!.data!!.installmentDetails!!
                        }
                        singleInstallmentAmount =
                            response.body()!!.getResponse()!!.data!!.totalPrice!!
                        if (response.body()!!.getResponse()!!.data!!.invoices!!.size > 0) {
                            invoiceArrayList = response.body()!!.getResponse()!!.data!!.invoices
                        }
                        tripStatus = response.body()!!.getResponse()!!.data!!.tripStatus!!
                        if (response.body()!!.getResponse()!!.data!!.trip_type.equals("international"))
                        {
                            if (tripStatus == 0) {
                                submitIntentionButton.visibility = View.VISIBLE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 1) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.VISIBLE
                                tripStatusTextView.text = getString(R.string.waiting_for_approval)
                            } else if (tripStatus == 2) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 3) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.VISIBLE
                                submitDetailsButton.setText(getString(R.string.trip_status_5))
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 4) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.VISIBLE
                                tripStatusTextView.text = getString(R.string.trip_cancelled)
                            } else if (tripStatus == 5) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.VISIBLE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 6) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.VISIBLE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 7) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                viewInvoice.visibility = View.VISIBLE
                                tripStatusTextView.visibility = View.GONE
                            } else {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.GONE
                            }
                        }else if (response.body()!!.getResponse()!!.data!!.trip_type.equals("domestic"))
                        {
                            if (tripStatus == 0) {
                                submitIntentionButton.visibility = View.VISIBLE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 1) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.VISIBLE
                                tripStatusTextView.text = getString(R.string.waiting_for_approval)
                            } else if (tripStatus == 2) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 3) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.VISIBLE
                                submitDetailsButton.setText(getString(R.string.trip_status_0))
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 4) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.VISIBLE
                                tripStatusTextView.text = getString(R.string.trip_cancelled)
                            } else if (tripStatus == 5) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.VISIBLE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 6) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.VISIBLE
                                tripStatusTextView.visibility = View.GONE
                            } else if (tripStatus == 7) {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                viewInvoice.visibility = View.VISIBLE
                                tripStatusTextView.visibility = View.GONE
                            } else {
                                submitIntentionButton.visibility = View.GONE
                                submitDetailsButton.visibility = View.GONE
                                paymentButton.visibility = View.GONE
                                tripStatusTextView.visibility = View.GONE
                            }
                        }
                        else{

                        }
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })



    }
    private fun initialiseUI() {
        mProgressRelLayout=findViewById(R.id.progressDialog)

        extras = intent.extras
        if (extras != null) {
            tripID = extras!!.getString("tripID")!!
            tripName = extras!!.getString("tripName")!!
        }
        val fixedLength = 2
        passportURIArray = java.util.ArrayList(fixedLength)
        for (i in 0 until fixedLength) {
            passportURIArray.add(Uri.EMPTY)
        }
        visaURIArray = java.util.ArrayList(fixedLength)
        for (i in 0 until fixedLength) {
            visaURIArray.add(Uri.EMPTY)
        }
        eIDURIArray = java.util.ArrayList(fixedLength)
        for (i in 0 until fixedLength) {
            eIDURIArray.add(Uri.EMPTY)
        }

        progressDialogP = ProgressBarDialog(context)
        tripImageRecycler = findViewById<RecyclerView>(R.id.tripImageRecycler)
        tripMainBanner = findViewById<ViewPager>(R.id.tripMainImage)
        tripNameTextView = findViewById<TextView>(R.id.tripNameTextView)
        tripAmountTextView = findViewById<TextView>(R.id.tripAmountTextView)
        dateTextView = findViewById<TextView>(R.id.dateTextView)
        tripImageRecycler = findViewById<RecyclerView>(R.id.tripImageRecycler)
        coordinatorNameTextView = findViewById<TextView>(R.id.coordinatorNameTextView)
        coordinatorDetails = findViewById<TextView>(R.id.coordinatorDetails)
        tripDescriptionTextView = findViewById<TextView>(R.id.tripDescriptionTextView)
        submitIntentionButton = findViewById<Button>(R.id.submitIntentionButton)
        submitDetailsButton = findViewById<Button>(R.id.submitDetailsButton)
        tripStatusTextView = findViewById<TextView>(R.id.tripStatusTextView)
        viewInvoice = findViewById<Button>(R.id.viewInvoice)
        //        descriptionTextView = findViewById(R.id.tripDescriptionTextView);
        paymentButton = findViewById<Button>(R.id.paymentButton)
        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader)
        headermanager = HeaderManager(this@TripDetailActivity, tripName)
        headermanager.getHeader(relativeHeader, 6)
        back = headermanager.getLeftButton()
        btn_history = headermanager.getRightHistoryImage()
        btn_history!!.setVisibility(View.INVISIBLE)
        tripImageRecycler.setHasFixedSize(true)
        val spacing = 5 // 50px
        val itemDecoration = ItemOffsetDecoration(context, spacing)
        recyclerViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tripImageRecycler.addItemDecoration(DividerItemDecoration(context!!.resources.getDrawable(R.drawable.list_divider)))
        tripImageRecycler.addItemDecoration(itemDecoration)
        tripImageRecycler.addOnItemTouchListener(
            RecyclerItemListener(
                context,
                tripImageRecycler,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        val intent = Intent(
                            context,
                            TripImagesViewActivity::class.java
                        )
                        intent.putExtra("photo_array", imagesArray)
                        intent.putExtra("pos", position)
                        startActivity(intent)
                    }

                    override fun onLongClickItem(v: View?, position: Int) {}
                })
        )
        getChoicePreferenceArrayList()
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
        submitIntentionButton.setOnClickListener(View.OnClickListener {
            if (tripChoiceExceed.equals("", ignoreCase = true)) {
                showIntentionPopUp()
            } else {
                CommonFunctions.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    "You cannot submit any more intentions, as you have already reached your limit.",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })
        submitDetailsButton.setOnClickListener(View.OnClickListener {
            checkTripCount(tripID
            ) { isTripCountEmpty: Boolean ->
                if (isTripCountEmpty) {
                    if (Build.VERSION.SDK_INT > 30) {
                        showDocumentSubmissionPopUp()
                    } else {
                        if (ActivityCompat.checkSelfPermission(
                                context!!,
                                permissionsRequiredExternalStorage[0]
                            ) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(
                                context!!,
                                permissionsRequiredExternalStorage[1]
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    (context as Activity?)!!,
                                    permissionsRequiredExternalStorage[0]
                                )
                                || ActivityCompat.shouldShowRequestPermissionRationale(
                                    (context as Activity?)!!,
                                    permissionsRequiredExternalStorage[1]
                                )
                            ) {
                                //Show information about why you need the permission
                                val builder =
                                    AlertDialog.Builder(
                                        context!!
                                    )
                                builder.setTitle("Need Storage Permission")
                                builder.setMessage("This module needs Storage permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    ActivityCompat.requestPermissions(
                                        (context as Activity?)!!,
                                        permissionsRequiredExternalStorage,
                                       PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE
                                    )
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which -> dialog.cancel() }
                                builder.show()
                            } else if (externalStoragePermissionStatus!!.getBoolean(
                                    permissionsRequiredExternalStorage[0],
                                    false
                                )
                            ) {
                                //Previously Permission Request was cancelled with 'Dont Ask Again',
                                // Redirect to Settings after showing information about why you need the permission
                                val builder =
                                    AlertDialog.Builder(
                                        context!!
                                    )
                                builder.setTitle("Need Storage Permission")
                                builder.setMessage("This module needs Storage permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    externalStorageToSettings = true
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri =
                                        Uri.fromParts(
                                            "package",
                                            context!!.packageName,
                                            null
                                        )
                                    intent.setData(uri)
                                    startActivityForResult(
                                        intent,
                                       REQUEST_PERMISSION_EXTERNAL_STORAGE
                                    )
                                    Toast.makeText(
                                        context,
                                        "Go to settings and grant access to storage",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    externalStorageToSettings = false
                                }
                                builder.show()
                            } else if (externalStoragePermissionStatus!!.getBoolean(
                                    permissionsRequiredExternalStorage[1],
                                    false
                                )
                            ) {
                                //Previously Permission Request was cancelled with 'Dont Ask Again',
                                // Redirect to Settings after showing information about why you need the permission
                                val builder =
                                    AlertDialog.Builder(
                                        context!!
                                    )
                                builder.setTitle("Need Storage Permission")
                                builder.setMessage("This module needs Storage permissions.")
                                builder.setCancelable(false)
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    externalStorageToSettings = true
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri =
                                        Uri.fromParts(
                                            "package",
                                            context!!.packageName,
                                            null
                                        )
                                    intent.setData(uri)
                                    startActivityForResult(
                                        intent,
                                        REQUEST_PERMISSION_EXTERNAL_STORAGE
                                    )
                                    Toast.makeText(
                                        context,
                                        "Go to settings and grant access to storage",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    externalStorageToSettings = false
                                }
                                builder.show()
                            } else {

                                //just request the permission
//                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                requestPermissions(
                                    permissionsRequiredExternalStorage,
                                   PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE
                                )
                            }
                            val editor =
                                externalStoragePermissionStatus!!.edit()
                            editor.putBoolean(permissionsRequiredExternalStorage[0], true)
                            editor.commit()
                        } else {
                            showDocumentSubmissionPopUp()
                        }
                    }
                } else {
                    CommonFunctions.showDialogAlertDismiss(
                        context as Activity?,
                        "Alert",
                        "You can no longer apply for this trip, as all the slots have been filled.",
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

//                if (tripCountCheck()) {
//                if (Build.VERSION.SDK_INT > 30) {
//                    showDocumentSubmissionPopUp();
//                } else {
//
//                    if (ActivityCompat.checkSelfPermission(context, permissionsRequiredExternalStorage[0]) != PackageManager.PERMISSION_GRANTED
//                            || ActivityCompat.checkSelfPermission(context, permissionsRequiredExternalStorage[1]) != PackageManager.PERMISSION_GRANTED) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissionsRequiredExternalStorage[0])
//                                || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissionsRequiredExternalStorage[1])) {
//                            //Show information about why you need the permission
//                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
//                            builder.setTitle("Need Storage Permission");
//                            builder.setMessage("This module needs Storage permissions.");
//
//                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    ActivityCompat.requestPermissions((Activity) context, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//
//                                }
//                            });
//                            builder.show();
//                        } else if (externalStoragePermissionStatus.getBoolean(permissionsRequiredExternalStorage[0], false)) {
//                            //Previously Permission Request was cancelled with 'Dont Ask Again',
//                            // Redirect to Settings after showing information about why you need the permission
//                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
//                            builder.setTitle("Need Storage Permission");
//                            builder.setMessage("This module needs Storage permissions.");
//
//                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    externalStorageToSettings = true;
//
//                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//                                    intent.setData(uri);
//                                    startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
//                                    Toast.makeText(context, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    externalStorageToSettings = false;
//
//                                }
//                            });
//                            builder.show();
//                        } else if (externalStoragePermissionStatus.getBoolean(permissionsRequiredExternalStorage[1], false)) {
//                            //Previously Permission Request was cancelled with 'Dont Ask Again',
//                            // Redirect to Settings after showing information about why you need the permission
//                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
//                            builder.setTitle("Need Storage Permission");
//                            builder.setMessage("This module needs Storage permissions.");
//                            builder.setCancelable(false);
//
//                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    externalStorageToSettings = true;
//
//                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//                                    intent.setData(uri);
//                                    startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
//                                    Toast.makeText(context, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                    externalStorageToSettings = false;
//
//                                }
//                            });
//                            builder.show();
//                        } else {
//
//                            //just request the permission
////                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
//                            requestPermissions(permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
//
//                        }
//                        SharedPreferences.Editor editor = externalStoragePermissionStatus.edit();
//                        editor.putBoolean(permissionsRequiredExternalStorage[0], true);
//                        editor.commit();
//                    } else {
//                        showDocumentSubmissionPopUp();
//                    }
//                }
//                } else
//                    AppUtils.showDialogAlertDismiss((Activity) context, "Alert", "You can no longer apply for this trip, as all the slots have been filled.", R.drawable.exclamationicon, R.drawable.round);
        })
        paymentButton.setOnClickListener(View.OnClickListener {
            checkTripCount(tripID,object :TripCountCheckCallback{
                override fun onTripCountChecked(isTripCountEmpty: Boolean) {
                    if (isTripCountEmpty) {
                        if (tripPaymentExceed.equals("", ignoreCase = true)) {
                            showPaymentsPopUp(context)
                        } else {
                            if (tripStatus==6) {
                                showPaymentsPopUp(context)
                            } else CommonFunctions.showDialogAlertDismiss(
                                context as Activity?,
                                "Alert",
                                "You cannot submit any more payments, as you have already reached your trip limit.",
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }
                    }
                    else
                    {
                        if (tripStatus==6) {
                            showPaymentsPopUp(context)
                        }
                        else
                        {
                            CommonFunctions.showDialogAlertDismiss(
                                context as Activity?,
                                "Alert",
                                "You can no longer pay for this trip, as all the slots have been filled.",
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }

                    }
                }
                })


        })
        coordinatorDetails.setOnClickListener(View.OnClickListener { showCoordinatorDetailsPopUp() })
        viewInvoice.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                context,
                TripInvoiceListingActivity::class.java
            )
            intent.putExtra("tripID", tripID)
            intent.putExtra("tripName", tripName)
            startActivity(intent)
        })
        var internetCheck = InternetCheckClass.isInternetAvailable(context)
        if (internetCheck)
        {
            getTripDetails(tripID)
            getTripConsent()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

    }
    override fun onItemSelected(choice: String) {
        selectedChoice = choice
        Log.e("Selected", selectedChoice)
    }


    fun interface TripCountCheckCallback {
        fun onTripCountChecked(isTripCountEmpty: Boolean)
    }

    fun checkTripCount(tripID: String?, callback: TripCountCheckCallback) {
        val paramObject = JsonObject()
        Log.e("tripID name", tripID!!)
        paramObject.addProperty("trip_id", tripID)

        val call: Call<TripCountResponseModel> =
            ApiClient.getClient.tripCountCheck("Bearer " + PreferenceData().getaccesstoken(context),paramObject)
        call.enqueue(object : Callback<TripCountResponseModel> {
            override fun onFailure(call: Call<TripCountResponseModel>, t: Throwable) {
                callback.onTripCountChecked(false)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<TripCountResponseModel>, response: Response<TripCountResponseModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    val isTripCountEmpty =
                        response.isSuccessful() && response.body() != null && response.body()!!
                            .getResponse()!!.trip_max_students!!.isEmpty()
                    callback.onTripCountChecked(isTripCountEmpty)
                }
            }

        })



    }
    private fun getChoicePreferenceArrayList() {

        val paramObject = JsonObject()
        Log.e("tripID name", tripID!!)
        paramObject.addProperty("trip_id", tripID)

        val call: Call<TripChoicePreferenceResponseModel> =
            ApiClient.getClient.tripChoicePreference("Bearer " + PreferenceData().getaccesstoken(context))
        call.enqueue(object : Callback<TripChoicePreferenceResponseModel> {
            override fun onFailure(call: Call<TripChoicePreferenceResponseModel>, t: Throwable) {
                Toast.makeText(this@TripDetailActivity, "Some error occurred.", Toast.LENGTH_SHORT)
                    .show()

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<TripChoicePreferenceResponseModel>, response: Response<TripChoicePreferenceResponseModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                choicePreferenceArray = response.body()!!.getResponse()!!.choices!!
                for (choice in choicePreferenceArray) {
                    val model = ChoicePreferenceModel()
                    model.setChoiceName(choice)
                    model.setSelected(false)
                    choicePreferenceSorted.add(model)
                }
            }

        })


        /*val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<TripChoicePreferenceResponseModel> =
            service.tripChoicePreference("Bearer " + PreferenceManager.getAccessToken(context))
        call.enqueue(object : Callback<TripChoicePreferenceResponseModel> {
            override fun onResponse(
                call: Call<TripChoicePreferenceResponseModel>,
                response: Response<TripChoicePreferenceResponseModel>
            ) {

                }
            }

            override fun onFailure(call: Call<TripChoicePreferenceResponseModel>, t: Throwable) {

            }
        })*/
    }

    private fun showDocumentSubmissionPopUp() {
        val dial = Dialog(context!!)
        dial.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dial.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dial.setCancelable(false)
        dial.setContentView(R.layout.dialog_details_submit_new)
        val permissionLinear = dial.findViewById<ConstraintLayout>(R.id.permissionDetailsConstraint)
        val passportLinear = dial.findViewById<LinearLayout>(R.id.passportConstraint)
        val visaDetailsLinear = dial.findViewById<LinearLayout>(R.id.visaConstraint)
        val eIDDetailsLinear = dial.findViewById<LinearLayout>(R.id.emirateIDConstraint)
        val payButtonConstraint = dial.findViewById<ConstraintLayout>(R.id.payNowButtonConstraint)
        val chooseFilePassportFront = dial.findViewById<Button>(R.id.chooseFilePassportFront)
        val passportNumberEditText = dial.findViewById<EditText>(R.id.passportNumberEditText)
        val chooseFilePassportBack = dial.findViewById<Button>(R.id.chooseFilePassportBack)
        val uploadPassportDetailsButton =
            dial.findViewById<Button>(R.id.uploadPassportDetailsButton)
        val visaEditText = dial.findViewById<EditText>(R.id.visaEditText)
        val permissionSlipTextView = dial.findViewById<TextView>(R.id.permissionSlipTextView)
        val chooseFileVisaFront = dial.findViewById<Button>(R.id.chooseFileVisaFront)
        val chooseFileVisaBack = dial.findViewById<Button>(R.id.choosefileVisaBack)
        val uploadVisaDetailsButton = dial.findViewById<Button>(R.id.uploadVisaDetailsButton)
        permissionSlipTextView.text = Html.fromHtml(permissionSlip)
        val eIDEditText = dial.findViewById<EditText>(R.id.eIDEditText)
        val studtitle = dial.findViewById<TextView>(R.id.studtitle)
        val passportTitleTV = dial.findViewById<TextView>(R.id.passportTitleTV)
        val visaTitleTV = dial.findViewById<TextView>(R.id.visaTitleTV)
        val emiratedIDTV = dial.findViewById<TextView>(R.id.emiratedIDTV)
        val permissiontitle = dial.findViewById<TextView>(R.id.permissiontitle)
        val chooseFileEIDFront = dial.findViewById<Button>(R.id.chooseFileEIDFront)
        val choosefileEIDBack = dial.findViewById<Button>(R.id.choosefileEIDBack)
        val uploadEIDDetailsButton = dial.findViewById<Button>(R.id.uploadEIDDetailsButton)
        val submitConsentButton = dial.findViewById<Button>(R.id.submitConsentButton)
        val payNowButtonText = dial.findViewById<TextView>(R.id.payNowButton)
        payNowButtonText.text = "PAY $singleInstallmentAmount AED"
        studtitle.text=PreferenceData().getStudentName(context)
        val close_btn = dial.findViewById<ImageView>(R.id.close_btn)
        val studentAdd = dial.findViewById<ImageView>(R.id.studentAdd)
        val passportAdd = dial.findViewById<ImageView>(R.id.passportAdd)
        val visaAdd = dial.findViewById<ImageView>(R.id.visaAdd)
        val emiratesAdd = dial.findViewById<ImageView>(R.id.emiratesAdd)
        val permissionAdd = dial.findViewById<ImageView>(R.id.permissionAdd)

        val medicalConstraint = dial.findViewById<LinearLayout>(R.id.medicalConstraint)
        val medicalconsentAdd = dial.findViewById<ImageView>(R.id.medicalconcentImg)
        val medicalconsentQuestion = dial.findViewById<TextView>(R.id.medicalconsentQuestion)
       // val medicalconsentEditText = dial.findViewById<EditText>(R.id.medicalconsentEditText)
        val uploadmedicalDetailsButton = dial.findViewById<Button>(R.id.uploadmedicalDetailsButton)
        val medicalIDTV = dial.findViewById<TextView>(R.id.medicalIDTV)
        if((PreferenceData().getStudentPhoto(context)!!).equals(""))
        {
            studentAdd.setImageResource(R.drawable.student)
        }
        else{

            Glide.with(context) //1
                .load(PreferenceData().getStudentPhoto(context)!!)
                .placeholder(R.drawable.student)
                .error(R.drawable.student)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .transform(CircleCrop()) //4
                .into(studentAdd)

        }

        val yesNoRadioGroup = dial.findViewById<RadioGroup>(R.id.yesNoRadioGroup)
        val yesButton = dial.findViewById<RadioButton>(R.id.yesRadio)
        val noButton = dial.findViewById<RadioButton>(R.id.noRadio)
        yesNoRadioGroup.orientation = LinearLayout.HORIZONTAL
        medicalconsentQuestion.setText(medicalquestion)
       chooseFilenameVisa =dial.findViewById<TextView>(R.id.textView7)
        chooseFilenameEIDBack =dial.findViewById<TextView>(R.id.textView666)
        chooseFilenameEIDFront =dial.findViewById<TextView>(R.id.textView77)
        chooseFilenamePassportBack =dial.findViewById<TextView>(R.id.textView6)
        chooseFilenamePassportFront =dial.findViewById<TextView>(R.id.textView5)
        val signhere = dial.findViewById<TextView>(R.id.signhere)



        yesButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
               // medicalconsentEditText.visibility = View.VISIBLE
                //                    submitIntentionButton.setVisibility(View.GONE);
            }
        }
        noButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
               // medicalconsentEditText.visibility = View.GONE
                uploadmedicalDetailsButton.visibility = View.VISIBLE
            }
        }
        //        if (tripType.equalsIgnoreCase("1")) {
//            studentAdd.setVisibility(View.GONE);
//            studtitle.setVisibility(View.GONE);
//            passportAdd.setVisibility(View.GONE);
//            passportTitleTV.setVisibility(View.GONE);
//            passportLinear.setVisibility(View.GONE);
//            visaDetailsLinear.setVisibility(View.GONE);
//            eIDDetailsLinear.setVisibility(View.GONE);
//            visaTitleTV.setVisibility(View.GONE);
//            visaAdd.setVisibility(View.GONE);
//            emiratedIDTV.setVisibility(View.GONE);
//            emiratesAdd.setVisibility(View.GONE);
//            permissiontitle.setVisibility(View.GONE);
//        }
        if (passportRequired==0) {
            passportTitleTV.visibility = View.GONE
            passportAdd.visibility = View.GONE
            passportLinear.visibility = View.GONE
        } else {
            passportTitleTV.visibility = View.VISIBLE
            passportAdd.visibility = View.VISIBLE
            passportLinear.visibility = View.VISIBLE
        }
        if (visaRequired==0) {
            visaTitleTV.visibility = View.GONE
            visaAdd.visibility = View.GONE
            visaDetailsLinear.visibility = View.GONE
        } else {
            visaTitleTV.visibility = View.VISIBLE
            visaAdd.visibility = View.VISIBLE
            visaDetailsLinear.visibility = View.VISIBLE
        }
        if (eIDRequired==0) {
            emiratedIDTV.visibility = View.GONE
            emiratesAdd.visibility = View.GONE
            eIDDetailsLinear.visibility = View.GONE
        } else {
            emiratedIDTV.visibility = View.VISIBLE
            emiratesAdd.visibility = View.VISIBLE
            eIDDetailsLinear.visibility = View.VISIBLE
        }
        if (consentRequired==0) {
            permissiontitle.visibility = View.GONE
            permissionAdd.visibility = View.GONE
            permissionLinear.visibility = View.GONE
        } else {
            permissiontitle.visibility = View.VISIBLE
            permissionAdd.visibility = View.VISIBLE
            permissionLinear.visibility = View.VISIBLE
        }
        if (medicalconsentRequired ==0) {
            medicalIDTV.visibility = View.GONE
            medicalconsentAdd.visibility = View.GONE
            medicalConstraint.visibility = View.GONE
        } else {
            medicalIDTV.visibility = View.VISIBLE
            medicalconsentAdd.visibility = View.VISIBLE
            medicalConstraint.visibility = View.VISIBLE
        }
        val signature_pad: SignaturePad = dial.findViewById(R.id.signature_pad)
        val termsconditionImg = dial.findViewById<CheckBox>(R.id.termsconditionImg)

        if (passportStatus == 1) {
            if (passportRequired === 1) {
                passportLinear.visibility = View.GONE
                passportAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                passportLinear.visibility = View.GONE
                passportAdd.visibility = View.GONE
                passportTitleTV.visibility = View.GONE
            }
        }
        if (visaStatus == 1) {
            if (visaRequired === 1) {
                visaDetailsLinear.visibility = View.GONE
                visaAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                visaDetailsLinear.visibility = View.GONE
                visaAdd.visibility = View.GONE
                visaTitleTV.visibility = View.GONE
            }
        }
        if (eIDStatus == 1) {
            if (eIDRequired === 1) {
                eIDDetailsLinear.visibility = View.GONE
                emiratesAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                eIDDetailsLinear.visibility = View.GONE
                emiratesAdd.visibility = View.GONE
                emiratedIDTV.visibility = View.GONE
            }
        }
        if (permissionStatus == 1) {
            if (consentRequired === 1) {
                permissionLinear.visibility = View.GONE
                permissionAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                permissionLinear.visibility = View.GONE
                permissionAdd.visibility = View.GONE
                permissiontitle.visibility = View.GONE
            }
        }
        if (medicalconsentstatus == 1) {
            if (medicalconsentRequired == 1) {
                medicalConstraint.visibility = View.GONE
                medicalconsentAdd.setImageResource(R.drawable.participatingsmallicon_new)
            } else {
                medicalConstraint.visibility = View.GONE
                medicalconsentAdd.visibility = View.GONE
                medicalIDTV.visibility = View.GONE
            }
        }
        payButtonConstraint.setOnClickListener {
            if (passportStatus == 1 && visaStatus == 1 && eIDStatus == 1 && permissionStatus == 1&& medicalconsentstatus == 1) {
                showPaymentsPopUp(context)
            } else Toast.makeText(
                context,
                "Please upload all documents.",
                Toast.LENGTH_SHORT
            ).show()
        }
        studentAdd.setOnClickListener {
            /*if (studentDetailsFLag) {
                passportLinear.visibility = View.GONE
                visaDetailsLinear.visibility = View.GONE
                eIDDetailsLinear.visibility = View.GONE
            } else {
                if (passportStatus != 1) {
                    passportLinear.visibility = View.VISIBLE
                }
                if (visaStatus != 1) {
                    visaDetailsLinear.visibility = View.VISIBLE
                }
                if (eIDStatus != 1) {
                    eIDDetailsLinear.visibility = View.VISIBLE
                }
            }
            studentDetailsFLag = !studentDetailsFLag*/
        }
        passportAdd.setOnClickListener {
            if (passportDetailsFLag) {
                passportLinear.visibility = View.GONE
            } else {
                passportLinear.visibility = View.VISIBLE
            }
            passportDetailsFLag = !passportDetailsFLag
        }
        visaAdd.setOnClickListener {
            if (visaDetailFlag) {
                visaDetailsLinear.visibility = View.GONE
            } else {
                visaDetailsLinear.visibility = View.VISIBLE
            }
            visaDetailFlag = !visaDetailFlag
        }
        emiratesAdd.setOnClickListener {
            if (eIDDetailFLag) {
                eIDDetailsLinear.visibility = View.GONE
            } else {
                eIDDetailsLinear.visibility = View.VISIBLE
            }
            eIDDetailFLag = !eIDDetailFLag
        }

        medicalconsentAdd.setOnClickListener {
            if (medicalconsentFlag) {
                medicalConstraint.visibility = View.GONE
            } else {
                medicalConstraint.visibility = View.VISIBLE
            }
            medicalconsentFlag = !medicalconsentFlag
        }
        permissionAdd.setOnClickListener {
            if (permissionFlag) {
                permissionLinear.visibility = View.GONE
            } else {
                permissionLinear.visibility = View.VISIBLE
            }
            permissionFlag = !permissionFlag
        }
        submitConsentButton.setOnClickListener {
            if (signature_pad.isEmpty()) {
                // Prompt the user to enter a signature
                Toast.makeText(
                    context,
                    "Please enter your signature",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (!termsconditionImg.isChecked) {
                Toast.makeText(
                    context,
                    "Please agree to terms and conditions",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                val signatureBitmap: Bitmap = signature_pad.getSignatureBitmap()
                val signatureFile: File = bitmapToFile(signatureBitmap)!!
                uploadConsentAPICall(dial, signatureFile)
            }
        }
        close_btn.setOnClickListener { dial.dismiss() }
        chooseFilePassportFront.setOnClickListener {
            currentPosition = 0
            openGallery(PICK_IMAGE_FRONT_PASSPORT)
        }

        termsconditionImg.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                signature_pad.setVisibility(View.VISIBLE)
                signhere.visibility=View.VISIBLE
            } else
            {
                signature_pad.setVisibility(View.GONE)
                signhere.visibility=View.GONE

            }
        }
        uploadmedicalDetailsButton.setOnClickListener {

            if (yesButton.isChecked) {

                uploadmedicalConsentApi(
                    dial,"1","")
            } else if (noButton.isChecked)  {

                uploadmedicalConsentApi(
                    dial, "2" ,"")
            } else Toast.makeText(context, getString(R.string.medical_consent_prompt), Toast.LENGTH_SHORT)
                .show()

        }
        chooseFilePassportBack.setOnClickListener {
            currentPosition = 1
            openGallery(PICK_IMAGE_BACK_PASSPORT)
        }
        uploadPassportDetailsButton.setOnClickListener { //Log.e("herer", "gesg");
            if (!passportURIArray[0].path.equals(
                    "",
                    ignoreCase = true
                ) && !passportURIArray[1].path.equals(
                    "",
                    ignoreCase = true
                ) && !passportNumberEditText.text.toString().equals("", ignoreCase = true)
            ) {
                uploadDocumentsAPICall(
                    dial,
                    passportURIArray,
                    passportNumberEditText.text.toString(),
                    "passport"
                )
            } else if (passportNumberEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(
                    context,
                    "Please enter passport number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Please upload both images.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        chooseFileVisaFront.setOnClickListener {
            currentPosition = 0
            openGallery(PICK_IMAGE_FRONT_VISA)
        }

        chooseFileVisaBack.setOnClickListener {
            currentPosition = 1
            openGallery(PICK_IMAGE_BACK_VISA)
        }
        uploadVisaDetailsButton.setOnClickListener { // Log.e("herer", visaURIArray.get(1).getPath());
            if (!visaURIArray[0].path.equals("", ignoreCase = true) && !visaEditText.text.toString()
                    .equals("", ignoreCase = true)
            ) {
                uploadSingleDocumentsAPICall(
                    dial,
                    visaURIArray[0],
                    visaEditText.text.toString(),
                    "visa"
                )
            } else if (visaEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(
                    context,
                    "Please enter Visa number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Please upload both images.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        chooseFileEIDFront.setOnClickListener {
            currentPosition = 0
            openGallery(PICK_IMAGE_FRONT_EID)
        }
        choosefileEIDBack.setOnClickListener {
            currentPosition = 1
            openGallery(PICK_IMAGE_BACK_EID)
        }
        uploadEIDDetailsButton.setOnClickListener { // Log.e("herer", eIDURIArray.get(0).getPath());
            //Log.e("front", eIDURIArray.get(0).getPath());
            // Log.e("back", eIDURIArray.get(1).getPath());
            if (!eIDURIArray[0].path.equals(
                    "",
                    ignoreCase = true
                ) && !eIDURIArray[1].path.equals(
                    "",
                    ignoreCase = true
                ) && !eIDEditText.text.toString().equals("", ignoreCase = true)
            ) {
                uploadDocumentsAPICall(dial, eIDURIArray, eIDEditText.text.toString(), "emirates")
            } else if (eIDEditText.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(
                    context,
                    "Please enter Emirates ID number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Please upload both images.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        dial.show()
    }
    private fun uploadConsentAPICall(dial: Dialog, signatureFile: File) {


        val requestFile1: RequestBody
        var requestFile2: RequestBody
        var attachment1: MultipartBody.Part? = null


        if (signatureFile.length() > 0) {
            val requestFile1 =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), signatureFile)
            attachment1 =
                MultipartBody.Part.createFormData("attachment1", signatureFile.name, requestFile1)
        }


        val frontImagePart: MultipartBody.Part? = attachment1
        val action = RequestBody.create("text/plain".toMediaTypeOrNull(), "consent")
        val student_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            PreferenceData().getStudentID(context).toString()
        )
        val trip_item_id = RequestBody.create("text/plain".toMediaTypeOrNull(), tripID)
        val card_number = RequestBody.create("text/plain".toMediaTypeOrNull(), "number")
        progressDialogP.show()
        val call: Call<TripDocumentSubmitResponseModel> = ApiClient.getClient.uploadPermissionSlip(
            "Bearer " + PreferenceData().getaccesstoken(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart
        )
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.getResponseCode() == 100) {
                    dial.dismiss()
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Permission slip successfully submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                    passportStatus = response.body()!!.getResponseData()!!.documentStatus!!.passportStatus
                    visaStatus =
                        response.body()!!.getResponseData()!!.documentStatus!!.visaStatus
                    eIDStatus = response.body()!!.getResponseData()!!.documentStatus!!.emiratesStatus
                    permissionStatus =
                        response.body()!!.getResponseData()!!.documentStatus!!.consentStatus
                    dial.dismiss()
                    if (response.body()!!.getResponseData()!!.documentStatus!!.documentCompletionStatus === 1
                    ) {
                        getTripDetails(tripID)
                    } else {
                        showDocumentSubmissionPopUp()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Permission slip submit failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("failed", "failed")
            }
        })

//




        /*val requestFile1: RequestBody
        var requestFile2: RequestBody
        var attachment1: Part? = null
        val attachment2: Part? = null
        if (signatureFile.length() > 0) {
            requestFile1 = RequestBody.create(parse.parse("multipart/form-data"), signatureFile)
            attachment1 =
                createFormData.createFormData("attachment1", signatureFile.name, requestFile1)
        }
        val action = RequestBody.create(parse.parse("text/plain"), "consent")
        val student_id: RequestBody =
            create(parse.parse("text/plain"), PreferenceManager.getStudIdForCCA(context))
        val trip_item_id = RequestBody.create(parse.parse("text/plain"), tripID)
        val card_number = RequestBody.create(parse.parse("text/plain"), "")
        val frontImagePart: Part? = attachment1
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        TripDetailActivity.progressDialogP.show()
        val call: Call<TripDocumentSubmitResponseModel> = service.uploadPermissionSlip(
            "Bearer " + PreferenceManager.getAccessToken(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart
        )
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                TripDetailActivity.progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponseData().getStatusCode().equalsIgnoreCase("303")) {

                    } else {
                        Toast.makeText(
                            this@TripDetailActivity,
                            "Permission slip submit failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(
                    this@TripDetailActivity,
                    "Permission slip submit failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
                TripDetailActivity.progressDialogP.dismiss()
                //Log.e("Response", t.getLocalizedMessage());
            }
        })*/
    }
    private fun bitmapToFile(bitmap: Bitmap): File? {
        val signatureFile = File(externalCacheDir, "signature.png")
        try {
            // Write the bitmap to the file
            val fos = FileOutputStream(signatureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return signatureFile
    }
    private fun prepareImagePart(uri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val file = File(uri.path)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream) // Adjust quality as needed
            val byteArray = stream.toByteArray()
            val currentTimeMillis = System.currentTimeMillis().toString()
            val compressedFile = File(
                context!!.cacheDir,
                "compressed_image$currentTimeMillis.jpg"
            )
            val fos = FileOutputStream(compressedFile)
            fos.write(byteArray)
            fos.flush()
            fos.close()
            val requestFile =
                compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, compressedFile.name, requestFile)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun uploadSingleDocumentsAPICall(
        dial: Dialog,
        uriArray: Uri,
        number: String,
        documentType: String
    ) {

        val file1 = File(uriArray.path)
        val frontImagePart = prepareImagePart(uriArray, "attachment1")
        Log.e("path", uriArray.path.toString())

        val action = RequestBody.create("text/plain".toMediaTypeOrNull(), documentType)
        val student_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            PreferenceData().getStudentID(context).toString()
        )
        val trip_item_id = RequestBody.create("text/plain".toMediaTypeOrNull(), tripID)
        val card_number = RequestBody.create("text/plain".toMediaTypeOrNull(), number)
        progressDialogP.show()
        val call: Call<TripDocumentSubmitResponseModel> = ApiClient.getClient.uploadSingleDocument(
            "Bearer " + PreferenceData().getaccesstoken(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart)
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.getResponseCode() == 100) {
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Documents successfully submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                    dial.dismiss()
                    passportStatus = response.body()!!.getResponseData()!!.documentStatus!!.passportStatus
                    visaStatus =
                        response.body()!!.getResponseData()!!.documentStatus!!.visaStatus
                    eIDStatus = response.body()!!.getResponseData()!!.documentStatus!!.emiratesStatus
                    permissionStatus = response.body()!!.getResponseData()!!.documentStatus!!.consentStatus
                  //  medicalconsentstatus = response.body()!!.getResponseData()!!.documentStatus!!.medicalconsentStatus

                    if (response.body()!!.getResponseData()!!.documentStatus!!.documentCompletionStatus=== 1
                    ) {
                        getTripDetails(tripID)
                    } else {
                        showDocumentSubmissionPopUp()
                    }
                } else {
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Document submit failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("failed", "Failed")
            }

        })



      /*  var requestFile1: RequestBody
        var requestFile2: RequestBody
        val attachment1: Part? = null
        val attachment2: Part? = null
        val file1 = File(uriArray.path)
        val frontImagePart: Part? = prepareImagePart(uriArray, "attachment1")
        // Log.e("path", uriArray.getPath());
        val action = RequestBody.create(parse.parse("text/plain"), documentType)
        val student_id: RequestBody =
            create(parse.parse("text/plain"), PreferenceManager.getStudIdForCCA(context))
        val trip_item_id = RequestBody.create(parse.parse("text/plain"), tripID)
        val card_number = RequestBody.create(parse.parse("text/plain"), number)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        TripDetailActivity.progressDialogP.show()
        val call: Call<TripDocumentSubmitResponseModel> = service.uploadSingleDocument(
            "Bearer " + PreferenceManager.getAccessToken(context),

        )
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                TripDetailActivity.progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponseData().getStatusCode().equalsIgnoreCase("303")) {

                    } else {
                        Toast.makeText(
                            this@TripDetailActivity,
                            "Documents submit failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(
                    this@TripDetailActivity,
                    "Documents submit failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
               progressDialogP.dismiss()
                // Log.e("Response", t.getLocalizedMessage());
            }
        })*/
    }
    private fun uploadmedicalConsentApi(dial: Dialog, preference: String, reason: String)
    {

        var paramObject = JsonObject()
        // android.util.Log.e("student name", studentNameTxt.getText().toString())
        paramObject.addProperty("action", "medical_consent")
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("student_id",PreferenceData().getStudentID(context))
        paramObject.addProperty("medical_concern",preference)
        paramObject.addProperty("medical_concern_reason", reason)


        progressDialogP.show()
        val call: Call<SubmitDocResponseModel> = ApiClient.getClient.uploadmedicalDocuments(
            "Bearer " + PreferenceData().getaccesstoken(context),
            paramObject
        )
        call.enqueue(object : Callback<SubmitDocResponseModel> {
            override fun onResponse(
                call: Call<SubmitDocResponseModel>,
                response: Response<SubmitDocResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {
                    dial.dismiss()
                    Toast.makeText(
                        this@TripDetailActivity,
                        getString(R.string.document_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    passportStatus = response.body()!!.data.documentStatus.passportStatus
                    visaStatus = response.body()!!.data.documentStatus.visaStatus
                    eIDStatus = response.body()!!.data.documentStatus.emiratesStatus
                    permissionStatus = response.body()!!.data.documentStatus.consentStatus
                    medicalconsentstatus = response.body()!!.data.documentStatus.medicalconsentStatus
                    dial.dismiss()
                    if (response.body()!!.data.documentStatus.documentCompletionStatus == 1
                    ) {
                        getTripDetails(tripID)
                    } else {
                        showDocumentSubmissionPopUp()
                    }
                } else {
                    Toast.makeText(
                        this@TripDetailActivity,
                        getString(R.string.document_submit_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SubmitDocResponseModel>, t: Throwable) {
                progressDialogP.dismiss()

            }
        })

    }
    private fun uploadDocumentsAPICall(
        dial: Dialog,
        uriArray: java.util.ArrayList<Uri>,
        number: String,
        documentType: String
    ) {

        val file1 = File(uriArray[0].path)
        val file2 = File(uriArray[1].path)
        progressDialogP.show()
        val frontImagePart = prepareImagePart(uriArray[0], "attachment1")
        val backImagePart = prepareImagePart(uriArray[1], "attachment2")
        Log.e("path", uriArray[0].path.toString())
        Log.e("path", uriArray[1].path.toString())

        val action = RequestBody.create("text/plain".toMediaTypeOrNull(), documentType)
        val student_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            PreferenceData().getStudentID(context).toString()
        )
        val trip_item_id = RequestBody.create("text/plain".toMediaTypeOrNull(), tripID)
        val card_number = RequestBody.create("text/plain".toMediaTypeOrNull(), number)
        val call: Call<TripDocumentSubmitResponseModel> = ApiClient.getClient.uploadDocuments(
            "Bearer " + PreferenceData().getaccesstoken(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart,
            backImagePart
        )
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.getResponseCode() == 100) {
                    dial.dismiss()
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Documents successfully submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                    passportStatus = response.body()!!.getResponseData()!!.documentStatus!!.passportStatus
                    visaStatus =
                        response.body()!!.getResponseData()!!.documentStatus!!.visaStatus
                    eIDStatus = response.body()!!.getResponseData()!!.documentStatus!!.emiratesStatus
                    permissionStatus =
                        response.body()!!.getResponseData()!!.documentStatus!!.consentStatus
                    if (response.body()!!.getResponseData()!!.documentStatus
                            !!.documentCompletionStatus === 1
                    ) {
                        getTripDetails(tripID)
                    } else {
                        showDocumentSubmissionPopUp()
                    }
                } else {
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Document submit failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("failed", "Failed")
            }

        })

        /*var requestFile1: RequestBody
        var requestFile2: RequestBody
        val attachment1: Part? = null
        val attachment2: Part? = null
        val file1 = File(uriArray[0].path)
        val file2 = File(uriArray[1].path)
        val frontImagePart: Part? = prepareImagePart(uriArray[0], "attachment1")
        val backImagePart: Part? = prepareImagePart(uriArray[1], "attachment2")
        // Log.e("path", uriArray.get(0).getPath());
        // Log.e("path", uriArray.get(1).getPath());
        val action = RequestBody.create(parse.parse("text/plain"), documentType)
        val student_id: RequestBody =
            create(parse.parse("text/plain"), PreferenceManager.getStudIdForCCA(context))
        val trip_item_id = RequestBody.create(parse.parse("text/plain"), tripID)
        val card_number = RequestBody.create(parse.parse("text/plain"), number)
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        TripDetailActivity.progressDialogP.show()
        val call: Call<TripDocumentSubmitResponseModel> = service.uploadDocuments(
            "Bearer " + PreferenceManager.getAccessToken(context),
            action,
            trip_item_id,
            student_id,
            card_number,
            frontImagePart,
            backImagePart
        )
        call.enqueue(object : Callback<TripDocumentSubmitResponseModel> {
            override fun onResponse(
                call: Call<TripDocumentSubmitResponseModel>,
                response: Response<TripDocumentSubmitResponseModel>
            ) {
                TripDetailActivity.progressDialogP.dismiss()
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponseData().getStatusCode().equalsIgnoreCase("303")) {

                    } else {
                        Toast.makeText(
                            this@TripDetailActivity,
                            "Documents submit failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(
                    this@TripDetailActivity,
                    "Documents submit failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<TripDocumentSubmitResponseModel>, t: Throwable) {
                TripDetailActivity.progressDialogP.dismiss()
                // Log.e("Response", t.getLocalizedMessage());
            }
        })*/
    }





    private fun getRealPathFromURI(uri: Uri): String? {
        var filePath: String? = ""
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            // If the URI is a content URI
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    filePath = cursor.getString(columnIndex)
                }
                cursor.close()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            // If the URI is a file URI
            filePath = uri.path
        } else {
            // Handle other URIs if needed
        }

        // If the file path is a default content URI, try to get the full path
        if (filePath != null && filePath.startsWith("/storage/emulated/0/")) {
            val documentFile = DocumentFile.fromSingleUri(applicationContext, uri)
            if (documentFile != null && documentFile.exists()) {
                filePath = documentFile.uri.path
            }
        }
        // Log.e("filepath", filePath);
        return filePath
    }
    private fun openGallery(requestCode: Int) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, requestCode)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Log.e("curent", currentPosition.toString());
        if (requestCode == PICK_IMAGE_FRONT_PASSPORT && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val imagePath = cursor.getString(columnIndex)
            cursor.close()
            passportURIArray[currentPosition] = Uri.parse(imagePath)
            // Log.e("uri", passportURIArray.get(currentPosition).getPath());
            val filename: String = passportURIArray.get(currentPosition).getPath()!!.substring(passportURIArray.get(currentPosition).getPath()!!.lastIndexOf("/") + 1)
            chooseFilenamePassportFront.setText(filename)
        } else if (requestCode == PICK_IMAGE_BACK_PASSPORT && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val imagePath = cursor.getString(columnIndex)
            cursor.close()

            passportURIArray[currentPosition] = Uri.parse(imagePath)
            val filename: String = passportURIArray.get(currentPosition).getPath()!!.substring(passportURIArray.get(currentPosition).getPath()!!.lastIndexOf("/") + 1)
            chooseFilenamePassportBack.setText(filename)
        } else if (requestCode == PICK_IMAGE_FRONT_VISA && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val imagePath = cursor.getString(columnIndex)
            cursor.close()
            visaURIArray[currentPosition] = Uri.parse(imagePath)
            val filename: String = visaURIArray.get(currentPosition).getPath()!!.substring(visaURIArray.get(currentPosition).getPath()!!.lastIndexOf("/") + 1)
            chooseFilenameVisa.setText(filename)
            Log.e("urifront", visaURIArray.get(currentPosition).getPath()!!);
        } else if (requestCode == PICK_IMAGE_BACK_VISA && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val imagePath = cursor.getString(columnIndex)
            cursor.close()
            visaURIArray[currentPosition] = Uri.parse(imagePath)
             Log.e("uriback", visaURIArray.get(currentPosition).getPath()!!);
        } else if (requestCode == PICK_IMAGE_FRONT_EID && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val imagePath = cursor.getString(columnIndex)
            cursor.close()
            eIDURIArray[currentPosition] = Uri.parse(imagePath)
            // Log.e("uri", visaURIArray.get(currentPosition).getPath());
            //  Log.e("ursai", String.valueOf(Uri.parse(imagePath)));
            val filename: String =  eIDURIArray.get(currentPosition).getPath()!!.substring( eIDURIArray.get(currentPosition).getPath()!!.lastIndexOf("/") + 1)

            chooseFilenameEIDFront.setText(filename )
        } else if (requestCode == PICK_IMAGE_BACK_EID && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val imagePath = cursor.getString(columnIndex)
            cursor.close()
            eIDURIArray[currentPosition] = Uri.parse(imagePath)
            // Log.e("uri", visaURIArray.get(currentPosition).getPath());
            // Log.e("ursai", String.valueOf(Uri.parse(imagePath)));
            val filename: String =  eIDURIArray.get(currentPosition).getPath()!! .substring(  eIDURIArray.get(currentPosition).getPath()!! .lastIndexOf("/") + 1)
            chooseFilenameEIDBack.setText( filename )

        } else if (requestCode == 101) {
            when (resultCode) {
                Activity.RESULT_OK -> onCardPaymentResponse(
                    CardPaymentData.getFromIntent(data!!)
                )
                Activity.RESULT_CANCELED ->{
                    Toast.makeText(context, "Transaction Failed", Toast.LENGTH_SHORT).show();

                }
            }
        } else Toast.makeText(context, "Transaction failed", Toast.LENGTH_SHORT).show()
    }
    fun onCardPaymentResponse(data: CardPaymentData) {
        when (data.code) {
            CardPaymentData.STATUS_PAYMENT_AUTHORIZED,
            CardPaymentData.STATUS_PAYMENT_CAPTURED -> {
                var internetCheck = InternetCheckClass.isInternetAvailable(context)
                if (internetCheck)
                {
                    paymentSubmitAPI()
                }
                else{
                    InternetCheckClass.showSuccessInternetAlert(context)
                }
            }
            CardPaymentData.STATUS_PAYMENT_FAILED -> {
                Toast.makeText(context, "Transaction Failed", Toast.LENGTH_SHORT).show();
            }
            CardPaymentData.STATUS_GENERIC_ERROR -> {
                Toast.makeText(context, data.reason, Toast.LENGTH_SHORT).show();
            }
            else -> IllegalArgumentException(
                "Unknown payment response (${data.reason})")
        }
    }
    private fun paymentSubmitAPI() {

        val paramObject = JsonObject()
        paramObject.addProperty("student_id", PreferenceData().getStudentID(context))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("order_reference", OrderRef)
        paramObject.addProperty("invoice_number", merchantOrderReference)
        paramObject.addProperty("paid_amount", singleInstallmentAmount)
        paramObject.addProperty("payment_type", "full_payment")
        paramObject.addProperty("device_type", "2")
        paramObject.addProperty("device_name", "Android")
        paramObject.addProperty("app_version", "1.0")
        val call: Call<TripPaymentSubmitModel> = ApiClient.getClient.paymentSubmit(
            "Bearer " + PreferenceData().getaccesstoken(context),paramObject

        )
        call.enqueue(object : Callback<TripPaymentSubmitModel> {
            override fun onResponse(
                call: Call<TripPaymentSubmitModel>,
                response: Response<TripPaymentSubmitModel>
            ) {
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {
                    Toast.makeText(context, "Payment Successful", Toast.LENGTH_SHORT).show()
                    getTripDetails(tripID)
                } else {
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Document submit failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<TripPaymentSubmitModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("failed", "Failed")
            }

        })

       /* val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<MusicBaseResponseModel> = service.paymentSubmit(
            "Bearer " + PreferenceManager.getAccessToken(context),
            paramObject
        )
        TripDetailActivity.progressDialogP.show()
        call.enqueue(object : Callback<MusicBaseResponseModel?> {
            override fun onResponse(
                call: Call<MusicBaseResponseModel?>,
                response: Response<MusicBaseResponseModel?>
            ) {
                TripDetailActivity.progressDialogP.hide()
                if (response.body() != null) {
                    val responseCode = response.code().toString()
                    if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                        if (response.body().getResponse().getStatusCode().equalsIgnoreCase("303")) {

                        } else {
                            Toast.makeText(
                                this@TripDetailActivity,
                                "Submission Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                    //                    studentDataList = new ArrayList<>();
                }
            }

            override fun onFailure(call: Call<MusicBaseResponseModel?>, t: Throwable) {
                TripDetailActivity.progressDialogP.hide()
                AppUtils.showNotifyAlertDismiss(
                    context,
                    "Alert",
                    "Submission Failed",
                    R.drawable.remove,
                    R.drawable.round
                )
            }
        })*/
    }
    private fun showCoordinatorDetailsPopUp() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_coordinator_details)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val coordinatorNameTextView = dialog.findViewById<TextView>(R.id.coordinatorNameTextView)
        val phoneContactEditText = dialog.findViewById<EditText>(R.id.phoneContactEditText)
        val emailContactEditText = dialog.findViewById<EditText>(R.id.emailContactEditText)
        val whatsappContactEditText = dialog.findViewById<EditText>(R.id.whatsappContactEditText)
        coordinatorNameTextView.text = coodName
        phoneContactEditText.setText(coodPhone)
        emailContactEditText.setText(coodEmail)
        whatsappContactEditText.setText(coodWhatsapp)
        emailContactEditText.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf(coodEmail)
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.setType("text/html")
            intent.setPackage("com.google.android.gm")
            context.startActivity(Intent.createChooser(intent, "Send mail"))
        }
        phoneContactEditText.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$coodPhone"))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        whatsappContactEditText.setOnClickListener { /*String url = "https://wa.me/" + coodWhatsapp;
                     Intent intent = new Intent(Intent.ACTION_VIEW);
                     intent.setData(Uri.parse(url));
                     if (intent.resolveActivity(getPackageManager()) != null) {
                         startActivity(intent);
                     }*/
            val url = "https://api.whatsapp.com/send?phone=$coodWhatsapp"
            try {
                val pm = context!!.packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(url))
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    context,
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
        dialog.show()
    }

    private fun showPaymentsPopUp(activity: Context) {
        val bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        val layout: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_bottom_sheet_payment, null)
        bottomSheetDialog.setContentView(layout)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        val payTotalView = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.payTotalView)
        val payInstallmentView =
            bottomSheetDialog.findViewById<ConstraintLayout>(R.id.payInstallmentView)
        val totalAmountTextView = bottomSheetDialog.findViewById<TextView>(R.id.totalAmountTextView)
        totalAmountTextView!!.text = "$singleInstallmentAmount AED"
        if (multipleInstallmentsArray.size > 1) {
            payInstallmentView!!.visibility = View.VISIBLE
        } else {
            payInstallmentView!!.visibility = View.GONE
        }
        if (tripStatus==6) {
            payInstallmentView.visibility = View.VISIBLE
            payTotalView!!.visibility = View.GONE
        }
        payInstallmentView.setOnClickListener {
            bottomSheetDialog.dismiss()
            val intent = Intent(
                context,
                TripInstallmentActivity::class.java
            )
            intent.putExtra("tripID", tripID)
            intent.putExtra("tripName", tripName)
            context!!.startActivity(intent)
        }
        payTotalView!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            initialisePayment()
        }
        bottomSheetDialog.show()
    }
    private fun initialisePayment() {
       progressDialogP.show()
        val paymentTokenBody = PaymentTokenApiModel( PreferenceData().getStudentID(context).toString())
        val call: Call<PaymentTokenModel> =
            ApiClient.getClient.payment_token_trip(paymentTokenBody,"Bearer " + PreferenceData().getaccesstoken(context))
        call.enqueue(object : Callback<PaymentTokenModel> {
            override fun onFailure(call: Call<PaymentTokenModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<PaymentTokenModel>, response: Response<PaymentTokenModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    progressDialogP.dismiss()
                    if (response.body()!!.status==100) {

                        acessToken = response.body()!!.responseArray.access_token
                        val amountDouble = singleInstallmentAmount.toDouble() * 100
                        val amuntInt = amountDouble.toInt()
                        val strDouble = amuntInt.toString()
                        var arrayLength = 0
                        val array = strDouble.toCharArray()
                        arrayLength = array.size
                        var firstNonZeroAt = 0
                        for (i in array.indices) {
                            if (!array[i].toString().equals("0", ignoreCase = true)) {
                                firstNonZeroAt = i
                                break
                            }
                        }
                        val newArray = Arrays.copyOfRange(array, firstNonZeroAt, arrayLength)
                        val resultString = String(newArray)
                        // System.out.println("amount removed zero" + resultString);
                        val unixTime = System.currentTimeMillis() / 1000L
                        orderId = "BISAD_TRIP_AND_$unixTime"
                        callForPayment(strDouble)
                    }
                    else {

//                        progressBarDialog.hide();
                        CommonFunctions.showDialogAlertDismiss(
                            context as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }

                }
            }

        })
       /* val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val call: Call<FeePayAccessTokenResponseModel> =
            service.accessTokenTripPayment("Bearer " + PreferenceManager.getAccessToken(context))
        call.enqueue(object : Callback<FeePayAccessTokenResponseModel> {
            override fun onResponse(
                call: Call<FeePayAccessTokenResponseModel>,
                response: Response<FeePayAccessTokenResponseModel>
            ) {
                if (response.isSuccessful()) {
                    TripDetailActivity.progressDialogP.hide()
                    val apiResponse: FeePayAccessTokenResponseModel? = response.body()
                    val response_code: String = apiResponse.getResponsecode()
                    if (response_code == "200") {
                        val statuscode: String = apiResponse.getResponse().getStatuscode()
                        val responseData: FeePayAccessTokenResponseModel.ResponseData =
                            apiResponse.getResponse()
                        if (statuscode == "303") {

                        }
                    }
                }
            }

            override fun onFailure(call: Call<FeePayAccessTokenResponseModel>, t: Throwable) {
                TripDetailActivity.progressDialogP.hide()
                CommonFunctions.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    context!!.getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })*/
    }
    private fun callForPayment(strDouble: String) {
       // progressDialogP.show()
        mProgressRelLayout.visibility=View.VISIBLE


        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
     //   var mechantorderRef=invoice_ref+"-"+ts
         merchantOrderReference="BISTRIPAND$ts"

        val token = PreferenceData().getaccesstoken(context)
        val paymentGatewayBody = PaymentGatewayApiModelTrip(strDouble,PreferenceData().getUserEmail(context).toString(),
            merchantOrderReference,PreferenceData().getStudentName(context)!!,"","BISAD","","Abu Dhabi",
            acessToken!!,tripID)
        val call: Call<TripPaymentInitiateResponseModel> =
            ApiClient.getClient.payment_gateway_trip(paymentGatewayBody, "Bearer " + token)
        call.enqueue(object : Callback<TripPaymentInitiateResponseModel> {
            override fun onFailure(call: Call<TripPaymentInitiateResponseModel>, t: Throwable) {

                mProgressRelLayout.visibility=View.GONE
                CommonFunctions.faliurepopup(context)
            }

            override fun onResponse(call: Call<TripPaymentInitiateResponseModel>, response: Response<TripPaymentInitiateResponseModel>) {
                val responsedata = response.body()
                mProgressRelLayout.visibility=View.GONE
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {

                        if (response.body()!!.getResponsecode() == 100) {

                            OrderRef = responsedata.getResponse()!!.order_reference
                            var orderPageUrl = responsedata.getResponse()!!.order_paypage_url
                            var auth = responsedata.getResponse()!!.authorization
                            val Code: String = orderPageUrl!!.split("=").toTypedArray().get(1)
                            SDKConfig.shouldShowOrderAmount(true)
                            SDKConfig.shouldShowCancelAlert(true)
                            val request: CardPaymentRequest =
                                CardPaymentRequest.Builder().gatewayUrl(auth!!).code(Code).build()

                            val paymentClient = PaymentClient(mActivity, "fdhasfd")
                            paymentClient.launchCardPayment(request, 101)
                            /*if (responsedata.getResponse()!!.trip_max_students.equals("")) {
                                paymentClient.launchCardPayment(request, 101)
                            } else CommonFunctions.showDialogAlertDismiss(
                                context as Activity?,
                                "Alert",
                                "You can no longer pay for this trip, as all the slots have been filled.",
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )*/
                        } else {
                            if (response.body()!!.getResponsecode() == 116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)

                            } else {
                                if (response.body()!!.getResponsecode() == 103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(
                                        response.body()!!.getResponsecode()!!,
                                        context
                                    )
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            }
        })

        }














       /* val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        merchantOrderReference = "NASTRIPAND$ts"

        // merchantOrderReference="MUSICACAT2-01-1610606134";
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val paramObject = JsonObject()
        paramObject.addProperty("access_token", paymentToken)
        paramObject.addProperty("amount", strDouble)
        paramObject.addProperty("merchantOrderReference", merchantOrderReference)
        paramObject.addProperty("firstName", PreferenceManager.getCanteenStudentName(context))
        paramObject.addProperty("lastName", "")
        paramObject.addProperty("address1", "NAS DUBAI")
        paramObject.addProperty("city", "DUBAI")
        paramObject.addProperty("countryCode", "UAE")
        paramObject.addProperty("emailAddress", PreferenceManager.getUserEmail(context))
        paramObject.addProperty("trip_id", tripID)
        val call: Call<TripPaymentInitiateResponseModel> = service.createTripPayment(
            "Bearer " + PreferenceManager.getAccessToken(context),
            paramObject
        )
        call.enqueue(object : Callback<TripPaymentInitiateResponseModel?> {
            override fun onResponse(
                call: Call<TripPaymentInitiateResponseModel?>,
                response: Response<TripPaymentInitiateResponseModel?>
            ) {
                if (response.isSuccessful()) {
                    TripDetailActivity.progressDialogP.hide()
                    val apiResponse: TripPaymentInitiateResponseModel? = response.body()
                    val response_code: String = apiResponse.getResponsecode()
                    if (response_code == "200") {
                        val statuscode: String = apiResponse.getResponse().getStatuscode()
                        val responseData: TripPaymentInitiateResponseModel.ResponseData =
                            apiResponse.getResponse()
                        if (statuscode == "303") {
                            OrderRef = apiResponse.getResponse().getOrder_reference()
                            PayUrl = apiResponse.getResponse().getOrder_paypage_url()
                            AuthUrl = apiResponse.getResponse().getAuthorization()
                            val Code = PayUrl!!.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()[1]
                            val request: CardPaymentRequest =
                                Builder().gatewayUrl(AuthUrl).code(Code).build()
                            val paymentClient = PaymentClient(mActivity, "fdhasfd")

                    } else {
                        *//*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*//*
                        TripDetailActivity.progressDialogP.hide()
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                }
            }

            override fun onFailure(call: Call<TripPaymentInitiateResponseModel?>, t: Throwable) {
                TripDetailActivity.progressDialogP.hide()
                AppUtils.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    context!!.getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })*/


    private fun getTripConsent() {


        val paramObject = JsonObject()
      //  Log.e("name", PreferenceManager.getStudIdForCCA(context))
        paramObject.addProperty("student_id", PreferenceData().getStudentID(context))
        paramObject.addProperty("trip_item_id", tripID)
        val call: Call<TripConsentResponseModel> = ApiClient.getClient.tripConsent(
            "Bearer " + PreferenceData().getaccesstoken(context),paramObject

        )
        call.enqueue(object : Callback<TripConsentResponseModel> {
            override fun onResponse(
                call: Call<TripConsentResponseModel>,
                response: Response<TripConsentResponseModel>
            ) {

                progressDialogP.dismiss()
                if (response.body()!!.getResponseCode() == 100) {
                    permissionSlip = response.body()!!.getResponseDetail()!!.permissionData!!.permissionContent!!
                }



            }

            override fun onFailure(call: Call<TripConsentResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("failed", "Failed")
            }

        })


    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        ) // create a menuOption Array
        val builder = android.app.AlertDialog.Builder(context)
        builder.setItems(
            optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {
                val takePicture =
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (optionsMenu[i] == "Choose from Gallery") {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }


    private fun showIntentionPopUp() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_intention_pop_up)
        val yesNoRadioGroup = dialog.findViewById<RadioGroup>(R.id.yesNoRadioGroup)
        val yesButton = dialog.findViewById<RadioButton>(R.id.yesRadio)
        val tripIntentionQuestion = dialog.findViewById<TextView>(R.id.tripIntentionQuestion)
        val studentNameTextView = dialog.findViewById<TextView>(R.id.studentNameTextView)
        studentNameTextView.setText(PreferenceData().getStudentName(context))
        val noButton = dialog.findViewById<RadioButton>(R.id.noRadio)
        val preferenceLayout = dialog.findViewById<ConstraintLayout>(R.id.preferenceLayout)
        val submitIntentionButton = dialog.findViewById<Button>(R.id.submitIntentionButton)
        val closeImageView = dialog.findViewById<ImageView>(R.id.close_img)
        val choicePreferenceQuestion = dialog.findViewById<TextView>(R.id.choicePreferenceQuestion)
        val choicePreferenceListView =
            dialog.findViewById<RecyclerView>(R.id.choicePreferenceRecycler)
        preferenceLayout.visibility = View.GONE // Initially hidden
        yesNoRadioGroup.orientation = LinearLayout.HORIZONTAL
        val adapter = ChoicePreferenceAdapter(context, choicePreferenceSorted, this) // Replace yourDataList with your list of data
        choicePreferenceListView.adapter = adapter
        val layoutManager = GridLayoutManager(context, 3)
        choicePreferenceListView.layoutManager = layoutManager
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val itemDecoration = GridSpacingItemDecoration(3, spacing, true)
        choicePreferenceListView.addItemDecoration(itemDecoration)
        choicePreferenceListView.addOnItemTouchListener(
            RecyclerItemListener(
                context,
                choicePreferenceListView,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
//                submitIntent("1",dialog,choicePreferenceArray.get(position));
                    }

                    override fun onLongClickItem(v: View?, position: Int) {
//                submitIntent("1",dialog,choicePreferenceArray.get(position));
                    }
                })
        )
        submitIntentionButton.setOnClickListener {
            if (yesButton.isChecked ) {

                submitIntent("1", dialog)
            } else if (noButton.isChecked) {

                submitIntent("2", dialog)
            } else Toast.makeText(context, getString(R.string.provide_intention_prompt), Toast.LENGTH_SHORT)
                .show()


        }
        yesButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                preferenceLayout.visibility = View.GONE
                //                    submitIntentionButton.setVisibility(View.GONE);
            }
        }
        noButton.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                preferenceLayout.visibility = View.GONE
                submitIntentionButton.visibility = View.VISIBLE
            }
        }
        closeImageView.setOnClickListener { dialog.dismiss() }

        closeImageView.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
    private fun submitIntent(intent: String, dialog: Dialog) {
       progressDialogP.show()
        val paramObject = JsonObject()
        // Log.e("tripID name", tripID);
        paramObject.addProperty("student_id", PreferenceData().getStudentID(context))
        paramObject.addProperty("trip_item_id", tripID)
        paramObject.addProperty("status", intent)
        val call: Call<GeneralSubmitResponseModel> = ApiClient.getClient.tripIntentSubmit(
            "Bearer " + PreferenceData().getaccesstoken(context),paramObject

        )
        call.enqueue(object : Callback<GeneralSubmitResponseModel> {
            override fun onResponse(
                call: Call<GeneralSubmitResponseModel>,
                response: Response<GeneralSubmitResponseModel>
            ) {
                dialog.dismiss()
                progressDialogP.dismiss()
                if (response.body()!!.status == 100) {
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Intention successfully submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                }  else if (response.body()!!.status
                        .equals("313")
                ) {
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Intention already submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.body()!!.status
                        .equals("314")
                ) {
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Intention already submitted for this choice. Select any other choice.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@TripDetailActivity,
                        "Intention Submission Failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                getTripDetails(tripID)
            }

            override fun onFailure(call: Call<GeneralSubmitResponseModel>, t: Throwable) {
                progressDialogP.dismiss()
                Log.e("failed", "Failed")
            }

        })


    }
    private fun CheckPermission(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = java.util.ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(context, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this@TripDetailActivity,
                listPermissionsNeeded.toTypedArray<String>(), MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }
}