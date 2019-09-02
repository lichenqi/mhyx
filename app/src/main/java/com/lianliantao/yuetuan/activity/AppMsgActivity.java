package com.lianliantao.yuetuan.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.fragment.AppMsgFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppMsgActivity extends BaseTitleActivity {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String[] titles = {"官方通知", "订单通知"};
    private List<Fragment> fragments;
    private TabLayoutAdapter adapter;
    AppMsgFragment fragment;
    Bundle bundle;

    @Override
    public int getContainerView() {
        return R.layout.appmsgactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("消息");
        initView();
    }

    private void initView() {
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            fragment = new AppMsgFragment();
            bundle = new Bundle();
            bundle.putString("type", String.valueOf(i + 1));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        adapter = new TabLayoutAdapter(fragments, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(fragments.size());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    EventBus.getDefault().post("appMsgUpdate");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
