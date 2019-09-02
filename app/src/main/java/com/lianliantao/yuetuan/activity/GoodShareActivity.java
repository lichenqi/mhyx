package com.lianliantao.yuetuan.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lianliantao.yuetuan.MainActivity;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.HorizontalImagesAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.ShareSelectBean;
import com.lianliantao.yuetuan.bean.TKLBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.itemdecoration.PinLeiItemDecoration;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.myutil.MyBitmapUtil;
import com.lianliantao.yuetuan.photo_dispose_util.ShareManager;
import com.lianliantao.yuetuan.photo_dispose_util.Tools;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.CommonUtil;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodShareActivity extends BaseTitleActivity {

    @BindView(R.id.copyTKL)
    TextView copyTKL;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ed_input_content)
    EditText ed_input_content;
    String imagesList, title, payPrice, zkFinalPrice, volume, estimatedEarn, couponAmount, userType, itemId, pictUrl, couponClickUrl;
    @BindView(R.id.title)
    TextView tvtitle;
    @BindView(R.id.tvSalePrice)
    TextView tvSalePrice;
    @BindView(R.id.tvOldPrice)
    TextView tvOldPrice;
    @BindView(R.id.saleNum)
    TextView saleNum;
    @BindView(R.id.tvCoupon)
    TextView tvCoupon;
    @BindView(R.id.estimateMoney)
    TextView estimateMoney;
    @BindView(R.id.wcaht_friend)
    LinearLayout wcahtFriend;
    @BindView(R.id.wchat_circle)
    LinearLayout wchatCircle;
    @BindView(R.id.weibo)
    LinearLayout weibo;
    @BindView(R.id.qq)
    LinearLayout qq;
    @BindView(R.id.qqZone)
    LinearLayout qqZone;
    @BindView(R.id.fzwb)
    LinearLayout fzwb;
    @BindView(R.id.bctp)
    LinearLayout bctp;
    @BindView(R.id.qhsy)
    LinearLayout qhsy;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    String tklContent, tklBeanUrl;
    ClipboardManager cm;
    Dialog dialog;
    LinkedHashMap<Integer, Integer> saveSelectedPosition = new LinkedHashMap<>();
    private List<Integer> selectedPosition;
    private View viewPoster;
    RoundedImageView ivBig, qrcodeIv;
    Dialog loadingDialog;
    /*选中的图片路劲集合*/
    private List<String> selectedImagesList;
    List<ShareSelectBean> list;
    /*朋友圈分享请求权限码*/
    private int WCHATCILCLE_CODE = 1;

    @Override
    public int getContainerView() {
        return R.layout.goodshareactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("商品分享");
        Intent intent = getIntent();
        itemId = intent.getStringExtra("itemId");
        imagesList = intent.getStringExtra("imagesList");
        title = intent.getStringExtra("title");
        payPrice = intent.getStringExtra("payPrice");
        zkFinalPrice = intent.getStringExtra("zkFinalPrice");
        volume = intent.getStringExtra("volume");
        estimatedEarn = intent.getStringExtra("estimatedEarn");
        couponAmount = intent.getStringExtra("couponAmount");
        userType = intent.getStringExtra("userType");
        pictUrl = intent.getStringExtra("pictUrl");
        couponClickUrl = intent.getStringExtra("couponClickUrl");
        initRecyclerview();
        initEditTextView();
        getTKLData();/*获取淘口令接口*/
        /*初始化默认海报*/
        initViewPoster();
    }

    private void initViewPoster() {
        viewPoster = LayoutInflater.from(getApplicationContext()).inflate(R.layout.buidfirstposter, null);
        TextView tvtitle = viewPoster.findViewById(R.id.title);
        TextView tvSalePrice = viewPoster.findViewById(R.id.tvSalePrice);
        TextView tvOldPrice = viewPoster.findViewById(R.id.tvOldPrice);
        TextView tvCoupon = viewPoster.findViewById(R.id.tvCoupon);
        ivBig = viewPoster.findViewById(R.id.ivBig);
        RelativeLayout reIvBig = viewPoster.findViewById(R.id.reIvBig);
        TextView yellowPrice = viewPoster.findViewById(R.id.yellowPrice);
        qrcodeIv = viewPoster.findViewById(R.id.qrcodeIv);
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
        int widthPixels = getResources().getDisplayMetrics().widthPixels - DensityUtils.dip2px(getApplicationContext(), 20);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivBig.getLayoutParams();
        layoutParams.height = widthPixels;
        ivBig.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParamsRe = (LinearLayout.LayoutParams) reIvBig.getLayoutParams();
        layoutParamsRe.height = widthPixels;
        reIvBig.setLayoutParams(layoutParamsRe);
    }

    /*图片水平滑动*/
    private void initRecyclerview() {
        list = new ArrayList<>();
        String[] split = imagesList.split(",");
        ShareSelectBean bean;
        for (int i = 0; i < split.length; i++) {
            bean = new ShareSelectBean();
            bean.setUrl(split[i]);
            list.add(bean);
        }
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(manager);
        recyclerview.addItemDecoration(new PinLeiItemDecoration(DensityUtils.dip2px(getApplicationContext(), 10), split));
        HorizontalImagesAdapter adapter = new HorizontalImagesAdapter(getApplicationContext(), list);
        recyclerview.setAdapter(adapter);
        list.get(0).setSelected(true);
        adapter.notifyDataSetChanged();
        saveSelectedPosition.put(0, 0);
        /*选中点击*/
        adapter.setOnselectClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                if (list.get(position).isSelected()) {
                    saveSelectedPosition.remove(position);
                    list.get(position).setSelected(false);
                } else {
                    saveSelectedPosition.put(position, position);
                    list.get(position).setSelected(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
        /*图片点击*/
        adapter.setOnIvClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), ShareImageBigLookActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("imagesList", imagesList);
                intent.putExtra("title", title);
                intent.putExtra("payPrice", payPrice);
                intent.putExtra("zkFinalPrice", zkFinalPrice);
                intent.putExtra("couponAmount", couponAmount);
                intent.putExtra("userType", userType);
                intent.putExtra("tklBeanUrl", tklBeanUrl);
                startActivity(intent);
            }
        });
    }

    /*基本信息初始化*/
    private void initEditTextView() {
        String priceS = MoneyFormatUtil.StringFormatWithYuan(payPrice);
        String priceO = MoneyFormatUtil.StringFormatWithYuan(zkFinalPrice);
        String content = title + "\n" + "【价格】" + priceS + "元\n【券后价】" + priceO + "元\n【下载麻花优选再省】" +
                estimatedEarn + "元\n-------------\n请识别图片二维码下单";
        ed_input_content.setText(content);
        ed_input_content.setSelection(content.length());
        IconAndTextGroupUtil.setTextView(getApplicationContext(), tvtitle, title, userType);
        tvSalePrice.setText(priceS);
        tvOldPrice.setText("¥" + priceO);
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        saleNum.setText(NumUtil.getNum(volume) + "人购买");
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
        estimateMoney.setText("预估赚 ¥ " + estimatedEarn);
    }

    @OnClick({R.id.copyTKL, R.id.wcaht_friend, R.id.wchat_circle, R.id.qq, R.id.qqZone, R.id.fzwb, R.id.bctp, R.id.qhsy})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.copyTKL:
                if (!TextUtils.isEmpty(tklContent)) {
                    showTKLDialog(tklContent);
                }
                break;
            case R.id.wcaht_friend:/*微信好友*/
                if (ContextCompat.checkSelfPermission(GoodShareActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(GoodShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有存储权限
                    ActivityCompat.requestPermissions(GoodShareActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    commonSelectImgFunction("WChatFriend");
                }
                break;
            case R.id.wchat_circle:/*微信朋友圈*/
                if (ContextCompat.checkSelfPermission(GoodShareActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(GoodShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有存储权限
                    ActivityCompat.requestPermissions(GoodShareActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    /*朋友圈分享*/
                    commonSelectImgFunction("WChatCircle");
                }
                break;
            case R.id.qq:/*QQ好友分享*/
                if (ContextCompat.checkSelfPermission(GoodShareActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(GoodShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有存储权限
                    ActivityCompat.requestPermissions(GoodShareActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                } else {
                    commonSelectImgFunction("QQFriend");
                }
                break;
            case R.id.qqZone:/*QQ空间分享*/

                break;
            case R.id.fzwb:/*复制文本*/
                copyOfficial();
                break;
            case R.id.bctp:/*保存图片*/
                if (ContextCompat.checkSelfPermission(GoodShareActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(GoodShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有存储权限
                    ActivityCompat.requestPermissions(GoodShareActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
                } else {
                    commonSelectImgFunction("saveImages");
                }
                break;
            case R.id.qhsy:/*返回首页*/
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("loginout", "loginout");
                startActivity(intent);
                finish();
                break;

        }
    }

    /*复制文案*/
    private void copyOfficial() {
        String official_content = ed_input_content.getText().toString();
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", official_content);
        cm.setPrimaryClip(mClipData);
        ClipContentUtil.getInstance(getApplicationContext()).putNewSearch(official_content);//保存记录到数据库
        ToastUtils.showBackgroudCenterToast(getApplicationContext(), "分享文案复制成功");
    }

    /*公共的最后选中图片方法*/
    private void commonSelectImgFunction(String type) {
        selectedPosition = new ArrayList<>();
        Iterator<Map.Entry<Integer, Integer>> iterator = saveSelectedPosition.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> next = iterator.next();
            Integer value = next.getValue();
            selectedPosition.add(value);
        }
        Collections.sort(selectedPosition);
        if (selectedPosition.size() > 0) {
            Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(tklBeanUrl, DensityUtils.dip2px(getApplicationContext(), 100));
            qrcodeIv.setImageBitmap(qrCodeBitmap);
            selectedImagesList = new ArrayList<>();
            for (int i = 0; i < selectedPosition.size(); i++) {
                selectedImagesList.add(list.get(selectedPosition.get(i)).getUrl());
            }
            switch (type) {
                case "WChatFriend":
                    Glide.with(getApplicationContext()).load(selectedImagesList.get(0)).asBitmap().into(target);
                    break;
                case "WChatCircle":
                    Glide.with(getApplicationContext()).load(selectedImagesList.get(0)).asBitmap().into(targetOfWChatCircle);
                    break;
                case "QQFriend":
                    Glide.with(getApplicationContext()).load(selectedImagesList.get(0)).asBitmap().into(targetOfQQFriend);
                    break;
                case "saveImages":
                    Glide.with(getApplicationContext()).load(selectedImagesList.get(0)).asBitmap().into(targetOfSaveImages);
                    break;
            }

        } else {
            ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请至少勾选一张图片分享");
        }
    }

    /*保存图片方法*/
    private SimpleTarget targetOfSaveImages = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            ivBig.setImageBitmap(bitmap);
            viewSaveToImageOfSaveImages(viewPoster);
        }
    };

    private void viewSaveToImageOfSaveImages(View viewPoster) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        Bitmap hebingBitmap = MyBitmapUtil.createBitmapOfNew(getApplicationContext(), viewPoster, width, height);
        if (selectedImagesList.size() == 1) {
            CommonUtil.saveBitmap2file(hebingBitmap, getApplicationContext());
        } else {
            CommonUtil.saveBitmap2file(hebingBitmap, getApplicationContext());
            saveMorePhotoToLocal();
        }
    }

    /*QQ好友的分享方法*/
    private SimpleTarget targetOfQQFriend = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            ivBig.setImageBitmap(bitmap);
            viewSaveToImageOfQQFriend(viewPoster);
        }
    };

    private void viewSaveToImageOfQQFriend(View viewPoster) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        Bitmap hebingBitmap = MyBitmapUtil.createBitmapOfNew(getApplicationContext(), viewPoster, width, height);
        ShareManager shareManager = new ShareManager(GoodShareActivity.this);
        shareManager.setShareImage(hebingBitmap, selectedImagesList, "QQFriend");
    }

    /*微信朋友圈的分享方法*/
    private SimpleTarget targetOfWChatCircle = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            ivBig.setImageBitmap(bitmap);
            viewSaveToImageOfWChatCircle(viewPoster);
        }
    };

    private void viewSaveToImageOfWChatCircle(View view) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        Bitmap hebingBitmap = MyBitmapUtil.createBitmapOfNew(getApplicationContext(), view, width, height);
        if (selectedImagesList.size() == 1) {
            CommonUtil.saveBitmap2file(hebingBitmap, getApplicationContext());
        } else {
            CommonUtil.saveBitmap2file(hebingBitmap, getApplicationContext());
            saveMorePhotoToLocal();
        }
        /*提示保存成功 引导用户去微信朋友圈分享*/
        noticeWChatCircleDialog();
    }

    private void noticeWChatCircleDialog() {
        dialog = new Dialog(GoodShareActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(R.layout.wchatcircle_dialog_pics);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        TextView dismiss = dialog.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView open_wx = dialog.findViewById(R.id.open_wx);
        open_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tools.isAppAvilible(getApplicationContext(), "com.tencent.mm")) {
                    ToastUtils.showToast(getApplicationContext(), "您还没有安装微信客户端,请先安转客户端");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    /*微信好友分享公共方法*/
    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            ivBig.setImageBitmap(bitmap);
            viewSaveToImage(viewPoster);
        }
    };

    private void viewSaveToImage(View view) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        Bitmap hebingBitmap = MyBitmapUtil.createBitmapOfNew(getApplicationContext(), view, width, height);
        ShareManager shareManager = new ShareManager(GoodShareActivity.this);
        shareManager.setShareImage(hebingBitmap, selectedImagesList, "WChatFriend");
    }

    /*淘口令复制成功弹窗*/
    private void showTKLDialog(String text) {
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", text);
        cm.setPrimaryClip(mClipData);
        ClipContentUtil.getInstance(getApplicationContext()).putNewSearch(text);//保存记录到数据库
        dialog = new Dialog(GoodShareActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(R.layout.taokoulingcopydialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        window.setWindowAnimations(R.style.my_dialog_style_animation);
        TextView sure = dialog.findViewById(R.id.sure);
        TextView content = dialog.findViewById(R.id.content);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        content.setText(text);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    /*获取淘口令接口*/
    private void getTKLData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        map.put("text", title);
        map.put("url", couponClickUrl);
        map.put("logo", pictUrl);
        map.put("payPrice", MoneyFormatUtil.StringFormatWithYuan(payPrice));
        map.put("zkFinalPrice", MoneyFormatUtil.StringFormatWithYuan(zkFinalPrice));
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.TKLDATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("淘口令接口", response.toString());
                        TKLBean tklBean = GsonUtil.GsonToBean(response.toString(), TKLBean.class);
                        if (tklBean.getErrno() == CommonApi.RESULTCODEOK) {
                            tklContent = tklBean.getText();
                            tklBeanUrl = tklBean.getUrl();
                        } else {
                            ToastUtils.showToast(getApplicationContext(), tklBean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }

    /*批量下载图片*/     /*网络路劲存储*/
    private void saveMorePhotoToLocal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl;
                try {
                    for (int i = 1; i < selectedImagesList.size(); i++) {
                        imageurl = new URL(selectedImagesList.get(i));
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
            CommonUtil.saveBitmap2file(bitmap, getApplicationContext());
        }
    };

    /*存储权限回调*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:/*微信朋友圈分享*/
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length <= 1 || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    commonSelectImgFunction("WChatCircle");
                }
                break;
            case 4:/*保存图片*/
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length <= 1 || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    commonSelectImgFunction("saveImages");
                }
                break;
            case 1:/*微信好友*/
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length <= 1 || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    commonSelectImgFunction("WChatFriend");
                }
                break;
            case 3:/*QQ好友*/
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length <= 1 || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    commonSelectImgFunction("QQFriend");
                }
                break;
        }
    }
}
