package com.hijewel.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Functions.isOnline;
import static com.hijewel.utils.Functions.loadService;


/**
 * Created by ${Vrund} on 02-08-2017.
 */

public class Liveratedb extends AsyncTask<String, String, String> {

    private Context context;
    private String response;
    private boolean error = false;

    public Liveratedb(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        if (isOnline(context)) {
            SoapObject request = new SoapObject(LINK, "GetLiveRateshijewel");
            try {
                response = loadService(API, request, "GetLiveRateshijewel");
                Log.e("response_c", response);
            } catch (Exception e) {
                error = true;
                Log.e("Contact_error", e.toString());
                e.printStackTrace();
            }
        } else {
            error = true;
        }
        return "1";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!error) {
            Intent i = new Intent("getRates");
            i.putExtra("data", response);
            context.sendBroadcast(i);
        }
    }
}
