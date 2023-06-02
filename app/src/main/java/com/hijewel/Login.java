package com.hijewel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.AppCompatCheckBox;
//import android.support.v7.widget.AppCompatEditText;
//import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hijewel.utils.PreferenceUtils;
import com.hijewel.utils.UserUtils;

import org.ksoap2.serialization.SoapObject;

import static com.hijewel.CreatePass.activityCreatePass;
import static com.hijewel.OTPActivity.activityOTP;
import static com.hijewel.Register.activityRegister;
import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Functions.BuildSnake;
import static com.hijewel.utils.Functions.isOnline;
import static com.hijewel.utils.Functions.loadService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Context context;
    PreferenceUtils utils;
    UserUtils userUtils;
    RelativeLayout rrLogin;

    AppCompatEditText mobile, password;
    ImageView clear, hide_show;
    TextView register;
    AppCompatCheckBox remember;

    boolean flag = false;
    String MOBILE, PASS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        context = Login.this;
        utils = new PreferenceUtils(context);
        userUtils = new UserUtils(context);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mobile = findViewById(R.id.mobile);
        mobile.setText(utils.getPrefrence("save_mobile", ""));
        mobile.setSelection(mobile.length());
        password = findViewById(R.id.password);
        password.setText(utils.getPrefrence("save_password", ""));
        password.setSelection(password.length());
        password.setTransformationMethod(new PasswordTransformationMethod());
        clear = findViewById(R.id.clear);
        clear.setVisibility(View.GONE);
        clear.setOnClickListener(this);
        hide_show = findViewById(R.id.hide_show);
        hide_show.setImageResource(R.drawable.ic_show);
        hide_show.setOnClickListener(this);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        remember = findViewById(R.id.remember);
        rrLogin = findViewById(R.id.rrLogin);

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void login(View view) {
        MOBILE = mobile.getText().toString().trim();
        PASS = password.getText().toString().trim();
        if (MOBILE.isEmpty()) {
            BuildSnake(context, rrLogin, "Please Enter Your Mobile Number");
        } else if (PASS.isEmpty()) {
            BuildSnake(context, rrLogin, "Please Enter Your Password");
        } else {
//            if (checkPermissionSMS(context)) {
            if (isOnline(context)) {
                new LoginKEK().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(context, "Please Check Your Internet Connection..", Toast.LENGTH_SHORT).show();
            }
//            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                mobile.setText("");
                break;
            case R.id.hide_show:
                if (!flag) {
                    hide_show.setImageResource(R.drawable.ic_hide);
                    password.setTransformationMethod(null);
                    password.setSelection(password.length());
                    flag = true;
                } else {
                    hide_show.setImageResource(R.drawable.ic_show);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    password.setSelection(password.length());
                    flag = false;
                }
                break;
            case R.id.register:
                startActivity(new Intent(context, Register.class));
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stable, R.anim.push_down_in);
        Intent home = new Intent(context, Home.class);
        startActivity(home);

    }

    private class LoginKEK extends AsyncTask<String, String, String> {

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
                SoapObject request = new SoapObject(LINK, "CheckLoginDetailNew");
                request.addProperty("Mobile", MOBILE);
                request.addProperty("loginpass", PASS);

                try {
                    regresponse = loadService(API, request, "CheckLoginDetailNew");
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
                    userUtils.saveUser(kek);
                    if (remember.isChecked()) {
                        utils.setPrefrences("save_mobile", MOBILE);
                        utils.setPrefrences("save_password", PASS);
                    } else {
                        utils.setPrefrences("save_mobile", "");
                        utils.setPrefrences("save_password", "");
                    }
                    Toast.makeText(getApplicationContext(), "Welcome Back " + kek[3], Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);

                    if (activityRegister != null && activityOTP != null && activityCreatePass != null) {

                        activityRegister.finish();
                        activityOTP.finish();
                        activityCreatePass.finish();
                    }
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), regresponse, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), regresponse, Toast.LENGTH_LONG).show();
            }
        }
    }
}
