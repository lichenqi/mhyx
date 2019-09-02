package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.util.StatusBarUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageUrlBigLookActivity extends OriginalActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.notice)
    TextView notice;
    private List<CircleRecommendBean.InfoBean.ImgInfoBean> list;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.shareimagebiglookactivity);
        ButterKnife.bind(this);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivBack.getLayoutParams();
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        ivBack.setLayoutParams(layoutParams);
        Intent intent = getIntent();
        list = (List<CircleRecommendBean.InfoBean.ImgInfoBean>) intent.getSerializableExtra("imageList");
        position = intent.getIntExtra("position", 0);
        initViewPager();
    }

    private void initViewPager() {
        viewpager.setAdapter(new ImageviewAdapter());
        viewpager.setCurrentItem(position);
        notice.setText((position + 1) + "/" + list.size());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                notice.setText((position + 1) + "/" + list.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class ImageviewAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(getApplicationContext()).load(list.get(position).getUrl()).into(imageView);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            return imageView;
        }
    }

    @OnClick({R.id.ivBack})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
