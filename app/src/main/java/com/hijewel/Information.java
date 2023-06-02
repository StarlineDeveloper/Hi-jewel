package com.hijewel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hijewel.adapters.UpdateAdapterBullion;
import com.hijewel.models.UpdatesModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Information extends AppCompatActivity {



    Unbinder unbinder;

    @BindView(R.id.rvNotification)
    RecyclerView rvNotification;

    AlertDialog alertDialog;



    @BindView(R.id.tvNotificationNotAvailableMessage)
    TextView tvNotificationNotAvailableMessage;

    private Context context;
    private ArrayList<UpdatesModel> updateDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Information");

        unbinder = ButterKnife.bind(this);

        setAdapter();

       //getUpdatesDetails();
        getUpdatesDetails2222();


    }


    private void setAdapter() {
        updateDataList = new ArrayList<UpdatesModel>();

        RecyclerView.LayoutManager homelayoutmanager = new GridLayoutManager(this, 1);
        rvNotification.setLayoutManager(homelayoutmanager);

        //rvNotification.setLayoutManager(new LinearLayoutManager(Information.this, LinearLayoutManager.VERTICAL, false,1));
        rvNotification.setAdapter(new UpdateAdapterBullion(Information.this, updateDataList));


    }


    @SuppressLint("StaticFieldLeak")
    private void getUpdatesDetails() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {

                String bankDetailsResponse = "";
                SoapObject request = new SoapObject("http://tempuri.org/", "UpdateDetail");
                try {
                    bankDetailsResponse = loadServiceForBullion2(request, "UpdateDetail");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return bankDetailsResponse;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {

                    Log.e("resold",response);
                    String[] bankDetailsRespones = response.split("~");
                    if (bankDetailsRespones[0].equalsIgnoreCase("true") && bankDetailsRespones.length > 1) {
                        tvNotificationNotAvailableMessage.setVisibility(View.GONE);
                        rvNotification.setVisibility(View.VISIBLE);

                        updateDataList.clear();
                        updateDataList.addAll((Collection<? extends UpdatesModel>) new Gson().fromJson(bankDetailsRespones[1], new TypeToken<ArrayList<UpdatesModel>>() {
                        }.getType()));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(rvNotification.getAdapter()).notifyDataSetChanged();
                        }

                    } else {
                        tvNotificationNotAvailableMessage.setText(bankDetailsRespones[1]);
                        tvNotificationNotAvailableMessage.setVisibility(View.VISIBLE);
                        rvNotification.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }



    @SuppressLint("StaticFieldLeak")
    private void getUpdatesDetails2222() {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {

                String bankDetailsResponse = "";
                SoapObject request = new SoapObject("http://tempuri.org/", "UpdateList");
                try {

                    bankDetailsResponse = loadServiceForBullion(request, "UpdateList");
                } catch (Exception e) {

                    e.printStackTrace();
                }

                return bankDetailsResponse;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {


                    Log.e("reponce",response);

                    JSONObject student1 = new JSONObject(response);
                    String Array11=student1.getString("Table");
                    Log.e("Array11",Array11);



                    updateDataList.clear();
                    updateDataList.addAll((Collection<? extends UpdatesModel>) new Gson().fromJson(Array11, new TypeToken<ArrayList<UpdatesModel>>() {
                    }.getType()));


                    String Table1=student1.getString("Table1");
                    JSONArray jsonArray = new JSONArray(Table1);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String Title = jsonObject1.optString("Title");
                        String Description = jsonObject1.optString("Description");

                        //DialogPopup(Title,Description);

                        //DialogSystem(Title,Description);

                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(rvNotification.getAdapter()).notifyDataSetChanged();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    public static String loadServiceForBullion(SoapObject request, String method) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        HttpTransportSE transport = new HttpTransportSE("http://www.hijewel.in/" + "webservice/WebServiceHiranya.asmx");
        transport.call("http://tempuri.org/" + method, envelope);
        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
        return response.toString();
    }

    public static String loadServiceForBullion2(SoapObject request, String method) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        HttpTransportSE transport = new HttpTransportSE("http://aartigold.in/" + "webservice/WebService.asmx");
        transport.call("http://tempuri.org/" + method, envelope);
        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
        return response.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    public void DialogSystem(String titel,String des){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titel);
        alertDialogBuilder.setMessage(des);
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void DialogPopup(String titel,String des) {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.item_pop, (ViewGroup)null);

        TextView tv_title=confirmDialog.findViewById(R.id.tv_title);
        final TextView desc=confirmDialog.findViewById(R.id.desc);

        ImageView tv_close=confirmDialog.findViewById(R.id.tv_close);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tv_title.setText(titel);
        desc.setText(des);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

}
