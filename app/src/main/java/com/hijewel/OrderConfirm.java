package com.hijewel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.hijewel.database.MasterDatabase.clearCart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class OrderConfirm extends AppCompatActivity {

    Context context;

    ImageView image_response;
    TextView text_response;

    String response;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirm);

        context = OrderConfirm.this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        response = getIntent().getStringExtra("response");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Order Confirmation");

        image_response = findViewById(R.id.image_response);
        text_response = findViewById(R.id.text_response);

        if (response.toLowerCase().contains("ok")) {
            clearCart();
            image_response.setImageResource(R.drawable.success);
        } else {
            image_response.setImageResource(R.drawable.cancel);
        }
        text_response.setText(response.split("~")[1]);
    }

    public void ok(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (response.toLowerCase().contains("ok")) {
            Intent home = new Intent(context, Home.class);
            startActivity(home);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
