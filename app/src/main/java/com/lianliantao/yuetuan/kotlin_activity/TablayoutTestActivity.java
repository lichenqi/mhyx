package com.lianliantao.yuetuan.kotlin_activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TablayoutTestActivity extends BaseTitleActivity {

    private List<Fragment> fragments;
    private String[] titles = {"00:00", "09:00", "10:00", "12:00", "15:00", "17:00", "20:00", "00:00", "09:00", "10:00", "12:00", "15:00", "17:00", "20:00"};
    private String[] status = {"已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始"};
    private TabLayoutAdapter adapter;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    public int getContainerView() {
        return R.layout.tablayouttestactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("限时抢购");
        initView();
    }

    private void initView() {
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TestFragment orderFragment = new TestFragment();
            fragments.add(orderFragment);
        }
        adapter = new TabLayoutAdapter(fragments, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(fragments.size());
        for (int i = 0; i < adapter.getCount(); i++) {
            TabLayout.Tab tabAt = tabLayout.getTabAt(i);
            tabAt.setCustomView(R.layout.item_custom_tablayout_view);
            View customView = tabAt.getCustomView();
            TextView time = customView.findViewById(R.id.time);
            TextView statu = customView.findViewById(R.id.statu);
            LinearLayout llParent = customView.findViewById(R.id.llParent);
            time.setText(titles[i]);
            statu.setText(status[i]);
            llParent.setBackgroundColor(0xff000000);
            if (i == 0) {
                llParent.setBackgroundColor(0xfffa6025);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.llParent).setBackgroundColor(0xfffa6025);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.llParent).setBackgroundColor(0xff000000);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
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
