package com.smarttahi.easydowload;

import android.app.AlertDialog;
import android.os.AsyncTask;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int PAUSE = 2;
    public static final int CANCEL = 3;
    private StateListener listener;
    private int lastProgress;   //记录下载进度
    private boolean isSuccess = false;
    private boolean isFailed = false;
    private boolean isPaused = false;
    private boolean isCanceled = false;

    private AlertDialog.Builder getProgressDialog() {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress >lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case SUCCESS:
                listener.onSucess();
                break;
            case FAILED:
                listener.onFailed();
                break;
            case PAUSE:
                listener.onPaused();
            case CANCEL:
                listener.onCanceled();
            default:
                break;
        }
    }

    public void PausedDownload(){
        isPaused = true;
    }

    public void CanceledDownload(){
        isCanceled = true;
    }

    public void SuccessDownload(){
        isSuccess = true;
    }

    public void FailedDownload(){
        isFailed = true;
    }

    protected Integer doInBackground(String... strings) {
        //TODO 请求网络，执行文件下载操作，并返回状态参数
        if(isPaused){
            return PAUSE;
        }
        if(isCanceled){
            return CANCEL;
        }
        return FAILED;
    }

    public interface StateListener {
        void onPaused();

        void onSucess();

        void onFailed();

        void onCanceled();

        void onProgress(int progress);
    }

}
