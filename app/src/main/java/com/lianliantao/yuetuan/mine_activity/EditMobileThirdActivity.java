package com.lianliantao.yuetuan.mine_activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.BaseBean;
import com.lianliantao.yuetuan.bean.GetAuthCodeBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ParamUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditMobileThirdActivity extends BaseTitleActivity {

    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.inputCode)
    EditText inputCode;
    @BindView(R.id.getCode)
    TextView getCode;
    @BindView(R.id.next)
    TextView next;
    private String mobile;
    private Dialog loadingDialog;
    private TimeCount time = new TimeCount(60000, 1000);
    private String codeContent;

    @Override
    public int getContainerView() {
        return R.layout.editmobilethirdactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("修改手机号");
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        if (!TextUtils.isEmpty(mobile)) {
            String mobileOfNew = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
            tvMobile.setText(mobileOfNew);
        }
    }

    @OnClick({R.id.next, R.id.getCode})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.getCode:
                getCodeData();
                break;
            case R.id.next:
                codeContent = inputCode.getText().toString().trim();
                if (TextUtils.isEmpty(codeContent)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请输入验证码");
                    return;
                }
                nextData();
                break;
        }
    }

    /*保存更改的新手机号*/
    private void nextData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("mobile", mobile);
        map.put("code", codeContent);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.EDIT_SAVE_MOBILE + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("保存新手机号", response.toString());
                        BaseBean bean = GsonUtil.GsonToBean(response.toString(), BaseBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            PreferUtils.putString(getApplicationContext(), "mobile", mobile);
                            EventBus.getDefault().post(CommonApi.EDITPHONE);
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

    /*获取验证码接口*/
    private void getCodeData() {
        loadingDialog = DialogUtil.createLoadingDialog(EditMobileThirdActivity.this, "正在获取...");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("telNum", mobile);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GETAUTHCODE + mapParam)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("验证码", response.toString());
                        DialogUtil.closeDialog(loadingDialog, EditMobileThirdActivity.this);
                        GetAuthCodeBean bean = GsonUtil.GsonToBean(response.toString(), GetAuthCodeBean.class);
                        int errno = bean.getErrno();
                        if (errno == CommonApi.RESULTCODEOK) {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), "验证码已发送至该手机！");
                            time.start();
                        } else {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, EditMobileThirdActivity.this);
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setClickable(false);
            getCode.setText(millisUntilFinished / 1000 + "s" + "重发");
            getCode.setBackgroundResource(R.drawable.edit_mobile_bg_gray);
            getCode.setTextColor(0xff585858);
        }

        @Override
        public void onFinish() {
            getCode.setClickable(true);
            getCode.setText("获取验证码");
            getCode.setBackgroundResource(R.drawable.edit_mobile_bg);
            getCode.setTextColor(0xffffffff);
        }
    }

    @Override
    protected void onDestroy() {
        if (time != null) {
            time.cancel();
        }
        if (loadingDialog != null) {
            DialogUtil.closeDialog(loadingDialog, EditMobileThirdActivity.this);
        }
        super.onDestroy();
    }
}
