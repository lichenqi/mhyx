package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.BrandPrefectureActivity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.BrandChooseBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.fragment_adapter.BrandChooseAdapter;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ParamUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*精选界面*/
public class BrandChooseFragment extends LazyBaseFragment {

    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.ivToTop)
    ImageView ivToTop;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    private String id;
    Context context;
    private int pageNum = 1;
    private List<BrandChooseBean.BannerInfoBean> list = new ArrayList<>();
    BrandChooseAdapter adapter;
    Intent intent;

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
        setRecyclerview();
        initSwipeRefresh();
    }

    @OnClick({R.id.ivToTop})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivToTop:
                EventBus.getDefault().post(CommonApi.APPBARLAYOUTTOTOP);
                xrecyclerview.scrollToPosition(0);
                break;
        }
    }

    private void initSwipeRefresh() {
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

    private void setRecyclerview() {
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(context));
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setPullRefreshEnabled(false);
        adapter = new BrandChooseAdapter(context, list);
        xrecyclerview.setAdapter(adapter);
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
        adapter.setonclicklistener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                int i = position - 1;
                if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                    intent = new Intent(context, BrandPrefectureActivity.class);
                    intent.putExtra("brandDetail", list.get(i).getBrandDetail());
                    intent.putExtra("brandName", list.get(i).getBrandName());
                    intent.putExtra("logo", list.get(i).getLogo());
                    intent.putExtra("bid", list.get(i).getBid());
                    intent.putExtra("shopId", list.get(i).getShopId());
                } else {
                    intent = new Intent(context, MyWXLoginActivity.class);
                }
                startActivity(intent);
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
                    ivToTop.setVisibility(View.VISIBLE);
                } else {
                    ivToTop.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "20");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp()
                .post()
                .url(CommonApi.BASEURL + CommonApi.BRAND_CHOOSE_LIST + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("精选数据", response.toString());
                        BrandChooseBean bean = GsonUtil.GsonToBean(response.toString(), BrandChooseBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<BrandChooseBean.BannerInfoBean> result = bean.getBannerInfo();
                            if (result.size() == 0) {
                                xrecyclerview.setNoMore(true);
                                xrecyclerview.loadMoreComplete();
                            } else {
                                if (pageNum == 1) {
                                    list.clear();
                                    list.addAll(result);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    list.addAll(result);
                                    adapter.notifyDataSetChanged();
                                    xrecyclerview.loadMoreComplete();
                                }
                            }
                        } else {
                            xrecyclerview.setNoMore(true);
                            xrecyclerview.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        xrecyclerview.loadMoreComplete();
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.brandchoosefragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }
}
