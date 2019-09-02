package com.lianliantao.yuetuan.photo_dispose_util;

import android.content.Context;

import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;

import org.json.JSONObject;

import java.util.LinkedHashMap;

public class ShareNumData {

    private Context context;
    private String id;

    public ShareNumData(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public void share() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", id);
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.SHARE_NUM + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }
}
