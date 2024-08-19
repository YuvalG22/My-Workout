package com.example.myworkout;

import android.app.Application;

import com.example.myworkout.Utilities.SharedPreferencesManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
    }
}
