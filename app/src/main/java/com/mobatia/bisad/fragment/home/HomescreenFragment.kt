package com.mobatia.bisad.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.kyanogen.signatureview.SignatureView
import com.mobatia.bisad.BuildConfig
import com.mobatia.bisad.R
import com.mobatia.bisad.WebviewActivity
import com.mobatia.bisad.activity.common.LoginActivity
import com.mobatia.bisad.activity.home.DataCollectionActivity
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.home.PageView
import com.mobatia.bisad.activity.home.model.HealthInsuranceDetailAPIModel
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.constants.NaisClassNameConstants
import com.mobatia.bisad.constants.NaisTabConstants
import com.mobatia.bisad.fragment.apps.AppsFragment
import com.mobatia.bisad.fragment.calendar_new.CalFragment
import com.mobatia.bisad.fragment.canteen.CanteenFragment
import com.mobatia.bisad.fragment.communication.CommunicationFragment
import com.mobatia.bisad.fragment.contact_us.ContactUsFragment
import com.mobatia.bisad.fragment.forms.FormsFragment
import com.mobatia.bisad.fragment.home.model.*
import com.mobatia.bisad.fragment.home.model.datacollection.*
import com.mobatia.bisad.fragment.home.model.reenrollment.*
import com.mobatia.bisad.fragment.messages.MessageFragment
import com.mobatia.bisad.fragment.parents_meeting.ParentsMeetingFragment
import com.mobatia.bisad.fragment.payment.PaymentFragment
import com.mobatia.bisad.fragment.permission_slip.PermissionSlipFragment
import com.mobatia.bisad.fragment.report_absence.ReportAbsenceFragment
import com.mobatia.bisad.fragment.reports.ReportsFragment
import com.mobatia.bisad.fragment.settings.adapter.TriggerAdapter
import com.mobatia.bisad.fragment.settings.model.TriggerDataModel
import com.mobatia.bisad.fragment.settings.model.TriggerUSer
import com.mobatia.bisad.fragment.socialmedia.SocialMediaFragment
import com.mobatia.bisad.fragment.student_information.StudentInformationFragment
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.fragment.teacher_contact.TeacherContactFragment
import com.mobatia.bisad.fragment.termdates.TermDatesFragment
import com.mobatia.bisad.fragment.time_table.TimeTableFragment
import com.mobatia.bisad.manager.AppController
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


lateinit var relone: RelativeLayout
lateinit var reltwo: RelativeLayout
lateinit var relthree: RelativeLayout
lateinit var relfour: RelativeLayout
lateinit var relfive: RelativeLayout
lateinit var relsix: RelativeLayout
lateinit var relseven: RelativeLayout
lateinit var releight: RelativeLayout
lateinit var relnine: RelativeLayout
lateinit var reltxtnine: TextView

lateinit var relTxtone: TextView
lateinit var relTxttwo: TextView
lateinit var relTxtfive: TextView
lateinit var relTxtsix: TextView
lateinit var relTxtseven: TextView
lateinit var relTxteight: TextView
lateinit var relTxtthree: TextView
lateinit var relTxtfour: TextView

lateinit var relImgone: ImageView
lateinit var relImgtwo: ImageView
lateinit var relImgthree: ImageView
lateinit var relImgfour: ImageView
lateinit var relImgfive: ImageView
lateinit var relImgsix: ImageView
lateinit var relImgseven: ImageView
lateinit var relImgeight: ImageView
lateinit var relImgnine: ImageView
var versionfromapi: String = ""
var currentversion: String = ""


lateinit var mSectionText: Array<String?>
lateinit var homeActivity: HomeActivity
lateinit var appController: AppController
lateinit var listitems: Array<String>
lateinit var studentListArrayList: ArrayList<StudentList>
lateinit var titlesListArrayList: ArrayList<TitlesArrayList>
lateinit var countryistArrayList: ArrayList<CountryiesDetailModel>
lateinit var relationshipArrayList: ArrayList<RelationShipDetailModel>
lateinit var ownContactArrayList: ArrayList<OwnDetailsModel>
lateinit var kinDetailArrayList: ArrayList<KinDetailsModel>
lateinit var passportArrayList: ArrayList<PassportDetailModel>
lateinit var healthDetailArrayList: ArrayList<HealthInsuranceDetailModel>
lateinit var ownContactDetailSaveArrayList: ArrayList<OwnContactModel>
lateinit var kinDetailSaveArrayList: ArrayList<KinDetailApiModel>
lateinit var passportSaveArrayList: ArrayList<PassportApiModel>
lateinit var healthSaveArrayList: ArrayList<HealthInsuranceDetailAPIModel>
lateinit var mListImgArrays: TypedArray
lateinit var TouchedView: View
lateinit var pager_rel:RelativeLayout

lateinit var classNameConstants: NaisClassNameConstants

//lateinit var TAB_ID: String
private var TAB_ID: String = ""
private var CLICKED: String = ""
private var INTENT_TAB_ID: String = ""
private var TABIDfragment: String = ""
private var usertype: String = ""
private var USERDATA: String = ""
private var previousTriggerType: Int = 0

 var studentName: String=""
 var studentId: String=""
 var studentImg: String=""
 var studentClass: String=""
 var studntCount: Int=0

lateinit var naisTabConstants: NaisTabConstants
lateinit var sharedprefs: PreferenceData

lateinit var pager: ViewPager
var isDraggable: Boolean = false
lateinit var mContext: Context
lateinit var current_date:String


@Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS",
    "ControlFlowWithEmptyBody"
)
class HomescreenFragment : Fragment(), View.OnClickListener {

   lateinit var studDetailList:ArrayList<StudentReEnrollList>
   lateinit var reEnrollOptionList:ArrayList<String>

    var currentPage: Int = 0
    lateinit var jsonConstans: JsonConstants

    var bannerarray = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_homescreen, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet", "Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerarray = ArrayList()
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        homeActivity = activity as HomeActivity
        appController = AppController()
        CLICKED = homeActivity.sPosition.toString()
        naisTabConstants = NaisTabConstants()
        classNameConstants = NaisClassNameConstants()
        listitems = resources.getStringArray(R.array.navigation_items_guest)
        mListImgArrays = context!!.resources.obtainTypedArray(R.array.navigation_icons_guest)
        previousTriggerType = sharedprefs.getTriggerType(mContext)
        currentversion = BuildConfig.VERSION_NAME
        studDetailList= ArrayList()
        reEnrollOptionList= ArrayList()
        initializeUI()


        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck) {
            getbannerimages()

        } else {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }
        if (sharedprefs.getUserCode(mContext).equals("")) {

        } else {
            if (internetCheck) {
                callStudentListApi()
                callTilesListApi()
                callCountryListApi()
                callRelationshipApi()
                callSettingsUserDetail()
            } else {
                InternetCheckClass.showSuccessInternetAlert(mContext)
            }

        }

        setListeners()
        setdraglisteners()
        getButtonBgAndTextImages()

    }

    private fun setListeners() {
        relone.setOnClickListener(this)
        reltwo.setOnClickListener(this)
        relthree.setOnClickListener(this)
        relfour.setOnClickListener(this)
        relfive.setOnClickListener(this)
        relsix.setOnClickListener(this)
        relseven.setOnClickListener(this)
        releight.setOnClickListener(this)
        relnine.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v == relone) {
            INTENT_TAB_ID = sharedprefs.getbuttononetabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == reltwo) {

            INTENT_TAB_ID = sharedprefs.getbuttontwotabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relthree) {

            INTENT_TAB_ID = sharedprefs.getbuttonthreetabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relfour) {

            INTENT_TAB_ID = sharedprefs.getbuttonfourtabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relfive) {

            INTENT_TAB_ID = sharedprefs.getbuttonfivetabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relsix) {

            INTENT_TAB_ID = sharedprefs.getbuttonsixtabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relseven) {

            INTENT_TAB_ID = sharedprefs.getbuttonseventabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == releight) {

            INTENT_TAB_ID = sharedprefs.getbuttoneighttabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relnine) {

            INTENT_TAB_ID = sharedprefs.getbuttonninetabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }

    }

    private fun getButtonBgAndTextImages() {

        if (sharedprefs
                .getbuttononetextimage(mContext)!!.toInt() != 0
        ) {
            relImgone.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttononetextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr =
                if (listitems[sharedprefs
                        .getbuttononetextimage(mContext)!!.toInt()].equals(
                        classNameConstants.CCAS,
                        ignoreCase = true
                    )
                ) {
                    classNameConstants.CCAS
                } else if (listitems[sharedprefs
                        .getbuttononetextimage(mContext)!!.toInt()].equals(
                        classNameConstants.STUDENT_INFORMATION,
                        ignoreCase = true
                    )
                ) {
                    classNameConstants.STUDENT
                } else {
                    listitems[sharedprefs
                                        .getbuttononetextimage(mContext)!!.toInt()].uppercase(Locale.getDefault())
                }
            relTxtone.text = relTwoStr
            relTxtone.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relone.setBackgroundColor(
                sharedprefs
                    .getButtonOneGuestBg(mContext)
            )
        }
        if (sharedprefs.getbuttontwotextimage(mContext)!!.toInt() != 0
        ) {
            relImgtwo.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttontwotextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr =
                if (listitems[sharedprefs
                        .getbuttontwotextimage(mContext)!!.toInt()].equals(
                        classNameConstants.CCAS,
                        ignoreCase = true
                    )
                ) {
                    classNameConstants.CCAS
                } else if (listitems[sharedprefs
                        .getbuttontwotextimage(mContext)!!.toInt()].equals(
                        classNameConstants.STUDENT_INFORMATION,
                        ignoreCase = true
                    )
                ) {
                    classNameConstants.STUDENT
                } else {
                    listitems[sharedprefs
                                        .getbuttontwotextimage(mContext)!!.toInt()].uppercase(Locale.getDefault())
                }
            relTxttwo.text = relTwoStr
            relTxttwo.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            reltwo.setBackgroundColor(
                sharedprefs
                    .getButtontwoGuestBg(mContext)
            )
        }
        if (sharedprefs
                .getbuttonthreetextimage(mContext)!!.toInt() != 0
        ) {
            relImgthree.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonthreetextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonthreetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else if (listitems[sharedprefs
                    .getbuttonthreetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.STUDENT_INFORMATION,
                    ignoreCase = true
                )
            ) {
                classNameConstants.STUDENT
            } else {
                listitems[sharedprefs
                                .getbuttonthreetextimage(mContext)!!.toInt()].uppercase(Locale.getDefault())
            }
            relTxtthree.text = relTwoStr
            relTxtthree.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relthree.setBackgroundColor(
                sharedprefs
                    .getButtonthreeGuestBg(mContext)
            )
        }


        if (sharedprefs
                .getbuttonfourtextimage(mContext)!!.toInt() != 0
        ) {
            relImgfour.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonfourtextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonfourtextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else if (listitems[sharedprefs
                    .getbuttonfourtextimage(mContext)!!.toInt()].equals(
                    classNameConstants.STUDENT_INFORMATION,
                    ignoreCase = true
                )
            ) {
                classNameConstants.STUDENT
            } else {
                listitems[sharedprefs
                                .getbuttonfourtextimage(mContext)!!.toInt()].uppercase(Locale.getDefault())
            }
            relTxtfour.text = relTwoStr
            relTxtfour.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relfour.setBackgroundColor(
                sharedprefs
                    .getButtonfourGuestBg(mContext)
            )
        }


        if (sharedprefs
                .getbuttonfivetextimage(mContext)!!.toInt() != 0
        ) {
            relImgfive.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonfivetextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonfivetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else if (listitems[sharedprefs
                    .getbuttonfivetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.STUDENT_INFORMATION,
                    ignoreCase = true
                )
            ) {
                classNameConstants.STUDENT
            } else {
                listitems[sharedprefs
                                .getbuttonfivetextimage(mContext)!!.toInt()].uppercase(Locale.getDefault())
            }
            relTxtfive.text = relTwoStr
            relTxtfive.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relfive.setBackgroundColor(
                sharedprefs
                    .getButtonfiveGuestBg(mContext)
            )
        }
        if (sharedprefs.getbuttonsixtextimage(mContext)!!.toInt() != 0) {
            relImgsix.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonsixtextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonsixtextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else if (listitems[sharedprefs
                    .getbuttonsixtextimage(mContext)!!.toInt()].equals(
                    classNameConstants.STUDENT_INFORMATION,
                    ignoreCase = true
                )
            ) {
                classNameConstants.STUDENT
            } else {
                listitems[sharedprefs
                                .getbuttonsixtextimage(mContext)!!.toInt()].uppercase(Locale.ROOT)
            }
            relTxtsix.text = relTwoStr
            relTxtsix.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relsix.setBackgroundColor(
                sharedprefs
                    .getButtonsixGuestBg(mContext)
            )
        }
        if (sharedprefs
                .getbuttonseventextimage(mContext)!!.toInt() != 0
        ) {
            relImgseven.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonseventextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonseventextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else {
                listitems[sharedprefs
                                .getbuttonseventextimage(mContext)!!.toInt()].uppercase(Locale.getDefault())
            }
            relTxtseven.text = relTwoStr
            relTxtseven.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relseven.setBackgroundColor(
                sharedprefs
                    .getButtonsevenGuestBg(mContext)
            )
        }
        if (sharedprefs
                .getbuttoneighttextimage(mContext)!!.toInt() != 0
        ) {
            relImgeight.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttoneighttextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttoneighttextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else {
                listitems[sharedprefs
                                .getbuttoneighttextimage(mContext)!!.toInt()].uppercase(Locale.getDefault())
            }
            relTxteight.text = relTwoStr
            relTxteight.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            releight.setBackgroundColor(
                sharedprefs
                    .getButtoneightGuestBg(mContext)
            )
        }
        if (sharedprefs
                .getbuttonninetextimage(mContext)!!.toInt() != 0
        ) {
            relImgnine.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonninetextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonninetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else {
                listitems[sharedprefs
                                .getbuttonninetextimage(mContext)!!.toInt()].uppercase(Locale.ROOT)
            }
            reltxtnine.text = relTwoStr
            reltxtnine.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relnine.setBackgroundColor(
                sharedprefs
                    .getButtonnineGuestBg(mContext)
            )
        }


    }

    private fun setdraglisteners() {
        relone.setOnDragListener(DropListener())
        reltwo.setOnDragListener(DropListener())
        relthree.setOnDragListener(DropListener())
        relfour.setOnDragListener(DropListener())
        relfive.setOnDragListener(DropListener())
        relsix.setOnDragListener(DropListener())
        relseven.setOnDragListener(DropListener())
        releight.setOnDragListener(DropListener())
        relnine.setOnDragListener(DropListener())
    }

    @Suppress("EqualsBetweenInconvertibleTypes")
    class DropListener : View.OnDragListener {
        override fun onDrag(v: View?, event: DragEvent?): Boolean {

            when (event?.action) {
               /* DragEvent.ACTION_DRAG_STARTED -> Log.d("DRAG", "START")
                DragEvent.ACTION_DRAG_ENTERED -> Log.d("DRAG", "ENTERED")
                DragEvent.ACTION_DRAG_EXITED -> Log.d("DRAG", "EXITED")*/
                DragEvent.ACTION_DROP -> {
                    val intArray = IntArray(2)
                    v?.getLocationInWindow(intArray)
                    val x = intArray[0]
                    val y = intArray[1]
                    val sPosition = homeActivity.sPosition
                    getButtonViewTouched(x.toFloat().toInt(), y.toFloat().toInt())
                    mSectionText[0] = relTxtone.text.toString().uppercase(Locale.ENGLISH)
                    mSectionText[1] = relTxttwo.text.toString().uppercase(Locale.ENGLISH)
                    mSectionText[2] = relTxtthree.text.toString().uppercase(Locale.ENGLISH)
                    mSectionText[3] = relTxtfour.text.toString().uppercase(Locale.ENGLISH)
                    mSectionText[4] = relTxtfive.text.toString().uppercase(Locale.ENGLISH)
                    mSectionText[5] = relTxtsix.text.toString().uppercase(Locale.ENGLISH)
                    mSectionText[6] = relTxtseven.text.toString().uppercase(Locale.ENGLISH)
                    mSectionText[7] = relTxteight.text.toString().uppercase(Locale.ENGLISH)
                    mSectionText[8] = reltxtnine.text.toString().uppercase(Locale.ENGLISH)

                    for (i in mSectionText.indices) {
                        isDraggable = true
                        if (mSectionText[i].equals(
                                listitems[homeActivity.sPosition],
                                ignoreCase = true
                            )
                        ) {
                            isDraggable = false
                            break
                        }
                    }
                    if (isDraggable) {
                        getButtonDrawablesAndText(TouchedView, homeActivity.sPosition)

                    } else {

                        Toast.makeText(mContext, "Item Already Exists !!!", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                //DragEvent.ACTION_DRAG_ENDED -> Log.d("DRAG", "END")


            }


            return true

        }

        private fun getButtonDrawablesAndText(touchedView: View, sPosition: Int) {
            if (sPosition != 0) {
                if (touchedView == relone) {
                    relImgone.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    relTxtone.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttononetabid(mContext, TAB_ID)
                    //setBackgroundColorForView(appController.listitemArrays[sPosition], relone)
                    setBackgroundColorForView(listitems[sPosition], relone)
                    sharedprefs.setbuttononetextimage(mContext, sPosition.toString())
                } else if (touchedView == reltwo) {
                    relImgtwo.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    relTxttwo.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttontwotabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], reltwo)
                    sharedprefs.setbuttontwotextimage(mContext, sPosition.toString())
                } else if (touchedView == relthree) {
                    relImgthree.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    relTxtthree.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonthreetabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relthree)
                    sharedprefs.setbuttonthreetextimage(mContext, sPosition.toString())
                } else if (touchedView == relfour) {
                    relImgfour.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    relTxtfour.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonfourtabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relfour)
                    sharedprefs.setbuttonfourtextimage(mContext, sPosition.toString())
                } else if (touchedView == relfive) {
                    relImgfive.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    relTxtfive.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonfivetabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relfive)
                    sharedprefs.setbuttonfivetextimage(mContext, sPosition.toString())
                } else if (touchedView == relsix) {
                    relImgsix.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    relTxtsix.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonsixtabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relsix)
                    sharedprefs.setbuttonsixtextimage(mContext, sPosition.toString())
                } else if (touchedView == relseven) {
                    relImgseven.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    relTxtseven.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonseventabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relseven)
                    sharedprefs.setbuttonseventextimage(mContext, sPosition.toString())
                } else if (touchedView == releight) {
                    relImgeight.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    relTxteight.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttoneighttabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], releight)
                    sharedprefs.setbuttoneighttextimage(mContext, sPosition.toString())
                } else if (touchedView == relnine) {
                    relImgnine.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].uppercase(Locale.getDefault())
                    }
                    reltxtnine.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonninetabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relnine)
                    sharedprefs.setbuttonninetextimage(mContext, sPosition.toString())
                }

            }
        }

        private fun setBackgroundColorForView(s: String, v: View) {
            if (v == relone) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == reltwo) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relthree) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relfour) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relfive) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rel_five))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relsix) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relseven) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == releight) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relnine) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            }
        }

        private fun saveButtonBgColor(v: View, color: Int) {
            if (v == relone) {
                sharedprefs.setButtonOneGuestBg(mContext, color)
            } else if (v == reltwo) {
                sharedprefs.setButtontwoGuestBg(mContext, color)
            } else if (v == relthree) {
                sharedprefs.setButtonthreeGuestBg(mContext, color)
            } else if (v == relfour) {
                sharedprefs.setButtonfourGuestBg(mContext, color)
            } else if (v == relfive) {
                sharedprefs.setButtonfiveGuestBg(mContext, color)
            } else if (v == relsix) {
                sharedprefs.setButtonsixGuestBg(mContext, color)
            } else if (v == relseven) {
                sharedprefs.setButtonsevenGuestBg(mContext, color)
            } else if (v == releight) {
                sharedprefs.setButtoneightGuestBg(mContext, color)
            } else if (v == relnine) {
                sharedprefs.setButtonnineGuestBg(mContext, color)
            }

        }

        private fun getTabId(textdata: String) {
            when {

                textdata.equals(classNameConstants.STUDENT_INFORMATION) -> {
                    TAB_ID = naisTabConstants.TAB_STUDENT_INFORMATION

                }
                textdata.equals("Student Information") -> {
                    TAB_ID = naisTabConstants.TAB_STUDENT_INFORMATION

                }

                textdata.equals(classNameConstants.CALENDAR, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_CALENDAR

                }
                textdata.equals(classNameConstants.MESSAGES, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_MESSAGES
                }
                textdata.equals(classNameConstants.COMMUNICATION, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_COMMUNICATION
                }
                textdata.equals(classNameConstants.REPORT_ABSENCE, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_REPORT_ABSENCE
                }
                textdata.equals(classNameConstants.TEACHER_CONTACT, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_TEACHER_CONTACT
                }
                textdata.equals(classNameConstants.SOCIAL_MEDIA, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_SOCIAL_MEDIA
                }
                textdata.equals(classNameConstants.TIME_TABLE, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_TIME_TABLE
                }
                textdata.equals(classNameConstants.TERM_DATES, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_TERM_DATES
                }

                textdata.equals(classNameConstants.CONTACT_US, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_CONTACT_US

                }
                textdata.equals(classNameConstants.APPS, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_APPS

                } textdata.equals(classNameConstants.FORMS, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_FORMS

                }
                textdata.equals(classNameConstants.REPORTS, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_REPORTS

                }
//                textdata.equals(classNameConstants.ATTENDANCE, ignoreCase = true) -> {
//                    TAB_ID = naisTabConstants.TAB_ATTENDANCE
//
//                }
                textdata.equals(classNameConstants.CURRICULUM, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_CURRICULUM

                }
//                textdata.equals(classNameConstants.UPDATE_ACCOUNT_DETAILS, ignoreCase = true) -> {
//                    TAB_ID = naisTabConstants.TAB_UPDATE
//
//                }
                textdata.equals(classNameConstants.STAFF_DIRECTORY, ignoreCase = true) -> {
                TAB_ID = naisTabConstants.TAB_STAFF_DIRECTORY

            }textdata.equals(classNameConstants.PERMISSION_SLIPS, ignoreCase = true) -> {
                TAB_ID = naisTabConstants.TAB_PERMISSION_SLIPS

            }
                textdata.equals(classNameConstants.PARENT_MEETINGS, ignoreCase = true) -> {
                TAB_ID = naisTabConstants.TAB_PARENT_MEETINGS

            } textdata.equals(classNameConstants.CANTEEN, ignoreCase = true) -> {
                TAB_ID = naisTabConstants.TAB_CANTEEN

            } textdata.equals(classNameConstants.PAYMENT, ignoreCase = true) -> {
                TAB_ID = naisTabConstants.TAB_PAYMENT

            }

            }

        }

        private fun getButtonViewTouched(centerX: Int, centerY: Int) {
            val array1 = IntArray(2)
            relone.getLocationInWindow(array1)
            val x1: Int = array1[0]
            val x2 = x1 + relone.width
            val y1: Int = array1[1]
            val y2 = y1 + relone.height

            val array2 = IntArray(2)
            reltwo.getLocationInWindow(array2)
            val x3: Int = array2[0]
            val x4 = x3 + reltwo.width
            val y3: Int = array2[1]
            val y4 = y3 + reltwo.height

            val array3 = IntArray(2)
            relthree.getLocationInWindow(array3)
            val x5: Int = array3[0]
            val x6 = x5 + relthree.width
            val y5: Int = array3[1]
            val y6 = y5 + relthree.height

            val array4 = IntArray(2)
            relfour.getLocationInWindow(array4)
            val x7: Int = array4[0]
            val x8 = x7 + relfour.width
            val y7: Int = array4[1]
            val y8 = y7 + relfour.height

            val array5 = IntArray(2)
            relfive.getLocationInWindow(array5)
            val x9: Int = array5[0]
            val x10 = x9 + relfive.width
            val y9: Int = array5[1]
            val y10 = y9 + relfive.height

            val array6 = IntArray(2)
            relsix.getLocationInWindow(array6)
            val x11: Int = array6[0]
            val x12 = x11 + relsix.width
            val y11: Int = array6[1]
            val y12 = y11 + relsix.height

            val array7 = IntArray(2)
            relseven.getLocationInWindow(array7)
            val x13: Int = array7[0]
            val x14 = x13 + relseven.width
            val y13: Int = array7[1]
            val y14 = y13 + relseven.height

            val array8 = IntArray(2)
            releight.getLocationInWindow(array8)
            val x15: Int = array8[0]
            val x16 = x15 + releight.width
            val y15: Int = array8[1]
            val y16 = y15 + releight.height

            val array9 = IntArray(2)
            relnine.getLocationInWindow(array9)
            val x17: Int = array9[0]
            val x18 = x17 + relnine.width
            val y17: Int = array9[1]
            val y18 = y17 + relnine.height

            if (centerX in x1..x2 && y1 <= centerY && centerY <= y2) {
                TouchedView = relone
            } else if (centerX in x3..x4 && y3 <= centerY && centerY <= y4) {
                TouchedView = reltwo
            } else if (centerX in x5..x6 && y5 <= centerY && centerY <= y6) {
                TouchedView = relthree
            } else if (centerX in x7..x8 && y7 <= centerY && centerY <= y8) {
                TouchedView = relfour
            } else if (centerX in x9..x10 && y9 <= centerY && centerY <= y10) {
                TouchedView = relfive
            } else if (centerX in x11..x12 && y11 <= centerY && centerY <= y12) {
                TouchedView = relsix
            } else if (centerX in x13..x14 && y13 <= centerY && centerY <= y14) {
                TouchedView = relseven
            } else if (centerX in x15..x16 && y15 <= centerY && centerY <= y16) {
                TouchedView = releight
            } else if (centerX in x17..x18 && y17 <= centerY && centerY <= y18) {
                TouchedView = relnine
            }

        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initializeUI() {
        pager = view!!.findViewById<ViewPager>(R.id.bannerImagePager)
        relone = view!!.findViewById(R.id.relOne) as RelativeLayout
        reltwo = view!!.findViewById(R.id.relTwo) as RelativeLayout
        relthree = view!!.findViewById(R.id.relThree) as RelativeLayout
        relfour = view!!.findViewById(R.id.relFour) as RelativeLayout
        relfive = view!!.findViewById(R.id.relFive) as RelativeLayout
        relsix = view!!.findViewById(R.id.relSix) as RelativeLayout
        relseven = view!!.findViewById(R.id.relSeven) as RelativeLayout
        releight = view!!.findViewById(R.id.relEight) as RelativeLayout
        relnine = view!!.findViewById(R.id.relNine) as RelativeLayout

        relTxtone = view!!.findViewById(R.id.relTxtOne) as TextView
        relTxttwo = view!!.findViewById(R.id.relTxtTwo) as TextView
        relTxtthree = view!!.findViewById(R.id.relTxtThree) as TextView
        relTxtfour = view!!.findViewById(R.id.relTxtFour) as TextView
        relTxtfive = view!!.findViewById(R.id.relTxtFive) as TextView
        relTxtsix = view!!.findViewById(R.id.relTxtSix) as TextView
        relTxtseven = view!!.findViewById(R.id.relTxtSeven) as TextView
        relTxteight = view!!.findViewById(R.id.relTxtEight) as TextView
        reltxtnine = view!!.findViewById(R.id.relTxtNine) as TextView

        relImgone = view!!.findViewById(R.id.relImgOne) as ImageView
        relImgtwo = view!!.findViewById(R.id.relImgTwo) as ImageView
        relImgthree = view!!.findViewById(R.id.relImgThree) as ImageView
        relImgfour = view!!.findViewById(R.id.relImgFour) as ImageView
        relImgfive = view!!.findViewById(R.id.relImgFive) as ImageView
        relImgsix = view!!.findViewById(R.id.relImgSix) as ImageView
        relImgseven = view!!.findViewById(R.id.relImgSeven) as ImageView
        relImgeight = view!!.findViewById(R.id.relImgEight) as ImageView
        relImgnine = view!!.findViewById(R.id.relImgNine) as ImageView
        mSectionText = arrayOfNulls(9)

        pager_rel=view!!.findViewById(R.id.pager_rel)
        updatedata()


        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
            }

        })

    }

    fun updatedata() {
        val handler = Handler()


        val update = Runnable {
            if (currentPage == bannerarray.size) {
                currentPage = 0
                pager.setCurrentItem(
                    currentPage,
                    true
                )
            } else {
                pager
                    .setCurrentItem(currentPage++, true)
            }
        }
        val swipetimer = Timer()

        swipetimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 100, 6000)

    }


    private fun CHECKINTENTVALUE(intentTabId: String) {
        TAB_ID = intentTabId
        var mFragment: Fragment? = null
        if (sharedprefs.getUserCode(mContext).equals("")) {
            when (intentTabId) {
                naisTabConstants.TAB_STUDENT_INFORMATION -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_CALENDAR -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }

                naisTabConstants.TAB_MESSAGES -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_COMMUNICATION -> {
                    mFragment = CommunicationFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_REPORT_ABSENCE -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_TEACHER_CONTACT -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_SOCIAL_MEDIA -> {
                    mFragment = SocialMediaFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_TIME_TABLE -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_REPORTS -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
//                naisTabConstants.TAB_ATTENDANCE -> {
//                    showSuccessAlert(
//                        mContext,
//                        "This feature is only available for registered users.",
//                        "Alert"
//                    )
//                }
//                naisTabConstants.TAB_CURRICULUM -> {
//                    showSuccessAlert(
//                        mContext,
//                        "This feature is only available for registered users.",
//                        "Alert"
//                    )
//                }
                naisTabConstants.TAB_TERM_DATES -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
//                naisTabConstants.TAB_UPDATE -> {
//                    showSuccessAlert(
//                        mContext,
//                        "This feature is only available for registered users.",
//                        "Alert"
//                    )
//                }
                naisTabConstants.TAB_CONTACT_US -> {
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        checkpermission()


                    } else {
                        mFragment = ContactUsFragment()
                        fragmentIntent(mFragment)
                    }
                }
                naisTabConstants.TAB_APPS -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_FORMS -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
//                naisTabConstants.TAB_STAFF_DIRECTORY -> {
//                mFragment = StaffDirectoryFragment()
//                fragmentIntent(mFragment)
//                }
                naisTabConstants.TAB_PERMISSION_SLIPS -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_PARENT_MEETINGS -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_PAYMENT -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_CANTEEN -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }

            }
        } else {
            Log.e("tabid",intentTabId)
            when (intentTabId) {


                naisTabConstants.TAB_STUDENT_INFORMATION -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = StudentInformationFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_CALENDAR -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = CalFragment()
                    fragmentIntent(mFragment)
                }

                naisTabConstants.TAB_MESSAGES -> {
                    mFragment = MessageFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_COMMUNICATION -> {
Log.e("success","Sucess")
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        checkpermissionCommunication()
                        mFragment = CommunicationFragment()
                        fragmentIntent(mFragment)

                    } else {
                        mFragment = CommunicationFragment()
                        fragmentIntent(mFragment)
                    }

                }
                naisTabConstants.TAB_REPORT_ABSENCE -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = ReportAbsenceFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_TEACHER_CONTACT -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = TeacherContactFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_SOCIAL_MEDIA -> {
                    mFragment = SocialMediaFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_TIME_TABLE -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = TimeTableFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_TERM_DATES -> {
                    mFragment = TermDatesFragment()
                    fragmentIntent(mFragment)
                }
//                naisTabConstants.TAB_ATTENDANCE -> {
//                    sharedprefs.setStudentID(mContext, "")
//                    sharedprefs.setStudentName(mContext, "")
//                    sharedprefs.setStudentPhoto(mContext, "")
//                    sharedprefs.setStudentClass(mContext, "")
//                    mFragment = AttendanceFragment()
//                    fragmentIntent(mFragment)
//                }
                naisTabConstants.TAB_REPORTS -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = ReportsFragment()
                    fragmentIntent(mFragment)
                }
//                naisTabConstants.TAB_CURRICULUM -> {
//                    sharedprefs.setStudentID(mContext, "")
//                    sharedprefs.setStudentName(mContext, "")
//                    sharedprefs.setStudentPhoto(mContext, "")
//                    sharedprefs.setStudentClass(mContext, "")
//                    mFragment = CurriculumFragment()
//                    fragmentIntent(mFragment)
//                }
//                naisTabConstants.TAB_UPDATE -> {
//                    if (sharedprefs.getDataCollection(mContext)==1)
//                    {
//                        if(sharedprefs.getDataCollectionShown(mContext)==0)
//                        {
//                            sharedprefs.setSuspendTrigger(mContext,"2")
//                            sharedprefs.setDataCollectionShown(mContext,1)
//                            callSettingsUserDetail()
//
//                        }
//
//                    }
//                    else{
//                        showTriggerDataCollection(mContext,"Confirm?", "Select one or more areas to update", R.drawable.questionmark_icon, R.drawable.round)
//
//                    }
////                    mFragment = CurriculumFragment()
////                    fragmentIntent(mFragment)
//                }
                naisTabConstants.TAB_CONTACT_US -> {
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        checkpermission()


                    } else {
                        mFragment = ContactUsFragment()
                        fragmentIntent(mFragment)
                    }
                    //permissioncheck()


                }
                naisTabConstants.TAB_APPS -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = AppsFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_FORMS -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = FormsFragment()
                    fragmentIntent(mFragment)
                }

//                naisTabConstants.TAB_STAFF_DIRECTORY -> {
//                    sharedprefs.setStudentID(mContext, "")
//                    sharedprefs.setStudentName(mContext, "")
//                    sharedprefs.setStudentPhoto(mContext, "")
//                    sharedprefs.setStudentClass(mContext, "")
//                    mFragment = StaffDirectoryFragment()
//                    fragmentIntent(mFragment)
//                }
                naisTabConstants.TAB_PERMISSION_SLIPS -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = PermissionSlipFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_PARENT_MEETINGS -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = ParentsMeetingFragment()
                    fragmentIntent(mFragment)
                }

                naisTabConstants.TAB_PAYMENT -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = PaymentFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_CANTEEN -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = CanteenFragment()
                    fragmentIntent(mFragment)
                }

            }
        }

    }

    fun getbannerimages() {
        val version = BuildConfig.VERSION_NAME
        val bannerModel = BannerModel(version, 2)
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<ResponseBody> =
            ApiClient.getClient.bannerimages(bannerModel, "Bearer " + token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val bannerresponse = response.body()
                if (bannerresponse != null) {
                    try {

                        val jsonObject = JSONObject(bannerresponse.string())
                        if (jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            if (status == 100) {
                                val responseObj = jsonObject.getJSONObject("responseArray")
                                val dataArray = responseObj.getJSONArray("banner_images")
                                val appVersion = responseObj.optString("android_app_version")
                                var reEnrollStatus=responseObj.optString("enrollment_status")
                                sharedprefs.setAppVersion(mContext, appVersion)
                                versionfromapi =
                                    sharedprefs.getAppVersion(mContext)!!.replace(".", "")
                                currentversion = currentversion.replace(".", "")



                                if (!sharedprefs.getAppVersion(mContext).equals("", true)) {
                                    if (versionfromapi > currentversion) {
                                        showforceupdate(mContext)

                                    }
                                }

                                if (dataArray.length() > 0) {
                                    for (i in 0..dataArray.length()) {
                                        bannerarray.add(dataArray.optString(i))
                                    }
                                    pager.adapter = activity?.let { PageView(it, bannerarray) }
                                } else {
                                    pager.setBackgroundResource(R.drawable.aboutbanner)
                                }
                                if (reEnrollStatus.equals("1")){
                                    val re_enrollvalue= sharedprefs.getreenrollvalue(mContext)
                                    if (re_enrollvalue!!.equals("1")){
                                        getReenrollForm()
                                    }

                                }
                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(mContext)
                                    getbannerimages()

                                } else {
                                    if (status == 103) {
                                        //validation check error

                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
                                    }
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

     fun showforceupdate(mContext: Context) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_updateversion)
        val btnUpdate =
            dialog.findViewById<View>(R.id.btnUpdate) as Button

        btnUpdate.setOnClickListener {
            dialog.dismiss()
            val appPackageName =
                mContext.packageName
            try {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )

            } catch (e: android.content.ActivityNotFoundException) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }

        }
        dialog.show()
    }


    fun fragmentIntent(mFragment: Fragment?) {
        if (mFragment != null) {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.fragment_holder, mFragment, appController.mTitles)
                .addToBackStack(appController.mTitles).commitAllowingStateLoss() //commit
            //.commit()
        }
    }


    fun showSuccessAlert(context: Context, message: String, msgHead: String) {
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
    fun showSuccessReenrollAlert(context: Context, message: String, msgHead: String,dlg:Dialog,d:Dialog) {
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
        iconImageView.setImageResource(R.drawable.tick)
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()
            dlg.dismiss()
            d.dismiss()

        }
        dialog.show()
    }

    fun callSettingsUserDetail() {
        val bannerModel = BannerModel("1.0.0", 2)
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<ResponseBody> =
            ApiClient.getClient.settingsUserDetail(bannerModel, "Bearer " + token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val bannerresponse = response.body()
                if (bannerresponse != null) {
                    try {
                        val jsonObject = JSONObject(bannerresponse.string())
                        if (jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            if (status == 100) {
                                val responseObj = jsonObject.getJSONObject("responseArray")
                                val appVersion = responseObj.optString("android_app_version")
                                val data_collection = responseObj.optInt("data_collection")
                                val trigger_type = responseObj.optInt("trigger_type")
                                val already_triggered = responseObj.optInt("already_triggered")
                                val deleted = responseObj.optInt("deleted")
                                if(deleted==1)
                                {
                                    sharedprefs.setUserCode(mContext,"")
                                    sharedprefs.setUserID(mContext,"")

                                    var dummyOwn=ArrayList<OwnContactModel>()
                                    sharedprefs.setOwnContactDetailArrayList(mContext,dummyOwn)
                                    var dummyKinPass=ArrayList<KinDetailApiModel>()
                                    sharedprefs.setKinDetailPassArrayList(mContext,dummyKinPass)
                                    var dummyKinShow=ArrayList<KinDetailApiModel>()
                                    sharedprefs.setKinDetailArrayList(mContext,dummyKinShow)
                                    var dummyHealth=ArrayList<HealthInsuranceDetailAPIModel>()
                                    sharedprefs.setHealthDetailArrayList(mContext,dummyHealth)
                                    var dummyPassport=ArrayList<PassportApiModel>()
                                    sharedprefs.setPassportDetailArrayList(mContext,dummyPassport)
                                    var dummyStudent=ArrayList<StudentListDataCollection>()
                                    sharedprefs.setStudentArrayList(mContext,dummyStudent)
                                    sharedprefs.setUserEmail(mContext,"")
                                    sharedprefs.setUserCode(mContext,"")
                                    sharedprefs.setUserID(mContext,"")
                                    val mIntent = Intent(activity, LoginActivity::class.java)
                                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    activity!!.startActivity(mIntent)
                                }
                                else
                                {

                                }
                                sharedprefs.setAppVersion(mContext, appVersion)
                                sharedprefs.setDataCollection(mContext, data_collection)
                                sharedprefs.setTriggerType(mContext, trigger_type)
                                sharedprefs.setAlreadyTriggered(mContext, already_triggered)

                                if (sharedprefs.getDataCollection(mContext) == 1)
                                {

                                    if (sharedprefs.getAlreadyTriggered(mContext) == 0) {
                                        callDataCollectionAPI()

                                    } else {
                                        if (previousTriggerType == sharedprefs.getTriggerType(
                                                mContext
                                            )
                                        ) {
                                            if (!sharedprefs.getSuspendTrigger(mContext)
                                                    .equals("1")
                                            ) {
                                                val intent = Intent(
                                                    activity,
                                                    DataCollectionActivity::class.java
                                                )
                                                activity?.startActivity(intent)
                                            }

                                        } else
                                        {
                                            callDataCollectionAPI()
                                        }
                                    }

                                }

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(mContext)
                                    callSettingsUserDetail()

                                } else {
                                    if (status == 103) {
                                        //validation check error

                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
                                    }
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


    private fun callStudentListApi() {
        studentListArrayList = ArrayList()
        var studentSaveArrayList = ArrayList<StudentListDataCollection>()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer " + token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
               // CommonFunctions.faliurepopup(mContext)
                showSessionExpiredPopUp()
            }

            override fun onResponse(
                call: Call<StudentListModel>,
                response: Response<StudentListModel>
            ) {

                if (response.body()!!.status == 100) {
                    studentListArrayList.addAll(response.body()!!.responseArray.studentList)
                    for (i in 0..studentListArrayList.size - 1) {
                        var model = StudentListDataCollection()
                        model.id = studentListArrayList.get(i).id
                        model.name = studentListArrayList.get(i).name
                        model.unique_id = studentListArrayList.get(i).unique_id
                        model.house = studentListArrayList.get(i).house
                        model.photo = studentListArrayList.get(i).photo
                        model.section = studentListArrayList.get(i).section
                        model.studentListClass = studentListArrayList.get(i).studentClass
                        model.isConfirmed = false
                        studentSaveArrayList.add(model)
                    }
                    studentName=studentListArrayList.get(0).name
                    studentImg=studentListArrayList.get(0).photo
                    studentId=studentListArrayList.get(0).id
                    studentClass=studentListArrayList.get(0).section
                    studntCount= studentListArrayList.size
                    sharedprefs.setStudentArrayList(mContext, studentSaveArrayList)


                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callStudentListApi()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }


            }

        })
    }
    private fun showSessionExpiredPopUp() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<ImageView>(R.id.iconImageView)
        icon.setBackgroundResource(R.drawable.round)
        icon.setImageResource(R.drawable.exclamationicon)
        val text = dialog.findViewById<TextView>(R.id.text_dialog)
        val textHead = dialog.findViewById<TextView>(R.id.alertHead)
        text.text = "You will now be logged out."
        textHead.text = "Session Expired"
        val dialogButton = dialog.findViewById<Button>(R.id.btn_Ok)
        dialogButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(mContext, LoginActivity::class.java)
            //PreferenceData.setbackpresskey(mContext, "0")
           // PreferenceData.setAccessToken(mContext, "")
            PreferenceData().setUserCode(mContext, "")
            //PreferenceData.setStaffId(mContext, "")

            mContext.startActivity(intent)
            //finish()
        }

        dialog.show()
    }
    fun callTilesListApi() {
        titlesListArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<TilesListModel> = ApiClient.getClient.titlesList("Bearer " + token)
        call.enqueue(object : Callback<TilesListModel> {
            override fun onFailure(call: Call<TilesListModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(
                call: Call<TilesListModel>,
                response: Response<TilesListModel>
            ) {

                if (response.body()!!.status == 100) {
                    titlesListArrayList.addAll(response.body()!!.responseArray.titlesList)
                    sharedprefs.setTitleArrayList(mContext, titlesListArrayList)

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callTilesListApi()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }


            }

        })
    }

    fun callCountryListApi() {
        countryistArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<CountryListModel> = ApiClient.getClient.countryList("Bearer " + token)
        call.enqueue(object : Callback<CountryListModel> {
            override fun onFailure(call: Call<CountryListModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(
                call: Call<CountryListModel>,
                response: Response<CountryListModel>
            ) {
                if (response.body()!!.status == 100) {
                    countryistArrayList.addAll(response.body()!!.responseArray.countriesList)
                    sharedprefs.setCountryArrayList(mContext, countryistArrayList)

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callCountryListApi()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }


            }

        })
    }

    fun callRelationshipApi() {
        relationshipArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<RelationShipListModel> =
            ApiClient.getClient.relationshipList("Bearer " + token)
        call.enqueue(object : Callback<RelationShipListModel> {
            override fun onFailure(call: Call<RelationShipListModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(
                call: Call<RelationShipListModel>,
                response: Response<RelationShipListModel>
            ) {
                if (response.body()!!.status == 100) {
                    relationshipArrayList.addAll(response.body()!!.responseArray.contactTypesList)
                    sharedprefs.setRelationShipArrayList(mContext, relationshipArrayList)

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callRelationshipApi()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }


            }

        })
    }

    fun callDataCollectionAPI() {
        ownContactArrayList = ArrayList()
        kinDetailArrayList = ArrayList()
        passportArrayList = ArrayList()
        healthDetailArrayList = ArrayList()
        ownContactDetailSaveArrayList = ArrayList()
        passportSaveArrayList = ArrayList()
        healthSaveArrayList = ArrayList()
        kinDetailSaveArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<DataCollectionModel> =
            ApiClient.getClient.dataCollectionDetail("Bearer " + token)
        call.enqueue(object : Callback<DataCollectionModel> {
            override fun onFailure(call: Call<DataCollectionModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

            }

            override fun onResponse(
                call: Call<DataCollectionModel>,
                response: Response<DataCollectionModel>
            ) {
                if (response.body()!!.status == 100) {

                    sharedprefs.setDisplayMessage(
                        mContext,
                        response.body()!!.responseArray.display_message
                    )
                    ownContactArrayList = response.body()!!.responseArray.ownDetailsList
                    kinDetailArrayList = response.body()!!.responseArray.kinDetailsList
                    passportArrayList = response.body()!!.responseArray.passportDetailsList
                    healthDetailArrayList = response.body()!!.responseArray.healthInsurenceList
                    if (ownContactArrayList.size > 0) {
                        for (i in 0..ownContactArrayList.size - 1) {
                            var model = OwnContactModel()
                            model.id = ownContactArrayList.get(i).id
                            model.user_id = ownContactArrayList.get(i).user_id
                            model.title = ownContactArrayList.get(i).title
                            model.name = ownContactArrayList.get(i).name
                            model.last_name = ownContactArrayList.get(i).last_name
                            model.relationship = ownContactArrayList.get(i).relationship
                            model.email = ownContactArrayList.get(i).email
                            model.phone = ownContactArrayList.get(i).phone
                            model.code = ownContactArrayList.get(i).code
                            model.user_mobile = ownContactArrayList.get(i).user_mobile
                            model.address1 = ownContactArrayList.get(i).address1
                            model.address2 = ownContactArrayList.get(i).address2
                            model.address3 = ownContactArrayList.get(i).address3
                            model.town = ownContactArrayList.get(i).town
                            model.state = ownContactArrayList.get(i).state
                            model.country = ownContactArrayList.get(i).country
                            model.pincode = ownContactArrayList.get(i).pincode
                            model.status = ownContactArrayList.get(i).status
                            model.created_at = ownContactArrayList.get(i).created_at
                            model.updated_at = ownContactArrayList.get(i).updated_at
                            model.isUpdated = false
                            model.isConfirmed = false
                            ownContactDetailSaveArrayList.add(model)

                        }

                        if (sharedprefs.getOwnContactDetailArrayList(mContext) == null || sharedprefs.getOwnContactDetailArrayList(
                                mContext
                            )!!.size == 0
                        ) {
                            sharedprefs.setIsAlreadyEnteredOwn(mContext, true)
                            sharedprefs.setOwnContactDetailArrayList(
                                mContext,
                                ownContactDetailSaveArrayList
                            )
                        } else {
                            if (!sharedprefs.getIsAlreadyEnteredOwn(mContext)) {
                                sharedprefs.setIsAlreadyEnteredOwn(mContext, true)
                                sharedprefs.setOwnContactDetailArrayList(
                                    mContext,
                                    ownContactDetailSaveArrayList
                                )
                            }
                        }
                    }

                    if (passportArrayList.size > 0) {
                        for (i in 0..passportArrayList.size - 1) {
                            var mModel = PassportApiModel()
                            mModel.id = passportArrayList.get(i).id
                            mModel.student_unique_id = passportArrayList.get(i).student_unique_id
                            mModel.student_id = passportArrayList.get(i).student_id
                            mModel.student_name = passportArrayList.get(i).student_name
                            mModel.passport_number = passportArrayList.get(i).passport_number
                            mModel.nationality = passportArrayList.get(i).nationality
                            mModel.passport_image = passportArrayList.get(i).passport_image
                            mModel.date_of_issue = passportArrayList.get(i).date_of_issue
                            mModel.expiry_date = passportArrayList.get(i).expiry_date
                            mModel.passport_expired = passportArrayList.get(i).passport_expired
                            mModel.emirates_id_no = passportArrayList.get(i).emirates_id_no
                            mModel.emirates_id_image = passportArrayList.get(i).emirates_id_image
                            mModel.passport_image_name = ""
                            mModel.emirates_id_image_path = ""
                            mModel.emirates_id_image_name = ""
                            mModel.emirates_id_image_path = ""
                            mModel.status = passportArrayList.get(i).status
                            mModel.request = passportArrayList.get(i).request
                            mModel.created_at = passportArrayList.get(i).created_at
                            mModel.updated_at = passportArrayList.get(i).updated_at
                            mModel.is_date_changed = false
                            passportSaveArrayList.add(mModel)
                        }
                        if (sharedprefs.getPassportDetailArrayList(mContext) == null || sharedprefs.getPassportDetailArrayList(
                                mContext
                            )!!.size == 0
                        ) {
                            sharedprefs.setPassportDetailArrayList(mContext, passportSaveArrayList)
                        }

                    }

                    if (healthDetailArrayList.size > 0) {
                        for (i in 0..healthDetailArrayList.size - 1) {
                            var hModel = HealthInsuranceDetailAPIModel()
                            hModel.id = healthDetailArrayList.get(i).id
                            hModel.student_unique_id =
                                healthDetailArrayList.get(i).student_unique_id
                            hModel.student_id = healthDetailArrayList.get(i).student_id
                            hModel.student_name = healthDetailArrayList.get(i).student_name
                            hModel.health_detail = healthDetailArrayList.get(i).health_detail
                            hModel.health_form_link = healthDetailArrayList.get(i).health_form_link
                            hModel.status = healthDetailArrayList.get(i).status
                            hModel.request = healthDetailArrayList.get(i).request
                            hModel.created_at = healthDetailArrayList.get(i).created_at
                            hModel.updated_at = healthDetailArrayList.get(i).updated_at
                            healthSaveArrayList.add(hModel)

                        }
                        if (sharedprefs.getHealthDetailArrayList(mContext) == null || sharedprefs.getHealthDetailArrayList(
                                mContext
                            )!!.size == 0
                        ) {
                            sharedprefs.setHealthDetailArrayList(mContext, healthSaveArrayList)
                        }
                    }
                    if (kinDetailArrayList.size > 0) {
                        for (i in 0..kinDetailArrayList.size - 1) {
                            var mModel = KinDetailApiModel()
                            mModel.id = kinDetailArrayList.get(i).id
                            mModel.user_id = kinDetailArrayList.get(i).user_id
                            mModel.kin_id = kinDetailArrayList.get(i).kin_id
                            mModel.title = kinDetailArrayList.get(i).title
                            mModel.name = kinDetailArrayList.get(i).name
                            mModel.last_name = kinDetailArrayList.get(i).last_name
                            mModel.relationship = kinDetailArrayList.get(i).relationship
                            mModel.email = kinDetailArrayList.get(i).email
                            mModel.phone = kinDetailArrayList.get(i).phone
                            mModel.code = kinDetailArrayList.get(i).code
                            mModel.user_mobile = kinDetailArrayList.get(i).user_mobile
                            mModel.status = kinDetailArrayList.get(i).status
                            mModel.request = kinDetailArrayList.get(i).request
                            mModel.created_at = kinDetailArrayList.get(i).created_at
                            mModel.updated_at = kinDetailArrayList.get(i).updated_at
                            mModel.NewData = false
                            mModel.Newdata = "NO"
                            mModel.isConfirmed = false
                            kinDetailSaveArrayList.add(mModel)

                        }
                        if (sharedprefs.getKinDetailArrayList(mContext) == null || sharedprefs.getKinDetailArrayList(
                                mContext
                            )!!.size == 0
                        ) {

                            sharedprefs.setIsAlreadyEnteredKin(mContext, true)
                            sharedprefs.setKinDetailArrayList(mContext, kinDetailSaveArrayList)
                            sharedprefs.setKinDetailPassArrayList(mContext, kinDetailSaveArrayList)
                        } else {
                            if (!sharedprefs.getIsAlreadyEnteredKin(mContext)) {
                                sharedprefs.setIsAlreadyEnteredKin(mContext, true)
                                sharedprefs.setKinDetailArrayList(mContext, kinDetailSaveArrayList)
                                sharedprefs.setKinDetailPassArrayList(
                                    mContext,
                                    kinDetailSaveArrayList
                                )
                            }
                        }
                    }

                    //Intent
                    if (!sharedprefs.getSuspendTrigger(mContext).equals("1")) {
                        val intent = Intent(mContext, DataCollectionActivity::class.java)
                        activity?.startActivity(intent)
                    }

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callDataCollectionAPI()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }
            }

        })
    }


    private fun checkpermission() {
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CALL_PHONE
                ),
                123
            )
        }
    }

    private fun checkpermissionCommunication() {
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                123
            )
        }
    }
    fun showTriggerDataCollection(context: Context,msgHead:String,msg:String,ico:Int,bgIcon:Int)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_trigger_data_collection)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var checkRecycler = dialog.findViewById(R.id.checkRecycler) as RecyclerView
        var linearLayoutManagerM : LinearLayoutManager = LinearLayoutManager(mContext)
        checkRecycler.layoutManager = linearLayoutManagerM
        checkRecycler.itemAnimator = DefaultItemAnimator()
        iconImageView.setBackgroundResource(bgIcon)
        iconImageView.setImageResource(ico)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        var progressDialog = dialog.findViewById(R.id.progress) as ProgressBar

        text_dialog.text = msg
        alertHead.text = msgHead
        var categoryList= ArrayList<String>()
        categoryList.add("All")
        categoryList.add("Student - Contact Details")
        categoryList.add("Student - Passport & Emirates ID")

        val mTriggerModelArrayList=ArrayList<TriggerDataModel>()
        for (i in 0..categoryList.size-1)
        {
            var model= TriggerDataModel()
            model.categoryName=categoryList.get(i)
            model.checkedCategory=false
            mTriggerModelArrayList.add(model)

        }
        var triggerAdapter= TriggerAdapter(mTriggerModelArrayList)
        checkRecycler.adapter = triggerAdapter
        checkRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (position==0)
                {
                    mTriggerModelArrayList.get(0).checkedCategory=true
                    mTriggerModelArrayList.get(1).checkedCategory=false
                    mTriggerModelArrayList.get(2).checkedCategory=false
                }
                else if (position==1)
                {
                    mTriggerModelArrayList.get(0).checkedCategory=false
                    mTriggerModelArrayList.get(1).checkedCategory=true
                    mTriggerModelArrayList.get(2).checkedCategory=false
                }
                else{
                    mTriggerModelArrayList.get(0).checkedCategory=false
                    mTriggerModelArrayList.get(1).checkedCategory=false
                    mTriggerModelArrayList.get(2).checkedCategory=true
                }

                var triggerAdapter= TriggerAdapter(mTriggerModelArrayList)
                checkRecycler.adapter = triggerAdapter
            }
        })
        btn_Ok.setOnClickListener()
        {
            var valueTrigger:String="0"
            if (mTriggerModelArrayList.get(0).checkedCategory) {
                valueTrigger="1"
            } else if (mTriggerModelArrayList.get(1).checkedCategory) {
                valueTrigger="2"
            } else if (mTriggerModelArrayList.get(2).checkedCategory) {
                valueTrigger="3"
            }

            if (valueTrigger.equals("0")) {
                Toast.makeText(
                    mContext,
                    "Please select any trigger type before confiming",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                progressDialog.visibility=View.VISIBLE
                callDataTriggerApi(valueTrigger,dialog,progressDialog)
            }

            // dialog.dismiss()
        }
        btn_Cancel.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun callDataTriggerApi(value:String,triggerDialog:Dialog,progress:ProgressBar)
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val requestLeaveBody= TriggerUSer(value)
        val call: Call<ResponseBody> = ApiClient.getClient.triggerUser(requestLeaveBody,"Bearer "+token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                progress.visibility=View.GONE
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                progress.visibility=View.GONE
                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if(jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            if (status == 100) {
                                progress.visibility=View.GONE
                                triggerDialog.dismiss()
                                callSettingsUserDetail()
                                // showSuccessDataAlert(mContext,"Alert","\"Update Account Details\" will start next time the Parent App is opened.", R.drawable.questionmark_icon, R.drawable.round)

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(mContext)
                                    callDataTriggerApi(value,triggerDialog,progress)
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
                                    }
                                }

                            }
                        }
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
    fun getReenrollForm() {
       // progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        var descrptn_reenroll:String=""
        val form_reenroll= GetreenrollmentApiModel(sharedprefs.getUserID(mContext)!!)
        val call: Call<GetreenrollmentModel> = ApiClient.getClient.getreenrollmentForm(form_reenroll,"Bearer "+token)
        call.enqueue(object : Callback<GetreenrollmentModel>{
            override fun onFailure(call: Call<GetreenrollmentModel>, t: Throwable) {
                //progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<GetreenrollmentModel>, response: Response<GetreenrollmentModel>) {
                //progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    var heading_reenroll=response.body()!!.responseArray.settingsdata.heading
                    current_date=response.body()!!.responseArray.current_date
                    if(response.body()!!.responseArray.settingsdata.description.isEmpty()){

                    }else{
                        descrptn_reenroll=response.body()!!.responseArray.settingsdata.description
                    }
                   // var descrptn_reenroll=response.body()!!.responseArray.settingsdata.description
                    var tandc_reenroll=response.body()!!.responseArray.settingsdata.t_and_c
                    var user_firstname=response.body()!!.responseArray.user.firstname
                    var user_lastname=response.body()!!.responseArray.user.last_name
                    var person_info_url=response.body()!!.responseArray.settingsdata.statement_url
                    var parent_name=user_firstname+" "+user_lastname
                    var user_email=response.body()!!.responseArray.user.email
                    var question=response.body()!!.responseArray.settingsdata.question
                    var reenrolldetail:ReEnrolldetail=
                        ReEnrolldetail(heading_reenroll,descrptn_reenroll,
                    tandc_reenroll,person_info_url,parent_name,user_email,question)

                    reEnrollOptionList.addAll(response.body()!!.responseArray.settingsdata.options)
                    studDetailList.addAll(response.body()!!.responseArray.student)
                    if(studDetailList.size==0){
                    }else{
                        re_enroll_popup(reenrolldetail,studDetailList,reEnrollOptionList)
                    }


                }else if(response.body()!!.status.equals("116"))
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        getReenrollForm()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status.toInt(),mContext)
                }

            }

        })
    }
    @SuppressLint("WrongConstant")
    fun re_enroll_popup(reEnrolldetail: ReEnrolldetail, studDetailList:ArrayList<StudentReEnrollList>,
    reEnrollOptionList: ArrayList<String>){

        val d = Dialog(mContext)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        d.setCancelable(false)
        d.setContentView(R.layout.dialogue_re_enrollment)

        var total_count:Int=studDetailList.size-1
        var page_count:Int=0

        var check:Int=0
        var question=d.findViewById<TextView>(R.id.question)
        var scrollView=d.findViewById<ScrollView>(R.id.scroll)
        var close_img=d.findViewById<ImageView>(R.id.close_img)
        var header=d.findViewById<TextView>(R.id.header)
        var mailIcon=d.findViewById<LinearLayout>(R.id.linear_mail)
        var prev_btn=d.findViewById<LinearLayout>(R.id.prev_linear)
        var nxt_btn=d.findViewById<LinearLayout>(R.id.nxt_linear)
        var sub_btn=d.findViewById<Button>(R.id.sub_btn)
        var terms_and_condtns=d.findViewById<Button>(R.id.terms_conditions)
        var personal_info=d.findViewById<Button>(R.id.personal_info)
        var save=d.findViewById<TextView>(R.id.save)
        var image_view=d.findViewById<ImageView>(R.id.image_view)
        var stud_img=d.findViewById<ImageView>(R.id.stud_img)
        var stud_name=d.findViewById<TextView>(R.id.stud_name)
        var stud_class=d.findViewById<TextView>(R.id.stud_class)
        var date_field=d.findViewById<EditText>(R.id.textField_date)
        var descrcrptn=d.findViewById<TextView>(R.id.descrptn_txt)
        var parent_name=d.findViewById<EditText>(R.id.textField_parentName)
        var parent_email=d.findViewById<EditText>(R.id.textField_parentEmail)
        var spinnerList=d.findViewById<Spinner>(R.id.spinnerlist)
        var option_txt=d.findViewById<TextView>(R.id.option_txt)
        var clear=d.findViewById<TextView>(R.id.clear)
        var sign_linear=d.findViewById<ConstraintLayout>(R.id.sign_linear)
        var signatureView=d.findViewById<SignatureView>(R.id.signature_view)
        var dropdown_btn=d.findViewById<ImageView>(R.id.dropdown_btn)
        var sign_btn=d.findViewById<Button>(R.id.signature_btn)
        var dropdownList:ArrayList<String>
       // var studDetailList:ArrayList<StudentReEnrollList>

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/mm/yyyy")
        val current = formatter.format(time)

        var myCalendar= Calendar.getInstance()
        var currentDate= Calendar.getInstance().time
        var year=myCalendar[Calendar.YEAR]
        var month=myCalendar[Calendar.MONTH]
        var day=myCalendar[java.util.Calendar.DAY_OF_MONTH]
        var radioButton=d.findViewById<RadioButton>(R.id.check_btn)
        var radioButton_info=d.findViewById<RadioButton>(R.id.check_btn_info)
       // var crnt_date=day.toString()+"/"+month+"/"+year
       var crnt_date=day.toString()+"/"+month+1+"/"+year
        var header_txt= year.toString() + " Re-enrolment and NAE Terms & Conditions"

       // var reEnrollsave:ArrayList<ReEnrollSaveModel>
        var reEnrollsave:ArrayList<ReEnrollSaveModel>
        reEnrollsave=ArrayList()
        var reEnrollsubmit:ArrayList<ReEnrollSubModel>
        reEnrollsubmit=ArrayList()

        mailIcon.setOnClickListener {
            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_send_email_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
            val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
            var text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
            var text_content = dialog.findViewById<View>(R.id.text_content) as EditText

            submitButton.setOnClickListener {
                if (text_dialog.text.toString().trim().equals("")) {
                    InternetCheckClass.showErrorAlert(
                        mContext,
                        "Please enter your subject",
                        "Alert"
                    )

                } else {
                    if (text_content.text.toString().trim().equals("")) {
                        InternetCheckClass.showErrorAlert(
                            mContext,
                            "Please enter your content",
                            "Alert"
                        )

                    } else {
                        // progressDialog.visibility = View.VISIBLE
                        val aniRotate: Animation =
                            AnimationUtils.loadAnimation(context, R.anim.linear_interpolator)
                        // progressDialog.startAnimation(aniRotate)
                        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                        if (internetCheck) {
                            emailvalidationcheck( text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),dialog)
                           /* reEnrollMailApi( text_dialog.text.toString().trim(),
                                text_content.text.toString().trim(),dialog)*/

                        } else {
                            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                        }
                    }
                }
            }
            dialogCancelButton.setOnClickListener { dialog.dismiss()
            }
            dialog.show()
        }
        for (i in 0..studDetailList.size-1){
            var m1=ReEnrollSaveModel("","")
            reEnrollsave.add(i,m1)
        }
        if (page_count==total_count){
            nxt_btn.visibility = GONE
            prev_btn.visibility = GONE
            sub_btn.visibility = VISIBLE
        }
        header.text = reEnrolldetail.heading
        descrcrptn.text = reEnrolldetail.description
        val dateFor= current_date.replace("-","/")
        date_field.setText(dateFor)
        dropdownList= ArrayList()
       // studDetailList= ArrayList()

        stud_name.text = studDetailList[0].name
        stud_class.text = studDetailList[0].section
        var stud_photo= studDetailList[0].photo
        var stud_id=studDetailList[0].id
        dropdownList.add(0,"Please Select")
        for (i in 1..reEnrollOptionList.size) {

                dropdownList.add(i, reEnrollOptionList[i-1])

        }
       /* dropdownList.add(1,"Returning")
        dropdownList.add(2,"Unsure")
        dropdownList.add(3,"Graduating")
        dropdownList.add(4,"Not returning")*/
        var sp_adapter = ArrayAdapter(mContext,
           R.layout.spinner_textview, dropdownList)
        spinnerList.adapter = sp_adapter
        spinnerList.setSelection(0)
        spinnerList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
var optionlistSize=dropdownList.size-1
                for (i in 1..optionlistSize){
                    if (selectedItem==dropdownList[i].toString()){
                        reEnrollsave[page_count].status=dropdownList[i]
                        check=1
                    }else if (selectedItem==dropdownList[0]){
                        check=0
                        reEnrollsave[page_count].status=""
                    }
                }
               /* if (selectedItem == "Graduating") {
                   reEnrollsave[page_count].status="Graduating"
                    check=1
                }else if (selectedItem == "Returning"){
                    check=1
                    reEnrollsave[page_count].status="Returning"
                }else if (selectedItem=="Not returning"){
                    check=1
                    reEnrollsave[page_count].status="Not returning"
                }else if (selectedItem=="Unsure"){
                    check=1
                    reEnrollsave[page_count].status="Unsure"
                }else if (selectedItem=="Please Select"){
                    check=0
                    reEnrollsave[page_count].status=""
                }*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        if(!stud_photo.equals(""))
        {
            Glide.with(mContext)
                .load(stud_photo)
                .placeholder(R.drawable.student)
                .error(R.drawable.student)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(CircleCrop())
                .into(stud_img)
        }
        else{
            stud_img.setImageResource(R.drawable.student)
        }

        if (reEnrolldetail.parent_name.isNotEmpty()){
           parent_name.setText(reEnrolldetail.parent_name)
        }
        if (reEnrolldetail.parent_email.isNotEmpty()){
            parent_email.setText(reEnrolldetail.parent_email)
        }
        if (reEnrolldetail.question.isNotEmpty()){
            question.text = reEnrolldetail.question
        }

        radioButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!radioButton.isSelected) {
                    radioButton.isChecked = true
                   radioButton.isSelected = true

                } else {
                    radioButton.isChecked = false
                    radioButton.isSelected = false

                }

            }
        })
        radioButton_info.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!radioButton_info.isSelected) {
                   radioButton_info.isChecked = true
                    radioButton_info.isSelected = true

                } else {
                    radioButton_info.isChecked = false
                    radioButton_info.isSelected = false

                }


            }
        })

        sign_btn.setOnClickListener {
            sign_linear.visibility = VISIBLE
            dropdown_btn.visibility = VISIBLE
        }
        dropdown_btn.setOnClickListener {

            sign_linear.visibility = GONE
            dropdown_btn.visibility = GONE
        }
        option_txt.setOnClickListener {
            option_txt.visibility = GONE
            spinnerList.visibility = VISIBLE
        }
        terms_and_condtns.setOnClickListener {
            val intent = Intent(mContext, WebviewActivity::class.java)
            intent.putExtra("Url",reEnrolldetail.tandc)
            startActivity(intent)

        }
        personal_info.setOnClickListener {
            val intent = Intent(mContext, WebviewActivity::class.java)
            intent.putExtra("Url",reEnrolldetail.pers_info_url)
            startActivity(intent)
        }
        sub_btn.setOnClickListener {

            if (check==0){
                radioButton.isChecked = false
                for (i in studDetailList.indices){
                    if (reEnrollsave[i].status.isNotEmpty()){
                        reEnrollsave[i].student_id=studDetailList[i].id
                    }
                }
                for (i in reEnrollsave.indices){
                    if (reEnrollsave[i].student_id.isNotEmpty()&&reEnrollsave[i].status.isNotEmpty()) {
                        if (reEnrollsubmit.size == 0) {
                            var r1 =
                                ReEnrollSubModel(reEnrollsave[i].student_id, reEnrollsave[i].status)
                            reEnrollsubmit.add(0, r1)
                        }else{
                        // var r1=ReEnrollSubModel(reEnrollsave[i].student_id,reEnrollsave[i].status)
                        for (j in 1..reEnrollsubmit.size ) {

                            if (reEnrollsave[i].student_id != reEnrollsubmit[j-1].student_id) {
                                var r1 = ReEnrollSubModel(
                                    reEnrollsave[i].student_id,
                                    reEnrollsave[i].status
                                )
                                reEnrollsubmit.add(j, r1)
                            } else {

                            }

                        }
                    }
                    }
                }
                if (reEnrollsubmit.size>0){
                    showerror(mContext,"Do you want to Submit?","Alert",reEnrollsubmit,d)
                }else{
                    showRenrollnoData(mContext,"You didn't enter any data of your child.Please Enter data and Submit",
                        "Alert",d)
                    //showSuccessAlert(mContext,"No Options Selected","Alert")
                }

              // saveReenrollApi(reEnrollsubmit,d)
            }else{
                if (radioButton.isChecked) {
                    if (radioButton_info.isChecked){
                    for (i in studDetailList.indices) {
                        if (reEnrollsave[i].status.isNotEmpty()) {
                            reEnrollsave[i].student_id = studDetailList[i].id
                        }
                    }
                    for (i in reEnrollsave.indices) {
                        if (reEnrollsave[i].student_id.isNotEmpty() && reEnrollsave[i].status.isNotEmpty()) {
                            if (reEnrollsubmit.size == 0) {
                                var r1 =
                                    ReEnrollSubModel(
                                        reEnrollsave[i].student_id,
                                        reEnrollsave[i].status
                                    )
                                reEnrollsubmit.add(0, r1)
                            } else {
                                // var r1=ReEnrollSubModel(reEnrollsave[i].student_id,reEnrollsave[i].status)
                                for (j in 1..reEnrollsubmit.size) {

                                    if (reEnrollsave[i].student_id != reEnrollsubmit[j - 1].student_id) {
                                        var r1 = ReEnrollSubModel(
                                            reEnrollsave[i].student_id,
                                            reEnrollsave[i].status
                                        )
                                        reEnrollsubmit.add(j, r1)
                                    } else {

                                    }

                                }
                            }
                        }
                    }
                    showerror(mContext, "Do you want to Submit?", "Alert", reEnrollsubmit, d)
                    // saveReenrollApi(reEnrollsubmit, d)
                }else{
                    InternetCheckClass.showErrorAlert(mContext,"Make sure you have confirmed all the declarations","Alert")

                }
                }else{
                        InternetCheckClass.showErrorAlert(mContext,"Make sure you have confirmed all the declarations","Alert")
                    }

                }

           /* val gson = Gson()
            val Datas= gson.toJson(reEnrollsubmit)

            var JSON:JSONArray = JSONArray(Datas)
            //var jsonobj:JSONObject= JSONObject(JSON)
            */



        }

        nxt_btn.setOnClickListener {
            if (check==0){
                radioButton.isChecked = false
                radioButton.isSelected=false
                radioButton_info.isChecked=false
                radioButton_info.isSelected=false
                page_count=page_count+1


                if (page_count==studDetailList.size-1){
                    nxt_btn.visibility = GONE
                    sub_btn.visibility = VISIBLE
                    prev_btn.visibility = VISIBLE
                }
                else {
                    nxt_btn.visibility = VISIBLE
                    prev_btn.visibility = VISIBLE
                    sub_btn.visibility = GONE
                }
                if (reEnrollsave[page_count].status.equals("")){
                    spinnerList.setSelection(0)
                    radioButton.isChecked = false
                    radioButton.isSelected=false
                    radioButton_info.isSelected=false
                    radioButton_info.isChecked = false
                }else{
                    for (i in dropdownList.indices){
                        if (reEnrollsave[page_count].status==dropdownList[i]){
                            spinnerList.setSelection(i)
                            radioButton.isChecked = true
                            radioButton_info.isChecked = true
                            radioButton.isSelected=true
                            radioButton_info.isSelected=true
                        }
                    }
                }

                stud_name.text = studDetailList[page_count].name
                stud_class.text = studDetailList[page_count].section
                var stud_photo= studDetailList[page_count].photo
                var stud_id=studDetailList[page_count].id
                if(!stud_photo.equals(""))
                {
                    Glide.with(mContext)
                        .load(stud_photo)
                        .placeholder(R.drawable.student)
                        .error(R.drawable.student)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transform(CircleCrop())
                        .into(stud_img)
                }

            }else{
                if (radioButton.isChecked) {
                    if (radioButton_info.isChecked) {
                    page_count = page_count + 1

                    if (page_count == studDetailList.size - 1) {
                        nxt_btn.visibility = GONE
                        sub_btn.visibility = VISIBLE
                        prev_btn.visibility = VISIBLE
                    } else {
                        nxt_btn.visibility = VISIBLE
                        prev_btn.visibility = VISIBLE
                        sub_btn.visibility = GONE
                    }

                    if (reEnrollsave[page_count].status.equals("")) {
                        spinnerList.setSelection(0)
                        radioButton.isChecked = false
                        radioButton_info.isChecked = false
                        radioButton.isSelected=false
                        radioButton_info.isSelected=false
                    } else {
                        for (i in dropdownList.indices) {
                            if (reEnrollsave[page_count].status == dropdownList[i]) {
                                spinnerList.setSelection(i)
                                radioButton.isChecked = true
                                radioButton_info.isChecked = true
                                radioButton.isSelected=true
                                radioButton_info.isSelected=true
                            }
                        }
                    }

                    stud_name.text = studDetailList[page_count].name
                    stud_class.text = studDetailList[page_count].section
                    var stud_photo = studDetailList[page_count].photo
                    var stud_id = studDetailList[page_count].id
                    if (!stud_photo.equals("")) {
                        Glide.with(mContext)
                            .load(stud_photo)
                            .placeholder(R.drawable.student)
                            .error(R.drawable.student)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .transform(CircleCrop())
                            .into(stud_img)
                    }
                }else{
                        InternetCheckClass.showErrorAlert(mContext,"Make sure you have confirmed all the declarations"
                            ,"Alert")
                }
                }else{
                    InternetCheckClass.showErrorAlert(mContext,"Make sure you have confirmed all the declarations",
                        "Alert")
                }

            }

            scrollView.post(Runnable { //X,Y are scroll positions untill where you want scroll up
                val X = 0
                val Y = 0
                scrollView.scrollTo(X, Y)
            })
            }

        prev_btn.setOnClickListener {
            if (check==0){
                radioButton.isChecked=false
                radioButton_info.isChecked=false
                radioButton.isSelected=false
                radioButton_info.isSelected=false
                page_count=page_count-1


                if (page_count==0){
                    nxt_btn.visibility = VISIBLE
                    prev_btn.visibility = GONE
                    sub_btn.visibility = GONE
                }else if (page_count<studDetailList.size-1){
                    if (page_count==studDetailList.size-1){
                        nxt_btn.visibility = GONE
                        sub_btn.visibility = VISIBLE
                        prev_btn.visibility = VISIBLE
                    }
                    else{
                        nxt_btn.visibility = VISIBLE
                        prev_btn.visibility = VISIBLE
                        sub_btn.visibility = GONE
                    }
                }
                if (reEnrollsave[page_count].status.equals("")){
                    spinnerList.setSelection(0)
                    radioButton.isChecked = false
                    radioButton_info.isChecked = false
                    radioButton.isSelected=false
                    radioButton_info.isSelected=false
                }else{
                    for (i in dropdownList.indices){
                        if (reEnrollsave[page_count].status==dropdownList[i]){
                            spinnerList.setSelection(i)
                            radioButton.isChecked = true
                            radioButton_info.isChecked = true
                            radioButton.isSelected=true
                            radioButton_info.isSelected=true
                        }
                    }
                }

                stud_name.text = studDetailList[page_count].name
                stud_class.text = studDetailList[page_count].section
                var stud_photo= studDetailList[page_count].photo
                var stud_id=studDetailList[page_count].id
                if(!stud_photo.equals(""))
                {
                    Glide.with(mContext)
                        .load(stud_photo)
                        .placeholder(R.drawable.student)
                        .error(R.drawable.student)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transform(CircleCrop())
                        .into(stud_img)
                }
            }else{
                if (radioButton.isChecked){
            page_count=page_count-1


            if (page_count==0){
                nxt_btn.visibility = VISIBLE
                prev_btn.visibility = GONE
                sub_btn.visibility = GONE
            }else if (page_count<studDetailList.size-1){
                if (page_count==studDetailList.size-1){
                    nxt_btn.visibility = GONE
                    sub_btn.visibility = VISIBLE
                    prev_btn.visibility = VISIBLE
                }
                else{
                    nxt_btn.visibility = VISIBLE
                    prev_btn.visibility = VISIBLE
                    sub_btn.visibility = GONE
                }
            }

            if (reEnrollsave[page_count].status.equals("")){
                spinnerList.setSelection(0)
                radioButton.isChecked = false
                radioButton_info.isChecked = false
                radioButton.isSelected=false
                radioButton_info.isSelected=false
            }else{
                for (i in dropdownList.indices){
                    if (reEnrollsave[page_count].status==dropdownList[i]){
                        spinnerList.setSelection(i)
                        radioButton.isChecked = true
                        radioButton_info.isChecked = true
                        radioButton.isSelected=true
                        radioButton_info.isSelected=true
                    }
                }
            }

                    stud_name.text = studDetailList[page_count].name
                    stud_class.text = studDetailList[page_count].section
            var stud_photo= studDetailList[page_count].photo
            var stud_id=studDetailList[page_count].id
            if(!stud_photo.equals(""))
            {
                Glide.with(mContext)
                    .load(stud_photo)
                    .placeholder(R.drawable.student)
                    .error(R.drawable.student)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transform(CircleCrop())
                    .into(stud_img)
            } }else{
                    InternetCheckClass.showErrorAlert(mContext,"Please Fill the Checkbox","Alert")
                }

            }
            scrollView.post(Runnable { //X,Y are scroll positions untill where you want scroll up
                val X = 0
                val Y = 0
                scrollView.scrollTo(X, Y)
            })
        }


        close_img.setOnClickListener {
            sharedprefs.setreenrollvalue(mContext,"2")
            d.dismiss()
        }

        d.show()


        /*clear.setOnClickListener {

            image_view.setVisibility(GONE)
            sign_linear.setVisibility(VISIBLE)
            signatureView.setVisibility(VISIBLE)
            signatureView.clearCanvas()
        }
        save.setOnClickListener {
            image_view.visibility=View.VISIBLE
            signatureView.setVisibility(GONE)
            var bitmap:Bitmap=signatureView.signatureBitmap
            signatureView.signatureBitmap
            image_view.setImageBitmap(signatureView.signatureBitmap)
            //signatureView.clearCanvas()
var byte_array=bitmapToBytes(bitmap)
            var base64_string=bytesToBase64(byte_array)


            Base64Image.encode(bitmap) { base64 ->
                base64?.let {
                    // success
                }
            }
            var base64String=encodeImage(bitmap)

        }*/
    }
    fun bitmapToBytes(photo: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()

    }
    fun  saveReenrollApi(reEnrollsubmit:ArrayList<ReEnrollSubModel>,dlg:Dialog,d:Dialog){

        val token = sharedprefs.getaccesstoken(mContext)
        val save_reenroll: SaveReenrollmentApiModel? = SaveReenrollmentApiModel(reEnrollsubmit)

        val call: Call<SavereenrollmentModel> = ApiClient.getClient.savereenrollmentForm(save_reenroll!!,"Bearer "+token)
        call.enqueue(object : Callback<SavereenrollmentModel>{
            override fun onFailure(call: Call<SavereenrollmentModel>, t: Throwable) {
                //progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)

            }
            override fun onResponse(call: Call<SavereenrollmentModel>, response: Response<SavereenrollmentModel>) {
                //progressDialog.visibility = View.GONE

                if (response.body()!!.status.equals("100"))
                {
                    showSuccessReenrollAlert(mContext, "Successfully submitted the Re Enrollment", "Thankyou",dlg,d)

                }else if(response.body()!!.status.equals("116"))
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        saveReenrollApi(reEnrollsubmit,dlg,d)
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status.toInt(),mContext)
                }

            }

        })

    }
    private fun showerror(context: Context,message : String,msgHead : String,
                          reEnrollsubmit:ArrayList<ReEnrollSubModel>,d:Dialog)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
            saveReenrollApi(reEnrollsubmit,dialog,d)
            dialog.dismiss()

        }
        btn_Cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun showRenrollnoData(context: Context,message : String,msgHead : String,d:Dialog
                          )
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.exclamationicon)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
       // var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {

            //getReenrollForm()
            dialog.dismiss()
            //d.dismiss()

        }
       /* btn_Cancel.setOnClickListener {
            dialog.dismiss()
        }*/
        dialog.show()
    }
    fun emailvalidationcheck( title: String,
                              message: String,
                              dialog: Dialog) {
        val EMAIL_PATTERN: String =
            "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$"
        val pattern: String = "^([a-zA-Z ]*)$"
        if (title.toString().trim().matches(pattern.toRegex())) {
            if (title.toString().length >= 500) {
                Toast.makeText(mContext, "Subject is too long", Toast.LENGTH_SHORT).show()

            } else {
                if (message.toString().trim().matches(pattern.toRegex())) {
                    if (InternetCheckClass.isInternetAvailable(mContext)) {
                        if (message.length <= 500) {
                            reEnrollMailApi(
                                title,
                                message, dialog
                            )
                        } else {
                            Toast.makeText(mContext, "Message is too long", Toast.LENGTH_SHORT)
                                .show()

                        }
                    } else {
                        CommonFunctions.faliurepopup(mContext)
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                        mContext, mContext.getResources().getString(
                            R.string.enter_valid_contents
                        ), Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
            /*if (title.equals("")) {
            val toast: Toast = Toast.makeText(
                mContext, mContext.getResources().getString(
                    com.mobatia.bisad.R.string.enter_subjects
                ), Toast.LENGTH_SHORT
            )
            toast.show()
        } else {
            if (message.equals("")) {
                val toast: Toast = Toast.makeText(
                    mContext, mContext.getResources().getString(
                        R.string.enter_contents
                    ), Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                reEnrollMailApi(title,
                    message,dialog)
            }
        }*/
        }
    }
    private fun reEnrollMailApi(title: String,
                                  message: String,
                                  dialog: Dialog
    ){
        val token = sharedprefs.getaccesstoken(mContext)
        val sendMailBody = ReEnrollEmailApiModel( title, message)
        val call: Call<ReEnrollEmailModel> =
            ApiClient.getClient.re_enrollment_mailhelp(sendMailBody, "Bearer " + token)
        call.enqueue(object : Callback<ReEnrollEmailModel> {
            override fun onFailure(call: Call<ReEnrollEmailModel>, t: Throwable) {
                CommonFunctions.faliurepopup(mContext)

                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(call: Call<ReEnrollEmailModel>, response: Response<ReEnrollEmailModel>) {
                val responsedata = response.body()
                //progressDialog.visibility = View.GONE
                if (responsedata != null) {
                    try {


                        if (response.body()!!.status==100) {
                            dialog.dismiss()
                            showSuccessmailAlert(
                                mContext,
                                "Email sent Successfully ",
                                "Success",
                                dialog
                            )
                        }else {
                            if (response.body()!!.status==116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                reEnrollMailApi( title,
                                    message,dialog)
                            } else {
                                if (response.body()!!.status==103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
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
    fun showSuccessmailAlert(context: Context, message: String, msgHead: String, mdialog: Dialog) {
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
            mdialog.dismiss()
        }
        dialog.show()
    }
    fun bytesToBase64(bytes: ByteArray?): String? {
        return Base64.encodeToString(bytes, 0)
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}




