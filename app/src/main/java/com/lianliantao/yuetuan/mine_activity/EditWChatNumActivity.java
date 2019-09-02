package com.lianliantao.yuetuan.mine_activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.BaseBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditWChatNumActivity extends BaseTitleActivity {

    @BindView(R.id.inputContent)
    EditText inputContent;
    @BindView(R.id.commit)
    TextView commit;

    @Override
    public int getContainerView() {
        return R.layout.editwchatnumactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("微信号设置");
        String wechatNumber = PreferUtils.getString(getApplicationContext(), "wechatNumber");
        if (TextUtils.isEmpty(wechatNumber)) {
            inputContent.setHint("请输入您的微信号");
        } else {
            inputContent.setText(wechatNumber);
            inputContent.setSelection(wechatNumber.length());
        }
    }

    @OnClick({R.id.commit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.commit:
                String content = inputContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请先输入您的微信号");
                    return;
                }
                commitData(content);
                break;
        }
    }

    private void commitData(String content) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("wechatCode", content);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.EDIT_WCHAT_NUM + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        BaseBean bean = GsonUtil.GsonToBean(response.toString(), BaseBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            PreferUtils.putString(getApplicationContext(), "wechatNumber", content);
                            EventBus.getDefault().post(CommonApi.EDIT_WCHAT_NUM);
                            finish();
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
}
