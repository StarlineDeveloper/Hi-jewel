package com.hijewel;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hijewel.models.SymbolModel;
import com.hijewel.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.hijewel.utils.Constatnts.KEY_ASK;
import static com.hijewel.utils.Constatnts.KEY_BID;
import static com.hijewel.utils.Constatnts.KEY_BUY_HEAD;
import static com.hijewel.utils.Constatnts.KEY_GSYMBPLE;
import static com.hijewel.utils.Constatnts.KEY_HIGH;
import static com.hijewel.utils.Constatnts.KEY_ISDISPLAY;
import static com.hijewel.utils.Constatnts.KEY_LOW;
import static com.hijewel.utils.Constatnts.KEY_MSG;
import static com.hijewel.utils.Constatnts.KEY_PRODUCT;
import static com.hijewel.utils.Constatnts.KEY_PRODUCTTYPE;
import static com.hijewel.utils.Constatnts.KEY_P_HEADER;
import static com.hijewel.utils.Constatnts.KEY_SELL_HEAD;
import static com.hijewel.utils.Constatnts.KEY_SOURCE;
import static com.hijewel.utils.Constatnts.KEY_STATUS;
import static com.hijewel.utils.Constatnts.KEY_STATUS_DISPLAY;
import static com.hijewel.utils.Constatnts.KEY_STOCK;
import static com.hijewel.utils.Constatnts.KEY_SYMBOLID;
import static com.hijewel.utils.Constatnts.LIVE_RATES;
import static com.hijewel.utils.Constatnts.VALUE_BUY;
import static com.hijewel.utils.Constatnts.VALUE_SELL;
import static com.hijewel.utils.Functions.changeColor;
import static com.hijewel.utils.Functions.checkIT;

public class Liverates extends MainActivity {

    Context context;
    PreferenceUtils utils;

    TextView textRateNA, main_product, main_sell,
            spot_product, spot_bid, spot_ask, spot_low, spot_high,
            future_product, future_bid, future_ask, future_low, future_high;
    LinearLayout main_layout, spot_layout, future_layout;


    TextView tv_symbol_inr,inr_bid,inr_ask,inr_low,inr_high;

    ArrayList<SymbolModel> mainData, spotData, futureData,inrSpot;

    private BroadcastReceiver rates = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.e("kekdkghdj", "ahahaha");
            loadData(intent.getStringExtra("data"));
        }
    };

    private void loadData(String data) {
        spotData = new ArrayList<>();
        mainData = new ArrayList<>();
        futureData = new ArrayList<>();
        inrSpot = new ArrayList<>();



        try {
            JSONObject jsonObject = new JSONObject(data);

            Log.e("jsonObject",""+jsonObject);

            JSONObject main = jsonObject.optJSONObject("NewDataSet");
            utils.setPrefrences(KEY_STATUS_DISPLAY, main.optJSONObject("DisplayRate").optString(KEY_STATUS_DISPLAY));
            utils.setPrefrences(KEY_MSG, main.optJSONObject("DisplayRateMessage").optString(KEY_MSG));
            String product_header = main.optJSONObject("ProductHeaderDetails").optString(KEY_P_HEADER);
            utils.setPrefrences(KEY_P_HEADER, product_header);
            String hdr[] = product_header.split("~");
            utils.setPrefrences(KEY_BUY_HEAD, hdr[2]);
            utils.setPrefrences(VALUE_BUY, hdr[3]);
            utils.setPrefrences(KEY_SELL_HEAD, hdr[0]);
            utils.setPrefrences(VALUE_SELL, hdr[1]);
            utils.setPrefrences(KEY_PRODUCT, hdr[4]);
            JSONArray jsonArray = main.optJSONArray("GeneralPremium");
            for (int i = 0; i < jsonArray.length(); i++) {
                SymbolModel sm = new SymbolModel();
                JSONObject j = jsonArray.optJSONObject(i);
                sm.setSymbolId(Integer.valueOf(j.optString(KEY_SYMBOLID)));
                sm.setSymbol(j.optString(KEY_GSYMBPLE));
                sm.setBid(j.optString(KEY_BID));
                sm.setAsk(j.optString(KEY_ASK));
                sm.setHigh(j.optString(KEY_HIGH));
                sm.setLow(j.optString(KEY_LOW));
                sm.setSource(j.optString(KEY_SOURCE));
                sm.setStock(j.optString(KEY_STOCK));
                sm.setProductType(j.optString(KEY_PRODUCTTYPE));
                sm.setStatus(j.optString(KEY_STATUS));
                sm.setBidStatus(checkIT(utils.getPrefrence("last_bid" + sm.getSymbolId(), ""), sm.getBid()));
                sm.setAskStatus(checkIT(utils.getPrefrence("last_ask" + sm.getSymbolId(), ""), sm.getAsk()));
                sm.setIsDisplay("1");
                sm.setW_display(j.optString(KEY_ISDISPLAY));
                utils.setPrefrences("last_bid" + sm.getSymbolId(), sm.getBid());
                utils.setPrefrences("last_ask" + sm.getSymbolId(), sm.getAsk());

                Log.e("eeeeeeee",sm.getProductType());

                if (sm.getProductType().equals("0")) {
                    mainData.add(sm);
                } else if (sm.getProductType().equals("1")) {
                    spotData.add(sm);
                } else if (sm.getProductType().equals("2")) {
                    futureData.add(sm);
                }

                if(sm.getSource().equals("INRSpot")){
                    inrSpot.add(sm);

                }
            }


            product.setText(mainData.get(0).getSymbol());
            sell.setText(mainData.get(0).getAsk());
            changeColor(context, 1, mainData.get(0), sell);
            utils.setPrefrences(LIVE_RATES, data);
            update();
        } catch (Exception e) {
            Log.e("Error", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.liverates, frameLayout);

        context = Liverates.this;
        utils = new PreferenceUtils(context);

        this.registerReceiver(this.rates, new IntentFilter("getRates"));

        // Main
        textRateNA = findViewById(R.id.textRateNA);
        main_layout = findViewById(R.id.main_layout);
        main_product = findViewById(R.id.main_product);
        main_sell = findViewById(R.id.main_sell);

        //Spot
        spot_layout = findViewById(R.id.spot_layout);
        spot_product = findViewById(R.id.spot_product);
        spot_bid = findViewById(R.id.spot_bid);
        spot_ask = findViewById(R.id.spot_ask);
        spot_low = findViewById(R.id.spot_low);
        spot_high = findViewById(R.id.spot_high);

        //future
        future_layout = findViewById(R.id.future_layout);
        future_product = findViewById(R.id.future_product);
        future_bid = findViewById(R.id.future_bid);
        future_ask = findViewById(R.id.future_ask);
        future_low = findViewById(R.id.future_low);
        future_high = findViewById(R.id.future_high);


        tv_symbol_inr=findViewById(R.id.tv_symbol_inr);
        inr_bid = findViewById(R.id.inr_bid);
        inr_ask = findViewById(R.id.inr_ask);
        inr_low = findViewById(R.id.inr_low);
        inr_high = findViewById(R.id.inr_high);





        loadData(utils.getPrefrence(LIVE_RATES, ""));

    }

    private void update() {
        try {
            if (utils.getPrefrence(KEY_STATUS_DISPLAY, "").equals("true")) {
                textRateNA.setVisibility(View.GONE);
                if (mainData.size() > 0) {
                    main_layout.setVisibility(View.VISIBLE);
                } else {
                    main_layout.setVisibility(View.GONE);
                }
            } else {
                main_layout.setVisibility(View.GONE);
                textRateNA.setVisibility(View.VISIBLE);
                textRateNA.setText(utils.getPrefrence(KEY_MSG, ""));
            }

            SymbolModel main_data = mainData.get(0);
            main_product.setText(main_data.getSymbol());


            main_sell.setText(main_data.getAsk());
            if (main_data.getAskStatus().equals("down")) {
                if (main_data.getAsk().equals("--")) {
                    main_sell.setBackgroundResource(0);
                } else {
                    main_sell.setBackgroundResource(R.drawable.red_back);
                }
            } else if (main_data.getAskStatus().equals("up")) {
                if (main_data.getAsk().equals("--")) {
                    main_sell.setBackgroundResource(0);
                } else {
                    main_sell.setBackgroundResource(R.drawable.green_back);
                }
            } else if (main_data.getAskStatus().equals("eq")) {
                main_sell.setBackgroundResource(0);
            }

            SymbolModel spot_data = spotData.get(0);
            spot_product.setText(spot_data.getSymbol());
            spot_bid.setText(spot_data.getBid());
            spot_ask.setText(spot_data.getAsk());
            spot_low.setText(spot_data.getLow());
            spot_high.setText(spot_data.getHigh());
            changeColor(context, 0, spot_data, spot_bid);
            changeColor(context, 1, spot_data, spot_ask);

            SymbolModel future_data = futureData.get(0);
            future_product.setText(future_data.getSymbol());
            future_bid.setText(future_data.getBid());
            future_ask.setText(future_data.getAsk());
            future_low.setText(future_data.getLow());
            future_high.setText(future_data.getHigh());
            changeColor(context, 0, future_data, future_bid);
            changeColor(context, 1, future_data, future_ask);



            SymbolModel inrSpotdata = inrSpot.get(0);
            tv_symbol_inr.setText(inrSpotdata.getSymbol());
            inr_bid.setText(inrSpotdata.getBid());
            inr_ask.setText(inrSpotdata.getAsk());
            inr_low.setText(inrSpotdata.getLow());
            inr_high.setText(inrSpotdata.getHigh());
            changeColor(context, 0, inrSpotdata, inr_bid);
            changeColor(context, 1, inrSpotdata, inr_ask);




        } catch (Exception e) {
            e.printStackTrace();
        }

        if (spotData.size() > 0) {
            spot_layout.setVisibility(View.VISIBLE);
        } else {
            spot_layout.setVisibility(View.GONE);
        }
        if (futureData.size() > 0) {
            future_layout.setVisibility(View.VISIBLE);
        } else {
            future_layout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLayout)) {
            mDrawerLayout.closeDrawer(mLayout);
        } else {
            Intent home = new Intent(context, Home.class);
            startActivity(home);
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
}
