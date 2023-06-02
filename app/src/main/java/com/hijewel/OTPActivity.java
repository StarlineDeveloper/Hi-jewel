package com.hijewel;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Functions.BuildSnake;
import static com.hijewel.utils.Functions.isOnline;
import static com.hijewel.utils.Functions.loadService;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class OTPActivity extends AppCompatActivity {

    Context context;
    public static Activity activityOTP;

    TextView sub_txt, text1, text2, text3, text4;

    String OTP, MOBILE,CLIENT_ID;

//    private BroadcastReceiver SmsListener = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(
//                    "android.provider.Telephony.SMS_RECEIVED")) {
//                final Bundle bundle = intent.getExtras();
//                try {
//                    if (bundle != null) {
//                        final Object[] pdusObj = (Object[]) bundle.get("pdus");
//                        assert pdusObj != null;
//                        for (int i = 0; i < pdusObj.length; i++) {
//                            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
//                            String senderNum = currentMessage.getDisplayOriginatingAddress();
//                            String test = currentMessage.getDisplayMessageBody();
//                            String message = test.substring(test.length() - 6, test.length() - 2);
//                            try {
//                                if (senderNum.contains("HIJEWL")) {
//                                    text1.setText(message.substring(0, 1));
//                                    text2.setText(message.substring(1, 2));
//                                    text3.setText(message.substring(2, 3));
//                                    text4.setText(message.substring(3, 4));
//                                    OTP = text1.getText().toString() + text2.getText().toString() +
//                                            text3.getText().toString() + text4.getText().toString();
//                                    if (isOnline(context)) {
//                                        new VerifyOtp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                    } else {
//                                        Toast.makeText(context, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_screen);

        context = OTPActivity.this;
        activityOTP = OTPActivity.this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        MOBILE = intent.getStringExtra("mobile");
        sub_txt = findViewById(R.id.sub_txt);
        sub_txt.setText("Please type verification code sent to " + MOBILE);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);

//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
//            context.registerReceiver(SmsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
//        }
    }

    public void setText(View view) {
        setTextToEditor(((TextView) view).getText());
    }

    public void removeText(View view) {
        if (!text4.getText().toString().isEmpty()) {
            text4.setText("");
        } else if (!text3.getText().toString().isEmpty()) {
            text3.setText("");
        } else if (!text2.getText().toString().isEmpty()) {
            text2.setText("");
        } else if (!text1.getText().toString().isEmpty()) {
            text1.setText("");
        }
    }

    public void resend(View view) {
        if (isOnline(context)) {
            new ResendOTP().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(context, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
        }
    }

    public void verifyOtp(View view) {
        OTP = text1.getText().toString() + text2.getText().toString() +
                text3.getText().toString() + text4.getText().toString();
        if (OTP.isEmpty()) {
            BuildSnake(context, findViewById(R.id.forSnake), "Enter OTP");
        } else {
            if (isOnline(context)) {
                new VerifyOtp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(context, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setTextToEditor(CharSequence s) {
        if (text1.getText().toString().isEmpty()) {
            text1.setText(s);
        } else if (text2.getText().toString().isEmpty()) {
            text2.setText(s);
        } else if (text3.getText().toString().isEmpty()) {
            text3.setText(s);
        } else if (text4.getText().toString().isEmpty()) {
            text4.setText(s);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (SmsListener != null)
//            this.unregisterReceiver(SmsListener);
//    }

    private class VerifyOtp extends AsyncTask<String, String, String> {

        boolean error = false;
        ProgressDialog pd;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Verifying..");
        }

        @Override
        protected String doInBackground(String... params) {
            if (isOnline(context)) {
                SoapObject request = new SoapObject(LINK, "CheckClientOTP");
                request.addProperty("Mobile", MOBILE);
                request.addProperty("Otp", OTP);

                try {
                    response = loadService(API, request, "CheckClientOTP");
                    Log.e("otp_response", response);
                } catch (Exception e) {
                    error = true;
                    response = "something went wrong..";
                    Log.e("otp_response", e.toString());
                    e.printStackTrace();
                }
            } else {
                response = "Please Check Your Internet Connection..";
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing())
                pd.dismiss();
            if (!error) {
                if (response.toLowerCase().contains("ok")) {
                    String[] kek = response.split("~");
//                    if (SmsListener != null)
//                        context.unregisterReceiver(SmsListener);
                    Intent i = new Intent(context, CreatePass.class);
                    i.putExtra("mobile", MOBILE);
                    startActivity(i);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ResendOTP extends AsyncTask<String, String, String> {

        boolean error = false;
        ProgressDialog pd;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Please Wait..");
        }

        @Override
        protected String doInBackground(String... params) {
            if (isOnline(context)) {
                SoapObject request = new SoapObject(LINK, "ResendOTP");
                request.addProperty("Mobile", MOBILE);

                try {
                    response = loadService(API, request, "ResendOTP");
                    Log.e("resendotp_response", response);
                } catch (Exception e) {
                    error = true;
                    response = "something went wrong..";
                    Log.e("presendotp_response", e.toString());
                    e.printStackTrace();
                }
            } else {
                response = "Please Check Your Internet Connection..";
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing())
                pd.dismiss();
            if (!error) {
                if (response.toLowerCase().contains("ok")) {
                    String[] kek = response.split("~");
                    Toast.makeText(getApplicationContext(), kek[1], Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }
    }
}
