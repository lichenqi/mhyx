package com.lianliantao.yuetuan.home_fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.HomeListBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.fragment_adapter.HomeOtherHeadAdapter;
import com.lianliantao.yuetuan.fragment_adapter.HomeOtherListAdapter;
import com.lianliantao.yuetuan.itemdecoration.GridLayoutManagerItemSpace;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*首页其他界面*/
public class HomeOtherFragment extends LazyBaseFragment {
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartrefresh;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Context context;
    private String id, uId;
    private int pageNum = 1;
    private List<HomeListBean.GoodsInfoBean> list = new ArrayList<>();
    HomeOtherListAdapter homeOtherListAdapter;
    private View headView;
    private RecyclerView recyclerview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            id = arguments.getString("id");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this, getView());
        context = MyApplication.getInstance();
        uId = PreferUtils.getString(context, "uId");
        getHeadData();
        getListData();
        setRecyclerview();
        initRefresh();/*刷新*/
    }

    private void initRefresh() {
        smartrefresh.setRefreshHeader(new ClassicsHeader(context));
        smartrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getListData();
                getHeadData();
                smartrefresh.finishRefresh(1000);
            }
        });
    }

    @OnClick({R.id.iv_top})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_top:
                xrecyclerview.scrollToPosition(0);
                EventBus.getDefault().post(CommonApi.HOME_TO_TOP);
                break;
        }
    }

    private void setRecyclerview() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setPullRefreshEnabled(false);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new GridLayoutManager(context, 2));
        xrecyclerview.addItemDecoration(new GridLayoutManagerItemSpace(DensityUtils.dip2px(context, 4)));
        homeOtherListAdapter = new HomeOtherListAdapter(context, list);
        xrecyclerview.setAdapter(homeOtherListAdapter);
        headView = LayoutInflater.from(context).inflate(R.layout.home_other_list_head, null);
        xrecyclerview.addHeaderView(headView);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getListData();
            }
        });
        initHeadView();
        homeOtherListAdapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                int i = position - 2;
                JumpUtil.jump2ShopDetail(context, list.get(i).getItemId());
            }
        });
        xrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int i = recyclerView.computeVerticalScrollOffset();
                if (i > 1300) {
                    ivTop.setVisibility(View.VISIBLE);
                } else {
                    ivTop.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initHeadView() {
        recyclerview = headView.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
    }

    /*获取列表数据*/
    private void getListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", "2");
        map.put("cid", id);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "20");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_OTHER_DATA_LIST + mapParam)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("首页其他列表数据", response.toString());
                        HomeListBean bean = GsonUtil.GsonToBean(response.toString(), HomeListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<HomeListBean.GoodsInfoBean> result = bean.getGoodsInfo();
                            if (result.size() > 0) {
                                if (pageNum == 1) {
                                    result.remove(0);
                                    result.remove(0);
                                    result.remove(0);
                                    list.clear();
                                    list.addAll(result);
                                    homeOtherListAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    list.addAll(result);
                                    homeOtherListAdapter.notifyDataSetChanged();
                                    xrecyclerview.loadMoreComplete();
                                }
                            } else {
                                xrecyclerview.loadMoreComplete();
                            }
                        } else {
                            xrecyclerview.loadMoreComplete();
                            ToastUtils.showToast(context, bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                        xrecyclerview.loadMoreComplete();
                    }
                });
    }

    private void getHeadData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", "2");
        map.put("cid", id);
        map.put("pageNo", "1");
        map.put("pageSize", "3");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_OTHER_DATA_LIST + mapParam)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("首页头部三条数据", response.toString());
                        HomeListBean bean = GsonUtil.GsonToBean(response.toString(), HomeListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<HomeListBean.GoodsInfoBean> list = bean.getGoodsInfo();
                            if (list.size() > 0) {
                                HomeOtherHeadAdapter homeOtherHeadAdapter = new HomeOtherHeadAdapter(context, list);
                                recyclerview.setAdapter(homeOtherHeadAdapter);
                                homeOtherHeadAdapter.setOnClickListener(new OnItemClick() {
                                    @Override
                                    public void OnItemClickListener(View view, int position) {
                                        JumpUtil.jump2ShopDetail(context, list.get(position).getItemId());
                                    }
                                });
                            }
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

    @Override
    protected int getLayoutId() {
        return R.layout.homeotherfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }
}
