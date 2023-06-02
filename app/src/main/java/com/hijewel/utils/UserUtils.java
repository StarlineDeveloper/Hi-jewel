package com.hijewel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hijewel.Home;
import com.hijewel.R;

import static com.hijewel.database.MasterDatabase.clearCart;
import static com.hijewel.utils.Constatnts.USER_EMAIL;
import static com.hijewel.utils.Constatnts.USER_ID;
import static com.hijewel.utils.Constatnts.USER_MOBILE;
import static com.hijewel.utils.Constatnts.USER_NAME;

public class UserUtils {

    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public UserUtils(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return !getUserData("user_id", "").equals("");
    }

    public void saveUser(String[] data) {
        editor = pref.edit();
        editor.putString(USER_ID, data[2]);
        editor.putString(USER_NAME, data[3]);
        editor.putString(USER_EMAIL, data[4]);
        editor.putString(USER_MOBILE, data[5]);
        editor.apply();
    }

    public void logout() {
        clearCart();
        editor = pref.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(context, Home.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        ((Activity) context).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void updateData(String key, String value) {
        editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void updateData(String key, int value) {
        editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void updateData(String key, boolean value) {
        editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getUserData(String key, String Default) {
        return pref.getString(key, Default);
    }

    public int getUserData(String key, int Default) {
        return pref.getInt(key, Default);
    }

    public boolean getUserData(String key, boolean Default) {
        return pref.getBoolean(key, Default);
    }
}
