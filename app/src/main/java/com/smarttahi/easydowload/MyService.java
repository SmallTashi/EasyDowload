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
import android.widget.Toast;


public class MyService extends Service {
    private DownloadBinder mBinder = new DownloadBinder();
    public DownloadTask downloadTask;


    private DownloadTask.StateListener listener = new DownloadTask.StateListener() {
        @Override
        public void onPaused() {
            downloadTask=null;
            getNotificationManager().notify(1,getNotification("下载任务已暂停",-1));
            Toast.makeText(MyService.this,"已暂停...",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载完成",-1));
            Toast.makeText(MyService.this,"下载成功...",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败",-1));
            Toast.makeText(MyService.this,"下载失败...",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask=null;
            getNotificationManager().notify(1,getNotification("下载任务已取消",-1));
            Toast.makeText(MyService.this,"任务取消...",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("正在下载",progress));
        }
    };
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

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MyService","onUnbind");
        return super.onUnbind(intent);
    }

    /**此类用于执行希望服务进行的操作（方法申明为public）*/
    class DownloadBinder extends Binder{

        public void startDownload(DownloadMessage message){
            if(downloadTask==null){
                downloadTask = new DownloadTask(listener,message);
                downloadTask.execute();
                startForeground(1,getNotification("开始下载...",0));
                Toast.makeText(MyService.this,"开始下载...",Toast.LENGTH_SHORT).show();
            }

        }

        public void pauseDownload(){
            if(downloadTask!=null){
                downloadTask.PausedDownload();
            }
        }

        public void cancelDownload(){
            if(downloadTask != null){
                downloadTask.CanceledDownload();
            }
            getNotificationManager().cancel(1);
            stopForeground(true);
        }
    }




    private Notification getNotification(String title,int progress){
        Intent proIntent = new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,proIntent,0);

        //点击通知的时候跳转到MainActivity
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);

        //更新通知，显示下载进度
        if(progress>0){
                    notification.setContentTitle(progress + "%")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.seventeen)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.thriteen))
                    .setContentIntent(pi)
                    .setProgress(100,progress,false)
                    ;
        }else {
            notification.setContentTitle(progress + "%")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.seventeen)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.thriteen))
                    .setContentIntent(pi);
        }
        return notification.build();
    }


    /**获得Notification的实例*/
    public NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }
}

