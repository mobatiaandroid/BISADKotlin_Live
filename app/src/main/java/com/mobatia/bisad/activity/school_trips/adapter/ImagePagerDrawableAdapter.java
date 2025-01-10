package com.mobatia.bisad.activity.school_trips.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.mobatia.bisad.R;
import com.mobatia.bisad.constants.CommonFunctions;


import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 29/3/16.
 */
public class ImagePagerDrawableAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<Integer> mImagesArrayListBg;
    ArrayList<String> mImagesArrayListUrlBg;
    private LayoutInflater mInflaters;


    public ImagePagerDrawableAdapter(Context context, ArrayList<Integer> mImagesArrayList) {
        this.mImagesArrayListBg = new ArrayList<Integer>();
        this.mContext = context;
        this.mImagesArrayListBg = mImagesArrayList;
    }

    public ImagePagerDrawableAdapter(ArrayList<String> mImagesArrayListUrlBg, Context context) {
        this.mImagesArrayListUrlBg = new ArrayList<String>();
        this.mContext = context;
        this.mImagesArrayListUrlBg = mImagesArrayListUrlBg;
    }

    @Override
    public int getCount() {
        return mImagesArrayListUrlBg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View pageview = null;
        mInflaters = LayoutInflater.from(mContext);
        pageview = mInflaters.inflate(R.layout.layout_imagepager_adapter, null);
        ImageView imageView = pageview.findViewById(R.id.adImg);

//        imageView.setBackgroundResource(mImagesArrayListBg.get(position));
        if (!mImagesArrayListUrlBg.get(position).equals("")) {
//            Picasso.with(mContext).load(AppUtils.replace(mImagesArrayListUrlBg.get(position))).fit()
//                    .into(imageView, new com.squareup.picasso.Callback() {
//                        @Override
//                        public void onSuccess() {
//                            System.out.println("Image Succes:"+AppUtils.replace(mImagesArrayListUrlBg.get(position)));
//
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });
            //Glide.with(mContext).load(AppUtils.replace(mImagesArrayListUrlBg.get(position).toString())).placeholder(R.drawable.default_banner).centerCrop().into(imageView);

            Glide.with(mContext).load(CommonFunctions.Companion.replace(mImagesArrayListUrlBg.get(position))).placeholder(R.drawable.default_banner).centerCrop().into(imageView);
        }


        container.addView(pageview, 0);

        return pageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
