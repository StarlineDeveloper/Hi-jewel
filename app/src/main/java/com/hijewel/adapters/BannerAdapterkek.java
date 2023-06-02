package com.hijewel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
//import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.demono.adapter.InfinitePagerAdapter;
import com.hijewel.R;
import com.hijewel.models.BannerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.hijewel.utils.Constatnts.URL;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class BannerAdapterkek extends PagerAdapter {

    private Context context;
    private ArrayList<BannerModel> data;

    public BannerAdapterkek(Context context, ArrayList<BannerModel> data) {
        this.context = context;
        this.data = data;
    }

    private BannerModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        @SuppressLint("InflateParams")
        View root = LayoutInflater.from(context).inflate(R.layout.pager_images, null);
        ImageView cover = root.findViewById(R.id.cover);
        Picasso.with(context).load(URL + getItem(position).getBammer_image()).into(cover);
        container.addView(root);
        return root;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}
