package com.mobatia.bisad.activity.school_trips

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.JsonObject
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.school_trips.adapter.TripListAdapter
import com.mobatia.bisad.activity.school_trips.model.TripListResponseModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.ProgressBarDialog

import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.manager.HeaderManager
import com.mobatia.bisad.manager.HeaderManagerNoColorSpace
import com.mobatia.bisad.manager.ItemOffsetDecoration
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.recyclermanager.DividerItemDecoration

import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripListingActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var extras: Bundle
    var tab_type: String? = null
    private lateinit var progressDialogP: ProgressBarDialog
    lateinit var relativeHeader: RelativeLayout
    lateinit var headermanager: HeaderManager
    lateinit var bannerImageView: ImageView
    lateinit var descriptionTextView: TextView
    lateinit var sendEmailImageView: ImageView
    lateinit var tripListRecycler: RecyclerView
    lateinit var recyclerViewLayoutManager: GridLayoutManager
    var categoryID = ""
    var categoryName = ""
    lateinit var mStudentSpinner: LinearLayout
    lateinit var studentArrayList :ArrayList<StudentList>


    var categoriesList: ArrayList<TripListResponseModel.TripItem>? = null
    var tripsCategoryAdapter: TripListAdapter? = null
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
    lateinit var Student_name : String
    var studentList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        context = this
        initialiseUI()

        var internetCheck = InternetCheckClass.isInternetAvailable(context)
        if (internetCheck)
        {
            getStudentsListAPI()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

    }

    private fun initialiseUI() {
        extras = intent.extras!!
        if (extras != null) {
            categoryID = extras!!.getString("trip_category_id")!!
            categoryName = extras!!.getString("trip_category_name")!!
        }
        progressDialogP = ProgressBarDialog(context)
      //  bannerImageView = findViewById<ImageView>(R.id.bannerImage)
       // descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
       // sendEmailImageView = findViewById<ImageView>(R.id.sendEmailImageView)
        relativeHeader = findViewById<RelativeLayout>(R.id.relativeHeader)

            headermanager = HeaderManager(
                this@TripListingActivity,
                categoryName
            )


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
            CommonFunctions.hideKeyBoard(context)
            finish()
        }
        home = headermanager.getLogoButton()!!
        home!!.setOnClickListener {
            val `in` = Intent(
                context,
                HomeActivity::class.java
            )
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        mStudentSpinner = findViewById<LinearLayout>(R.id.studentSpinner)
       // studentName = findViewById<TextView>(R.id.studentName)
        studentName = findViewById<TextView>(R.id.studentName)
        studImg = findViewById<ImageView>(R.id.imagicon)
        mStudentSpinner.setOnClickListener(View.OnClickListener {
            if (studentArrayList.size > 0) {
                showStudentList(context,studentArrayList)
               // showStudentSelection(studentArrayList)
            } else {
                CommonFunctions.showDialogAlertDismiss(
                    context as Activity?,
                    "Alert",
                    getString(R.string.student_not_available),
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })

        var internetCheck = InternetCheckClass.isInternetAvailable(context)
        if (internetCheck)
        {
            getStudentsListAPI()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }
    }

    private fun getStudentsListAPI() {

        progressDialogP.show()
        studentArrayList=ArrayList<StudentList>()
        val token = PreferenceData().getaccesstoken(context)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                val arraySize :Int = response.body()!!.responseArray.studentList.size
                if (response.body()!!.status==100)
                {
                    studentArrayList=ArrayList<StudentList>()

                    studentArrayList.addAll(response.body()!!.responseArray.studentList)

                    if (PreferenceData().getStudentID(context).equals(""))
                    {
                        Student_name=studentArrayList.get(0).name
                        stud_img=studentArrayList.get(0).photo
                        stud_id=studentArrayList.get(0).id
                        studClass=studentArrayList.get(0).section
                        PreferenceData().setStudentID(context,stud_id)
                        PreferenceData().setStudentName(context,Student_name)
                        PreferenceData().setStudentPhoto(context,stud_img)
                        PreferenceData().setStudentClass(context,studClass)
                        studentName.text=Student_name
                        if(!stud_img.equals(""))
                        {
                            Glide.with(context) //1
                                .load(studImg)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        }
                        else{
                            studImg.setImageResource(R.drawable.student)
                        }

                    }
                    else{
                        Student_name= PreferenceData().getStudentName(context)!!
                        stud_img= PreferenceData().getStudentPhoto(context)!!
                        stud_id= PreferenceData().getStudentID(context)!!
                        studClass= PreferenceData().getStudentClass(context)!!
                        studentName.text=Student_name
                        if(!stud_img.equals(""))
                        {
                            Glide.with(context) //1
                                .load(stud_img)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        }
                        else{
                            studImg.setImageResource(R.drawable.student)
                        }
                        getTripList(categoryID)

                    }
                }
            }
        })

    }
    override fun onResume() {
        getTripList(categoryID)
        super.onResume()
    }

    private fun getTripList(categoryID: String) {
       progressDialogP.show()
        val paramObject = JsonObject()
        categoriesList = java.util.ArrayList<TripListResponseModel.TripItem>()
        // Log.e("student name",studentName.getText().toString());
        paramObject.addProperty("student_id", PreferenceData().getStudentID(context))
        paramObject.addProperty("trip_category_id", categoryID)
        paramObject.addProperty("limit", "100")
        paramObject.addProperty("skip", "0")

        val call: Call<TripListResponseModel> =
            ApiClient.getClient.tripList("Bearer " + PreferenceData().getaccesstoken(context),paramObject)
        call.enqueue(object : Callback<TripListResponseModel> {
            override fun onFailure(call: Call<TripListResponseModel>, t: Throwable) {
                CommonFunctions.faliurepopup(context)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<TripListResponseModel>, response: Response<TripListResponseModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    progressDialogP.dismiss()
                    if (response.body()!!.getResponseCode()==100) {
                        if (response.body()!!.getResponse()!!.data!!.size > 0) {
                            categoriesList = response.body()!!.getResponse()!!.data
                            if (categoriesList!!.size > 0) {
                                // Log.e("Here","Here");
                                tripsCategoryAdapter = TripListAdapter(context, categoriesList!!)
                                tripListRecycler!!.adapter = tripsCategoryAdapter
                            } else {
                                //Log.e("Here","not");
                                categoriesList = ArrayList<TripListResponseModel.TripItem>()
                                tripsCategoryAdapter =
                                    TripListAdapter(context, categoriesList!!)
                                tripListRecycler!!.adapter = tripsCategoryAdapter
                                Toast.makeText(this@TripListingActivity, "No trips available.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            categoriesList = ArrayList<TripListResponseModel.TripItem>()
                            tripsCategoryAdapter =
                                TripListAdapter(context, categoriesList!!)
                            tripListRecycler!!.adapter = tripsCategoryAdapter
                            Toast.makeText(this@TripListingActivity, "No trips available.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                    }
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }

        })
    }


















        /*val service: APIInterface = APIClient.getRetrofitInstance().create(APIInterface::class.java)
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
        })*/

fun showStudentList(mcontext: Context ,mStudentList : ArrayList<StudentList>)
{
    val dialog = Dialog(mcontext)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialogue_student_list)
    var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
    var btn_dismiss = dialog.findViewById(R.id.btn_dismiss) as Button
    var studentListRecycler = dialog.findViewById(R.id.studentListRecycler) as RecyclerView
    iconImageView.setImageResource(R.drawable.boy)
    //if(mSocialMediaArray.get())
    val sdk = Build.VERSION.SDK_INT
    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
        btn_dismiss.setBackgroundDrawable(
            mcontext.resources.getDrawable(R.drawable.button_new)
        )
    } else {
        btn_dismiss.background = mcontext.resources.getDrawable(R.drawable.button_new)
    }

    studentListRecycler.setHasFixedSize(true)
    val llm = LinearLayoutManager(mcontext)
    llm.orientation = LinearLayoutManager.VERTICAL
    studentListRecycler.layoutManager = llm
    if(mStudentList.size>0)
    {
        val studentAdapter = StudentListAdapter(mcontext,mStudentList)
        studentListRecycler.adapter = studentAdapter
    }
    btn_dismiss.setOnClickListener()
    {
        dialog.dismiss()
    }
    studentListRecycler.addOnItemClickListener(object: OnItemClickListener {
        override fun onItemClicked(position: Int, view: View) {
            // Your logic
            Student_name=mStudentList.get(position).name
            stud_img=mStudentList.get(position).photo
            stud_id=mStudentList.get(position).id
            studClass=mStudentList.get(position).section
            PreferenceData().setStudentID(mcontext,stud_id)
            PreferenceData().setStudentName(mcontext,Student_name)
            PreferenceData().setStudentPhoto(mcontext,stud_img)
            PreferenceData().setStudentClass(mcontext,studClass)
            studentName.text=Student_name
            if(!stud_img.equals(""))
            {
                Glide.with(mcontext) //1
                    .load(stud_img)
                    .placeholder(R.drawable.student)
                    .error(R.drawable.student)
                    .skipMemoryCache(true) //2
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                    .transform(CircleCrop()) //4
                    .into(studImg)
            }
            else{
                studImg.setImageResource(R.drawable.student)
            }
            getTripList(categoryID)
            dialog.dismiss()
        }
    })
    dialog.show()
}
   /* fun showStudentSelection(mStudentArray: java.util.ArrayList<StudentDataModel.DataItem>) {
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
                        studentName.setText(mStudentArray[position].getName())
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

                    }

                    fun onLongClickItem(v: View?, position: Int) {
                        println("On Long Click Item interface")
                    }
                })
        )
        dialog.show()
    }*/

}