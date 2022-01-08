package ru.smirnov.test.moretechapp;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class CustomApplication extends Application {
    private final static String TAG = CustomApplication.class.getName();

    public static String userToken = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
