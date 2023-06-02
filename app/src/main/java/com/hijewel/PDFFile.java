package com.hijewel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hijewel.adapters.PDFAdapterBullion;
import com.hijewel.adapters.UpdateAdapterBullion;
import com.hijewel.models.PDFModel;
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

public class PDFFile extends AppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.rvNotification)
    RecyclerView rvNotification;


    @BindView(R.id.tvNotificationNotAvailableMessage)
    TextView tvNotificationNotAvailableMessage;

    private ArrayList<PDFModel> updateDataList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdffile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("PDF Quick View");

        unbinder = ButterKnife.bind(this);

        setAdapter();

        getUpdatesDetails2222();



    }




    private void setAdapter() {


        updateDataList = new ArrayList<PDFModel>();
        RecyclerView.LayoutManager homelayoutmanager = new GridLayoutManager(this, 1);
        rvNotification.setLayoutManager(homelayoutmanager);
        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.setAdapter(new PDFAdapterBullion(PDFFile.this, updateDataList));


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
                SoapObject request = new SoapObject("http://tempuri.org/", "Get_Pdffile_all");
                try {

                    bankDetailsResponse = loadServiceForBullion(request, "Get_Pdffile_all");
                } catch (Exception e) {

                    e.printStackTrace();
                }

                return bankDetailsResponse;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {

                    updateDataList.clear();
                    updateDataList.addAll((Collection<? extends PDFModel>) new Gson().fromJson(response, new TypeToken<ArrayList<PDFModel>>() {
                    }.getType()));

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
}
