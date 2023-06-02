package com.hijewel;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.hijewel.async.GetAndSaveData;
import com.hijewel.models.SymbolModel;
import com.hijewel.ui.MyNestedScrollView;
import com.hijewel.utils.GetData;
import com.victor.loading.rotate.RotateLoading;

import org.xml.sax.XMLReader;

import static com.hijewel.utils.Constatnts.ABOUT;
import static com.hijewel.utils.Constatnts.ABOUT_US;
import static com.hijewel.utils.Constatnts.KEY_STATUS_DISPLAY;
import static com.hijewel.utils.Constatnts.LIVE_RATES;
import static com.hijewel.utils.Functions.changeColor;
import static com.hijewel.utils.Functions.isOnline;

public class CompanyProfile extends MainActivity implements GetAndSaveData.OnDataGetListener {

    Context context;
    GetData gd;

    WebView about;
    MyNestedScrollView scroll;
    RotateLoading progress;

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
        getLayoutInflater().inflate(R.layout.company_profile, frameLayout);

        context = CompanyProfile.this;
        gd = new GetData(context);

        this.registerReceiver(this.rates, new IntentFilter("getRates"));

        about = findViewById(R.id.about);
        about.getSettings().setJavaScriptEnabled(true);
        about.getSettings().setTextZoom(100);
        about.setBackgroundColor(Color.TRANSPARENT);
        about.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        about.loadUrl("javascript:document.body.style.setProperty(\"color\", \"white\");");
        scroll = findViewById(R.id.scroll);
        progress = findViewById(R.id.progress);
        if (isOnline(context)) {
            new GetAndSaveData(context, ABOUT_US).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            progress.stop();
            scroll.setVisibility(View.VISIBLE);
            about.loadDataWithBaseURL("", "<font color='white'>" + utils.getPrefrence(ABOUT_US, "").replaceAll("&lt;", "<").replaceAll("&gt;", ">") + "</font>", "text/html", "UTF-8", "");
        }

        loadData(utils.getPrefrence(LIVE_RATES, ""));
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

    @Override
    public void onDataGet(String data) {
        progress.stop();
        scroll.setVisibility(View.VISIBLE);
        about.loadDataWithBaseURL("", "<font color='white'>" + data.replaceAll("&lt;", "<").replaceAll("&gt;", ">") + "</font>", "text/html", "UTF-8", "");
    }

    public class UlTagHandler implements Html.TagHandler {
        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                              XMLReader xmlReader) {
            if (tag.equals("ul") && !opening) output.append("\n");
            if (tag.equals("li") && opening) output.append("\n\t•");
        }
    }
}
