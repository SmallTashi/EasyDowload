package com.smarttahi.easydowload;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    static Context context;
    static Activity activity;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        }

    public static Context getThisContext(){
        return context;
    }


}
