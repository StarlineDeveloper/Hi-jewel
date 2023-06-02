package com.hijewel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hijewel.adapters.CatsAdapter;
import com.hijewel.adapters.SocialAdapter;
import com.hijewel.adapters.UpdateAdapterBullion;
import com.hijewel.async.Liveratedb;
import com.hijewel.models.CatsModel;
import com.hijewel.models.ContactModel;
import com.hijewel.models.MenuCatsModel;
import com.hijewel.models.SocialModel;
import com.hijewel.models.SymbolModel;
import com.hijewel.models.UpdatesModel;
import com.hijewel.ui.MovableFloatingActionButton;
import com.hijewel.ui.NonScrollExpandableListView;
import com.hijewel.utils.Common;
import com.hijewel.utils.GetData;
import com.hijewel.utils.PreferenceUtils;
import com.hijewel.utils.ScrollTextView;
import com.hijewel.utils.UserUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hijewel.database.MasterDatabase.getCartCount;
import static com.hijewel.notification.MyFirebaseMessagingService.ACTION_TYPE_PRODUCT_ADDED;
import static com.hijewel.notification.MyFirebaseMessagingService.KEY_NOTIFICATION_TYPE;
import static com.hijewel.utils.Constatnts.CONTACT_US;
import static com.hijewel.utils.Constatnts.DialogPopOption;
import static com.hijewel.utils.Constatnts.USER_MOBILE;
import static com.hijewel.utils.Constatnts.USER_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected static boolean isLaunch = true;

    Context context;
    UserUtils userUtils;
    PreferenceUtils utils;
    GetData gd;

    FrameLayout frameLayout, mLayout;
    FrameLayout home, liverates, company, my_cart, contact_us, login_logout,inf,
            texttt, logo, count_back,information,pdf;
    TextView init, name, mobile, login_label, cart_count;
    ImageView menu, login_icon;
    DrawerLayout mDrawerLayout;
    NonScrollExpandableListView categories;



    ContactModel cm;
    private ArrayList<SocialModel> updateDataList;

    MovableFloatingActionButton ivWhatsApp;

    String showtap="";


    LinearLayout rate_layout;
    TextView product, sell;

    CatsAdapter adapter;
    Handler handler;
    Runnable runnable;
    ArrayList<SymbolModel> mainData;
    private int lastExpandedPosition = -1;

    ScrollTextView tvMarquee;

    TextView social;

    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        userUtils = new UserUtils(context);
        mainData = new ArrayList<>();
        utils = new PreferenceUtils(context);
        gd = new GetData(context);

        social=findViewById(R.id.social);

        menu = findViewById(R.id.menu);
        frameLayout = findViewById(R.id.frame_container);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mLayout = findViewById(R.id.container);




        categories = findViewById(R.id.categories);
        categories.setGroupIndicator(null);
        setItems();

        categories.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    categories.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        categories.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
                mDrawerLayout.closeDrawer(mLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent home = new Intent(context, ProductList.class);
                        home.putExtra("MenuURL", adapter.getGroup(groupPosition).getTitle().toLowerCase());
                        home.putExtra("CategoryURL", adapter.getChild(groupPosition, childPosition).getURL());
                        startActivity(home);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 200);
                return false;
            }
        });

        rate_layout = findViewById(R.id.rate_layout);
        product = findViewById(R.id.product);
        sell = findViewById(R.id.sell);

        home = findViewById(R.id.home);
        home.setOnClickListener(this);

        liverates = findViewById(R.id.liverates);
        liverates.setOnClickListener(this);

        information=findViewById(R.id.information);
        information.setOnClickListener(this);

        pdf=findViewById(R.id.pdf);
        pdf.setOnClickListener(this);

        company = findViewById(R.id.company);
        company.setOnClickListener(this);
        my_cart = findViewById(R.id.my_cart);
        count_back = findViewById(R.id.count_back);
        cart_count = findViewById(R.id.cart_count);
        my_cart.setOnClickListener(this);
        contact_us = findViewById(R.id.contact_us);
        contact_us.setOnClickListener(this);
        login_logout = findViewById(R.id.login_logout);
        login_logout.setOnClickListener(this);

        texttt = findViewById(R.id.texttt);
        logo = findViewById(R.id.logo);
        init = findViewById(R.id.init);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        login_label = findViewById(R.id.login_label);
        login_icon = findViewById(R.id.login_icon);

        manageLogin();



        social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("DialogPopOption",DialogPopOption);


                    showSocialMediaDialog();



            }
        });

        ivWhatsApp=findViewById(R.id.ivWhatsApp);


        showtap=Common.getPreferenceString(MainActivity.this,"showtap","");
        //Toast.makeText(MainActivity.this, ""+showtap, Toast.LENGTH_LONG).show();
        if(showtap.equals("")||showtap==null){
            showHintView();


        }







        try {
            cm = new ContactModel(new JSONObject(utils.getPrefrence(CONTACT_US, "")));

            tvMarquee=findViewById(R.id.tvMarquee);
            tvMarquee.setText(cm.getmarquee());
            tvMarquee.startScroll();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ivWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact=cm.Whatsapp();

                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = context.getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        if (isLaunch) {
            isLaunch = false;
            updateUI();
            Intent home = new Intent(context, Home.class);
            if (getIntent().hasExtra(KEY_NOTIFICATION_TYPE) &&
                    getIntent().getStringExtra(KEY_NOTIFICATION_TYPE).equalsIgnoreCase(ACTION_TYPE_PRODUCT_ADDED)) {
                home.putExtra("SKU", getIntent().getStringExtra("SKU"));
            }
            startActivity(home);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        if(DialogPopOption.equals("1")){
            getUpdatesDetails2222();

        }



    }

    public void manageLogin() {
        if (userUtils.isLoggedIn()) {
            logo.setVisibility(View.GONE);
            texttt.setVisibility(View.VISIBLE);
            init.setText(userUtils.getUserData(USER_NAME, "").substring(0, 1).toUpperCase());
            name.setText(userUtils.getUserData(USER_NAME, ""));
            mobile.setText(userUtils.getUserData(USER_MOBILE, ""));
            login_label.setText("Logout");
            login_icon.setImageResource(R.drawable.ic_logout);
        } else {
            logo.setVisibility(View.VISIBLE);
            texttt.setVisibility(View.GONE);
            init.setText("");
            name.setText("Welcome User");
            mobile.setText("");
            login_label.setText("Login");
            login_icon.setImageResource(R.drawable.ic_login);
        }
    }

    public void openMenu(View view) {
        mDrawerLayout.openDrawer(mLayout);
    }

    private void setItems() {

        ArrayList<MenuCatsModel> main = new ArrayList<>();
        HashMap<String, List<CatsModel>> localHashMap = new HashMap<>();

        main.add(new MenuCatsModel(R.drawable.ic_jewellery, "Jewellery"));
        List<CatsModel> jewl = new ArrayList<>();
        jewl.addAll(gd.getCats("4"));

        main.add(new MenuCatsModel(R.drawable.ic_utsav, "Utsav"));
        List<CatsModel> uts = new ArrayList<>();
        uts.addAll(gd.getCats("3"));

        main.add(new MenuCatsModel(R.drawable.ic_rohit, "Platinium"));
        List<CatsModel> plet = new ArrayList<>();
        plet.addAll(gd.getCats("6"));

        localHashMap.put(main.get(0).getTitle(), jewl);
        localHashMap.put(main.get(1).getTitle(), uts);
        localHashMap.put(main.get(2).getTitle(), plet);

        adapter = new CatsAdapter(localHashMap, main);
        categories.setAdapter(adapter);
    }

    private void showSocialMediaDialog() {



        social.setVisibility(View.GONE);


        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        assert window != null;
        window.getAttributes().windowAnimations = R.style.DialogTheme;


        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.social_dialog);


        dialog.show();

        //dialog.setCanceledOnTouchOutside(false);
       // dialog.setCancelable(false);


        final LinearLayout llMainDialogView = dialog.findViewById(R.id.llMainDialodView);

        llMainDialogView.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                dialog.dismiss();
                social.setVisibility(View.VISIBLE);

            }
        });

        RecyclerView rvNotification=dialog.findViewById(R.id.rvNotification);

        updateDataList = new ArrayList<SocialModel>();

        RecyclerView.LayoutManager homelayoutmanager = new GridLayoutManager(this, 1);
        rvNotification.setLayoutManager(homelayoutmanager);
        rvNotification.setAdapter(new SocialAdapter(MainActivity.this, updateDataList));



        updateDataList.clear();
        updateDataList.addAll((Collection<? extends SocialModel>) new Gson().fromJson(cm.sociallink(), new TypeToken<ArrayList<SocialModel>>() {
        }.getType()));

        Log.e("ttttttttt",cm.sociallink());


//        llMainDialogView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                dialog.dismiss();
//                social.setVisibility(View.VISIBLE);
//
//                return false;
//            }
//        });





    }

    public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.Holder> {

        private Context context;
        private ArrayList<SocialModel> arrayList;


        public SocialAdapter(Context context, ArrayList<SocialModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(context).inflate(R.layout.item_social, null);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            try {
                final SocialModel updateAdapterBullion = arrayList.get(position);



               Picasso.with(context).load(updateAdapterBullion.geticon()).into(holder.profile_image);



                holder.profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        social.setVisibility(View.VISIBLE);


                        Uri uri = Uri.parse(updateAdapterBullion.getlink());
                        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                        likeIng.setPackage("com."+updateAdapterBullion.getTitle()+".android");
                        try {
                            context.startActivity(likeIng);
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        }


                    /*if(updateAdapterBullion.getTitle().equals("facebook")){

                    }
                    else if(updateAdapterBullion.getTitle().equals("INSTAGRAM")){

                    }
                    else if(updateAdapterBullion.getTitle().equals("LINKEDIN")){

                    }else {



                    }*/

                    }
                });



            } catch (Exception e) {
                e.printStackTrace();
            }


        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public SocialModel getItem(int pos) {
            return arrayList.get(pos);
        }


        class Holder extends RecyclerView.ViewHolder {




            CircleImageView profile_image;



            private Holder(View vi) {
                super(vi);
                profile_image=vi.findViewById(R.id.profile_image);



            }
        }


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




                    String Table1=student1.getString("Table1");
                    JSONArray jsonArray = new JSONArray(Table1);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String Title = jsonObject1.optString("Title");
                        String Description = jsonObject1.optString("Description");


                        DialogSystem(Title,Description);

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

    public void DialogSystem(String titel,String des){

        DialogPopOption="0";


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml(titel));
        alertDialogBuilder.setMessage(Html.fromHtml(des));
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showHintView() {

            new TapTargetSequence(this)
                    .targets(TapTarget.forView(social, "SOCIAL MEDIA", "Tap here to connect to social media")
                                    .outerCircleColor(R.color.semi_transparent)
                                    .targetCircleColor(R.color.white)
                                    .descriptionTextColor(R.color.white)
                                    .titleTextColor(R.color.white)
                                    .tintTarget(false)
                                    .cancelable(false),




                            TapTarget.forView(ivWhatsApp, "WhatsApp chat", "Tap here to open WhatsApp chat.")
                                    .outerCircleColor(R.color.semi_transparent)
                                    .targetCircleColor(R.color.white)
                                    .descriptionTextColor(R.color.white)
                                    .titleTextColor(R.color.white)
                                    .tintTarget(false)
                                    .cancelable(false)
                    ).listener(new TapTargetSequence.Listener() {

                @Override
                public void onSequenceFinish() {
                    Common.setPreferenceString(MainActivity.this,"showtap","No");

                }

                @Override
                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                }

                @Override
                public void onSequenceCanceled(TapTarget lastTarget) {

                }
            }).start();

    }




//    private void connectWebSocket() {
//        URI uri;
//        try {
//            uri = new URI("ws://192.168.3.243:1234");
//        } catch (URISyntaxException e) {
//            Log.e("lol", e.toString());
//            e.printStackTrace();
//            return;
//        }
//
//        mWebSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
//            }
//
//            @Override
//            public void onMessage(String s) {
//                final String message = s;
//                Log.e("RRRRRR", message);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("rrr", "mmm");
//                        Intent i = new Intent("getRates");
//                        i.putExtra("data", message);
//                        i.putExtra("save_prev", "yes");
//                        sendBroadcast(i);
//                    }
//                });
//            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                Log.e("Websocket", "Closed " + s);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent i = new Intent("getRates");
//                        i.putExtra("data", utils.getPrefrence(LIVE_RATES, ""));
//                        sendBroadcast(i);
//                        if (!stopped && (mWebSocketClient == null || !mWebSocketClient.getConnection().isOpen()))
//                            connectWebSocket();
//                    }
//                });
//                Log.e("mjfjkasd", "kek");
//            }
//
//            @Override
//            public void onError(Exception e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent i = new Intent("getRates");
//                        i.putExtra("data", utils.getPrefrence(LIVE_RATES, ""));
//                        sendBroadcast(i);
//                    }
//                });
//                Log.e("Websocket", "Error " + e.getMessage());
//            }
//        };
//        mWebSocketClient.connect();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                mDrawerLayout.closeDrawer(mLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, Home.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 200);
                break;
            case R.id.liverates:
                mDrawerLayout.closeDrawer(mLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, Liverates.class);
                        startActivity(i);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 200);
                break;
            case R.id.information:
                mDrawerLayout.closeDrawer(mLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, Information.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 200);
                break;
            case R.id.pdf:
                mDrawerLayout.closeDrawer(mLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, PDFNEW.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 200);
                break;
            case R.id.company:
                mDrawerLayout.closeDrawer(mLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, CompanyProfile.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 200);
                break;
            case R.id.my_cart:
                mDrawerLayout.closeDrawer(mLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (userUtils.isLoggedIn()) {
                            Intent i = new Intent(context, MyCart.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        } else {
                            Intent i = new Intent(context, Login.class);
                            startActivityForResult(i, 69);
                            overridePendingTransition(R.anim.push_up_in, R.anim.stable);
                        }
                    }
                }, 200);
                break;
            case R.id.contact_us:
                mDrawerLayout.closeDrawer(mLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, ContactUs.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }, 200);
                break;
            case R.id.login_logout:
                if (userUtils.isLoggedIn()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userUtils.logout();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alt = builder.create();
                    alt.setTitle("Are you sure!!");
                    alt.setMessage("Are you sure you want to logout?");
                    alt.show();
                } else {
                    mDrawerLayout.closeDrawer(mLayout);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(context, Login.class);
                            startActivityForResult(i, 69);
                            overridePendingTransition(R.anim.push_up_in, R.anim.stable);
                        }
                    }, 200);
                }
                break;
            default:
                break;
        }

    }

    public void updateCartCount() {
        int count = getCartCount();
        cart_count.setText(String.valueOf(count));
        if (count > 0) {
            count_back.setVisibility(View.VISIBLE);
        } else {
            count_back.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 69) {
                manageLogin();
            }
        }
    }

    public void updateUI() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    new Liveratedb(context).execute();
                    handler.postDelayed(this, 700);
                } catch (Exception e) {
                    handler.postDelayed(this, 700);
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        stopped = false;
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        stopped = true;
//        if (mWebSocketClient != null) {
//            mWebSocketClient.close();
//        }
//    }
}
