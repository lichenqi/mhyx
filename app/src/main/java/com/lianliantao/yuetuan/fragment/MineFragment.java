package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.AppMsgActivity;
import com.lianliantao.yuetuan.activity.InvitedFriendActivity;
import com.lianliantao.yuetuan.activity.MyBaseHtml5Activity;
import com.lianliantao.yuetuan.activity.MyOrderListActivity;
import com.lianliantao.yuetuan.activity.MyTeamActivity;
import com.lianliantao.yuetuan.activity.SettingActivity;
import com.lianliantao.yuetuan.activity.WithdrawDepositerActivity;
import com.lianliantao.yuetuan.adapter.MineModuleAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.ConsultBean;
import com.lianliantao.yuetuan.bean.UserLevelBean;
import com.lianliantao.yuetuan.bean.UserMoneyBean;
import com.lianliantao.yuetuan.collect.CollectListActivity;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.custom_view.UPMarqueeView;
import com.lianliantao.yuetuan.dianpu.CheckUserBeian2ShopManager;
import com.lianliantao.yuetuan.mine_activity.ContantOurActivity;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends Fragment {
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.user_iv)
    CircleImageView userIv;
    @BindView(R.id.re_oval)
    RelativeLayout reOval;
    @BindView(R.id.swiperefreshlayout)
    SmartRefreshLayout swiperefreshlayout;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.ivUserLevel)
    ImageView ivUserLevel;
    @BindView(R.id.tvUserLevel)
    TextView tvUserLevel;
    @BindView(R.id.inviteCode)
    TextView inviteCode;
    @BindView(R.id.ivSetting)
    ImageView ivSetting;
    @BindView(R.id.ivMsg)
    ImageView ivMsg;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tvUserBalance)
    TextView tvUserBalance;
    @BindView(R.id.todayEstimate)
    TextView todayEstimate;
    @BindView(R.id.yestdayEstimate)
    TextView yestdayEstimate;
    @BindView(R.id.monthEstyimate)
    TextView monthEstyimate;
    @BindView(R.id.lastMonthEstyimate)
    TextView lastMonthEstyimate;
    @BindView(R.id.upmarqueeview)
    UPMarqueeView upmarqueeview;
    @BindView(R.id.tvLook)
    TextView tvLook;
    @BindView(R.id.llMyOrder)
    LinearLayout llMyOrder;
    @BindView(R.id.llIncoming)
    LinearLayout llIncoming;
    @BindView(R.id.llToday)
    LinearLayout llToday;
    @BindView(R.id.llYestday)
    LinearLayout llYestday;
    @BindView(R.id.llThisMonth)
    LinearLayout llThisMonth;
    @BindView(R.id.llLastMonth)
    LinearLayout llLastMonth;
    @BindView(R.id.llTeamOrder)
    LinearLayout llTeamOrder;
    @BindView(R.id.reMyincome)
    RelativeLayout reMyincome;
    @BindView(R.id.withDraw)
    TextView withDraw;
    @BindView(R.id.llDiTuiWuLiao)
    LinearLayout llDiTuiWuLiao;
    @BindView(R.id.llXinShouJiao)
    LinearLayout llXinShouJiao;
    @BindView(R.id.llShangXueYuan)
    LinearLayout llShangXueYuan;
    private View view;
    Context context;
    Intent intent;
    private String[] titles = {"邀请好友", "我的收藏", "常见问题", "推广行为规范", "联系我们",};
    private int[] images = {R.drawable.wd_zuo_iconyaoqing, R.drawable.wd_zuo_iconshoucang, R.drawable.wd_zuo_iconchangjianwenti
            , R.drawable.wd_zuo_iconxingweiguifan, R.drawable.wd_zuo_iconlianxiwomen};
    String avatar, nickname, invitationCode, userRank, level, income;
    private List<ConsultBean.ConsultInfoBean> informationList;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 声明一个订阅方法，用于接收事件
    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case CommonApi.USER_DATA_CHANGE:
                getUserData();/*获取用户金额信息接口*/
                break;
            case CommonApi.LOGIN_SUCCESS_LABEL:
                getUserData();/*获取用户金额信息接口*/
                initViewData();/*用户头像基本信息*/
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.minefragment, container, false);
            context = MyApplication.getInstance();
            ButterKnife.bind(this, view);
            EventBus.getDefault().register(this);
            int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(context);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
            layoutParams.height = statusBarHeight;
            viewHeight.setLayoutParams(layoutParams);
            getUserData();/*获取用户信息接口*/
            initRefresh();
            initViewData();/*用户基本资料*/
            initMoudleView();/*设置列表*/
            initZiXunData();
            getUserLevelData();/*获取用户等级接口*/
        }
        return view;
    }

    /*设置资讯数据*/
    private void initZiXunData() {
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.CONSULT_DATA + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("资讯数据", response.toString());
                        ConsultBean bean = GsonUtil.GsonToBean(response.toString(), ConsultBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            informationList = bean.getConsultInfo();
                            List<View> views = new ArrayList<>();
                            for (int i = 0; i < informationList.size(); i++) {
                                View view = LayoutInflater.from(context).inflate(R.layout.item_home_news_scroll, null);
                                TextView name = view.findViewById(R.id.name);
                                name.setText(informationList.get(i).getTitle());
                                views.add(view);
                            }
                            upmarqueeview.setViews(views);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }

    /*获取用户金额数据*/
    private void getUserData() {
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.MINEUSERMONEYDATA + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("我的数据", response.toString());
                        UserMoneyBean bean = GsonUtil.GsonToBean(response.toString(), UserMoneyBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            UserMoneyBean.BalanceInfoBean balanceInfo = bean.getBalanceInfo();
                            income = balanceInfo.getIncome();
                            tvUserBalance.setText(balanceInfo.getBalance());
                            todayEstimate.setText(balanceInfo.getTodayEarn());
                            yestdayEstimate.setText(balanceInfo.getYesterdayEarn());
                            monthEstyimate.setText(balanceInfo.getThisMonthEarn());
                            lastMonthEstyimate.setText(balanceInfo.getLastMonthEarn());
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

    private void initMoudleView() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        MineModuleAdapter adapter = new MineModuleAdapter(context, titles, images);
        recyclerview.setAdapter(adapter);
        adapter.setonclicklistener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                switch (position) {
                    case 0:/*邀请好友*/
                        intent = new Intent(context, InvitedFriendActivity.class);
                        startActivity(intent);
                        break;
                    case 1:/*我的收藏*/
                        intent = new Intent(context, CollectListActivity.class);
                        startActivity(intent);
                        break;
                    case 2:/*常见问题*/
                        intent = new Intent(context, MyBaseHtml5Activity.class);
                        intent.putExtra("url", PreferUtils.getString(context, "questionListLink"));
                        startActivity(intent);
                        break;
                    case 3:/*推广行为规范*/
                        intent = new Intent(context, MyBaseHtml5Activity.class);
                        intent.putExtra("url", PreferUtils.getString(context, "userRuleLink"));
                        startActivity(intent);
                        break;
                    case 4:/*联系我们*/
                        intent = new Intent(context, ContantOurActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /*用户基本资料赋值*/
    private void initViewData() {
        avatar = PreferUtils.getString(context, "avatar");
        nickname = PreferUtils.getString(context, "nickName");
        invitationCode = PreferUtils.getString(context, "invitationCode");
        userRank = PreferUtils.getString(context, "userRank");
        level = PreferUtils.getString(context, "level");
        Glide.with(context).load(avatar).into(userIv);
        nickName.setText(nickname);
        inviteCode.setText("邀请码: " + invitationCode);
        tvUserLevel.setText(userRank);
        switch (level) {
            case "1":
                ivUserLevel.setImageResource(R.drawable.wd_zuo_iconhuiyuan);
                break;
            case "2":
                ivUserLevel.setImageResource(R.drawable.super_vip);
                break;
            case "3":
                ivUserLevel.setImageResource(R.drawable.wd_zuo_iconzongjian);
                break;
            case "4":
                ivUserLevel.setImageResource(R.drawable.wd_zuo_icongaojizongjian);
                break;
        }
    }

    /*刷新控件*/
    private void initRefresh() {
        swiperefreshlayout.setPrimaryColors(0xfffc8a1c, 0xffEB873C);
        swiperefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getUserData();
                initViewData();
                getUserLevelData();/*获取用户等级接口*/
                refreshLayout.finishRefresh(1200);
            }
        });
    }


    @OnClick({R.id.user_iv, R.id.ivSetting, R.id.llMyOrder, R.id.llIncoming, R.id.llMyteam, R.id.reMyincome
            , R.id.llToday, R.id.llYestday, R.id.llThisMonth, R.id.llLastMonth, R.id.llTeamOrder, R.id.withDraw
            , R.id.ivMsg, R.id.llDiTuiWuLiao, R.id.llShangXueYuan, R.id.llXinShouJiao, R.id.tvLook})
    public void OnClik(View view) {
        switch (view.getId()) {
            case R.id.user_iv:
                intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ivSetting:
                intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.llMyOrder:
                intent = new Intent(context, MyOrderListActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.llTeamOrder:
                intent = new Intent(context, MyOrderListActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.llIncoming:
                intent = new Intent(context, InComingStatementActivity.class);
                intent.putExtra("income", income);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.reMyincome:
                intent = new Intent(context, InComingStatementActivity.class);
                intent.putExtra("income", income);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.llToday:
                intent = new Intent(context, InComingStatementActivity.class);
                intent.putExtra("income", income);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.llYestday:
                intent = new Intent(context, InComingStatementActivity.class);
                intent.putExtra("income", income);
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            case R.id.llThisMonth:
                intent = new Intent(context, InComingStatementActivity.class);
                intent.putExtra("income", income);
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
            case R.id.llLastMonth:
                intent = new Intent(context, InComingStatementActivity.class);
                intent.putExtra("income", income);
                intent.putExtra("position", 3);
                startActivity(intent);
                break;
            case R.id.llMyteam:
                intent = new Intent(context, MyTeamActivity.class);
                startActivity(intent);
                break;
            case R.id.withDraw:
                intent = new Intent(context, WithdrawDepositerActivity.class);
                startActivity(intent);
                break;
            case R.id.ivMsg:
                intent = new Intent(context, AppMsgActivity.class);
                startActivity(intent);
                break;
            case R.id.llDiTuiWuLiao:
                intent = new Intent(context, MyBaseHtml5Activity.class);
                intent.putExtra("url", "https://dk.gaoyong666.com/gylm/tuoshop/v1/offline?v=4&uid=8589470&utype=9&version=3.6.2");
                startActivity(intent);
                break;
            case R.id.llShangXueYuan:
                intent = new Intent(context, MyBaseHtml5Activity.class);
                intent.putExtra("url", PreferUtils.getString(context, "businessLink"));
                startActivity(intent);
                break;
            case R.id.llXinShouJiao:
                intent = new Intent(context, MyBaseHtml5Activity.class);
                intent.putExtra("url", PreferUtils.getString(context, "guidebookLink"));
                startActivity(intent);
                break;
            case R.id.tvLook:/*资讯查看点击*/
                setInformationClick();
                break;
        }
    }

    /*资讯点击方法*/
    private void setInformationClick() {
        int displayedChild = upmarqueeview.getDisplayedChild();
        ConsultBean.ConsultInfoBean bean = informationList.get(displayedChild);
        String type = bean.getType();
        String redirectType = bean.getRedirectType();
        String title = bean.getTitle();
        String url = bean.getUrl();
        if (type.equals("native")) {/*原生跳转*/
            if (url.contains("goodsdetail")) {/*跳转商品详情*/
                String[] split = url.split("=");
                JumpUtil.jump2ShopDetail(context, split[1]);
            }
        } else if (type.equals("h5")) {/*h5跳转*/
            if (redirectType.equals("mahua")) {/*内部跳转*/
                intent = new Intent(context, MyBaseHtml5Activity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            } else if (redirectType.equals("ali")) {/*店铺跳转*/
                String[] split = url.split("=");
                CheckUserBeian2ShopManager manager = new CheckUserBeian2ShopManager(context, split[1], activity);
                manager.check();
            }
        }
    }

    /*用户等级接口*/
    private void getUserLevelData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.USER_LEVEL + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("等级数据", response.toString());
                        UserLevelBean bean = GsonUtil.GsonToBean(response.toString(), UserLevelBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            String level = bean.getLevel();
                            tvUserLevel.setText(bean.getUserRank());
                            switch (level) {
                                case "1":
                                    ivUserLevel.setImageResource(R.drawable.wd_zuo_iconhuiyuan);
                                    break;
                                case "2":
                                    ivUserLevel.setImageResource(R.drawable.super_vip);
                                    break;
                                case "3":
                                    ivUserLevel.setImageResource(R.drawable.wd_zuo_iconzongjian);
                                    break;
                                case "4":
                                    ivUserLevel.setImageResource(R.drawable.wd_zuo_icongaojizongjian);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }

    FragmentActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
}
