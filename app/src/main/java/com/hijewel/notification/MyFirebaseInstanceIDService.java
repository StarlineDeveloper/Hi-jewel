package com.hijewel.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hijewel.utils.PreferenceUtils;

import static com.hijewel.utils.Constatnts.KEY_REGID;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    PreferenceUtils utils;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("TOKEN_ID", refreshedToken);
        utils = new PreferenceUtils(getApplicationContext());
        utils.setSavedUser(KEY_REGID, refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // sending gcm token to server

        Log.e(TAG, "sendRegistrationToServer: " + token);
    }
}
