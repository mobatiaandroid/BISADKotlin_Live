package com.mobatia.bisad.activity.school_trips.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.volleywrappermanager.CustomProgressBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 29/3/16.
 */
public class TripsImagePagerAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<String> mImagesArrayListBg;
    String orientation;
    RelativeLayout relativeLayout;
    int pos;
    ImageView imageView;
    ImageView downloadimageView;
    ImageView share;
    CustomProgressBar pDialog;
    private LayoutInflater mInflaters;
    private Target mTarget;

    public TripsImagePagerAdapter(Context context, ArrayList<String> mImagesArrayList, String orientation) {
        this.mContext = context;
        this.mImagesArrayListBg = mImagesArrayList;
        this.orientation = orientation;
    }

    @Override
    public int getCount() {
        return mImagesArrayListBg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        pDialog = new CustomProgressBar(mContext, R.drawable.spinner);
        pos = position;
        View pageview = null;
        mInflaters = LayoutInflater.from(mContext);
        pageview = mInflaters.inflate(R.layout.layout_photo_imagepager_adapter, null);
        imageView = (ImageView) pageview.findViewById(R.id.adImg);
        downloadimageView = (ImageView) pageview.findViewById(R.id.download);
        share = (ImageView) pageview.findViewById(R.id.share);
        ImageView back = (ImageView) pageview.findViewById(R.id.back);
        relativeLayout = (RelativeLayout) pageview.findViewById(R.id.relativeLayout);
        if (!mImagesArrayListBg.get(position).equals("")) {
            if (orientation.equals("portrait")) {
                imageView.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                loadImage(mContext, AppUtils.replace(mImagesArrayListBg.get(position)), imageView);
            } else {
                imageView.setVisibility(View.VISIBLE);

                back.setVisibility(View.GONE);
                loadImage(mContext, AppUtils.replace(mImagesArrayListBg.get(position)), imageView);

            }
        }


        back.setOnClickListener(v -> ((Activity) mContext).finish());
        downloadimageView.setVisibility(View.GONE);
        share.setVisibility(View.GONE);


        ((ViewPager) container).addView(pageview, 0);

        return pageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    void loadImage(Context context, String url, final ImageView img) {

        pDialog.show();

        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                //Do something

                img.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context).load(url).into(mTarget);
        pDialog.dismiss();
    }
}
