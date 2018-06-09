package com.smarttahi.easydowload;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private MyService.DownloadBinder downloadBinder;
    private FloatingActionButton ChangeState;
    private int currentProgress;
    private EditText fileName;
    private EditText fileUrl;
    private ProgressBar progressBar;
    private TextView cancel;
    private TextView start;
    private TextView test;
    private TextView pause;
    private String name = "";
    private String url = "";

    public static List<DownloadMessage> list = null;
    AlertDialog.Builder builder;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            return inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.setListener(new MyService.Listener() {
                @Override
                public void onFinish() {
                    Log.e("zzz","finish");
                }
                @Override
                public void onDownload(int progress) {
                    Log.e("zzz","progress"+progress);
                    progressBar.setProgress(progress);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        findView();
        builder = new AlertDialog.Builder(this);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);    //绑定服务

        fileUrl = new EditText(this);
        fileName = new EditText(this);

        name = fileName.getText().toString();
        url = fileUrl.getText().toString();


        ChangeState.setOnClickListener(this);
        Log.d("MyApp", "OnCreate");
        start.setOnClickListener(this);
        test.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void findView() {
        test = findViewById(R.id.test);
        fileUrl = findViewById(R.id.edit_file_url);
        fileName = findViewById(R.id.edit_file_name);
        progressBar = findViewById(R.id.progressBar);
        start = findViewById(R.id.test_start);
        ChangeState = findViewById(R.id.floatingActionButton);
        pause = findViewById(R.id.test_pause);
        cancel = findViewById(R.id.test_cancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_start:
                if (list != null) {
                    Log.d("a","list:"+list.size());
                    if (list.lastIndexOf(MyApplication.getAutoMessage()) == list.size() - 1) {
                        Toast.makeText(this, "Please add downloadMessage", Toast.LENGTH_SHORT).show();
                    } else {
                        downloadBinder.startDownload(list.get(list.size() - 1));
                        progressBar.setProgress(downloadBinder.updateProgress());
                    }
                } else {
                    Toast.makeText(this, "Please add downloadMessage", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.test:
                Toast.makeText(this, "Execute auto download", Toast.LENGTH_SHORT).show();
                list = new ArrayList<>();
                list.add(MyApplication.getAutoMessage());
                downloadBinder.startDownload(MyApplication.getAutoMessage());
                progressBar.setProgress(downloadBinder.updateProgress());
                break;
            case R.id.test_pause:
                if (list == null) {
                    Toast.makeText(this, "Please add downloadMessage", Toast.LENGTH_SHORT).show();
                } else {
                    downloadBinder.pauseDownload();
                }
                break;
            case R.id.test_cancel:
                if (list == null) {
                    Toast.makeText(this, "Please add downloadMessage", Toast.LENGTH_SHORT).show();
                } else {
                    downloadBinder.cancelDownload();
                }
                break;
            case R.id.floatingActionButton:
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                builder.setView(R.layout.input_dialog)
                        .setPositiveButton(R.string.Do, this)
                        .setNegativeButton(R.string.Cancel, this)
                        .create();
                builder.show();
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MyApp", "OnStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MyApp", "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyApp", "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MyApp", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (url.length() > 2 && name.length() > 2) {
                    DownloadMessage message = new DownloadMessage();
                    message.setDownloadURL(url);
                    message.setName(name);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(message);
                    downloadBinder.startDownload(list.get(list.lastIndexOf(message)));
                } else {
                    Toast.makeText(this, "Please complete downloadMessage", Toast.LENGTH_SHORT).show();
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(this, "Cancel downloadTask", Toast.LENGTH_SHORT).show();

        }
    }

    public static void openFile(Context context, DownloadMessage message) {
        String path = message.getPath() + "/123.mp3";
        Log.e("zzz",path);
        File f = new File(path);
        Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.parse(path);
        myIntent.setDataAndType(uri,"audio/mp3");
//        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(f).toString());
//        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//        myIntent.setDataAndType(Uri.fromFile(f), mimetype);
        context.startActivity(myIntent);
    }

}
