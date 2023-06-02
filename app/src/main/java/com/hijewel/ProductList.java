package com.hijewel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.design.widget.BottomSheetDialogFragment;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hijewel.fragments.FilterFragment;
import com.hijewel.models.CatsModel;
import com.hijewel.models.ProductModel;
import com.hijewel.models.SymbolModel;
import com.hijewel.ui.GridSpacingItemDecoration;
import com.hijewel.ui.HidingScrollListener;
import com.hijewel.utils.Functions;
import com.hijewel.utils.GetData;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import static com.hijewel.utils.Constatnts.API;
import static com.hijewel.utils.Constatnts.KEY_STATUS_DISPLAY;
import static com.hijewel.utils.Constatnts.LINK;
import static com.hijewel.utils.Constatnts.LIVE_RATES;
import static com.hijewel.utils.Constatnts.PRODUCT_IMAGE;
import static com.hijewel.utils.Constatnts.PRODUCT_SEARCH;
import static com.hijewel.utils.Functions.changeColor;
import static com.hijewel.utils.Functions.isOnline;
import static com.hijewel.utils.Functions.loadService;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class ProductList extends MainActivity implements FilterFragment.OnApplyListener {

    Context context;
    GetData gd;
    Intent intent;

    RecyclerView categories, products;
    RotateLoading progress;
    FloatingActionButton gender, search;
    TextView no_prod;
    RelativeLayout cats_lay;

    ArrayList<CatsModel> catlist;
    ArrayList<ProductModel> productslist;
    ProductsAdaspter adapter;
    GridLayoutManager gm;
    categories catadp;

    SearchView search_et;
    String searched_text;

    boolean spanFlag = true, loading = true;
    boolean M = false, F = false;
    int page = 1;
    String MALE_FEMALE = "null";
    String subCatId;

    private BroadcastReceiver rates = new BroadcastReceiver() {
        public void onReceive(final Context context, final Intent intent) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    loadData(intent.getStringExtra("data"));
                }
            });
        }
    };

    private void loadData(String data) {
        SymbolModel sm = gd.getLiveRates(utils, data);
        if (utils.getPrefrence(KEY_STATUS_DISPLAY, "").equals("true")) {
            if (sm != null) {
                rate_layout.setVisibility(View.VISIBLE);
                product.setText(sm.getSymbol());
                sell.setText(sm.getAsk());
                changeColor(context, 1, sm, sell);
            } else {
                rate_layout.setVisibility(View.GONE);
            }
        } else {
            rate_layout.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.product_list, frameLayout);

        context = ProductList.this;
        gd = new GetData(context);
        intent = getIntent();

        this.registerReceiver(this.rates, new IntentFilter("getRates"));

        catlist = new ArrayList<>();
        productslist = new ArrayList<>();

        cats_lay = findViewById(R.id.cats_lay);
        progress = findViewById(R.id.progress);
        no_prod = findViewById(R.id.no_prod);
        gender = findViewById(R.id.gender);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.hide();
                gender.hide();
                final Dialog settingsDialog = new Dialog(context);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(LayoutInflater.from(context)
                        .inflate(R.layout.search_layout
                                , null));

                ImageView backarrow = settingsDialog.findViewById(R.id.iv_back);
//                ImageView iv_cancel = settingsDialog.findViewById(R.id.iv_cancel);
                TextView tv_search = settingsDialog.findViewById(R.id.tv_cancel);

                search_et = settingsDialog.findViewById(R.id.et_search);
//                iv_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        search_et.getText().clear();
//                    }
//                });
                backarrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        settingsDialog.dismiss();
                        search.show();
                        gender.show();
                    }
                });

                search_et.setActivated(true);
                search_et.setQueryHint("Product name,,,SKU");
                search_et.onActionViewExpanded();
                search_et.setIconified(false);
                search_et.clearFocus();
                search_et.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searched_text = search_et.getQuery().toString();
                        new GetSearchdata().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        settingsDialog.dismiss();
                        search.show();
                        categories.setVisibility(View.GONE);
                        gender.hide();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

                tv_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        settingsDialog.dismiss();
                        search.show();
                        gender.show();
                    }
                });

                settingsDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                settingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                settingsDialog.setCanceledOnTouchOutside(true);
                settingsDialog.show();
            }
        });
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cd = new Bundle();
                cd.putBoolean("male", M);
                cd.putBoolean("female", F);
                BottomSheetDialogFragment bottomSheetDialogFragment = new FilterFragment();
                bottomSheetDialogFragment.setArguments(cd);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        findViewById(R.id.toolbar_shadow).setVisibility(View.GONE);

        categories = findViewById(R.id.categories);
        LinearLayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        categories.setLayoutManager(lm);
        categories.smoothScrollToPosition(intent.getIntExtra("pos", 0));

        products = findViewById(R.id.products);
        gm = new GridLayoutManager(context, 2);
        products.setLayoutManager(gm);
        products.addItemDecoration(new GridSpacingItemDecoration(2, 5, true));
        adapter = new ProductsAdaspter(productslist, true);
        products.setAdapter(adapter);

        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.hasFooter() && position == adapter.getItemCount() - 1) {
                    return 2;
                } else {
                    if (spanFlag) {
                        return 1;
                    } else {
                        return 2;
                    }
                }
            }
        });

        final RelativeLayout cats_lay = findViewById(R.id.cats_lay);
        products.addOnScrollListener(new HidingScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int th = 4;
                int count = gm.getItemCount();

                if (gm.findLastCompletelyVisibleItemPosition() >= count
                        - th) {
                    if (!loading) {
                        loading = true;
                        adapter.updateFooter(true);
                        if (isOnline(context)) {
                            page++;
                            if (catadp.array.get(0)) {
                                new GetMainCatProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                new GetSubCatProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, subCatId);
                            }
                        } else {
                            loading = false;
                            adapter.updateFooter(false);
                        }
                    }
                }
            }

            @Override
            public void onHide() {
                gender.hide();
                cats_lay.animate().translationY(-categories.getHeight());
            }

            @Override
            public void onShow() {
                gender.show();
                cats_lay.animate().translationY(0);
            }
        });

        if (isOnline(context)) {
            new GetMainCatProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            no_prod.setVisibility(View.VISIBLE);
            adapter.removeFooter();
            no_prod.setText("please check your internet connection");
            products.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "please check your internet connection......", Toast.LENGTH_SHORT).show();
        }

        loadData(utils.getPrefrence(LIVE_RATES, ""));
    }

    @Override
    public void onApply(boolean male, boolean female) {
        M = male;
        F = female;
        if (M && F) {
            MALE_FEMALE = "null";
        } else if (M) {
            MALE_FEMALE = "male";
        } else if (F) {
            MALE_FEMALE = "female";
        } else {
            MALE_FEMALE = "null";
        }
        loading = true;
        page = 1;
        productslist.clear();
        if (catadp.array.get(0)) {
            new GetMainCatProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new GetSubCatProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, subCatId);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLayout)) {
            mDrawerLayout.closeDrawer(mLayout);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rates != null) {
            this.unregisterReceiver(this.rates);
        }
    }

    private class GetMainCatProducts extends AsyncTask<String, String, String> {

        String response;
        int current_count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (page == 1) {
                progress.start();
                products.setVisibility(View.GONE);
                no_prod.setVisibility(View.GONE);
                productslist.clear();
            }
        }

        protected String doInBackground(String... params) {
            if (isOnline(context)) {
                SoapObject request = new SoapObject(LINK, "GetCategory_by_url");
                request.addProperty("CategoryURL", getIntent().getStringExtra("CategoryURL"));
                request.addProperty("MenuURL", getIntent().getStringExtra("MenuURL"));
                request.addProperty("gender", MALE_FEMALE);
                request.addProperty("RowsPerPage", "20");
                request.addProperty("PageNumber", page);
                Log.e("Pro_req", request.toString());
                try {
                    response = loadService(API, request, "GetCategory_by_url");
                    current_count = gd.getProducts(response, "Table1").size();
                    productslist.addAll(gd.getProducts(response, "Table1"));
                    Log.e("Pro_res", response);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                    e.printStackTrace();
                }
            }
            return "1";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.stop();
            if (page == 1) {
                CatsModel cm = new CatsModel("ALL");
                catlist.add(cm);
                catlist.addAll(gd.getSubCats(response));
                catadp = new categories();
                categories.setAdapter(catadp);
                if (catadp.getItemCount() > 1) {
                    cats_lay.setVisibility(View.VISIBLE);
                } else {
                    cats_lay.setVisibility(View.GONE);
                }
            }

            if (productslist.size() > 0) {
                if (current_count >= 10) {
                    loading = false;
                    adapter.addFooter();
                } else {
                    loading = true;
                    adapter.removeFooter();
                }
                adapter.notifyDataSetChanged();
                products.setVisibility(View.VISIBLE);
                gender.setVisibility(View.VISIBLE);
                no_prod.setVisibility(View.GONE);
            } else {
                no_prod.setVisibility(View.VISIBLE);
                products.setVisibility(View.GONE);
                gender.setVisibility(View.GONE);
            }
        }
    }

    private class GetSubCatProducts extends AsyncTask<String, String, String> {

        String response;
        int current_count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (page == 1) {
                progress.start();
                products.setVisibility(View.GONE);
                no_prod.setVisibility(View.GONE);
                productslist.clear();
            }
        }

        protected String doInBackground(String... params) {
            if (isOnline(context)) {
                SoapObject request = new SoapObject(LINK, "SubCategoryById");
                request.addProperty("SubCategoryID", params[0]);
                request.addProperty("gender", MALE_FEMALE);
                request.addProperty("NoOfRecord", "20");
                request.addProperty("PageNo", page);
                Log.e("Pro_req", request.toString());
                try {
                    response = loadService(API, request, "SubCategoryById");
                    current_count = gd.getProducts(response, "Table").size();
                    productslist.addAll(gd.getProducts(response, "Table"));
                    Log.e("Pro_res", response);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                    e.printStackTrace();
                }
            }
            return "1";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.stop();
            if (productslist.size() > 0) {
                if (current_count >= 10) {
                    loading = false;
                    adapter.addFooter();
                } else {
                    loading = true;
                    adapter.removeFooter();
                }
                adapter.notifyDataSetChanged();
                products.setVisibility(View.VISIBLE);
                no_prod.setVisibility(View.GONE);
            } else {
                no_prod.setVisibility(View.VISIBLE);
                products.setVisibility(View.GONE);
            }
        }

    }

    private class GetSearchdata extends AsyncTask<String, String, String> {

        String response;
        int current_count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (page == 1) {
                progress.start();
                products.setVisibility(View.GONE);
                no_prod.setVisibility(View.GONE);
                productslist.clear();
            }
        }

        protected String doInBackground(String... params) {
            if (isOnline(context)) {
                SoapObject request = new SoapObject(LINK, PRODUCT_SEARCH);
                request.addProperty("Product", searched_text);
                Log.e("Pro_req", request.toString());
                try {
                    response = loadService(API, request, PRODUCT_SEARCH);
                    current_count = gd.getProducts(response, "Table").size();
                    productslist.addAll(gd.getProducts(response, "Table"));
                    Log.e("Pro_res", response);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                    e.printStackTrace();
                }
            }
            return searched_text;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.stop();
            if (productslist.size() > 0) {
                if (current_count >= 10) {
                    loading = false;
                    adapter.addFooter();
                } else {
                    loading = true;
                    adapter.removeFooter();
                }
                adapter.notifyDataSetChanged();
                products.setVisibility(View.VISIBLE);
                no_prod.setVisibility(View.GONE);
            } else {
                no_prod.setVisibility(View.VISIBLE);
                products.setVisibility(View.GONE);
            }
        }

    }

    private class categories extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        SparseBooleanArray array;

        private categories() {
            array = new SparseBooleanArray(catlist.size());
            array.put(intent.getIntExtra("pos", 0), true);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.category, null);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Holder holder1 = (Holder) holder;
            if (position == 0) {
                holder1.cat.setText(catlist.get(position).getCategoryDisplayName());
            } else {
                holder1.cat.setText(catlist.get(position).getCategoryDisplayName() + " (" + catlist.get(position).getProductCount() + ")");
            }
            if (array.get(position)) {
                holder1.main_cat.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                holder1.cat.setTextColor(Color.WHITE);
            } else {
                holder1.main_cat.setCardBackgroundColor(Color.WHITE);
                holder1.cat.setTextColor(Color.parseColor("#111111"));
            }
        }

        @Override
        public int getItemCount() {
            return catlist.size();
        }

        private class Holder extends RecyclerView.ViewHolder {

            CardView main_cat;
            TextView cat;

            private Holder(View itemView) {
                super(itemView);
                main_cat = itemView.findViewById(R.id.main_cat);
                cat = itemView.findViewById(R.id.cat);

                main_cat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isOnline(context)) {
                            int pos = getAdapterPosition();
                            if (!array.get(pos)) {
                                array.clear();
                                array.put(pos, true);
                                notifyDataSetChanged();
                                page = 1;
                                MALE_FEMALE = "null";
                                loading = true;
                                if (pos == 0) {
                                    new GetMainCatProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    subCatId = catlist.get(pos).getCategoryID();
                                    new GetSubCatProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, subCatId);
                                }
                                categories.smoothScrollToPosition(pos);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "please check your internet connection......", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private class ProductsAdaspter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<ProductModel> data;
        private boolean withFooter, conn;

        private int ITEM = 0;
        private int FOOTER = 1;

        private ProductsAdaspter(ArrayList<ProductModel> data, boolean withFooter) {
            this.data = data;
            this.withFooter = withFooter;
            conn = true;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == FOOTER) {
                View root = LayoutInflater.from(context).inflate(R.layout.load_footer, null);
                return new FooterHolder(root);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.product_item, null);
                return new ProductHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == FOOTER) {
                FooterHolder vh = (FooterHolder) holder;
                vh.rotateloading.start();
                if (conn) {
                    vh.loading.setVisibility(View.VISIBLE);
                    vh.no_internet.setVisibility(View.GONE);
                } else {
                    vh.loading.setVisibility(View.GONE);
                    vh.no_internet.setVisibility(View.VISIBLE);
                }
            } else if (getItemViewType(position) == ITEM) {
                ProductHolder vh = (ProductHolder) holder;
                String imagePath = PRODUCT_IMAGE + getItem(position).getProduct_id() + "/thumb/" + getItem(position).getProduct_image().split(",")[0];
                Picasso.with(context)
                        .load(imagePath)
//                        .resize(120, 120)
                        .into(vh.poster);
                vh.title.setText(getItem(position).getProduct_title());
                vh.type.setText(getItem(position).getSource());
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (withFooter && position == getItemCount() - 1)
                return FOOTER;
            return ITEM;
        }

        @Override
        public int getItemCount() {
            int itemCount = data.size();
            if (withFooter)
                itemCount++;
            return itemCount;
        }

        private ProductModel getItem(int pos) {
            return data.get(pos);
        }

        private void refill(ArrayList<ProductModel> data) {
            int kek = this.data.size();
            this.data = data;
            notifyItemRangeInserted(kek, this.data.size());
        }

        public void updateFooter(boolean conn) {
            this.conn = conn;
            notifyDataSetChanged();
        }

        public void removeFooter() {
            withFooter = false;
            notifyDataSetChanged();
        }

        public void addFooter() {
            withFooter = true;
            notifyDataSetChanged();
        }

        public boolean hasFooter() {
            return withFooter;
        }

        class ProductHolder extends RecyclerView.ViewHolder {

            ImageView poster;
            CardView main;
            TextView title, type;

            ProductHolder(View itemView) {
                super(itemView);
                main = itemView.findViewById(R.id.main);
                poster = itemView.findViewById(R.id.poster);
                title = itemView.findViewById(R.id.title);
                type = itemView.findViewById(R.id.type);

                main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ProductDetails.class);
                        i.putExtra("data", getItem(getAdapterPosition()));
                        startActivity(i);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                });
            }
        }

        private class FooterHolder extends RecyclerView.ViewHolder {

            RotateLoading rotateloading;
            LinearLayout loading, no_internet;

            private FooterHolder(View itemView) {
                super(itemView);
                rotateloading = itemView.findViewById(R.id.rotateloading);
                loading = itemView.findViewById(R.id.loading);
                no_internet = itemView.findViewById(R.id.no_internet);
            }
        }
    }
}
