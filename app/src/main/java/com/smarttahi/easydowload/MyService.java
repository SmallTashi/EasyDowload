package com.smarttahi.easydowload;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class MyService extends Service {
    private DownloadBinder mBinder = new DownloadBinder();
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate executed");
//        Intent intent = new Intent(this,MainActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
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

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MyService","onUnbind");
        return super.onUnbind(intent);
    }

    /**此类用于执行希望服务执行的操作（方法申明为public）*/
    static class DownloadBinder extends Binder{
        public void startService(){
            Log.d("MyServiceBinder", "startService: ");
        }

        public int getProgress(){
            Log.d("MyServiceBinder", "getProgress: ");
            return 0;
        }

    }

    private Notification getNotification(String title,int progress){
        Intent proIntent = new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,proIntent,0);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);

        if(progress>0){
                    notification.setContentTitle(progress + "%")
                    .setContentText("This is text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.seventeen)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.thriteen))
                    .setContentIntent(pi)
                    .setProgress(100,progress,false)
                    ;
        }else {
            notification.setContentTitle(progress + "%")
                    .setContentText("This is text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.seventeen)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.thriteen))
                    .setContentIntent(pi);
        }
        return notification.build();
    }
}

