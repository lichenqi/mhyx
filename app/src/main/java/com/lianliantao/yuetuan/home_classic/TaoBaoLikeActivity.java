package com.lianliantao.yuetuan.home_classic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.SearchKeyWordActivity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.LikeBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.itemdecoration.GridLayoutManagerItemSpace;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.myutil.StatusBarUtils;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaoBaoLikeActivity extends OriginalActivity {
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartrefresh;
    private int pageNum = 1;
    private TaoBaoLikeAdapter adapter;
    private List<LikeBean.LikeInfoBean> list = new ArrayList<>();
    View headView;
    private TextView num;
    private RelativeLayout re_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.taobaolikeactivity);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        initSwipeRefresh();
        getData();
        initView();
        getNumData();
    }

    private void initSwipeRefresh() {
        smartrefresh.setPrimaryColors(0xffEB873C, 0xffE96849);
        smartrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getData();
                refreshLayout.finishRefresh(1000);
            }
        });
    }

    private void initView() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setPullRefreshEnabled(false);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        xrecyclerview.addItemDecoration(new GridLayoutManagerItemSpace(DensityUtils.dip2px(getApplicationContext(), 4)));
        adapter = new TaoBaoLikeAdapter(getApplicationContext(), list);
        xrecyclerview.setAdapter(adapter);
        headView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.taobao_like_head, null);
        xrecyclerview.addHeaderView(headView);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getData();
            }
        });
        adapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                String itemId = list.get(position - 2).getItemId();
                JumpUtil.jump2ShopDetail(getApplicationContext(), itemId);
            }
        });
        initHeadView();
        xrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int i = recyclerView.computeVerticalScrollOffset();
                if (i > 1200) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initHeadView() {
        ImageView back = headView.findViewById(R.id.back);
        num = headView.findViewById(R.id.num);
        re_search = headView.findViewById(R.id.re_search);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        re_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchKeyWordActivity.class));
            }
        });
    }

    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "20");
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.LIKEDATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        LikeBean bean = GsonUtil.GsonToBean(response.toString(), LikeBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<LikeBean.LikeInfoBean> goodsInfo = bean.getLikeInfo();
                            if (goodsInfo.size() > 0) {
                                if (pageNum == 1) {
                                    list.clear();
                                    list.addAll(goodsInfo);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    list.addAll(goodsInfo);
                                    adapter.notifyDataSetChanged();
                                    xrecyclerview.loadMoreComplete();
                                }
                            } else {
                                xrecyclerview.loadMoreComplete();
                            }
                        } else {
                            xrecyclerview.loadMoreComplete();
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void getNumData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.COUPON_NUM + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        CouponNumBean bean = GsonUtil.GsonToBean(response.toString(), CouponNumBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            num.setText("当前有 " + bean.getCoupon() + " 张优惠券");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }

    @OnClick({R.id.iv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv:
                xrecyclerview.scrollToPosition(0);
                break;
        }
    }
}
