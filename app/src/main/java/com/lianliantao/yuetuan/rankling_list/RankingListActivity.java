package com.lianliantao.yuetuan.rankling_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.HomeListBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.myutil.StatusBarUtils;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RankingListActivity extends OriginalActivity {

    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartrefresh;
    private String type;
    private String title;
    private int pageNum = 1;
    private RankingListAdapter adapter;
    private List<HomeListBean.GoodsInfoBean> list = new ArrayList<>();
    private View viewHeight;
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivHead;
    private int statusBarHeight;
    private LinearLayout llTitleParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.rankling_list_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        type = intent.getStringExtra("type");
        getData();
        initRefresh();
        initView();
    }

    @OnClick({R.id.iv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv:
                xrecyclerview.scrollToPosition(0);
                break;
        }
    }

    private void initRefresh() {
        smartrefresh.setRefreshHeader(new ClassicsHeader(getApplicationContext()));
        smartrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getData();
                smartrefresh.finishRefresh(1000);
            }
        });
    }

    private void initView() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setPullRefreshEnabled(false);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new RankingListAdapter(getApplicationContext(), list);
        xrecyclerview.setAdapter(adapter);
        View headView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.ranking_list_item_head, null);
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
        llTitleParent = headView.findViewById(R.id.llTitleParent);
        viewHeight = headView.findViewById(R.id.viewHeight);
        ivBack = headView.findViewById(R.id.ivBack);
        tvTitle = headView.findViewById(R.id.tvTitle);
        ivHead = headView.findViewById(R.id.ivHead);
        initHeadView();
    }

    private void initHeadView() {
        if (type.equals("9")) {
            ivHead.setImageResource(R.mipmap.img_gaoyongjin);
            tvTitle.setText("高佣金商品");
            llTitleParent.setBackgroundResource(R.mipmap.gaoyongjin_title_bg);
        } else if (type.equals("8")) {
            ivHead.setImageResource(R.mipmap.taobaopaihangbang);
            tvTitle.setText("出单排行版");
            llTitleParent.setBackgroundResource(R.mipmap.taobao_paihangbang_title);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", type);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "20");
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_OTHER_DATA_LIST + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("排行榜数据", response.toString());
                        HomeListBean bean = GsonUtil.GsonToBean(response.toString(), HomeListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<HomeListBean.GoodsInfoBean> goodsInfo = bean.getGoodsInfo();
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
}
