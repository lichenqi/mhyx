package com.lianliantao.yuetuan.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.BaseBean;
import com.lianliantao.yuetuan.bean.UserMoneyBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.keyboardutil.Unregistrar;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
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

public class WithdrawDepositerActivity extends OriginalActivity {
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tiixanRecord)
    ImageView tiixanRecord;
    @BindView(R.id.ketixian)
    TextView ketixian;
    @BindView(R.id.lastMonthJieSuan)
    TextView lastMonthJieSuan;
    @BindView(R.id.thisMonthJieSuan)
    TextView thisMonthJieSuan;
    @BindView(R.id.waitJieSuan)
    TextView waitJieSuan;
    @BindView(R.id.lookDetail)
    TextView lookDetail;
    @BindView(R.id.nestedscrollview)
    NestedScrollView nestedscrollview;
    @BindView(R.id.alipayNum)
    EditText alipayNum;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.inputMoney)
    EditText inputMoney;
    @BindView(R.id.tixianButton)
    TextView tixianButton;
    Unregistrar mUnregistrar;
    String withdraw;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneTopStyleUtil.setPhoneStatusTheme(this, 0);
        PhoneTopStyleUtil.setBottomNavigationBarColor(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        int bottomStatusHeight = PhoneTopStyleUtil.getBottomStatusHeight(getApplicationContext());
        setContentView(R.layout.withdrawdepositeractivity);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParamsParent = (LinearLayout.LayoutParams) nestedscrollview.getLayoutParams();
        layoutParamsParent.setMargins(0, 0, 0, bottomStatusHeight);
        nestedscrollview.setLayoutParams(layoutParamsParent);
        /*监听键盘开关状态*/
//        mUnregistrar = KeyboardVisibilityEvent.registerEventListener(this, new KeyboardVisibilityEventListener() {
//            @Override
//            public void onVisibilityChanged(boolean isOpen) {
//                isKeyBoardState(isOpen);
//            }
//        });
        /*设置软键盘为关闭*/
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getData();
    }

    /*用户金额数据*/
    private void getData() {
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.MINEUSERMONEYDATA + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("我的数据", response.toString());
                        UserMoneyBean bean = GsonUtil.GsonToBean(response.toString(), UserMoneyBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            UserMoneyBean.BalanceInfoBean balanceInfo = bean.getBalanceInfo();
                            withdraw = balanceInfo.getWithdraw();/*用户可提现金额*/
                            ketixian.setText(withdraw);
                            lastMonthJieSuan.setText(balanceInfo.getLastMonthSettlement());
                            thisMonthJieSuan.setText(balanceInfo.getThisMonthSettlement());
                            waitJieSuan.setText(balanceInfo.getPendingSettlement());
                        } else {
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    @OnClick({R.id.lookDetail, R.id.back, R.id.tiixanRecord, R.id.tixianButton})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.lookDetail:
                intent = new Intent(getApplicationContext(), WithdrawTimeActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tiixanRecord:
                intent = new Intent(getApplicationContext(), TiXianRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.tixianButton:/*提现按钮*/
                String input_alipay = alipayNum.getText().toString().trim();
                String input_name = name.getText().toString().trim();
                String input_money = inputMoney.getText().toString().trim();
                if (TextUtils.isEmpty(input_alipay)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请输入支付宝账号");
                    return;
                }
                if (TextUtils.isEmpty(input_name)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请输入您的姓名");
                    return;
                }
                if (TextUtils.isEmpty(input_money)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请输入提现金额");
                    return;
                }
                if (Double.valueOf(input_money) < Double.valueOf(1)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "提现金额不能小于1元");
                    return;
                }
                if (Double.valueOf(input_money) > Double.valueOf(withdraw)) {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "提现金额不能超过当前账户可提现余额");
                    return;
                }
                /*去提现*/
                tiXianData(input_alipay, input_name, input_money);
                break;
        }
    }

    Dialog loadingDialog;

    /*提现接口*/
    private void tiXianData(String input_alipay, String input_name, String input_money) {
        loadingDialog = DialogUtil.createLoadingDialog(WithdrawDepositerActivity.this, "正在申请...");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("alipayUsername", input_alipay);
        map.put("name", input_name);
        map.put("money", input_money);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.WITHDRAW_DEPOSITER + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        DialogUtil.closeDialog(loadingDialog, WithdrawDepositerActivity.this);
                        Log.i("提现接口", response.toString());
                        BaseBean bean = GsonUtil.GsonToBean(response.toString(), BaseBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            EventBus.getDefault().post(CommonApi.USER_DATA_CHANGE);
                            finish();
                        } else {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, WithdrawDepositerActivity.this);
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void isKeyBoardState(boolean isOpen) {
        if (isOpen) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //设置ScrollView滚动到顶部
                    // scrollView.fullScroll(ScrollView.FOCUS_UP);
                    //设置ScrollView滚动到顶部
                    nestedscrollview.fullScroll(NestedScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
//        mUnregistrar.unregister();
        super.onDestroy();
    }
}
