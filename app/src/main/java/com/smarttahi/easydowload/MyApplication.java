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


    public static DownloadMessage getMessage() {
        return message;
    }

    public static void setMessage(DownloadMessage message) {
        MyApplication.message = message;
    }

    public static DownloadMessage getAutoMessage(){
        DownloadMessage downloadMessage = new DownloadMessage();
        downloadMessage.setPath("/storage/emulated/0/1"+"temp");
        downloadMessage.setDownloadURL("http://image.coolapk.com/apk_image/2017/0906/efc629db13374722fb11ea966b09022e-for-158877-o_1bpb511hu1sa41hnis2haif123o1f-uid-1124262.png.t.jpg");
        downloadMessage.setName("tempFile");
        return downloadMessage;
    }
}
