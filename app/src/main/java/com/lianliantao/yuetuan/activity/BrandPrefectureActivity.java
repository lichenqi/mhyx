package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.BrandPrefectureAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.SearchListBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dianpu.CheckUserBeian2ShopManager;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.StatusBarUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrandPrefectureActivity extends OriginalActivity {

    String brandDetail, logo, brandName, bid, shopId;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.iv)
    RoundedImageView iv;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.entryShop)
    TextView entryShop;
    @BindView(R.id.detailname)
    TextView detailname;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.retitletop)
    RelativeLayout retitletop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.brandprefectureactivity);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) retitletop.getLayoutParams();
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        retitletop.setLayoutParams(layoutParams);
        Intent intent = getIntent();
        brandDetail = intent.getStringExtra("brandDetail");
        brandName = intent.getStringExtra("brandName");
        logo = intent.getStringExtra("logo");
        bid = intent.getStringExtra("bid");
        shopId = intent.getStringExtra("shopId");
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getData();
        Glide.with(getApplicationContext()).load(logo).into(iv);
        name.setText(brandName);
        detailname.setText(brandDetail);
    }

    @OnClick({R.id.entryShop, R.id.back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.entryShop:
                /*店铺链接跳转*/
                CheckUserBeian2ShopManager manager = new CheckUserBeian2ShopManager(getApplicationContext(), shopId, this);
                manager.check();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", "3");
        map.put("bid", bid);
        map.put("pageNo", "1");
        map.put("pageSize", "30");
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.HOME_OTHER_DATA_LIST + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        SearchListBean bean = GsonUtil.GsonToBean(response.toString(), SearchListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<SearchListBean.GoodsInfoBean> list = bean.getGoodsInfo();
                            BrandPrefectureAdapter brandPrefectureAdapter = new BrandPrefectureAdapter(getApplicationContext(), list);
                            recyclerview.setAdapter(brandPrefectureAdapter);
                            brandPrefectureAdapter.setOnItemClickListener(new OnItemClick() {
                                @Override
                                public void OnItemClickListener(View view, int position) {
                                    JumpUtil.jump2ShopDetail(getApplicationContext(), list.get(position).getItemId());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

}
