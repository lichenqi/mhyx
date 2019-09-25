package com.lianliantao.yuetuan.kotlin_activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*主线程发消息到子线程*/
public class MainThreadSendMsg2ChildThread extends AppCompatActivity {

    private Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HandlerThread handlerThread = new HandlerThread("magic");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        handler.sendEmptyMessage(0);
    }

    class MyThread extends Thread {

        private Looper looper;

        @Override
        public void run() {
            Looper.prepare();/*创建该子线程的looper*/
            looper = Looper.myLooper();/*取出该子线程的looper*/
            Looper.loop();
        }
    }

}
