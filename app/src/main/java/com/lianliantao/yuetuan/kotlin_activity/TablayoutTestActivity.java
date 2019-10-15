package com.lianliantao.yuetuan.kotlin_activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.util.DensityUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TablayoutTestActivity extends BaseTitleActivity {

    private List<Fragment> fragments;
    private String[] titles = {"00:00", "09:00", "00:00", "00:00", "00:00", "00:00", "00:00", "00:00", "00:00", "00:00", "00:00"};
    private String[] status = {"已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始", "已开始"};
    @BindView(R.id.magicindicator)
    MagicIndicator magicindicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private int width;

    @Override
    public int getContainerView() {
        return R.layout.tablayouttestactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("限时抢购");
        width = getResources().getDisplayMetrics().widthPixels * 2 / 5;
        initView();
    }

    private void initView() {
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TestFragment testFragment = new TestFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", titles[i]);
            testFragment.setArguments(bundle);
            fragments.add(testFragment);
        }
        CommonNavigator commonNavigator = new CommonNavigator(getApplicationContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles == null ? 0 : titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.item_custom_tablayout_view);
                TextView time = commonPagerTitleView.findViewById(R.id.time);
                TextView statu = commonPagerTitleView.findViewById(R.id.statu);
                time.setText(titles[index]);
                statu.setText(status[index]);
                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
                    }
                });
                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setColors(new Integer[]{Integer.valueOf(Color.parseColor("#fa6025"))});
                linePagerIndicator.setLineHeight(DensityUtils.dip2px(getApplicationContext(), 46));
                return linePagerIndicator;
            }
        });
        commonNavigator.setLeftPadding(width);
        commonNavigator.setRightPadding(width);
        magicindicator.setNavigator(commonNavigator);
        viewpager.setCurrentItem(0);
        TabLayoutAdapter adapter = new TabLayoutAdapter(fragments, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicindicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicindicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicindicator.onPageScrollStateChanged(state);
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

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
