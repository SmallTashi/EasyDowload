package com.smarttahi.easydowload;

import android.os.Environment;

public class DownloadMessage {
    private String downloadURL = null ;
    private String path = Environment.getExternalStorageDirectory().getPath();
    private String name = null;
    private long contentLen = 0;

    /*volatile :
    1.保证可见——当共享变量被volatile修饰时
    他会保证修改的值立即更新到主存中，其他线程访问时会去主存访问最新的值

    2.禁止JVM进行指令重排
    */
    private volatile long completedLen;

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getPath() {
        return path;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getContentLen() {
        return contentLen;
    }

    public void setContentLen(long contentLen) {
        this.contentLen = contentLen;
    }

    public long getCompletedLen() {
        return completedLen;
    }

    public void setCompletedLen(long completedLen) {
        this.completedLen = completedLen;
    }
}
