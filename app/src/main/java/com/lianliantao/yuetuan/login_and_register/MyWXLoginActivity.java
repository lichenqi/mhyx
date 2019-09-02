package com.lianliantao.yuetuan.login_and_register;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.myutil.StatusBarUtils;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyWXLoginActivity extends OriginalActivity {
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.reloginbg)
    RelativeLayout reloginbg;
    @BindView(R.id.tvLoginBotton)
    TextView tvLoginBotton;
    /*登录模式切换按钮*/
    @BindView(R.id.llChangeLogin)
    LinearLayout llChangeLogin;
    @BindView(R.id.tvChangeLogin)
    TextView tvChangeLogin;
    @BindView(R.id.tvXieyi)
    TextView tvXieyi;
    @BindView(R.id.ivChoose)
    ImageView ivChoose;
    @BindView(R.id.reAgreement)
    RelativeLayout reAgreement;
    private boolean changeLoginStatus = false;
    IWXAPI iwxapi;
    Drawable leftWeChat, leftiPhone;
    int leftWidth;
    Intent intent;
    /*用户协议默认不勾选*/
    private boolean userAgreenment = false;

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
        setContentView(R.layout.mywxloginactivity);
        ButterKnife.bind(this);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        /*微信登录*/
        iwxapi = WXAPIFactory.createWXAPI(this, CommonApi.WCHATAPPID, true);
        iwxapi.registerApp(CommonApi.WCHATAPPID);
        EventBus.getDefault().register(this);
        leftWeChat = getResources().getDrawable(R.mipmap.icon_wechat);
        leftiPhone = getResources().getDrawable(R.mipmap.icon_iphone);
        leftWidth = DensityUtils.dip2px(getApplicationContext(), 20);
    }

    @OnClick({R.id.reloginbg, R.id.llBack, R.id.llChangeLogin, R.id.tvXieyi, R.id.reAgreement})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.llChangeLogin:
                if (changeLoginStatus) {
                    tvChangeLogin.setText("其它登录方式");
                    tvLoginBotton.setText("微信登录");
                    reloginbg.setBackgroundResource(R.drawable.xchat_login_bg);
                    tvLoginBotton.setCompoundDrawablesWithIntrinsicBounds(leftWeChat, null, null, null);
                    tvLoginBotton.setCompoundDrawablePadding(leftWidth);
                } else {
                    tvChangeLogin.setText("返回");
                    tvLoginBotton.setText("手机登录");
                    reloginbg.setBackgroundResource(R.drawable.phone_login_bg);
                    tvLoginBotton.setCompoundDrawablesWithIntrinsicBounds(leftiPhone, null, null, null);
                    tvLoginBotton.setCompoundDrawablePadding(leftWidth);
                }
                changeLoginStatus = !changeLoginStatus;
                break;
            case R.id.reloginbg:
                if (userAgreenment == true) {
                    if (changeLoginStatus) {
                        /*手机登录按钮*/
                        intent = new Intent(getApplicationContext(), PhoneLoginActivity.class);
                        intent.putExtra("uId", "");
                        intent.putExtra("uniqId", "");
                        startActivity(intent);
                    } else {
                        /*微信登录按钮*/
                        if (!iwxapi.isWXAppInstalled()) {
                            ToastUtils.showToast(getApplicationContext(), "请先安装微信APP");
                        } else {
                            PreferUtils.putString(getApplicationContext(), "uId", "");
                            final SendAuth.Req req = new SendAuth.Req();
                            req.scope = "snsapi_userinfo";
                            req.state = "wechat_sdk_xb_live_state";
                            iwxapi.sendReq(req);
                        }
                    }
                } else {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请勾选同意协议！");
                }
                break;
            case R.id.llBack:
                finish();
                break;
            case R.id.tvXieyi:
                intent = new Intent(getApplicationContext(), UserAgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.reAgreement:
                if (userAgreenment) {
                    ivChoose.setImageResource(R.mipmap.dl_cet_buttonmoren);
                } else {
                    ivChoose.setImageResource(R.mipmap.dl_cet_buttonxuanzhong);
                }
                userAgreenment = !userAgreenment;
                break;
        }
    }

    // 声明一个订阅方法，用于接收事件
    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case CommonApi.LOGIN_SUCCESS_LABEL:
                finish();
                break;
        }
    }

}
