package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.JieSuanLogAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.JieSuanBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JieSuanLogFragment extends LazyBaseFragment {

    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.nodata)
    ImageView nodata;
    Context context;
    JieSuanLogAdapter adapter;
    private int pageNum = 1;
    private List<JieSuanBean.SettlementLogBean> list = new ArrayList<>();

    @Override
    protected void initData() {
        context = MyApplication.getInstance();
        ButterKnife.bind(this, getView());
        getData();
        iniview();
    }

    private void iniview() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(context));
        adapter = new JieSuanLogAdapter(context, list);
        xrecyclerview.setAdapter(adapter);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getData();
            }
        });
    }

    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "10");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.JIESUAN_LOG + mapParam)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("结算列表", response.toString());
                        JieSuanBean bean = GsonUtil.GsonToBean(response.toString(), JieSuanBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<JieSuanBean.SettlementLogBean> settlementLog = bean.getSettlementLog();
                            if (pageNum == 1) {
                                if (settlementLog.size() == 0) {
                                    nodata.setVisibility(View.VISIBLE);
                                    xrecyclerview.setVisibility(View.GONE);
                                } else {
                                    nodata.setVisibility(View.GONE);
                                    xrecyclerview.setVisibility(View.VISIBLE);
                                }
                                list.clear();
                                list.addAll(settlementLog);
                                adapter.notifyDataSetChanged();
                                xrecyclerview.refreshComplete();
                            } else {
                                list.addAll(settlementLog);
                                adapter.notifyDataSetChanged();
                                xrecyclerview.loadMoreComplete();
                            }
                        } else {
                            xrecyclerview.refreshComplete();
                            xrecyclerview.loadMoreComplete();
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
        return R.layout.tixianrecordfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }
}
