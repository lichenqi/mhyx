package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.ShopDeailBean;
import com.lianliantao.yuetuan.bean.TKLBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.QRCodeUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodBigPosterPhotoLookFragment extends LazyBaseFragment {

    Context context;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvSalePrice)
    TextView tvSalePrice;
    @BindView(R.id.tvOldPrice)
    TextView tvOldPrice;
    @BindView(R.id.tvCoupon)
    TextView tvCoupon;
    @BindView(R.id.ivBig)
    RoundedImageView ivBig;
    @BindView(R.id.yellowPrice)
    TextView yellowPrice;
    @BindView(R.id.reIvBig)
    RelativeLayout reIvBig;
    @BindView(R.id.qrcodeIv)
    RoundedImageView qrcodeIv;
    String itemId, imgUrl;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.llContentParent)
    LinearLayout llContentParent;
    @BindView(R.id.reParent)
    RelativeLayout reParent;
    private ShopDeailBean.GoodsDetailBean goodsDetail;
    String tklBeanUrl;/*二维码内容*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            itemId = arguments.getString("itemId");
            imgUrl = arguments.getString("imgUrl");
        }
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this, getView());
        context = MyApplication.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        getGoodDetailData();
    }

    @OnClick({R.id.reParent})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.reParent:
                EventBus.getDefault().post("goodbigfinish");
                break;
        }
    }

    /*商品详情接口*/
    private void getGoodDetailData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
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
                            ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
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
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.TKLDATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("淘口令接口", response.toString());
                        TKLBean tklBean = GsonUtil.GsonToBean(response.toString(), TKLBean.class);
                        if (tklBean.getErrno() == CommonApi.RESULTCODEOK) {
                            tklBeanUrl = tklBean.getUrl();
                            initPosterData();
                        } else {
                            ToastUtils.showToast(context, tklBean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    /*二维码海报赋值*/
    private void initPosterData() {
        IconAndTextGroupUtil.setTextView(context, title, goodsDetail.getTitle(), goodsDetail.getUserType());
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
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(tklBeanUrl, DensityUtils.dip2px(context, 80));
        qrcodeIv.setImageBitmap(qrCodeBitmap);
        int widthPixels = getResources().getDisplayMetrics().widthPixels - DensityUtils.dip2px(context, 90);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivBig.getLayoutParams();
        layoutParams.height = widthPixels;
        ivBig.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParamsRe = (LinearLayout.LayoutParams) reIvBig.getLayoutParams();
        layoutParamsRe.height = widthPixels;
        reIvBig.setLayoutParams(layoutParamsRe);
        Glide.with(context).load(goodsDetail.getPictUrl()).into(ivBig);
        progressBar.setVisibility(View.GONE);
        llContentParent.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.goodbigposterphotolookfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }
}
