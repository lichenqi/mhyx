package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.myutil.MyBitmapUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.QRCodeUtil;
import com.lianliantao.yuetuan.util.StatusBarUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareImageBigLookActivity extends OriginalActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.notice)
    TextView notice;
    String imagesList, payPrice, title, zkFinalPrice, couponAmount, userType, tklBeanUrl;
    int position;
    RoundedImageView ivBig;
    String[] split;
    View viewPoster;
    private List<String> photoList;
    private String pictUrl;

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
        position = intent.getIntExtra("position", 0);
        imagesList = intent.getStringExtra("imagesList");
        pictUrl = intent.getStringExtra("pictUrl");
        title = intent.getStringExtra("title");
        payPrice = intent.getStringExtra("payPrice");
        zkFinalPrice = intent.getStringExtra("zkFinalPrice");
        couponAmount = intent.getStringExtra("couponAmount");
        userType = intent.getStringExtra("userType");
        tklBeanUrl = intent.getStringExtra("tklBeanUrl");
        photoList = new ArrayList<>();
        if (TextUtils.isEmpty(imagesList)) {
            photoList.add(pictUrl);
        } else {
            split = imagesList.split(",");
            for (int i = 0; i < split.length; i++) {
                photoList.add(split[i]);
            }
        }
        buidFirstPoster();
    }

    /*生成第一张海报图*/
    private void buidFirstPoster() {
        viewPoster = LayoutInflater.from(getApplicationContext()).inflate(R.layout.buidfirstposter, null);
        TextView tvtitle = viewPoster.findViewById(R.id.title);
        TextView tvSalePrice = viewPoster.findViewById(R.id.tvSalePrice);
        TextView tvOldPrice = viewPoster.findViewById(R.id.tvOldPrice);
        TextView tvCoupon = viewPoster.findViewById(R.id.tvCoupon);
        ivBig = viewPoster.findViewById(R.id.ivBig);
        RelativeLayout reIvBig = viewPoster.findViewById(R.id.reIvBig);
        TextView yellowPrice = viewPoster.findViewById(R.id.yellowPrice);
        RoundedImageView qrcodeIv = viewPoster.findViewById(R.id.qrcodeIv);
        IconAndTextGroupUtil.setTextView(getApplicationContext(), tvtitle, title, userType);
        tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(payPrice));
        tvOldPrice.setText("¥" + MoneyFormatUtil.StringFormatWithYuan(zkFinalPrice));
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvOldPrice.getPaint().setAntiAlias(true);// 抗锯齿
        if (!TextUtils.isEmpty(couponAmount)) {
            if (Integer.valueOf(couponAmount) > 0) {
                tvCoupon.setVisibility(View.VISIBLE);
                tvCoupon.setText(couponAmount + "元券");
            } else {
                tvCoupon.setVisibility(View.GONE);
            }
        } else {
            tvCoupon.setVisibility(View.GONE);
        }
        yellowPrice.setText(MoneyFormatUtil.StringFormatWithYuan(payPrice));
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(tklBeanUrl, DensityUtils.dip2px(getApplicationContext(), 100));
        qrcodeIv.setImageBitmap(qrCodeBitmap);
        int widthPixels = getResources().getDisplayMetrics().widthPixels - DensityUtils.dip2px(getApplicationContext(), 20);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivBig.getLayoutParams();
        layoutParams.height = widthPixels;
        ivBig.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParamsRe = (LinearLayout.LayoutParams) reIvBig.getLayoutParams();
        layoutParamsRe.height = widthPixels;
        reIvBig.setLayoutParams(layoutParamsRe);
        Glide.with(getApplicationContext()).load(photoList.get(0)).asBitmap().into(target);
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            ivBig.setImageBitmap(bitmap);
            viewSaveToImage(viewPoster);
        }
    };

    Bitmap hebingBitmap;

    private void viewSaveToImage(View view) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        hebingBitmap = MyBitmapUtil.createBitmapOfNew(getApplicationContext(), view, width, height);
        initViewPager();
    }

    private void initViewPager() {
        viewpager.setAdapter(new ImageviewAdapter());
        viewpager.setCurrentItem(position);
        notice.setText((position + 1) + "/" + photoList.size());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                notice.setText((position + 1) + "/" + photoList.size());
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
            return photoList == null ? 0 : photoList.size();
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
            if (position == 0) {
                imageView.setImageBitmap(hebingBitmap);
            } else {
                Glide.with(getApplicationContext()).load(photoList.get(position)).into(imageView);
            }
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
