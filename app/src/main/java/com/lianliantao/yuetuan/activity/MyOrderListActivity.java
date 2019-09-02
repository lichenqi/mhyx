package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrderListActivity extends BaseTitleActivity {
    @BindView(R.id.tvLook)
    TextView tvLook;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String[] titles = {"全部", "已付款", "已收货", "已结算", "已失效"};
    private List<Fragment> fragments;
    private TabLayoutAdapter adapter;
    Bundle bundle;
    String type;

    @Override
    public int getContainerView() {
        return R.layout.myorderlistactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        if (type.equals("1")) {
            setMiddleTitle("我的订单");
        } else {
            setMiddleTitle("团队订单");
        }
        initView();
    }

    @OnClick({R.id.tvLook})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvLook:
                Intent intent = new Intent(getApplicationContext(), WithdrawTimeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initView() {
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            OrderFragment orderFragment = new OrderFragment();
            bundle = new Bundle();
            bundle.putString("tkStatus", String.valueOf(i));
            bundle.putString("type", type);
            orderFragment.setArguments(bundle);
            fragments.add(orderFragment);
        }
        adapter = new TabLayoutAdapter(fragments, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
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
