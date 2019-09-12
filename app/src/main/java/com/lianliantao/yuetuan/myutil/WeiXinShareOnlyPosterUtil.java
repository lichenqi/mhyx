package com.lianliantao.yuetuan.myutil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.bean.ShopDeailBean;
import com.lianliantao.yuetuan.bean.TKLBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dialogfragment.BaseNiceDialog;
import com.lianliantao.yuetuan.dialogfragment.NiceDialog;
import com.lianliantao.yuetuan.dialogfragment.ViewConvertListener;
import com.lianliantao.yuetuan.dialogfragment.ViewHolder;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.CommonUtil;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.QRCodeUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WeiXinShareOnlyPosterUtil {

    private Context context;
    private List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo;
    private List<Bitmap> bitmapList;
    private AppCompatActivity appCompatActivity;
    private String shareAppType;
    private int widthPixels;
    private int heightPixels;
    private NiceDialog notice_dialog;
    private TextView tv_content;
    private ComponentName comp;
    private List<File> files = new ArrayList<>();

    public WeiXinShareOnlyPosterUtil(Context context, List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo, AppCompatActivity appCompatActivity, String shareAppType) {
        this.context = context;
        this.imgInfo = imgInfo;
        this.shareAppType = shareAppType;
        this.appCompatActivity = appCompatActivity;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
    }

    public void setWeiXinFriendShare() {
        bitmapList = new ArrayList<>();
        notice_dialog = NiceDialog.init();
        notice_dialog.setLayoutId(R.layout.photo_save_process_dialog);
        notice_dialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                tv_content = holder.getView(R.id.tv_content);
            }
        });
        notice_dialog.setMargin(100);
        notice_dialog.show(appCompatActivity.getSupportFragmentManager());
        for (int i = 0; i < imgInfo.size(); i++) {
            String itemId = imgInfo.get(i).getItemId();
            getGoodDetailData(itemId);
        }
    }

    /*商品详情接口*/
    private void getGoodDetailData(String itemId) {
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
                            ShopDeailBean.GoodsDetailBean goodsDetail = bean.getGoodsDetail();
                            getTKLData(goodsDetail);/*获取淘口令接口*/
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
    private void getTKLData(ShopDeailBean.GoodsDetailBean goodsDetail) {
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
                            String tklBeanUrl = tklBean.getUrl();
                            String shareUrl = tklBean.getShareUrl();
                            if (shareAppType.contains("WChat")) {
                                initPosterData(tklBeanUrl, goodsDetail);
                            } else {
                                initPosterData(shareUrl, goodsDetail);
                            }
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

    /*二维码布局填充*/
    private void initPosterData(String tklBeanUrl, ShopDeailBean.GoodsDetailBean goodsDetail) {
        View viewPoster = LayoutInflater.from(context).inflate(R.layout.buidfirstposter, null);
        TextView tvtitle = viewPoster.findViewById(R.id.title);
        TextView tvSalePrice = viewPoster.findViewById(R.id.tvSalePrice);
        TextView tvOldPrice = viewPoster.findViewById(R.id.tvOldPrice);
        TextView tvCoupon = viewPoster.findViewById(R.id.tvCoupon);
        RoundedImageView ivBig = viewPoster.findViewById(R.id.ivBig);
        RelativeLayout reIvBig = viewPoster.findViewById(R.id.reIvBig);
        TextView yellowPrice = viewPoster.findViewById(R.id.yellowPrice);
        RoundedImageView qrcodeIv = viewPoster.findViewById(R.id.qrcodeIv);
        IconAndTextGroupUtil.setTextView(context, tvtitle, goodsDetail.getTitle(), goodsDetail.getUserType());
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
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(tklBeanUrl, DensityUtils.dip2px(context, 100));
        qrcodeIv.setImageBitmap(qrCodeBitmap);
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels - DensityUtils.dip2px(context, 20);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivBig.getLayoutParams();
        layoutParams.height = widthPixels;
        ivBig.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParamsRe = (LinearLayout.LayoutParams) reIvBig.getLayoutParams();
        layoutParamsRe.height = widthPixels;
        reIvBig.setLayoutParams(layoutParamsRe);
        Glide.with(context).load(goodsDetail.getPictUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ivBig.setImageBitmap(resource);
                viewSaveToImage(viewPoster);
            }
        });
    }

    private void viewSaveToImage(View view) {
        Bitmap hebingBitmap = MyBitmapUtil.createBitmapOfNew(context, view, widthPixels, heightPixels);
        bitmapList.add(hebingBitmap);
        File file_one = CommonUtil.saveFile(hebingBitmap, context);
        files.add(file_one);
        tv_content.setText("正在保存第" + bitmapList.size() + "张图 ");
        if (imgInfo.size() == bitmapList.size()) {
            if (shareAppType.equals("WChatFriend")) {
                tv_content.setText("正在唤起微信客户端");
            } else {
                tv_content.setText("正在唤起QQ客户端");
            }
            /*唤起微信好友分享*/
            arouseWeiXinApp();
        }
    }

    private void arouseWeiXinApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                if (shareAppType.equals("WChatFriend")) {
                    comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                } else {
                    comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
                }
                intent.setComponent(comp);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");
                intent.putExtra("Kdescription", "");
                ArrayList<Uri> imageUris = new ArrayList<>();
                for (File f : files) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//android 7.0以下
                        imageUris.add(Uri.fromFile(f));
                    } else {   //android 7.0及以上
                        Uri uri = null;
                        try {
                            uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), f.getName(), null));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        imageUris.add(uri);
                    }
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notice_dialog.dismiss();
            }
        }).start();
    }
}
