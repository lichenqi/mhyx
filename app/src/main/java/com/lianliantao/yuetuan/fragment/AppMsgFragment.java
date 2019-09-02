package com.lianliantao.yuetuan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.AppMsgAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.AppMsgBean;
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

public class AppMsgFragment extends LazyBaseFragment {

    String type;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    private Context context;
    private int pageNum = 1;
    private List<AppMsgBean.NoticeInfoBean> list = new ArrayList<>();
    AppMsgAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            type = arguments.getString("type");
        }
    }

    @Override
    protected void initData() {
        context = MyApplication.getInstance();
        ButterKnife.bind(this, getView());
        getData();
        initView();
    }

    private void initView() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AppMsgAdapter(context, list, type);
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
        map.put("type", type);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "10");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.APP_MSG + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("消息", response.toString());
                        AppMsgBean bean = GsonUtil.GsonToBean(response.toString(), AppMsgBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<AppMsgBean.NoticeInfoBean> noticeInfo = bean.getNoticeInfo();
                            if (noticeInfo.size() > 0) {
                                if (pageNum == 1) {
                                    list.clear();
                                    list.addAll(noticeInfo);
                                    adapter.notifyDataSetChanged();
                                    xrecyclerview.refreshComplete();
                                } else {
                                    list.addAll(noticeInfo);
                                    adapter.notifyDataSetChanged();
                                    xrecyclerview.loadMoreComplete();
                                }
                            } else {
                                xrecyclerview.refreshComplete();
                                xrecyclerview.loadMoreComplete();
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
        return R.layout.appmsgfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }
}
