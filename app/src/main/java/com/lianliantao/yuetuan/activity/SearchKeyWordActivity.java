package com.lianliantao.yuetuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.FuzzyAdater;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.FuzzyData;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.HistorySearchUtil;
import com.lianliantao.yuetuan.util.ParamUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchKeyWordActivity extends BaseTitleActivity {

    /*搜索关键字*/
    @BindView(R.id.ed_keyword)
    EditText edKeyword;
    /*取消搜索字的按钮*/
    @BindView(R.id.iv_cancel)
    RelativeLayout ivCancel;
    /*搜索按钮*/
    @BindView(R.id.finish)
    TextView finish;
    /*历史搜索头部大布局*/
    @BindView(R.id.re_histoy_notiy)
    RelativeLayout re_histoy_notiy;
    /*清空搜索按钮*/
    @BindView(R.id.iv_clean_histoy)
    ImageView ivCleanHistoy;
    /*历史搜索布局*/
    @BindView(R.id.tagFlowlayout)
    TagFlowLayout tagFlowlayout;
    /*模糊搜索布局*/
    @BindView(R.id.fuzzy_recycler)
    RecyclerView fuzzyRecycler;
    @BindView(R.id.llSearchParent)
    LinearLayout llSearchParent;

    @Override
    public int getContainerView() {
        return R.layout.searchkeywordactivity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case CommonApi.NOTICESEARCHKEYWORDFINISH:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMiddleTitle("搜索");
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        fuzzyRecycler.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        fuzzyRecycler.setLayoutManager(manager);
        getHistoryList();
        /*监听键盘输入的字*/
        setEditWatch();
        initEditTextView();
    }

    private void initEditTextView() {
        edKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    getFuzzyData(s.toString());
                    ivCancel.setVisibility(View.VISIBLE);
                } else {
                    fuzzyRecycler.setVisibility(View.GONE);
                    llSearchParent.setVisibility(View.VISIBLE);
                    ivCancel.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.finish, R.id.iv_clean_histoy, R.id.iv_cancel})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                String content = edKeyword.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast(getApplicationContext(), "请先输入搜索关键字");
                } else {
                    HistorySearchUtil.getInstance(getApplicationContext()).putNewSearch(content);//保存记录到数据库
                    getHistoryList();
                    Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                    intent.putExtra("keyword", content);
                    intent.putExtra("search_type", 1);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.iv_clean_histoy:
                HistorySearchUtil.getInstance(getApplicationContext()).deleteAllHistorySearch();
                getHistoryList();
                break;
            case R.id.iv_cancel:
                String content1 = edKeyword.getText().toString().trim();
                if (!TextUtils.isEmpty(content1)) {
                    edKeyword.setText("");
                    ivCancel.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void getHistoryList() {
        final List<String> list = HistorySearchUtil.getInstance(getApplicationContext()).queryHistorySearchList();
        Collections.reverse(list);
        if (list.size() > 0) {
            tagFlowlayout.setVisibility(View.VISIBLE);
            re_histoy_notiy.setVisibility(View.VISIBLE);
        } else {
            tagFlowlayout.setVisibility(View.GONE);
            re_histoy_notiy.setVisibility(View.GONE);
        }
        //设置标签数据
        TagAdapter adapter = new TagAdapter(list) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flow_layout, tagFlowlayout, false);
                TextView flow_tag = view.findViewById(R.id.flow_tag);
                flow_tag.setText(list.get(position));
                return view;
            }
        };
        tagFlowlayout.setAdapter(adapter);
        tagFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("keyword", list.get(position));
                intent.putExtra("search_type", 1);
                startActivityForResult(intent, 1);
                return false;
            }
        });
    }

    private void setEditWatch() {
        edKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = edKeyword.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.showToast(getApplicationContext(), "请先输入搜索关键字");
                    } else {
                        HistorySearchUtil.getInstance(getApplicationContext()).putNewSearch(content);//保存记录到数据库
                        getHistoryList();
                        Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                        intent.putExtra("keyword", content);
                        intent.putExtra("search_type", 1);
                        startActivityForResult(intent, 1);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getFuzzyData(String s) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("q", s);
        map.put("code", "utf-8");
        String param = ParamUtil.getMapParam(map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.FUZZY_DATA + param)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("模糊", response.toString());
                        FuzzyData fuzzyData = GsonUtil.GsonToBean(response.toString(), FuzzyData.class);
                        final List<List<String>> result = fuzzyData.getResult();
                        if (result.size() > 0) {
                            fuzzyRecycler.setVisibility(View.VISIBLE);
                            llSearchParent.setVisibility(View.GONE);
                            FuzzyAdater fuzzyAdater = new FuzzyAdater(getApplicationContext(), result);
                            fuzzyRecycler.setAdapter(fuzzyAdater);
                            fuzzyAdater.setonclicklistener(new OnItemClick() {
                                @Override
                                public void OnItemClickListener(View view, int position) {
                                    HistorySearchUtil.getInstance(getApplicationContext()).putNewSearch(result.get(position).get(0));//保存记录到数据库
                                    Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                                    intent.putExtra("keyword", result.get(position).get(0));
                                    intent.putExtra("search_type", 1);
                                    startActivityForResult(intent, 1);
                                    getHistoryList();
                                }
                            });
                        } else {
                            fuzzyRecycler.setVisibility(View.GONE);
                            llSearchParent.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        edKeyword.setFocusable(true);
        edKeyword.setFocusableInTouchMode(true);
        edKeyword.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edKeyword, 0);
            }
        }, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            String keyword = data.getStringExtra("keyword");
            edKeyword.setText(keyword);
            edKeyword.setSelection(keyword.length());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
