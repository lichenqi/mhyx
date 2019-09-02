package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.IncomingStatementBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ParamUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IncomingStatementFragment extends LazyBaseFragment {
    @BindView(R.id.totalMoney)
    TextView tvtotalMoney;
    @BindView(R.id.myMoney)
    TextView tvmyMoney;
    @BindView(R.id.teamMoney)
    TextView tvteamMoney;
    @BindView(R.id.progressBar_taobao_team_order_nums)
    ProgressBar progressBar_taobao_team_order_nums;
    @BindView(R.id.progressBar_taobao_order_nums)
    ProgressBar progressBar_taobao_order_nums;
    Context context;
    String type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            type = arguments.getString("type");
        }
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this, getView());
        context = MyApplication.getInstance();
        initView();
        getData();
    }

    /*获取收入报表数据*/
    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", type);
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.INCOME_STATEMENT_DATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("收入", response.toString());
                        IncomingStatementBean bean = GsonUtil.GsonToBean(response.toString(), IncomingStatementBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            IncomingStatementBean.BalanceInfoBean balanceInfo = bean.getBalanceInfo();
                            String teamMoney = balanceInfo.getTeamMoney();
                            String myMoney = balanceInfo.getMyMoney();
                            String totalMoney = balanceInfo.getTotalMoney();
                            tvtotalMoney.setText(totalMoney);
                            tvteamMoney.setText(teamMoney);
                            tvmyMoney.setText(myMoney);
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

    private void initView() {
        progressBar_taobao_order_nums.setProgress(50);
        progressBar_taobao_team_order_nums.setProgress(50);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.incomingstatementfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }
}
