package com.hijewel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hijewel.models.CartFooterModel;
import com.hijewel.models.CartModel;
import com.hijewel.utils.UserUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import static com.hijewel.database.MasterDatabase.deleteItem;
import static com.hijewel.database.MasterDatabase.getAllProducts;
import static com.hijewel.database.MasterDatabase.getCartCount;
import static com.hijewel.database.MasterDatabase.getFooterData;
import static com.hijewel.database.MasterDatabase.updateCartData;
import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Constatnts.PRODUCT_IMAGE;
import static com.hijewel.utils.Constatnts.USER_ID;
import static com.hijewel.utils.Constatnts.USER_NAME;
import static com.hijewel.utils.Functions.isOnline;
import static com.hijewel.utils.Functions.loadService;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyCart extends AppCompatActivity {

    Context context;
    UserUtils userUtils;

    FrameLayout checkout;
    ImageView no_prod;
    RecyclerView cart_list;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cart);

        context = MyCart.this;
        userUtils = new UserUtils(context);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Cart");

        no_prod = findViewById(R.id.no_prod);
        cart_list = findViewById(R.id.cart_list);
        checkout = findViewById(R.id.checkout);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        cart_list.setLayoutManager(lm);
        adapter = new CartAdapter(getAllProducts(), getFooterData());
        cart_list.setAdapter(adapter);
        updateUI();
    }

    public void updateUI() {
        if (getCartCount() > 0) {
            adapter.reFill(getAllProducts(), getFooterData());
            cart_list.setVisibility(View.VISIBLE);
            checkout.setVisibility(View.VISIBLE);
            no_prod.setVisibility(View.GONE);
        } else {
            cart_list.setVisibility(View.GONE);
            checkout.setVisibility(View.GONE);
            no_prod.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stable, R.anim.push_down_in);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private String getOrderProducts() {
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < adapter.getItemCount() - 1; i++) {
                JSONObject j = new JSONObject();
                j.put("product_id", adapter.getItem(i).getProduct_id());
                j.put("product_title", adapter.getItem(i).getProduct_title());
                j.put("ProductCode", adapter.getItem(i).getProductCode());
                j.put("product_image", adapter.getItem(i).getProduct_image().split(",")[0]);
                j.put("MrpPrice", adapter.getItem(i).getMrpPrice());
                j.put("OurPrice", adapter.getItem(i).getOurPrice());
                j.put("Weight", adapter.getItem(i).getWeight());
                j.put("Source", adapter.getItem(i).getSource());
                j.put("SubSource", adapter.getItem(i).getSubSource());
                j.put("carat", adapter.getItem(i).getCarat());
                j.put("total_price", adapter.getItem(i).getTotal_price());
                j.put("total_weight", adapter.getItem(i).getTotal_weight());
                j.put("total_qty", adapter.getItem(i).getQty());
                jsonArray.put(j);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    private String getOrder() {
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject j = new JSONObject();
            j.put("user_name", userUtils.getUserData(USER_NAME, ""));
            j.put("Jewl_total_weight", adapter.getFoot().getJewl_total_weight());
            j.put("Jewl_total_qty", adapter.getFoot().getJewl_total_qty());
            j.put("Jewl_total_price", adapter.getFoot().getJewl_total_price());
            j.put("Utsav_total_weight", adapter.getFoot().getUtsav_total_weight());
            j.put("Utsav_total_qty", adapter.getFoot().getUtsav_total_qty());
            j.put("Utsav_total_price", adapter.getFoot().getUtsav_total_price());
            j.put("Plat_total_weight", adapter.getFoot().getPlat_total_weight());
            j.put("Plat_total_qty", adapter.getFoot().getPlat_total_qty());
            j.put("Plat_total_price", adapter.getFoot().getPlat_total_price());
            jsonArray.put(j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public void checkout(View view) {
        new Checkout().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class Checkout extends AsyncTask<String, String, String> {

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
                SoapObject request = new SoapObject(LINK, "Proccedtocheckout");
                request.addProperty("Userrid", userUtils.getUserData(USER_ID, ""));
                request.addProperty("strorderproduct", getOrderProducts());
                request.addProperty("strorder", getOrder());
                Log.e("check_req", request.toString());
                try {
                    regresponse = loadService(API, request, "Proccedtocheckout");
                    Log.e("check_response", regresponse);
                } catch (Exception e) {
                    error = true;
                    regresponse = "something went wrong..";
                    Log.e("check_error", e.toString());
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
                Intent i = new Intent(context, OrderConfirm.class);
                i.putExtra("response", regresponse);
                startActivity(i);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(getApplicationContext(), regresponse, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private int ITEM = 0, FOOTER = 1;
        private ArrayList<CartModel> data;
        private CartFooterModel cfm;

        CartAdapter(ArrayList<CartModel> data, CartFooterModel cfm) {
            this.data = data;
            this.cfm = cfm;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM) {
                View view = LayoutInflater.from(context).inflate(R.layout.cart_item, null);
                return new Holder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.cart_footer_new, null);
                return new Footer(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == ITEM) {
                Holder vh = (Holder) holder;
                String imagePath = PRODUCT_IMAGE + getItem(position).getProduct_id() + "/thumb/" + getItem(position).getProduct_image().split(",")[0];
                Picasso.with(context)
                        .load(imagePath)
                        .into(vh.poster);
                vh.title.setText(getItem(position).getProduct_title());
                if (getItem(position).getSource().toLowerCase().equals("jewellery")) {
                    vh.price.setText("Price : --");
                } else {
                    vh.price.setText("Price : " + getItem(position).getOurPrice());
                }
                vh.weight.setText("Weight : " + getItem(position).getWeight() + " " + getItem(position).getSubSource());
                if (getItem(position).getCarat().isEmpty()) {
                    vh.carat.setText("Carat : --");
                } else {
                    vh.carat.setText("Carat : " + getItem(position).getCarat());
                }
                vh.total_weight.setText(getItem(position).getTotal_weight());
                vh.total_price.setText(getItem(position).getTotal_price());
                vh.qty.setText(String.valueOf(getItem(position).getQty()));
            } else {
                Footer ft = (Footer) holder;
                ft.jewl_total_weight.setText(cfm.getJewl_total_weight());
                ft.jewl_total_qty.setText(cfm.getJewl_total_qty());
                ft.jewl_total_price.setText("--");
                ft.utsav_total_weight.setText(cfm.getUtsav_total_weight());
                ft.utsav_total_qty.setText(cfm.getUtsav_total_qty());
                ft.utsav_total_price.setText(cfm.getUtsav_total_price());
                ft.plat_total_weight.setText(cfm.getPlat_total_weight());
                ft.plat_total_qty.setText(cfm.getPlat_total_qty());
                ft.plat_total_price.setText(cfm.getPlat_total_price());
            }
        }

        CartModel getItem(int pos) {
            return data.get(pos);
        }

        CartFooterModel getFoot() {
            return cfm;
        }

        void reFill(ArrayList<CartModel> data, CartFooterModel cfm) {
            this.data.clear();
            this.data.addAll(data);
            this.cfm = cfm;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return data.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return FOOTER;
            }
            return ITEM;
        }

        private class Holder extends RecyclerView.ViewHolder {

            ImageView poster;
            TextView title, price, weight, carat, total_weight, total_price,
                    qty, remove;
            ImageView minus, plus;

            Holder(View itemView) {
                super(itemView);
                poster = itemView.findViewById(R.id.poster);
                title = itemView.findViewById(R.id.title);
                price = itemView.findViewById(R.id.price);
                weight = itemView.findViewById(R.id.weight);
                carat = itemView.findViewById(R.id.carat);
                total_weight = itemView.findViewById(R.id.total_weight);
                total_price = itemView.findViewById(R.id.total_price);
                remove = itemView.findViewById(R.id.remove);
                qty = itemView.findViewById(R.id.qty);
                minus = itemView.findViewById(R.id.minus);
                plus = itemView.findViewById(R.id.plus);

                minus.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        LinearLayout ll = (LinearLayout) v.getParent();
                        TextView tv = (TextView) ll.getChildAt(1);
                        int qty = Integer.parseInt(tv.getText().toString());
                        if (qty != 1) {
                            qty = qty - 1;
                            updateList(getAdapterPosition(), qty);
                            tv.setText(String.valueOf(qty));
                        }
                    }
                });

                plus.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        LinearLayout ll = (LinearLayout) v.getParent();
                        TextView tv = (TextView) ll.getChildAt(1);
                        int qty = Integer.parseInt(tv.getText().toString());
                        qty += 1;
                        updateList(getAdapterPosition(), qty);
                        tv.setText(String.valueOf(qty));
                    }
                });

                remove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final int pos = getAdapterPosition();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                MyCart.this);
                        builder1.setMessage("Do you want to remove this product from your Cart..?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        deleteItem(getItem(pos).getProduct_id());
                                        updateUI();
                                        dialog.dismiss();
                                    }
                                });

                        builder1.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                });
            }

            private void updateList(int pos, int qty) {
                CartModel cm = getItem(pos);
                try {
                    float t_w = Float.parseFloat(cm.getWeight()) * qty;
                    cm.setTotal_weight(String.valueOf(t_w));
                    float t_p = Float.parseFloat(cm.getOurPrice()) * qty;
                    cm.setTotal_price(String.valueOf(t_p));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cm.setQty(qty);
                updateCartData(cm);
                updateUI();
            }
        }

        private class Footer extends RecyclerView.ViewHolder {

            TextView jewl_total_weight, utsav_total_weight, plat_total_weight,
                    jewl_total_qty, utsav_total_qty, plat_total_qty,
                    jewl_total_price, utsav_total_price, plat_total_price;

            Footer(View itemView) {
                super(itemView);
                jewl_total_weight = itemView.findViewById(R.id.jewl_total_weight);
                utsav_total_weight = itemView.findViewById(R.id.utsav_total_weight);
                plat_total_weight = itemView.findViewById(R.id.plat_total_weight);
                jewl_total_qty = itemView.findViewById(R.id.jewl_total_qty);
                utsav_total_qty = itemView.findViewById(R.id.utsav_total_qty);
                plat_total_qty = itemView.findViewById(R.id.plat_total_qty);
                jewl_total_price = itemView.findViewById(R.id.jewl_total_price);
                utsav_total_price = itemView.findViewById(R.id.utsav_total_price);
                plat_total_price = itemView.findViewById(R.id.plat_total_price);
            }
        }
    }
}
