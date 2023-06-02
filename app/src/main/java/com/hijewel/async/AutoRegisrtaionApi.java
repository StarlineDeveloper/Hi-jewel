package com.hijewel.async;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hijewel.utils.Functions;
import com.hijewel.utils.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.KEY_REGID;
import static com.hijewel.utils.Constatnts.LINK;

public class AutoRegisrtaionApi extends AsyncTask<Void, Void, Void> {
    private static final String TAG = AutoRegisrtaionApi.class.getSimpleName();
    private String response, MAC_ID, DEVICE_TOKEN;
    private Context context;
    private PreferenceUtils sessionManager;

    public AutoRegisrtaionApi(Context context) {
        this.context = context;
        sessionManager = new PreferenceUtils(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    protected Void doInBackground(Void... voids) {

        try {
            MAC_ID = Functions.getMacAddr();
            DEVICE_TOKEN = sessionManager.getSavedUser(KEY_REGID);

            if (Functions.isOnline(context)) {

                SoapObject soapObject = new SoapObject(LINK, "AutoRegistrationDetailsNew");
                soapObject.addProperty("DeviceToken", DEVICE_TOKEN);
                soapObject.addProperty("DeviceType", "android");
                soapObject.addProperty("Mac", MAC_ID);

                response = Functions.loadService(API, soapObject, "AutoRegistrationDetailsNew");

                Log.e(TAG, "Registraion response: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
