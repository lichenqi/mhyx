package com.lianliantao.yuetuan.myokhttputils;

import android.os.Handler;
import android.os.Looper;


import com.lianliantao.yuetuan.myokhttputils.builder.DeleteBuilder;
import com.lianliantao.yuetuan.myokhttputils.builder.DownloadBuilder;
import com.lianliantao.yuetuan.myokhttputils.builder.GetBuilder;
import com.lianliantao.yuetuan.myokhttputils.builder.PatchBuilder;
import com.lianliantao.yuetuan.myokhttputils.builder.PostBuilder;
import com.lianliantao.yuetuan.myokhttputils.builder.PutBuilder;
import com.lianliantao.yuetuan.myokhttputils.builder.UploadBuilder;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * MyOkhttp
 * Created by tsy on 16/9/14.
 */
public class MyOkHttp {
    private static OkHttpClient mOkHttpClient;
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * construct
     */
    public MyOkHttp() {
        this(null);
    }

    /**
     * construct
     *
     * @param okHttpClient custom okhttpclient
     */
    public MyOkHttp(OkHttpClient okHttpClient) {
        if (mOkHttpClient == null) {
            synchronized (MyOkHttp.class) {
                if (mOkHttpClient == null) {
                    if (okHttpClient == null) {
                        mOkHttpClient = new OkHttpClient();
                    } else {
                        mOkHttpClient = okHttpClient;
                    }
                }
            }
        }
    }

    public GetBuilder get() {
        return new GetBuilder(this);
    }

    public PostBuilder post() {
        return new PostBuilder(this);
    }

    public PutBuilder put() {
        return new PutBuilder(this);
    }

    public PatchBuilder patch() {
        return new PatchBuilder(this);
    }

    public DeleteBuilder delete() {
        return new DeleteBuilder(this);
    }

    public UploadBuilder upload() {
        return new UploadBuilder(this);
    }

    public DownloadBuilder download() {
        return new DownloadBuilder(this);
    }

    /**
     * do cacel by tag
     *
     * @param tag tag
     */
    public void cancel(Object tag) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
