package com.smarttahi.easydowload;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ProService extends Service {
    public static final String TAG = "MyProService";
    public ProService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate executed");
        Intent proIntent = new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,proIntent,0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("文件下载中...")
                .setContentText("This is text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.seventeen)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.thriteen))
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //在启动Service的时候 所需要进行的操作
        Log.d(TAG, "onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy executed");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyService.DownloadBinder();
    }
}
