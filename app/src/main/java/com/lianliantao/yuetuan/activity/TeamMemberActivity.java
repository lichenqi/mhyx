package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.TeamMemberAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.TeamMemberBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
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
import butterknife.OnClick;

public class TeamMemberActivity extends BaseTitleActivity {

    @BindView(R.id.inputPhone)
    EditText inputPhone;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.nodata)
    ImageView nodata;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    private int pageNum = 1;
    String type;
    String phone = "";
    TeamMemberAdapter adapter;
    private List<TeamMemberBean.TeamListBean> list = new ArrayList<>();

    @Override
    public int getContainerView() {
        return R.layout.teammemberactivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        setMiddleTitle("团队成员");
        initView();
        getData(phone);
        setEditWatch();
    }

    @OnClick({R.id.iv_search})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                phone = inputPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showToast(getApplicationContext(), "请先输入搜索手机号");
                    return;
                }
                pageNum = 1;
                getData(phone);
                break;
        }
    }

    private void initView() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new TeamMemberAdapter(getApplicationContext(), list);
        xrecyclerview.setAdapter(adapter);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getData(phone);
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getData(phone);
            }
        });
    }

    private void getData(String phone) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", type);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "10");
        map.put("mobile", phone);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.TEAM_MEMBER_DATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("团队认识", response.toString());
                        TeamMemberBean bean = GsonUtil.GsonToBean(response.toString(), TeamMemberBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<TeamMemberBean.TeamListBean> teamList = bean.getTeamList();
                            if (pageNum == 1) {
                                if (teamList.size() == 0) {
                                    nodata.setVisibility(View.VISIBLE);
                                } else {
                                    nodata.setVisibility(View.GONE);
                                }
                                list.clear();
                                list.addAll(teamList);
                                adapter.notifyDataSetChanged();
                                xrecyclerview.refreshComplete();
                            } else {
                                list.addAll(teamList);
                                adapter.notifyDataSetChanged();
                                xrecyclerview.loadMoreComplete();
                            }
                        } else {
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                            xrecyclerview.loadMoreComplete();
                            xrecyclerview.refreshComplete();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void setEditWatch() {
        inputPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    phone = inputPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtils.showToast(getApplicationContext(), "请先输入搜索手机号");
                    } else {
                        pageNum = 1;
                        getData(phone);
                    }
                    return true;
                }
                return false;
            }
        });
    }

}
