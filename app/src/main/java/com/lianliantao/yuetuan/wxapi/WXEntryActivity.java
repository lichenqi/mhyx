package com.lianliantao.yuetuan.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.LoginBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.login_and_register.InputInviteCodeActivity;
import com.lianliantao.yuetuan.login_and_register.PhoneLoginActivity;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.UserSaveUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;


public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {
    IWXAPI iwxapi;

    /**
     * 处理微信发出的向第三方应用请求app message
     * <p>
     * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
     * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
     * 做点其他的事情，包括根本不打开任何页面
     */
    public void onGetMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null) {
            Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
            startActivity(iLaunchMyself);
        }
    }

    /**
     * 处理微信向第三方应用发起的消息
     * <p>
     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
     * 回调。
     * <p>
     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
     */
    public void onShowMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null && msg.mediaObject != null
                && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
        }
    }

    int num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, CommonApi.WCHATAPPID, false);
        iwxapi.handleIntent(getIntent(), this);
        num = (int) ((Math.random() * 9 + 1) * 10000000);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        //登录回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                //获取用户信息
                int type = baseResp.getType();
                Log.i("微信返回值", code + "  " + type);
                wchatLoginData(code);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    private void wchatLoginData(String code) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", "1");
        map.put("code", code);
        String param = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().url(CommonApi.BASEURL + CommonApi.WCHATLOGIN + param)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("后台返回值微信", response.toString());
                        LoginBean bean = GsonUtil.GsonToBean(response.toString(), LoginBean.class);
                        if (bean == null) {
                            finish();
                            return;
                        }
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            LoginBean.UserInfoBean userInfo = bean.getUserInfo();
                            if (userInfo == null) {
                                finish();
                                return;
                            } else {
                                String mobile = userInfo.getMobile();
                                String hasUserLink = userInfo.getHasUserLink();
                                if (TextUtils.isEmpty(mobile)) {
                                    /*新微信第一次登陆 手机号不存在 并且 不存在邀请码这个状态 即hasUserLink为false*/
                                    Intent intent = new Intent(getApplicationContext(), PhoneLoginActivity.class);
                                    intent.putExtra("uId", userInfo.getUId());
                                    intent.putExtra("uniqId", userInfo.getUniqId());
                                    startActivity(intent);
                                    finish();
                                } else if (!TextUtils.isEmpty(mobile) && userInfo.getHasUserLink().equals("true")) {
                                    /*老微信 直接登陆进去 手机号存在  并且  邀请码状态存在 即hasUserLink为true*/
                                    UserSaveUtil.saveData(getApplicationContext(), userInfo);
                                    EventBus.getDefault().post(CommonApi.LOGIN_SUCCESS_LABEL);
                                    finish();
                                } else if (!TextUtils.isEmpty(mobile) && hasUserLink.equals("false")) {
                                    /*  手机号存在  并且  邀请码状态不存在  即hasUserLink为false*/
                                    Intent intent = new Intent(getApplicationContext(), InputInviteCodeActivity.class);
                                    intent.putExtra("uId", userInfo.getUId());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    /*状态不祥*/
                                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                                    finish();
                                }
                            }
                        } else {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                        finish();
                    }
                });
    }

}
