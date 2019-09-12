package com.lianliantao.yuetuan.myutil;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
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
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.PosterPhotoSaveUtil;
import com.lianliantao.yuetuan.util.QRCodeUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SaveFirstPosterAndImageUrlUtil {

    private Context context;
    private List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo;
    private String itemId;
    private AppCompatActivity activity;
    private List<Bitmap> firstIsPhotoBitmap;
    private NiceDialog notice_dialog, openWeiXinDialog;
    private TextView tv_content;
    private int widthPixels;
    private int heightPixels;
    private String type;

    public SaveFirstPosterAndImageUrlUtil(Context context, List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo, String itemId, AppCompatActivity activity, String type) {
        this.context = context;
        this.activity = activity;
        this.imgInfo = imgInfo;
        this.itemId = itemId;
        this.type = type;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
    }

    public void setSave() {
        firstIsPhotoBitmap = new ArrayList<>();
        /*提示图片保存进度*/
        notice_dialog = NiceDialog.init();
        notice_dialog.setLayoutId(R.layout.photo_save_process_dialog);
        notice_dialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                tv_content = holder.getView(R.id.tv_content);
            }
        });
        notice_dialog.setMargin(100);
        notice_dialog.show(activity.getSupportFragmentManager());
        getGoodDetailData();
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

    /*淘口令接口*/
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
                            if (type.contains("WChat")) {
                                initSecondModePosterData(tklBeanUrl, goodsDetail);
                            } else {
                                initSecondModePosterData(shareUrl, goodsDetail);
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

    /*二维码合成*/
    private void initSecondModePosterData(String tklBeanUrl, ShopDeailBean.GoodsDetailBean goodsDetail) {
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
                viewSecondModeSaveToImage(viewPoster);
            }
        });
    }

    private void viewSecondModeSaveToImage(View viewPoster) {
        Bitmap hebingBitmap = MyBitmapUtil.createBitmapOfNew(context, viewPoster, widthPixels, heightPixels);
        firstIsPhotoBitmap.add(hebingBitmap);
        PosterPhotoSaveUtil.saveBitmap2file(hebingBitmap, context);
        tv_content.setText("正在保存第" + firstIsPhotoBitmap.size() + "张图");
        saveMorePhotoToLocal();
    }

    /*批量下载图片*/     /*网络路劲存储*/
    private void saveMorePhotoToLocal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl;
                try {
                    for (int i = 1; i < imgInfo.size(); i++) {
                        imageurl = new URL(imgInfo.get(i).getUrl());
                        HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        Message msg = new Message();
                        // 把bm存入消息中,发送到主线程
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            PosterPhotoSaveUtil.saveBitmap2file(bitmap, context);
            firstIsPhotoBitmap.add(bitmap);
            tv_content.setText("正在保存第" + firstIsPhotoBitmap.size() + "张图");
            if (firstIsPhotoBitmap.size() == imgInfo.size()) {
                notice_dialog.dismiss();
                gotoWeiXinAppDialog();
            }
        }
    };

    /*去微信提示框*/
    private void gotoWeiXinAppDialog() {
        openWeiXinDialog = NiceDialog.init();
        openWeiXinDialog.setLayoutId(R.layout.goto_weixin_dialog);
        openWeiXinDialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                TextView cancel = holder.getView(R.id.cancel);
                TextView open = holder.getView(R.id.open);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWeiXinDialog.dismiss();
                    }
                });
                open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWeiXinDialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(cmp);
                        context.startActivity(intent);
                    }
                });
            }
        });
        openWeiXinDialog.setMargin(100);
        openWeiXinDialog.show(activity.getSupportFragmentManager());
        openWeiXinDialog.setOutCancel(false);
        openWeiXinDialog.setCancelable(false);
    }

}
