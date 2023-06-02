package com.hijewel;

import android.content.Context;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hijewel.models.ContactModel;
import com.hijewel.utils.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.hijewel.utils.Constatnts.CONTACT_US;
import static com.hijewel.utils.Functions.call;
import static com.hijewel.utils.Functions.checkForEmpty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {

    Context context;
    PreferenceUtils utils;

    CardView contact1, contact2;
    TextView company1, company2, company3, person1, number11, number12, email1, address1;
    TextView company4, company5, person2, number21, number22, email2, address2;
    ImageView border1, border2, border3;
    ImageView border4, border5;
    LinearLayout person1_layout, number1_layout, email1_layout, address1_layout;
    LinearLayout person2_layout, number2_layout, email2_layout, address2_layout;
    View view11, view12, number1_view, view13;
    View view21, view22, number2_view, view23;

    ContactModel cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        context = ContactUs.this;
        utils = new PreferenceUtils(context);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        initViews();

        try {
            cm = new ContactModel(new JSONObject(utils.getPrefrence(CONTACT_US, "")));
            checkValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        contact1 = findViewById(R.id.contact1);
        company1 = findViewById(R.id.company1);
        border1 = findViewById(R.id.border1);
        company2 = findViewById(R.id.company2);
        border2 = findViewById(R.id.border2);
        company3 = findViewById(R.id.company3);
        border3 = findViewById(R.id.border3);
        person1_layout = findViewById(R.id.person1_layout);
        person1 = findViewById(R.id.person1);
        view11 = findViewById(R.id.view11);
        number1_layout = findViewById(R.id.number1_layout);
        number11 = findViewById(R.id.number11);
        number11.setOnClickListener(this);
        number12 = findViewById(R.id.number12);
        number12.setOnClickListener(this);
        number1_view = findViewById(R.id.number1_view);
        view12 = findViewById(R.id.view12);
        email1_layout = findViewById(R.id.email1_layout);
        email1 = findViewById(R.id.email1);
        view13 = findViewById(R.id.view13);
        address1_layout = findViewById(R.id.address1_layout);
        address1 = findViewById(R.id.address1);

        contact2 = findViewById(R.id.contact2);
        company4 = findViewById(R.id.company4);
        border4 = findViewById(R.id.border4);
        company5 = findViewById(R.id.company5);
        border5 = findViewById(R.id.border5);
        person2_layout = findViewById(R.id.person2_layout);
        person2 = findViewById(R.id.person2);
        view21 = findViewById(R.id.view21);
        number2_layout = findViewById(R.id.number2_layout);
        number21 = findViewById(R.id.number21);
        number21.setOnClickListener(this);
        number22 = findViewById(R.id.number22);
        number22.setOnClickListener(this);
        number2_view = findViewById(R.id.number2_view);
        view22 = findViewById(R.id.view22);
        email2_layout = findViewById(R.id.email2_layout);
        email2 = findViewById(R.id.email2);
        view23 = findViewById(R.id.view23);
        address2_layout = findViewById(R.id.address2_layout);
        address2 = findViewById(R.id.address2);
    }

    private void checkValues() {
        if (checkForEmpty(cm.getCompany1()) && checkForEmpty(cm.getCompany2()) && checkForEmpty(cm.getCompany3())) {
            contact1.setVisibility(View.GONE);
        } else {
            if (checkForEmpty(cm.getCompany1())) {
                company1.setVisibility(View.GONE);
                border1.setVisibility(View.GONE);
            } else {
                company1.setVisibility(View.VISIBLE);
                border1.setVisibility(View.VISIBLE);
                company1.setText(cm.getCompany1());
            }
            if (checkForEmpty(cm.getCompany2())) {
                company2.setVisibility(View.GONE);
                border2.setVisibility(View.GONE);
            } else {
                company2.setVisibility(View.VISIBLE);
                border2.setVisibility(View.VISIBLE);
                company2.setText(cm.getCompany2());
            }
            if (checkForEmpty(cm.getCompany3())) {
                company3.setVisibility(View.GONE);
                border3.setVisibility(View.GONE);
            } else {
                company3.setVisibility(View.VISIBLE);
                border3.setVisibility(View.VISIBLE);
                company3.setText(cm.getCompany3());
            }
        }
        if (checkForEmpty(cm.getPerson1())) {
            person1_layout.setVisibility(View.GONE);
            view11.setVisibility(View.GONE);
        } else {
            person1_layout.setVisibility(View.VISIBLE);
            view11.setVisibility(View.VISIBLE);
            person1.setText(cm.getPerson1());
        }

        if (checkForEmpty(cm.getContact11()) && checkForEmpty(cm.getContact12())) {
            number1_layout.setVisibility(View.GONE);
            view12.setVisibility(View.GONE);
        } else {
            number1_layout.setVisibility(View.VISIBLE);
            view12.setVisibility(View.VISIBLE);
            if (checkForEmpty(cm.getContact11())) {
                number11.setVisibility(View.GONE);
                number1_view.setVisibility(View.GONE);
            } else {
                number11.setVisibility(View.VISIBLE);
                number11.setText(cm.getContact11());
            }
            if (checkForEmpty(cm.getContact12())) {
                number12.setVisibility(View.GONE);
                number1_view.setVisibility(View.GONE);
            } else {
                number12.setVisibility(View.VISIBLE);
                number12.setText(cm.getContact12());
            }
        }
        if (checkForEmpty(cm.getEmail1())) {
            email1_layout.setVisibility(View.GONE);
            view13.setVisibility(View.GONE);
        } else {
            email1_layout.setVisibility(View.VISIBLE);
            view13.setVisibility(View.VISIBLE);
            email1.setText(cm.getEmail1());
        }
        if (checkForEmpty(cm.getAddress1())) {
            address1_layout.setVisibility(View.GONE);
        } else {
            address1_layout.setVisibility(View.VISIBLE);
            address1.setText(cm.getAddress1());
        }

        if (checkForEmpty(cm.getCompany4()) && checkForEmpty(cm.getCompany5())) {
            contact2.setVisibility(View.GONE);
        } else {
            if (checkForEmpty(cm.getCompany4())) {
                company4.setVisibility(View.GONE);
                border4.setVisibility(View.GONE);
            } else {
                company4.setVisibility(View.VISIBLE);
                border4.setVisibility(View.VISIBLE);
                company4.setText(cm.getCompany4());
            }
            if (checkForEmpty(cm.getCompany5())) {
                company5.setVisibility(View.GONE);
                border5.setVisibility(View.GONE);
            } else {
                company5.setVisibility(View.VISIBLE);
                border5.setVisibility(View.VISIBLE);
                company5.setText(cm.getCompany5());
            }
        }
        if (checkForEmpty(cm.getPerson2())) {
            person2_layout.setVisibility(View.GONE);
            view21.setVisibility(View.GONE);
        } else {
            person2_layout.setVisibility(View.VISIBLE);
            view21.setVisibility(View.VISIBLE);
            person2.setText(cm.getPerson2());
        }

        if (checkForEmpty(cm.getContact21()) && checkForEmpty(cm.getContact22())) {
            number2_layout.setVisibility(View.GONE);
            view22.setVisibility(View.GONE);
        } else {
            number2_layout.setVisibility(View.VISIBLE);
            view22.setVisibility(View.VISIBLE);
            if (checkForEmpty(cm.getContact21())) {
                number21.setVisibility(View.GONE);
                number2_view.setVisibility(View.GONE);
            } else {
                number21.setVisibility(View.VISIBLE);
                number21.setText(cm.getContact21());
            }
            if (checkForEmpty(cm.getContact22())) {
                number22.setVisibility(View.GONE);
                number2_view.setVisibility(View.GONE);
            } else {
                number22.setVisibility(View.VISIBLE);
                number22.setText(cm.getContact22());
            }
        }
        if (checkForEmpty(cm.getEmail2())) {
            email2_layout.setVisibility(View.GONE);
            view23.setVisibility(View.GONE);
        } else {
            email2_layout.setVisibility(View.VISIBLE);
            view23.setVisibility(View.VISIBLE);
            email2.setText(cm.getEmail2());
        }
        if (checkForEmpty(cm.getAddress2())) {
            address2_layout.setVisibility(View.GONE);
        } else {
            address2_layout.setVisibility(View.VISIBLE);
            address2.setText(cm.getAddress2());
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.number11:
                call(context, number11.getText().toString().trim());
                break;
            case R.id.number12:
                call(context, number12.getText().toString().trim());
                break;
            case R.id.number21:
                call(context, number21.getText().toString().trim());
                break;
            case R.id.number22:
                call(context, number22.getText().toString().trim());
                break;
            default:
                break;
        }
    }
}
