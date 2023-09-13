package com.mobatia.bisad.activity.parent_meetings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.common.LoginActivity
import com.mobatia.bisad.activity.settings.tutorial.adapter.TutorialViewPagerAdapter
import com.mobatia.bisad.manager.IntentPassValueConstants
import com.mobatia.bisad.manager.IntentPassValueConstants.TYPE
import java.util.*

class ParentsEveninginfoActivity :AppCompatActivity(),IntentPassValueConstants {
    lateinit  var mImgCircle: Array<ImageView?>
    lateinit var mLinearLayout: LinearLayout
    var mTutorialViewPager: ViewPager? = null
    var mContext: Context? = null
    var mTutorialViewPagerAdapter: TutorialViewPagerAdapter? = null
    var mPhotoList = ArrayList(Arrays.asList<Int>(R.drawable.t_1, R.drawable.t_2, R.drawable.t_3,
        R.drawable.t_5, R.drawable.t_6, R.drawable.t_7, R.drawable.t_9))
    var dataType = 0
    lateinit var imageSkip: ImageView
    var mStaffid: String? = null
    var mStudentId: String? = null
    var mStudentName: String? = null
    var mStaffName: String? = null
    var mClass: String? = null
    var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        mContext=this
        val bundle = intent.extras
        if (bundle != null) {
            dataType = bundle.getInt(TYPE, 0)
//            mStaffid = bundle.getString(JTAG_STAFF_ID);
//            mStudentId = bundle.getString(JTAG_STUDENT_ID);
//            mStudentName = bundle.getString("studentName");
//            mStaffName = bundle.getString("staffName");
//            mClass = bundle.getString("studentClass");
//            selectedDate = bundle.getString("selectedDate");
        }
        initialiseViewPagerUI()
        /* back.setOnClickListener {
             finish()
         }*/
    }

    private fun initialiseViewPagerUI() {
        mTutorialViewPager = findViewById<View>(R.id.tutorialViewPager) as ViewPager
        mLinearLayout=findViewById(R.id.linear)
        imageSkip=findViewById(R.id.imageSkip)
        //        if (WissPreferenceManager.getUserName(mContext).equals("")) {
//            mPhotoList = new int[] { R.drawable.tut_1, R.drawable.tut_2,
//                    R.drawable.tut_3, R.drawable.tut_4, R.drawable.tut_5 };
//        } else {
//            mPhotoList = new int[] { R.drawable.tut_1, R.drawable.tut_2,
//                    R.drawable.tut_3, R.drawable.tut_4, R.drawable.tut_6 };
//        }
        mImgCircle = arrayOfNulls(mPhotoList.size)
        mTutorialViewPagerAdapter = TutorialViewPagerAdapter(mContext!!, mPhotoList)
        mTutorialViewPager!!.currentItem = 0
        mTutorialViewPager!!.adapter = mTutorialViewPagerAdapter
        addShowCountView(0)

        imageSkip.setOnClickListener(View.OnClickListener {
            finish()
           /* if (dataType == 0) {
                finish()
            } else {
                val loginIntent = Intent(mContext, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }*/
        })
        mTutorialViewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {

//                for (int i = 0; i < mPhotoList.size(); i++) {
//                    mImgCircle[i].setBackgroundDrawable(getResources()
//                            .getDrawable(R.drawable.blackround));
//                }
//                if (position < mPhotoList.size()) {
//                    mImgCircle[position].setBackgroundDrawable(getResources()
//                            .getDrawable(R.drawable.redround));
//                    mLinearLayout.removeAllViews();
//                    addShowCountView(position);
//                } else {
////                    mLinearLayout.removeAllViews();
////                    finish();
//                    mLinearLayout.removeAllViews();
//                    if (dataType==0)
//                    {
//                        finish();
//
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(mContext, ParentsAssociationListActivity.class);
//                        intent.putExtra("tab_type", "Parents Association");
//                        mContext.startActivity(intent);
//                        finish();
//                    }
//
//                }
                for (i in mPhotoList.indices) {
                    mImgCircle[i]!!.setBackgroundDrawable(
                        resources
                            .getDrawable(R.drawable.blackround)
                    )
                }
                if (position < mPhotoList.size) {
                    mImgCircle[position]!!.setBackgroundDrawable(
                        resources
                            .getDrawable(R.drawable.redround)
                    )
                    mLinearLayout.removeAllViews()
                    addShowCountView(position)
                } else {
                    mLinearLayout.removeAllViews()
                    if (dataType == 0) {
                        finish()
                    } else {

//                        Intent intent = new Intent(mContext, ParentsEveningTimeSlotActivity.class);
//                        intent.putExtra(JTAG_STAFF_ID,mStaffid);
//                        intent.putExtra(JTAG_STUDENT_ID,mStudentId);
//                        intent.putExtra("studentName",mStudentName);
//                        intent.putExtra("staffName",mStaffName);
//                        intent.putExtra("studentClass", mClass);
//                        intent.putExtra("selectedDate", selectedDate);
//                        mContext.startActivity(intent);
                        finish()
                    }
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
        mTutorialViewPager!!.adapter!!.notifyDataSetChanged()
    }
    private fun addShowCountView(count: Int) {
        for (i in mPhotoList.indices) {
            mImgCircle[i] = ImageView(this)
            val layoutParams = LinearLayout.LayoutParams(resources
                .getDimension(R.dimen.home_circle_width).toInt(), resources.getDimension(
                R.dimen.home_circle_height).toInt())
            mImgCircle[i]!!.layoutParams = layoutParams
            if (i == count) {
                mImgCircle[i]!!.setBackgroundResource(R.drawable.redround)
            } else {
                mImgCircle[i]!!.setBackgroundResource(R.drawable.blackround)
            }
            mLinearLayout.addView(mImgCircle[i])
        }
    }

}