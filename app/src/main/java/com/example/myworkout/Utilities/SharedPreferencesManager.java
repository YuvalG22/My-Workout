package com.example.myworkout.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static volatile SharedPreferencesManager instance = null;

    private static final String SP_FILE = "SP_FILE";
    private SharedPreferences sharedPref;

    private SharedPreferencesManager(Context context) {
        this.sharedPref = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesManager init(Context context){
        if (instance == null){
            synchronized (SharedPreferencesManager.class){
                if (instance == null){
                    instance = new SharedPreferencesManager(context);
                }
            }
        }
        return getInstance();
    }

    public static SharedPreferencesManager getInstance() {
        return instance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getString( String key, String defaultValue) {
        return sharedPref.getString(key, defaultValue);
    }

    public int getInt( String key, int defaultValue) {
        return sharedPref.getInt(key, defaultValue);
    }
}
