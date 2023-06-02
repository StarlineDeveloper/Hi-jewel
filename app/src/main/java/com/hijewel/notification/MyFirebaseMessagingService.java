package com.hijewel.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hijewel.Home;
import com.hijewel.Information;
import com.hijewel.MainActivity;
import com.hijewel.R;
import com.hijewel.utils.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String ACTION_TYPE_PRODUCT_ADDED = "1";
    public static final String KEY_NOTIFICATION_TYPE = "notification_type";
    private final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private Map<String, String> data;
    private String notificationMSg = "";
    private String SKUID = "";
    private PreferenceUtils sessionManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {






        sessionManager = new PreferenceUtils(getApplicationContext());

        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Message data:" + remoteMessage.getData());
            data = remoteMessage.getData();

            Log.e("remotemagase1",""+remoteMessage.getData());

            detectNotificationType(data.get("body"), data.get("sku"), data.get("ntype"),data.get("bit"));

        } else if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            JSONObject notificationObj = null;
            try {
                notificationObj = new JSONObject(remoteMessage.getNotification().getBody());
                String bodyMSG = notificationObj.getString("body");
                String sku = notificationObj.getString("sku");
                String nType = notificationObj.getString("ntype");
                String bit = notificationObj.getString("bit");


                Log.e("bit",""+bit);

                Log.e("remotemagase",""+remoteMessage.getData());

                detectNotificationType(bodyMSG, sku, nType,bit);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.i(TAG, "Message Notification Body: NULL");
        }
    }


    private void detectNotificationType(String notificationMSg, String sku, String nType,String bit) {

        this.notificationMSg = notificationMSg;
        this.SKUID = sku;

        if(bit.equals("2")){
            handelNewsUpdateNotificationInformation();
        }
        else {
            handelNewsUpdateNotification();

        }


//        if (nType.equalsIgnoreCase(ACTION_TYPE_PRODUCT_ADDED)) {
        //}
    }


    private void handelNewsUpdateNotification() {
        Intent intent;

        intent = new Intent(this, Home.class);
        intent.putExtra(KEY_NOTIFICATION_TYPE, ACTION_TYPE_PRODUCT_ADDED);
        intent.putExtra("SKU", SKUID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sendNotificationNew(intent);
    }

    private void handelNewsUpdateNotificationInformation() {
        Intent intent;

        intent = new Intent(this, Information.class);
        intent.putExtra(KEY_NOTIFICATION_TYPE, ACTION_TYPE_PRODUCT_ADDED);
        intent.putExtra("SKU", SKUID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sendNotificationNew(intent);
    }

    public void sendNotificationNew(Intent intent) {


        Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        final int NOTIFY_ID = new Random().nextInt(100000000); // ID of notification
        String id = getString(R.string.default_notification_channel_id); // default_channel_id
        String title = getString(R.string.default_notification_channel_title); // default_channel_id
        NotificationCompat.Builder builder;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            assert notificationManager != null;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

        } else {
            builder = new NotificationCompat.Builder(this);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        builder.setSmallIcon(R.drawable.ic_notifications_active_black_24dp) // required
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(Html.fromHtml(notificationMSg))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(notificationMSg)))
                .setLargeIcon(rawBitmap)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        Notification notification = builder.build();
        assert notificationManager != null;
        notificationManager.notify(NOTIFY_ID, notification);

        wakeUPDevice();
    }

    private void wakeUPDevice() {
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
        wl.release();
    }
}
