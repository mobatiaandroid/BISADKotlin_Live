package com.mobatia.bisad.activity.canteen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.mobatia.bisad.R

class ImagePagerDrawableAdapter( var homeBannerUrlImageArray: ArrayList<String>,var mContext:Context):
    PagerAdapter() {

    var mImagesArrayListBg: ArrayList<Int>? = null
    var mImagesArrayListUrlBg: ArrayList<String>? = null
    private var mInflaters: LayoutInflater? = null


    fun ImagePagerDrawableAdapter(context: Context?, mImagesArrayList: ArrayList<Int>?) {
        mImagesArrayListBg = ArrayList()

        mImagesArrayListBg = mImagesArrayList
    }

    fun ImagePagerDrawableAdapter(mImagesArrayListUrlBg: ArrayList<String>?, context: Context?) {
        this.mImagesArrayListUrlBg = ArrayList()

        this.mImagesArrayListUrlBg = mImagesArrayListUrlBg
    }

    override fun getCount(): Int {
        return mImagesArrayListUrlBg!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var pageview: View? = null
        mInflaters = LayoutInflater.from(mContext)
        pageview = mInflaters?.inflate(R.layout.layout_imagepager_adapter, null)
        val imageView = pageview!!.findViewById<View>(R.id.adImg) as ImageView

//        imageView.setBackgroundResource(mImagesArrayListBg.get(position));
        if (mImagesArrayListUrlBg!![position] != "") {
//            Picasso.with(mContext).load(AppUtils.replace(mImagesArrayListUrlBg.get(position))).fit()
//                    .into(imageView, new com.squareup.picasso.Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });
            //Glide.with(mContext).load(AppUtils.replace(mImagesArrayListUrlBg.get(position).toString())).placeholder(R.drawable.default_banner).centerCrop().into(imageView);
            var str=mImagesArrayListUrlBg!![position].replace(" ".toRegex(), "%20")
            Glide.with(mContext).load(str)
                .centerCrop().into(imageView)
        }
        (container as ViewPager).addView(pageview, 0)
        return pageview
    }

    /*fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) {
        (container as ViewPager).removeView(`object` as View?)
    }*/

}