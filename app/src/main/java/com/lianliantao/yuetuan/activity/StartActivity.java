package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lianliantao.yuetuan.MainActivity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.app_status.AppStatus;
import com.lianliantao.yuetuan.app_status.AppStatusManager;
import com.lianliantao.yuetuan.bean.BaseTitleBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.common_manager.ConfigurationManager;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.UserSaveUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.SpUtil;

import org.json.JSONObject;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
        boolean isFirst = PreferUtils.getBoolean(getApplicationContext(), "isFirst");
        UserSaveUtil.aboutuIdSave(getApplicationContext());
        getTitleData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFirst) {
                    intent = new Intent(getApplicationContext(), GuideActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                //app状态改为正常
                AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL);
                startActivity(intent);
                PreferUtils.putBoolean(getApplicationContext(), "isFirst", true);
                finish();
            }
        }, 1500);
        ConfigurationManager manager = new ConfigurationManager(getApplicationContext());
        manager.manager();
    }

    /*获取指示器数据*/
    private void getTitleData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_DATA + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("指示器数据", response.toString());
                        BaseTitleBean bean = GsonUtil.GsonToBean(response.toString(), BaseTitleBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<BaseTitleBean.CateInfoBean> cateInfo = bean.getCateInfo();
                            if (cateInfo == null) return;
                            if (cateInfo.size() > 0) {
                                SpUtil.putList(getApplicationContext(), CommonApi.TITLE_DATA_LIST, cateInfo);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            //非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

}
