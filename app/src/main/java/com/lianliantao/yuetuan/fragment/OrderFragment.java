package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.OrderListAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.OrderListBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFragment extends LazyBaseFragment {

    Context context;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.nodata)
    ImageView nodata;
    private int pageNum = 1;
    private List<OrderListBean.OrderInfoBean> list = new ArrayList<>();
    OrderListAdapter adapter;
    String tkStatus, type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            tkStatus = arguments.getString("tkStatus");
            type = arguments.getString("type");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this, getView());
        context = MyApplication.getInstance();
        initView();
        getListData();
    }

    private void getListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", type);
        map.put("tkStatus", tkStatus);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "10");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .tag(this)
                .url(CommonApi.BASEURL + CommonApi.MY_ORDER_LIST_DATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("我的订单", response.toString());
                        OrderListBean bean = GsonUtil.GsonToBean(response.toString(), OrderListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<OrderListBean.OrderInfoBean> orderInfo = bean.getOrderInfo();
                            if (pageNum == 1) {
                                if (orderInfo.size() == 0) {
                                    nodata.setVisibility(View.VISIBLE);
                                } else {
                                    nodata.setVisibility(View.GONE);
                                }
                                list.clear();
                                list.addAll(orderInfo);
                                xrecyclerview.refreshComplete();
                                adapter.notifyDataSetChanged();
                            } else {
                                list.addAll(orderInfo);
                                xrecyclerview.loadMoreComplete();
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            nodata.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void initView() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(context));
        adapter = new OrderListAdapter(context, list);
        xrecyclerview.setAdapter(adapter);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getListData();
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getListData();
            }
        });
        adapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                JumpUtil.jump2ShopDetail(context, list.get(position - 1).getItemId());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.orderfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }
}
