package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.OtherListBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dianpu.CheckUserBeian2ShopManager;
import com.lianliantao.yuetuan.fragment_adapter.BrandOtherListAdapter;
import com.lianliantao.yuetuan.itemdecoration.BrandBaseItem;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandOthertFragment extends LazyBaseFragment {

    @BindView(R.id.recyclerview)
    XRecyclerView recyclerview;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private String id;
    Context context;
    BrandOtherListAdapter adapter;
    private int pageNum = 1;
    private List<OtherListBean.BrandInfoBean> list = new ArrayList<>();

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
        getListData();
        setRefreshData();
        setRecyclerviewData();
    }

    private void setRecyclerviewData() {
        recyclerview.setHasFixedSize(true);
        XRecyclerViewUtil.setView(recyclerview);
        recyclerview.setPullRefreshEnabled(false);
        recyclerview.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerview.addItemDecoration(new BrandBaseItem(DensityUtils.dip2px(context, 1)));
        adapter = new BrandOtherListAdapter(context, list);
        recyclerview.setAdapter(adapter);
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getListData();
            }
        });
        adapter.setOnClickListener(new OnItemClick() {

            private Intent intent;

            @Override
            public void OnItemClickListener(View view, int position) {
                /*店铺链接去跳转*/
                if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                    String shopId = list.get(position - 1).getShopId();
                    CheckUserBeian2ShopManager manager = new CheckUserBeian2ShopManager(context, shopId, activity);
                    manager.check();
                } else {
                    intent = new Intent(context, MyWXLoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setRefreshData() {
        swiperefreshlayout.setColorSchemeColors(0xff000000);
        swiperefreshlayout.setProgressBackgroundColorSchemeColor(0xffffffff);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum = 1;
                        getListData();
                        swiperefreshlayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    /*列表数据*/
    private void getListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("cid", id);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "40");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp()
                .post()
                .url(CommonApi.BASEURL + CommonApi.BRAND_HEAD_DATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("其他列表数据", response.toString());
                        OtherListBean bean = GsonUtil.GsonToBean(response.toString(), OtherListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<OtherListBean.BrandInfoBean> brandInfo = bean.getBrandInfo();
                            if (pageNum == 1) {
                                list.clear();
                                list.addAll(brandInfo);
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                list.addAll(brandInfo);
                                adapter.notifyDataSetChanged();
                                recyclerview.loadMoreComplete();
                            }
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
        return R.layout.brandothertfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }

    FragmentActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
}
