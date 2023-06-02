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

public class GetAndSaveHomeData extends AsyncTask<String, String, String> {

    private Context context;
    private PreferenceUtils utils;
    private String SOURCE;
    private OnDataGetListener listener;

    public GetAndSaveHomeData(Context context, String SOURCE) {
        this.context = context;
        this.SOURCE = SOURCE;
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
            SoapObject request = new SoapObject(LINK, "GetCategoryHome_by_url");
            request.addProperty("Source", SOURCE);
            Log.e("REEEQ", request.toString());
            try {
                response = loadService(API, request, "GetCategoryHome_by_url");
                utils.setPrefrences(SOURCE, response);
                Log.e("home_RES", response);
            } catch (Exception e) {
                Log.e("home_ERROR", e.toString());
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
        void onDataGet(String data);
    }
}
