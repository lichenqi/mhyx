package com.lianliantao.yuetuan.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.MainActivity;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.BaseBean;
import com.lianliantao.yuetuan.bean.SettingBean;
import com.lianliantao.yuetuan.bean.UserBindTBKBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.mine_activity.EditMobileActivity;
import com.lianliantao.yuetuan.mine_activity.EditWChatNumActivity;
import com.lianliantao.yuetuan.mine_activity.VersionUpgradeManager;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.DataCleanManager;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.NetUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.VersionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class SettingActivity extends BaseTitleActivity {
    @BindView(R.id.switch_one)
    Switch switch_one;
    @BindView(R.id.switch_two)
    Switch switch_two;
    @BindView(R.id.switch_three)
    Switch switch_three;
    @BindView(R.id.iv_circle)
    CircleImageView ivCircle;
    @BindView(R.id.tv_login_out)
    TextView tv_login_out;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.reTaobaoQudao)
    RelativeLayout reTaobaoQudao;
    @BindView(R.id.reCancelTaoBaoAuth)
    RelativeLayout reCancelTaoBaoAuth;
    @BindView(R.id.tvTaoBaoApproveLabel)
    TextView tvTaoBaoApproveLabel;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.reCellPhoneBind)
    RelativeLayout reCellPhoneBind;
    @BindView(R.id.reWChat)
    RelativeLayout reWChat;
    @BindView(R.id.tvWChatNum)
    TextView tvWChatNum;
    @BindView(R.id.tvCache)
    TextView tvCache;
    @BindView(R.id.cleanCacha)
    TextView cleanCacha;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.reVersion)
    RelativeLayout reVersion;
    Dialog dialog;
    AlibcLogin alibcLogin;
    String taobaoNick, taobaoUserIv;/*淘宝头像和昵称*/
    String authUrl;/*用户头像*/
    Intent intent;
    private String mobile;
    private String orderPrivacy = "";
    private String orderIncomeNotice = "";
    private String shareAppDownload = "";
    private String hasBindTbkdata = "";

    // 声明一个订阅方法，用于接收事件
    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case CommonApi.EDITPHONE:
                initPhoneView();
                break;
            case CommonApi.EDIT_WCHAT_NUM:
                initWChatNum();
                break;
        }
    }

    @Override
    public int getContainerView() {
        return R.layout.settingactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setMiddleTitle("设置");
        alibcLogin = AlibcLogin.getInstance();
        authUrl = PreferUtils.getString(getApplicationContext(), "authUrl");
        initViewData();/*基本信息初始化*/
        initPhoneView();/*手机号设置*/
        initWChatNum();/*微信号设置*/
        getSwitchData();/*获取通知按钮状态*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserBindTbkData();/*获取用户备案淘宝客状态*/
    }

    /*获取用户备案淘宝客状态*/
    private void getUserBindTbkData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.USER_BIND_TBK + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("备案信息", "onSuccess: " + response);
                        UserBindTBKBean bean = GsonUtil.GsonToBean(response.toString(), UserBindTBKBean.class);
                        if (bean.errno == CommonApi.RESULTCODEOK) {
                            hasBindTbkdata = bean.getHasBindTbk();
                            if (hasBindTbkdata.equals("true")) {
                                tvTaoBaoApproveLabel.setText("已认证");
                            } else {
                                tvTaoBaoApproveLabel.setText("未认证");
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void getSwitchData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.SETTING_INFO + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("设置信息", response.toString());
                        SettingBean bean = GsonUtil.GsonToBean(response.toString(), SettingBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            SettingBean.SetInfoBean setInfo = bean.getSetInfo();
                            shareAppDownload = setInfo.getShareAppDownload();
                            orderIncomeNotice = setInfo.getOrderIncomeNotice();
                            orderPrivacy = setInfo.getOrderPrivacy();
                            if (shareAppDownload.equals("1")) {
                                switch_one.setChecked(true);
                            } else {
                                switch_one.setChecked(false);
                            }
                            if (orderIncomeNotice.equals("1")) {
                                switch_two.setChecked(true);
                            } else {
                                switch_two.setChecked(false);
                            }
                            if (orderPrivacy.equals("1")) {
                                switch_three.setChecked(true);
                            } else {
                                switch_three.setChecked(false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }

    private void initPhoneView() {
        mobile = PreferUtils.getString(getApplicationContext(), "mobile");
        if (!TextUtils.isEmpty(mobile)) {
            mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
            tvMobile.setText(mobile);
        }
    }

    private void initWChatNum() {
        String wechatNumber = PreferUtils.getString(getApplicationContext(), "wechatNumber");
        if (TextUtils.isEmpty(wechatNumber)) {
            tvWChatNum.setText("未设置");
        } else {
            tvWChatNum.setText(wechatNumber);
        }
    }

    private void initViewData() {
        String avatar = PreferUtils.getString(getApplicationContext(), "avatar");
        String nickName = PreferUtils.getString(getApplicationContext(), "nickName");
        Glide.with(getApplicationContext()).load(avatar).into(ivCircle);
        tvNickName.setText(nickName);
        String totalCacheSize = DataCleanManager.getTotalCacheSize(getApplicationContext());
        tvCache.setText(totalCacheSize);
        version.setText("V" + VersionUtil.getAndroidNumVersion(getApplicationContext()));
    }

    @OnClick({R.id.tv_login_out, R.id.reTaobaoQudao, R.id.reCancelTaoBaoAuth, R.id.reCellPhoneBind, R.id.reWChat, R.id.cleanCacha
            , R.id.switch_one, R.id.switch_two, R.id.switch_three, R.id.reVersion})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_out:/*退出登录*/
                showLoginOutDialog();
                break;
            case R.id.reTaobaoQudao:/*淘宝渠道认证*/
                if (NetUtil.getNetWorkState(SettingActivity.this) < 0) {
                    ToastUtils.showToast(getApplicationContext(), "您的网络异常，请联网重试");
                    return;
                }
                if (hasBindTbkdata.equals("true")) {
                    session = alibcLogin.getSession();
                    String openId = session.openId;
                    if (TextUtils.isEmpty(openId)) {
                        alibcLogin.showLogin(new AlibcLoginCallback() {
                            @Override
                            public void onSuccess(int i, String s, String s1) {
                                session = alibcLogin.getSession();
                                taobaoUserIv = session.avatarUrl;
                                taobaoNick = session.nick;
                                intent = new Intent(getApplicationContext(), TaoBaoChannelApproveSuccessActivity.class);
                                intent.putExtra("avatarUrl", session.avatarUrl);
                                intent.putExtra("nick", session.nick);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                            }
                        });
                    } else {
                        intent = new Intent(getApplicationContext(), TaoBaoChannelApproveSuccessActivity.class);
                        intent.putExtra("avatarUrl", session.avatarUrl);
                        intent.putExtra("nick", session.nick);
                        startActivity(intent);
                    }
                } else {
                    taobaoLogin();/*淘宝渠道备案操作*/
                }
                break;
            case R.id.reCancelTaoBaoAuth:
                break;
            case R.id.reCellPhoneBind:/*手机绑定*/
                intent = new Intent(getApplicationContext(), EditMobileActivity.class);
                startActivity(intent);
                break;
            case R.id.reWChat:/*微信号设置*/
                intent = new Intent(getApplicationContext(), EditWChatNumActivity.class);
                startActivity(intent);
                break;
            case R.id.cleanCacha:
                DataCleanManager.clearAllCache(getApplicationContext());
                tvCache.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
                break;
            case R.id.switch_one:
                if (NetUtil.getNetWorkState(SettingActivity.this) < 0) {
                    ToastUtils.showToast(getApplicationContext(), "您的网络异常，请联网重试");
                    return;
                }
                if (shareAppDownload.equals("1")) {
                    setButtonData("shareAppDownload", "2");
                } else {
                    setButtonData("shareAppDownload", "1");
                }
                break;
            case R.id.switch_two:
                if (NetUtil.getNetWorkState(SettingActivity.this) < 0) {
                    ToastUtils.showToast(getApplicationContext(), "您的网络异常，请联网重试");
                    return;
                }
                if (orderIncomeNotice.equals("1")) {
                    setButtonData("orderIncomeNotice", "2");
                } else {
                    setButtonData("orderIncomeNotice", "1");
                }
                break;
            case R.id.switch_three:
                if (NetUtil.getNetWorkState(SettingActivity.this) < 0) {
                    ToastUtils.showToast(getApplicationContext(), "您的网络异常，请联网重试");
                    return;
                }
                if (orderPrivacy.equals("1")) {
                    setButtonData("orderPrivacy", "2");
                } else {
                    setButtonData("orderPrivacy", "1");
                }
                break;
            case R.id.reVersion:/*点击更新版本接口*/
                if (NetUtil.getNetWorkState(SettingActivity.this) < 0) {
                    ToastUtils.showToast(getApplicationContext(), "您的网络异常，请联网重试");
                    return;
                }
                VersionUpgradeManager manager = new VersionUpgradeManager(getApplicationContext(), this);
                manager.set();
                break;
        }
    }

    /*设置按钮接口*/
    private void setButtonData(String para, String type) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(para, type);
        String otherParamSign = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.SETTINGSAVE + otherParamSign)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("设置结果", response.toString());
                        BaseBean bean = GsonUtil.GsonToBean(response.toString(), BaseBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            switch (para) {
                                case "shareAppDownload":
                                    if (type.equals("1")) {
                                        switch_one.setChecked(true);
                                        shareAppDownload = "1";
                                    } else {
                                        switch_one.setChecked(false);
                                        shareAppDownload = "2";
                                    }
                                    break;
                                case "orderIncomeNotice":
                                    if (type.equals("1")) {
                                        switch_two.setChecked(true);
                                        orderIncomeNotice = "1";
                                    } else {
                                        switch_two.setChecked(false);
                                        orderIncomeNotice = "2";
                                    }
                                    break;
                                case "orderPrivacy":
                                    if (type.equals("1")) {
                                        switch_three.setChecked(true);
                                        orderPrivacy = "1";
                                    } else {
                                        switch_three.setChecked(false);
                                        orderPrivacy = "2";
                                    }
                                    break;
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

    Session session;

    /*淘宝授权*/
    private void taobaoLogin() {
        session = alibcLogin.getSession();
        String openId = session.openId;
        if (TextUtils.isEmpty(openId)) {/*阿里百川未授权*/
            alibcLogin.showLogin(new AlibcLoginCallback() {
                @Override
                public void onSuccess(int i, String s, String s1) {
                    session = alibcLogin.getSession();
                    taobaoNick = session.nick;/*淘宝昵称*/
                    taobaoUserIv = session.avatarUrl;/*淘宝头像*/
                    intent = new Intent(getApplicationContext(), TaoBaoAuthActivity.class);
                    intent.putExtra("nick", taobaoNick);
                    intent.putExtra("avatarUrl", taobaoUserIv);
                    startActivityForResult(intent, 100);
                }

                @Override
                public void onFailure(int i, String s) {
                }
            });
        } else {/*淘宝授权已登录*/
            taobaoNick = session.nick;/*淘宝昵称*/
            taobaoUserIv = session.avatarUrl;/*淘宝头像*/
            intent = new Intent(getApplicationContext(), TaoBaoAuthActivity.class);
            intent.putExtra("nick", taobaoNick);
            intent.putExtra("avatarUrl", taobaoUserIv);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            tvTaoBaoApproveLabel.setText("已认证");
        }
    }

    /*退出登录弹框*/
    private void showLoginOutDialog() {
        dialog = new Dialog(SettingActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(R.layout.login_out_dialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        TextView sure = dialog.findViewById(R.id.sure);
        TextView cancel = dialog.findViewById(R.id.cancel);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                JPushInterface.setAlias(getApplicationContext(), 0, "");
                PreferUtils.putBoolean(getApplicationContext(), CommonApi.ISLOGIN, false);
                PreferUtils.putString(getApplicationContext(), "uId", "");
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("loginout", "loginout");
                startActivity(intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
