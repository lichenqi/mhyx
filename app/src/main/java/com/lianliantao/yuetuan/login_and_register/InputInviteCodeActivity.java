package com.lianliantao.yuetuan.login_and_register;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.CheckBean;
import com.lianliantao.yuetuan.bean.LoginBean;
import com.lianliantao.yuetuan.common_manager.AppUtils;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.common_manager.EncryptUtil;
import com.lianliantao.yuetuan.common_manager.NetUtil;
import com.lianliantao.yuetuan.common_manager.SortUtils;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.UserSaveUtil;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.VersionUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputInviteCodeActivity extends BaseLoginActivity {

    @BindView(R.id.cell)
    ImageView cell;
    @BindView(R.id.edCodeOrPhone)
    EditText edCodeOrPhone;
    @BindView(R.id.iv)
    CircleImageView iv;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.llSuperView)
    LinearLayout llSuperView;
    @BindView(R.id.next)
    TextView next;
    String uId;
    private String invitationCode;

    @Override
    public int getContainerView() {
        return R.layout.inputinvitecodeactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("输入邀请码");
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        initInputCode();
    }

    /*监听输入框变化*/
    private void initInputCode() {
        edCodeOrPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                if (length >= 6) {
                    next.setBackgroundResource(R.drawable.done_bg);
                    next.setText("完成");
                    if (length == 6 || length == 11) {
                        getSuperiorData(s.toString());
                    }
                } else {
                    next.setBackgroundResource(R.drawable.next_bg);
                    next.setText("下一步");
                    llSuperView.setVisibility(View.GONE);
                }
            }
        });
    }

    /*获取上级邀请人信息*/
    private void getSuperiorData(String code) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("code", code);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.CHECK_CODE_DATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("检验信息", response.toString());
                        CheckBean checkBean = GsonUtil.GsonToBean(response.toString(), CheckBean.class);
                        if (checkBean.getErrno() == CommonApi.RESULTCODEOK) {
                            CheckBean.UserInfoBean userInfo = checkBean.getUserInfo();
                            llSuperView.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(userInfo.getAvatar()).into(iv);
                            name.setText("您的邀请人： " + userInfo.getNickname());
                            invitationCode = userInfo.getInvitationCode();
                        } else {
                            llSuperView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }

    @OnClick({R.id.next})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                String code = edCodeOrPhone.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "邀请码或手机号不能为空");
                    return;
                }
                if (code.length() == 6 || code.length() == 11) {
                    doneData(code);
                } else {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "邀请码或手机号格式不对");
                }
                break;
        }
    }

    Dialog loadingDialog;

    public void doneData(String code) {
        loadingDialog = DialogUtil.createLoadingDialog(InputInviteCodeActivity.this, "验证中...");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("invitationCode", invitationCode);
        map.put("uId", uId);
        map.put("netType", NetUtil.getNetClassic(getApplicationContext()));
        map.put("deviceOs", "Android");
        map.put("deviceOp", VersionUtil.getAndroidNumVersion(getApplicationContext()));
        map.put("deviceId", AppUtils.getPesudoUniqueID());
        String qianMingMapParam = SortUtils.formatUrlParam(map) + "!oop5clnr57";
        String strSig = "1" + EncryptUtil.encrypt(qianMingMapParam);
        map.put("sig", strSig);
        String yuNum = SortUtils.getYuNum(map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.DONE_INVITATION_CODE_DATA + "?" + yuNum)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("用户绑定邀请码信息", response.toString());
                        DialogUtil.closeDialog(loadingDialog, InputInviteCodeActivity.this);
                        LoginBean bean = GsonUtil.GsonToBean(response.toString(), LoginBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            UserSaveUtil.saveData(getApplicationContext(), bean.getUserInfo());
                            EventBus.getDefault().post(CommonApi.LOGIN_SUCCESS_LABEL);
                            finish();
                        } else {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, InputInviteCodeActivity.this);
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

}
