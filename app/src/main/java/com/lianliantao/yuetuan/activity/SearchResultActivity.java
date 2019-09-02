package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.SearchListAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.SearchListBean;
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
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResultActivity extends OriginalActivity {
    String keyword;
    @BindView(R.id.re_search)
    RelativeLayout re_search;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_search_name)
    TextView tvSearchName;
    Intent intent;
    @BindView(R.id.synthesize)
    TextView synthesize;
    @BindView(R.id.commission)
    TextView commission;
    @BindView(R.id.saleVolumes)
    TextView saleVolumes;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.llPrice)
    LinearLayout llPrice;
    @BindView(R.id.llChoice)
    LinearLayout llChoice;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.nodata)
    RelativeLayout nodata;
    @BindView(R.id.switch_one)
    Switch switch_one;
    @BindView(R.id.to_top)
    ImageView to_top;
    @BindView(R.id.ivArrows)
    ImageView ivArrows;
    private int pageNum = 1;
    private List<SearchListBean.GoodsInfoBean> list = new ArrayList<>();
    SearchListAdapter searchListAdapter;
    private String hasCoupon = "false";/*是否显示优惠券*/
    private String sort = "";/*排序字段*/
    private boolean priceDown = false;/*价格字段*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresultactivity);
        ButterKnife.bind(this);
        intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        tvSearchName.setText(keyword);
        initRecyclerView();
        getListData();
        initSwitchData();
    }

    /*切换优惠券商品*/
    private void initSwitchData() {
        switch_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hasCoupon = "true";
                    pageNum = 1;
                    xrecyclerview.refresh();
                } else {
                    hasCoupon = "false";
                    pageNum = 1;
                    xrecyclerview.refresh();
                }
            }
        });
    }

    private void initRecyclerView() {
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        XRecyclerViewUtil.setView(xrecyclerview);
        searchListAdapter = new SearchListAdapter(getApplicationContext(), list);
        xrecyclerview.setAdapter(searchListAdapter);
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
        searchListAdapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                JumpUtil.jump2ShopDetail(getApplicationContext(), list.get(position - 1).getItemId());
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
                if (i > 1200) {
                    to_top.setVisibility(View.VISIBLE);
                } else {
                    to_top.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.re_search, R.id.synthesize, R.id.commission, R.id.saleVolumes, R.id.llPrice, R.id.to_top})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(CommonApi.NOTICESEARCHKEYWORDFINISH);
                finish();
                break;
            case R.id.re_search:
                finish();
                break;
            case R.id.synthesize:
                synthesize.setTextColor(0xffFC5203);
                commission.setTextColor(0xff000000);
                saleVolumes.setTextColor(0xff000000);
                price.setTextColor(0xff000000);
                ivArrows.setImageResource(R.drawable.jiage_daixuan);
                sort = "";
                pageNum = 1;
                xrecyclerview.refresh();
                break;
            case R.id.commission:
                synthesize.setTextColor(0xff000000);
                commission.setTextColor(0xffFC5203);
                saleVolumes.setTextColor(0xff000000);
                price.setTextColor(0xff000000);
                ivArrows.setImageResource(R.drawable.jiage_daixuan);
                sort = "tk_total_commi_des";
                pageNum = 1;
                xrecyclerview.refresh();
                break;
            case R.id.saleVolumes:
                synthesize.setTextColor(0xff000000);
                commission.setTextColor(0xff000000);
                saleVolumes.setTextColor(0xffFC5203);
                price.setTextColor(0xff000000);
                ivArrows.setImageResource(R.drawable.jiage_daixuan);
                sort = "total_sales_des";
                pageNum = 1;
                xrecyclerview.refresh();
                break;
            case R.id.llPrice:
                synthesize.setTextColor(0xff000000);
                commission.setTextColor(0xff000000);
                saleVolumes.setTextColor(0xff000000);
                price.setTextColor(0xffFC5203);
                if (priceDown) {
                    sort = "price_asc";
                    ivArrows.setImageResource(R.drawable.jiage_shang);
                } else {
                    sort = "price_des";
                    ivArrows.setImageResource(R.drawable.jiage_xia);
                }
                priceDown = !priceDown;
                pageNum = 1;
                xrecyclerview.refresh();
                break;
            case R.id.to_top:
                xrecyclerview.scrollToPosition(0);
                break;
        }
    }

    /*列表数据*/
    private void getListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", "5");
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "15");
        map.put("keywords", keyword);
        map.put("hasCoupon", hasCoupon);
        map.put("sort", sort);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_OTHER_DATA_LIST + mapParam)
                .tag(this)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        SearchListBean bean = GsonUtil.GsonToBean(response.toString(), SearchListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<SearchListBean.GoodsInfoBean> goodsInfo = bean.getGoodsInfo();
                            if (pageNum == 1 && goodsInfo.size() == 0) {
                                nodata.setVisibility(View.VISIBLE);
                                xrecyclerview.setVisibility(View.GONE);
                                xrecyclerview.setNoMore(true);
                                xrecyclerview.refreshComplete();
                                xrecyclerview.loadMoreComplete();
                            } else {
                                nodata.setVisibility(View.GONE);
                                xrecyclerview.setVisibility(View.VISIBLE);
                                if (pageNum == 1) {
                                    list.clear();
                                    list.addAll(goodsInfo);
                                    searchListAdapter.notifyDataSetChanged();
                                    xrecyclerview.refreshComplete();
                                } else {
                                    list.addAll(goodsInfo);
                                    searchListAdapter.notifyDataSetChanged();
                                    xrecyclerview.loadMoreComplete();
                                }
                            }
                        } else {
                            nodata.setVisibility(View.VISIBLE);
                            xrecyclerview.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        xrecyclerview.refreshComplete();
                        xrecyclerview.loadMoreComplete();
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }
}
