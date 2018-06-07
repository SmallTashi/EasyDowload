package com.smarttahi.easydowload;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyService.DownloadBinder downloadBinder = new MyService.DownloadBinder();
    private FloatingActionButton ChangeState;
    private Intent bindIntent;
    private Intent stopIntent;
    Intent startIntent;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startService();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView start = findViewById(R.id.test_start);
        ChangeState = findViewById(R.id.floatingActionButton);
        ChangeState.setOnClickListener(this);
        Log.d("MyApp", "OnCreate");
        start.setOnClickListener(this);
        TextView stop = findViewById(R.id.test_stop);
//        TextView intentSer = findViewById(R.id.test_intent_service);
//        intentSer.setOnClickListener(this);
        stop.setOnClickListener(this);
        TextView bindButton = findViewById(R.id.test_bind);
        TextView unbindButton = findViewById(R.id.test_unbind);
        bindButton.setOnClickListener(this);
        unbindButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.test_intent_service:
//                Log.d("MyMainActvity","Thread id is"+Thread.currentThread().getId());
//                Intent intentService = new Intent(this,MyIntentService.class);
//                startService(intentService);
//                break;
            case R.id.test_start:
                startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.test_stop:
                if(startIntent ==null){
                    Toast.makeText(this,"UnStart",Toast.LENGTH_SHORT).show();
                }
                stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            case R.id.test_bind:
                bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);    //绑定服务
                break;
            case R.id.test_unbind:
                if(bindIntent == null){
                    Toast.makeText(this,"Unbind",Toast.LENGTH_SHORT).show();
                }else {
                    unbindService(connection);
                }

                break;
            case R.id.floatingActionButton:
                Toast.makeText(this,"Add",Toast.LENGTH_SHORT).show();
                //TODO create a dialog for new download task
            default:
                break;
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
        Log.d("MyApp", "onDestroy");
    }
}
