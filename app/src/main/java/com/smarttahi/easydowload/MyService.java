package com.smarttahi.easydowload;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class MyService extends Service {
    private DownloadBinder mBinder = new DownloadBinder();
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //在启动Service的时候 所需要进行的操作
        Log.d("MyService", "onStartCommand executed");

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // 创建一个Binder对象对下载的各个功能进行管理
        return mBinder;
    }
    static class DownloadBinder extends Binder{
        public void startService(){
            Log.d("MyServiceBinder", "startService: ");
        }

        public int getProgress(){
            Log.d("MyServiceBinder", "getProgress: ");
            return 0;
        }

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MyService","onUnbind");
        return super.onUnbind(intent);
    }
}
