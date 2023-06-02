package com.hijewel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hijewel.async.AutoRegisrtaionApi;

import org.ksoap2.serialization.SoapObject;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Functions.BuildSnake;
import static com.hijewel.utils.Functions.getUniqueId;
import static com.hijewel.utils.Functions.isOnline;
import static com.hijewel.utils.Functions.loadService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

public class Register extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    Context context;
    public static Activity activityRegister;

    AppCompatEditText name, mobile, email, city;
    LinearLayout login;

    String NAME, MOBILE, EMAIL, CITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        context = Register.this;
        activityRegister = Register.this;
        new AutoRegisrtaionApi(context).execute();
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        city = findViewById(R.id.city);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Intent i = new Intent(context, Login.class);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void register(View view) {
        NAME = name.getText().toString().trim();
        MOBILE = mobile.getText().toString().trim();
        EMAIL = email.getText().toString().trim();
        CITY = city.getText().toString().trim();
        if (NAME.isEmpty()) {
            BuildSnake(context, findViewById(R.id.forSnake), "Please Enter Your Name");
        } else if (MOBILE.length() < 10) {
            BuildSnake(context, findViewById(R.id.forSnake), "Please Enter Valid Contact Number");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()) {
            BuildSnake(context, findViewById(R.id.forSnake), "Please Enter Valid Email");
        } else if (CITY.isEmpty()) {
            BuildSnake(context, findViewById(R.id.forSnake), "Please Enter City");
        } else {
//            if (checkPermissionSMS(context)) {
            if (isOnline(context)) {
                new RegisterKEK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(context, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
            }
//            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 100) {
////            if (checkPermissionSMS(context)) {
//                if (isOnline(context)) {
//                    new RegisterKEK().execute();
//                } else {
//                    Toast.makeText(context, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
//                }
////            } else {
////                Toast.makeText(context, "OTP will not be detetcted automatically", Toast.LENGTH_SHORT).show();
////            }
//        }
//    }

    private class RegisterKEK extends AsyncTask<String, String, String> {

        boolean error = false;
        ProgressDialog pd;
        String regresponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Please Wait..");
        }

        @Override
        protected String doInBackground(String... params) {
            if (isOnline(context)) {
                SoapObject request = new SoapObject(LINK, "ClientRegister");
                request.addProperty("Name", NAME);
                request.addProperty("MobileNo", MOBILE);
                request.addProperty("Email", EMAIL);
                request.addProperty("City", CITY);
                request.addProperty("Mac", getUniqueId(context));

                try {
                    regresponse = loadService(API, request, "ClientRegister");
                    Log.e("reg_response", regresponse);
                } catch (Exception e) {
                    error = true;
                    regresponse = "something went wrong..";
                    Log.e("reg_response", e.toString());
                    e.printStackTrace();
                }
            } else {
                regresponse = "Please Check Your Internet Connection..";
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
                if (regresponse.toLowerCase().contains("ok")) {
                    String[] kek = regresponse.split("~");
                    Toast.makeText(getApplicationContext(), kek[1], Toast.LENGTH_LONG)   .show();
                    Intent i = new Intent(context, OTPActivity.class);
                    i.putExtra("mobile", MOBILE);
                    i.putExtra("otp", kek[3]);

                    Log.e("OTP", kek[3]);

                    i.putExtra("client_id", kek[2]);
                    startActivity(i);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
                    Toast.makeText(getApplicationContext(), regresponse, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), regresponse, Toast.LENGTH_LONG).show();
            }
        }
    }
}
