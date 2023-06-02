package com.hijewel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    private SharedPreferences pref,saved_user;
    private SharedPreferences.Editor editor;

    public PreferenceUtils(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        saved_user = context.getSharedPreferences("saved_user", Context.MODE_PRIVATE);
    }

    public void setPrefrences(String key, String value) {
        editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPrefrences(String key, int value) {
        editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void setSavedUser(String key, String value) {
        editor = saved_user.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPrefrences(String key, boolean value) {
        editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getSavedUser(String key) {
        return saved_user.getString(key, "");
    }

    public String getPrefrence(String key, String Default) {
        return pref.getString(key, Default);
    }

    public int getPrefrence(String key, int Default) {
        return pref.getInt(key, Default);
    }

    public boolean getPrefrence(String key, boolean Default) {
        return pref.getBoolean(key, Default);
    }
}
