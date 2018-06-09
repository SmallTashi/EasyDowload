package com.smarttahi.easydowload;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;


public class MyService extends Service {
    private DownloadBinder mBinder = new DownloadBinder();
    public DownloadTask downloadTask;
    public int currentProgress = 0;
    private Listener lis;


    private DownloadTask.StateListener listener = new DownloadTask.StateListener() {
        @Override
        public void onPaused() {
            downloadTask = null;
            getNotificationManager().notify(1, getNotification("Download task paused...", -1));
            Toast.makeText(MyService.this, "Download task paused...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            if (lis != null) lis.onFinish();
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download task is completed", -1));
            Toast.makeText(MyService.this, "Download task is completed", Toast.LENGTH_SHORT).show();
            MainActivity.openFile(MyService.this,MainActivity.list.get(MainActivity.list.size()-1));
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Failed", -1));
            Toast.makeText(MyService.this, "Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            getNotificationManager().notify(1, getNotification("Canceled", -1));
            Toast.makeText(MyService.this, "Canceled", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProgress(int progress) {
            Log.e("progress","p:"+progress);
            if (lis != null) lis.onDownload(progress);
            currentProgress = progress;
            getNotificationManager().notify(1, getNotification("Downloading", progress));
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
        //TODO 与bind谁执行下载任务
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
        Log.d("MyService", "onUnbind");
        return super.onUnbind(intent);
    }


    public interface Listener{
        void onFinish();
        void onDownload(int progress);
    }
    /**
     * 此类用于执行希望服务进行的操作（方法申明为public）
     */
    class DownloadBinder extends Binder {

        public void setListener(Listener listener) {
            lis = listener;
        }

        public int updateProgress(){
            return currentProgress;
        }

        public void startDownload(DownloadMessage message) {
            if (downloadTask == null) {
                downloadTask = new DownloadTask(listener, message);
                downloadTask.execute();
                startForeground(1, getNotification("Begin download...", 0));
                Toast.makeText(MyService.this, "Begin download...", Toast.LENGTH_SHORT).show();
            } else {
                downloadTask.execute();
            }

        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.PausedDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.CanceledDownload();
            }
            getNotificationManager().cancel(1);
            stopForeground(true);
        }
    }


    private Notification getNotification(String title, int progress) {
        Intent proIntent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, proIntent, 0);

        //点击通知的时候跳转到MainActivity
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);

        //更新通知，显示下载进度
        if (progress > 0) {
            notification.setContentTitle(progress + "%")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.seventeen)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.thriteen))
                    .setContentIntent(pi)
                    .setProgress(100, progress, false)
            ;
        } else {
            notification.setContentTitle(progress + "%")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.seventeen)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.thriteen))
                    .setContentIntent(pi);
        }
        return notification.build();
    }


    /**
     * 获得Notification的实例
     */
    public NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
}

