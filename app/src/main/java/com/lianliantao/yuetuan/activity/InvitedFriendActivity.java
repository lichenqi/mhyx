package com.lianliantao.yuetuan.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.CardAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.PosterBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dialogfragment.BaseNiceDialog;
import com.lianliantao.yuetuan.dialogfragment.NiceDialog;
import com.lianliantao.yuetuan.dialogfragment.ViewConvertListener;
import com.lianliantao.yuetuan.dialogfragment.ViewHolder;
import com.lianliantao.yuetuan.itemdecoration.InviteFriendItem;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.myutil.MyBitmapUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.myutil.StatusBarUtils;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.CommonUtil;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.NetUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.QRCodeUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class InvitedFriendActivity extends OriginalActivity {

    @BindView(R.id.viewline)
    View viewline;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.llShareLink)
    LinearLayout llShareLink;
    @BindView(R.id.llSharePoster)
    LinearLayout llSharePoster;
    @BindView(R.id.llQrcode)
    LinearLayout llQrcode;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.reCopyCode)
    RelativeLayout reCopyCode;
    String invitationCode, appLink;
    NiceDialog niceDialog;
    int space;
    Bitmap qrCodeBitmap;
    private int currentPosition = 0;
    List<PosterBean.PosterInfoBean> list;
    ImageView iv;
    View share_view;
    Bitmap hebingBitmap;
    Dialog dialog;
    IWXAPI iwxapi;
    ClipboardManager cm;
    int widthPixels, heightPixels, customHeight;
    Dialog loadingDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.invitedfriendactivity);
        ButterKnife.bind(this);
        iwxapi = WXAPIFactory.createWXAPI(getApplicationContext(), CommonApi.WCHATAPPID, true);
        iwxapi.registerApp(CommonApi.WCHATAPPID);
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewline.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewline.setLayoutParams(layoutParams);
        invitationCode = PreferUtils.getString(getApplicationContext(), "invitationCode");
        appLink = PreferUtils.getString(getApplicationContext(), "appLink");
        code.setText(invitationCode);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
        customHeight = DensityUtils.dip2px(getApplicationContext(), 700);
        initSpeedView();
        getData();
        qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(appLink, DensityUtils.dip2px(getApplicationContext(), 70));
        initSharePosterView();
    }

    /*分享出去的海报*/
    private void initSharePosterView() {
        share_view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.share_view_invite_code, null);
        iv = share_view.findViewById(R.id.iv);
        ImageView code = share_view.findViewById(R.id.code);
        Bitmap codeBitmap = QRCodeUtil.createQRCodeBitmap(appLink, DensityUtils.dip2px(getApplicationContext(), 130));
        code.setImageBitmap(codeBitmap);
    }

    private void initSpeedView() {
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        space = DensityUtils.dip2px(getApplicationContext(), 15);
    }

    private void getData() {
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.INVITE_FRIEND_POSTER + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("邀请好友", response.toString());
                        PosterBean bean = GsonUtil.GsonToBean(response.toString(), PosterBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            list = bean.getPosterInfo();
                            if (list.size() > 0) {
                                recyclerview.addItemDecoration(new InviteFriendItem(space, list.size()));
                                CardAdapter adapter = new CardAdapter(getApplicationContext(), list, qrCodeBitmap);
                                recyclerview.setAdapter(adapter);
                                for (PosterBean.PosterInfoBean a : list) {
                                    a.setChoose(false);
                                }
                                list.get(0).setChoose(true);
                                currentPosition = 0;
                                adapter.onclicklistener(new OnItemClick() {
                                    @Override
                                    public void OnItemClickListener(View view, int position) {
                                        currentPosition = position;
                                        for (PosterBean.PosterInfoBean bean : list) {
                                            bean.setChoose(false);
                                        }
                                        list.get(position).setChoose(true);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                /*图片点击放大效果*/
                                adapter.setOnBigIvClickListener(new OnItemClick() {
                                    @Override
                                    public void OnItemClickListener(View view, int position) {
                                        Intent intent = new Intent(getApplicationContext(), InvitePosterBigPhotoActivity.class);
                                        intent.putExtra("appLink", appLink);
                                        intent.putExtra("list", (Serializable) list);
                                        intent.putExtra("position", position);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    @OnClick({R.id.llQrcode, R.id.llSharePoster, R.id.llShareLink, R.id.reCopyCode})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.llQrcode:
                /*先开存储权限*/
                if (ContextCompat.checkSelfPermission(InvitedFriendActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(InvitedFriendActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有存储权限
                    ActivityCompat.requestPermissions(InvitedFriendActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    /*地推二维码*/
                    qrcodeDialog();
                }
                break;
            case R.id.llSharePoster:
                /*分享海报*/
                if (NetUtil.getNetWorkState(InvitedFriendActivity.this) < 0) {
                    ToastUtils.showToast(getApplicationContext(), "您的网络异常，请联网重试");
                    return;
                }
                /*先开存储权限*/
                if (ContextCompat.checkSelfPermission(InvitedFriendActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(InvitedFriendActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有存储权限
                    ActivityCompat.requestPermissions(InvitedFriendActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    if (list != null && list.size() > 0) {
                        loadingDialog = DialogUtil.createLoadingDialog(InvitedFriendActivity.this, "正在加载...");
                        Glide.with(getApplicationContext()).load(list.get(currentPosition).getLogo()).asBitmap().into(target);
                    }
                }
                break;
            case R.id.llShareLink:
                /*复制分享链接*/
                shareLinkDialog();
                break;
            case R.id.reCopyCode:
                /*复制邀请码*/
                ClipData mClipData = ClipData.newPlainText("Label", invitationCode);
                cm.setPrimaryClip(mClipData);
                ClipContentUtil.getInstance(getApplicationContext()).putNewSearch(invitationCode);//保存记录到数据库
                ToastUtils.showBackgroudCenterToast(getApplicationContext(), "邀请码复制成功");
                break;
        }
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            iv.setImageBitmap(bitmap);
            getViewBitmap(share_view);
        }
    };

    //把布局变成Bitmap
    private void getViewBitmap(View view) {
        hebingBitmap = MyBitmapUtil.createBitmapOfNew(getApplicationContext(), view, widthPixels, customHeight);
        DialogUtil.closeDialog(loadingDialog, InvitedFriendActivity.this);
        /*自定义九宫格样式*/
        customShareDialog();
    }

    private void customShareDialog() {
        niceDialog = NiceDialog.init();
        niceDialog.setLayoutId(R.layout.custom_share_style)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        LinearLayout wcaht_friend = holder.getView(R.id.wcaht_friend);
                        LinearLayout wchat_circle = holder.getView(R.id.wchat_circle);
                        LinearLayout save_img = holder.getView(R.id.save_img);
                        wcaht_friend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                wchatFriendShare();
                            }
                        });
                        wchat_circle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                wchatFriendCircle();
                            }
                        });
                        save_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                CommonUtil.saveBitmap2file(hebingBitmap, getApplicationContext());
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .setOutCancel(true)
                .setAnimStyle(R.style.DefaultAnimation)
                .show(getSupportFragmentManager());
    }

    /*微信朋友圈分享*/
    private void wchatFriendCircle() {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setImageData(hebingBitmap);
        sp.setShareType(Platform.SHARE_IMAGE);
        Platform weChat = ShareSDK.getPlatform(WechatMoments.NAME);
        weChat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        weChat.share(sp);
    }


    /*微信好友分享*/
    private void wchatFriendShare() {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setImageData(hebingBitmap);
        sp.setShareType(Platform.SHARE_IMAGE);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        wechat.share(sp);
    }

    /*复制分享链接弹框*/
    private void shareLinkDialog() {
        ClipData mClipData = ClipData.newPlainText("Label", appLink);
        cm.setPrimaryClip(mClipData);
        ClipContentUtil.getInstance(getApplicationContext()).putNewSearch(appLink);//保存记录到数据库
        dialog = new Dialog(InvitedFriendActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(R.layout.open_weixin);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        window.setWindowAnimations(R.style.my_dialog_style_animation);
        TextView sure = dialog.findViewById(R.id.sure);
        TextView cancel = dialog.findViewById(R.id.cancel);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (!iwxapi.isWXAppInstalled()) {
                    ToastUtils.showToast(getApplicationContext(), "请先安装微信APP");
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }

    /*地推二维码弹框*/
    private void qrcodeDialog() {
        int width = widthPixels - DensityUtils.dip2px(getApplicationContext(), 130);
        niceDialog = NiceDialog.init();
        niceDialog.setLayoutId(R.layout.dituiqrcode_dialog);
        niceDialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                ImageView iv = holder.getView(R.id.iv);
                TextView sure = holder.getView(R.id.sure);
                TextView cancel = holder.getView(R.id.cancel);
                LinearLayout.LayoutParams ivLayoutParams = (LinearLayout.LayoutParams) iv.getLayoutParams();
                ivLayoutParams.height = width;
                iv.setLayoutParams(ivLayoutParams);
                Bitmap codeBitmap = QRCodeUtil.createQRCodeBitmap(appLink, width);
                iv.setImageBitmap(codeBitmap);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        niceDialog.dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        CommonUtil.saveBitmap2file(codeBitmap, getApplicationContext());
                    }
                });
            }
        });
        niceDialog.setMargin(100);
        niceDialog.show(getSupportFragmentManager());
        niceDialog.setCancelable(false);
        niceDialog.setOutCancel(false);
    }

    public void onBack(View view) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "分享海报需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length <= 1 || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "分享海报需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (list.size() > 0) {
                        Glide.with(getApplicationContext()).load(list.get(currentPosition).getLogo()).asBitmap().into(target);
                    }
                }
                break;
            case 2:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "保存图片需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length <= 1 || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(getApplicationContext(), "保存图片需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                    return;
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    qrcodeDialog();
                }
                break;
        }
    }
}
