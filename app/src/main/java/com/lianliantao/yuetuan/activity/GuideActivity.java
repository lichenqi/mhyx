package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lianliantao.yuetuan.MainActivity;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.BaseTitleBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.common_manager.ConfigurationManager;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.UserSaveUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.SpUtil;
import com.lianliantao.yuetuan.util.StatusBarUtils;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.jump)
    TextView jump;
    @BindView(R.id.llpoint)
    LinearLayout llpoint;
    private int[] guides = {R.mipmap.guide_one, R.mipmap.guide_two, R.mipmap.guide_three};
    private ImageView[] indicators;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) > 0) {
            /**为了防止重复启动多个闪屏页面**/
            finish();
            return;
        }
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.guideactivity);
        ButterKnife.bind(this);
        UserSaveUtil.aboutuIdSave(getApplicationContext());
        indicators = new ImageView[guides.length];
        for (int i = 0; i < guides.length; i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_cycle_viewpager_indicator, null);
            ImageView iv = view.findViewById(R.id.image_indicator);
            indicators[i] = iv;
            llpoint.addView(view);
        }
        setIndicator(0);
        viewpager.setAdapter(new GuideAdapter());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                if (position == 2) {
                    jump.setVisibility(View.VISIBLE);
                } else {
                    jump.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*获取首页指示器标题*/
        getTitleData();
        ConfigurationManager manager = new ConfigurationManager(getApplicationContext());
        manager.manager();
    }


    /*设置指示器*/
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(R.drawable.guide_unchoose);
        }
        indicators[selectedPosition % indicators.length].setBackgroundResource(R.drawable.guide_choose);
    }

    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guides == null ? 0 : guides.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.guide_item, container, false);
            ImageView iv = view.findViewById(R.id.iv);
            iv.setImageResource(guides[position]);
            container.addView(view);
            return view;
        }
    }

    @OnClick({R.id.jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jump:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void getTitleData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_DATA + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("指示器数据", response.toString());
                        BaseTitleBean bean = GsonUtil.GsonToBean(response.toString(), BaseTitleBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<BaseTitleBean.CateInfoBean> cateInfo = bean.getCateInfo();
                            if (cateInfo == null) return;
                            if (cateInfo.size() > 0) {
                                SpUtil.putList(getApplicationContext(), CommonApi.TITLE_DATA_LIST, cateInfo);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }
}
