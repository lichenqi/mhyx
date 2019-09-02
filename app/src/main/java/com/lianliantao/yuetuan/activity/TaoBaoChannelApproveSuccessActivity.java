package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaoBaoChannelApproveSuccessActivity extends BaseTitleActivity {
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sure)
    TextView sure;

    @Override
    public int getContainerView() {
        return R.layout.taobaochannelapprovesuccessactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String avatarUrl = intent.getStringExtra("avatarUrl");
        String nick = intent.getStringExtra("nick");
        Glide.with(getApplicationContext()).load(avatarUrl).into(iv);
        name.setText(nick);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
