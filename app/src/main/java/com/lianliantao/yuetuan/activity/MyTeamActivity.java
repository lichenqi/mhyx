package com.lianliantao.yuetuan.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.MyTeamBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ParamUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTeamActivity extends BaseTitleActivity {
    @BindView(R.id.reTopParent)
    RelativeLayout reTopParent;
    @BindView(R.id.iv)
    CircleImageView iv;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.copy)
    TextView copy;
    @BindView(R.id.reOrderDetail)
    RelativeLayout reOrderDetail;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.llTotal)
    LinearLayout llTotal;
    @BindView(R.id.majordomoNums)
    TextView majordomoNums;
    @BindView(R.id.llDirectlyMajordomo)
    LinearLayout llDirectlyMajordomo;
    @BindView(R.id.todayAdd)
    TextView todayAdd;
    @BindView(R.id.llTodayAdd)
    LinearLayout llTodayAdd;
    @BindView(R.id.yesterdayAdd)
    TextView yesterdayAdd;
    @BindView(R.id.llYesteredayAdd)
    LinearLayout llYesteredayAdd;
    @BindView(R.id.directlyVip)
    TextView directlyVip;
    @BindView(R.id.llDirectlyVip)
    LinearLayout llDirectlyVip;
    @BindView(R.id.directlyVipUnder)
    TextView directlyVipUnder;
    @BindView(R.id.llDirectlyVipUnder)
    LinearLayout llDirectlyVipUnder;
    String invitationCode;
    Intent intent;

    @Override
    public int getContainerView() {
        return R.layout.myteamactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("我的团队");
        invitationCode = PreferUtils.getString(getApplicationContext(), "invitationCode");
        getData();
        code.setText("我的邀请码： " + invitationCode);
    }

    /*团队数据*/
    private void getData() {
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.MY_TEAM_DATA + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("团队", response.toString());
                        MyTeamBean bean = GsonUtil.GsonToBean(response.toString(), MyTeamBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            MyTeamBean.TeamInfoBean teamInfo = bean.getTeamInfo();
                            String referrerMobile = teamInfo.getReferrerMobile();
                            if (TextUtils.isEmpty(referrerMobile) || referrerMobile == null) {
                                reTopParent.setVisibility(View.GONE);
                            } else {
                                reTopParent.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext()).load(teamInfo.getReferrerLogo()).into(iv);
                                name.setText(teamInfo.getReferrerName());
                                phone.setText(teamInfo.getReferrerMobile());
                            }
                            total.setText(teamInfo.getTotalNum());
                            majordomoNums.setText(teamInfo.getDirectorNum());
                            todayAdd.setText(teamInfo.getTodayNum());
                            yesterdayAdd.setText(teamInfo.getYesterdayNum());
                            directlyVip.setText(teamInfo.getDirectMember());
                            directlyVipUnder.setText(teamInfo.getIndirectMember());
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

    @OnClick({R.id.llTotal, R.id.llDirectlyMajordomo, R.id.llTodayAdd, R.id.llYesteredayAdd, R.id.llDirectlyVip, R.id.llDirectlyVipUnder, R.id.reOrderDetail, R.id.copy})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.llTotal:
                intent = new Intent(getApplicationContext(), TeamMemberActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.llDirectlyMajordomo:
                intent = new Intent(getApplicationContext(), TeamMemberActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.llTodayAdd:
                intent = new Intent(getApplicationContext(), TeamMemberActivity.class);
                intent.putExtra("type", "4");
                startActivity(intent);
                break;
            case R.id.llYesteredayAdd:
                intent = new Intent(getApplicationContext(), TeamMemberActivity.class);
                intent.putExtra("type", "5");
                startActivity(intent);
                break;
            case R.id.llDirectlyVip:
                intent = new Intent(getApplicationContext(), DirectlyTeamMemberActivity.class);
                startActivity(intent);
                break;
            case R.id.llDirectlyVipUnder:
                intent = new Intent(getApplicationContext(), TeamMemberActivity.class);
                intent.putExtra("type", "7");
                startActivity(intent);
                break;
            case R.id.reOrderDetail:
                intent = new Intent(getApplicationContext(), MyOrderListActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", invitationCode);
                cm.setPrimaryClip(mClipData);
                ToastUtils.showBackgroudCenterToast(getApplicationContext(), "邀请码复制成功");
                ClipContentUtil.getInstance(getApplicationContext()).putNewSearch(invitationCode);//保存记录到数据库
                break;
        }
    }
}
