package com.hijewel;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hijewel.models.CartModel;
import com.hijewel.models.ProductModel;
import com.hijewel.models.SymbolModel;
import com.hijewel.ui.MyNestedScrollView;
import com.hijewel.utils.GetData;
import com.hijewel.utils.UserUtils;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.hijewel.database.MasterDatabase.checkInCart;
import static com.hijewel.database.MasterDatabase.insertCartData;
import static com.hijewel.utils.Constatnts.KEY_STATUS_DISPLAY;
import static com.hijewel.utils.Constatnts.LIVE_RATES;
import static com.hijewel.utils.Constatnts.PRODUCT_IMAGE;
import static com.hijewel.utils.Functions.changeColor;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class ProductDetails extends MainActivity {

    Context context;
    GetData gd;
    UserUtils userUtils;
    ViewPager images;

    MyNestedScrollView scroll;
    TextView title, size_main, price, sku, desc;
    RadioGroup carats;

    ProductModel pm;
    String CARAT = "";

    private BroadcastReceiver rates = new BroadcastReceiver() {
        public void onReceive(final Context context, final Intent intent) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    loadData(intent.getStringExtra("data"));
                }
            });
        }
    };

    private void loadData(String data) {
        SymbolModel sm = gd.getLiveRates(utils, data);
        if (utils.getPrefrence(KEY_STATUS_DISPLAY, "").equals("true")) {
            if (sm != null) {
                rate_layout.setVisibility(View.VISIBLE);
                product.setText(sm.getSymbol());
                sell.setText(sm.getAsk());
                changeColor(context, 1, sm, sell);
            } else {
                rate_layout.setVisibility(View.GONE);
            }
        } else {
            rate_layout.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.product_details_new, frameLayout);

        context = ProductDetails.this;
        gd = new GetData(context);
        userUtils = new UserUtils(context);
        pm = (ProductModel) getIntent().getSerializableExtra("data");

        this.registerReceiver(this.rates, new IntentFilter("getRates"));

        menu.setImageResource(R.drawable.ic_back_gold);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        scroll = findViewById(R.id.scroll);

        // IMages
        images = findViewById(R.id.images);
        images.setAdapter(new MyPagerAdapter());

        PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setAnimationType(AnimationType.FILL);
        pageIndicatorView.setRadius(6);
        pageIndicatorView.setPadding(6);
        pageIndicatorView.setSelectedColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        pageIndicatorView.setUnselectedColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        pageIndicatorView.setViewPager(images);

        RelativeLayout ind_lay = findViewById(R.id.ind_lay);
        if (images.getAdapter().getCount() > 1) {
            ind_lay.setVisibility(View.VISIBLE);
        } else {
            ind_lay.setVisibility(View.GONE);
        }

        title = findViewById(R.id.title);
        title.setText(pm.getProduct_title());
        size_main = findViewById(R.id.size_main);
        size_main.setText(pm.getWeight() + " " + pm.getSubSource());
        price = findViewById(R.id.price);
        carats = findViewById(R.id.carats);
        if (pm.getSource().toLowerCase().equals("jewellery")) {
            price.setText("Price : --");
            CARAT = "18";
            carats.setVisibility(View.VISIBLE);
        } else {
            price.setText("Price : " + pm.getOurPrice());
            carats.setVisibility(View.GONE);
        }
        sku = findViewById(R.id.sku);
        sku.setText("(SKU : " + pm.getProductCode() + ")");
        desc = findViewById(R.id.desc);
        desc.setText(pm.getDescription());

//        scroll.setOnScrollChangedListener(new MyNestedScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(NestedScrollView who, int l, int t, int oldl, int oldt) {
//                ViewCompat.setTranslationY(findViewById(R.id.pager_layout), t * 0.5f);
//            }
//        });
        loadData(utils.getPrefrence(LIVE_RATES, ""));
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.c_18:
                if (checked)
                    CARAT = "18";
                break;
            case R.id.c_22:
                if (checked)
                    CARAT = "22";
                break;
        }
    }

    public void addToCart(View view) {
        if (userUtils.isLoggedIn()) {
            if (!checkInCart(pm.getProduct_id())) {
                CartModel cm = new CartModel(pm, CARAT);
                insertCartData(cm);
                updateCartCount();
                Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Product is already in cart", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent i = new Intent(context, Login.class);
            startActivityForResult(i, 69);
            overridePendingTransition(R.anim.push_up_in, R.anim.stable);
        }
    }

    public void buyNow(View view) {
        if (userUtils.isLoggedIn()) {
            if (!checkInCart(pm.getProduct_id())) {
                CartModel cm = new CartModel(pm, CARAT);
                insertCartData(cm);
                Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Product is already in cart", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(context, MyCart.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_up_in, R.anim.stable);
        } else {
            Intent i = new Intent(context, Login.class);
            startActivityForResult(i, 69);
            overridePendingTransition(R.anim.push_up_in, R.anim.stable);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLayout)) {
            mDrawerLayout.closeDrawer(mLayout);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rates != null) {
            this.unregisterReceiver(this.rates);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        private String[] IMAGES;

        private MyPagerAdapter() {
            this.IMAGES = pm.getProduct_image().split(",");
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
            final String imagePath = PRODUCT_IMAGE + pm.getProduct_id() + "/thumb/" + IMAGES[position];
            Picasso.with(context)
                    .load(imagePath)
//                    .resize(280, 280)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(context).load(imagePath.replaceAll("/thumb", "/large"))
                                    .placeholder(imageView.getDrawable()).into(imageView);
                        }

                        @Override
                        public void onError() {
                            Log.e("Error", PRODUCT_IMAGE + IMAGES[position]);
                        }
                    });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, FullImage.class);
                    i.putExtra("data", IMAGES);
                    i.putExtra("id", pm.getProduct_id());
                    i.putExtra("pos", position);
                    startActivity(i);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });

            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}
