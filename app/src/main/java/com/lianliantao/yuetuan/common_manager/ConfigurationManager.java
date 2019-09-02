package com.lianliantao.yuetuan.common_manager;

import android.content.Context;
import android.util.Log;

import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.AppConfigurationBean;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;

import org.json.JSONObject;

public class ConfigurationManager {

    private Context context;

    public ConfigurationManager(Context context) {
        this.context = context;
    }

    public void manager() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.SYS_DATA + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("app系统消息", response.toString());
                        AppConfigurationBean bean = GsonUtil.GsonToBean(response.toString(), AppConfigurationBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            AppConfigurationBean.AppConfigBean appConfig = bean.getAppConfig();
                            PreferUtils.putString(context, "businessLink", appConfig.getBusinessLink());/*商学院链接*/
                            PreferUtils.putString(context, "guideLink", appConfig.getGuideLink());/*教程链接*/
                            PreferUtils.putString(context, "guidebookLink", appConfig.getGuidebookLink());/*新手教程链接*/
                            PreferUtils.putString(context, "privacyLink", appConfig.getPrivacyLink());/*用户协议链接*/
                            PreferUtils.putString(context, "questionListLink", appConfig.getQuestionListLink());/*常见问题链接*/
                            PreferUtils.putString(context, "userRuleLink", appConfig.getUserRuleLink());/*推广行为链接*/
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }
}
