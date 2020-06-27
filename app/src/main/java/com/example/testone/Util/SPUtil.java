package com.example.testone.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
    public final String KEY_PHONE = "phone";
    public final String KEY_EMAIL = "email";
    public final String KEY_NAME = "name";

    private SharedPreferences preferences;

    public SPUtil(Context context) {
        final String SP_NAME = "ZhiHu.xml";
        context = context.getApplicationContext();
        preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String def) {
        return preferences.getString(key, def);
    }

    public void clearAll() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
