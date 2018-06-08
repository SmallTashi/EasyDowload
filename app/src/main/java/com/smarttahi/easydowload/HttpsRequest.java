package com.smarttahi.easydowload;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class HttpsRequest {

    private static OutputStream out = null;

    private HttpsURLConnection connectionHttps = null;

    private HttpURLConnection connectionHttp = null;

    HttpsRequest(String downloadURL) throws MalformedURLException {
        URL url = new URL(downloadURL);

        try {
            if (downloadURL.startsWith("https:"))

            {
                connectionHttps = (HttpsURLConnection) url.openConnection();
                
            } else {
                connectionHttp = (HttpURLConnection) url.openConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Download(final HttpURLConnection conn, final Callback callback) {
        try {
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                try {
                    callback.onSuccess(conn.getInputStream());
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFiled(e);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //使用回调，返回请求得到的数据

            } else {
                callback.onFiled(new Exception("网络连接失败"));
            }
        } catch (final ProtocolException e) {

            callback.onFiled(e);
        } catch (final IOException e) {
            e.printStackTrace();

            callback.onFiled(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void Download(final HttpsURLConnection conn, final Callback callback) {
        try {
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {

                try {

                    callback.onSuccess(conn.getInputStream());
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFiled(e);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //使用回调，返回请求得到的数据

            } else {
                callback.onFiled(new Exception("网络连接失败"));
            }
        } catch (final ProtocolException e) {

            callback.onFiled(e);
        } catch (final IOException e) {
            e.printStackTrace();

            callback.onFiled(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void beginDownload(DownloadMessage message, Callback callback) {
        if (message.getContentLen() == 0) {
            message.setContentLen(Long.parseLong(connectionHttps.getHeaderField("content-length")));
        } else {
            connectionHttps.setRequestProperty("Range", "bytes=" + message.getCompletedLen() + "-" + message.getContentLen());
        }
        Download(connectionHttps, callback);
    }


    public interface Callback {
        void onSuccess(InputStream inputStream) throws JSONException;

        void onFiled(Exception e);
    }


}
