package com.lianliantao.yuetuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.ShopDeailBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.myutil.StatusBarUtils;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TianMaoCommmonActivity extends OriginalActivity {

    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvClose)
    TextView tvClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    @BindView(R.id.rlParent)
    RelativeLayout rlParent;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.yijianchaxuan)
    TextView yijianchaxuan;
    @BindView(R.id.ll_yijian_view)
    LinearLayout llYijianView;
    @BindView(R.id.ivOne)
    ImageView ivOne;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.reShare)
    RelativeLayout reShare;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.reBuy)
    RelativeLayout reBuy;
    @BindView(R.id.llAction)
    RelativeLayout llAction;
    private String title;
    private String url;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private String goods_id;
    private ShopDeailBean.GoodsDetailBean goodsDetail;
    private String couponClickUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.my_shop_webview_layout);
        ButterKnife.bind(this);
        ViewGroup.LayoutParams layoutParams = viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        tvTitle.setText(title);
        setWebview();
    }

    private void setWebview() {
        WebSettings settings = webview.getSettings();
        webview.setVerticalScrollBarEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
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
                Log.i("店铺地址", url);
                if (url.matches(".*(tmall.com|taobao.com|alimama.com|95095.com|taobao.hk|tmall.hk|alimama.hk|95095.hk).*")) {
                    if (url.matches(".*(item.htm|detail.htm|container.htm|item.html|detail.html|container.html).*")) {
                        //将String类型的地址转变为URI类型
                        Uri uri = Uri.parse(url);
                        /*获取商品id*/
                        goods_id = uri.getQueryParameter("id");
                        if (!TextUtils.isEmpty(uri.getQueryParameter("id")) || !TextUtils.isEmpty(uri.getQueryParameter("itemId"))) {
                            tvNotice.setVisibility(View.VISIBLE);
                            llYijianView.setVisibility(View.VISIBLE);
                        }
                        if (TextUtils.isEmpty(goods_id)) {
                            goods_id = uri.getQueryParameter("itemId");
                        }
                    } else {
                        tvNotice.setVisibility(View.GONE);
                        llYijianView.setVisibility(View.GONE);
                        llAction.setVisibility(View.GONE);
                    }
                }
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        });
        webview.loadUrl(url);
    }

    @OnClick({R.id.ivBack, R.id.tvClose, R.id.ivRight, R.id.ll_yijian_view, R.id.reShare, R.id.reBuy})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.tvClose:
                finish();
                break;
            case R.id.ivRight:
                if (webview != null) {
                    webview.reload();
                }
                break;
            case R.id.ll_yijian_view:/*一键查询优惠券  请求商品详情接口*/
                getGoodDetailData();
                break;
            case R.id.reShare:/*分享赚*/
                Intent intent = new Intent(getApplicationContext(), GoodShareActivity.class);
                intent.putExtra("itemId", goodsDetail.getItemId());/*商品id*/
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
                break;
            case R.id.reBuy:/*返佣购买按钮*/
                goToTaoBao();
                break;
        }
    }

    private void goToTaoBao() {
        alibcShowParams = new AlibcShowParams(OpenType.Native, true);
        AlibcBasePage page = new AlibcPage(couponClickUrl);
        HashMap<String, String> exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");
        AlibcTrade.show(TianMaoCommmonActivity.this, page, alibcShowParams, null, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                /*阿里百川进淘宝成功*/
            }

            @Override
            public void onFailure(int i, String s) {
                /*阿里百川进淘宝失败*/
            }
        });
    }

    private void getGoodDetailData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", goods_id);
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
                            llYijianView.setVisibility(View.GONE);
                            llAction.setVisibility(View.VISIBLE);
                            tvShare.setText("分享赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getEstimatedEarn()));
                        } else {
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                            llYijianView.setVisibility(View.VISIBLE);
                            llAction.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();
            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }
}
