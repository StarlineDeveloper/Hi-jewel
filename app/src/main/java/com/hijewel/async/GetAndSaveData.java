package com.hijewel.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hijewel.utils.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Functions.isOnline;
import static com.hijewel.utils.Functions.loadService;

/**
 * Created by ${Vrund} on 12-02-2018.
 */

public class GetAndSaveData extends AsyncTask<String, String, String> {

    private Context context;
    private PreferenceUtils utils;
    private String method;
    private OnDataGetListener listener;

    public GetAndSaveData(Context context, String method) {
        this.context = context;
        this.method = method;
        utils = new PreferenceUtils(context);
        try {
            listener = (OnDataGetListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        if (isOnline(context)) {
            SoapObject request = new SoapObject(LINK, method);
            try {
                response = loadService(API, request, method);
                utils.setPrefrences(method, response);
                Log.e("cats_RES", response);
            } catch (Exception e) {
                Log.e("cats_ERROR", e.toString());
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (listener != null)
            listener.onDataGet(s);
    }

    public interface OnDataGetListener {
        public void onDataGet(String data);
    }
}
