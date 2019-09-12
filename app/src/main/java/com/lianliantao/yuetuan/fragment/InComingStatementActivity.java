package com.lianliantao.yuetuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InComingStatementActivity extends OriginalActivity {

    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String[] titles = {"今日", "昨日", "本月", "上月"};
    private List<Fragment> fragments;
    private TabLayoutAdapter adapter;
    Bundle bundle;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneTopStyleUtil.setPhoneStatusTheme(this, 0);
        PhoneTopStyleUtil.setBottomNavigationBarColor(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        int bottomStatusHeight = PhoneTopStyleUtil.getBottomStatusHeight(getApplicationContext());
        setContentView(R.layout.incomingstatementactivity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        String income = intent.getStringExtra("income");
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        initView();
        money.setText(income + "元");
    }

    @OnClick({R.id.back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void initView() {
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            IncomingStatementFragment incomingStatementFragment = new IncomingStatementFragment();
            bundle = new Bundle();
            bundle.putString("type", String.valueOf(i + 1));
            incomingStatementFragment.setArguments(bundle);
            fragments.add(incomingStatementFragment);
        }
        adapter = new TabLayoutAdapter(fragments, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(position);
        viewpager.setOffscreenPageLimit(fragments.size());
    }

    private class TabLayoutAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public TabLayoutAdapter(List<Fragment> fragments, FragmentManager fm) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles == null ? 0 : titles.length;
        }
    }

}
