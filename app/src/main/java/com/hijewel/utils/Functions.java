package com.hijewel.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
//import android.support.design.widget.Snackbar;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hijewel.R;
import com.hijewel.models.SymbolModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Constatnts.URL;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class Functions {

    public static void BuildSnake(Context context, View forSnake, String msg) {
        Snackbar snackbar = Snackbar.make(forSnake, msg, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        snackbar.show();
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static boolean isOnline(Context con) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            return networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return false;
    }

    @SuppressLint("HardwareIds")
    public static String getUniqueId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void call(Context context, String phone_no) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone_no));
        context.startActivity(callIntent);
    }

    public static boolean checkForEmpty(String kek) {
        return (kek.equals("") || kek.equals("0"));
    }

    public static boolean checkForEmpty(String name, String num) {
        return (name.equals("") || name.equals("0") || num.equals("") || num.equals("0"));
    }

    public static boolean appInstalledOrNot(Context context, String paramString) {
        PackageManager localPackageManager = context.getPackageManager();
        try {
            //noinspection WrongConstant
            localPackageManager.getPackageInfo(paramString, 0);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    public static void closeKeyboard(Context context) {
        try {
            View view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String loadService(String API, SoapObject request, String method) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        HttpTransportSE transport = new HttpTransportSE(API);
        transport.call(LINK + method, envelope);
        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
        return response.toString();
    }


    public static String loadServiceString(SoapObject request, String method) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        HttpTransportSE transport = new HttpTransportSE(API);
        transport.call(LINK + method, envelope);
        return envelope.getResponse().toString();
    }

    public static SoapPrimitive loadServiceKEK(SoapObject request, String method) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        HttpTransportSE transport = new HttpTransportSE(URL);
        transport.call(LINK + method, envelope);
        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
        Log.e("rekjhvjdhv", response.toString());
        return response;
    }

    public static SoapObject loadServiceObject(SoapObject request, String method) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        HttpTransportSE transport = new HttpTransportSE(URL);
        transport.call(LINK + method, envelope);
        return (SoapObject) envelope.getResponse();
    }

    public static SoapObject loadServiceObjectTest(SoapObject request, String method) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        HttpTransportSE transport = new HttpTransportSE("http://www.mxgold.co/webservice/WebServiceMXGold.asmx");
        transport.call(LINK + method, envelope);
        return (SoapObject) envelope.getResponse();
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String checkIT(String p, String c) {
        String status;
        Log.e("gagaga", p + " " + c);
        try {
            float prev = Float.valueOf(p);
            float crt = Float.valueOf(c);
            if (prev < crt) {
                status = "up";
            } else if (prev > crt) {
                status = "down";
            } else {
                status = "eq";
            }
        } catch (Exception e) {
            status = "eq";
            e.printStackTrace();
        }
        return status;
    }

    public static String[] formatDate(String dataaaa) {
        String[] kek = new String[2];
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
            Date newDate = format.parse(dataaaa);
            format = new SimpleDateFormat("hh:mm a", Locale.US);
            kek[0] = format.format(newDate);
            format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            kek[1] = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return kek;
    }

    public static void postVersion(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.update_available, null);
        TextView update_txt = dialogView.findViewById(R.id.update_txt);
        update_txt.setText(activity.getString(R.string.update_txt) + " " + activity.getString(R.string.app_name));
        builder.setView(dialogView);
        final AlertDialog alt = builder.create();
        alt.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                activity.finish();
            }
        });
        alt.show();

        dialogView.findViewById(R.id.remindLater).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alt.dismiss();
                        activity.finish();
                    }
                });

        dialogView.findViewById(R.id.updateNow).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alt.dismiss();
                        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            activity.startActivity(myAppLinkToMarket);
                            activity.onBackPressed();
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(activity.getApplicationContext(), "unable to find market app", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void changeColor(Context context, int kek, SymbolModel mdl, TextView tv) {
        int WHITE = Color.WHITE;
        int GREEN = ContextCompat.getColor(context, R.color.green);
        int RED = ContextCompat.getColor(context, R.color.red);
        if (kek == 0) {
            if (mdl.getBidStatus().equals("down")) {
                if (mdl.getBid().equals("--")) {
                    tv.setTextColor(WHITE);
                } else {
                    tv.setTextColor(RED);
                }
            } else if (mdl.getBidStatus().equals("up")) {
                if (mdl.getBid().equals("--")) {
                    tv.setTextColor(WHITE);
                } else {
                    tv.setTextColor(GREEN);
                }
            } else if (mdl.getBidStatus().equals("eq")) {
                tv.setTextColor(WHITE);
            }
        } else {
            // ASK
            if (mdl.getAskStatus().equals("down")) {
                if (mdl.getAsk().equals("--")) {
                    tv.setTextColor(WHITE);
                } else {
                    tv.setTextColor(RED);
                }
            } else if (mdl.getAskStatus().equals("up")) {
                if (mdl.getAsk().equals("--")) {
                    tv.setTextColor(WHITE);
                } else {
                    tv.setTextColor(GREEN);
                }
            } else if (mdl.getAskStatus().equals("eq")) {
                tv.setTextColor(WHITE);
            }
        }
    }

    public static void openAppUpdateDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.update_available, null);
        TextView update_txt = (TextView) dialogView.findViewById(R.id.update_txt);
        update_txt.setText(activity.getString(R.string.update_txt) + " " + activity.getString(R.string.app_name));
        builder.setView(dialogView);
        final AlertDialog alt = builder.create();
        alt.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                activity.finish();
            }
        });
        alt.show();

        dialogView.findViewById(R.id.updateNow).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alt.dismiss();
                        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            activity.startActivity(myAppLinkToMarket);
                            activity.onBackPressed();
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(activity.getApplicationContext(), "unable to find market app", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
