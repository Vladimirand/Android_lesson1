package com.nehvedovich.vladimir.pogoda.screens.database;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class WeatherApplication extends Application {

    private static WeakReference<Context> context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = new WeakReference<>(this.getApplicationContext());
    }

    public static Context getContext() {
        return context.get();
    }
}
