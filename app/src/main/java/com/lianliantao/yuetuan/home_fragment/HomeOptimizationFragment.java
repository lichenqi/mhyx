package com.lianliantao.yuetuan.home_fragment;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.InvitedFriendActivity;
import com.lianliantao.yuetuan.adapter.HomeHeadClassicAdapter;
import com.lianliantao.yuetuan.adapter.HomeOptimizationAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.CheaperBean;
import com.lianliantao.yuetuan.bean.HomeHeadBean;
import com.lianliantao.yuetuan.bean.SearchListBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.custom_view.MyBannerRoundImageView;
import com.lianliantao.yuetuan.custom_view.ScrollingDigitialAnimationSecond;
import com.lianliantao.yuetuan.itemdecoration.CarItemDecoration;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.oneyuanandfreeorder.OneYuanBuyActivity;
import com.lianliantao.yuetuan.oneyuanandfreeorder.ZeroBuyActivity;
import com.lianliantao.yuetuan.port_inner.BannerOnClickListener;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*首页优选界面*/
public class HomeOptimizationFragment extends LazyBaseFragment implements ViewPager.OnPageChangeListener {

    Context context;
    @BindView(R.id.swiperefreshlayout)
    SmartRefreshLayout swiperefreshlayout;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.ivToTop)
    ImageView ivToTop;
    private int pageNum = 1;
    private List<SearchListBean.GoodsInfoBean> list = new ArrayList<>();
    HomeOptimizationAdapter adapter;
    HomeHeadBean homeHeadBean;
    View headView;
    int widthPixels, spaceheight;
    private RecyclerView recyclerview;
    private LinearLayout llParent;
    private AppBarLayout appbarlayout;
    private Timer timer = new Timer();
    private int money;/*为用户省钱*/

    /*设置间隔时间*/
    private void setTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
        }, 5000, 5000);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                int v = (int) (Math.random() * 100);
                money = v + money;
                dnView.setNumberString(String.valueOf(money));
            }
        }
    };

    /*用户可见和不可见方法设置*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {/*可见*/
            if (banner != null) {
                if (recyclerviewHeight < 1300) {
                    banner.startAutoPlay();
                }
            }
        } else {
            if (banner != null) {
                banner.stopAutoPlay();
            }
        }
    }

    public HomeOptimizationFragment() {
    }

    public HomeOptimizationFragment(LinearLayout llParent, AppBarLayout appbarlayout) {
        this.llParent = llParent;
        this.appbarlayout = appbarlayout;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.homeoptimizationfragment;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this, getView());
        context = MyApplication.getInstance();
        widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        spaceheight = DensityUtils.dip2px(context, 85);
        initRefreshLayout();
        getListData();
        initRecyclerview();
        getHeadData();/*获取头部商品数据*/
        getSaveMoneyData();/*省钱数据*/
        setTimer();/*设置定时器*/
    }

    private void getSaveMoneyData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.APP_CHEAPER + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        CheaperBean bean = GsonUtil.GsonToBean(response.toString(), CheaperBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            String cheaper = bean.getCheaper();
                            int v = (int) (Math.random() * 100);
                            money = Integer.parseInt(cheaper) * v;
                            int saveUserMoney = PreferUtils.getInt(context, "saveUserMoney", 0);
                            /*数字跳动*/
                            if (saveUserMoney > 0) {
                                dnView.setNumberString(String.valueOf(saveUserMoney + 123));
                            } else {
                                dnView.setNumberString(String.valueOf(money));
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }

    @OnClick({R.id.ivToTop})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivToTop:
                xrecyclerview.scrollToPosition(0);
                EventBus.getDefault().post(CommonApi.HOME_TO_TOP);
                break;
        }
    }

    int recyclerviewHeight;

    private void initRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(manager);
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setPullRefreshEnabled(false);
        adapter = new HomeOptimizationAdapter(list, context);
        xrecyclerview.setAdapter(adapter);
        headView = LayoutInflater.from(context).inflate(R.layout.home_youxuan_head, null);
        xrecyclerview.addHeaderView(headView);
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
                JumpUtil.jump2ShopDetail(context, list.get(position - 2).getItemId());
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
                recyclerviewHeight = recyclerView.computeVerticalScrollOffset();
                if (recyclerviewHeight > 1300) {
                    PreferUtils.putInt(context, "recyclerviewHeight", 1300);
                    ivToTop.setVisibility(View.VISIBLE);
                    llParent.setBackgroundColor(0xfffa6025);
                    appbarlayout.setBackgroundColor(0xfffa6025);
                    if (banner != null) {
                        banner.stopAutoPlay();
                    }
                } else {
                    PreferUtils.putInt(context, "recyclerviewHeight", 0);
                    ivToTop.setVisibility(View.GONE);
                    llParent.setBackgroundColor(evaluate);
                    appbarlayout.setBackgroundColor(evaluate);
                    if (banner != null) {
                        banner.startAutoPlay();
                    }
                }
            }
        });
        initHeadView();
    }

    Banner banner;
    ScrollingDigitialAnimationSecond dnView;
    View viewColor, zhongjianLine;
    ImageView todayFreeTwo, todayFreeThree, todayFreeFour, todayFreeOne, oneyuangou, yaoqinghaoyou;
    LinearLayout llLeft, llOneYuan, llYao;

    /*头部控件*/
    private void initHeadView() {
        llLeft = headView.findViewById(R.id.llLeft);
        llOneYuan = headView.findViewById(R.id.llOneYuan);
        llYao = headView.findViewById(R.id.llYao);
        viewColor = headView.findViewById(R.id.viewColor);
        todayFreeOne = headView.findViewById(R.id.todayFreeOne);
        todayFreeTwo = headView.findViewById(R.id.todayFreeTwo);
        todayFreeThree = headView.findViewById(R.id.todayFreeThree);
        todayFreeFour = headView.findViewById(R.id.todayFreeFour);
        zhongjianLine = headView.findViewById(R.id.zhongjianLine);
        yaoqinghaoyou = headView.findViewById(R.id.yaoqinghaoyou);
        oneyuangou = headView.findViewById(R.id.oneyuangou);
        recyclerview = headView.findViewById(R.id.recyclerview);
        banner = headView.findViewById(R.id.banner);
        dnView = headView.findViewById(R.id.dnView);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(context, 4));
        int space = DensityUtils.dip2px(context, 15);
        recyclerview.addItemDecoration(new CarItemDecoration(space));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) zhongjianLine.getLayoutParams();
        layoutParams.height = spaceheight + 315;
        zhongjianLine.setLayoutParams(layoutParams);
        /*轮播图基本设置*/
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnPageChangeListener(this);
        /*今日免单等点击事件*/
        todayFreeClick();
    }

    private void todayFreeClick() {
        /*邀请好友*/
        llYao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                    Intent intent = new Intent(context, InvitedFriendActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(context, MyWXLoginActivity.class));
                }
            }
        });
        /*一元购*/
        llOneYuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                    Intent intent = new Intent(context, OneYuanBuyActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(context, MyWXLoginActivity.class));
                }
            }
        });
        /*今日免单*/
        llLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                    Intent intent = new Intent(context, ZeroBuyActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(context, MyWXLoginActivity.class));
                }
            }
        });
    }

    private void initRefreshLayout() {
        swiperefreshlayout.setRefreshHeader(new ClassicsHeader(context));
        swiperefreshlayout.setEnableLoadMore(false);
        swiperefreshlayout.setEnableNestedScroll(true);//是否启用嵌套滚动
        swiperefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getListData();
                if (homeHeadBean == null) {
                    getHeadData();
                }
                getSaveMoneyData();
                refreshlayout.finishRefresh(1000);
            }
        });
    }

    /*数据*/
    private void getListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type", "1");
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "20");
        String otherParamSign = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.HOME_OTHER_DATA_LIST + otherParamSign)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        SearchListBean bean = GsonUtil.GsonToBean(response.toString(), SearchListBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<SearchListBean.GoodsInfoBean> goodsInfo = bean.getGoodsInfo();
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
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    List<HomeHeadBean.BannerInfoBean> bannerInfo;
    List<HomeHeadBean.MenuInfoBean> menuInfo;

    /*获取头部数据*/
    private void getHeadData() {
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.HOME_DATA + CommonParamUtil.getCommonParamSign(context))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("头部数据", response.toString());
                        homeHeadBean = GsonUtil.GsonToBean(response.toString(), HomeHeadBean.class);
                        if (homeHeadBean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<HomeHeadBean.ActivityInfoBean> activityInfo = homeHeadBean.getActivityInfo();
                            bannerInfo = homeHeadBean.getBannerInfo();
                            menuInfo = homeHeadBean.getMenuInfo();
                            setTodayFreeChargeData(activityInfo);/*今日免单相关数据*/
                            HomeHeadClassicAdapter adapter = new HomeHeadClassicAdapter(context, menuInfo, activity);/*首页分类数据 淘抢购数据等*/
                            recyclerview.setAdapter(adapter);
                            setBannerData(bannerInfo);/*轮播图设置数据*/
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }

    private void setBannerData(List<HomeHeadBean.BannerInfoBean> bannerInfo) {
        List<String> bannerList = new ArrayList<>();
        for (int i = 0; i < bannerInfo.size(); i++) {
            bannerList.add(bannerInfo.get(i).getLogo());
        }
        banner.setOnBannerListener(new BannerOnClickListener(context, bannerInfo, activity));
        banner.setImageLoader(new MyBannerRoundImageView()).setImages(bannerInfo).start();
    }

    private void setTodayFreeChargeData(List<HomeHeadBean.ActivityInfoBean> activityInfo) {
        if (activityInfo.size() > 0) {
            String logo = activityInfo.get(0).getLogo();
            String[] split = logo.split(",");
            Glide.with(context).load(split[0]).asBitmap()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            //原始图片宽高
                            todayFreeOne.setImageBitmap(resource);
                        }
                    });
            Glide.with(context).load(split[1]).asBitmap()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            //原始图片宽高
                            todayFreeTwo.setImageBitmap(resource);
                        }
                    });
            Glide.with(context).load(split[2]).asBitmap()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            //原始图片宽高
                            todayFreeThree.setImageBitmap(resource);
                        }
                    });
            Glide.with(context).load(split[3]).asBitmap()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            //原始图片宽高
                            todayFreeFour.setImageBitmap(resource);
                        }
                    });
            Glide.with(context).load(activityInfo.get(1).getLogo()).into(oneyuangou);
            Glide.with(context).load(activityInfo.get(2).getLogo()).into(yaoqinghaoyou);
        }
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }

    String first_color, second_color;
    int evaluate;

    /*banner滑动监听事件*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        first_color = bannerInfo.get(position % bannerInfo.size()).getColor();
        if (position % bannerInfo.size() == bannerInfo.size() - 1) {
            second_color = bannerInfo.get(0).getColor();
        } else {
            second_color = bannerInfo.get(position % bannerInfo.size() + 1).getColor();
        }
        int one_color = Color.parseColor(first_color);
        int two_color = Color.parseColor(second_color);
        evaluate = (Integer) argbEvaluator.evaluate(positionOffset, one_color, two_color);
        viewColor.setBackgroundColor(evaluate);
        if (appbarlayout != null) {
            appbarlayout.setBackgroundColor(evaluate);
        }
        if (llParent != null) {
            llParent.setBackgroundColor(evaluate);
        }
        PreferUtils.putInt(context, "colorChange", evaluate);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (context != null) {
            PreferUtils.putInt(context, "saveUserMoney", money);
        }
    }
}
