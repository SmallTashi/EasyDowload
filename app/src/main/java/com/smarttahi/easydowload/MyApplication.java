package com.smarttahi.easydowload;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context context;
    public static DownloadMessage message;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        }

    public static Context getThisContext(){
        return context;
    }

    public static DownloadMessage getAutoMessage(){
        DownloadMessage downloadMessage = new DownloadMessage();
        downloadMessage.setDownloadURL("http://other.web.rh01.sycdn.kuwo.cn/resource/n2/25/67/2959040831.mp3");
//        downloadMessage.setName("");
        downloadMessage.setName(downloadMessage.getDownloadURL());
        return downloadMessage;
    }
}
