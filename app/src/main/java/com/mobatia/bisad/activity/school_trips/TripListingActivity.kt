package com.mobatia.bisad.activity.school_trips

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripListingActivity : AppCompatActivity() {
    var context: Context? = null
    var extras: Bundle? = null
    var tab_type: String? = null
    private val progressDialogP: ProgressBarDialog? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    var bannerImageView: ImageView? = null
    var descriptionTextView: TextView? = null
    var sendEmailImageView: ImageView? = null
    var tripListRecycler: RecyclerView? = null
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var categoryID = ""
    var categoryName = ""
    var mStudentSpinner: LinearLayout? = null


    var categoriesList: ArrayList<TripListResponseModel.TripItem>? = null
    var tripsCategoryAdapter: TripListAdapter? = null
    var contactEmail = ""
    var back: ImageView? =
        null
    var btn_history:android.widget.ImageView? = null
    var home:android.widget.ImageView? = null

    //    LinearLayout mStudentSpinner;
    var studentName: TextView? = null
    var studImg: ImageView? = null
    var stud_id: String? = null
    var studClass = ""
    var orderId = ""
    var stud_img = ""
    var studentsModelArrayList: ArrayList<StudentDataModel.DataItem> =
        ArrayList<StudentDataModel.DataItem>()
    var studentList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        context = this
        initialiseUI()
        if (AppUtils.checkInternet(context)) {
            getStudentsListAPI()
        } else {
            AppUtils.showDialogAlertDismiss(
                context as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }

    private fun initialiseUI() {
        extras = intent.extras
        if (extras != null) {
            categoryID = extras!!.getString("trip_category_id")!!
            categoryName = extras!!.getString("trip_category_name")!!
        }
        TripListingActivity.progressDialogP = ProgressBarDialog(context, R.drawable.spinner)
        bannerImageView = findViewById<ImageView>(R.id.bannerImage)
        descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        sendEmailImageView = findViewById<ImageView>(R.id.sendEmailImageView)
        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader)
        headermanager = HeaderManager(this@TripListingActivity, categoryName)
        headermanager.getHeader(relativeHeader, 6)
        back = headermanager.getLeftButton()
        btn_history = headermanager.getRightHistoryImage()
        btn_history!!.visibility = View.INVISIBLE
        tripListRecycler = findViewById<RecyclerView>(R.id.tripListRecycler)
        tripListRecycler.setHasFixedSize(true)
        val spacing = 5 // 50px
        val itemDecoration = ItemOffsetDecoration(context, spacing)
        recyclerViewLayoutManager = GridLayoutManager(context, 1)
        tripListRecycler.addItemDecoration(
            DividerItemDecoration(context!!.resources.getDrawable(R.drawable.list_divider))
        )
        tripListRecycler.addItemDecoration(itemDecoration)
        tripListRecycler.setLayoutManager(recyclerViewLayoutManager)
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back)
        back!!.setOnClickListener {
            AppUtils.hideKeyBoard(context)
            finish()
        }
        home = headermanager.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(
                context,
                HomeListAppCompatActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        mStudentSpinner = findViewById<LinearLayout>(R.id.studentSpinner)
        TripListingActivity.studentName = findViewById<TextView>(R.id.studentName)
        TripListingActivity.studentName = findViewById<TextView>(R.id.studentName)
        TripListingActivity.studImg = findViewById<ImageView>(R.id.imagicon)
        mStudentSpinner.setOnClickListener(View.OnClickListener {
            if (TripListingActivity.studentsModelArrayList.size > 0) {
                showStudentSelection(TripListingActivity.studentsModelArrayList)
            } else {
                AppUtils.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    getString(R.string.student_not_available),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })
    }

    private fun getStudentsListAPI() {
        TripListingActivity.progressDialogP.show()
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val paramObject = JsonObject()
        paramObject.addProperty("portal", "1")
        val call: Call<StudentDataModel> = service.studentListcanpreorder(
            "Bearer " + PreferenceManager.getAccessToken(context),
            paramObject
        )
        call.enqueue(object : Callback<StudentDataModel?> {
            override fun onResponse(
                call: Call<StudentDataModel?>,
                response: Response<StudentDataModel?>
            ) {
                if (response.body() != null) {
                    val response_code: String = response.body().getResponsecode()
                    val statusCode: String = response.body().getResponse().getStatuscode()
                    if (response_code.equals("200", ignoreCase = true)) {
                        if (statusCode.equals("303", ignoreCase = true)) {
                            TripListingActivity.progressDialogP.hide()
                            TripListingActivity.studentsModelArrayList.clear()
                            studentList.clear()
                            if (response.body().getResponse().getData().size() > 0) {
                                for (dataItem in response.body().getResponse().getData()) {
                                    val item: StudentDataModel.DataItem = DataItem()
                                    item.setId(dataItem.getId())
                                    item.setName(dataItem.getName())
                                    item.setClassName(dataItem.getClassName())
                                    item.setPhoto(dataItem.getPhoto())
                                    item.setSection(dataItem.getSection())
                                    TripListingActivity.studentsModelArrayList.add(item)
                                }
                                TripListingActivity.studentName.setText(
                                    TripListingActivity.studentsModelArrayList.get(
                                        0
                                    ).getName()
                                )
                                stud_id = TripListingActivity.studentsModelArrayList.get(0).getId()
                                //                                  PreferenceManager.setLeaveStudentId(mContext, stud_id);
//                                  PreferenceManager.setLeaveStudentName(mContext, studentsModelArrayList.get(0).getmName());
                                studClass =
                                    TripListingActivity.studentsModelArrayList.get(0).getClassName()
                                TripListingActivity.stud_img =
                                    TripListingActivity.studentsModelArrayList.get(0).getPhoto()
                                PreferenceManager.setStudIdForCCA(
                                    context,
                                    TripListingActivity.studentsModelArrayList.get(0).getId()
                                )
                                PreferenceManager.setCanteenStudentImage(
                                    context,
                                    TripListingActivity.studentsModelArrayList.get(0).getPhoto()
                                )
                                PreferenceManager.setCanteenStudentName(
                                    context,
                                    TripListingActivity.studentsModelArrayList.get(0).getName()
                                )
                                if (TripListingActivity.stud_img != "") {
                                    Picasso.with(context)
                                        .load(AppUtils.replace(TripListingActivity.stud_img))
                                        .placeholder(R.drawable.boy).fit()
                                        .into(TripListingActivity.studImg)
                                } else {
                                    TripListingActivity.studImg.setImageResource(R.drawable.boy)
                                }
                                getTripList(categoryID)
                            } else {
                                TripListingActivity.progressDialogP.hide()
                                AppUtils.showDialogAlertDismiss(
                                    context as Activity?,
                                    "Alert",
                                    getString(R.string.student_not_available),
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            }
                        }
                    } else {
                        TripListingActivity.progressDialogP.hide()
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?,
                            "Alert",
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    TripListingActivity.progressDialogP.hide()
                    AppUtils.showDialogAlertDismiss(
                        context as Activity?,
                        "Alert",
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<StudentDataModel?>, t: Throwable) {
                TripListingActivity.progressDialogP.hide()
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
    override fun onResume() {
        getTripList(categoryID)
        super.onResume()
    }

    private fun getTripList(categoryID: String) {
        TripListingActivity.progressDialogP.show()
        val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
        val paramObject = JsonObject()
        categoriesList = java.util.ArrayList<TripListResponseModel.TripItem>()
        // Log.e("student name",studentName.getText().toString());
        paramObject.addProperty("student_id", PreferenceManager.getStudIdForCCA(context))
        paramObject.addProperty("trip_category_id", categoryID)
        paramObject.addProperty("limit", "100")
        paramObject.addProperty("skip", "0")
        val call: Call<TripListResponseModel> =
            service.tripList("Bearer " + PreferenceManager.getAccessToken(context), paramObject)
        call.enqueue(object : Callback<TripListResponseModel?> {
            override fun onResponse(
                call: Call<TripListResponseModel?>,
                response: Response<TripListResponseModel?>
            ) {
                TripListingActivity.progressDialogP.dismiss()
                categoriesList = java.util.ArrayList<TripListResponseModel.TripItem>()
                assert(response.body() != null)
                if (response.body().getResponseCode().equalsIgnoreCase("200")) {
                    if (response.body().getResponse().getStatusCode().equalsIgnoreCase("303")) {
                        if (response.body().getResponse().getData().size() > 0) {
                            categoriesList = response.body().getResponse().getData()
                            if (categoriesList!!.size > 0) {
                                // Log.e("Here","Here");
                                tripsCategoryAdapter = TripListAdapter(context, categoriesList)
                                tripListRecycler!!.adapter = tripsCategoryAdapter
                            } else {
                                //Log.e("Here","not");
                                categoriesList =
                                    java.util.ArrayList<TripListResponseModel.TripItem>()
                                tripsCategoryAdapter =
                                    TripListAdapter(context, java.util.ArrayList<E>())
                                tripListRecycler!!.adapter = tripsCategoryAdapter
                                Toast.makeText(
                                    this@TripListingActivity,
                                    "No trips available.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            categoriesList = java.util.ArrayList<TripListResponseModel.TripItem>()
                            tripsCategoryAdapter =
                                TripListAdapter(context, java.util.ArrayList<E>())
                            tripListRecycler!!.adapter = tripsCategoryAdapter
                            Toast.makeText(
                                this@TripListingActivity,
                                "No trips available.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<TripListResponseModel?>, t: Throwable) {
                TripListingActivity.progressDialogP.dismiss()
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

    fun showStudentSelection(mStudentArray: java.util.ArrayList<StudentDataModel.DataItem>) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_student_media_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<Button>(R.id.btn_dismiss)
        val iconImageView = dialog.findViewById<ImageView>(R.id.iconImageView)
        iconImageView.setImageResource(R.drawable.boy)
        val socialMediaList = dialog.findViewById<RecyclerView>(R.id.recycler_view_social_media)
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(context!!.resources.getDrawable(R.drawable.button))
        } else {
            dialogDismiss.background = context!!.resources.getDrawable(R.drawable.button)
        }
        socialMediaList.addItemDecoration(DividerItemDecoration(context!!.resources.getDrawable(R.drawable.list_divider_teal)))
        socialMediaList.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        socialMediaList.layoutManager = llm
        val studentAdapter = StudentSpinnerAdapter(context, mStudentArray)
        socialMediaList.adapter = studentAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
        socialMediaList.addOnItemTouchListener(
            RecyclerItemListener(
                context,
                socialMediaList,
                object : RecyclerTouchListener() {
                    fun onClickItem(v: View?, position: Int) {
                        dialog.dismiss()
                        TripListingActivity.studentName.setText(mStudentArray[position].getName())
                        stud_id = mStudentArray[position].getId()
                        studClass = mStudentArray[position].getClassName()
                        TripListingActivity.stud_img = mStudentArray[position].getPhoto()
                        if (TripListingActivity.stud_img != "") {
                            Picasso.with(context)
                                .load(AppUtils.replace(TripListingActivity.stud_img))
                                .placeholder(R.drawable.boy).fit().into(TripListingActivity.studImg)
                        } else {
                            TripListingActivity.studImg.setImageResource(R.drawable.boy)
                        }
                        Log.e("id", mStudentArray[position].getId())
                        PreferenceManager.setStudIdForCCA(context, mStudentArray[position].getId())
                        PreferenceManager.setCanteenStudentImage(
                            context,
                            mStudentArray[position].getPhoto()
                        )
                        PreferenceManager.setCanteenStudentName(
                            context,
                            TripListingActivity.studentsModelArrayList.get(position).getName()
                        )
                        getTripList(categoryID)
                    }

                    fun onLongClickItem(v: View?, position: Int) {
                        println("On Long Click Item interface")
                    }
                })
        )
        dialog.show()
    }

}