package com.lianliantao.yuetuan.collect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.CollectListAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.BaseBean;
import com.lianliantao.yuetuan.bean.HomeListBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectListActivity extends BaseTitleActivity {

    @BindView(R.id.nodata)
    LinearLayout nodata;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.ivCollectClick)
    ImageView ivCollectClick;
    @BindView(R.id.deleteRight)
    TextView deleteRight;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    private TextView tv_right_name;
    private int pageNum = 1;
    private List<HomeListBean.GoodsInfoBean> list = new ArrayList<>();
    private CollectListAdapter adapter;
    private boolean is_edit = true;
    private boolean isAllSelect = false;
    private LinkedHashMap<Integer, Integer> savePosition = new LinkedHashMap<>();
    private List<Integer> listTotalPosition;

    @Override
    public int getContainerView() {
        return R.layout.collectlistactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tv_right_name = findViewById(R.id.tv_right_name);
        setMiddleTitle("我的收藏");
        setRightTVVisible();
        tv_right_name.setText("编辑");
        getListData();
        initRecyclerview();
    }

    @OnClick({R.id.ivCollectClick, R.id.deleteRight, R.id.tv_right_name})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivCollectClick:
                if (isAllSelect) {
                    ivCollectClick.setImageResource(R.mipmap.collect_unchecked);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setChecked(false);
                        savePosition.remove(i);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ivCollectClick.setImageResource(R.mipmap.collect_checked);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setChecked(true);
                        savePosition.put(i, i);
                    }
                    adapter.notifyDataSetChanged();
                }
                isAllSelect = !isAllSelect;
                break;
            case R.id.deleteRight:/*删除按钮*/
                listTotalPosition = new ArrayList<>();
                Iterator<Map.Entry<Integer, Integer>> iterator = savePosition.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> next = iterator.next();
                    Integer value = next.getValue();
                    listTotalPosition.add(value);
                }
                Collections.sort(listTotalPosition);
                Log.i("打印选中", listTotalPosition + "");
                if (listTotalPosition.size() > 0) {
                    deletaData();
                } else {
                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "请至少选中一个商品才能删除");
                }
                break;
            case R.id.tv_right_name:/*编辑按钮*/
                if (is_edit) {
                    tv_right_name.setText("取消");
                    llBottom.setVisibility(View.VISIBLE);
                    xrecyclerview.setPullRefreshEnabled(false);
                    xrecyclerview.setLoadingMoreEnabled(false);
                    for (HomeListBean.GoodsInfoBean bean : list) {
                        bean.setDeleteShow(true);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    tv_right_name.setText("编辑");
                    llBottom.setVisibility(View.GONE);
                    xrecyclerview.setPullRefreshEnabled(true);
                    xrecyclerview.setLoadingMoreEnabled(true);
                    for (HomeListBean.GoodsInfoBean bean : list) {
                        bean.setDeleteShow(false);
                    }
                    adapter.notifyDataSetChanged();
                }
                is_edit = !is_edit;
                break;
        }
    }

    /*取消收藏商品接口*/
    private void deletaData() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listTotalPosition.size(); i++) {
            if (sb.length() > 0) {//该步即不会第一位有逗号，也防止最后一位拼接逗号！
                sb.append(",");
            }
            sb.append(list.get(listTotalPosition.get(i)).getItemId());
        }
        Log.i("打印拼接", sb + "");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", sb.toString());
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GOODCANCELCOLLECT + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("收藏信息", response.toString());
                        BaseBean bean = GsonUtil.GsonToBean(response.toString(), BaseBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            xrecyclerview.setPullRefreshEnabled(true);
                            xrecyclerview.refresh();
                            EventBus.getDefault().post(CommonApi.COLLECT_CHANGE);
                            savePosition.clear();
                        } else {
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void initRecyclerview() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new CollectListAdapter(list, getApplicationContext());
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
                JumpUtil.jump2ShopDetail(getApplicationContext(), list.get(position - 1).getItemId());
            }
        });
        adapter.setOnDeleteCollectLisrtener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                int positionWhich = position - 1;
                Log.i("点击的位置", positionWhich + "");
                if (list.get(positionWhich).isChecked()) {
                    savePosition.remove(positionWhich);
                    list.get(positionWhich).setChecked(false);
                } else {
                    savePosition.put(positionWhich, positionWhich);
                    list.get(positionWhich).setChecked(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", "4");
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "20");
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_OTHER_DATA_LIST + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("收藏信息", response.toString());
                        HomeListBean bean = GsonUtil.GsonToBean(response.toString(), HomeListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<HomeListBean.GoodsInfoBean> goodsInfo = bean.getGoodsInfo();
                            tv_right_name.setText("编辑");
                            llBottom.setVisibility(View.GONE);
                            is_edit = true;
                            if (pageNum == 1) {
                                if (goodsInfo.size() == 0) {
                                    nodata.setVisibility(View.VISIBLE);
                                } else {
                                    nodata.setVisibility(View.GONE);
                                }
                                list.clear();
                                list.addAll(goodsInfo);
                                adapter.notifyDataSetChanged();
                                xrecyclerview.refreshComplete();
                            } else {
                                list.addAll(goodsInfo);
                                adapter.notifyDataSetChanged();
                                xrecyclerview.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }
}
