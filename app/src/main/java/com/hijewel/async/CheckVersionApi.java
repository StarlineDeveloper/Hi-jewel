package com.hijewel.async;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.hijewel.BuildConfig;
import com.hijewel.Home;
import com.hijewel.R;
import com.hijewel.utils.Constatnts;
import com.hijewel.utils.Functions;
import com.hijewel.utils.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;


public class CheckVersionApi extends AsyncTask<Void, Void, Void> {

    private static final String TAG = CheckVersionApi.class.getSimpleName();
    private String play_store_version_code;
    private Activity activity;
    private int CURRENT_VERSION_CODE;


    public CheckVersionApi(Activity activity) {
        this.activity = activity;
        CURRENT_VERSION_CODE = BuildConfig.VERSION_CODE;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            SoapObject soapObject = new SoapObject(Constatnts.LINK, Constatnts.GET_VERSION);
            play_store_version_code = Functions.loadServiceString(soapObject, Constatnts.GET_VERSION);

            Log.e(TAG, "Version: "+play_store_version_code );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (play_store_version_code != null && !play_store_version_code.isEmpty()) {
            try {

                if (CURRENT_VERSION_CODE < Integer.parseInt(play_store_version_code)) {

                    Functions.openAppUpdateDialog(activity);

                } else {
                    activity.startActivity(new Intent(activity, Home.class));
                    activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }

            } catch (NumberFormatException e) {
                activity.startActivity(new Intent(activity, Home.class));
                activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        } else {

            activity.startActivity(new Intent(activity, Home.class));
            activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }


    }

}
