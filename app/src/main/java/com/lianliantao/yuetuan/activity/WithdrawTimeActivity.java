package com.lianliantao.yuetuan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WithdrawTimeActivity extends OriginalActivity {

    @BindView(R.id.viewHeight)
    View viewHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneTopStyleUtil.setPhoneStatusTheme(this, 0);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.withdrawtimeactivity);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
    }

    public void onBack(View view) {
        finish();
    }
}
