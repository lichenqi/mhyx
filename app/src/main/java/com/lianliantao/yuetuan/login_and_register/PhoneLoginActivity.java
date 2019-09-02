package com.lianliantao.yuetuan.login_and_register;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.GetAuthCodeBean;
import com.lianliantao.yuetuan.bean.LoginBean;
import com.lianliantao.yuetuan.common_manager.AppUtils;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.common_manager.EncryptUtil;
import com.lianliantao.yuetuan.common_manager.NetUtil;
import com.lianliantao.yuetuan.common_manager.SortUtils;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.UserSaveUtil;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.VersionUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneLoginActivity extends BaseTitleActivity {

    @BindView(R.id.cell)
    ImageView cell;
    /*手机号输入*/
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.yzm)
    ImageView yzm;
    /*验证码输入*/
    @BindView(R.id.ed_auth_code)
    EditText edAuthCode;
    /*验证码按钮*/
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.login)
    TextView login;
    private TimeCount time = new TimeCount(60000, 1000);
    String phoneNum, authCode;
    Dialog dialog;
    String uId, uniqId;
    IWXAPI iwxapi;
    String first_phone_num;

    @Override
    public int getContainerView() {
        return R.layout.phoneloginactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMiddleTitle("手机登录");
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        iwxapi = WXAPIFactory.createWXAPI(this, CommonApi.WCHATAPPID, true);
        iwxapi.registerApp(CommonApi.WCHATAPPID);
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uniqId = intent.getStringExtra("uniqId");
        first_phone_num = PreferUtils.getString(getApplicationContext(), "first_phone_num");
        initInpuNumView();
    }

    /*设置手机输入框是否显示手机号*/
    private void initInpuNumView() {
        if (!TextUtils.isEmpty(first_phone_num)) {
            edPhone.setText(first_phone_num);
            edPhone.setSelection(first_phone_num.length());
            PreferUtils.putString(getApplicationContext(), "first_phone_num", "");
        }
    }

    @OnClick({R.id.tv_get_code, R.id.login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                phoneNum = edPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请先输入您的手机号");
                    return;
                }
                getAuthCodeData(phoneNum);
                break;
            case R.id.login:
                phoneNum = edPhone.getText().toString().trim();
                authCode = edAuthCode.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请先输入您的手机号");
                    return;
                }
                if (TextUtils.isEmpty(authCode)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请输入您获取的验证码");
                    return;
                }
                loginData(phoneNum, authCode);
                break;
        }
    }

    /*手机登录接口*/
    private void loginData(String phoneNum, String authCode) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", "2");
        map.put("uId", uId);
        map.put("telNum", phoneNum);
        map.put("verifyCode", authCode);
        map.put("uniqId", uniqId);
        map.put("netType", NetUtil.getNetClassic(getApplicationContext()));
        map.put("deviceOs", "Android");
        map.put("deviceOp", VersionUtil.getAndroidNumVersion(getApplicationContext()));
        map.put("deviceId", AppUtils.getPesudoUniqueID());
        String qianMingMapParam = SortUtils.formatUrlParam(map) + "!oop5clnr57";
        String strSig = "1" + EncryptUtil.encrypt(qianMingMapParam);
        map.put("sig", strSig);
        String yuNum = SortUtils.getYuNum(map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.WCHATLOGIN + "?" + yuNum)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("微信绑定手机号接口", response.toString());
                        LoginBean bean = GsonUtil.GsonToBean(response.toString(), LoginBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            LoginBean.UserInfoBean userInfo = bean.getUserInfo();
                            String uniqId = userInfo.getUniqId();/*微信唯一标识*/
                            String hasUserLink = userInfo.getHasUserLink();/*是否锁粉过来 true为是的  false为没有*/
                            String uId = userInfo.getUId();
                            if (!TextUtils.isEmpty(uniqId)) {
                                /*已绑定微信*/
                                if (hasUserLink.equals("true")) {
                                    /*锁粉过来 或者 老用户 直接登录*/
                                    UserSaveUtil.saveData(getApplicationContext(), userInfo);
                                    EventBus.getDefault().post(CommonApi.LOGIN_SUCCESS_LABEL);
                                    finish();
                                } else {
                                    /* 新用户 这里是进入输入邀请码界面*/
                                    Intent intent = new Intent(getApplicationContext(), InputInviteCodeActivity.class);
                                    intent.putExtra("uId", uId);
                                    startActivity(intent);
                                }
                            } else {
                                /*该账户还没有绑定微信*/
                                if (!iwxapi.isWXAppInstalled()) {
                                    ToastUtils.showToast(getApplicationContext(), "请先安装微信APP");
                                } else {
                                    PreferUtils.putString(getApplicationContext(), "uId", uId);
                                    final SendAuth.Req req = new SendAuth.Req();
                                    req.scope = "snsapi_userinfo";
                                    req.state = "wechat_sdk_xb_live_state";
                                    iwxapi.sendReq(req);
                                    finish();
                                }
                            }
                        } else {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    Dialog loadingDialog;

    /*获取验证码接口*/
    private void getAuthCodeData(String phoneNum) {
        loadingDialog = DialogUtil.createLoadingDialog(PhoneLoginActivity.this, "正在获取...");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("telNum", phoneNum);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GETAUTHCODE + mapParam)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("验证码", response.toString());
                        DialogUtil.closeDialog(loadingDialog, PhoneLoginActivity.this);
                        GetAuthCodeBean bean = GsonUtil.GsonToBean(response.toString(), GetAuthCodeBean.class);
                        if (bean == null) return;
                        int errno = bean.getErrno();
                        if (errno == 432) {
                            /*新手机号 即该手机号未绑定微信*/
                            showPhoneNumUnRegisterDialog(phoneNum);
                        } else if (errno == 200) {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), "验证码已发送至该手机！");
                            time.start();
                        } else {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, PhoneLoginActivity.this);
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    /*提示用户手机号未注册弹框*/
    private void showPhoneNumUnRegisterDialog(String phoneNum) {
        dialog = new Dialog(PhoneLoginActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(R.layout.phone_num_un_register_dialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        window.setWindowAnimations(R.style.my_dialog_style_animation);
        TextView sure = dialog.findViewById(R.id.sure);
        TextView cancel = dialog.findViewById(R.id.cancel);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*微信登录按钮*/
                if (!iwxapi.isWXAppInstalled()) {
                    ToastUtils.showToast(getApplicationContext(), "请先安装微信APP");
                } else {
                    dialog.dismiss();
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_xb_live_state";
                    iwxapi.sendReq(req);
                    PreferUtils.putString(getApplicationContext(), "first_phone_num", phoneNum);
                    finish();
                }
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

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setClickable(false);
            tvGetCode.setText(millisUntilFinished / 1000 + "s" + "重发");
        }

        @Override
        public void onFinish() {
            tvGetCode.setClickable(true);
            tvGetCode.setText("获取验证码");
        }
    }

    @Override
    protected void onDestroy() {
        if (time != null) {
            time.cancel();
        }
        EventBus.getDefault().unregister(this);
        if (loadingDialog != null) {
            DialogUtil.closeDialog(loadingDialog, PhoneLoginActivity.this);
        }
        super.onDestroy();
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
