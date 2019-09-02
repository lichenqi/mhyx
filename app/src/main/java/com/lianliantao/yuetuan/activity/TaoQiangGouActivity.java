package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.TaoQiangGouAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.HomeListBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.itemdecoration.GridLayoutManagerItemSpace;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.myutil.StatusBarUtils;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaoQiangGouActivity extends OriginalActivity {
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.zonghe)
    TextView zonghe;
    @BindView(R.id.yongjin)
    TextView yongjin;
    @BindView(R.id.xiaoliang)
    TextView xiaoliang;
    @BindView(R.id.tvJiage)
    TextView tvJiage;
    @BindView(R.id.ivJiage)
    ImageView ivJiage;
    @BindView(R.id.llJiage)
    LinearLayout llJiage;
    @BindView(R.id.llParent)
    LinearLayout llParent;
    @BindView(R.id.toTop)
    ImageView toTop;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartrefresh;
    private int pageNum = 1;
    private List<HomeListBean.GoodsInfoBean> list = new ArrayList<>();
    TaoQiangGouAdapter adapter;
    private View headView;
    private String sort = "";/*排序字段*/
    private boolean priceDown = false;/*价格字段*/
    /*颜色值变化*/
    private int colorId = 1;
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.taoqianggouactivity);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        Intent intent = getIntent();
        String titleContent = intent.getStringExtra("title");
        type = intent.getStringExtra("type");
        title.setText(titleContent);
        getData();
        initView();
        initRefresh();
    }

    private void initRefresh() {
        smartrefresh.setRefreshHeader(new ClassicsHeader(getApplicationContext()));
        smartrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getData();
                smartrefresh.finishRefresh(1000);
            }
        });
    }

    private int height, head_view_hight;

    private void initView() {
        height = DensityUtils.dip2px(getApplicationContext(), 160);
        head_view_hight = DensityUtils.dip2px(getApplicationContext(), 200);
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setPullRefreshEnabled(false);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        xrecyclerview.addItemDecoration(new GridLayoutManagerItemSpace(DensityUtils.dip2px(getApplicationContext(), 4)));
        adapter = new TaoQiangGouAdapter(getApplicationContext(), list);
        xrecyclerview.setAdapter(adapter);
        headView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.taoqiangguo_item, null);
        xrecyclerview.addHeaderView(headView);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getData();
            }
        });
        adapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                String itemId = list.get(position - 2).getItemId();
                JumpUtil.jump2ShopDetail(getApplicationContext(), itemId);
            }
        });
        initHeadView();
        xrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int i = recyclerView.computeVerticalScrollOffset();
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisiableChildView = layoutManager.findViewByPosition(position);
                int itemHeight = firstVisiableChildView.getHeight();
                int scollYDistance = (position) * itemHeight - firstVisiableChildView.getTop();
                if (scollYDistance - head_view_hight >= height) {
                    llParent.setVisibility(View.VISIBLE);
                    switch (colorId) {
                        case 1:
                            zonghe.setTextColor(0xfffc5203);
                            yongjin.setTextColor(0xff000000);
                            xiaoliang.setTextColor(0xff000000);
                            tvJiage.setTextColor(0xff000000);
                            ivJiage.setImageResource(R.drawable.jiage_daixuan);
                            break;
                        case 2:
                            zonghe.setTextColor(0xff000000);
                            yongjin.setTextColor(0xfffc5203);
                            xiaoliang.setTextColor(0xff000000);
                            tvJiage.setTextColor(0xff000000);
                            ivJiage.setImageResource(R.drawable.jiage_daixuan);
                            break;
                        case 3:
                            zonghe.setTextColor(0xff000000);
                            yongjin.setTextColor(0xff000000);
                            xiaoliang.setTextColor(0xfffc5203);
                            tvJiage.setTextColor(0xff000000);
                            ivJiage.setImageResource(R.drawable.jiage_daixuan);
                            break;
                        case 4:
                            zonghe.setTextColor(0xff000000);
                            yongjin.setTextColor(0xff000000);
                            xiaoliang.setTextColor(0xff000000);
                            tvJiage.setTextColor(0xfffc5203);
                            if (!priceDown) {
                                ivJiage.setImageResource(R.drawable.jiage_shang);
                            } else {
                                ivJiage.setImageResource(R.drawable.jiage_xia);
                            }
                            break;
                    }
                } else {
                    llParent.setVisibility(View.GONE);
                }
                if (i > 1200) {
                    toTop.setVisibility(View.VISIBLE);
                } else {
                    toTop.setVisibility(View.GONE);
                }
            }
        });
    }

    TextView synthesize, commission, saleVolumes, price;
    ImageView ivArrows;
    LinearLayout llPrice;

    private void initHeadView() {
        ImageView iv_head = headView.findViewById(R.id.iv_head);
        synthesize = headView.findViewById(R.id.synthesize);
        commission = headView.findViewById(R.id.commission);
        saleVolumes = headView.findViewById(R.id.saleVolumes);
        price = headView.findViewById(R.id.price);
        llPrice = headView.findViewById(R.id.llPrice);
        ivArrows = headView.findViewById(R.id.ivArrows);
        synthesize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synthesize();
            }
        });
        commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commission();
            }
        });
        saleVolumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saleVolumes();
            }
        });
        llPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price();
            }
        });
        if (type.equals("7")) {
            iv_head.setImageResource(R.mipmap.img_taoqianggou);
        } else if (type.equals("6")) {
            iv_head.setImageResource(R.mipmap.img_jiujiubaoyou_bg);
        }
    }

    /*点击综合按钮*/
    private void synthesize() {
        colorId = 1;
        synthesize.setTextColor(0xffFC5203);
        commission.setTextColor(0xff000000);
        saleVolumes.setTextColor(0xff000000);
        price.setTextColor(0xff000000);
        ivArrows.setImageResource(R.drawable.jiage_daixuan);
        sort = "";
        pageNum = 1;
        getData();
    }

    /*点击佣金按钮*/
    private void commission() {
        colorId = 2;
        synthesize.setTextColor(0xff000000);
        commission.setTextColor(0xffFC5203);
        saleVolumes.setTextColor(0xff000000);
        price.setTextColor(0xff000000);
        ivArrows.setImageResource(R.drawable.jiage_daixuan);
        sort = "tk_total_commi_des";
        pageNum = 1;
        getData();
    }

    /*点击销量按钮*/
    private void saleVolumes() {
        colorId = 3;
        synthesize.setTextColor(0xff000000);
        commission.setTextColor(0xff000000);
        saleVolumes.setTextColor(0xffFC5203);
        price.setTextColor(0xff000000);
        ivArrows.setImageResource(R.drawable.jiage_daixuan);
        sort = "total_sales_des";
        pageNum = 1;
        getData();
    }

    /*点击价格按钮*/
    private void price() {
        colorId = 4;
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
        getData();
    }

    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", type);
        map.put("sort", sort);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "20");
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.HOME_OTHER_DATA_LIST + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("淘抢购数据", response.toString());
                        HomeListBean bean = GsonUtil.GsonToBean(response.toString(), HomeListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<HomeListBean.GoodsInfoBean> goodsInfo = bean.getGoodsInfo();
                            if (goodsInfo.size() > 0) {
                                if (pageNum == 1) {
                                    list.clear();
                                    list.addAll(goodsInfo);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    list.addAll(goodsInfo);
                                    adapter.notifyDataSetChanged();
                                    xrecyclerview.loadMoreComplete();
                                }
                            } else {
                                xrecyclerview.loadMoreComplete();
                            }
                        } else {
                            xrecyclerview.loadMoreComplete();
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    @OnClick({R.id.back, R.id.zonghe, R.id.yongjin, R.id.xiaoliang, R.id.llJiage, R.id.toTop})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.zonghe:
                xrecyclerview.scrollToPosition(0);
                synthesize();
                break;
            case R.id.yongjin:
                xrecyclerview.scrollToPosition(0);
                commission();
                break;
            case R.id.xiaoliang:
                xrecyclerview.scrollToPosition(0);
                saleVolumes();
                break;
            case R.id.llJiage:
                xrecyclerview.scrollToPosition(0);
                price();
                break;
            case R.id.toTop:
                xrecyclerview.scrollToPosition(0);
                break;
        }
    }
}
