package com.hijewel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.demono.adapter.InfinitePagerAdapter;
import com.hijewel.R;
import com.hijewel.models.BannerModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.hijewel.utils.Constatnts.URL;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class BannerAdapter extends InfinitePagerAdapter {

    private Context context;
    private ArrayList<BannerModel> data;

    public BannerAdapter(Context context, ArrayList<BannerModel> data) {
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
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public View getItemView(int position, View view, ViewGroup viewGroup) {
        @SuppressLint("InflateParams") View root = LayoutInflater.from(context).inflate(R.layout.pager_images, null);
        ImageView cover = root.findViewById(R.id.cover);
        Picasso.with(context).load(URL + getItem(position).getBammer_image()).into(cover);
        Log.e("kekekek", position + "");
        return root;
    }

}
