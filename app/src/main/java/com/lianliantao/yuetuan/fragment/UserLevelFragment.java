package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.InvitedFriendActivity;
import com.lianliantao.yuetuan.adapter.UserImageIvListAdapter;
import com.lianliantao.yuetuan.adapter.UserLevelUpgradeListAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.LevelCentyBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.coverflow.CoverFlowLayoutManger;
import com.lianliantao.yuetuan.coverflow.RecyclerCoverFlow;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ParamUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserLevelFragment extends Fragment {
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartrefresh;
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.userIv)
    CircleImageView userIv;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userLevel)
    ImageView userLevel;
    @BindView(R.id.sjquanyi)
    TextView sjquanyi;
    @BindView(R.id.ruhejingsheng)
    TextView ruhejingsheng;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.joinMyTeam)
    TextView joinMyTeam;
    @BindView(R.id.ourbossType)
    TextView ourbossType;
    @BindView(R.id.recyclerviewUserList)
    RecyclerCoverFlow recyclerviewUserList;
    @BindView(R.id.othernickName)
    TextView othernickName;
    @BindView(R.id.otherType)
    TextView otherType;
    @BindView(R.id.otherMoney)
    TextView otherMoney;
    @BindView(R.id.one)
    TextView one;
    @BindView(R.id.oneBelow)
    TextView oneBelow;
    @BindView(R.id.two)
    TextView two;
    @BindView(R.id.twoBelow)
    TextView twoBelow;
    @BindView(R.id.three)
    TextView three;
    @BindView(R.id.threeBelow)
    TextView threeBelow;
    @BindView(R.id.four)
    TextView four;
    @BindView(R.id.fourBelow)
    TextView fourBelow;
    @BindView(R.id.five)
    TextView five;
    @BindView(R.id.fiveBelow)
    TextView fiveBelow;
    @BindView(R.id.six)
    TextView six;
    @BindView(R.id.sixBelow)
    TextView sixBelow;
    private View view;
    Context context;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 声明一个订阅方法，用于接收事件
    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case CommonApi.LOGIN_SUCCESS_LABEL:
                setUserData();/*用户基本信息*/
                getOtherData();/*获取团队用户信息*/
                initQuanXianOfficial();/*设置权限文案*/
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.userlevelfragment, container, false);
            ButterKnife.bind(this, view);
            EventBus.getDefault().register(this);
            context = MyApplication.getInstance();
            int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(context);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHeight.getLayoutParams();
            layoutParams.height = statusBarHeight;
            viewHeight.setLayoutParams(layoutParams);
            recyclerview.setHasFixedSize(true);
            recyclerview.setLayoutManager(new LinearLayoutManager(context));
            recyclerviewUserList.setAlphaItem(true);
            recyclerviewUserList.setFlatFlow(false);
            setUserData();/*用户基本信息*/
            getOtherData();/*获取团队用户信息*/
            initSwipeRefresh();/*刷新控件*/
            initQuanXianOfficial();/*设置权限文案*/
        }
        return view;
    }

    private void initQuanXianOfficial() {
        String level = PreferUtils.getString(context, "level");
        switch (level) {
            case "1":/*会员*/
                vipAndSuperVipOfficial();
                break;
            case "2":/*超级会员*/
                vipAndSuperVipOfficial();
                break;
            case "3":/*总监*/
                majordomoAndSuperMajordomo(3);
                break;
            case "4":/*高级总监*/
                majordomoAndSuperMajordomo(4);
                break;
        }
    }

    /*总监和高级总监文案内容*/
    private void majordomoAndSuperMajordomo(int type) {
        if (type == 3) {
            sjquanyi.setText("升级高级总监权益");
        } else {
            sjquanyi.setText("高级总监权益");
        }
        one.setText("所有权益");
        oneBelow.setText("总监");
        two.setText("收益提升66%");
        twoBelow.setText("自推出单");
        three.setText("收益提升63%");
        threeBelow.setText("直接会员出单");
        four.setText("收益提升43%");
        fourBelow.setText("间接会员出单");
        five.setText("奖励12%");
        fiveBelow.setText("直属总监团队");
        six.setText("奖励6%");
        sixBelow.setText("间接总监团队");
    }

    /*会员和超级会员文案内容*/
    private void vipAndSuperVipOfficial() {
        sjquanyi.setText("升级总监权益");
        one.setText("所有权益");
        oneBelow.setText("超级会员");
        two.setText("收益提升55%");
        twoBelow.setText("自买返佣");
        three.setText("收益提升55%");
        threeBelow.setText("分享返佣");
        four.setText("奖励57%");
        fourBelow.setText("直接会员出单");
        five.setText("奖励37%");
        fiveBelow.setText("间接会员出单");
        six.setText("奖励6%");
        sixBelow.setText("直属总监团队");
    }

    private void initSwipeRefresh() {
        smartrefresh.setPrimaryColors(0xfffaa530, 0xfffaa530);
        smartrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                setUserData();/*用户基本信息*/
                getOtherData();/*获取团队用户信息*/
                initQuanXianOfficial();/*设置权限文案*/
                refreshLayout.finishRefresh(1200);
            }
        });
    }

    private void setUserData() {
        String avatar = PreferUtils.getString(context, "avatar");
        String nickName = PreferUtils.getString(context, "nickName");
        String level = PreferUtils.getString(context, "level");
        Glide.with(context).load(avatar).into(userIv);
        userName.setText(nickName);
        switch (level) {
            case "1":
                userLevel.setImageResource(R.mipmap.cjhy_top_wenzihuiyuan);
                break;
            case "2":
                userLevel.setImageResource(R.mipmap.cjhy_top_wenzichaojihuiyuan);
                break;
            case "3":
                userLevel.setImageResource(R.mipmap.cjhy_top_wenzizongjian);
                break;
            case "4":
                userLevel.setImageResource(R.mipmap.cjhy_top_wenzigaojizongjian);
                break;
        }
    }

    @OnClick({R.id.joinMyTeam})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.joinMyTeam:
                Intent intent = new Intent(context, InvitedFriendActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getOtherData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.USERLEVELCENTY + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("用户叔叔啊", response.toString());
                        LevelCentyBean bean = GsonUtil.GsonToBean(response.toString(), LevelCentyBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<LevelCentyBean.UpgradeUserBean> otherUserList = bean.getUpgradeUser();
                            List<LevelCentyBean.UpgradeConditionBean> upgradeCondition = bean.getUpgradeCondition();
                            otherType.setText(bean.getRank());
                            ourbossType.setText("我们的" + bean.getRank());
                            ruhejingsheng.setText(bean.getRankInfo());
                            if (otherUserList.size() > 0) {
                                setOtherDataView(otherUserList);/*用户头像的数据*/
                            }
                            if (upgradeCondition.size() > 0) {
                                initRecyclerview(upgradeCondition);/*进度条那里的数据*/
                            }
                        } else {
                            ToastUtils.showBackgroudCenterToast(context, bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void setOtherDataView(List<LevelCentyBean.UpgradeUserBean> otherUserList) {
        UserImageIvListAdapter adapter = new UserImageIvListAdapter(context, otherUserList);
        recyclerviewUserList.setAdapter(adapter);
        recyclerviewUserList.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                othernickName.setText(otherUserList.get(position).getNickname());
                otherMoney.setText(otherUserList.get(position).getLastMonthEarn());
            }
        });
        recyclerviewUserList.scrollToPosition(2);
    }

    private void initRecyclerview(List<LevelCentyBean.UpgradeConditionBean> upgradeCondition) {
        UserLevelUpgradeListAdapter adapter = new UserLevelUpgradeListAdapter(context, upgradeCondition);
        recyclerview.setAdapter(adapter);
    }
}
