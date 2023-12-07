
package com.mobatia.bisad.fragment.calendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.calendar_new.calmodel.CalDateAdapter
import com.mobatia.bisad.fragment.calendar_new.model.*
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment: Fragment() {
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var calendarRecycler: RecyclerView
   // lateinit var progressDialog: RelativeLayout
    lateinit var titleTextView: TextView
    lateinit var noEventTxt: TextView
    lateinit var noEventImage: ImageView
    lateinit var mContext: Context
    lateinit var monthLinear: LinearLayout
    lateinit var calendarArrayList: ArrayList<CalendarResponseArray>
    lateinit var primaryArrayList: ArrayList<VEVENT>
    lateinit var secondaryArrayList: ArrayList<VEVENT>
    lateinit var wholeSchoolArrayList: ArrayList<VEVENT>
    lateinit var primaryShowArrayList: ArrayList<PrimaryModel>
    lateinit var secondaryShowArrayList: ArrayList<PrimaryModel>
    lateinit var wholeSchoolShowArrayList: ArrayList<PrimaryModel>
    lateinit var calendarShowArrayList: ArrayList<PrimaryModel>
    lateinit var calendarShowFilteredArrayList: ArrayList<PrimaryModel>
    lateinit var mCalendarFinalArrayList: ArrayList<CalendarDateModel>
    var primaryColor: String = ""
    var secondaryColor: String = ""
    var wholeSchoole: String = ""
    var isPrimarySelected: Boolean = true
    var isSecondarySeleted: Boolean = true
    var isWholeSchoolSelected: Boolean = true
    var isAllSelected: Boolean = true
    var year: Int = 0
    var currentMonth: Int = 0
    lateinit var monthTxt: String
    lateinit var monthYearTxt: TextView
    lateinit var prevBtn:ImageView
    lateinit var nxtBtn:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        callCalendarApi()

    }
    private fun initializeUI(){
        monthLinear = requireView().findViewById(R.id.monthLinear)
        titleTextView = requireView().findViewById(R.id.titleTextView)
        monthYearTxt = requireView().findViewById(R.id.monthYearTxt)
        nxtBtn=requireView().findViewById(R.id.nextBtn)
        prevBtn=requireView().findViewById(R.id.previousBtn)
        calendarRecycler = requireView().findViewById(R.id.calendarRecycler) as RecyclerView

        mCalendarFinalArrayList=ArrayList()
        titleTextView.text="Calendar"
        year = Calendar.getInstance().get(Calendar.YEAR)
        currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        month(currentMonth, year)

   nxtBtn.setOnClickListener(View.OnClickListener {

       currentMonth = currentMonth + 1
       if (currentMonth > 11) {
           currentMonth = currentMonth - 12
           year = year + 1


       }
       month(currentMonth, year)
       primaryShowCalendar(  isAllSelected,
           isPrimarySelected,
           isSecondarySeleted,
           isWholeSchoolSelected)
     /*  showCalendarEvent(
           isAllSelected,
           isPrimarySelected,
           isSecondarySeleted,
           isWholeSchoolSelected
       )*/
   })
        prevBtn.setOnClickListener {
            if (currentMonth == 0) {
                currentMonth = 11 - currentMonth
                year = year - 1
            } else {
                currentMonth = currentMonth - 1
            }
            month(currentMonth, year)
            primaryShowCalendar( isAllSelected,
                isPrimarySelected,
                isSecondarySeleted,
                isWholeSchoolSelected)
           /* showCalendarEvent(
                isAllSelected,
                isPrimarySelected,
                isSecondarySeleted,
                isWholeSchoolSelected
            )*/
        }

    }
  fun callCalendarApi() {
        calendarArrayList = ArrayList()
        primaryArrayList = ArrayList()
        secondaryArrayList = ArrayList()
        wholeSchoolArrayList = ArrayList()
        //progressDialog.visibility = View.VISIBLE
        val call: Call<CalendarModel> =
            ApiClient.getClient.calendarList()
        call.enqueue(object : Callback<CalendarModel> {
            override fun onFailure(call: Call<CalendarModel>, t: Throwable) {
                //progressDialog.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<CalendarModel>,
                response: Response<CalendarModel>
            ) {
               // progressDialog.visibility = View.GONE
                if (response.body()!!.status == 100) {
                    calendarArrayList.addAll(response.body()!!.calendarList)
                    if (calendarArrayList.size > 0) {
                        for (i in 0..calendarArrayList.size - 1) {
                            if (calendarArrayList.get(i).title.equals("Primary Event")) {
                                if (calendarArrayList.get(i).calendarDetail.cal.VEVENT.size > 0) {
                                    primaryArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    primaryColor = calendarArrayList.get(i).color
                                }

                            } else if (calendarArrayList.get(i).title.equals("Secondary Event")) {
                                if (calendarArrayList.get(i).calendarDetail.cal.VEVENT.size > 0) {

                                    secondaryArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    secondaryColor = calendarArrayList.get(i).color
                                }

                            } else if (calendarArrayList.get(i).title.equals("Whole School Event")) {
                                if (calendarArrayList.get(i).calendarDetail.cal.VEVENT.size > 0) {
                                    wholeSchoolArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    wholeSchoole = calendarArrayList.get(i).color
                                }

                            }

                            isAllSelected = true
                            isPrimarySelected = true
                            isSecondarySeleted = true
                            isWholeSchoolSelected = true

                           /*showCalendarEvent(
                                isAllSelected,
                                isPrimarySelected,
                                isSecondarySeleted,
                                isWholeSchoolSelected
                            )*/
                        }

                        primaryShowCalendar(isAllSelected,isPrimarySelected,isSecondarySeleted,
                            isWholeSchoolSelected)
                        var categoryList = ArrayList<String>()
                        categoryList.add("Select all/none")
                        categoryList.add("Primary Event")
                        categoryList.add("Secondary Event")
                        categoryList.add("Whole School Event")

                        //mTriggerModelArrayList = ArrayList()
                        for (i in 0..categoryList.size - 1) {
                            var model = CategoryModel()
                            model.categoryName = categoryList.get(i)
                            model.checkedCategory = true
                            if (i == 0) {
                                var whiteColor = "#000000"
                                model.color = whiteColor
                            } else {

                                if (i == 1) {
                                    model.color = primaryColor
                                }
                                if (i == 2) {
                                    model.color = secondaryColor
                                }
                                if (i == 3) {
                                    model.color = wholeSchoole

                                }
                            }

                            //mTriggerModelArrayList.add(model)

                        }
                    } else {


                    }

                } else {

                }
            }

        })
    }
   private fun month(month: Int, year: Int) {
        when (month) {
            0 -> {
                monthTxt = "January"
                monthYearTxt.text = monthTxt +" "+ year.toString()

            }

            1 -> {
                monthTxt = "February"
                monthYearTxt.text = monthTxt +" "+ year.toString()
            }

            2 -> {
                monthTxt = "March"
                monthYearTxt.text = monthTxt +" "+ year.toString()
            }

            3 -> {
                monthTxt = "April"
                monthYearTxt.text = monthTxt +" "+ year.toString()
            }

            4 -> {
                monthTxt = "May"
                monthYearTxt.text = monthTxt +" "+ year.toString()
            }

            5 -> {
                monthTxt = "June"
                monthYearTxt.text = monthTxt +" "+ year.toString()
            }

            6 -> {
                monthTxt = "July"
                monthYearTxt.text = monthTxt +" "+ year.toString()
            }

            7 -> {
                monthTxt = "August"
                monthYearTxt.text = monthTxt +" "+ year.toString()
            }

            8 -> {
                monthTxt = "September"
                monthYearTxt.text = monthTxt  +" "+ year.toString()
            }

            9 -> {
                monthTxt = "October"
                monthYearTxt.text = monthTxt  +" "+ year.toString()
            }

            10 -> {
                monthTxt = "November"
                monthYearTxt.text = monthTxt  +" "+ year.toString()
            }

            11 -> {
                monthTxt = "December"
                monthYearTxt.text = monthTxt  +" "+ year.toString()
            }

        }
    }
    private fun primaryShowCalendar( allSeleted: Boolean,
                                     primarySelected: Boolean,
                                     secondarySelected: Boolean,
                                     wholeSchoolSelected: Boolean){
        primaryShowArrayList= ArrayList()
        secondaryShowArrayList = ArrayList()
        wholeSchoolShowArrayList = ArrayList()
        mCalendarFinalArrayList= ArrayList()
        if (primaryArrayList.size > 0) {
            for (i in 0 until primaryArrayList.size) {
                var pModel = PrimaryModel()
                if (primaryArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTSTART)
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    val enddate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var outputDateStrend: String = outputFormat.format(enddate)
                    pModel.DTSTART = result

                } else if (primaryArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTSTART = outputDateStrstart

                }
                if (primaryArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTEND = result

                } else if (primaryArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTEND = outputDateStrstart

                }

                pModel.SUMMARY = primaryArrayList.get(i).SUMMARY
                pModel.DESCRIPTION = primaryArrayList.get(i).DESCRIPTION
                pModel.LOCATION = primaryArrayList.get(i).LOCATION
                pModel.color = primaryColor
                pModel.type = 1
                primaryShowArrayList.add(pModel)
            }
        }
        if (secondaryArrayList.size > 0) {
            for (i in 0..secondaryArrayList.size - 1) {
                var sModel = PrimaryModel()
                if (secondaryArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTSTART)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTSTART = result

                } else if (secondaryArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTSTART = outputDateStrstart

                }
                if (secondaryArrayList.get(i).DTEND.equals("null")) {
                    sModel.DTEND = ""
                } else if (secondaryArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTEND = result

                } else if (secondaryArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTEND = outputDateStrstart

                }
                sModel.SUMMARY = secondaryArrayList.get(i).SUMMARY
                sModel.DESCRIPTION = secondaryArrayList.get(i).DESCRIPTION
                sModel.LOCATION = secondaryArrayList.get(i).LOCATION
                sModel.color = secondaryColor
                sModel.type = 2
                secondaryShowArrayList.add(sModel)
            }
        }
        if (wholeSchoolArrayList.size > 0) {
            for (i in 0..wholeSchoolArrayList.size - 1) {
                var wModel = PrimaryModel()
                if (wholeSchoolArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTSTART)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTSTART = result

                } else if (wholeSchoolArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTSTART = outputDateStrstart

                }
                if (wholeSchoolArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTEND = result

                } else if (wholeSchoolArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTEND = outputDateStrstart

                }

                wModel.SUMMARY = wholeSchoolArrayList.get(i).SUMMARY
                wModel.DESCRIPTION = wholeSchoolArrayList.get(i).DESCRIPTION
                wModel.LOCATION = wholeSchoolArrayList.get(i).LOCATION
                wModel.color = wholeSchoole
                wModel.type = 3
                wholeSchoolShowArrayList.add(wModel)
            }
        }

        if (allSeleted) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && !primarySelected && !secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            var dummy = ArrayList<PrimaryModel>()
            calendarShowArrayList = dummy
        } else if (!allSeleted && !primarySelected && !secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && !primarySelected && secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }

        } else if (!allSeleted && !primarySelected && secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && primarySelected && !secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }

        } else if (!allSeleted && primarySelected && !secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && primarySelected && secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }

        }


        if (primaryShowArrayList.size>0){

            for (j in primaryShowArrayList.indices){
                var month_check:String=""
                var fModel = PrimaryModel()

                if (primaryShowArrayList[j].DTSTART.toString().length == 20) {
                    val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val outputFormat: DateFormat = SimpleDateFormat("MMMM yyyy")
                    val startdate: Date = inputFormat.parse(primaryShowArrayList[j].DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    month_check = outputDateStrstart
                }else if (primaryShowArrayList[j].DTSTART.toString().length == 11) {
                    val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("MMMM yyyy")
                    val startdate: Date = inputFormat.parse(primaryShowArrayList[j].DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    month_check = outputDateStrstart
                }


                if (month_check.equals(monthYearTxt.text)) {

                    var cModel = CalendarDateModel()
                    cModel.startDate = primaryShowArrayList[j].DTSTART
                    cModel.endDate = primaryShowArrayList[j].DTEND
                    var calendarDetaiArray = ArrayList<CalendarDetailModel>()
                    var dModel = CalendarDetailModel()
                    dModel.DTSTART = primaryShowArrayList[j].DTSTART
                    dModel.DTEND = primaryShowArrayList[j].DTEND
                    dModel.SUMMARY = primaryShowArrayList[j].SUMMARY
                    dModel.DESCRIPTION = primaryShowArrayList[j].DESCRIPTION
                    dModel.LOCATION = primaryShowArrayList[j].LOCATION
                    dModel.color = primaryShowArrayList[j].color
                    dModel.type = primaryShowArrayList[j].type
                    calendarDetaiArray.add(dModel)
                    cModel.detailList = calendarDetaiArray
                    mCalendarFinalArrayList.add(cModel)

                    calendarRecycler.layoutManager=LinearLayoutManager(mContext)
                    var cale_adapter = CalDateAdapter(mContext, mCalendarFinalArrayList)
                    calendarRecycler.adapter = cale_adapter
                }else{
                    calendarRecycler.layoutManager=LinearLayoutManager(mContext)
                    var cale_adapter = CalDateAdapter(mContext, mCalendarFinalArrayList)
                    calendarRecycler.adapter = cale_adapter
                }

            }

        }


    }



    private fun showCalendarEvent(
        allSeleted: Boolean,
        primarySelected: Boolean,
        secondarySelected: Boolean,
        wholeSchoolSelected: Boolean
    ) {
        primaryShowArrayList = ArrayList()
        secondaryShowArrayList = ArrayList()
        wholeSchoolShowArrayList = ArrayList()
        //calendarFilterArrayList = ArrayList()
        if (primaryArrayList.size > 0) {
            for (i in 0..primaryArrayList.size - 1) {
                var pModel = PrimaryModel()
                if (primaryArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTSTART)
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    val enddate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var outputDateStrend: String = outputFormat.format(enddate)
                    pModel.DTSTART = result

                } else if (primaryArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTSTART = outputDateStrstart

                }
                if (primaryArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTEND = result

                } else if (primaryArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTEND = outputDateStrstart

                }

                pModel.SUMMARY = primaryArrayList.get(i).SUMMARY
                pModel.DESCRIPTION = primaryArrayList.get(i).DESCRIPTION
                pModel.LOCATION = primaryArrayList.get(i).LOCATION
                pModel.color = primaryColor
                pModel.type = 1
                primaryShowArrayList.add(pModel)
            }
        }
        if (secondaryArrayList.size > 0) {
            for (i in 0..secondaryArrayList.size - 1) {
                var sModel = PrimaryModel()
                if (secondaryArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTSTART)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTSTART = result

                } else if (secondaryArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTSTART = outputDateStrstart

                }
                if (secondaryArrayList.get(i).DTEND.equals("null")) {
                    sModel.DTEND = ""
                } else if (secondaryArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTEND = result

                } else if (secondaryArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTEND = outputDateStrstart

                }
                sModel.SUMMARY = secondaryArrayList.get(i).SUMMARY
                sModel.DESCRIPTION = secondaryArrayList.get(i).DESCRIPTION
                sModel.LOCATION = secondaryArrayList.get(i).LOCATION
                sModel.color = secondaryColor
                sModel.type = 2
                secondaryShowArrayList.add(sModel)
            }
        }
        if (wholeSchoolArrayList.size > 0) {
            for (i in 0..wholeSchoolArrayList.size - 1) {
                var wModel = PrimaryModel()
                if (wholeSchoolArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTSTART)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTSTART = result

                } else if (wholeSchoolArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTSTART = outputDateStrstart

                }
                if (wholeSchoolArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTEND = result

                } else if (wholeSchoolArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTEND = outputDateStrstart

                }

                wModel.SUMMARY = wholeSchoolArrayList.get(i).SUMMARY
                wModel.DESCRIPTION = wholeSchoolArrayList.get(i).DESCRIPTION
                wModel.LOCATION = wholeSchoolArrayList.get(i).LOCATION
                wModel.color = wholeSchoole
                wModel.type = 3
                wholeSchoolShowArrayList.add(wModel)
            }
        }

        if (allSeleted) {
            calendarShowArrayList = ArrayList()
            calendarShowFilteredArrayList= ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

            for (i in calendarShowArrayList.indices){
                var month_check:String=""
                var fModel = PrimaryModel()

                if (calendarShowArrayList[i].DTSTART.toString().length == 20) {
                    val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val outputFormat: DateFormat = SimpleDateFormat("MMMM yyyy")
                    val startdate: Date = inputFormat.parse(calendarShowArrayList[i].DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    month_check = outputDateStrstart
                }else if (calendarShowArrayList[i].DTSTART.toString().length == 11) {
                    val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("MMMM yyyy")
                    val startdate: Date = inputFormat.parse(calendarShowArrayList[i].DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    month_check = outputDateStrstart
                }

                if (month_check.equals(monthYearTxt.text)){


                    for (j in 0..mCalendarFinalArrayList.size-1){
                        if (!calendarShowArrayList.get(i).DTSTART.equals(mCalendarFinalArrayList[j].startDate)){

                            var cModel = CalendarDateModel()
                            cModel.startDate = calendarShowArrayList.get(i).DTSTART
                            cModel.endDate = calendarShowArrayList.get(i).DTEND
                            var calendarDetaiArray = ArrayList<CalendarDetailModel>()
                            var dModel = CalendarDetailModel()
                            dModel.DTSTART = calendarShowArrayList.get(i).DTSTART
                            dModel.DTEND = calendarShowArrayList.get(i).DTEND
                            dModel.SUMMARY = calendarShowArrayList.get(i).SUMMARY
                            dModel.DESCRIPTION = calendarShowArrayList.get(i).DESCRIPTION
                            dModel.LOCATION = calendarShowArrayList.get(i).LOCATION
                            dModel.color = calendarShowArrayList.get(i).color
                            dModel.type = calendarShowArrayList.get(i).type
                            calendarDetaiArray.add(dModel)
                            cModel.detailList = calendarDetaiArray
                            mCalendarFinalArrayList.add(cModel)

                           /* calendarRecycler.layoutManager=LinearLayoutManager(mContext)
                            var cale_adapter = CalDateAdapter(mContext, mCalendarFinalArrayList)
                            calendarRecycler.adapter = cale_adapter*/
                           /* cModel.detailList = calendarDetaiArray
                            mCalendarFinalArrayList.add(cModel)*/
                        }else{
                            var cModel = CalendarDateModel()
                            var calendarDetaiArray = ArrayList<CalendarDetailModel>()
                            var dModel = CalendarDetailModel()
                            dModel.DTSTART = calendarShowArrayList.get(i).DTSTART
                            dModel.DTEND = calendarShowArrayList.get(i).DTEND
                            dModel.SUMMARY = calendarShowArrayList.get(i).SUMMARY
                            dModel.DESCRIPTION = calendarShowArrayList.get(i).DESCRIPTION
                            dModel.LOCATION = calendarShowArrayList.get(i).LOCATION
                            dModel.color = calendarShowArrayList.get(i).color
                            dModel.type = calendarShowArrayList.get(i).type
                            calendarDetaiArray.add(dModel)
                            cModel.detailList = calendarDetaiArray
                            mCalendarFinalArrayList.add(cModel)


                        }
                        calendarRecycler.layoutManager=LinearLayoutManager(mContext)
                        var cale_adapter = CalDateAdapter(mContext, mCalendarFinalArrayList)
                        calendarRecycler.adapter = cale_adapter
                    }




                        /*else{
                            for (j in 0..mCalendarFinalArrayList.size ){
                                if (calendarShowArrayList.get(n).DTSTART==mCalendarFinalArrayList[j].startDate){
                                    var cModel = CalendarDateModel()
                                    cModel.startDate = calendarShowArrayList.get(n).DTSTART
                                    cModel.endDate = calendarShowArrayList.get(n).DTEND
                                    var calendarDetaiArray = ArrayList<CalendarDetailModel>()
                                    var dModel = CalendarDetailModel()
                                    dModel.DTSTART = calendarShowArrayList.get(n).DTSTART
                                    dModel.DTEND = calendarShowArrayList.get(n).DTEND
                                    dModel.SUMMARY = calendarShowArrayList.get(n).SUMMARY
                                    dModel.DESCRIPTION = calendarShowArrayList.get(n).DESCRIPTION
                                    dModel.LOCATION = calendarShowArrayList.get(n).LOCATION
                                    dModel.color = calendarShowArrayList.get(n).color
                                    dModel.type = calendarShowArrayList.get(n).type
                                    calendarDetaiArray.add(dModel)
                                    mCalendarFinalArrayList[j].detailList.add(dModel)
                                    cModel.detailList = calendarDetaiArray
                                    mCalendarFinalArrayList.add(cModel)
                                }else{
                                    var cModel = CalendarDateModel()
                                    cModel.startDate = calendarShowArrayList.get(n).DTSTART
                                    cModel.endDate = calendarShowArrayList.get(n).DTEND
                                    var calendarDetaiArray = ArrayList<CalendarDetailModel>()
                                    var dModel = CalendarDetailModel()
                                    dModel.DTSTART = calendarShowArrayList.get(n).DTSTART
                                    dModel.DTEND = calendarShowArrayList.get(n).DTEND
                                    dModel.SUMMARY = calendarShowArrayList.get(n).SUMMARY
                                    dModel.DESCRIPTION = calendarShowArrayList.get(n).DESCRIPTION
                                    dModel.LOCATION = calendarShowArrayList.get(n).LOCATION
                                    dModel.color = calendarShowArrayList.get(n).color
                                    dModel.type = calendarShowArrayList.get(n).type
                                    calendarDetaiArray.add(dModel)
                                    cModel.detailList = calendarDetaiArray
                                    mCalendarFinalArrayList.add(cModel)

                                }
                            }
                        }*/


                    calendarRecycler.layoutManager=LinearLayoutManager(mContext)
                    var cal_adapter = CalDateAdapter(mContext, mCalendarFinalArrayList)
                    calendarRecycler.adapter = cal_adapter
                }

            }

        }else{



        }
    }
}
/*else if (!allSeleted && !primarySelected && !secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            var dummy = ArrayList<PrimaryModel>()
            calendarShowArrayList = dummy
        } else if (!allSeleted && !primarySelected && !secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && !primarySelected && secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }

        } else if (!allSeleted && !primarySelected && secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && primarySelected && !secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }

        } else if (!allSeleted && primarySelected && !secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && primarySelected && secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }

        }*/

