package com.mobatia.bisad.activity.school_trips

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class TripImagesViewActivity : AppCompatActivity() {
    var mContext: Context? = null
    var back: ImageView? = null
    var intent: Intent? = null
    var mPhotosModelArrayList: ArrayList<String>? = null
    var extras: Bundle? = null
    var bannerImageViewPager: ViewPager? = null
    var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_images_view)
        mContext = this
        initUI()
    }
    private fun initUI() {
        back = findViewById<View>(R.id.back) as ImageView
        bannerImageViewPager = findViewById<View>(R.id.bannerImageViewPager) as ViewPager
        extras = getIntent().extras
        if (extras != null) {
            mPhotosModelArrayList =
                extras!!.getSerializable("photo_array") as java.util.ArrayList<String?>?
            pos = extras!!.getInt("pos")
        }
        back!!.setOnClickListener { v: View? -> finish() }
        bannerImageViewPager!!.adapter =
            TripsImagePagerAdapter(mContext, mPhotosModelArrayList, "portrait")
        bannerImageViewPager!!.currentItem = pos
        bannerImageViewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
//                headermanager.setTitle((position + 1) + " Of " + mPhotosModelArrayList.size());
                pos = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}