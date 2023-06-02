package com.hijewel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.hijewel.adapters.BannerAdapterkek;
import com.hijewel.adapters.ProductAdapterHome;
import com.hijewel.models.ProductModel;
import com.hijewel.models.SymbolModel;
import com.hijewel.ui.MyNestedScrollView;
import com.hijewel.ui.NonScrollGridView;
import com.hijewel.ui.TabletTransformer;
import com.hijewel.utils.Constatnts;
import com.hijewel.utils.GetData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.KEY_STATUS_DISPLAY;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Constatnts.LIVE_RATES;
import static com.hijewel.utils.Constatnts.PRODUCT_SEARCH;
import static com.hijewel.utils.Functions.changeColor;
import static com.hijewel.utils.Functions.loadService;

public class Home extends MainActivity {

    Context context;
    GetData gd;

    MyNestedScrollView scroll;
    LinearLayout utsav_layout, rohit_layout,rohit_layout2;
    AutoScrollViewPager banners;
    NonScrollGridView utsav, rohit,rohit2;

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
        getLayoutInflater().inflate(R.layout.home, frameLayout);

        context = Home.this;
        gd = new GetData(context);

//        getProductDetailsUsingSKU("UPP2");
        if (getIntent().hasExtra("SKU")) {

            getProductDetailsUsingSKU(getIntent().getStringExtra("SKU"));
        } else {

            this.registerReceiver(this.rates, new IntentFilter("getRates"));
        }

        //Banners
        banners = findViewById(R.id.banners);
        BannerAdapterkek adapter = new BannerAdapterkek(context, gd.getBanners());
        banners.setAdapter(adapter);
        banners.setScrollFactor(5);
        banners.setOffscreenPageLimit(adapter.getCount());
        banners.startAutoScroll(2000);
        banners.setPageTransformer(true, new TabletTransformer());

        //utsav
        utsav_layout = findViewById(R.id.utsav_layout);
        utsav = findViewById(R.id.utsav);
        utsav.setFocusable(false);
        ProductAdapterHome UTSAV = new ProductAdapterHome(context, gd.getHomeProducts("utsav"));
        utsav.setAdapter(UTSAV);
        if (UTSAV.getCount() > 0) {
            utsav_layout.setVisibility(View.VISIBLE);
        } else {
            utsav_layout.setVisibility(View.GONE);
        }

        //rohit
        rohit_layout = findViewById(R.id.rohit_layout);
        rohit_layout2 = findViewById(R.id.rohit_layout2);
        rohit = findViewById(R.id.rohit);
        rohit2 = findViewById(R.id.rohit2);
        rohit.setFocusable(false);
        rohit2.setFocusable(false);

        ProductAdapterHome ROHIT = new ProductAdapterHome(context, gd.getHomeProducts("Platinium"));
        rohit.setAdapter(ROHIT);
        if (ROHIT.getCount() > 0) {
            rohit_layout.setVisibility(View.VISIBLE);
        } else {
            rohit_layout.setVisibility(View.GONE);
        }


        ProductAdapterHome ROHIT2 = new ProductAdapterHome(context, gd.getHomeProducts("jewellery"));
        rohit2.setAdapter(ROHIT2);
        if (ROHIT2.getCount() > 0) {
            rohit_layout2.setVisibility(View.VISIBLE);
        } else {
            rohit_layout2.setVisibility(View.GONE);
        }

        Log.e("pppnewdatad",""+gd.getHomeProducts("Platinium"));

        scroll = findViewById(R.id.scroll);
//        scroll.setOnScrollChangedListener(new MyNestedScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(NestedScrollView who, int l, int t, int oldl, int oldt) {
//                ViewCompat.setTranslationY(findViewById(R.id.banners), t * 0.5f);
//            }
//        });
        loadData(utils.getPrefrence(LIVE_RATES, ""));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLayout)) {
            mDrawerLayout.closeDrawer(mLayout);
        } else {
            isLaunch = true;
            finishAffinity();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rates != null) {
            this.unregisterReceiver(this.rates);
        }
    }

    private void getProductDetailsUsingSKU(final String skuID) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SoapObject request = new SoapObject(LINK, PRODUCT_SEARCH);
                request.addProperty("Product", skuID);
                Log.e("Pro_req", request.toString());
                try {
                    String response = loadService(API, request, PRODUCT_SEARCH);
                    Log.e("responsetttt", response);

                    ProductModel productModel = new ProductModel();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Table");
                    if (jsonArray.length() > 0) {

                        JSONObject productObj = jsonArray.getJSONObject(0);

                        productModel.setProduct_id(productObj.getString("ProductID"));
                        productModel.setProduct_title(productObj.getString("ProductName"));
                        productModel.setProductCode(productObj.getString("ProductCode"));
                        productModel.setDescription(productObj.getString("Description"));
                        productModel.setShortDescription(productObj.getString("ShortDescription"));
                        productModel.setProduct_image(productObj.getString("ProductImage1"));
                        productModel.setMrpPrice(productObj.getString("MrpPrice"));
                        productModel.setOurPrice(productObj.getString("OurPrice"));
                        productModel.setWeight(productObj.getString("Weight"));
                        productModel.setSpecification(productObj.getString("Specification"));
                        productModel.setSource(productObj.getString("Source"));
                        productModel.setSubSource(productObj.getString("SubSource"));

                    }


                    Intent i = new Intent(context, ProductDetails.class);
                    i.putExtra("data", productModel);
                    context.startActivity(i);
                    ((Activity) context).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    Log.e("Pro_res", response);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                    e.printStackTrace();
                }
            }

        }).start();


    }
}
