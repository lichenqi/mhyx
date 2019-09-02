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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.MyBaseHtml5Activity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.CircleLabelBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HairRingFragment extends Fragment {

    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.course)
    TextView course;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private View view;
    Context context;
    private List<Fragment> fragments;
    private TabLayoutAdapter adapter;
    Bundle bundle;
    List<CircleLabelBean.CateInfoBean> cateInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.hairringfragment, container, false);
            ButterKnife.bind(this, view);
            context = MyApplication.getInstance();
            int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(context);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
            layoutParams.height = statusBarHeight;
            viewHeight.setLayoutParams(layoutParams);
            getData();
        }
        return view;
    }

    @OnClick({R.id.course})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.course:
                Intent intent = new Intent(context, MyBaseHtml5Activity.class);
                intent.putExtra("url", PreferUtils.getString(context, "guideLink"));
                startActivity(intent);
                break;
        }
    }

    private void getData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.CIRCLE_CLASSIC_LABEL + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("发圈分类标语", response.toString());
                        CircleLabelBean bean = GsonUtil.GsonToBean(response.toString(), CircleLabelBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            cateInfo = bean.getCateInfo();
                            if (cateInfo.size() > 0) {
                                initView();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }

    private void initView() {
        fragments = new ArrayList<>();
        GoodRecommendFragment fragment = new GoodRecommendFragment();
        bundle = new Bundle();
        bundle.putString("pid", cateInfo.get(0).getPid());
        fragment.setArguments(bundle);
        fragments.add(fragment);
        for (int i = 1; i < cateInfo.size(); i++) {
            YingXiaoOrXinShouOrShangXueYuanFragment otherFragment = new YingXiaoOrXinShouOrShangXueYuanFragment();
            bundle = new Bundle();
            bundle.putString("pid", cateInfo.get(i).getPid());
            bundle.putSerializable("cidinfo", (Serializable) cateInfo.get(i).getCidInfo());
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
            return cateInfo.get(position).getName();
        }

        @Override
        public int getCount() {
            return cateInfo == null ? 0 : cateInfo.size();
        }
    }

}
