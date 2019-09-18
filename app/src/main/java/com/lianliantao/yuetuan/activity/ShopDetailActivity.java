package com.lianliantao.yuetuan.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lianliantao.yuetuan.MainActivity;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.adapter.GoodRecommendAdapter;
import com.lianliantao.yuetuan.adapter.LikeAdapter;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.BaseBean;
import com.lianliantao.yuetuan.bean.CollectBean;
import com.lianliantao.yuetuan.bean.GoodRecommendBean;
import com.lianliantao.yuetuan.bean.GoodsDecribeBean;
import com.lianliantao.yuetuan.bean.LikeBean;
import com.lianliantao.yuetuan.bean.PromotionlinkBean;
import com.lianliantao.yuetuan.bean.ShopDeailBean;
import com.lianliantao.yuetuan.collect.CollectListActivity;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.dialogfragment.BaseNiceDialog;
import com.lianliantao.yuetuan.dialogfragment.NiceDialog;
import com.lianliantao.yuetuan.dialogfragment.ViewConvertListener;
import com.lianliantao.yuetuan.dialogfragment.ViewHolder;
import com.lianliantao.yuetuan.dianpu.ShopLinkBean;
import com.lianliantao.yuetuan.itemdecoration.CainiXiHuanItem;
import com.lianliantao.yuetuan.itemdecoration.XiangGuangTuiJianItem;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;
import com.lianliantao.yuetuan.util.StatusBarUtils;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.view.RoundBannerLoader;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDetailActivity extends OriginalActivity {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.nestedscrollview)
    NestedScrollView nestedscrollview;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBuyNum)
    TextView tvBuyNum;
    @BindView(R.id.tvSalePrice)
    TextView tvSalePrice;
    @BindView(R.id.tvOldPrice)
    TextView tvOldPrice;
    @BindView(R.id.ll_sale_price)
    LinearLayout llSalePrice;
    @BindView(R.id.estimateMoney)
    TextView estimateMoney;
    @BindView(R.id.tvCommentNums)
    TextView tvCommentNums;
    @BindView(R.id.circleimageview)
    CircleImageView circleimageview;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvComment)
    TextView tvComment;
    @BindView(R.id.shopLogo)
    RoundedImageView shopLogo;
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.ivShopType)
    ImageView ivShopType;
    @BindView(R.id.entryShop)
    TextView entryShop;
    @BindView(R.id.tvBoby)
    TextView tvBoby;
    @BindView(R.id.ivBoby)
    ImageView ivBoby;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.ivSell)
    ImageView ivSell;
    @BindView(R.id.tvLogistics)
    TextView tvLogistics;
    @BindView(R.id.ivLogistics)
    ImageView ivLogistics;
    @BindView(R.id.recyclerviewRecommend)
    RecyclerView recyclerviewRecommend;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.recyclerviewLike)
    RecyclerView recyclerviewLike;
    @BindView(R.id.couponMoney)
    TextView couponMoney;
    @BindView(R.id.userTime)
    TextView userTime;
    @BindView(R.id.immediatelyGet)
    TextView immediatelyGet;
    @BindView(R.id.couponTextStyle)
    TextView couponTextStyle;
    @BindView(R.id.ivToTop)
    ImageView ivToTop;
    @BindView(R.id.reTitleChange)
    RelativeLayout reTitleChange;
    @BindView(R.id.iv_yuanxing_back)
    ImageView iv_yuanxing_back;
    @BindView(R.id.iv_to_collect_list)
    ImageView iv_to_collect_list;
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.shangPing)
    TextView shangPing;
    @BindView(R.id.shangPingLine)
    View shangPingLine;
    @BindView(R.id.xiangqing)
    TextView xiangqing;
    @BindView(R.id.xiangqingLine)
    View xiangqingLine;
    @BindView(R.id.tuijian)
    TextView tuijian;
    @BindView(R.id.tuijianLine)
    View tuijianLine;
    @BindView(R.id.ivLikeList)
    ImageView ivLikeList;
    @BindView(R.id.llShangPing)
    LinearLayout llShangPing;
    @BindView(R.id.llXiangQing)
    LinearLayout llXiangQing;
    @BindView(R.id.llTuiJian)
    LinearLayout llTuiJian;
    @BindView(R.id.viewAboveTuijian)
    View viewAboveTuijian;
    @BindView(R.id.tvLikeWenBen)
    TextView tvLikeWenBen;
    @BindView(R.id.llCollect)
    LinearLayout llCollect;
    @BindView(R.id.ivCollectLogo)
    ImageView ivCollectLogo;
    @BindView(R.id.tvCollectWenben)
    TextView tvCollectWenben;
    @BindView(R.id.buySaveMoney)
    TextView buySaveMoney;
    @BindView(R.id.shareMoney)
    TextView shareMoney;
    @BindView(R.id.tvUpgradeContent)
    TextView tvUpgradeContent;
    @BindView(R.id.tvToUpgrade)
    TextView tvToUpgrade;
    @BindView(R.id.llShareMakeMoney)
    LinearLayout llShareMakeMoney;
    @BindView(R.id.llBuySheng)
    LinearLayout llBuySheng;
    @BindView(R.id.llLookAllEvaluate)
    LinearLayout llLookAllEvaluate;
    @BindView(R.id.xiangguantuijian)
    TextView xiangguantuijian;
    @BindView(R.id.llbabymiaoshu)
    LinearLayout llbabymiaoshu;
    @BindView(R.id.reOnePersonalContent)
    RelativeLayout reOnePersonalContent;
    @BindView(R.id.reSonParent)
    RelativeLayout reSonParent;
    @BindView(R.id.llTypeface)
    LinearLayout llTypeface;
    @BindView(R.id.shangPingxiangqing)
    TextView shangPingxiangqing;
    @BindView(R.id.relijishengji)
    RelativeLayout relijishengji;
    private String itemId;
    ShopDeailBean.GoodsDetailBean goodsDetail;
    GoodsDecribeBean goodsDecribeBean;
    Intent intent;
    /*收藏标识*/
    private boolean isCollect = false;
    int statusBarHeight;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    String couponClickUrl;
    private String evaluateUrl;/*查看全部链接*/
    private int fiftyHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.shop_detail_activity);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        LinearLayout.LayoutParams layoutOne = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutOne.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutOne);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) reTitleChange.getLayoutParams();
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        reTitleChange.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParamsBanner = (LinearLayout.LayoutParams) banner.getLayoutParams();
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        layoutParamsBanner.height = widthPixels;
        banner.setLayoutParams(layoutParamsBanner);
        intent = getIntent();
        itemId = intent.getStringExtra("itemId");
        alibcShowParams = new AlibcShowParams();
        alibcShowParams.setOpenType(OpenType.Native);
        alibcShowParams.setBackUrl("alisdk://");
        fiftyHeight = DensityUtils.dip2px(getApplicationContext(), 50);
        getShopDetailData();/*商品详情信息接口*/
        initRecyclerviewRecommendView();
        getGoodRecommendListData();/*水平的相关推荐接口*/
        initRecyclerviewLike();
        getLikeData();/*猜你喜欢接口*/
        initNestedScrollView();/*滑动监听*/
        goodTitleCopy();/*商品标题长按复制*/
        getGoodsDescribeData();/*商品描述接口*/
        initWebView();/*商品详情用h5实现*/
        getGoodCollectData();/*商品收藏信息*/
    }

    private void getGoodCollectData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GOOD_COLLECT_DATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("收藏信息", response.toString());
                        CollectBean bean = GsonUtil.GsonToBean(response.toString(), CollectBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            String hasCollect = bean.getHasCollect();
                            if (hasCollect.equals("false")) {
                                ivCollectLogo.setImageResource(R.drawable.nolike);
                                tvCollectWenben.setText("收藏");
                                tvCollectWenben.setTextColor(0xff585858);
                                isCollect = false;
                            } else {
                                ivCollectLogo.setImageResource(R.mipmap.liked);
                                tvCollectWenben.setText("已收藏");
                                tvCollectWenben.setTextColor(0xffF6223E);
                                isCollect = true;
                            }
                        } else {
                            ivCollectLogo.setImageResource(R.drawable.nolike);
                            tvCollectWenben.setText("收藏");
                            tvCollectWenben.setTextColor(0xff585858);
                            isCollect = false;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ivCollectLogo.setImageResource(R.drawable.nolike);
                        tvCollectWenben.setText("收藏");
                        tvCollectWenben.setTextColor(0xff585858);
                        isCollect = false;
                    }
                });
    }

    /*商品详情用h5实现*/
    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        WebSettings settings = webview.getSettings();
        webview.setVerticalScrollBarEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
    }

    private void goodTitleCopy() {
        tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String title = goodsDetail.getTitle();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", title);
                cm.setPrimaryClip(mClipData);
                ToastUtils.showBackgroudCenterToast(getApplicationContext(), "标题复制成功");
                ClipContentUtil.getInstance(getApplicationContext()).putNewSearch(title);//保存记录到数据库
                return false;
            }
        });
    }

    /*滑动监听*/
    private void initNestedScrollView() {
        nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    reTitleChange.setVisibility(View.VISIBLE);
                    viewHeight.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
                    reSonParent.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                    ivBack.setVisibility(View.GONE);
                    llTypeface.setVisibility(View.GONE);
                    ivLikeList.setVisibility(View.GONE);
                    shangPing.setTextColor(0xffFC5203);
                    shangPingLine.setVisibility(View.VISIBLE);
                    xiangqing.setTextColor(0xff585858);
                    xiangqingLine.setVisibility(View.INVISIBLE);
                    tuijian.setTextColor(0xff585858);
                    tuijianLine.setVisibility(View.INVISIBLE);
                    getShopDetailData();
                } else if (scrollY > 0 && scrollY <= 600) {
                    reTitleChange.setVisibility(View.GONE);
                    ivBack.setVisibility(View.VISIBLE);
                    llTypeface.setVisibility(View.VISIBLE);
                    ivLikeList.setVisibility(View.VISIBLE);
                    float scale = (float) scrollY / 600;
                    float alpha = (255 * scale);
                    // 只是layout背景透明
                    viewHeight.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
                    reSonParent.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else if (scrollY > 600 && scrollY <= shangPingxiangqing.getTop() - fiftyHeight) {
                    viewHeight.setBackgroundColor(Color.argb((int) 255, 0, 0, 0));
                    reSonParent.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    reTitleChange.setVisibility(View.GONE);
                    ivBack.setVisibility(View.VISIBLE);
                    llTypeface.setVisibility(View.VISIBLE);
                    ivLikeList.setVisibility(View.VISIBLE);
                } else if (scrollY >= shangPingxiangqing.getTop() - fiftyHeight && scrollY <= tvLikeWenBen.getTop() - fiftyHeight) {/*滑动到商品详情处*/
                    viewHeight.setBackgroundColor(Color.argb((int) 255, 0, 0, 0));
                    reSonParent.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    reTitleChange.setVisibility(View.GONE);
                    ivBack.setVisibility(View.VISIBLE);
                    llTypeface.setVisibility(View.VISIBLE);
                    ivLikeList.setVisibility(View.VISIBLE);
                    shangPing.setTextColor(0xff585858);
                    shangPingLine.setVisibility(View.INVISIBLE);
                    xiangqing.setTextColor(0xffFC5203);
                    xiangqingLine.setVisibility(View.VISIBLE);
                    tuijian.setTextColor(0xff585858);
                    tuijianLine.setVisibility(View.INVISIBLE);
                } else if (scrollY >= tvLikeWenBen.getTop() - fiftyHeight) {/*滑动到猜你喜欢处*/
                    viewHeight.setBackgroundColor(Color.argb((int) 255, 0, 0, 0));
                    reSonParent.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    reTitleChange.setVisibility(View.GONE);
                    ivBack.setVisibility(View.VISIBLE);
                    llTypeface.setVisibility(View.VISIBLE);
                    ivLikeList.setVisibility(View.VISIBLE);
                    shangPing.setTextColor(0xff585858);
                    shangPingLine.setVisibility(View.INVISIBLE);
                    xiangqing.setTextColor(0xff585858);
                    xiangqingLine.setVisibility(View.INVISIBLE);
                    tuijian.setTextColor(0xffFC5203);
                    tuijianLine.setVisibility(View.VISIBLE);
                }
                /*一键置顶按钮*/
                if (scrollY > 1300) {
                    ivToTop.setVisibility(View.VISIBLE);
                } else {
                    ivToTop.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.ivToTop, R.id.iv_yuanxing_back, R.id.llShangPing, R.id.llXiangQing, R.id.llTuiJian
            , R.id.ivBack, R.id.ivLikeList, R.id.iv_to_collect_list, R.id.llCollect, R.id.tvToUpgrade
            , R.id.immediatelyGet, R.id.llShareMakeMoney, R.id.llBuySheng, R.id.llLookAllEvaluate, R.id.entryShop})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivToTop:
                nestedscrollview.scrollTo(0, 0);
                break;
            case R.id.iv_yuanxing_back:
                finish();
                break;
            case R.id.llShangPing:
                shangPing.setTextColor(0xffFC5203);
                shangPingLine.setVisibility(View.VISIBLE);
                xiangqing.setTextColor(0xff585858);
                xiangqingLine.setVisibility(View.INVISIBLE);
                tuijian.setTextColor(0xff585858);
                tuijianLine.setVisibility(View.INVISIBLE);
                nestedscrollview.scrollTo(0, 0);
                break;
            case R.id.llXiangQing:
                shangPing.setTextColor(0xff585858);
                shangPingLine.setVisibility(View.INVISIBLE);
                xiangqing.setTextColor(0xffFC5203);
                xiangqingLine.setVisibility(View.VISIBLE);
                tuijian.setTextColor(0xff585858);
                tuijianLine.setVisibility(View.INVISIBLE);
                if (list.size() > 0) {
                    nestedscrollview.scrollTo(0, recyclerviewRecommend.getBottom() - DensityUtils.dip2px(getApplication(), 40) - statusBarHeight);
                } else {
                    nestedscrollview.scrollTo(0, viewAboveTuijian.getBottom() - DensityUtils.dip2px(getApplication(), 40) - statusBarHeight);
                }
                break;
            case R.id.llTuiJian:
                shangPing.setTextColor(0xff585858);
                shangPingLine.setVisibility(View.INVISIBLE);
                xiangqing.setTextColor(0xff585858);
                xiangqingLine.setVisibility(View.INVISIBLE);
                tuijian.setTextColor(0xffFC5203);
                tuijianLine.setVisibility(View.VISIBLE);
                nestedscrollview.scrollTo(0, tvLikeWenBen.getBottom() - statusBarHeight - DensityUtils.dip2px(getApplicationContext(), 40));
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivLikeList:
                /*收藏列表*/
                intent = new Intent(getApplicationContext(), CollectListActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_to_collect_list:
                /*收藏列表*/
                intent = new Intent(getApplicationContext(), CollectListActivity.class);
                startActivity(intent);
                break;
            case R.id.llCollect:/*点击收藏按钮*/
                if (isCollect) {
                    cancelCollect();
                } else {
                    collectData();
                }
                break;
            case R.id.tvToUpgrade:/*升级按钮*/
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("loginout", "vipCenty");
                startActivity(intent);
                finish();
                break;
            case R.id.immediatelyGet:/*立即领取*/
                if (!TextUtils.isEmpty(couponClickUrl)) {
                    initAliPageData();
                } else {
                    /*未备案*/
                    taobaoBeiAn(200);
                }
                break;
            case R.id.llShareMakeMoney:/*分享赚 要判断用户是否淘宝备案*/
                if (goodsDetail == null) {
                    ToastUtils.showToast(getApplicationContext(), "商品信息获取中");
                    return;
                }
                if (!TextUtils.isEmpty(couponClickUrl)) {/*已备案*/
                    intent = new Intent(getApplicationContext(), GoodShareActivity.class);
                    intent.putExtra("itemId", itemId);/*商品id*/
                    intent.putExtra("imagesList", goodsDetail.getSmallImages());
                    intent.putExtra("title", goodsDetail.getTitle());
                    intent.putExtra("payPrice", goodsDetail.getPayPrice());
                    intent.putExtra("zkFinalPrice", goodsDetail.getZkFinalPrice());
                    intent.putExtra("volume", goodsDetail.getVolume());
                    intent.putExtra("estimatedEarn", goodsDetail.getEstimatedEarn());
                    intent.putExtra("couponAmount", goodsDetail.getCouponAmount());
                    intent.putExtra("userType", goodsDetail.getUserType());
                    intent.putExtra("pictUrl", goodsDetail.getPictUrl());
                    intent.putExtra("couponClickUrl", couponClickUrl);
                    startActivity(intent);
                } else {/*未备案*/
                    taobaoBeiAn(100);
                }
                break;
            case R.id.llBuySheng:/*购买省*/
                if (!TextUtils.isEmpty(couponClickUrl)) {
                    initAliPageData();
                } else {
                    /*未备案*/
                    taobaoBeiAn(200);
                }
                break;
            case R.id.llLookAllEvaluate:/*查看全部评论按钮*/
                intent = new Intent(getApplicationContext(), TaoBaoBaseH5Activity.class);
                intent.putExtra("evaluateUrl", evaluateUrl);
                startActivity(intent);
                break;
            case R.id.entryShop:/*进店逛逛*/
                getShopLink();
                break;
        }
    }

    Dialog loadingDialog;

    /*淘宝渠道备案*/
    private void taobaoBeiAn(int requestCode) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i, String s, String s1) {
                Session session = alibcLogin.getSession();
                String nick = session.nick;/*淘宝昵称*/
                String avatarUrl = session.avatarUrl;/*淘宝头像*/
                intent = new Intent(getApplicationContext(), TaoBaoAuthActivity.class);
                intent.putExtra("nick", nick);
                intent.putExtra("avatarUrl", avatarUrl);
                startActivityForResult(intent, requestCode);
            }

            @Override
            public void onFailure(int i, String s) {
                DialogUtil.closeDialog(loadingDialog, ShopDetailActivity.this);
            }
        });
    }

    NiceDialog alipay_gif_dialog;

    /*阿里百川唤起淘宝*/
    private void initAliPageData() {
        alipay_gif_dialog = NiceDialog.init();
        alipay_gif_dialog.setLayoutId(R.layout.alipay_gif_dialog);
        alipay_gif_dialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                ImageView iv = holder.getView(R.id.iv);
                TextView tv = holder.getView(R.id.tv);
                Glide.with(getApplicationContext()).load(R.mipmap.alipay_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                tv.setText("付款后预估赚 ¥ " + goodsDetail.getEstimatedEarn());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> exParams = new HashMap<>();
                        exParams.put("isv_code", "appisvcode");
                        exParams.put("alibaba", "阿里巴巴");
                        // 以显示传入url的方式打开页面（第二个参数是套件名称 暂时传“”）
                        AlibcTrade.openByUrl(ShopDetailActivity.this, "", couponClickUrl, null,
                                new WebViewClient(), new WebChromeClient(), alibcShowParams,
                                null, exParams, new AlibcTradeCallback() {
                                    @Override
                                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                    }
                                });
                    }
                }, 2000);
            }
        });
        alipay_gif_dialog.setMargin(120);
        alipay_gif_dialog.show(getSupportFragmentManager());
    }

    private void initRecyclerviewLike() {
        recyclerviewLike.setHasFixedSize(true);
        recyclerviewLike.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        int space = DensityUtils.dip2px(getApplicationContext(), 4);
        recyclerviewLike.addItemDecoration(new CainiXiHuanItem(space));
    }

    /*相关推荐设置*/
    private void initRecyclerviewRecommendView() {
        recyclerviewRecommend.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerviewRecommend.setLayoutManager(linearLayoutManager);
        int space = DensityUtils.dip2px(getApplicationContext(), 10);
        recyclerviewRecommend.addItemDecoration(new XiangGuangTuiJianItem(space));
    }

    /*获取商品详情信息*/
    private void getShopDetailData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.SHOPDETAILAPI + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("商品详情", response.toString());
                        ShopDeailBean bean = GsonUtil.GsonToBean(response.toString(), ShopDeailBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            goodsDetail = bean.getGoodsDetail();
                            couponClickUrl = goodsDetail.getCouponClickUrl();/*淘宝唤起二合一链接*/
                            setBannerDataView();/*banner设置*/
                            setShopBasicViewData();/*商品基本信息*/
                            setShopViewData();/*店铺信息*/
                            setCouponData();/*优惠券信息*/
                            shareAndBuyData();/*分享赚和购买省*/
                            String upgradeEarnInfo = goodsDetail.getUpgradeEarnInfo();
                            if (TextUtils.isEmpty(upgradeEarnInfo)) {
                                relijishengji.setVisibility(View.GONE);
                            } else {
                                relijishengji.setVisibility(View.VISIBLE);
                                tvUpgradeContent.setText(upgradeEarnInfo);
                            }
                        } else {
                            ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void shareAndBuyData() {
        String estimatedEarn = goodsDetail.getEstimatedEarn();
        shareMoney.setText(estimatedEarn);
        buySaveMoney.setText(estimatedEarn);
    }

    private void setCouponData() {
        String couponAmount = goodsDetail.getCouponAmount();
        if (!TextUtils.isEmpty(couponAmount)) {
            Integer couponNum = Integer.valueOf(couponAmount);
            if (couponNum > 0) {
                couponTextStyle.setVisibility(View.VISIBLE);
                couponMoney.setText(goodsDetail.getCouponAmount());
                userTime.setText("使用期限： " + goodsDetail.getCouponStartTime() + "至" + goodsDetail.getCouponEndTime());
            } else {
                setCommonCouponView();
            }
        } else {
            setCommonCouponView();
        }
    }

    private void setCommonCouponView() {
        couponTextStyle.setVisibility(View.GONE);
        couponMoney.setText("暂无优惠券");
        userTime.setText("*注：分享或购买后可获得佣金");
    }

    /*店铺信息*/
    private void setShopViewData() {
        Glide.with(getApplicationContext()).load(goodsDetail.getShopPict()).into(shopLogo);
        shopName.setText(goodsDetail.getShopTitle());
        if (goodsDetail.getShopType().equals("天猫")) {
            ivShopType.setImageResource(R.drawable.tianmao_shop_logo);
        } else {
            ivShopType.setImageResource(R.drawable.taobao_shop_logo);
        }
    }

    /*商品描述接口*/
    private void getGoodsDescribeData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GOODSDESCRIBEDATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("商品描述", response.toString());
                        goodsDecribeBean = GsonUtil.GsonToBean(response.toString(), GoodsDecribeBean.class);
                        if (goodsDecribeBean.getErrno() == CommonApi.RESULTCODEOK) {
                            setBabyViewData();/*宝贝评价*/
                            setPingFenViewData(goodsDecribeBean.getShopEvaluate());/*评分设置*/
                            String itemInfoUrl = goodsDecribeBean.getItemInfoUrl();  /*商品图片链接*/
                            evaluateUrl = goodsDecribeBean.getEvaluateUrl();
                            webview.loadUrl(itemInfoUrl);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    /*评分设置*/
    private void setPingFenViewData(List<GoodsDecribeBean.ShopEvaluateBean> shopEvaluate) {
        if (shopEvaluate.size() > 0) {
            llbabymiaoshu.setVisibility(View.VISIBLE);
            GoodsDecribeBean.ShopEvaluateBean oneBean = shopEvaluate.get(0);
            GoodsDecribeBean.ShopEvaluateBean twoBean = shopEvaluate.get(1);
            GoodsDecribeBean.ShopEvaluateBean threeBean = shopEvaluate.get(2);
            tvBoby.setText(oneBean.getTitle() + " " + oneBean.getScore());
            switch (oneBean.getRank()) {
                case "down":
                    ivBoby.setImageResource(R.drawable.img_di);
                    break;
                case "up":
                    ivBoby.setImageResource(R.drawable.img_gao);
                    break;
                case "equal":
                    ivBoby.setImageResource(R.drawable.img_ping);
                    break;
            }
            tvSell.setText(twoBean.getTitle() + " " + twoBean.getScore());
            switch (twoBean.getRank()) {
                case "down":
                    ivSell.setImageResource(R.drawable.img_di);
                    break;
                case "up":
                    ivSell.setImageResource(R.drawable.img_gao);
                    break;
                case "equal":
                    ivSell.setImageResource(R.drawable.img_ping);
                    break;
            }
            tvLogistics.setText(threeBean.getTitle() + " " + threeBean.getScore());
            switch (threeBean.getRank()) {
                case "down":
                    ivLogistics.setImageResource(R.drawable.img_di);
                    break;
                case "up":
                    ivLogistics.setImageResource(R.drawable.img_gao);
                    break;
                case "equal":
                    ivLogistics.setImageResource(R.drawable.img_ping);
                    break;
            }
        } else {
            llbabymiaoshu.setVisibility(View.GONE);
        }
    }

    private void setBabyViewData() {
        String evaluateTotal = goodsDecribeBean.getEvaluateTotal();
        if (evaluateTotal.equals("0")) {
            reOnePersonalContent.setVisibility(View.GONE);
            tvComment.setVisibility(View.GONE);
            tvCommentNums.setText("宝贝评价");
        } else {
            reOnePersonalContent.setVisibility(View.VISIBLE);
            tvComment.setVisibility(View.VISIBLE);
            tvCommentNums.setText("宝贝评价(" + goodsDecribeBean.getEvaluateTotal() + "人)");
            Glide.with(getApplicationContext()).load(goodsDecribeBean.getEvaluateUserLogo()).into(circleimageview);
            tvNickName.setText(goodsDecribeBean.getEvaluateUsername());
            tvComment.setText(goodsDecribeBean.getEvaluateContent());
        }
    }

    /*商品基本信息*/
    private void setShopBasicViewData() {
        IconAndTextGroupUtil.setTextView(getApplicationContext(), tvTitle, goodsDetail.getTitle(), goodsDetail.getUserType());
        tvBuyNum.setText(NumUtil.getNum(goodsDetail.getVolume()) + "人购买");
        tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getPayPrice()));
        tvOldPrice.setText("¥" + MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getZkFinalPrice()));
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        estimateMoney.setText("预估赚 ¥ " + goodsDetail.getEstimatedEarn());
    }

    /*设置banner*/
    private void setBannerDataView() {
        List<String> bannerList = new ArrayList<>();
        String smallImages = goodsDetail.getSmallImages();/*商品轮播图集合*/
        String pictUrl = goodsDetail.getPictUrl();/*商品主图*/
        if (TextUtils.isEmpty(smallImages)) {
            bannerList.add(pictUrl);
        } else {
            String[] bannerImages = smallImages.split(",");
            for (int i = 0; i < bannerImages.length; i++) {
                bannerList.add(bannerImages[i]);
            }
        }
        banner.setImageLoader(new RoundBannerLoader());
        banner.setImages(bannerList);
        banner.isAutoPlay(true);
        banner.setDelayTime(5000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                intent = new Intent(getApplicationContext(), GoodBannerActivity.class);
                intent.putStringArrayListExtra("bannerList", (ArrayList<String>) bannerList);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    /*商品详情相关推荐接口*/
    List<GoodRecommendBean.RecommendInfoBean> list;

    private void getGoodRecommendListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GOODRECOMMENDDATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("相关推荐", response.toString());
                        GoodRecommendBean goodRecommendBean = GsonUtil.GsonToBean(response.toString(), GoodRecommendBean.class);
                        if (goodRecommendBean.getErrno() == CommonApi.RESULTCODEOK) {
                            list = goodRecommendBean.getRecommendInfo();
                            if (list.size() > 0) {
                                xiangguantuijian.setVisibility(View.VISIBLE);
                                recyclerviewRecommend.setVisibility(View.VISIBLE);
                                GoodRecommendAdapter goodRecommendAdapter = new GoodRecommendAdapter(getApplicationContext(), list);
                                recyclerviewRecommend.setAdapter(goodRecommendAdapter);
                                goodRecommendAdapter.setOnClickListener(new OnItemClick() {
                                    @Override
                                    public void OnItemClickListener(View view, int position) {
                                        intent = new Intent(getApplicationContext(), ShopDetailActivity.class);
                                        intent.putExtra("itemId", list.get(position).getItemId());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                recyclerviewRecommend.setVisibility(View.GONE);
                                xiangguantuijian.setVisibility(View.GONE);
                            }
                        } else {
                            xiangguantuijian.setVisibility(View.GONE);
                            recyclerviewRecommend.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        xiangguantuijian.setVisibility(View.GONE);
                        recyclerviewRecommend.setVisibility(View.GONE);
                    }
                });
    }

    /*猜你喜欢接口*/
    private void getLikeData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.LIKEDATA + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        LikeBean likeBean = GsonUtil.GsonToBean(response.toString(), LikeBean.class);
                        if (likeBean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<LikeBean.LikeInfoBean> list = likeBean.getLikeInfo();
                            LikeAdapter likeAdapter = new LikeAdapter(getApplicationContext(), list);
                            recyclerviewLike.setAdapter(likeAdapter);
                            likeAdapter.setOnClickListener(new OnItemClick() {
                                @Override
                                public void OnItemClickListener(View view, int position) {
                                    intent = new Intent(getApplicationContext(), ShopDetailActivity.class);
                                    intent.putExtra("itemId", list.get(position).getItemId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }

    /*收藏接口*/
    private void collectData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GOODADDCOLLECT + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("收藏信息", response.toString());
                        BaseBean bean = GsonUtil.GsonToBean(response.toString(), BaseBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            ivCollectLogo.setImageResource(R.mipmap.liked);
                            tvCollectWenben.setText("已收藏");
                            tvCollectWenben.setTextColor(0xffF6223E);
                            isCollect = true;
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

    /*取消收藏*/
    private void cancelCollect() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
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
                            ivCollectLogo.setImageResource(R.drawable.nolike);
                            tvCollectWenben.setText("收藏");
                            tvCollectWenben.setTextColor(0xff585858);
                            isCollect = false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            /*获取唤醒淘宝链接*/
            loadingDialog = DialogUtil.createLoadingDialog(ShopDetailActivity.this, "加载中...");
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("itemId", itemId);
            String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
            MyApplication.getInstance().getMyOkHttp().post().tag(this)
                    .url(CommonApi.BASEURL + CommonApi.GETGOOD_PROMOTIONLINK + mapParam)
                    .enqueue(new JsonResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            super.onSuccess(statusCode, response);
                            Log.i("有淘宝链接", response.toString());
                            DialogUtil.closeDialog(loadingDialog, ShopDetailActivity.this);
                            PromotionlinkBean bean = GsonUtil.GsonToBean(response.toString(), PromotionlinkBean.class);
                            if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                                couponClickUrl = bean.getLink();
                                if (requestCode == 100) {
                                    intent = new Intent(getApplicationContext(), GoodShareActivity.class);
                                    intent.putExtra("itemId", itemId);/*商品id*/
                                    intent.putExtra("imagesList", goodsDetail.getSmallImages());
                                    intent.putExtra("title", goodsDetail.getTitle());
                                    intent.putExtra("payPrice", goodsDetail.getPayPrice());
                                    intent.putExtra("zkFinalPrice", goodsDetail.getZkFinalPrice());
                                    intent.putExtra("volume", goodsDetail.getVolume());
                                    intent.putExtra("estimatedEarn", goodsDetail.getEstimatedEarn());
                                    intent.putExtra("couponAmount", goodsDetail.getCouponAmount());
                                    intent.putExtra("userType", goodsDetail.getUserType());
                                    intent.putExtra("pictUrl", goodsDetail.getPictUrl());
                                    intent.putExtra("couponClickUrl", couponClickUrl);
                                    startActivity(intent);
                                } else if (requestCode == 200) {
                                    initAliPageData();
                                }
                            } else {
                                ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            DialogUtil.closeDialog(loadingDialog, ShopDetailActivity.this);
                            ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                        }
                    });
        }
    }

    /*请求店铺链接*/
    private void getShopLink() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("sellerId", goodsDetail.getSellerId());
        String commonParamSign = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GOOD_SHOP_LINK + commonParamSign)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        ShopLinkBean bean = GsonUtil.GsonToBean(response.toString(), ShopLinkBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {/*代表已备案*/
                            String link = bean.getLink();
                            HashMap<String, String> exParams = new HashMap<>();
                            exParams.put("isv_code", "appisvcode");
                            exParams.put("alibaba", "阿里巴巴");
                            // 以显示传入url的方式打开页面（第二个参数是套件名称 暂时传“”）
                            AlibcTrade.openByUrl(ShopDetailActivity.this, "", link, null,
                                    new WebViewClient(), new WebChromeClient(), alibcShowParams,
                                    null, exParams, new AlibcTradeCallback() {
                                        @Override
                                        public void onTradeSuccess(AlibcTradeResult tradeResult) {
                                        }

                                        @Override
                                        public void onFailure(int code, String msg) {
                                        }
                                    });
                        } else if (bean.getErrno() == 434) {/*用户淘宝未备案*/
                            taobaoBeiAn(600);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (alipay_gif_dialog != null) {
            alipay_gif_dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (alipay_gif_dialog != null) {
            alipay_gif_dialog.dismiss();
        }
    }

    // 声明一个订阅方法，用于接收事件
    @Subscribe
    public void onEvent(String msg) {
        switch (msg) {
            case CommonApi.COLLECT_CHANGE:
                getGoodCollectData();
                break;
        }
    }

}
