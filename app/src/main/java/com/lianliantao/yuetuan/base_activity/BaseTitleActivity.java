package com.lianliantao.yuetuan.base_activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;

/*公共的头部封装*/
public abstract class BaseTitleActivity extends OriginalActivity {

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_right_name;
    private ImageView iv_right;
    private FrameLayout fl_container;
    private RelativeLayout rl_parent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basetitleactivity);
        rl_parent = findViewById(R.id.rl_parent);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_right_name = findViewById(R.id.tv_right_name);
        iv_right = findViewById(R.id.iv_right);
        fl_container = findViewById(R.id.fl_container);
        View view = getLayoutInflater().inflate(getContainerView(), null);
        fl_container.addView(view);
    }

    public abstract int getContainerView();

    public void setTitleBackgroudColor(int color) {
        rl_parent.setBackgroundColor(color);
    }

    public void setMiddleTitle(String name) {
        tv_title.setText(name);
    }

    public void setMiddleColor(int color) {
        tv_title.setTextColor(color);
    }

    public void setRightTitle(String name) {
        tv_right_name.setText(name);
    }

    public void setRightColor(int color) {
        tv_right_name.setTextColor(color);
    }

    public void onBack(View view) {
        finish();
    }

    public void setRightImageView(int images) {
        iv_right.setImageResource(images);
    }

    public void setLeftImageView(int images) {
        iv_back.setImageResource(images);
    }

    public void setRightIVVisible() {
        iv_right.setVisibility(View.VISIBLE);
    }

    public void setRightIVGone() {
        iv_right.setVisibility(View.GONE);
    }

    public void setRightTVVisible() {
        tv_right_name.setVisibility(View.VISIBLE);
    }

    public void setRightTVGone() {
        tv_right_name.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
