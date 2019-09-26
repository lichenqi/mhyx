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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.SearchKeyWordActivity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.AppMsgReadBean;
import com.lianliantao.yuetuan.bean.BaseTitleBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.home_fragment.HomeOptimizationFragment;
import com.lianliantao.yuetuan.home_fragment.HomeOtherFragment;
import com.lianliantao.yuetuan.kotlin_activity.ChildThreadSendMsg2MainThread;
import com.lianliantao.yuetuan.kotlin_activity.MainThreadSendMsg2ChildThread;
import com.lianliantao.yuetuan.kotlin_activity.TablayoutTestActivity;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.SpUtil;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    @BindView(R.id.llParent)
    LinearLayout llParent;
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.re_search)
    RelativeLayout reSearch;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private View view;
    Context context;
    private List<Fragment> fragments;
    TabLayoutAdapter adapter;
    Bundle bundle;
    List<BaseTitleBean.CateInfoBean> cateInfoList;
    private Intent intent;

    // 声明一个订阅方法，用于接收事件
    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case CommonApi.HOME_TO_TOP:
                //拿到 appbar 的 behavior,让 appbar 滚动
                ViewGroup.LayoutParams layoutParams = appbarlayout.getLayoutParams();
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
                if (behavior instanceof AppBarLayout.Behavior) {
                    AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
                    //拿到下方tabs的y坐标，即为我要的偏移量
                    float y = 0;
                    //注意传递负值
                    appBarLayoutBehavior.setTopAndBottomOffset(0);
                }
                break;
            case "appMsgUpdate":/*消息接口更新*/
                getAppMsgIconData();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.home_fragment, container, false);
            context = MyApplication.getInstance();
            ButterKnife.bind(this, view);
            int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(context);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
            layoutParams.height = statusBarHeight;
            viewHeight.setLayoutParams(layoutParams);
            initView();
            getAppMsgIconData();
        }
        return view;
    }

    @OnClick({R.id.re_search, R.id.iv_msg})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.re_search:
                intent = new Intent(context, SearchKeyWordActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_msg:
                if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
//                    intent = new Intent(context, AppMsgActivity.class);
                    intent = new Intent(context, ChildThreadSendMsg2MainThread.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(context, MyWXLoginActivity.class));
                }
                break;
        }
    }

    private void initView() {
        cateInfoList = SpUtil.getList(context, CommonApi.TITLE_DATA_LIST);
        if (cateInfoList != null) {
            initBaseViewData();
        } else {
            /*获取头部标题数据*/
            getHeadTitleData();
        }
    }

    /*初始计首页基本数据*/
    private void initBaseViewData() {
        fragments = new ArrayList<>();
        HomeOptimizationFragment homeOptimizationFragment = new HomeOptimizationFragment(llParent, appbarlayout);
        bundle = new Bundle();
        bundle.putString("id", cateInfoList.get(0).getId());
        homeOptimizationFragment.setArguments(bundle);
        fragments.add(homeOptimizationFragment);
        for (int i = 1; i < cateInfoList.size(); i++) {
            HomeOtherFragment otherFragment = new HomeOtherFragment();
            bundle = new Bundle();
            bundle.putString("id", cateInfoList.get(i).getId());
            otherFragment.setArguments(bundle);
            fragments.add(otherFragment);
        }
        adapter = new TabLayoutAdapter(fragments, getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(fragments.size());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    int colorChange = PreferUtils.getInt(context, "colorChange", 0);
                    int recyclerviewHeight = PreferUtils.getInt(context, "recyclerviewHeight", 0);
                    if (recyclerviewHeight == 0) {
                        if (colorChange == 0) {
                            llParent.setBackgroundColor(0xfffa6025);
                            appbarlayout.setBackgroundColor(0xfffa6025);
                        } else {
                            llParent.setBackgroundColor(colorChange);
                            appbarlayout.setBackgroundColor(colorChange);
                        }
                    } else if (recyclerviewHeight == 1300) {
                        llParent.setBackgroundColor(0xfffa6025);
                        appbarlayout.setBackgroundColor(0xfffa6025);
                    }
                } else {
                    llParent.setBackgroundColor(0xfffa6025);
                    appbarlayout.setBackgroundColor(0xfffa6025);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class TabLayoutAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public TabLayoutAdapter(List<Fragment> fragments, FragmentManager fm) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return cateInfoList.get(position).getCateName();
        }

        @Override
        public int getCount() {
            return cateInfoList == null ? 0 : cateInfoList.size();
        }
    }

    /*
     * app通知消息是否阅读数据
     * */
    private void getAppMsgIconData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.APP_NEWS + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("消息啊", response.toString());
                        AppMsgReadBean bean = GsonUtil.GsonToBean(response.toString(), AppMsgReadBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            String hasNews = bean.getHasNews();
                            if (hasNews.equals("true")) {
                                ivMsg.setImageResource(R.mipmap.home_msg_icon);
                            } else {
                                ivMsg.setImageResource(R.mipmap.appmsg_red);
                            }
                        } else {
                            ivMsg.setImageResource(R.mipmap.appmsg_red);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ivMsg.setImageResource(R.mipmap.appmsg_red);
                    }
                });
    }

    /*获取头部标题数据*/
    private void getHeadTitleData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_DATA + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("指示器数据", response.toString());
                        BaseTitleBean bean = GsonUtil.GsonToBean(response.toString(), BaseTitleBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            cateInfoList = bean.getCateInfo();
                            if (cateInfoList == null) return;
                            if (cateInfoList.size() > 0) {
                                SpUtil.putList(context, CommonApi.TITLE_DATA_LIST, cateInfoList);
                                initBaseViewData();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

}
