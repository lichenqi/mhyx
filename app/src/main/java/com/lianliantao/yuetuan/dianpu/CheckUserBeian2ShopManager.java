package com.lianliantao.yuetuan.dianpu;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import androidx.fragment.app.FragmentActivity;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.lianliantao.yuetuan.activity.TaoBaoAuthActivity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CheckUserBeian2ShopManager {

    private String id;
    private Context context;
    private FragmentActivity activity;

    public CheckUserBeian2ShopManager(Context context, String id, FragmentActivity activity) {
        this.context = context;
        this.id = id;
        this.activity = activity;
    }

    public void check() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("sellerId", id);
        String commonParamSign = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GOOD_SHOP_LINK + commonParamSign)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        ShopLinkBean bean = GsonUtil.GsonToBean(response.toString(), ShopLinkBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {/*代表已备案*/
                            String link = bean.getLink();
                            AlibcShowParams alibcShowParams = new AlibcShowParams();
                            alibcShowParams.setOpenType(OpenType.Native);
                            alibcShowParams.setBackUrl("alisdk://");
                            HashMap<String, String> exParams = new HashMap<>();
                            exParams.put("isv_code", "appisvcode");
                            exParams.put("alibaba", "阿里巴巴");
                            // 以显示传入url的方式打开页面（第二个参数是套件名称 暂时传“”）
                            AlibcTrade.openByUrl(activity, "", link, null,
                                    new WebViewClient(), new WebChromeClient(), alibcShowParams,
                                    null, exParams, new AlibcTradeCallback() {
                                        @Override
                                        public void onTradeSuccess(AlibcTradeResult tradeResult) {
                                        }

                                        @Override
                                        public void onFailure(int code, String msg) {
                                        }
                                    });
                        } else if (bean.getErrno() == 434) {/*用户淘宝未备案*/
                            taobaoBeiAn();
                        } else {
                            ToastUtils.showToast(context, bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    Session session;

    /*淘宝备案*/
    private void taobaoBeiAn() {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        session = alibcLogin.getSession();
        String openId = session.openId;
        if (TextUtils.isEmpty(openId)) {/*阿里百川未授权*/
            alibcLogin.showLogin(new AlibcLoginCallback() {
                @Override
                public void onSuccess(int i, String s, String s1) {
                    session = alibcLogin.getSession();
                    String nick = session.nick;/*淘宝昵称*/
                    String avatarUrl = session.avatarUrl;/*淘宝头像*/
                    Intent intent = new Intent(context, TaoBaoAuthActivity.class);
                    intent.putExtra("nick", nick);
                    intent.putExtra("avatarUrl", avatarUrl);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                @Override
                public void onFailure(int i, String s) {
                }
            });
        } else {
            String nick = session.nick;/*淘宝昵称*/
            String avatarUrl = session.avatarUrl;/*淘宝头像*/
            Intent intent = new Intent(context, TaoBaoAuthActivity.class);
            intent.putExtra("nick", nick);
            intent.putExtra("avatarUrl", avatarUrl);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
