package com.lianliantao.yuetuan.kotlin_activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.custom_view.SketchView;
import com.lianliantao.yuetuan.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*子线程向主线程通讯*/
public class ChildThreadSendMsg2MainThread extends AppCompatActivity {


    @BindView(R.id.sketchView)
    SketchView sketchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.childthreadsendmsg2mainthread);
        ButterKnife.bind(this);
        sketchView.startAnimation();
        /*开启一个子线程 */
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                message.obj = "我是李晨奇";
                handler.sendMessage(message);
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String content = (String) msg.obj;
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), content);
                    break;
                case 1:
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "a");
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick(R.id.sketchView)
    public void onViewClicked() {
    }
}
