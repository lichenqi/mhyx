package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.fragment.GoodBigPosterPhotoLookFragment;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.util.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MultiGoodsCompoundPhotoActivity extends OriginalActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.lookgooddetail)
    TextView lookgooddetail;
    @BindView(R.id.notice)
    TextView notice;
    List<CircleRecommendBean.InfoBean.ImgInfoBean> list;
    int positionWhich;
    private List<Fragment> fragments;
    Bundle bundle;

    // 声明一个订阅方法，用于接收事件
    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case "goodbigfinish":
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.multigoodscompoundphotoactivity);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivBack.getLayoutParams();
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        ivBack.setLayoutParams(layoutParams);
        Intent intent = getIntent();
        list = (List<CircleRecommendBean.InfoBean.ImgInfoBean>) intent.getSerializableExtra("imageList");
        positionWhich = intent.getIntExtra("position", 0);
        initViewPager();
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            GoodBigPosterPhotoLookFragment orderFragment = new GoodBigPosterPhotoLookFragment();
            bundle = new Bundle();
            bundle.putString("itemId", list.get(i).getItemId());
            bundle.putString("imgUrl", list.get(i).getUrl());
            orderFragment.setArguments(bundle);
            fragments.add(orderFragment);
        }
        viewpager.setAdapter(new MyPagerAdapter(fragments, getSupportFragmentManager()));
        viewpager.setCurrentItem(positionWhich);
        viewpager.setOffscreenPageLimit(fragments.size());
        notice.setText((positionWhich + 1) + "/" + list.size());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positionWhich = position;
                notice.setText((position + 1) + "/" + list.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public MyPagerAdapter(List<Fragment> fragments, FragmentManager fm) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }
    }

    @OnClick({R.id.ivBack, R.id.lookgooddetail})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.lookgooddetail:
                String itemId = list.get(positionWhich).getItemId();
                JumpUtil.jump2ShopDetail(getApplicationContext(), itemId);
                break;
        }
    }

}
