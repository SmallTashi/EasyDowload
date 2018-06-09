package com.smarttahi.easydowload;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    private static final int SUCCESS = 0;
    private static final int FAILED = 1;
    private static final int PAUSE = 2;
    private static final int CANCEL = 3;
    private StateListener listener;
    private int lastProgress;
    private DownloadMessage message;
    private int status;
    File file = null;
    private boolean isStop = false;
    private RandomAccessFile saveFile = null;
    private boolean isPaused = false;
    private boolean isCanceled = false;


    DownloadTask(StateListener listener, DownloadMessage message) {
        this.message = message;
        this.listener = listener;
    }


    /**
     * 此方法运行在UI线程中，可更新UI
     * */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    /**
     * 在doInBackground 执行完之后运行
     * 并运行在UI线程中，可更新UI
     * */

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case SUCCESS:
                listener.onSuccess();
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

//    public void stop() {
//        isStop = true;
//    }

    public void PausedDownload() {
        isPaused = true;
    }

    public void CanceledDownload() {
        isCanceled = true;
    }

//    public void SuccessDownload() {
//        boolean isSuccess = true;
//    }
//
//    public void FailedDownload() {
//        boolean isFailed = true;
//    }

    /**
     * RandomAccessFile:
     * 实现断点续传
     * 访问保存数据记录的文件，并进行读写，文件的位置与操作数据的大小必须是已知的
     * 该类仅限于操作文件
     *
     * @method 定位：               .getFilePointer()
     * 可在文件里移动：      .seek()
     * 判断文件大小：        .length()
     * 跳过指定大小的字节数： .skipBytes()
     */

    private int saveFile(InputStream inputStream) {
        file = new File(message.getName());
        BufferedInputStream bis = null;
        if(message.getContentLen()==0){
            return  FAILED;
        }else if(message.getContentLen()==message.getCompletedLen()){
            return SUCCESS;
        }
            byte[] buffer = new byte[1024 * 2];
            try {
                saveFile = new RandomAccessFile(file, "rwd");
                int len = 0;
                bis = new BufferedInputStream(inputStream);
                saveFile.seek(message.getCompletedLen());
                while (!isStop && -1 != (len = bis.read(buffer))) {
                    if(isCanceled){
                        return CANCEL;
                    }
                    if(isPaused){
                        return PAUSE;
                    }
                    saveFile.write(buffer, 0, len);
                    message.setCompletedLen(message.getCompletedLen() + len);
                    lastProgress = (int) (message.getCompletedLen() * 100 / message.getContentLen());
                    publishProgress(lastProgress);
                }

                if (len == -1) {
                    Log.d("tag", "下载完成");
                    return SUCCESS;
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            finally {
                try {
                    if(bis!=null)
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(file!=null&&isCanceled){
                        if(file.delete()){
                            status = CANCEL;
                        }
                    }
                    if(saveFile!=null)
                    saveFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(inputStream!=null)
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        return FAILED;
    }


    protected Integer doInBackground(String... strings) {

        InternetRequest request;
        try {
            request = new InternetRequest(message.getDownloadURL());
            request.beginDownload(message, new InternetRequest.Callback() {
                @Override
                public void onSuccess(InputStream inputStream) {
                    status= saveFile(inputStream);
                }

                @Override
                public void onFiled(Exception e) {
                    status = FAILED;
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
            status=FAILED;
        }
        if (isPaused) {
            return PAUSE;
        }
        if (isCanceled) {
            return CANCEL;
        }
        return status;
    }

    public interface StateListener {
        void onPaused();

        void onSuccess();

        void onFailed();

        void onCanceled();

        void onProgress(int progress);
    }

}
