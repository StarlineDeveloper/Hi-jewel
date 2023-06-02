package com.hijewel.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hijewel.ProductDetails;
import com.hijewel.R;
import com.hijewel.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.hijewel.utils.Constatnts.PRODUCT_IMAGE;
import static com.hijewel.utils.Constatnts.URL;

import androidx.cardview.widget.CardView;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class ProductAdapterHome extends BaseAdapter {

    private Context context;
    private ArrayList<ProductModel> data;
    private LayoutInflater inflater;

    public ProductAdapterHome(Context context, ArrayList<ProductModel> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ProductModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {
        Holder vh;
        if (itemView == null) {
            vh = new Holder();
            itemView = inflater.inflate(R.layout.product_item, null);
            vh.main = itemView.findViewById(R.id.main);
            vh.poster = itemView.findViewById(R.id.poster);
            vh.title = itemView.findViewById(R.id.title);
            vh.type = itemView.findViewById(R.id.type);
            itemView.setTag(vh);
        } else {
            vh = (Holder) itemView.getTag();
        }
        String imagePath = PRODUCT_IMAGE + getItem(position).getProduct_id() + "/thumb/" + getItem(position).getProduct_image().split(",")[0];
        Picasso.with(context)
                .load(imagePath)
//                .resize(120, 120)
                .into(vh.poster);
        vh.title.setText(getItem(position).getProduct_title());
        vh.type.setText(getItem(position).getSource());
        vh.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetails.class);
                i.putExtra("data", getItem(position));
                context.startActivity(i);
                ((Activity) context).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        return itemView;
    }

    private class Holder {
        ImageView poster;
        CardView main;
        TextView title, type;
    }
}
