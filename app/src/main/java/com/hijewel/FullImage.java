package com.hijewel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hijewel.ui.TouchImageView;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.hijewel.utils.Constatnts.PRODUCT_IMAGE;
import static com.hijewel.utils.Constatnts.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class FullImage extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    RelativeLayout details_below;
    RecyclerView bottom_images;

    Intent intent;
    Context context;

    BottomImages adp;
    MyPagerAdapter pageAdap;
    String[] images;
    int pos;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        context = FullImage.this;
        intent = getIntent();
        images = intent.getStringArrayExtra("data");
        pos = intent.getIntExtra("pos", 0);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear);

        pager = findViewById(R.id.pager);
        details_below = findViewById(R.id.details_below);
        pageAdap = new MyPagerAdapter(images);
        pager.setAdapter(pageAdap);
        pager.setCurrentItem(pos);

        pager.setPageMargin(10);
        pager.setOffscreenPageLimit(images.length);

        bottom_images = findViewById(R.id.bottom_images);
        LinearLayoutManager lm = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        bottom_images.setLayoutManager(lm);
        adp = new BottomImages(images);
        bottom_images.setAdapter(adp);
        adp.selectImage(pos);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adp.selectImage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (flag) {
                    toolbar.animate().translationY(0);
                    details_below.animate().translationY(0);
                    flag = false;
                }
            }
        });

        /*PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setAnimationType(AnimationType.FILL);
        pageIndicatorView.setRadius(8);
        pageIndicatorView.setPadding(8);
        pageIndicatorView.setSelectedColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        pageIndicatorView.setUnselectedColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        pageIndicatorView.setViewPager(pager);
        pageIndicatorView.setSelection(pos);*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyPagerAdapter extends PagerAdapter {

        SparseArray<View> views = new SparseArray<>();
        private String[] IMAGES;

        private MyPagerAdapter(String[] IMAGES) {
            this.IMAGES = IMAGES;
        }

        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View layout = LayoutInflater.from(context)
                    .inflate(R.layout.pager_images, null);

            final ImageView imageView = layout.findViewById(R.id.cover);
            imageView.setVisibility(View.GONE);
            final TouchImageView zoom_cover = layout.findViewById(R.id.zoom_cover);
            zoom_cover.setVisibility(View.VISIBLE);
            zoom_cover.resetZoom();
            final String imagePath = PRODUCT_IMAGE + getIntent().getStringExtra("id") + "/thumb/" + IMAGES[position];
            Log.e("sfsfgsfsf", imagePath);
            Picasso.with(context)
                    .load(imagePath)
//                    .resize(280, 280)
                    .into(zoom_cover, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(context).load(imagePath.replaceAll("/thumb", "/large"))
                                    .placeholder(imageView.getDrawable()).into(zoom_cover);
                        }

                        @Override
                        public void onError() {
                            Log.e("Error", PRODUCT_IMAGE + IMAGES[position]);
                        }
                    });
            zoom_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if  (!flag) {
                        toolbar.animate().translationY(-toolbar.getHeight());
                        details_below.animate().translationY(details_below.getHeight());
                        flag = true;
                    } else {
                        toolbar.animate().translationY(0);
                        details_below.animate().translationY(0);
                        flag = false;
                    }
                }
            });

            container.addView(layout);
            views.put(position, layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
            views.remove(position);
            container = null;
        }

        @Override
        public void notifyDataSetChanged() {
            int key = 0;
            for (int i = 0; i < views.size(); i++) {
                key = views.keyAt(i);
                View view = views.get(key);
                final TouchImageView zoom_cover = view.findViewById(R.id.zoom_cover);
                zoom_cover.resetZoom();
            }
            super.notifyDataSetChanged();
        }
    }

    private class BottomImages extends RecyclerView.Adapter<BottomImages.Holder> {

        private String[] IMAGES;
        private SparseBooleanArray mArray;

        private BottomImages(String[] IMAGES) {
            this.IMAGES = IMAGES;
            mArray = new SparseBooleanArray(IMAGES.length);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(context).inflate(R.layout.fullimage_bottom_item, null);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final String imagePath = PRODUCT_IMAGE + getIntent().getStringExtra("id") + "/thumb/" + IMAGES[position];
            Picasso.with(context)
                    .load(imagePath)
//                    .resize(250, 250)
                    .into(holder.cover);
            holder.cover.setSelected(mArray.get(position));
        }

        @Override
        public int getItemCount() {
            return IMAGES.length;
        }

        void selectImage(int possss) {
            if (!mArray.get(possss)) {
                Log.e("fhgfdh", possss + "");
                mArray.clear();
                mArray.put(possss, true);
                notifyDataSetChanged();
                pager.setCurrentItem(possss);
                pageAdap.notifyDataSetChanged();
            }
        }

        class Holder extends RecyclerView.ViewHolder {

            ImageView cover;

            Holder(View itemView) {
                super(itemView);
                cover = itemView.findViewById(R.id.cover);

                cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("fneskj", getAdapterPosition() + "");
                        selectImage(getAdapterPosition());
                    }
                });
            }
        }
    }
}
