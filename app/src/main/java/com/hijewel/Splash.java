package com.hijewel;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.hijewel.async.AutoRegisrtaionApi;
import com.hijewel.async.CheckVersionApi;
import com.hijewel.async.GetAndSaveData;
import com.hijewel.async.GetAndSaveHomeData;
import com.hijewel.utils.Functions;

import static com.hijewel.database.MasterDatabase.createCartTable;
import static com.hijewel.utils.Constatnts.BANNERS;
import static com.hijewel.utils.Constatnts.CATEGORIES;
import static com.hijewel.utils.Constatnts.CONTACT_US;
import static com.hijewel.utils.Constatnts.DialogPopOption;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        context = Splash.this;
        createCartTable();
        if (Functions.isOnline(context)) {
            new AutoRegisrtaionApi(context).execute();
            new GetAndSaveData(context, CATEGORIES).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new GetAndSaveData(context, BANNERS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new GetAndSaveData(context, CONTACT_US).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new GetAndSaveHomeData(context, "utsav").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new GetAndSaveHomeData(context, "Platinium").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new GetAndSaveHomeData(context, "jewellery").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            DialogPopOption="1";

        } else {
            Toast.makeText(context, R.string.error_internet_msg, Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {




                if(Functions.isOnline(context)) {
                    new CheckVersionApi(Splash.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(context, R.string.error_internet_msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
    }
}
