package com.hijewel;

import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hijewel.database.MasterDatabase;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseMessaging.getInstance().subscribeToTopic("HiranyaJewellers_android");

        if (MasterDatabase.mDatabase == null) {
            MasterDatabase.mDatabase = openOrCreateDatabase("Hiranya.db", MODE_PRIVATE, null);
        }
    }
}
