package com.lianliantao.yuetuan.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.bean.ShopDeailBean;
import com.lianliantao.yuetuan.bean.TKLBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.MyBitmapUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.QRCodeUtil;
import com.lianliantao.yuetuan.util.StatusBarUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Circle2PhotoLookActivity extends OriginalActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.notice)
    TextView notice;
    private String itemId;
    List<CircleRecommendBean.InfoBean.ImgInfoBean> list;
    int position;
    private ShopDeailBean.GoodsDetailBean goodsDetail;
    String tklBeanUrl;/*二维码内容*/
    View viewPoster;
    RoundedImageView ivBig;
    private TextView tvtitle;
    private TextView tvSalePrice;
    private TextView tvOldPrice;
    private TextView tvCoupon;
    private RelativeLayout reIvBig;
    private TextView yellowPrice;
    private RoundedImageView qrcodeIv;
    Dialog loadingDialog;

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
        itemId = intent.getStringExtra("itemId");
        list = (List<CircleRecommendBean.InfoBean.ImgInfoBean>) intent.getSerializableExtra("imageList");
        position = intent.getIntExtra("position", 0);
        buidFirstPoster();
        getGoodDetailData();/*商品详情接口*/
    }

    /*生成第一张海报图*/
    private void buidFirstPoster() {
        viewPoster = LayoutInflater.from(getApplicationContext()).inflate(R.layout.buidfirstposter, null);
        tvtitle = viewPoster.findViewById(R.id.title);
        tvSalePrice = viewPoster.findViewById(R.id.tvSalePrice);
        tvOldPrice = viewPoster.findViewById(R.id.tvOldPrice);
        tvCoupon = viewPoster.findViewById(R.id.tvCoupon);
        ivBig = viewPoster.findViewById(R.id.ivBig);
        reIvBig = viewPoster.findViewById(R.id.reIvBig);
        yellowPrice = viewPoster.findViewById(R.id.yellowPrice);
        qrcodeIv = viewPoster.findViewById(R.id.qrcodeIv);
    }

    /*商品详情接口*/
    private void getGoodDetailData() {
        loadingDialog = DialogUtil.createLoadingDialog(Circle2PhotoLookActivity.this, "加载中...");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.SHOPDETAILAPI + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("商品详情", response.toString());
                        ShopDeailBean bean = GsonUtil.GsonToBean(response.toString(), ShopDeailBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            goodsDetail = bean.getGoodsDetail();
                            getTKLData();/*获取淘口令接口*/
                        } else {
                            ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                            DialogUtil.closeDialog(loadingDialog, Circle2PhotoLookActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, Circle2PhotoLookActivity.this);
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    /*获取淘口令接口*/
    private void getTKLData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", goodsDetail.getItemId());
        map.put("text", goodsDetail.getTitle());
        map.put("url", goodsDetail.getCouponClickUrl());
        map.put("logo", goodsDetail.getPictUrl());
        map.put("payPrice", MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getPayPrice()));
        map.put("zkFinalPrice", MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getZkFinalPrice()));
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.TKLDATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        DialogUtil.closeDialog(loadingDialog, Circle2PhotoLookActivity.this);
                        Log.i("淘口令接口", response.toString());
                        TKLBean tklBean = GsonUtil.GsonToBean(response.toString(), TKLBean.class);
                        if (tklBean.getErrno() == CommonApi.RESULTCODEOK) {
                            tklBeanUrl = tklBean.getUrl();
                            initPosterData();
                        } else {
                            ToastUtils.showToast(getApplicationContext(), tklBean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                        DialogUtil.closeDialog(loadingDialog, Circle2PhotoLookActivity.this);
                    }
                });
    }

    /*二维码海报赋值*/
    private void initPosterData() {
        IconAndTextGroupUtil.setTextView(getApplicationContext(), tvtitle, goodsDetail.getTitle(), goodsDetail.getUserType());
        tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getPayPrice()));
        tvOldPrice.setText("¥" + MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getZkFinalPrice()));
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvOldPrice.getPaint().setAntiAlias(true);// 抗锯齿
        String couponAmount = goodsDetail.getCouponAmount();
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
        yellowPrice.setText(MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getPayPrice()));
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(tklBeanUrl, DensityUtils.dip2px(getApplicationContext(), 100));
        qrcodeIv.setImageBitmap(qrCodeBitmap);
        int widthPixels = getResources().getDisplayMetrics().widthPixels - DensityUtils.dip2px(getApplicationContext(), 20);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivBig.getLayoutParams();
        layoutParams.height = widthPixels;
        ivBig.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParamsRe = (LinearLayout.LayoutParams) reIvBig.getLayoutParams();
        layoutParamsRe.height = widthPixels;
        reIvBig.setLayoutParams(layoutParamsRe);
        Glide.with(getApplicationContext()).load(list.get(0).getUrl()).asBitmap().into(target);
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
            if (position == 0) {
                imageView.setImageBitmap(hebingBitmap);
            } else {
                Glide.with(getApplicationContext()).load(list.get(position).getUrl()).into(imageView);
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
