package com.hijewel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Functions.BuildSnake;
import static com.hijewel.utils.Functions.isOnline;
import static com.hijewel.utils.Functions.loadService;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class CreatePass extends AppCompatActivity {

    Context context;
    public static Activity activityCreatePass;

    AppCompatEditText new_pass, conf_pass;
    ImageView hide_show;

    boolean flag = false;
    String PASS, CONF_PASS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);

        context = CreatePass.this;
        activityCreatePass = CreatePass.this;

        new_pass = findViewById(R.id.new_pass);
        new_pass.setTransformationMethod(new PasswordTransformationMethod());
        conf_pass = findViewById(R.id.conf_pass);
        conf_pass.setTransformationMethod(new PasswordTransformationMethod());

        hide_show = findViewById(R.id.hide_show);
    }

    public void hideSHow(View view) {
        if (!flag) {
            hide_show.setImageResource(R.drawable.ic_hide);
            new_pass.setTransformationMethod(null);
            new_pass.setSelection(new_pass.length());
            conf_pass.setTransformationMethod(null);
            conf_pass.setSelection(conf_pass.length());
            flag = true;
        } else {
            hide_show.setImageResource(R.drawable.ic_show);
            new_pass.setTransformationMethod(new PasswordTransformationMethod());
            new_pass.setSelection(new_pass.length());
            conf_pass.setTransformationMethod(new PasswordTransformationMethod());
            conf_pass.setSelection(conf_pass.length());
            flag = false;
        }
    }

    public void create(View view) {
        PASS = new_pass.getText().toString().trim();
        CONF_PASS = conf_pass.getText().toString().trim();
        if (PASS.isEmpty()) {
            BuildSnake(context, findViewById(R.id.forSnake), "please Enter Password");
        } else if (!PASS.equals(CONF_PASS)) {
            BuildSnake(context, findViewById(R.id.forSnake), "specified password do not match");
        } else {
            if (isOnline(context)) {
                new CreatePassword().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(context, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private class CreatePassword extends AsyncTask<String, String, String> {

        boolean error = false;
        ProgressDialog pd;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Creating..");
        }

        @Override
        protected String doInBackground(String... params) {
            if (isOnline(context)) {
                SoapObject request = new SoapObject(LINK, "CreatePassword");
                request.addProperty("Password", PASS);
                request.addProperty("Mobile", getIntent().getStringExtra("mobile"));

                try {
                    response = loadService(API, request, "CreatePassword");
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
                    Toast.makeText(getApplicationContext(), kek[1], Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context, Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
}
