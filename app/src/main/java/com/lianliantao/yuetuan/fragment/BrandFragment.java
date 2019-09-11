package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.BrandBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dianpu.CheckUserBeian2ShopManager;
import com.lianliantao.yuetuan.fragment_adapter.BrandHeadAdapter;
import com.lianliantao.yuetuan.itemdecoration.CarItemDecoration;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandFragment extends Fragment {
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.brandname)
    TextView brandname;
    @BindView(R.id.head_recyclerview)
    RecyclerView head_recyclerview;
    @BindView(R.id.llChageTitle)
    LinearLayout llChageTitle;
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.white_brand_name)
    TextView white_brand_name;
    private View view;
    Context context;
    private List<Fragment> fragments;
    TabLayoutAdapter adapter;
    Bundle bundle;
    int statusBarHeight, i;
    List<BrandBean.CateInfoBean> cateInfo;
    private Intent intent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 声明一个订阅方法，用于接收事件
    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case CommonApi.APPBARLAYOUTTOTOP:
                //拿到 appbar 的 behavior,让 appbar 滚动
                ViewGroup.LayoutParams layoutParams = appbarlayout.getLayoutParams();
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
                if (behavior instanceof AppBarLayout.Behavior) {
                    AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
                    //拿到下方tabs的y坐标，即为我要的偏移量
                    float y = brandname.getY() - 100;
                    //注意传递负值
                    appBarLayoutBehavior.setTopAndBottomOffset((int) -y);
                }
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.brandfragment, container, false);
            context = MyApplication.getInstance();
            ButterKnife.bind(this, view);
            statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(context);
            i = DensityUtils.dip2px(context, 50);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) brandname.getLayoutParams();
            layoutParams.setMargins(0, statusBarHeight, 0, 0);
            brandname.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParamsView = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
            layoutParamsView.height = statusBarHeight;
            viewHeight.setLayoutParams(layoutParamsView);
            getHeadData();/*头部列表数据和指示器数据*/
            setRecyclerView();
            initAppBarLayoutListener();
        }
        return view;
    }

    private void initAppBarLayoutListener() {
        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int abs = Math.abs(i);
                if (abs <= 0) {//未滑动
                    llChageTitle.setVisibility(View.GONE);
                } else if (abs > 0) {
                    llChageTitle.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setTabLayoutView() {
        fragments = new ArrayList<>();
        BrandChooseFragment allFragment = new BrandChooseFragment();
        bundle = new Bundle();
        bundle.putString("id", cateInfo.get(0).getId());
        allFragment.setArguments(bundle);
        fragments.add(allFragment);
        for (int i = 1; i < cateInfo.size(); i++) {
            BrandOthertFragment otherFragment = new BrandOthertFragment();
            bundle = new Bundle();
            bundle.putString("id", cateInfo.get(i).getId());
            otherFragment.setArguments(bundle);
            fragments.add(otherFragment);
        }
        adapter = new TabLayoutAdapter(fragments, getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(fragments.size());
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
            return cateInfo.get(position).getCateName();
        }

        @Override
        public int getCount() {
            return cateInfo == null ? 0 : cateInfo.size();
        }
    }

    private void setRecyclerView() {
        head_recyclerview.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        head_recyclerview.setLayoutManager(manager);
        int space = DensityUtils.dip2px(context, 15);
        head_recyclerview.addItemDecoration(new CarItemDecoration(space));
    }

    /*头部列表数据*/
    private void getHeadData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("cid", "");
        map.put("pageNo", "1");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post().url(CommonApi.BASEURL + CommonApi.BRAND_HEAD_DATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("头部列表数据", response.toString());
                        BrandBean bean = GsonUtil.GsonToBean(response.toString(), BrandBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<BrandBean.BrandInfoBean> brandInfoList = bean.getBrandInfo();
                            cateInfo = bean.getCateInfo();
                            if (brandInfoList.size() > 0) {
                                head_recyclerview.setVisibility(View.VISIBLE);
                                BrandHeadAdapter brandHeadAdapter = new BrandHeadAdapter(context, brandInfoList);
                                head_recyclerview.setAdapter(brandHeadAdapter);
                                /*获取指示器数据*/
                                if (cateInfo.size() > 0) {
                                    tablayout.setVisibility(View.VISIBLE);
                                    setTabLayoutView();
                                }
                                brandHeadAdapter.setOnClickListerner(new OnItemClick() {

                                    @Override
                                    public void OnItemClickListener(View view, int position) {
                                        /*店铺链接去跳转*/
                                        if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                                            String shopId = brandInfoList.get(position).getShopId();
                                            CheckUserBeian2ShopManager manager = new CheckUserBeian2ShopManager(context, shopId, activity);
                                            manager.check();
                                        } else {
                                            intent = new Intent(context, MyWXLoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
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
